package may.yuntian.modules.sys.controller;

import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.common.validator.Assert;
import may.yuntian.modules.sys.entity.SysConlogEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserRoleEntity;
import may.yuntian.modules.sys.form.PasswordForm;
import may.yuntian.modules.sys.service.SysConlogService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-01-09
 */
@RestController
@Api(tags="系统用户管理")
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysConlogService sysConlogService;


	/**
	 * 所有用户列表不含中介
	 */
//	@GetMapping("/list")
//	@ApiOperation("分页查询用户列表不含中介")
//	@RequiresPermissions("sys:user:list")
//	public R list(@RequestParam Map<String, Object> params){
//		params.put("type", 1);//只显示公司成员
//		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
//		if(sysUserEntity.getSubjection()!=null) {
//			switch (sysUserEntity.getSubjection()) {
//			case "嘉兴安联":
//				params.put("subjection", sysUserEntity.getSubjection());
//				break;
//			case "宁波安联":
//				params.put("subjection", sysUserEntity.getSubjection());
//				break;
//
//			default:
//				break;
//			}
//		}
//		PageUtils page = sysUserService.queryPage(params);
//
//		return R.ok().put("page", page);
//	}

    /**
     * 所有用户列表不含中介
     */
//    @GetMapping("/shuntList")
    @GetMapping("/list")
    @ApiOperation("分页查询用户列表不含中介")
    @RequiresPermissions("sys:user:list")
    @AuthCode(url = "user",system = "sys")
    public R shuntList(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo){
        params.put("type", 1);//只显示公司成员
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection", "");
        }else {
            SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            if(sysUserEntity.getSubjection()!=null) {
                params.put("subjection", sysUserEntity.getSubjection());
            }else {
                params.put("subjection", "无隶属公司");
            }
        }

        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 所有中介列表不含用户
     */
//    @GetMapping("/shuntMediationList")
    @GetMapping("/listMediation")
    @ApiOperation("所有中介列表不含用户")
    @RequiresPermissions("sys:user:list")
    @AuthCode(url = "intermediary",system = "sys")
    public R shuntMediationList(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo){
        params.put("type", 2);//只显示中介成员
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection", "");
        }else {
            SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            if(sysUserEntity.getSubjection()!=null) {
                params.put("subjection", sysUserEntity.getSubjection());
            }else {
                params.put("subjection", "无隶属公司");
            }
        }

        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }
	
	/**
	 * 所有部门用户列表不含中介
	 */
	@GetMapping("/deptList")
	@ApiOperation("分页查询用户列表不含中介")
	@RequiresPermissions("sys:user:deptList")
	public R deptList(@RequestParam Map<String, Object> params){
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		params.put("type", 1);//只显示公司成员
		params.put("status", 2);
		params.put("deptId", String.valueOf(sysUserEntity.getDeptId()));
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	
	/**
	 * 所有中介列表
	 */
//	@GetMapping("/listMediation")
//	@ApiOperation("分页查询中介列表")
//	@RequiresPermissions("sys:user:list")
//	public R listMediation(@RequestParam Map<String, Object> params){
//		params.put("type", 2);//只显示中介
//		PageUtils page = sysUserService.queryPage(params);
//
//		return R.ok().put("page", page);
//	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@ApiOperation("修改登录用户密码")
	@PostMapping("/password")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	
	/**
	 * 不定时修改登录用户密码 当changeNumber为0时
	 */
//	@SysLog("修改密码")
	@ApiOperation("修改登录用户密码")
	@PostMapping("/updatePassword")
	public R updatePassword(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		String email = form.getUsername();
		SysUserEntity userEntity = sysUserService.queryByEmail(email);
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), userEntity.getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), userEntity.getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updateLoginPassword(email, password, newPassword,userEntity.getUserId());
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@ApiOperation("用户信息")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("新增用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		//ValidatorUtils.validateEntity(user, AddGroup.class);
		user.setCreateUserId(getUserId());
        user.setUsername(user.getUsername().trim());
		//System.out.println("控制层保存用户信息："+user.toString());
		sysUserService.save(user);
		
		//新增用户时 插入一条用户登录信息
		SysConlogEntity sysConlog = sysConlogService.getByUserId(user.getUserId());
		if(sysConlog==null) {
			SysConlogEntity sysConlogEntity = new SysConlogEntity();
			sysConlogEntity.setUserid(user.getUserId());
			sysConlogEntity.setMonthLoginTotal(0);
			sysConlogEntity.setWeekLoginTotal(0);
			sysConlogService.save(sysConlogEntity);
		}
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		//ValidatorUtils.validateEntity(user, UpdateGroup.class);
        user.setUsername(user.getUsername().trim());
		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户状态
	 * status 状态  0：禁用   1：正常
	 */
	@SysLog("修改用户状态")
	@ApiOperation("修改用户状态(status状态  0：禁用   1：正常)")
	@PostMapping("/updateStatus")
	@RequiresPermissions("sys:user:update")
	public R updateStatus(@RequestParam Long userId,@RequestParam Integer status){
		sysUserService.updateStatus(userId,status);
		
		return R.ok();
	}
	
	
	/**
	 * 是否建立档案
	 * isBookbuilding 是否建立档案 (0默认，1建立，2不建立)
	 */
	@SysLog("是否建立档案")
	@ApiOperation("是否建立档案 (0默认，1建立，2不建立)")
	@PostMapping("/updateIsBookbuilding")
	@RequiresPermissions("sys:update:isBookbuilding")
	public R updateIsBookbuilding(@RequestParam Long userId,@RequestParam Integer isBookbuilding){
		sysUserService.updateIsBookbuilding(userId,isBookbuilding);

		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		//用户删除同时删除用户登录信息
		for(Long userid:userIds) {
			sysConlogService.deleteByUserId(userid);
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
	/**
	 * 显示所有用户列表
	 * 不分页，用于下列选择用户
	 */
	@GetMapping("/listAll")
	@ApiOperation("显示所有用户列表(不分页,用于下列选择用户)")
	//@RequiresPermissions("sys:user:list")
	public R listAll(){
		List<SysUserEntity> list = sysUserService.listAll();

		return R.ok().put("list", list);
	}
	
	/**
	 * 显示所有用户列表
	 * 不分页，用于下列选择用户
	 * 不包含中介
	 */
	@GetMapping("/ListAllByType")
	@ApiOperation("显示所有用户列表(不分页,用于下列选择用户,不含中介)")
	//@RequiresPermissions("sys:user:list")
	public R ListAllByType(){
		List<SysUserEntity> list = sysUserService.ListAllByType();

		return R.ok().put("list", list);
	}
	
	/**
	 * 根据部门显示用户信息列表
	 * 不分页，用于下列选择用户
	 */
	@GetMapping("/listByDept")
	@ApiOperation("根据部门显示用户信息列表(不分页,用于下列选择用户)")
//	@RequiresPermissions("sys:user:list")
	public R listByDept(long deptId){
		List<SysUserEntity> list = sysUserService.listByDept(deptId);
		
		return R.ok().put("list", list);
	}
	
	/**
	 * 保存用户与角色的关系
	 */
	@SysLog("新增用户与角色的关系")
	@PostMapping("/saveUserRole")
	@RequiresPermissions("sys:user:save")
	@ApiOperation("新增用户与角色的关系")
	public R saveUserRole(@RequestBody SysUserRoleEntity entity){
		//根据角色ID与用户ID获取对应关系信息
		SysUserRoleEntity oldEntity = sysUserRoleService.queryByRoleIdAndUserId(entity);
		if(oldEntity == null) {
			sysUserRoleService.save(entity);
		}else {
			return R.error("此角色与用户对应关系已存在，不需要重复保存！");
		}
		
		return R.ok();
	}
	
	/**
	 * 删除用户与角色的关系
	 */
	@SysLog("删除用户与角色的关系")
	@PostMapping("/deleteUserRole")
	@RequiresPermissions("sys:user:save")
	@ApiOperation("删除用户与角色的关系")
	public R deleteUserRole(@RequestBody SysUserRoleEntity entity){
		//根据角色ID与用户ID获取对应关系信息
		SysUserRoleEntity oldEntity = sysUserRoleService.queryByRoleIdAndUserId(entity);
		if(oldEntity != null) {
			sysUserRoleService.removeById(oldEntity.getId());
		}
		
		return R.ok();
	}
}
