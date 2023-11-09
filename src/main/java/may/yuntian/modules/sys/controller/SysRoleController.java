package may.yuntian.modules.sys.controller;

import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysRoleEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysRoleMenuService;
import may.yuntian.modules.sys.service.SysRoleService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@RestController
@Api(tags="角色管理")
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;

	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@ApiOperation("显示角色信息列表")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
//		if(getUserId() != Constant.SUPER_ADMIN){
//			params.put("createUserId", getUserId());
//		}

		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		if(sysUserEntity.getSubjection()!=null) {
			switch (sysUserEntity.getSubjection()) {
			case "嘉兴安联":
				params.put("subordinate", 2);
				break;
			case "宁波安联":
				params.put("subordinate", 3);
				break;

			default:
				break;
			}
		}
		
		PageUtils page = sysRoleService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 角色列表
	 */
	@GetMapping("/getList")
	@ApiOperation("显示角色信息列表")
//	@RequiresPermissions("sys:role:list")
	public R getList(){
		List<SysRoleEntity> roleList = sysRoleService.getList();
		return R.ok().put("roleList", roleList);
	}
	
	/**
	 * 角色列表
	 */
	@GetMapping("/select")
	@ApiOperation("显示自己所拥有的角色列表,用于选择角色授权")
	@RequiresPermissions("sys:role:select")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		
		//如果不是超级管理员，则只查询自己所拥有的角色列表
//		if(getUserId() != Constant.SUPER_ADMIN){
//			map.put("create_user_id", getUserId());
//		}
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		if(sysUserEntity.getSubjection()!=null) {
			switch (sysUserEntity.getSubjection()) {
			case "嘉兴安联":
				map.put("subordinate", 2);
				break;
			case "宁波安联":
				map.put("subordinate", 3);
				break;

			default:
				break;
			}
		}
		List<SysRoleEntity> list = sysRoleService.listByMap(map);
		
		return R.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@GetMapping("/info/{roleId}")
	@ApiOperation("根据角色ID显示角色详细权限信息")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);
		
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		
		//查询角色对应的部门
		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
		role.setDeptIdList(deptIdList);
		
		//查询角色对应的项目类型权限
		List<Long> projectTypeList = sysRoleDeptService.queryProjectTypeList(new Long[]{roleId});
		role.setProjectTypeList(projectTypeList);
		
		//查询角色对应的项目隶属权限
		List<Long> orderList = sysRoleDeptService.queryOrderList(new Long[]{roleId});
		role.setOrderList(orderList);
		
		//查询角色对应的业务来源权限
		List<Long> sourceList = sysRoleDeptService.querySourceList(new Long[]{roleId});
		role.setSourceList(sourceList);
		
		return R.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@ApiOperation("保存角色信息")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role){
		//ValidatorUtils.validateEntity(role);
		//System.out.println("控制层保存角色信息："+role.toString());
		role.setCreateUserId(getUserId());
		sysRoleService.save(role);
		
		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@PostMapping("/update")
	@ApiOperation("修改角色信息")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role){
		//ValidatorUtils.validateEntity(role);
		
		role.setCreateUserId(getUserId());
		sysRoleService.update(role);
		
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@ApiOperation("删除角色信息")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		
		return R.ok();
	}
}
