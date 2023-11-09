package may.yuntian.modules.sys.controller;


import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysConfigEntity;
import may.yuntian.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * 系统配置信息
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@RestController
@Api(tags="系统参数配置")
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;
	
	/**
	 * 所有配置列表
	 */
	@GetMapping("/list")
	@ApiOperation("分页显示全部信息列表")
	@RequiresPermissions("sys:config:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysConfigService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	
	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示详情")
	@RequiresPermissions("sys:config:info")
	public R info(@PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.getById(id);
		
		return R.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	@ApiOperation("保存配置")
	@RequiresPermissions("sys:config:save")
	public R save(@RequestBody SysConfigEntity config){
		//ValidatorUtils.validateEntity(config);

		sysConfigService.saveEntity(config);
		
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PostMapping("/update")
	@ApiOperation("修改配置")
	@RequiresPermissions("sys:config:update")
	public R update(@RequestBody SysConfigEntity config){
		//ValidatorUtils.validateEntity(config);
		
		sysConfigService.update(config);
		
		return R.ok();
	}
	
	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@PostMapping("/delete")
	@ApiOperation("批量删除配置")
	@RequiresPermissions("sys:config:delete")
	public R delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);
		
		return R.ok();
	}

}
