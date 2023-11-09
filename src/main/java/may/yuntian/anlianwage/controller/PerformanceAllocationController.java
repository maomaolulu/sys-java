package may.yuntian.anlianwage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.AccountEntity;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.entity.PerformanceAllocationEntity;
import may.yuntian.anlianwage.service.PerCommissionService;
import may.yuntian.anlianwage.service.PerformanceAllocationService;
import may.yuntian.anlianwage.vo.PerformanceAllocationVo;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.untils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 检评-绩效分配控制层
 * @author: lixin
 */
@RestController
@Api(tags="检评-绩效分配")
@RequestMapping("anlianwage/performanceAllocation")
public class PerformanceAllocationController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PerformanceAllocationService performanceAllocationService;
    @Autowired
    private PerCommissionService perCommissionService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询绩效分配信息列表")
//    @RequiresPermissions("anlianwage:performanceAllocation:list")
    public Result list(@RequestParam Map<String, Object> params){
        List<PerformanceAllocationVo> pageList = performanceAllocationService.getPageList(params);

        return Result.resultData(pageList);
    }

    @GetMapping("/export")
    @ApiOperation("根据条件导出绩效分配信息列表")
    public Result export(@RequestParam Map<String, Object> params){
        List<PerformanceAllocationVo> pageList = performanceAllocationService.export(params);

        return Result.data(pageList);
    }

    /**
     * 信息
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation("根据ID显示绩效分配详情")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R getInfo(@PathVariable("id") Long id) {
        PerformanceAllocationEntity performanceAllocation = performanceAllocationService.getInfo(id);

        return R.ok().put("performanceAllocation", performanceAllocation);
    }

    /**
     * 提成分配获取
     */
    @GetMapping("/royaltyDistribution/{id}")
    @ApiOperation("根据ID提成分配获取")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R royaltyDistribution(@PathVariable("id") Long id) {
        List<PerCommissionEntity> commissionEntityList = performanceAllocationService.royaltyDistribution(id);

        return R.ok().put("commissionEntityList", commissionEntityList);
    }

    /**
     * 采样提成分配修改
     */
    @PostMapping("/updateCaiYangCommission")
    @SysLog
    @ApiOperation("采样提成分配修改")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R updateCaiYangCommission(@RequestBody List<PerCommissionEntity> commissionEntityList) {
        int ret = performanceAllocationService.updateCaiYangCommission(commissionEntityList);
        if (ret == 1){
            return R.error("分配的提成金额小于总提成金额");
        }else if (ret == 2){
            perCommissionService.updateBatchById(commissionEntityList);
            return R.ok();
        }else if (ret ==3){
            return R.error("分配的提成金额大于总提成金额");
        }else {
            return R.error("无提成数据!");
        }
    }

    /**
     * 采样编辑获取
     */
    @GetMapping("/getEditInformation/{id}")
    @ApiOperation("采样编辑获取")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R getEditInformation(@PathVariable("id") Long id) {
        PerformanceAllocationEntity performanceAllocationEntity = performanceAllocationService.getById(id);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("performanceMoney",performanceAllocationEntity.getPerformanceMoney());
        Map<Integer,List<PerCommissionEntity>> commissionMap = performanceAllocationService.getEditInformation(performanceAllocationEntity);

        return R.ok().put("commissionMap", commissionMap).put("map",map);
    }

    /**
     * 采样编辑修改
     */
    @PostMapping("/samplingEditing")
    @SysLog
    @ApiOperation("采样编辑修改")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R samplingEditing(@RequestBody List<PerCommissionEntity> commissionEntityList) {
        SysUserEntity sysUserEntity =  (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        int ret = performanceAllocationService.updateCaiYangCommission(commissionEntityList);
        if (ret == 2||ret == 1){
            performanceAllocationService.saveOrUpdateBatchCommission(commissionEntityList);
            return R.ok();
        }else if (ret ==3){
            return R.error("分配的提成金额大于总提成金额");
        }else {
            return R.error("无提成数据!");
        }

    }

    /**
     * 评价采样提成方法
     */
    @PostMapping("/caiyangPjCommission")
//    @SysLog
    @ApiOperation("评价采样提成")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R caiyangPjCommission(@RequestBody PerformanceNodeVo performanceNodeVo) {
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);
        ProjectDateEntity projectDateEntity = new ProjectDateEntity();
        projectDateEntity.setGatherAcceptDate(performanceNodeVo.getGatherAcceptDate());
        projectEntity.setProjectDateEntity(projectDateEntity);

        if (performanceNodeVo.getIsTime()==2){
            if (performanceNodeVo.getGatherAcceptDate()!=null&&performanceNodeVo.getReceivedDate()!=null){
                performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                projectService.gatherCommission(projectEntity);
                return R.ok();
            }else {
                if (StringUtils.checkValNull(performanceNodeVo.getGatherAcceptDate())){
                    return R.error("原始记录接收日期为空无法生成绩效提成");
                }else if(StringUtils.checkValNull(performanceNodeVo.getReceivedDate())){
                    return R.error("采样接收日期为空无法生成绩效提成");
                }
            }
        }else if (performanceNodeVo.getIsTime()==3){
            if (StringUtils.checkValNotNull(performanceNodeVo.getPhysicalAcceptDate())){
                performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                projectService.gatherCommission(projectEntity);
                return R.ok();
            }else {
                if (StringUtils.checkValNull(performanceNodeVo.getPhysicalAcceptDate())){
                    return R.error("物理因素接收日期为空无法生成绩效提成");
                }
            }
        }else {
            if (StringUtils.checkValNotNull(performanceNodeVo.getGatherAcceptDate())&&StringUtils.checkValNotNull(performanceNodeVo.getReceivedDate())&&StringUtils.checkValNotNull(performanceNodeVo.getPhysicalAcceptDate())){
                performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                projectService.gatherCommission(projectEntity);
                return R.ok();
            }else {
                if (StringUtils.checkValNull(performanceNodeVo.getGatherAcceptDate())){
                    return R.error("原始记录接收日期为空无法生成绩效提成");
                }else if(StringUtils.checkValNull(performanceNodeVo.getReceivedDate())){
                    return R.error("采样接收日期为空无法生成绩效提成");
                }else if (StringUtils.checkValNull(performanceNodeVo.getPhysicalAcceptDate())){
                    return R.error("物理因素接收日期为空无法生成绩效提成");
                }
            }
        }
        return R.error("未找到相应数据");
    }


    /**
     * 环境采样提成方法
     */
    @PostMapping("/caiyangHjCommission")
    @SysLog
    @ApiOperation("环境采样提成")
//    @RequiresPermissions("anlianwage:performanceAllocation:info")
    public R caiyangHjCommission(@RequestBody PerformanceNodeVo performanceNodeVo) {
        performanceAllocationService.caiyangHjCommission(performanceNodeVo);
        return R.ok();
    }

}
