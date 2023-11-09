package may.yuntian.anlianwage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.service.ProjectUserService;
import may.yuntian.anlianwage.entity.PerformanceAllocationEntity;
import may.yuntian.anlianwage.mapper.PerformanceAllocationMapper;
import may.yuntian.anlianwage.service.PerformanceAllocationService;
import may.yuntian.anlianwage.vo.PerformanceAllocationNewVO;
import may.yuntian.anlianwage.vo.PerformanceAllocationVo;
import may.yuntian.common.utils.DateUtils;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @description: 检评-绩效统计控制层
 * @author: lixin
 */
@RestController
@Api(tags="检评-绩效统计")
@RequestMapping("anlianwage/performanceStatic")
public class PerformanceStaticController {
    @Autowired
    private PerformanceAllocationMapper performanceAllocationMapper;
    @Autowired
    PerformanceAllocationService performanceAllocationService;
    @Autowired
    private ProjectUserService projectUserService;


    private String getFirstDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = may.yuntian.anlian.utils.DateUtils.dateTimeMonthStart();//当前月的第一天
        String performanceDateStart = format.format(date);
        return performanceDateStart;
    }


    /**
     * 采样提成统计列表-- 当前月
     */
    @GetMapping("/samplingCommission")
    @ApiOperation("采样提成统计列表")
    public R samplingCommission(){
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("performance_date", this.getFirstDay()).eq("types","采样提成");
        List<PerformanceAllocationVo> performanceAllocationVoList = performanceAllocationMapper.getListByQurryWrapper(queryWrapper);
        for (PerformanceAllocationVo performanceAllocationVo : performanceAllocationVoList){
            String people = projectUserService.getListByTypeAndProjectId(performanceAllocationVo.getProjectId());
            performanceAllocationVo.setSamplePeople(people);
        }
        return R.ok().put("samplingCommissionList",performanceAllocationVoList);
    }

    /**
     * 签发提成统计列表
     */
    @GetMapping("/issueCommission")
    @ApiOperation("签发提成统计列表")
    public R issueCommission(){
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("performance_date", this.getFirstDay()).eq("types","签发提成");
        List<PerformanceAllocationVo> performanceAllocationVoList = performanceAllocationMapper.getListByQurryWrapper(queryWrapper);
        for (PerformanceAllocationVo performanceAllocationVo : performanceAllocationVoList){
            String people = projectUserService.getListByTypeAndProjectId(performanceAllocationVo.getProjectId());
            performanceAllocationVo.setSamplePeople(people);
        }
        return R.ok().put("issueCommissionList",performanceAllocationVoList);
    }


    /**
     * 归档提成统计列表
     */
    @GetMapping("/fillingCommission")
    @ApiOperation("归档提成统计列表")
    public R fillingCommission(){
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("performance_date", this.getFirstDay()).eq("types","报告提成");
        List<PerformanceAllocationVo> performanceAllocationVoList = performanceAllocationMapper.getListByQurryWrapper(queryWrapper);
        for (PerformanceAllocationVo performanceAllocationVo : performanceAllocationVoList){
            String people = projectUserService.getListByTypeAndProjectId(performanceAllocationVo.getProjectId());
            performanceAllocationVo.setSamplePeople(people);
        }
        return R.ok().put("fillingCommissionList",performanceAllocationVoList);
    }

    /**
     * 统计相关图表
     */
    @GetMapping("/statisticalCorrelation")
    @ApiOperation("统计相关图表")
    public R statisticalCorrelation(@RequestParam Map<String,Object> params){
        PerformanceAllocationNewVO map = performanceAllocationService.statisticalCorrelation(params);
        return R.ok().put("map",map);
    }


}
