package may.yuntian.anlianwage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlianwage.entity.PerformanceEntity;
import may.yuntian.anlianwage.entity.PerformanceLevelEntity;
import may.yuntian.anlianwage.service.PerformanceLevelService;
import may.yuntian.anlianwage.service.PerformanceService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @description: 检评-人员目标控制层
 * @author: lixin
 */
@RestController
@Api(tags="检评-人员目标")
@RequestMapping("anlianwage/performancePeople")
public class PerformancePeopleController {
   @Autowired
    private PerformanceService performanceService;
   @Autowired
   private PerformanceLevelService performanceLevelService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询提成记录信息列表")
//    @RequiresPermissions("anlianwage:performancePeople:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = performanceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 人员晋升
     */
    @PostMapping("/promotion")
    @SysLog("人员晋升")
    @ApiOperation("人员晋升")
//    @RequiresPermissions("anlianwage:performancePeople:update")
    public R promotion(@RequestBody PerformanceEntity performanceEntity){
        performanceService.updateById(performanceEntity);

        return R.ok();
    }

    /**
     * 新增人员
     */
    @PostMapping("/save")
    @SysLog("新增人员")
    @ApiOperation("新增人员")
//    @RequiresPermissions("anlianwage:performancePeople:update")
    public R save(@RequestBody PerformanceEntity performanceEntity){
        performanceService.save(performanceEntity);

        return R.ok();
    }

    /**
     * 修改人员
     */
    @PostMapping("/update")
    @SysLog("修改人员")
    @ApiOperation("修改人员")
//    @RequiresPermissions("anlianwage:performancePeople:update")
    public R update(@RequestBody PerformanceEntity performanceEntity){
        performanceService.updateById(performanceEntity);

        return R.ok();
    }


    /**
     * 信息
     */
    @GetMapping("/getTypeList/{type}")
    @ApiOperation("获取类型等级信息")
    public R getTypeList(@PathVariable("type") String type) {
        List<PerformanceLevelEntity> list = performanceLevelService.list(type);

        return R.ok().put("data", list);
    }

}
