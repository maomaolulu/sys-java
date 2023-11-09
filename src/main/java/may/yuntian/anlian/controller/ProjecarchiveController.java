package may.yuntian.anlian.controller;

import java.util.Map;
import java.math.BigDecimal;
import java.util.Arrays;

import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.service.*;
import may.yuntian.anlianwage.service.GradePointService;
import may.yuntian.anlianwage.service.PerformanceAllocationService;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;
import may.yuntian.socket.service.IAbuProjectNoteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.anlian.utils.StringUtils;


/**
 * 项目归档文件目录管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@RestController
@Api(tags="项目归档文件目录")
@RequestMapping("anlian/projecarchive")
public class ProjecarchiveController {
    @Autowired
    private ProjecarchiveService projecarchiveService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CompanySurveyService companySurveyService;
	@Autowired
	private ProjectProceduresService projectProceduresService;//项目流程
	@Autowired
	private ProjectDateService projectDateService;//项目时间
    @Autowired
    private ProjectAmountService projectAmountService;
    
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private IAbuProjectNoteService iAbuProjectNoteService;
	
	private static String TYPE_NAME = "commissionRatio";//参数类型 
//	private static String TYPE_NAME_EVALUATION = "evaluationProjectType";//参数类型
//	private static String TYPE_NAME_ENVIEONMENT = "allevlutionProjectType";//参数类型

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询项目归档文件目录列表")
    @RequiresPermissions("anlian:projecarchive:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = projecarchiveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示项目归档文件目录详情")
    @RequiresPermissions("anlian:projecarchive:info")
    public R info(@PathVariable("id") Long id){
        ProjecarchiveEntity projecarchive = projecarchiveService.getById(id);

        return R.ok().put("projecarchive", projecarchive);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增项目归档文件目录")
    @ApiOperation("新增项目归档文件目录")
    @RequiresPermissions("anlian:projecarchive:save")
    public R save(@RequestBody ProjecarchiveEntity projecarchive){
        String[] pingjiaTeam = new String[]{"预评","专篇","控评","现状"};
        String[] jianpingTeam = new String[]{"检评","职卫监督"};
        String[] huanjing = new String[]{"环境验收","环境应急预案","排污许可证申请","排污许可后管理","环保管家","应急预案","环境示范","排污许可"};
    	Long projectId = projecarchive.getProjectId();
        ProjectEntity project = projectService.getById(projectId);
        if (StringUtils.isBlank(project.getCharge())) {
			return R.error("未填写项目负责人，无法归档。");
		}
        
        projecarchiveService.save(projecarchive);
        


        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
    	

        if(projecarchive.getReportIssue()!=null) {
        	if(project.getStatus()<40) {
        		project.setStatus(40);
        		ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
	    		proceduresEntity.setProjectId(project.getId());
	    		proceduresEntity.setStatus(40);
	    		projectProceduresService.save(proceduresEntity);
        	}
            projectDateEntity.setReportIssue(projecarchive.getReportIssue());
    	}
        if(projecarchive.getReportBinding()!=null) {
            projectDateEntity.setReportBinding(projecarchive.getReportBinding());
    	}
        if (projecarchive.getReportAccept()!=null) {
            projectDateEntity.setReportAccept(projecarchive.getReportAccept());
		}
        CompanySurveyEntity companySurvey = companySurveyService.seleteByProjectId(projectId);
        companySurvey.setIndustryCategory(projecarchive.getIndustryCategory());
        companySurvey.setRiskLevel(projecarchive.getRiskLevel());
        companySurvey.setTestItems(projecarchive.getTestItems());
        companySurveyService.updateById(companySurvey);
        if (projecarchive.getInspectionArchiveDate()!=null) {
            projectDateEntity.setReportFiling(projecarchive.getInspectionArchiveDate());
        }
        projectService.updateById(project);
        projectDateService.updateById(projectDateEntity);
        if(projecarchive.getDetectionArchiveDate()!=null) {
        	testCommission(projecarchive);
        }
        boolean a = true;
    	if(projecarchive.getInspectionArchiveDate()!=null) {
    	    reportCommission(projecarchive);
            GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
            PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
            PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
            performanceNodeVo.setProjectId(project.getId());
            performanceNodeVo.setReportFiling(projecarchive.getInspectionArchiveDate());
            if (Arrays.asList(jianpingTeam).contains(project.getType())){
                performanceAllocationService.fillingCommission(performanceNodeVo);//TODO 新归档提成-检评
            }else if (Arrays.asList(pingjiaTeam).contains(project.getType())){
                a = gradePointService.getFilingFees(performanceNodeVo);
            }else if (Arrays.asList(huanjing).contains(project.getType())){
                performanceAllocationService.issueHjCommission(performanceNodeVo);
            }
        }

    	if(projecarchive.getInspectionArchiveDate()!=null) {
//    		if(projectDateEntity!=null) {
//                projectDateEntity.setReportFiling(projecarchive.getInspectionArchiveDate());
//                projectDateService.updateById(projectDateEntity);
//    		}
    		if (project!=null) {
    			if(project.getStatus()<60) {
                    project.setStatus(60);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(60);
                    projectProceduresService.save(proceduresEntity);
    			}
                ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
                if(project.getStatus()<70&&projectAmountEntity.getNosettlementMoney().compareTo(BigDecimal.ZERO)==0) {
                    project.setStatus(70);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(70);
                    projectProceduresService.save(proceduresEntity);
                }
    		}
    	}
        if (!a){
            return R.error("未填写归档日期");
        }else {
            return R.ok();
        }
        
//        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改项目归档文件目录")
    @ApiOperation("修改项目归档文件目录")
    @RequiresPermissions("anlian:projecarchive:update")
    public R update(@RequestBody ProjecarchiveEntity projecarchive){
        String[] pingjiaTeam = new String[]{"预评","专篇","控评","现状"};
        String[] jianpingTeam = new String[]{"检评","职卫监督"};
        String[] huanjing = new String[]{"环境验收","环境应急预案","排污许可证申请","排污许可后管理","环保管家","应急预案","环境示范","排污许可"};
        Long projectId = projecarchive.getProjectId();
        ProjectEntity project = projectService.getById(projectId);
        if (StringUtils.isBlank(project.getCharge())) {
            return R.error("未填写项目负责人，无法归档。");
        }

        projecarchiveService.updateById(projecarchive);


        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);

        if(projecarchive.getReportIssue()!=null) {
            if(project.getStatus()<40) {
                project.setStatus(40);
                ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                proceduresEntity.setProjectId(project.getId());
                proceduresEntity.setStatus(40);
                projectProceduresService.save(proceduresEntity);
            }
            projectDateEntity.setReportIssue(projecarchive.getReportIssue());
        }
        if(projecarchive.getReportBinding()!=null) {
            projectDateEntity.setReportBinding(projecarchive.getReportBinding());
        }
        if (projecarchive.getReportAccept()!=null) {
            projectDateEntity.setReportAccept(projecarchive.getReportAccept());
        }
        CompanySurveyEntity companySurvey = companySurveyService.seleteByProjectId(projectId);
        if (companySurvey!=null){
            companySurvey.setIndustryCategory(projecarchive.getIndustryCategory());
            companySurvey.setRiskLevel(projecarchive.getRiskLevel());
            companySurvey.setTestItems(projecarchive.getTestItems());
            companySurveyService.updateById(companySurvey);
        }


//        if(projecarchive.getDetectionArchiveDate()!=null) {
//            testCommission(projecarchive);
//        }

        boolean a = true;
        if(projecarchive.getInspectionArchiveDate()!=null) {
            reportCommission(projecarchive);
            GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
            PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
            PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
            performanceNodeVo.setProjectId(project.getId());
            performanceNodeVo.setReportFiling(projecarchive.getInspectionArchiveDate());
            if (Arrays.asList(jianpingTeam).contains(project.getType())){
                performanceAllocationService.fillingCommission(performanceNodeVo);//TODO 新归档提成-检评
            }else if (Arrays.asList(pingjiaTeam).contains(project.getType())){
                a = gradePointService.getFilingFees(performanceNodeVo);
            }else if (Arrays.asList(huanjing).contains(project.getType())){
                performanceAllocationService.issueHjCommission(performanceNodeVo);
            }

        }
        if (projecarchive.getInspectionArchiveDate()!=null) {
            projectDateEntity.setReportFiling(projecarchive.getInspectionArchiveDate());
        }
        projectService.updateById(project);
        projectDateService.updateById(projectDateEntity);
        if(projecarchive.getInspectionArchiveDate()!=null) {
            if(projectDateEntity!=null) {
                projectDateEntity.setReportFiling(projecarchive.getInspectionArchiveDate());
                projectDateService.updateById(projectDateEntity);
            }
            if (project!=null) {
                if(project.getStatus()<60) {
                    project.setStatus(60);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(60);
                    projectProceduresService.save(proceduresEntity);
                }
                ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
                if(project.getStatus()<70&&projectAmountEntity.getNosettlementMoney().compareTo(BigDecimal.ZERO)==0) {
                    project.setStatus(70);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(70);
                    projectProceduresService.save(proceduresEntity);
                }
            }
        }
        if (!a){
            return R.error("未填写归档日期或签发日期");
        }else {
            return R.ok();
        }
    }
    
    /**
     * 检测提成计算
     * @param projecarchive
     */
    private void testCommission(ProjecarchiveEntity projecarchive) {
    	
    	Long projectId = projecarchive.getProjectId();
		ProjectEntity project = projectService.getById(projectId);
    	BigDecimal netvalue = project.getNetvalue();
    	
		SysDictEntity sysDict = sysDictService.queryByTypeAndCode(TYPE_NAME, "4");
    	Double commissionRatioDouble = Double.valueOf(sysDict.getValue());
    	BigDecimal commissionRatio = BigDecimal.valueOf(commissionRatioDouble);//提成比例
    	BigDecimal cmsAmount = netvalue.multiply(commissionRatio);//项目净值*提成比例
    	
    	CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId, "检测提成");
    	if(commissionEntity == null ) {
    		CommissionEntity commission = new CommissionEntity();
    		commission.setProjectId(projectId);
    		commission.setCommissionDate(projecarchive.getDetectionArchiveDate());
    		commission.setPersonnel("赵鑫");
    		commission.setType("检测提成");
    		commission.setState(1);
    		commission.setSubjection("杭州安联");
    		commission.setCmsAmount(cmsAmount);
    		commissionService.save(commission);
    	}else if (!commissionEntity.getCmsAmount().equals(cmsAmount)) {
			commissionEntity.setCmsAmount(cmsAmount);
			commissionService.updateById(commissionEntity);
		}

    	
	}
    
    /**
     * 报告编制提成计算
     * @param projecarchive
     */
    private void reportCommission(ProjecarchiveEntity projecarchive) {
    	
    	Long projectId = projecarchive.getProjectId();
		ProjectEntity project = projectService.getById(projectId);
    	BigDecimal netvalue = project.getNetvalue();
    	
		SysDictEntity sysDict = sysDictService.queryByTypeAndCode(TYPE_NAME, "3");
    	Double commissionRatioDouble = Double.valueOf(sysDict.getValue());
    	BigDecimal commissionRatio = BigDecimal.valueOf(commissionRatioDouble);//提成比例
    	BigDecimal cmsAmount = netvalue.multiply(commissionRatio);//项目净值*提成比例
    	
    	Long chargeId = project.getChargeId();
    	String subjection="";
    	String charge = "";
    	if(chargeId!=null) {
    		charge = project.getCharge();
    		SysUserEntity sysUserEntity = sysUserService.getById(chargeId);
        	subjection = sysUserEntity.getSubjection();
    	}
//    	SysUserEntity sysUserEntity = sysUserService.getById(chargeId);
//    	String subjection = sysUserEntity.getSubjection();
    	
    	CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId, "报告提成");
    	if(commissionEntity == null ) {
    		CommissionEntity commission = new CommissionEntity();
    		commission.setProjectId(projectId);
    		commission.setCommissionDate(projecarchive.getInspectionArchiveDate());
    		commission.setPersonnel(charge);
    		commission.setType("报告提成");
    		commission.setState(1);
    		commission.setSubjection(subjection);
    		commission.setCmsAmount(cmsAmount);
    		commissionService.save(commission);
    	}else if (!commissionEntity.getCmsAmount().equals(cmsAmount) || !commissionEntity.getPersonnel().equals(project.getCharge())) {
			commissionEntity.setCmsAmount(cmsAmount);
			commissionEntity.setPersonnel(charge);
			commissionEntity.setSubjection(subjection);
			commissionService.updateById(commissionEntity);
		}

	}

    
    

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除项目归档文件目录") 
    @ApiOperation("删除项目归档文件目录") 
    @RequiresPermissions("anlian:projecarchive:delete")
    public R delete(@RequestBody Long[] ids){
        projecarchiveService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
