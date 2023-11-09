package may.yuntian.modules.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.DateUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.EmailVo;
import may.yuntian.modules.sys.entity.SysEmailVerifyEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.EmailVoService;
import may.yuntian.modules.sys.service.SysEmailVerifyService;
import may.yuntian.modules.sys.service.SysUserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 发送邮箱所需字段
 * @author LiXin
 *
 */
@RestController
@Api(tags="发送邮箱")
@RequestMapping("anlian/mail")
public class EmailVoController {

	@Autowired
	private EmailVoService emailVoService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysEmailVerifyService sysEmailVerifyService;
	
	@Value("${spring.mail.properties.anlian}")
	private String anlian;
	
	 @PostMapping("/simple")
	 @ApiOperation("发送邮箱")
     public R sendSimpleMail(@RequestBody EmailVo emailVo){
		 emailVoService.sendSimpleMail(emailVo);
		 
		 return R.ok();
     }
	 
	 
	 @PostMapping("/forgetPassword")
	 @ApiOperation("忘记密码发送邮箱")
     public R forgetPassword(@RequestBody EmailVo emailVo, HttpServletRequest httpRequest) {
		 String email = "";
         String host;
         if (StringUtils.isBlank(emailVo.getHost())){
		     host = anlian;
         }else {
             host = emailVo.getHost();
         }

		 if(emailVo.getEmails()!=null&&emailVo.getEmails().length>0) {
			List<String> emailList = Arrays.asList(emailVo.getEmails());
			email = emailList.get(0);
			SysUserEntity user = sysUserService.queryByEmail(email);
			if(user==null) {
				return R.error("此用户不存在");
			}
		 }
		 emailVo.setSubject("密码重置");
		 emailVo.setContent("您好:\r\n" + 
		 		"\r\n您的账号"+email+"申请重置密码\r\n" + 
		 		"\r\n如需重置密码请在24h内点击下面的链接\r\n" + 
		 		anlian+"/replacement?uuid="+emailVo.getUuid()+"&host="+host+"\r\n" +
		 		"\r\n如您没有请求重置密码，您可以安全的忽略此电子邮件\r\n" + 
		 		"\r\n安联检测信息化运营平台");
		 
		 boolean a = emailVoService.sendMailByUpdatePassword(emailVo);

		 if (!a){
		     return R.error("邮件发送失败!");
         }

         SysEmailVerifyEntity sysEmailVerifyEntity = new SysEmailVerifyEntity();
         sysEmailVerifyEntity.setUuid(emailVo.getUuid());
         sysEmailVerifyEntity.setEmail(email);
         sysEmailVerifyEntity.setExpireTime(DateUtils.addDateHours(new Date(), 24));
         sysEmailVerifyService.save(sysEmailVerifyEntity);

         return R.ok().put("msg", "请前往您的"+email+"邮箱重置密码！");
     }
	 
	 @PostMapping("/resetPassWord")
	 @ApiOperation("重置密码验证")
	 public R resetPassWord(@RequestBody EmailVo emailVo) {
		 SysEmailVerifyEntity sysEmailVerifyEntity = sysEmailVerifyService.getOne(new QueryWrapper<SysEmailVerifyEntity>().eq("uuid", emailVo.getUuid()));
	        if(sysEmailVerifyEntity == null){
	            return R.error().put("code", 480).put("msg", "数据错误！请重新重置密码！");
	        }
	        String email = sysEmailVerifyEntity.getEmail();
	        //删除验证码
	        sysEmailVerifyService.removeById(emailVo.getUuid());

	        if(sysEmailVerifyEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
	        	return R.error().put("code", 481).put("msg", "此链接已失效！请重新重置密码！");
	        }
	        SysUserEntity user = sysUserService.queryByEmail(email);
	        //sha256加密
			String password = new Sha256Hash(emailVo.getPassword(), user.getSalt()).toHex();
			user.setPassword(password);
			if(user.getStatus()==0) {
				user.setStatus(1);
				user.setDefeats(0);
			}
			if (user.getChangeNumber()==0){
			    user.setChangeNumber(1);
            }
			sysUserService.updateById(user);
	        
		 return R.ok();
	 }
	 

}

