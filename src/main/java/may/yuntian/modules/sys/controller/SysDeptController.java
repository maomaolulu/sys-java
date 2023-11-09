package may.yuntian.modules.sys.controller;

import may.yuntian.common.utils.R;
import may.yuntian.common.utils.Constant;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDeptService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 部门管理
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@RestController
@Api(tags="部门管理")
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiOperation("显示全部部门信息列表")
	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		return deptList;
	}
	
	
	/**
	 * 根据隶属公司区分权限列表
	 */
	@GetMapping("/authList")
	@ApiOperation("根据隶属公司区分权限列表")
//	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> authList(){
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
//		List<SysDeptEntity> deptList = newCommission ArrayList<SysDeptEntity>();
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
		if(sysUserEntity.getSubjection()!=null) {
			switch (sysUserEntity.getSubjection()) {
			case "嘉兴安联":
				Long deptId = sysDeptService.getDeptIdByDeptName(sysUserEntity.getSubjection());
				deptList = sysDeptService.querySubList(deptId);
				break;
			case "宁波安联":
				Long deptmentId = sysDeptService.getDeptIdByDeptName(sysUserEntity.getSubjection());
				deptList = sysDeptService.querySubList(deptmentId);
				break;

			default:
				break;
			}
		}

		return deptList;
	}

	/**
	 * 选择部门(添加、修改菜单)
	 */
	@GetMapping("/select")
	@ApiOperation("选择部门(添加、修改菜单)")
	@RequiresPermissions("sys:dept:select")
	public R select(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		//添加一级部门
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级部门");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}

		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@GetMapping("/info")
	@ApiOperation("上级部门Id(管理员则为0)")
	@RequiresPermissions("sys:dept:list")
	public R info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			Long parentId = null;
			for(SysDeptEntity sysDeptEntity : deptList){
				if(parentId == null){
					parentId = sysDeptEntity.getParentId();
					continue;
				}

				if(parentId > sysDeptEntity.getParentId().longValue()){
					parentId = sysDeptEntity.getParentId();
				}
			}
			deptId = parentId;
		}

		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@GetMapping("/info/{deptId}")
	@ApiOperation("根据部门Id显示部门信息")
	@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId){
		SysDeptEntity dept = sysDeptService.getById(deptId);
		
		return R.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@PostMapping("/save")
	@ApiOperation("保存部门信息")
	@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept){
        if (dept.getParentId()!=0){
            SysDeptEntity sysDeptEntity = sysDeptService.getById(dept.getParentId());
            String deptStructure = sysDeptEntity.getDeptStructure()+sysDeptEntity.getDeptId()+",";
            dept.setDeptStructure(deptStructure);
        }

		sysDeptService.save(dept);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperation("修改部门信息")
	@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept){

        if (dept.getParentId()!=0){
            SysDeptEntity sysDeptEntity = sysDeptService.getById(dept.getParentId());
            String deptStructure = sysDeptEntity.getDeptStructure()+sysDeptEntity.getDeptId()+",";
            dept.setDeptStructure(deptStructure);
        }
		sysDeptService.updateById(dept);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@ApiOperation("根据部门ID删除部门信息")
	@RequiresPermissions("sys:dept:delete")
	public R delete(@RequestBody long deptId){
		//判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return R.error("请先删除子部门");
		}

		sysDeptService.removeById(deptId);
		
		return R.ok();
	}
	
	/**
	 * 根据部门ID显示本部门及子部门列表
	 */
	@GetMapping("/getSubDeptIdList/{deptId}")
	@ApiOperation("根据部门ID显示本部门及子部门列表")
	@RequiresPermissions("sys:dept:list")
	public R getSubDeptIdList(@PathVariable("deptId") Long deptId){
        
		List<SysDeptEntity> deptList = sysDeptService.querySubList(deptId);
		
		return R.ok().put("list", deptList);
	}
}
