package may.yuntian.modules.sys.controller;

import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.service.SysConlogService;
import may.yuntian.modules.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Map;


/**
 * 系统日志
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 登录成功列表
	 */
	@ResponseBody
	@GetMapping("/listByOperation")
	@RequiresPermissions("sys:log:list")
	public R listByOperation(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPageByOperation(params);

		return R.ok().put("page", page);
	}
	
	
    /**
     *  登录统计
     * @param username
     * @return
     */
	@ResponseBody
	@GetMapping("/getLoginStatistics")
//	@RequiresPermissions("sys:log:list")
	public R getLoginStatistics(@RequestParam Map<String, Object> params){
		Map<String, Object> map = sysLogService.getLoginStatistics(params);

		return R.ok().put("map", map);
	}
	
//    /**
//     *  获取redis中所有人员登录信息
//     */
//	@ResponseBody
//	@GetMapping("/getLogListByRedis")
////	@RequiresPermissions("sys:log:list")
//	public R getLogListByRedis(){
//		List<Map<String, Object>> mapList = sysLogService.getLogListByRedis();
//		return R.ok().put("mapList", mapList);
//	}
}
