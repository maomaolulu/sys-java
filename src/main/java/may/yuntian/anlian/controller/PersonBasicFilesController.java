package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.entity.PersonBasicFilesEntity;
import may.yuntian.anlian.service.PersonBasicFilesService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.modules.sys.entity.SysConlogEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysConlogService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import may.yuntian.modules.sys.service.SysUserService;

import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.AlRedisUntil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 技术人员基本档案管理
 * WEB请求处理层
 *
 * @author ZhangHao
 * @data 2021-06-04
 */
@RestController
@Api(tags = "技术人员基本档案")
@RequestMapping("anlian/personBasicFiles")
public class PersonBasicFilesController {

    @Autowired
    private PersonBasicFilesService personBasicFilesService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysConlogService sysConlogService;
    @Autowired
    private AlRedisUntil alRedisUntil;


    /**
     * 所有已建档用户列表不含中介
     */
    @GetMapping("/userList")
    @ApiOperation("分页查询已建档用户列表不含中介")
    @RequiresPermissions("sys:user:list")
    @AuthCode(url = "personShow", system = "sys")
    public R list(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
        // 登录人隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            params.put("subjection", "");
        } else {
            // 职能权限:分公司
            params.put("subjection", subjection);
        }
        //只显示公司成员
        params.put("type", 1);
        PageUtils page = personBasicFilesService.queryBookBuildingPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @ApiOperation("根据ID查询详情")
//	@RequiresPermissions("anlian:personBasicFiles:info")
    public R info(@PathVariable("id") Long id) {

        List<PersonBasicFilesEntity> list = personBasicFilesService.selectByUserIdList(id);
        SysUserEntity userEntity = sysUserService.getById(id);
//		//获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(id);
        userEntity.setRoleIdList(roleIdList);
        return R.ok().put("list", list).put("userEntity", userEntity);

    }

    //新增
    @PostMapping("/save")
    @SysLog("新增技术人员基本档案记录")
    @ApiOperation("新增技术人员基本档案记录")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody PersonBasicFilesEntity personBasicFilesEntity) {

        if (personBasicFilesEntity.getUserId() == 0) {
            return R.error("请补充完整用户信息，以便新增人员档案。");
        }
        if (StringUtils.isNotBlank(personBasicFilesEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personBasicFilesEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personBasicFilesEntity.getPath());
            }
        }

        personBasicFilesService.save(personBasicFilesEntity);
        return R.ok();
    }

    //批量新增
    @PostMapping("/saveBatch")
    @SysLog("批量新增技术人员基本档案记录")
    @ApiOperation("批量新增技术人员基本档案记录")
    @RequiresPermissions("sys:user:save")
    public R saveBatch(@RequestBody List<PersonBasicFilesEntity> personBasicFilesEntitys) {
        for (PersonBasicFilesEntity personBasicFilesEntity:personBasicFilesEntitys){
            if (StringUtils.isNotBlank(personBasicFilesEntity.getPath())){
                Object o = alRedisUntil.hget("anlian-java",personBasicFilesEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personBasicFilesEntity.getPath());
                }
            }
        }
        personBasicFilesService.saveBatch(personBasicFilesEntitys);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改技术人员基本档案记录")
    @ApiOperation("修改技术人员基本档案记录")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody PersonBasicFilesEntity personBasicFilesEntity) {
        PersonBasicFilesEntity oldPersonBasic = personBasicFilesService.getById(personBasicFilesEntity.getId());
        if (StringUtils.isNotBlank(oldPersonBasic.getPath())&& !oldPersonBasic.getPath().equals(personBasicFilesEntity.getPath())){
            MinioUtil.remove(oldPersonBasic.getPath());
        }
        if (StringUtils.isNotBlank(personBasicFilesEntity.getPath()) && StringUtils.isNotBlank(oldPersonBasic.getPath()) && !oldPersonBasic.getPath().equals(personBasicFilesEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personBasicFilesEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personBasicFilesEntity.getPath());
            }
        }
        personBasicFilesService.updateById(personBasicFilesEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除技术人员基本档案记录")
    @ApiOperation("删除技术人员基本档案记录")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] ids) {

        List<PersonBasicFilesEntity> list = personBasicFilesService.listByIds(Arrays.asList(ids));
        for (PersonBasicFilesEntity personBasicFilesEntity:list){
            if (StringUtils.isNotBlank(personBasicFilesEntity.getPath())){
                MinioUtil.remove(personBasicFilesEntity.getPath());
            }
        }
        personBasicFilesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 保存用户
     */
    @SysLog("新增用户")
    @PostMapping("/userSave")
    @ApiOperation("新增用户")
    @RequiresPermissions("sys:user:save")
    public R userSave(@RequestBody SysUserEntity user) {
        //ValidatorUtils.validateEntity(user, AddGroup.class);
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        user.setCreateUserId(sysUserEntity.getUserId());
        //System.out.println("控制层保存用户信息："+user.toString());
        sysUserService.save(user);


        //新增用户时 插入一条用户登录信息
        SysConlogEntity sysConlog = sysConlogService.getByUserId(user.getUserId());
        if (sysConlog == null) {
            SysConlogEntity sysConlogEntity = new SysConlogEntity();
            sysConlogEntity.setUserid(user.getUserId());
            sysConlogEntity.setMonthLoginTotal(0);
            sysConlogEntity.setWeekLoginTotal(0);
            sysConlogService.save(sysConlogEntity);
        }

        PersonBasicFilesEntity personBasicFilesEntity = new PersonBasicFilesEntity();
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("员工入职登记表");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;
                case 1:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("身份证复印件");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;
                case 2:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("学历学位证书扫描件");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;
                case 3:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("关于检验检测人员只在一个机构从业的承诺书");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;
                case 4:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("薪酬保密规定");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;
                case 5:
                    personBasicFilesEntity.setUserId(user.getUserId());
                    personBasicFilesEntity.setUserName(user.getUsername());
                    personBasicFilesEntity.setName("劳动合同");
                    personBasicFilesEntity.setCollectionDate(DateUtils.parseDate(user.getEntryTime()));
                    personBasicFilesService.save(personBasicFilesEntity);
                    break;

                default:
                    break;
            }
        }


        return R.ok();
    }


}

