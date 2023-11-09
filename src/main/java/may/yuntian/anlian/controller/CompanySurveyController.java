package may.yuntian.anlian.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.vo.CompanySurveyVo;
import may.yuntian.anliantest.entity.PublicityResults;
import may.yuntian.anliantest.service.MetNewsService;
import may.yuntian.anliantest.service.PublicityResultsService;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.Result;
import may.yuntian.untils.pageUtil2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.common.utils.R;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;

import may.yuntian.anlian.entity.CompanySurveyEntity;
//import may.yuntian.anlian.entity.GatherPlanEntity;
//import may.yuntian.anlian.entity.SubstanceEntity;
import may.yuntian.anlian.service.CompanySurveyService;
//import may.yuntian.anlian.service.GatherPlanService;
//import may.yuntian.anlian.service.SubstanceService;
import may.yuntian.anlian.utils.StringUtils;


/**
 * 用人单位概况调查管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@RestController
@Api(tags = "现场调查:用人单位概况调查")
@RequestMapping("anlian/companysurvey")
public class CompanySurveyController {
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MetNewsService metNewsService;
    @Autowired
    private PublicityResultsService publicityResultsService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询用人单位概况调查列表")
    @RequiresPermissions("sample:company:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = companySurveyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示用人单位概况调查详情")
    @RequiresPermissions("sample:company:info")
    public R info(@PathVariable("id") Long id) {
        CompanySurveyEntity companySurvey = companySurveyService.getById(id);

        return R.ok().put("companySurvey", companySurvey);
    }

    /**
     * 根据项目ID显示用人单位概况调查详情
     */
    @GetMapping("/infoByProjectId/{projectId}")
    @ApiOperation("根据项目ID显示用人单位概况调查详情")
    @RequiresPermissions("sample:company:info")
    public R infoByProjectId(@PathVariable("projectId") Long projectId) {
        CompanySurveyEntity companySurvey = companySurveyService.getOne(
                new QueryWrapper<CompanySurveyEntity>()
                        .eq("project_id", projectId)
        );
//    	//从采样计划中将物质id和物质ids查出并分组
//    	List<GatherPlanEntity> gatherPlanList = gatherPlanService.list(newCommission QueryWrapper<GatherPlanEntity>()
//    			.select("substance_id,substance_ids")
//    			.eq("project_id", projectId)
//    			.groupBy("substance_id,substance_ids")
//    			);
//    	List<String> sampleIemList = newCommission ArrayList<String>();
//    	if(gatherPlanList != null && gatherPlanList.size()>0) {
//    		List<Long> substanceIdList = gatherPlanList.stream().map(GatherPlanEntity::getSubstanceId).collect(Collectors.toList());
////    		System.out.println("去重前list="+substanceIdList.toString());
//    		List<Long> substanceIdsList = newCommission ArrayList<>();
//    		for(GatherPlanEntity gatherPlanEntity : gatherPlanList) {
//    			if(StringUtils.isNotBlank(gatherPlanEntity.getSubstanceIds())) {
//    				String substanceIds = gatherPlanEntity.getSubstanceIds();
//    				String[] strings = substanceIds.split(",");
//    				for(String string : strings) {
//    					if (substanceIdsList.contains(Long.valueOf(string))) {
//							continue;
//						} else {
//							substanceIdsList.add(Long.valueOf(string));
//						}
//    				}
//    			}
//    		}
//    		substanceIdList.addAll(substanceIdsList);
////    		System.out.println("id数组合并去重前结果="+substanceIdList.toString());
//    		List<Long> newSubstanceIdList2 = substanceIdList.stream().distinct().collect(Collectors.toList());
////    		System.out.println("id数组合并去重后结果"+newSubstanceIdList2.toString());
//    		List<SubstanceEntity> substanceList = substanceService.listByIds(newSubstanceIdList2);
//    		List<String> sampleIemList2 = substanceList.stream().map(SubstanceEntity::getName).collect(Collectors.toList());
//    		sampleIemList = sampleIemList2.stream().distinct().collect(Collectors.toList());
////    		System.out.println("id去重后查出的物质="+sampleIemList.toString());
//    	}
        return R.ok().put("companySurvey", companySurvey);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增用人单位概况调查")
    @ApiOperation("新增用人单位概况调查")
    @RequiresPermissions("sample:company:save")
    public R save(@RequestBody CompanySurveyEntity companySurvey) {
        companySurveyService.save(companySurvey);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改用人单位概况调查")
    @ApiOperation("修改用人单位概况调查")
    @RequiresPermissions("sample:company:update")
    public R update(@RequestBody CompanySurveyEntity companySurvey) {
        companySurveyService.updateById(companySurvey);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除用人单位概况调查")
    @ApiOperation("删除用人单位概况调查")
    @RequiresPermissions("sample:company:delete")
    public R delete(@RequestBody Long[] ids) {
        companySurveyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 信息
     */
    @GetMapping("/publicityList")
    @ApiOperation("公示项目列表")
    public Result publicityList(CompanySurveyVo companySurveyVo) {
        pageUtil2.startPage();
        List<CompanySurveyVo> companySurveyVos = companySurveyService.publicityList(companySurveyVo);

        return Result.resultData(companySurveyVos);
    }

    /**
     * 公示项目公示
     */
    @PostMapping("/publicityProject")
    @ApiOperation("公示项目公示")
    public Result publicityProject(@RequestBody List<CompanySurveyVo> companySurveyVos) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        PublicityResults publicityResults = new PublicityResults();


        for (CompanySurveyVo companySurveyVo : companySurveyVos
        ) {
            Boolean aBoolean = metNewsService.saveMetNews(companySurveyVo);
            if (aBoolean) {//公示成功
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setId(companySurveyVo.getProjectId());
                projectEntity.setApplyPublicityStatus(5);
                projectEntity.setPublicityLastTime(new Date());
                //申请记录
                publicityResults.setUsername(sysUserEntity.getUsername());
                publicityResults.setProjectId(companySurveyVo.getProjectId());
                publicityResults.setOperation("已公示");
                publicityResults.setOperationTime(new Date());
                //0：待申请 1：主管审核，2：质控审核 3：主管驳回 4：质控驳回 5：已公示
                publicityResults.setStatus(5);
                publicityResults.setRemark("");

                projectService.updateById(projectEntity);
                //申请记录保存
                publicityResultsService.save(publicityResults);
            } else {//失败
                return Result.error("公示失败");
            }
        }

        return Result.ok();


    }

    /**
     * 公示项目公示
     */
    @PostMapping("/publicityProjectInfo")
    @ApiOperation("公示项目公示")
    public Result publicityProjectInfo(@RequestBody CompanySurveyVo companySurveyVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        PublicityResults publicityResults = new PublicityResults();

        Boolean aBoolean = metNewsService.saveMetNews(companySurveyVo);
        if (aBoolean) {//公示成功
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setId(companySurveyVo.getProjectId());
            projectEntity.setApplyPublicityStatus(5);
            projectEntity.setPublicityLastTime(new Date());
            //申请记录
            publicityResults.setUsername(sysUserEntity.getUsername());
            publicityResults.setProjectId(companySurveyVo.getProjectId());
            publicityResults.setOperation("已公示");
            publicityResults.setOperationTime(new Date());
            //0：待申请 1：主管审核，2：质控审核 3：主管驳回 4：质控驳回 5：已公示
            publicityResults.setStatus(5);
            publicityResults.setRemark("");

            projectService.updateById(projectEntity);
            //申请记录保存
            publicityResultsService.save(publicityResults);
        } else {//失败
            return Result.error("公示失败");
        }


        return Result.ok();


    }

    /**
     * 公示项目驳回（质控）
     */
    @PostMapping("/rejectPublicityProject")
    @ApiOperation("公示项目驳回（质控）")
    public Result rejectPublicityProject(@RequestBody CompanySurveyVo companySurveyVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        PublicityResults publicityResults = new PublicityResults();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(companySurveyVo.getProjectId());
        projectEntity.setApplyPublicityStatus(4);
        projectEntity.setPublicityLastTime(new Date());
        projectEntity.setControlReject(companySurveyVo.getControlReject());
        //申请记录
        publicityResults.setUsername(sysUserEntity.getUsername());
        publicityResults.setProjectId(companySurveyVo.getProjectId());
        publicityResults.setOperation("质控驳回");
        publicityResults.setOperationTime(new Date());
        //0：待申请 1：主管审核，2：质控审核 3：主管驳回 4：质控驳回 5：已公示
        publicityResults.setStatus(4);
        publicityResults.setRemark(companySurveyVo.getControlReject());

        projectService.updateById(projectEntity);
        //申请记录保存
        publicityResultsService.save(publicityResults);

        return Result.ok("驳回成功");


    }

    /**
     * xin 评价项目公示-信息公示列表
     */
    @GetMapping("/publicityListPj")
    @ApiOperation("检评公示记录列表")
    public Result publicityListPj(CompanySurveyVo company) {
        List<CompanySurveyVo> list = companySurveyService.publicityListPj(company);
        return Result.resultData(list);
    }

    /**
     * xin 评价项目公示-公示记录列表
     */
    @GetMapping("/listSys")
    @ApiOperation("检评公示记录列表")
    public Result listSys(@RequestParam Map<String, Object> params) {
        List<ProjectEntity> list = companySurveyService.listSys(params);
        return Result.resultData(list);
    }


}
