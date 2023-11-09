package may.yuntian.modules.sys.controller;


import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.common.validator.ValidatorUtils;
import may.yuntian.modules.sys.entity.SysTaskEntity;
import may.yuntian.modules.sys.service.SysTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 任务计划信息
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@RestController
@RequestMapping("/sys/task")
public class SysTaskController extends AbstractController {
	@Autowired
	private SysTaskService sysTaskService;
	
	/**
	 * 所有任务计划列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:task:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysTaskService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	
	/**
	 * 任务计划信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:task:info")
	public R info(@PathVariable("id") Long id){
		SysTaskEntity task = sysTaskService.getById(id);
		
		return R.ok().put("task", task);
	}
	
	/**
	 * 保存任务计划
	 */
	@SysLog("保存任务计划")
	@PostMapping("/save")
	@RequiresPermissions("sys:task:save")
	public R save(@RequestBody SysTaskEntity task){
		ValidatorUtils.validateEntity(task);

		sysTaskService.save(task);
		
		return R.ok();
	}
	
	/**
	 * 修改任务计划
	 */
	@SysLog("修改任务计划")
	@PostMapping("/update")
	@RequiresPermissions("sys:task:update")
	public R update(@RequestBody SysTaskEntity task){
		ValidatorUtils.validateEntity(task);
		
		sysTaskService.updateById(task);
		
		return R.ok();
	}
	
	/**
	 * 删除任务计划
	 */
	@SysLog("删除任务计划")
	@PostMapping("/delete")
	@RequiresPermissions("sys:task:delete")
	public R delete(@RequestBody Long[] ids){
		sysTaskService.deleteBatchIds(Arrays.asList(ids));
		
		return R.ok();
	}

}
