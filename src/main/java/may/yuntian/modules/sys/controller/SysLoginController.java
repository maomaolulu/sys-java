package may.yuntian.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.HttpContextUtils;
import may.yuntian.common.utils.IPUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysLogEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.form.DynamicCodeFrom;
import may.yuntian.modules.sys.form.SysLoginForm;
import may.yuntian.modules.sys.oauth2.TokenGenerator;
import may.yuntian.modules.sys.service.*;
import may.yuntian.untils.AlRedisUntil;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Api(tags = "用户登录相关")
@RestController
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private AlRedisUntil alRedisUntil;

    @Autowired
    private SysDictService sysDictService;//数据字典


    /**
     * 验证码
     */
    @ApiOperation("验证码")
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
//		BufferedImage image = sysCaptchaService.getCaptcha(uuid);
        //获取数字验证码测试
        BufferedImage image = sysCaptchaService.getMathCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 生成动态码
     */
    @ApiOperation("生成动态码")
    @GetMapping("/sys/createDynamicCode")
    public R createDynamicCode(String prefix, HttpServletRequest httpRequest) throws IOException {
        String number = TokenGenerator.generateValue();
        String dynamicCode = number.substring(number.length() - 6, number.length());
        String token = httpRequest.getHeader("token");
        alRedisUntil.set(dynamicCode, prefix + "_" + token, Long.valueOf("120"));
        return R.ok().put("dynamicCode", dynamicCode);
    }

    /**
     * 动态码登录
     */
    @ApiOperation("动态码登录")
    @PostMapping("/sys/dynamicCodeLogin")
    public Map<String, Object> dynamicCodeLogin(@RequestBody DynamicCodeFrom params) throws IOException {

        String dynamicCode = params.getDynamicCode();
        String type = params.getType();
        if (alRedisUntil.get(dynamicCode) == null) {
            return R.error("动态码已失效,请重新生成后再登录!");
        }
        String value = (String) alRedisUntil.get(dynamicCode);
        String[] values = value.split("_");
        if (type.contains(values[0])) {
            String token = values[values.length - 1];
            if (alRedisUntil.get("token_" + token) == null) {
                return R.error(401, "登录已过期,无法用动态码登录!");
            } else {
                Map retMap = new HashMap();
                retMap = alRedisUntil.jsonToMap((String) alRedisUntil.get("token_" + token));

                String newString = alRedisUntil.toJson(retMap.get("user_info"));
                SysUserEntity sysUserEntity = alRedisUntil.fromJson(newString, SysUserEntity.class);

                SysUserEntity user = sysUserService.getById(sysUserEntity.getUserId());

                //获取request
                HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
                String clientIP = IPUtils.getIpAddr(request);//获取客户端的IP地址
                //系统日志信息
                SysLogEntity sysLog = new SysLogEntity();
                sysLog.setUserId(user.getUserId());
                sysLog.setUsername(user.getEmail());
                sysLog.setCreateDate(new Date());
                sysLog.setIp(clientIP);
                sysLog.setTime(0L);
                sysLog.setOperation("登录成功");
                sysLogService.save(sysLog);

                //生成token，并保存到数据库
                R r = sysUserTokenService.createToken(user.getUserId(), type);
                if (r.get("code").equals("21")) {
                    return r;
                }

                if (user != null) {
                    SysUserEntity retUser = new SysUserEntity();
                    retUser.setUserId(user.getUserId());
                    retUser.setUsername(user.getUsername());
                    retUser.setJobNum(user.getJobNum());
                    retUser.setMobile(user.getMobile());
                    retUser.setEmail(user.getEmail());
                    Long deptId = user.getDeptId();
                    retUser.setDeptId(deptId);
                    if (deptId != null) {
                        SysDeptEntity dept = sysDeptService.getById(deptId);
                        if (dept != null) {
                            retUser.setDeptName(dept.getName());
                        }
                    }
                    r.put("sysUser", retUser);
                }
                return r;
            }
        } else {
            return R.error("动态码与系统不匹配");
        }
    }


    /**
     * 登录验证用户是否存在
     */
    @ApiOperation("验证用户是否存在")
    @GetMapping("/sys/validation")
    public R validation(String username) throws IOException {
        SysUserEntity user = sysUserService.queryByEmail(username);
        if (user == null) {
            return R.error("此用户不存在");
        }
        return R.ok();
    }


//    /**
//     * 登录
//     */
//    @ApiOperation("登录验证")
//    @PostMapping("/sys/login")
//    public Map<String, Object> login(@RequestBody SysLoginForm form) throws IOException {
////		if(!form.getUsername().equals("zly@anliantest.com")) {
//        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
//        if(!captcha){
//            return R.error("验证码不正确");
//        }
////		}
//
//		//用户信息
//		//SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
//		//2021-2-3根据用户邮箱登录
//		SysUserEntity user = sysUserService.queryByEmail(form.getUsername());
//		//测试阶段通行密码，特殊需求
//		SysDictEntity  sysDict = sysDictService.queryByTypeAndCode("password","1");//type,code
//
//		//获取request
//		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
//		String clientIP = IPUtils.getIpAddr(request);//获取客户端的IP地址
//		//系统日志信息
//		SysLogEntity sysLog = new SysLogEntity();
//		sysLog.setUserId(user.getUserId());
//		sysLog.setUsername(form.getUsername());
//		sysLog.setCreateDate(new Date());
//		sysLog.setIp(clientIP);
//		sysLog.setTime(0L);
//
//		if(user != null && sysDict!=null && sysDict.getValue().equals(form.getPassword())) {//测试阶段通行密码，特殊需求
//			sysLog.setOperation("测试阶段通行证登录成功！");
//
//        } else {
//            //账号不存在、密码错误
//            if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
//
//                sysLog.setOperation("登录失败");
//                sysLog.setMethod("账号或密码错误");
//                String params = JSONObject.toJSONString(form);
//                sysLog.setParams(params);
//                sysLogService.save(sysLog);//保存系统日志
//
//                if (user != null) {
//                    Integer maxDefeats = 5;
//                    String configDefeats = sysConfigService.getValue("SystemLoginDefeats");//从配置表中读取用户登录允许连续失败最大次数
//                    if (configDefeats != null) {
//                        maxDefeats = Integer.valueOf(configDefeats);
//                    }
//
//                    Integer defeats = user.getDefeats() + 1;//登录失败次数加1，达到5次将锁定
//                    sysUserService.updateDefeats(user.getUserId(), defeats, maxDefeats);
//                    //账号锁定提醒
//                    if (user.getStatus() == 0 || user.getStatus() == 2) {
//                        return R.error("账号已被锁定,请联系管理员");
//                    }
//
//                    if (defeats >= 5) {
//
//                        return R.error("您的账号密码输入错误超过五次，账号已被锁定！");
//                    }
//
//                }
//
//                return R.error("账号或密码不正确");
//            }
//
//            //账号锁定
//            if (user.getStatus() == 0 || user.getStatus() == 2) {
//                return R.error("账号已被锁定,请联系管理员");
//            }
//
//            if (user.getIp() != null && user.getIp().length() > 6 && !"0:0:0:0:0:0:0:1".equals(clientIP)) {
//
//                if (!user.getIp().equals(clientIP)) {//判断用户绑定的IP与客户端的IP地址是否匹配
//                    sysLog.setOperation("登录失败");
//                    sysLog.setMethod("通过非绑定IP登录");
//                    sysLog.setParams("已绑定IP[" + user.getIp() + "]");
//                    sysLogService.save(sysLog);//保存系统日志
//
//                    return R.error("账号已设置指定计算机[" + user.getIp() + "]登录,目前为[" + clientIP + "],请联系管理员");
//                }
//            }
//
//            //当用户登录成功时，则将失败次数置为0
//            if (user.getDefeats() > 0) {
//                sysUserService.updateDefeats(user.getUserId(), 0);
//            }
//
//            sysLog.setOperation("登录成功");
//        }
//        sysLogService.save(sysLog);//保存系统日志
//
//        //生成token，并保存到数据库
//        R r = sysUserTokenService.createToken(user.getUserId(), form.getType());
//        if (r.get("code").equals("21")) {
//            return r;
//        }
//        //将用户信息返回给前端
//        if (user != null) {
//            SysUserEntity retUser = new SysUserEntity();
//            retUser.setUserId(user.getUserId());
//            retUser.setUsername(user.getUsername());
//            retUser.setJobNum(user.getJobNum());
//            retUser.setMobile(user.getMobile());
//            retUser.setEmail(user.getEmail());
//            Long deptId = user.getDeptId();
//            retUser.setDeptId(deptId);
//            if (deptId != null) {
//                SysDeptEntity dept = sysDeptService.getById(deptId);
//                if (dept != null) {
//                    retUser.setDeptName(dept.getName());
//                }
//            }
//            if (user != null && user.getChangeNumber() == 0) {
//                r.put("code", 20);
//            }
//            r.put("sysUser", retUser);
//        }
//        return r;
//    }

    /**
     * xin登录
     */
    @ApiOperation("登录验证")
    @PostMapping("/sys/login")
    public Map<String, Object> loginNew(@RequestBody SysLoginForm form) throws IOException {
//		if(!form.getUsername().equals("zly@anliantest.com")) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}
//		}

        //用户信息
        //SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
        //2021-2-3根据用户邮箱登录
        SysUserEntity user = sysUserService.queryByEmail(form.getUsername());
        //测试阶段通行密码，特殊需求 //type,code
        SysDictEntity sysDict = sysDictService.queryByTypeAndCode("password", "1");

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //获取客户端的IP地址
        String clientIP = IPUtils.getIpAddr(request);
        //系统日志信息
        SysLogEntity sysLog = new SysLogEntity();
        sysLog.setUserId(user.getUserId());
        sysLog.setUsername(form.getUsername());
        sysLog.setCreateDate(new Date());
        sysLog.setIp(clientIP);
        sysLog.setTime(0L);
        //测试阶段通行密码，特殊需求
        if (user != null && sysDict != null && sysDict.getValue().equals(form.getPassword())) {
            sysLog.setOperation("测试阶段通行证登录成功！");

        } else {
            //账号不存在、密码错误
            if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {

                sysLog.setOperation("登录失败");
                sysLog.setMethod("账号或密码错误");
                String params = JSONObject.toJSONString(form);
                sysLog.setParams(params);
                //保存系统日志
                sysLogService.save(sysLog);

                if (user != null) {
                    Integer maxDefeats = 5;
                    //从配置表中读取用户登录允许连续失败最大次数
                    String configDefeats = sysConfigService.getValue("SystemLoginDefeats");
                    if (configDefeats != null) {
                        maxDefeats = Integer.valueOf(configDefeats);
                    }
                    //登录失败次数加1，达到5次将锁定
                    Integer defeats = user.getDefeats() + 1;
                    sysUserService.updateDefeats(user.getUserId(), defeats, maxDefeats);
                    //账号锁定提醒
                    if (user.getStatus() == 0 || user.getStatus() == 2) {
                        return R.error("账号已被锁定,请联系管理员");
                    }

                    if (defeats >= 5) {

                        return R.error("您的账号密码输入错误超过五次，账号已被锁定！");
                    }

                }

                return R.error("账号或密码不正确");
            }

            //账号锁定
            if (user.getStatus() == 0 || user.getStatus() == 2) {
                return R.error("账号已被锁定,请联系管理员");
            }

            if (user.getIp() != null && user.getIp().length() > 6 && !"0:0:0:0:0:0:0:1".equals(clientIP)) {

                //判断用户绑定的IP与客户端的IP地址是否匹配
                if (!user.getIp().equals(clientIP)) {
                    sysLog.setOperation("登录失败");
                    sysLog.setMethod("通过非绑定IP登录");
                    sysLog.setParams("已绑定IP[" + user.getIp() + "]");
                    //保存系统日志
                    sysLogService.save(sysLog);

                    return R.error("账号已设置指定计算机[" + user.getIp() + "]登录,目前为[" + clientIP + "],请联系管理员");
                }
            }

            //当用户登录成功时，则将失败次数置为0
            if (user.getDefeats() > 0) {
                sysUserService.updateDefeats(user.getUserId(), 0);
            }

            sysLog.setOperation("登录成功");
        }
        //保存系统日志
        sysLogService.save(sysLog);

        //xin生成token，并保存到数据库
        R r = sysUserTokenService.newCreateToken(user.getUserId(), form.getType());
        if (r.get("code").equals("21")) {
            return r;
        }
        //将用户信息返回给前端
        if (user != null) {
            SysUserEntity retUser = new SysUserEntity();
            retUser.setUserId(user.getUserId());
            retUser.setUsername(user.getUsername());
            retUser.setJobNum(user.getJobNum());
            retUser.setMobile(user.getMobile());
            retUser.setEmail(user.getEmail());
            Long deptId = user.getDeptId();
            retUser.setSubjection(user.getSubjection());
            retUser.setDeptId(deptId);
            if (deptId != null) {
                SysDeptEntity dept = sysDeptService.getById(deptId);
                if (dept != null) {
                    retUser.setDeptName(dept.getName());
                }
            }
            if (user != null && user.getChangeNumber() == 0) {
                r.put("code", 20);
            }
            r.put("sysUser", retUser);
        }
        return r;
    }
//	/**
//	 * 免密登录
//	 */
//	@ApiOperation("免密登录验证")
//	@PostMapping("/sys/noPasswordLogin")
//	public Map<String, Object> noPasswordLogin(@RequestParam Map<String, Object> params){
//		String userIdStr = (String)params.get("userId");
//		Long userId = Long.valueOf(userIdStr);
//		String email = (String)params.get("email");
//		String username = (String)params.get("username");
//		SysUserEntity user = sysUserService.getSysUserEntity(userId, email, username);
//
//
//		//获取request
//		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
//		String clientIP = IPUtils.getIpAddr(request);//获取客户端的IP地址
//		//系统日志信息
//		SysLogEntity sysLog = newCommission SysLogEntity();
//		sysLog.setUsername(username);
//		sysLog.setCreateDate(newCommission Date());
//		sysLog.setIp(clientIP);
//		sysLog.setTime(0L);
//
//
//		//账号不存在、密码错误
//		if(user == null) {
//			sysLog.setOperation("登录失败");
//			sysLog.setMethod("用户不存在");
//			sysLogService.save(sysLog);//保存系统日志
//			return R.error(401,"用户不存在");
//		}
//		if(user != null) {
//			Integer maxDefeats = 5;
//			String  configDefeats = sysConfigService.getValue("SystemLoginDefeats"); //从配置表中读取用户登录允许连续失败最大次数
//			if(configDefeats != null) {
//				maxDefeats = Integer.valueOf(configDefeats);
//			}
//			Integer defeats =  user.getDefeats() + 1;//登录失败次数加1，达到5次将锁定
//			sysUserService.updateDefeats(user.getUserId(), defeats,maxDefeats);
//			//账号锁定提醒
//			if(user.getStatus() == 0 || user.getStatus() == 2){
//				return R.error("账号已被锁定,请联系管理员");
//			}
//			if(defeats>=5) {
//				return R.error("您的账号之前密码输入错误超过五次，账号已被锁定！");
//			}
//		}
//
//					//账号锁定
//					if(user.getStatus() == 0 || user.getStatus() == 2){
//						return R.error("账号已被锁定,请联系管理员");
//					}
//
//					if(user.getIp()!=null && user.getIp().length()>6 && !"0:0:0:0:0:0:0:1".equals(clientIP) ) {
//
//						if(!user.getIp().equals(clientIP)) {//判断用户绑定的IP与客户端的IP地址是否匹配
//							sysLog.setOperation("登录失败");
//							sysLog.setMethod("通过非绑定IP登录");
//							sysLog.setParams("已绑定IP["+user.getIp()+"]");
//							sysLogService.save(sysLog);//保存系统日志
//
//							return R.error("账号已设置指定计算机["+user.getIp()+"]登录,目前为["+clientIP+"],请联系管理员");
//						}
//					}
//
//					//当用户登录成功时，则将失败次数置为0
//					if(user.getDefeats()>0) {
//						sysUserService.updateDefeats(user.getUserId(), 0);
//					}
//
//				sysLog.setOperation("登录成功");
//
//				sysLogService.save(sysLog);//保存系统日志
//
//				//生成token，并保存到数据库
//				R r = sysUserTokenService.createToken(user.getUserId());
//
//				//将用户信息返回给前端
//				if(user != null) {
//					SysUserEntity retUser = newCommission SysUserEntity();
//					retUser.setUserId(user.getUserId());
//					retUser.setUsername(user.getUsername());
//					retUser.setJobNum(user.getJobNum());
//					retUser.setMobile(user.getMobile());
//					retUser.setEmail(user.getEmail());
//					Long deptId = user.getDeptId();
//					retUser.setDeptId(deptId);
//					if(deptId != null) {
//						SysDeptEntity dept = sysDeptService.getById(deptId);
//						if(dept != null) {
//							retUser.setDeptName(dept.getName());
//						}
//					}
//					if(user!=null&&user.getChangeNumber()==0) {
//						r.put("code", 20);
//					}
//					r.put("sysUser", retUser);
//				}
//
//
//		return r;
//	}


    /**
     * 登录 正常登录备份
     */
    @PostMapping("/sys/login2")
    public Map<String, Object> login2(@RequestBody SysLoginForm form) throws IOException {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error("验证码不正确");
        }

        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String clientIP = IPUtils.getIpAddr(request);//获取客户端的IP地址
        //系统日志信息
        SysLogEntity sysLog = new SysLogEntity();
        sysLog.setUsername(form.getUsername());
        sysLog.setCreateDate(new Date());
        sysLog.setIp(clientIP);
        sysLog.setTime(0L);

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {

            sysLog.setOperation("登录失败");
            sysLog.setMethod("账号或密码错误");
            sysLogService.save(sysLog);//保存系统日志

            if (user != null) {
                Integer maxDefeats = 5;
                String configDefeats = sysConfigService.getValue("SystemLoginDefeats");//从配置表中读取用户登录允许连续失败最大次数
                if (configDefeats != null) {
                    maxDefeats = Integer.valueOf(configDefeats);
                }

                Integer defeats = user.getDefeats() + 1;//登录失败次数加1，达到5次将锁定
                sysUserService.updateDefeats(user.getUserId(), defeats, maxDefeats);

                //账号锁定提醒
                if (user.getStatus() == 0) {
                    return R.error("账号已被锁定,请联系管理员");
                }

                if (defeats >= 3) {

                    return R.error("密码错误,你已连续登录失败" + defeats + "次，" + maxDefeats + "次将锁定！");
                }

            }

            return R.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }

        if (user.getIp() != null && user.getIp().length() > 6 && !"0:0:0:0:0:0:0:1".equals(clientIP)) {

            if (!user.getIp().equals(clientIP)) {//判断用户绑定的IP与客户端的IP地址是否匹配
                sysLog.setOperation("登录失败");
                sysLog.setMethod("通过非绑定IP登录");
                sysLog.setParams("已绑定IP[" + user.getIp() + "]");
                sysLogService.save(sysLog);//保存系统日志

                return R.error("账号已设置指定计算机[" + user.getIp() + "]登录,目前为[" + clientIP + "],请联系管理员");
            }
        }

        //当用户登录成功时，则将失败次数置为0
        if (user.getDefeats() > 0) {
            sysUserService.updateDefeats(user.getUserId(), 0);
        }

        sysLog.setOperation("登录成功");
        sysLogService.save(sysLog);//保存系统日志


        //生成token，并保存到数据库
        R r = sysUserTokenService.createToken(user.getUserId(), form.getType());

        //将用户信息返回给前端
        if (user != null) {
            SysUserEntity retUser = new SysUserEntity();
            retUser.setUserId(user.getUserId());
            retUser.setUsername(user.getUsername());
            retUser.setJobNum(user.getJobNum());
            retUser.setMobile(user.getMobile());
            retUser.setEmail(user.getEmail());
            retUser.setSubjection(user.getSubjection());
            Long deptId = user.getDeptId();
            retUser.setDeptId(deptId);
            if (deptId != null) {
                SysDeptEntity dept = sysDeptService.getById(deptId);
                if (dept != null) {
                    retUser.setDeptName(dept.getName());
                }
            }

            r.put("sysUser", retUser);
        }
        if (user != null && user.getChangeNumber() == 0) {
            r.put("code", 20);
        }
        return r;
    }


//    /**
//     * 退出
//     */
//    @SysLog("用户退出系统")
//    @PostMapping("/sys/logout")
//    public R logout(@RequestBody DynamicCodeFrom params) {
//        String type = params.getType();
//        sysUserTokenService.logout(getUserId(), type);
//        return R.ok();
//    }

    /**
     * xin退出
     */
    @SysLog("用户退出系统")
    @PostMapping("/sys/logout")
    public R logout(@RequestBody DynamicCodeFrom params) {
        String type = params.getType();
        sysUserTokenService.logoutNew(getUserId(), type);
        return R.ok();
    }

}
