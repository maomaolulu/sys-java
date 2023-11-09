package may.yuntian.modules.sys.controller;

import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysConlogEntity;
import may.yuntian.modules.sys.service.SysConlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 用户登录信息统计
 * 
 * @author LiXin
 * @date 2021-04-30
 */
@RestController
@Api(tags="用户登录信息统计")
@RequestMapping("/sys/conlog")
public class SysConlogController {
	@Autowired
	private SysConlogService sysConlogService;
	
	/**
	 * 列表
	 */
//	@ResponseBody
	@GetMapping("/statistical")
	@ApiOperation("显示全部信息列表")
	public R loginStatistical(){
		List<SysConlogEntity> conlogWeekList = sysConlogService.getWeekListDesc();
		List<SysConlogEntity> conlogMonthList = sysConlogService.getMonthListDesc();
		Map<String, Object> map = sysConlogService.getCountMap();
		return R.ok().put("conlogWeekList", conlogWeekList).put("conlogMonthList", conlogMonthList).put("map", map);
	}

}
