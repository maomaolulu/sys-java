package may.yuntian.anlian.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import may.yuntian.anlian.dto.ProjectAccountingDto;
import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.mapper.CategoryMapper;
import may.yuntian.anlian.mapper.ContractMapper;
import may.yuntian.anlian.service.*;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.anlian.vo.*;
import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;
import may.yuntian.anlianwage.newCommission.service.NewCommissionService;
import may.yuntian.anlianwage.service.GradePointService;
import may.yuntian.anlianwage.service.PerformanceAllocationService;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.common.utils.*;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys_v2.utils.SpringUtils;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;
import may.yuntian.socket.service.IAbuProjectNoteService;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.anlian.mapper.ProjectMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目表(包含了原任务表的字段)
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@SuppressWarnings("all")
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {

    @Autowired
    private OrderSourceService orderSourceService;        //项目隶属来源信息
    @Autowired
    private CategoryService categoryService;//项目类型管理
    @Autowired
    private SysRoleDeptService sysRoleDeptService;//角色与部门对应关系
    @Autowired
    private ProjectAmountService projectAmountService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectProceduresService projectProceduresService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private AlCompanySurveyService alCompanySurveyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictService sysDictService;//数据字典
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private ProjecarchiveService projecarchiveService;
    @Autowired
    private ProjectCountService projectCountService;
    @Autowired
    private BackupsProjectProceduresService backupsProjectProceduresService;
    @Autowired
    private ProjectMoneyLogService projectMoneyLogService;//项目修改金额日志
    @Autowired
    private ContractService contractService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private IAbuProjectNoteService iAbuProjectNoteService;


    private static String TYPE_NAME = "commissionRatio";//参数类型

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);

        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper.orderByDesc("id"));
        page.getRecords().forEach(action -> {
            Long projectId = action.getId();
            ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
            if (projectAmountEntity == null) {
                projectAmountEntity = new ProjectAmountEntity();
            }
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity == null) {
                projectDateEntity = new ProjectDateEntity();
            }
            List<AccountEntity> accountEntityList = accountService.listByProjectId(projectId);
            if (accountEntityList == null || accountEntityList.size() == 0) {
                accountEntityList = new ArrayList<>();
            }
            action.setProjectAmountEntity(projectAmountEntity);
            action.setProjectDateEntity(projectDateEntity);
            action.setAccountEntityList(accountEntityList);
        });

        return new PageUtils(page);
    }

    /**
     * 优化分页测试
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectEntity> getPageList(Map<String, Object> params) {
        String subjection = (String) params.get("subjection");
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
        }
        pageUtil2.startPage();
        List<ProjectEntity> projectEntities = baseMapper.selectList(queryWrapper.orderByDesc("id"));
        List<Long> amountDateIds = projectEntities.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        List<ProjectAmountEntity> projectAmountEntityList;
        List<ProjectDateEntity> projectDateEntityList;
        Map<Long, List<ProjectAmountEntity>> projectAmountMap;
        Map<Long, List<ProjectDateEntity>> projectDateMap;
        if (amountDateIds.size() > 0) {
            projectAmountEntityList = projectAmountService.selectListByProjectIds(amountDateIds);
            projectDateEntityList = projectDateService.selectListByProjectIds(amountDateIds);
            projectAmountMap = projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        } else {
            projectAmountMap = new HashMap<>();//projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = new HashMap<>();//projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        }

//        List<QueryProjectAmountDateVo> queryProjectAmountDateVoList = baseMapper.queryAllprojectAmountDate(queryWrapper);
        projectEntities.forEach(action -> {
            Long projectId = action.getId();
            List<AccountEntity> accountEntityList = accountService.listByProjectId(projectId);
            if (accountEntityList == null || accountEntityList.size() == 0) {
                accountEntityList = new ArrayList<>();
            }
            action.setAccountEntityList(accountEntityList);
            action.setProjectAmountEntity(projectAmountMap.get(projectId) == null ? new ProjectAmountEntity() : projectAmountMap.get(projectId).get(0));
            action.setProjectDateEntity(projectDateMap.get(projectId) == null ? new ProjectDateEntity() : projectDateMap.get(projectId).get(0));

        });

        return projectEntities;
    }

    /**
     * 收付款项页面
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectEntity> getPaymentReceiptList(Map<String, Object> params) {
        String subjection = (String) params.get("subjection");
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
//            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
//                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection));
//            queryWrapper.and(wrapper ->
//                    wrapper.like( "company_order", subjection).or().like( "business_source", subjection)
//                            .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
//            );
        }
        pageUtil2.startPage();
        List<ProjectEntity> projectEntities = baseMapper.selectList(queryWrapper.orderByDesc("id"));
        List<Long> amountDateIds = projectEntities.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        List<ProjectAmountEntity> projectAmountEntityList;
        List<ProjectDateEntity> projectDateEntityList;
        Map<Long, List<ProjectAmountEntity>> projectAmountMap;
        Map<Long, List<ProjectDateEntity>> projectDateMap;
        if (amountDateIds.size() > 0) {
            projectAmountEntityList = projectAmountService.selectListByProjectIds(amountDateIds);
            projectDateEntityList = projectDateService.selectListByProjectIds(amountDateIds);
            projectAmountMap = projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        } else {
            projectAmountMap = new HashMap<>();//projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = new HashMap<>();//projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        }

//        List<QueryProjectAmountDateVo> queryProjectAmountDateVoList = baseMapper.queryAllprojectAmountDate(queryWrapper);

        List<Long> projectIds = projectEntities.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        Map<Long, List<CommissionEntity>> commissionMap = new HashMap<>();
        if (projectIds != null && projectIds.size() > 0) {
            CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);
            List<CommissionEntity> commissionEntityList = commissionService.getCommissionListByProjectId(projectIds);
            if (commissionEntityList != null && commissionEntityList.size() > 0) {
                commissionMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getProjectId));
            }
        } else {
            commissionMap = new HashMap<>();
        }
//        projectEntities.forEach(action -> {
        for (ProjectEntity action : projectEntities
        ) {
//        projectEntities.forEach(action -> {
            Long projectId = action.getId();
            List<AccountEntity> accountEntityList = accountService.listByProjectId(projectId);
            if (accountEntityList == null || accountEntityList.size() == 0) {
                accountEntityList = new ArrayList<>();
            }
            action.setAccountEntityList(accountEntityList);
            action.setProjectAmountEntity(projectAmountMap.get(projectId) == null ? new ProjectAmountEntity() : projectAmountMap.get(projectId).get(0));
            action.setProjectDateEntity(projectDateMap.get(projectId) == null ? new ProjectDateEntity() : projectDateMap.get(projectId).get(0));

            if (commissionMap.get(projectId) != null) {
                List<CommissionEntity> commissionList = commissionMap.get(projectId);
                Map<String, List<CommissionEntity>> amountListMap = commissionList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
                action.setBusinessAmount(amountListMap.get("业务提成") != null ? amountListMap.get("业务提成").get(0).getCmsAmount() : BigDecimal.ZERO);//业务提成
                action.setSamplingAmount(amountListMap.get("采样提成") != null ? amountListMap.get("采样提成").get(0).getCmsAmount() : BigDecimal.ZERO);//采样提成
                action.setReportAmount(amountListMap.get("报告提成") != null ? amountListMap.get("报告提成").get(0).getCmsAmount() : BigDecimal.ZERO);//报告提成
                action.setDetectionAmount(amountListMap.get("检测提成") != null ? amountListMap.get("检测提成").get(0).getCmsAmount() : BigDecimal.ZERO);//检测提成


            } else {
                action.setBusinessAmount(BigDecimal.ZERO);//业务提成
                action.setSamplingAmount(BigDecimal.ZERO);//采样提成
                action.setReportAmount(BigDecimal.ZERO);//报告提成
                action.setDetectionAmount(BigDecimal.ZERO);//检测提成
//                action.setShare(BigDecimal.ZERO);
//                action.setEstimatedProfit(BigDecimal.ZERO);
            }

            BigDecimal totalMoney = action.getTotalMoney();
            BigDecimal proportion = new BigDecimal(0.45);
            //综合成本公摊
            BigDecimal share = totalMoney.multiply(proportion);//项目金额*计算比例
            //项目净值
            BigDecimal netvalue = action.getNetvalue();

            //预计利润=项目净值-各类业绩提成-综合公摊
//            BigDecimal estimatedProfit = netvalue.subtract(share).subtract(action.getBusinessAmount()).subtract(action.getSamplingAmount()).subtract(action.getReportAmount()).subtract(action.getDetectionAmount());
//            action.setShare(share);
//            action.setEstimatedProfit(estimatedProfit);

        }
        ;

        return projectEntities;
    }


    /**
     * 我的项目分页列表
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectEntity> getMyPageList(Map<String, Object> params) {
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        pageUtil2.startPage();
        List<ProjectEntity> projectEntities = baseMapper.selectList(queryWrapper.orderByDesc("id"));
        List<Long> amountDateIds = projectEntities.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        List<ProjectAmountEntity> projectAmountEntityList;
        List<ProjectDateEntity> projectDateEntityList;
        Map<Long, List<ProjectAmountEntity>> projectAmountMap;
        Map<Long, List<ProjectDateEntity>> projectDateMap;
        if (amountDateIds.size() > 0) {
            projectAmountEntityList = projectAmountService.selectListByProjectIds(amountDateIds);
            projectDateEntityList = projectDateService.selectListByProjectIds(amountDateIds);
            projectAmountMap = projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        } else {
            projectAmountMap = new HashMap<>();//projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
            projectDateMap = new HashMap<>();//projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
        }

//        List<QueryProjectAmountDateVo> queryProjectAmountDateVoList = baseMapper.queryAllprojectAmountDate(queryWrapper);
        projectEntities.forEach(action -> {
            Long projectId = action.getId();
            List<AccountEntity> accountEntityList = accountService.listByProjectId(projectId);
            if (accountEntityList == null || accountEntityList.size() == 0) {
                accountEntityList = new ArrayList<>();
            }
            action.setAccountEntityList(accountEntityList);
            action.setProjectAmountEntity(projectAmountMap.get(projectId) == null ? new ProjectAmountEntity() : projectAmountMap.get(projectId).get(0));
            action.setProjectDateEntity(projectDateMap.get(projectId) == null ? new ProjectDateEntity() : projectDateMap.get(projectId).get(0));

        });

        return projectEntities;
    }


    /**
     * 实验室列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByChilden(Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String company = sysUserEntity.getSubjection();
        Long deptId = sysUserEntity.getDeptId();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        // 支持分流
        queryWrapper.like(StrUtil.isNotBlank(company), "company_order", company);
//        queryWrapper.and(wrapper ->
//                wrapper.like(!"杭州安联".equals(company), "company_order", company.substring(0,2)).or().like(!"杭州安联".equals(company), "business_source", company.substring(0,2))
//                .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
//        );
        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper.orderByDesc("id"));
        page.getRecords().forEach(action -> {
            Long projectId = action.getId();

            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity == null) {
                projectDateEntity = new ProjectDateEntity();
            }
            action.setProjectDateEntity(projectDateEntity);
        });

        return new PageUtils(page);
    }

    /**
     * 任务下发列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils taskDistribution(Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String subjection = (String) params.get("subjection");
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        // 支持分流
        if (StringUtils.isNotBlank(subjection)){
//            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(company), "company_order", company)
//                    .or().eq(StringUtils.isNotBlank(company), "business_source", company));
            queryWrapper.and(wrapper ->
                    wrapper.like( "company_order", subjection).or().like( "business_source", subjection)
                            .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
            );
        }

//        queryWrapper.and(wrapper ->
//                wrapper.like(!"杭州安联".equals(company), "company_order", company.substring(0,2)).or().like(!"杭州安联".equals(company), "business_source", company.substring(0,2))
//                .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
//        );
        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper.orderByDesc("id"));
        page.getRecords().forEach(action -> {
            Long projectId = action.getId();

            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity == null) {
                projectDateEntity = new ProjectDateEntity();
            }
            action.setProjectDateEntity(projectDateEntity);
        });

        return new PageUtils(page);
    }


    /**
     * 项目录入列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils projectEntry(Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String company = (String) params.get("subjection");
        Long deptId = sysUserEntity.getDeptId();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
//        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        // 支持分流
        if (StringUtils.isNotBlank(company)) {
//            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(company), "company_order", company)
//                    .or().eq(StringUtils.isNotBlank(company), "business_source", company));
            queryWrapper.and(wrapper ->
                wrapper.like(StringUtils.isNotBlank(company), "company_order", company).or().like(StringUtils.isNotBlank(company), "business_source", company)
                .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
            );
        }
//        queryWrapper.and(wrapper ->
//                wrapper.like(!"杭州安联".equals(company), "company_order", company.substring(0,2)).or().like(!"杭州安联".equals(company), "business_source", company.substring(0,2))
//                .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
//        );
//        queryWrapper.orderByDesc("id");
//        pageUtil2.startPage();
//        List<ProjectEntity> list = baseMapper.selectList(queryWrapper);
        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper.orderByDesc("id"));
        page.getRecords().forEach(action -> {
            Long projectId = action.getId();

            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity == null) {
                projectDateEntity = new ProjectDateEntity();
            }
            action.setProjectDateEntity(projectDateEntity);
        });

        return new PageUtils(page);
    }

    /**
     * 项目归档列表
     *
     * @param params
     * @return
     */
    public PageUtils queryMyProjectAchive(Map<String, Object> params) {
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        params.put("companyOrder", sysUserEntity.getSubjection());
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);

        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper.orderByAsc("identifier"));
        page.getRecords().forEach(action -> {
            Long projectId = action.getId();
            ProjecarchiveEntity projecarchive = projecarchiveService.getByProjectId(projectId);
            if (projecarchive != null) {
                action.setProjecarchiveEntity(projecarchive);
            } else {
                action.setProjecarchiveEntity(new ProjecarchiveEntity());
            }
            //企业概况
            CompanySurveyEntity companySurvey = companySurveyService.seleteByProjectId(projectId);
            if (companySurvey != null) {
                action.setCompanySurveyEntity(companySurvey);
            } else {
                action.setCompanySurveyEntity(new CompanySurveyEntity());
            }

            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity == null) {
                projectDateEntity = new ProjectDateEntity();
            }
            action.setProjectDateEntity(projectDateEntity);
        });

        return new PageUtils(page);
    }


    /**
     * 报告接收时间填写
     *
     * @param projectAchiveVo
     */
    public boolean signIssue(ProjectAchiveVo projectAchiveVo) {
        String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
        String[] jianpingTeam = new String[]{"检评", "职卫监督"};
//        String[] huanjing = newCommission String[]{"环境验收","环境应急预案","排污许可证申请","排污许可后管理","环保管家","应急预案","环境示范","排污许可"};
        Long projectId = projectAchiveVo.getId();
        ProjectEntity projectEntity = this.getById(projectId);
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        projectAchiveVo.setReportIssue(projectAchiveVo.getReportAccept());

        boolean a = true;
        if (projectAchiveVo.getReportIssue() != null) {
            if (projectEntity.getStatus() < 40) {
                projectEntity.setStatus(40);
                ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                proceduresEntity.setProjectId(projectEntity.getId());
                proceduresEntity.setStatus(40);
                projectProceduresService.save(proceduresEntity);
                this.updateById(projectEntity);
                if ("集团发展".equals(projectEntity.getBusinessSource())){
                    if (null!=projectEntity.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(projectEntity.getId());
                        abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                        abuSendNoteDTO.setCompany(projectEntity.getCompany());
                        abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                        abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                        abuSendNoteDTO.setStatus(projectEntity.getStatus());
                        iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                    }
                }
            }
            PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
            GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
            PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
            performanceNodeVo.setProjectId(projectAchiveVo.getId());
            performanceNodeVo.setReportIssue(projectAchiveVo.getReportIssue());
            if (Arrays.asList(jianpingTeam).contains(projectEntity.getType())) {
                performanceAllocationService.issueCommission(performanceNodeVo);
            } else if (Arrays.asList(pingjiaTeam).contains(projectEntity.getType())) {
                a = gradePointService.getCommissionIssue(performanceNodeVo);
            }
//            else if (Arrays.asList(huanjing).contains(projectEntity.getType())){
//                performanceAllocationService.issueHjCommission(performanceNodeVo);
//            }
        }
        if (projectAchiveVo.getReportAccept() != null) {
            projectDateEntity.setReportAccept(projectAchiveVo.getReportAccept());
            projectDateEntity.setReportIssue(projectAchiveVo.getReportAccept());
        }
        projectDateService.updateById(projectDateEntity);

        return a;
    }


    /**
     * 项目信息导出
     *
     * @param params
     * @return
     */
    public List<ProjectEntity> exportOut(Map<String, Object> params) {
        String subjection = (String) params.get("subjection");
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
        }
        List<ProjectEntity> list = baseMapper.selectList(queryWrapper);
        List<Long> projectIdList = list.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        List<ProjectAmountEntity> projectAmountEntityList = projectAmountService.selectListByProjectIds(projectIdList);
        List<ProjectDateEntity> projectDateEntityList = projectDateService.selectListByProjectIds(projectIdList);
        Map<Long, List<ProjectAmountEntity>> projectAmountMap = projectAmountEntityList.stream().collect(Collectors.groupingBy(ProjectAmountEntity::getProjectId));
        Map<Long, List<ProjectDateEntity>> projectDateMap = projectDateEntityList.stream().collect(Collectors.groupingBy(ProjectDateEntity::getProjectId));
//        list.forEach(action -> {
//            Long projectId = action.getId();
////            ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
////            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
//
//            action.setProjectAmountEntity(projectAmountMap.get(projectId) == null ? new ProjectAmountEntity() : projectAmountMap.get(projectId).get(0));
//            action.setProjectDateEntity(projectDateMap.get(projectId) == null ? new ProjectDateEntity() : projectDateMap.get(projectId).get(0));
//        });

        List<Long> projectIds = list.stream().map(ProjectEntity::getId).distinct().collect(Collectors.toList());
        Map<Long, List<CommissionEntity>> commissionMap = new HashMap<>();
        if (projectIds != null && projectIds.size() > 0) {
            CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);
            List<CommissionEntity> commissionEntityList = commissionService.getCommissionListByProjectId(projectIds);
            if (commissionEntityList != null && commissionEntityList.size() > 0) {
                commissionMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getProjectId));
            }
        } else {
            commissionMap = new HashMap<>();
        }
//        projectEntities.forEach(action -> {
        for (ProjectEntity action : list
        ) {
//        projectEntities.forEach(action -> {
            Long projectId = action.getId();
            List<AccountEntity> accountEntityList = accountService.listByProjectId(projectId);
            if (accountEntityList == null || accountEntityList.size() == 0) {
                accountEntityList = new ArrayList<>();
            }
            action.setAccountEntityList(accountEntityList);
            action.setProjectAmountEntity(projectAmountMap.get(projectId) == null ? new ProjectAmountEntity() : projectAmountMap.get(projectId).get(0));
            action.setProjectDateEntity(projectDateMap.get(projectId) == null ? new ProjectDateEntity() : projectDateMap.get(projectId).get(0));
            BigDecimal businessAmount = BigDecimal.ZERO;
            BigDecimal samplingAmount = BigDecimal.ZERO;
            BigDecimal reportAmount = BigDecimal.ZERO;
            BigDecimal detectionAmount = BigDecimal.ZERO;
            if (commissionMap.get(projectId) != null) {
                List<CommissionEntity> commissionList = commissionMap.get(projectId);
                Map<String, List<CommissionEntity>> amountListMap = commissionList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
                businessAmount = amountListMap.get("业务提成") != null ? amountListMap.get("业务提成").get(0).getCmsAmount() : BigDecimal.ZERO;
                samplingAmount = amountListMap.get("采样提成") != null ? amountListMap.get("采样提成").get(0).getCmsAmount() : BigDecimal.ZERO;
                reportAmount = amountListMap.get("报告提成") != null ? amountListMap.get("报告提成").get(0).getCmsAmount() : BigDecimal.ZERO;
                detectionAmount = amountListMap.get("检测提成") != null ? amountListMap.get("检测提成").get(0).getCmsAmount() : BigDecimal.ZERO;
                action.setBusinessAmount(businessAmount);//业务提成
                action.setSamplingAmount(samplingAmount);//采样提成
                action.setReportAmount(reportAmount);//报告提成
                action.setDetectionAmount(detectionAmount);//检测提成


            } else {
                action.setBusinessAmount(businessAmount);//业务提成
                action.setSamplingAmount(samplingAmount);//采样提成
                action.setReportAmount(reportAmount);//报告提成
                action.setDetectionAmount(detectionAmount);//检测提成
//                action.setShare(BigDecimal.ZERO);
//                action.setEstimatedProfit(BigDecimal.ZERO);
            }

            BigDecimal totalMoney = StringUtils.checkValNotNull(action.getTotalMoney())? action.getTotalMoney():BigDecimal.ZERO;
            BigDecimal proportion = new BigDecimal(0.45);
            //综合成本公摊
            BigDecimal share = totalMoney.multiply(proportion);//项目金额*计算比例
            //项目净值
            BigDecimal netvalue = StringUtils.checkValNotNull(action.getNetvalue())?action.getNetvalue():BigDecimal.ZERO ;

            //预计利润=项目净值-各类业绩提成-综合公摊
            BigDecimal estimatedProfit = netvalue.subtract(share).subtract(businessAmount).subtract(samplingAmount).subtract(reportAmount).subtract(detectionAmount);
            action.setShare(share);
            action.setEstimatedProfit(estimatedProfit);

        }
        ;
        return list;
    }

    /**
     * 项目信息初始化
     *
     * @param project
     * @param saveMoney true金额回填到合同，false不回填
     * @return
     */
    public void saveProject(ProjectEntity project, boolean saveMoney) {
        String[] PJtypes1 = new String[]{"预评", "专篇", "控评", "现状"};
        //用户名
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        Long userid = sysUserEntity.getUserId();
        String username = sysUserEntity.getUsername();
        if (!(project.getDeptId() != null && project.getDeptId() > 0)) {
            Long deptId = sysUserEntity.getDeptId();
            project.setDeptId(deptId);
        }
        project.setUserid(userid);
        project.setUsername(username);
//        project.setNosettlementMoney(project.getTotalMoney());

        String idenfier = project.getIdentifier().trim();
        project.setIdentifier(idenfier);
        baseMapper.insert(project);
        Long contractId = project.getContractId();
        if (projectAmountService.notExistPlanByProject(project.getId())) {
            ProjectAmountEntity projectAmountEntity = new ProjectAmountEntity();
            projectAmountEntity.setCommission(project.getCommission());
            projectAmountEntity.setCommissionOutstanding(project.getCommission());
            BigDecimal commissionRatio = new BigDecimal(0);
            if (project.getTotalMoney() != null && project.getTotalMoney().compareTo(BigDecimal.ZERO) > 0 && project.getCommission() != null) {
                commissionRatio = project.getCommission().divide(project.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
            }
            projectAmountEntity.setCommissionRatio(commissionRatio);
            projectAmountEntity.setEvaluationFee(project.getEvaluationFee());
            projectAmountEntity.setEvaluationOutstanding(project.getEvaluationFee());
            projectAmountEntity.setNetvalue(project.getNetvalue());
            projectAmountEntity.setOtherExpenses(project.getOtherExpenses());
            projectAmountEntity.setOtherExpensesOutstanding(project.getOtherExpenses());
            projectAmountEntity.setNosettlementMoney(project.getTotalMoney());
            projectAmountEntity.setServiceCharge(project.getServiceCharge());
            projectAmountEntity.setServiceChargeOutstanding(project.getServiceCharge());
            projectAmountEntity.setSubprojectFee(project.getSubprojectFee());
            projectAmountEntity.setSubprojectOutstanding(project.getSubprojectFee());
            projectAmountEntity.setTotalMoney(project.getTotalMoney());
            projectAmountEntity.setContractId(project.getContractId());
            projectAmountEntity.setProjectId(project.getId());
            projectAmountService.save(projectAmountEntity);
        } else {
            R.error("项目金额信息中已存在此项目信息，不能重复录入！");
        }
        if (projectDateService.notExistPlanByProject(project.getId())) {
            ProjectDateEntity projectDateEntity = new ProjectDateEntity();
            projectDateEntity.setProjectId(project.getId());
            projectDateEntity.setSignDate(project.getSignDate());
            projectDateEntity.setEntrustDate(project.getEntrustDate());
            projectDateEntity.setClaimEndDate(project.getClaimEndDate());
            projectDateService.save(projectDateEntity);
        }else {
            R.error("项目日期表信息已存在此项目信息，不能重复录入！");
        }
        if (saveMoney) {//true金额回填到合同，false不回填
            projectAmountService.saveMoneyByContractId(contractId);
        }

        ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
        proceduresEntity.setProjectId(project.getId());
        proceduresEntity.setStatus(1);
        projectProceduresService.save(proceduresEntity);

        if (project.getStatus() != 1) {
            ProjectProceduresEntity proceduresEntit = new ProjectProceduresEntity();
            proceduresEntit.setProjectId(project.getId());
            proceduresEntit.setStatus(project.getStatus());
            projectProceduresService.save(proceduresEntit);
        }


        //新增是判断状态值是否为下发，若是则初始化任务排单信息以及初始化企业单位概况
        if (project.getStatus().equals(2)) {
            Integer urgent = 0;
            if (project.getUrgent() != null) {
                urgent = project.getUrgent();
            }

            Long projectId = project.getId();            //项目ID
            ProjectEntity projectEntity = baseMapper.selectById(projectId);//获取项目信息对象
            if (projectEntity.getStatus().equals(2) && projectEntity.getType().equals("来样检测")) {
                project.setStatus(10);
            }
            this.updateById(projectEntity);
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setTaskReleaseDate(new Date());
            projectDateService.updateById(projectDateEntity);

            if (Arrays.asList(PJtypes1).contains(project.getType())) {
                if (alCompanySurveyService.notExistPlanByProject(projectId)) {
                    AlCompanySurveyEntity alCompanySurveyEntity = new AlCompanySurveyEntity();
                    alCompanySurveyEntity.setCompany(project.getCompany());
                    alCompanySurveyEntity.setIdentifier(project.getIdentifier());
                    alCompanySurveyEntity.setProjectName(project.getProjectName());
                    alCompanySurveyEntity.setType(project.getType());
                    alCompanySurveyEntity.setProjectId(projectId);
                    alCompanySurveyEntity.setOfficeAddress(project.getOfficeAddress());
                    alCompanySurveyEntity.setContact(project.getContact());
                    alCompanySurveyEntity.setTelephone(project.getTelephone());
                    alCompanySurveyService.save(alCompanySurveyEntity);
                } else {
                    R.error("评价用人单位概况调查中已存在此项目信息，不能重复录入！");
                }
            }

            //生成用人单位概况调查初始化信息
            if (companySurveyService.notExistCompanySurveyByProject(projectId)) {
//        		ProjectEntity projectEntity = projectService.getById(projectId);//获取项目信息对象
                CompanyEntity companyEntity = companyService.getById(projectEntity.getCompanyId());//获取企业信息

                //将企业信息与项目信息赋值到单位概况中作为初始化数据
                CompanySurveyEntity companySurvey = new CompanySurveyEntity();
                companySurvey.setProjectId(project.getId());        //项目ID
                companySurvey.setCompany(projectEntity.getCompany());
                companySurvey.setIdentifier(projectEntity.getIdentifier());
                companySurvey.setProjectName(projectEntity.getProjectName());
                companySurvey.setOfficeAddress(projectEntity.getOfficeAddress());
                companySurvey.setEntrustAddress(projectEntity.getEntrustOfficeAddress());
                companySurvey.setEntrustCompany(projectEntity.getEntrustCompany());
                companySurvey.setContact(projectEntity.getContact());
                companySurvey.setTelephone(projectEntity.getTelephone());


                companySurvey.setIndustryCategory(companyEntity.getIndustryCategory());
                companySurvey.setRiskLevel(companyEntity.getRiskLevel());
                companySurvey.setLaborQuota(companyEntity.getPopulation());    //劳动定员(人数)
                companySurvey.setPopulation(companyEntity.getPopulation());    //人数
                companySurvey.setProduct(companyEntity.getProducts());
                companySurvey.setYield(companyEntity.getYields());
                companySurvey.setRegisteredAddress(companyEntity.getAddress());
                companySurvey.setUnifiedCode(companyEntity.getCode());

                companySurveyService.save(companySurvey);
            } else {
                R.error("用人单位概况调查中已存在此项目信息，不能重复下发！");
            }
        }
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

    }


    /**
     * 项目修改连带项目信息日期表和项目金额表一起修改
     *
     * @param projectEntity
     */
    public void updateProject(ProjectEntity projectEntity) {
        Long projectId = projectEntity.getId();
        Long contractId = projectEntity.getContractId();
        String[] PJtypes1 = new String[]{"预评", "专篇", "控评", "现状"};
        ProjectDateEntity projectDateEntity = projectEntity.getProjectDateEntity();
        ProjectEntity oldProject = this.getById(projectId);
        if (projectDateEntity != null) {
            if (projectDateEntity.getReportIssue() != null && projectEntity.getStatus() < 40) {
                projectEntity.setStatus(40);
                if ("集团发展".equals(projectEntity.getBusinessSource())){
                    if (null!=projectEntity.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(projectEntity.getId());
                        abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                        abuSendNoteDTO.setCompany(projectEntity.getCompany());
                        abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                        abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                        abuSendNoteDTO.setStatus(projectEntity.getStatus());
                        iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                    }
                }
                ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                if (proceduresEntity != null) {
                    projectProceduresService.removeById(proceduresEntity.getId());
                    procedures.setProjectId(projectId);
                    procedures.setStatus(40);
                    projectProceduresService.save(procedures);
                } else {
                    procedures.setProjectId(projectId);
                    procedures.setStatus(40);
                    projectProceduresService.save(procedures);
                }
            }

            if (projectDateEntity.getReportSend() != null && projectEntity.getStatus() < 50) {
                projectEntity.setStatus(50);
                if ("集团发展".equals(projectEntity.getBusinessSource())){
                    if (null!=projectEntity.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(projectEntity.getId());
                        abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                        abuSendNoteDTO.setCompany(projectEntity.getCompany());
                        abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                        abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                        abuSendNoteDTO.setStatus(projectEntity.getStatus());
                        iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                    }
                }
                ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 50);
                ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                if (proceduresEntity != null) {
                    projectProceduresService.removeById(proceduresEntity.getId());
                    procedures.setProjectId(projectId);
                    procedures.setStatus(50);
                    projectProceduresService.save(procedures);
                } else {
                    procedures.setProjectId(projectId);
                    procedures.setStatus(50);
                    projectProceduresService.save(procedures);
                }
            }
            projectDateEntity.setEntrustDate(projectEntity.getEntrustDate());
            projectDateEntity.setClaimEndDate(projectEntity.getClaimEndDate());
            projectDateService.updateById(projectDateEntity);
        } else {
            projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity.getReportIssue() != null && projectEntity.getStatus() < 40) {
                projectEntity.setStatus(40);
                if ("集团发展".equals(projectEntity.getBusinessSource())){
                    if (null!=projectEntity.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(projectEntity.getId());
                        abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                        abuSendNoteDTO.setCompany(projectEntity.getCompany());
                        abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                        abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                        abuSendNoteDTO.setStatus(projectEntity.getStatus());
                        iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                    }
                }
                ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                if (proceduresEntity != null) {
                    projectProceduresService.removeById(proceduresEntity.getId());
                    procedures.setProjectId(projectId);
                    procedures.setStatus(40);
                    projectProceduresService.save(procedures);
                } else {
                    procedures.setProjectId(projectId);
                    procedures.setStatus(40);
                    projectProceduresService.save(procedures);
                }
            }

            if (projectDateEntity.getReportSend() != null && projectEntity.getStatus() < 50) {
                projectEntity.setStatus(50);
                if ("集团发展".equals(projectEntity.getBusinessSource())){
                    if (null!=projectEntity.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(projectEntity.getId());
                        abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                        abuSendNoteDTO.setCompany(projectEntity.getCompany());
                        abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                        abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                        abuSendNoteDTO.setStatus(projectEntity.getStatus());
                        iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                    }
                }
                ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 50);
                ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                if (proceduresEntity != null) {
                    projectProceduresService.removeById(proceduresEntity.getId());
                    procedures.setProjectId(projectId);
                    procedures.setStatus(50);
                    projectProceduresService.save(procedures);
                } else {
                    procedures.setProjectId(projectId);
                    procedures.setStatus(50);
                    projectProceduresService.save(procedures);
                }
            }
            projectDateEntity.setEntrustDate(projectEntity.getEntrustDate());
            projectDateEntity.setClaimEndDate(projectEntity.getClaimEndDate());
            projectDateService.updateById(projectDateEntity);
        }

        ProjectAmountEntity projectAmountEntity = projectEntity.getProjectAmountEntity();


        if (projectAmountEntity != null) {
            projectEntity.setTotalMoney(projectAmountEntity.getTotalMoney());
            projectEntity.setNetvalue(projectAmountEntity.getNetvalue());
            this.updateById(projectEntity);

            Float totalMoneySettled = 0f;            //已结算金额
            Float commissionSettled = 0f;            //已结算佣金
            Float evaluationSettled = 0f;            //已结算评审费
            Float serviceChargeSettled = 0f;        //已结算服务费
            Float subprojectSettled = 0f;            //已结算分包费
            Float otherExpensesSettled = 0f;        //已结算其他支出
            Float virtualTax = 0f;         //虚拟税费
            Float invoiceMoney = 0f;           //已开票金额

            //type的枚举值有: 1项目款、2发票、3佣金、4评审费、5服务费、6差旅招待提成、7业务提成、8采样提成、9检测提成、10报告编制提成、11报告评审提成、12采样提成(补采)、13分包费、14其它支出
            List<AccountEntity> accountList = accountService.listByProjectId(projectId);//根据项目ID获取收付款记录

            for (AccountEntity accountEntity : accountList) {
                if (accountEntity.getAcVirtualTax() != null && accountEntity.getAcVirtualTax() > 0) {
                    virtualTax = virtualTax + accountEntity.getAcVirtualTax();
                }

                invoiceMoney = invoiceMoney + accountEntity.getInvoiceAmount();
                switch (accountEntity.getAcType()) {
                    case 1:
                        totalMoneySettled = totalMoneySettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 3:
                        commissionSettled = commissionSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 4:
                        evaluationSettled = evaluationSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 5:
                        serviceChargeSettled = serviceChargeSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 13:
                        subprojectSettled = subprojectSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 14:
                        otherExpensesSettled = otherExpensesSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;

                    default:
                        break;
                }
            }

            BigDecimal totalMoneyOutstanding = Number2Money.subtance(projectAmountEntity.getTotalMoney(), totalMoneySettled);    //未结算金额 项目金额totalMoney-已收款金额receiptMoney
            //支付款表中支出存储的应为负数，所以采样add
            BigDecimal commissionOutstanding = Number2Money.subtance(projectAmountEntity.getCommission(), commissionSettled);        //未结算佣金
            //System.out.println(project.getCommission()+ ",commissionOutstanding="+commissionOutstanding);
            //BigDecimal commissionOutstanding = newCommission BigDecimal(0);		//未结算佣金
            BigDecimal commissionRatio = new BigDecimal(0);//佣金比例,佣金/总金额  "0.00%"
            if (projectAmountEntity.getTotalMoney() != null && projectAmountEntity.getTotalMoney().compareTo(BigDecimal.ZERO) > 0) {
                commissionRatio = projectAmountEntity.getCommission().divide(projectAmountEntity.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
            }

            BigDecimal evaluationOutstanding = Number2Money.subtance(projectAmountEntity.getEvaluationFee(), evaluationSettled);        //未结算评审费
            BigDecimal serviceChargeOutstanding = Number2Money.subtance(projectAmountEntity.getServiceCharge(), serviceChargeSettled);        //未结算服务费
            BigDecimal subprojectOutstanding = Number2Money.subtance(projectAmountEntity.getSubprojectFee(), subprojectSettled);        //未结算分包费
            BigDecimal otherExpensesOutstanding = Number2Money.subtance(projectAmountEntity.getOtherExpenses(), otherExpensesSettled);        //未结算其他支出

            projectAmountEntity.setReceiptMoney(new BigDecimal(totalMoneySettled.toString()));            //已结算金额 已收款金额(元)

            projectAmountEntity.setNosettlementMoney(totalMoneyOutstanding);
            projectAmountEntity.setCommissionOutstanding(commissionOutstanding);
            projectAmountEntity.setCommissionRatio(commissionRatio);
            projectAmountEntity.setEvaluationOutstanding(evaluationOutstanding);
            projectAmountEntity.setServiceChargeOutstanding(serviceChargeOutstanding);
            projectAmountEntity.setSubprojectOutstanding(subprojectOutstanding);
            projectAmountEntity.setOtherExpensesOutstanding(otherExpensesOutstanding);

            projectAmountEntity.setVirtualTax(new BigDecimal(virtualTax.toString()));//虚拟税费
            projectAmountEntity.setInvoiceMoney(new BigDecimal(invoiceMoney.toString()));//已开票金额
            projectAmountService.updateById(projectAmountEntity);
        } else {
            projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
            projectEntity.setTotalMoney(projectAmountEntity.getTotalMoney());
            projectEntity.setNetvalue(projectAmountEntity.getNetvalue());
            this.updateById(projectEntity);
            Float totalMoneySettled = 0f;            //已结算金额
            Float commissionSettled = 0f;            //已结算佣金
            Float evaluationSettled = 0f;            //已结算评审费
            Float serviceChargeSettled = 0f;        //已结算服务费
            Float subprojectSettled = 0f;            //已结算分包费
            Float otherExpensesSettled = 0f;        //已结算其他支出
            Float virtualTax = 0f;         //虚拟税费
            Float invoiceMoney = 0f;           //已开票金额

            //type的枚举值有: 1项目款、2发票、3佣金、4评审费、5服务费、6差旅招待提成、7业务提成、8采样提成、9检测提成、10报告编制提成、11报告评审提成、12采样提成(补采)、13分包费、14其它支出
            List<AccountEntity> accountList = accountService.listByProjectId(projectId);//根据项目ID获取收付款记录

            for (AccountEntity accountEntity : accountList) {
                if (accountEntity.getAcVirtualTax() != null && accountEntity.getAcVirtualTax() > 0) {
                    virtualTax = virtualTax + accountEntity.getAcVirtualTax();
                }

                invoiceMoney = invoiceMoney + accountEntity.getInvoiceAmount();
                switch (accountEntity.getAcType()) {
                    case 1:
                        totalMoneySettled = totalMoneySettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 3:
                        commissionSettled = commissionSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 4:
                        evaluationSettled = evaluationSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 5:
                        serviceChargeSettled = serviceChargeSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 13:
                        subprojectSettled = subprojectSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;
                    case 14:
                        otherExpensesSettled = otherExpensesSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                        break;

                    default:
                        break;
                }
            }

            BigDecimal totalMoneyOutstanding = Number2Money.subtance(projectAmountEntity.getTotalMoney(), totalMoneySettled);    //未结算金额 项目金额totalMoney-已收款金额receiptMoney
            //支付款表中支出存储的应为负数，所以采样add
            BigDecimal commissionOutstanding = Number2Money.subtance(projectAmountEntity.getCommission(), commissionSettled);        //未结算佣金
            //System.out.println(project.getCommission()+ ",commissionOutstanding="+commissionOutstanding);
            //BigDecimal commissionOutstanding = newCommission BigDecimal(0);		//未结算佣金
            BigDecimal commissionRatio = new BigDecimal(0);//佣金比例,佣金/总金额  "0.00%"
            if (projectAmountEntity.getTotalMoney() != null && projectAmountEntity.getTotalMoney().compareTo(BigDecimal.ZERO) > 0) {
                commissionRatio = projectAmountEntity.getCommission().divide(projectAmountEntity.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
            }

            BigDecimal evaluationOutstanding = Number2Money.subtance(projectAmountEntity.getEvaluationFee(), evaluationSettled);        //未结算评审费
            BigDecimal serviceChargeOutstanding = Number2Money.subtance(projectAmountEntity.getServiceCharge(), serviceChargeSettled);        //未结算服务费
            BigDecimal subprojectOutstanding = Number2Money.subtance(projectAmountEntity.getSubprojectFee(), subprojectSettled);        //未结算分包费
            BigDecimal otherExpensesOutstanding = Number2Money.subtance(projectAmountEntity.getOtherExpenses(), otherExpensesSettled);        //未结算其他支出

            projectAmountEntity.setReceiptMoney(new BigDecimal(totalMoneySettled.toString()));            //已结算金额 已收款金额(元)

            projectAmountEntity.setNosettlementMoney(totalMoneyOutstanding);
            projectAmountEntity.setCommissionOutstanding(commissionOutstanding);
            projectAmountEntity.setCommissionRatio(commissionRatio);
            projectAmountEntity.setEvaluationOutstanding(evaluationOutstanding);
            projectAmountEntity.setServiceChargeOutstanding(serviceChargeOutstanding);
            projectAmountEntity.setSubprojectOutstanding(subprojectOutstanding);
            projectAmountEntity.setOtherExpensesOutstanding(otherExpensesOutstanding);

            projectAmountEntity.setVirtualTax(new BigDecimal(virtualTax.toString()));//虚拟税费
            projectAmountEntity.setInvoiceMoney(new BigDecimal(invoiceMoney.toString()));//已开票金额
            projectAmountService.updateById(projectAmountEntity);
        }
        if(!oldProject.getContractId().equals(contractId)){
            projectAmountService.saveMoneyByContractId(oldProject.getContractId());
            projectAmountService.saveMoneyByContractId(contractId);
        }else {
            projectAmountService.saveMoneyByContractId(contractId);
        }



        if (Arrays.asList(PJtypes1).contains(projectEntity.getType())) {
            AlCompanySurveyEntity alCompanySurveyEntity = alCompanySurveyService.getOneByProjectId(projectId);
            if (alCompanySurveyEntity != null) {
                alCompanySurveyEntity.setIdentifier(projectEntity.getIdentifier());
                alCompanySurveyEntity.setType(projectEntity.getType());
                alCompanySurveyEntity.setProjectName(projectEntity.getProjectName());
                alCompanySurveyEntity.setCompany(projectEntity.getCompany());
                alCompanySurveyEntity.setOfficeAddress(projectEntity.getOfficeAddress());
                alCompanySurveyEntity.setEntrustCompany(projectEntity.getEntrustCompany());
                alCompanySurveyEntity.setEntrustOfficeAddress(projectEntity.getEntrustOfficeAddress());
                alCompanySurveyEntity.setContact(projectEntity.getContact());
                alCompanySurveyEntity.setTelephone(projectEntity.getTelephone());
                alCompanySurveyService.updateById(alCompanySurveyEntity);
            }

        } else {
            CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);
            if (companySurveyEntity != null) {

                companySurveyEntity.setIdentifier(projectEntity.getIdentifier());
//                companySurveyEntity.setTestNature(projectEntity.getTestNature());
                companySurveyEntity.setProjectName(projectEntity.getProjectName());
                companySurveyEntity.setCompany(projectEntity.getCompany());
                companySurveyEntity.setOfficeAddress(projectEntity.getOfficeAddress());
                companySurveyEntity.setEntrustCompany(projectEntity.getEntrustCompany());
                companySurveyEntity.setEntrustAddress(projectEntity.getEntrustOfficeAddress());
                companySurveyEntity.setContact(projectEntity.getContact());
                companySurveyEntity.setTelephone(projectEntity.getTelephone());
                companySurveyService.updateById(companySurveyEntity);
            }

        }
    }


    /**
     * 通过项目编号获取项目id
     *
     * @param identifier
     * @return
     */
    public Long getIdByIdentifier(String identifier) {
        ProjectEntity projectEntity = baseMapper.selectOne(new QueryWrapper<ProjectEntity>()
                .eq("identifier", identifier)
        );
        Long id;
        if (projectEntity!=null){
            id = projectEntity.getId();
        }else {
            id = null;
        }

        return id;
    }

    /**
     * 根据查询条件统计金额
     *
     * @param params
     * @return
     */
    @Override
    public MoneyVo sumMoneyByParams(Map<String, Object> params) {
//        MoneyVo stringBigDecimalMap = baseMapper.sumMoneyByMyWrapper(queryWrapperByParamsAuth2(params).apply("ap.id = p1.project_id"));
//        if (stringBigDecimalMap == null) {
//            stringBigDecimalMap = new MoneyVo();
//        }else {
//
//
//            String subjection = (String) params.get("subjection");
//            QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParamsAuth2(params);
//            if (StringUtils.isNotBlank(subjection)) {
//                // 支持分流
//                queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
//                        .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
//            }
//            List<ProjectEntity> projectEntities = baseMapper.selectList(queryWrapper.select("id").orderByDesc("id"));
//            if (CollectionUtils.isNotEmpty(projectEntities)) {
//                List<Long> ids = projectEntities.stream().distinct().map(ProjectEntity::getId).collect(Collectors.toList());
//                CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);
//
//                QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
//                objectQueryWrapper.in("project_id", ids);
//                List<CommissionEntity> commissionEntityList = baseMapper.selectCommissionList(objectQueryWrapper);
//                if (CollectionUtils.isNotEmpty(commissionEntityList)) {
//
//                    Map<String, List<CommissionEntity>> amountListMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
//                    stringBigDecimalMap.setBusinessAmount(amountListMap.get("业务提成") != null ? amountListMap.get("业务提成").get(0).getCmsAmount() : BigDecimal.ZERO);//业务提成
//                    stringBigDecimalMap.setSamplingAmount(amountListMap.get("采样提成") != null ? amountListMap.get("采样提成").get(0).getCmsAmount() : BigDecimal.ZERO);//采样提成
//                    stringBigDecimalMap.setReportAmount(amountListMap.get("报告提成") != null ? amountListMap.get("报告提成").get(0).getCmsAmount() : BigDecimal.ZERO);//报告提成
//                    stringBigDecimalMap.setDetectionAmount(amountListMap.get("检测提成") != null ? amountListMap.get("检测提成").get(0).getCmsAmount() : BigDecimal.ZERO);//检测提成
//                } else {
//                    stringBigDecimalMap.setBusinessAmount(BigDecimal.ZERO);//业务提成
//                    stringBigDecimalMap.setSamplingAmount(BigDecimal.ZERO);//采样提成
//                    stringBigDecimalMap.setReportAmount(BigDecimal.ZERO);//报告提成
//                    stringBigDecimalMap.setDetectionAmount(BigDecimal.ZERO);//检测提成
//                }
//
//
//
//
//            }
//            //项目总金额
//            BigDecimal totalMoney = stringBigDecimalMap.getToltalMoney();
//            //项目净值
//            BigDecimal netvalue = stringBigDecimalMap.getNetvalue();
//            //业务提成
//            BigDecimal businessAmount = stringBigDecimalMap.getBusinessAmount();
//            //采样提成
//            BigDecimal samplingAmount = stringBigDecimalMap.getSamplingAmount();
//            //报告提成
//            BigDecimal reportAmount = stringBigDecimalMap.getReportAmount();
//            //检测提成
//            BigDecimal detectionAmount = stringBigDecimalMap.getDetectionAmount();
//            BigDecimal proportion = new BigDecimal(0.45);
//            //综合成本公摊
//            BigDecimal share = totalMoney.multiply(proportion);//项目金额*计算比例
//
//            //预计利润=项目净值-各类业绩提成-综合公摊
//            BigDecimal estimatedProfit = netvalue.subtract(share).subtract(businessAmount).subtract(samplingAmount).subtract(reportAmount).subtract(detectionAmount);
//            stringBigDecimalMap.setShare(share);
//            stringBigDecimalMap.setEstimatedProfit(estimatedProfit);
//        }
//        return stringBigDecimalMap;

        String subjection = (String) params.get("subjection");
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams2(params);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
        }
        MoneyVo stringBigDecimalMap = baseMapper.sumMoneyByMyWrapper(queryWrapper.apply("ap.id = p1.project_id"));
        if (stringBigDecimalMap == null) {
            stringBigDecimalMap = new MoneyVo();
        }
        return stringBigDecimalMap;
    }

    /**
     * 收付款项页面支持
     *
     * @param params
     * @return
     */
    @Override
    public MoneyVo getPaymentReceiptSumMoneyByParams(Map<String, Object> params) {
        String subjection = (String) params.get("subjection");
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
//            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
//                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection));
        }
//        QueryWrapper<ProjectEntity> queryWrapper2 = queryWrapperByParams2(params);
        List<ProjectEntity> projectEntities = baseMapper.selectList(queryWrapper.select("id").orderByDesc("id"));
//        List<ProjectEntity> projectEntityList = baseMapper.selectList(queryWrapper);
        List<Long> idList = projectEntities.stream().map(ProjectEntity::getId).collect(Collectors.toList());
        QueryWrapper<ProjectAmountEntity> queryParamsWrapper = new QueryWrapper<ProjectAmountEntity>();
        if (idList.size() > 0) {
            queryParamsWrapper.in("project_id", idList);
        } else {
            queryParamsWrapper.eq("project_id", 0);
        }

        MoneyVo stringBigDecimalMap = baseMapper.sumMoneyByMyWrapper2(queryParamsWrapper);

//        String subjection = (String) params.get("subjection");
//        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParamsAuth(params);
//        if (StringUtils.isNotBlank(subjection)) {
//            // 支持分流
//            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
//                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
//        }
//        MoneyVo stringBigDecimalMap = baseMapper.sumMoneyByMyWrapper(queryWrapper.apply("ap.id = p1.project_id"));

        if (stringBigDecimalMap == null) {
            stringBigDecimalMap = new MoneyVo();
        } else {
/**/
            if (CollectionUtils.isNotEmpty(projectEntities)) {
                List<Long> ids = projectEntities.stream().distinct().map(ProjectEntity::getId).collect(Collectors.toList());
                CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);

                QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
                objectQueryWrapper.in("project_id", ids);
                List<CommissionEntity> commissionEntityList = baseMapper.selectCommissionList(objectQueryWrapper);
                if (CollectionUtils.isNotEmpty(commissionEntityList)) {

                    Map<String, List<CommissionEntity>> amountListMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
                    stringBigDecimalMap.setBusinessAmount(amountListMap.get("业务提成") != null ? amountListMap.get("业务提成").get(0).getCmsAmount() : BigDecimal.ZERO);//业务提成
                    stringBigDecimalMap.setSamplingAmount(amountListMap.get("采样提成") != null ? amountListMap.get("采样提成").get(0).getCmsAmount() : BigDecimal.ZERO);//采样提成
                    stringBigDecimalMap.setReportAmount(amountListMap.get("报告提成") != null ? amountListMap.get("报告提成").get(0).getCmsAmount() : BigDecimal.ZERO);//报告提成
                    stringBigDecimalMap.setDetectionAmount(amountListMap.get("检测提成") != null ? amountListMap.get("检测提成").get(0).getCmsAmount() : BigDecimal.ZERO);//检测提成
                } else {
                    stringBigDecimalMap.setBusinessAmount(BigDecimal.ZERO);//业务提成
                    stringBigDecimalMap.setSamplingAmount(BigDecimal.ZERO);//采样提成
                    stringBigDecimalMap.setReportAmount(BigDecimal.ZERO);//报告提成
                    stringBigDecimalMap.setDetectionAmount(BigDecimal.ZERO);//检测提成
                }




            }
            //项目总金额
            BigDecimal totalMoney = stringBigDecimalMap.getToltalMoney();
            //项目净值
            BigDecimal netvalue = stringBigDecimalMap.getNetvalue();
            //业务提成
            BigDecimal businessAmount = stringBigDecimalMap.getBusinessAmount();
            //采样提成
            BigDecimal samplingAmount = stringBigDecimalMap.getSamplingAmount();
            //报告提成
            BigDecimal reportAmount = stringBigDecimalMap.getReportAmount();
            //检测提成
            BigDecimal detectionAmount = stringBigDecimalMap.getDetectionAmount();
            BigDecimal proportion = new BigDecimal(0.45);
            //综合成本公摊
            BigDecimal share = totalMoney.multiply(proportion);//项目金额*计算比例

            //预计利润=项目净值-各类业绩提成-综合公摊
            BigDecimal estimatedProfit = netvalue.subtract(share).subtract(businessAmount).subtract(samplingAmount).subtract(reportAmount).subtract(detectionAmount);
            stringBigDecimalMap.setShare(share);
            stringBigDecimalMap.setEstimatedProfit(estimatedProfit);
        }

        return stringBigDecimalMap;
    }

    /**
     * 根据查询条件统计金额
     *
     * @param params
     * @return
     */
    public MoneyVo sumMoneyByParams2(Map<String, Object> params) {
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams2(params);
        List<ProjectEntity> projectEntityList = baseMapper.selectList(queryWrapper);
        List<Long> idList = projectEntityList.stream().map(ProjectEntity::getId).collect(Collectors.toList());
        QueryWrapper<ProjectAmountEntity> queryParamsWrapper = new QueryWrapper<ProjectAmountEntity>();
        if (idList.size() > 0) {
            queryParamsWrapper.in("project_id", idList);
        } else {
            queryParamsWrapper.eq("project_id", 0);
        }
//        System.out.println("============"+idList);
        MoneyVo map = baseMapper.sumMoneyByMyWrapper2(queryParamsWrapper);
        return map;
    }

    /**
     * 根据项目编号查询是否已经存在于项目信息中
     *
     * @param identifier 项目编号
     * @return boolean
     */
    public Boolean notExistContractByIdentifier(String identifier) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ProjectEntity>().eq("identifier", identifier));
        if (count > 0)
            return false;
        else
            return true;
    }


    /**
     * 根据合同ID查询是否已经下发或者正在进行中
     *
     * @param contractId 项目编号
     * @return boolean
     */
    public Boolean notExistContractByContractId(Long contractId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ProjectEntity>().eq("contract_id", contractId)
                .ge("status", 3)
        );
        if (count > 0)
            return false;
        else
            return true;
    }

    /**
     * 通过合同ID查询相关的项目信息
     */
    public List<ProjectEntity> selectListByContractId(Long contractId) {
        List<ProjectEntity> projectList = baseMapper.selectList(
                new QueryWrapper<ProjectEntity>()
                        .eq("contract_id", contractId)
        );
        return projectList;
    }


    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     *
     * @param params
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(Map<String, Object> params) {
        //数据权限控制
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long deptId = sysUserEntity.getDeptId();//登录用户部门ID
//        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
//        List<Long> projectTypes = sysRoleDeptService.queryProjectTypeListByUserId(sysUserEntity.getUserId());
        List<Long> orderIds = sysRoleDeptService.queryOrderListByUserId(sysUserEntity.getUserId());
        List<Long> sourceIds = sysRoleDeptService.querySourceListByUserId(sysUserEntity.getUserId());

        //根据ID列表查询类型信息名称列表
//        List<String> projectTypeNames = new ArrayList<>();
//        if(projectTypes!=null && projectTypes.size()>0) {
//            projectTypeNames = categoryService.getCategoryNameByIds(projectTypes);
//        }else {
//            projectTypeNames.add("无项目类型权限");//项目类型权限控制,无任何权限时故意赋值0查不到任何数据
//            log.error("当前用户"+sysUserEntity.getUsername()+",部门ID："+deptId+",无任何项目类型权限!");
//        }
//
//        //数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
//                .in("dept_id", roleDeptIds)//部门权限控制,只根据数据权限显示数据，不根据归属部门
//                .in((projectTypeNames!=null && projectTypeNames.size()>0), "type", projectTypeNames);//项目类型权限控制,>0判断逻辑上稍有漏洞

        queryWrapper.and(wr -> {
            //根据ID获取项目隶属名称列表
            List<String> companyOrderList = new ArrayList<>();
            if (orderIds != null && orderIds.size() > 0) {
                companyOrderList = orderSourceService.getOrderSourceByIds(orderIds);
            } else {
                companyOrderList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
            }

            //根据ID获取业务来源名称列表
            List<String> businessSourcesList = new ArrayList<String>();
            if (sourceIds != null && sourceIds.size() > 0) {
                businessSourcesList = orderSourceService.getOrderSourceByIds(sourceIds);
            } else {
                businessSourcesList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
            }

            wr.in("company_order", companyOrderList);
            wr.or();
            wr.in("business_source", businessSourcesList);
        });

        return queryWrapper;
    }

    /**
     * 用于项目信息分页的查询条件的处理
     * 不限制部门数据权限及项目类型权限
     *
     * @param params
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParams(Map<String, Object> params) {
        String parentid = (String) params.get("parentid");//父级ID,一级指令为0
        String id = (String) params.get("id");
        String isTime = (String) params.get("isTime");
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String contractIdentifier = (String) params.get("contractIdentifier");//合同编号 模糊查询
        String projectName = (String) params.get("projectName");//项目名称    模糊搜索
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索
        String entrustCompany = (String) params.get("entrustCompany");
        // 业务员是 登录人
        String salesmen = (String) params.get("salesmen");//业务员    模糊搜索
        String telephone = (String) params.get("telephone");//联系电话    模糊搜索
        String entrustType = (String) params.get("entrustType");//委托类型
        String type = (String) params.get("type");//项目类型
        String companyOrder = (String) params.get("companyOrder");//项目隶属公司
        String contractId = (String) params.get("contractId");//合同ID

        String badDebt = (String) params.get("badDebt");//回款状态 1 正常 2 坏账

        String remarks = (String) params.get("remarks");
        String urgent = (String) params.get("urgent");//加急状态(0正常，1较急、2加急)
        String old = (String) params.get("old");//新老业务(0新业务，1续签业务)
        String careof = (String) params.get("careof");//是否为转交业务(0否，1是)
        String careofType = (String) params.get("careofType");//转交类型(0意向客户，1确定合作客户)
        String careofUsername = (String) params.get("careofUsername");//转交人
        String expressnumber = (String) params.get("expressnumber");//快递单号
        String keyClauses = (String) params.get("keyClauses");//关键条款
        String username = (String) params.get("username");//录入人姓名

        // 业务员是自己的中介
        List<String> nameList;
        if (params.get("nameList") != null) {
            nameList = (List<String>) params.get("nameList");//中介人员数组
        } else {
            nameList = new ArrayList<>();
        }

//    	String orderbyColumns = (String)params.get("orderby");//排序  数据库字段名 subject_fee
//        String isAscString = (String)params.get("isAsc");//正序倒叙 true为正序 false为倒叙
//        Boolean isAsc = Boolean.valueOf(isAscString);

        String statusMin = (String) params.get("statusMin");
        String statusMax = (String) params.get("statusMax");

        String statuses = (String) params.get("statuses");//项目状态 多选
        List<String> statusList = new ArrayList<>();
        if (StringUtils.isNotBlank(statuses)) {
            String[] statuse = statuses.split(",");
            statusList = Arrays.asList(statuse);
        }

        String types = (String) params.get("types");//项目类型 多选
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(types)) {
            String[] typev = types.split(",");
            typeList = Arrays.asList(typev);
        }

        String charge = (String) params.get("charge");//负责人
        String province = (String) params.get("province");//省份
        String city = (String) params.get("city");//市
        String area = (String) params.get("area");//区

        if ((String) params.get("province") != null && province.equals("省")) {
            province = "";
        }
        if ((String) params.get("city") != null && city.equals("市")) {
            city = "";
        }
        if ((String) params.get("area") != null && area.equals("区")) {
            area = "";
        }


        String officeAddress = (String) params.get("officeAddress");//受检地址
        String contact = (String) params.get("contact");//联系人
        String businessSource = (String) params.get("businessSource");//业务来源

        //-----------------------金额
//    	String totalMoney = (String)params.get("totalMoney");//项目金额
        String totalMoneyMin = (String) params.get("totalMoneyMin");//小于项目金额
        String totalMoneyMax = (String) params.get("totalMoneyMax");//大于项目金额

//    	String commission = (String)params.get("commission");//佣金
        String commissionMin = (String) params.get("commissionMin");//小于佣金
        String commissionMax = (String) params.get("commissionMax");//大于佣金

//    	String evaluationFee = (String)params.get("evaluationFee");//评审费
        String evaluationFeeMin = (String) params.get("evaluationFeeMin");//小于评审费
        String evaluationFeeMax = (String) params.get("evaluationFeeMax");//大于评审费

//    	String subprojectFee = (String)params.get("subprojectFee");//分包费
        String subprojectFeeMin = (String) params.get("subprojectFeeMin");//小于分包费
        String subprojectFeeMax = (String) params.get("subprojectFeeMax");//大于分包费

//    	String serviceCharge = (String)params.get("serviceCharge");//服务费
        String serviceChargeMin = (String) params.get("serviceChargeMin");//小于服务费
        String serviceChargeMax = (String) params.get("serviceChargeMax");//大于服务费

//    	String otherExpenses = (String)params.get("otherExpenses");//其他支出
        String otherExpensesMin = (String) params.get("otherExpensesMin");//小于其他支出
        String otherExpensesMax = (String) params.get("otherExpensesMax");//大于其他支出

//    	String netvalue = (String)params.get("netvalue");//项目净值
        String netvalueMin = (String) params.get("netvalueMin");//小于项目净值
        String netvalueMax = (String) params.get("netvalueMax");//大于项目净值

//    	String receiptMoney = (String)params.get("receiptMoney");//已收款金额
        String receiptMoneyMin = (String) params.get("receiptMoneyMin");//小于已收款金额
        String receiptMoneyMax = (String) params.get("receiptMoneyMax");//大于已收款金额

//    	String nosettlementMoney = (String)params.get("nosettlementMoney");//已收款金额
        String nosettlementMoneyMin = (String) params.get("nosettlementMoneyMin");//小于未结算金额
        String nosettlementMoneyMax = (String) params.get("nosettlementMoneyMax");//大于未结算金额

//    	String invoiceMoney = (String)params.get("invoiceMoney");//已开票金额
        String invoiceMoneyMin = (String) params.get("invoiceMoneyMin");//小于已开票金额
        String invoiceMoneyMax = (String) params.get("invoiceMoneyMax");//大于已开票金额

//    	String virtualTax = (String)params.get("virtualTax");//虚拟税费
        String virtualTaxMin = (String) params.get("virtualTaxMin");//小于虚拟税费
        String virtualTaxMax = (String) params.get("virtualTaxMax");//大于虚拟税费


        ProjectAmountVo projectAmountVo = new ProjectAmountVo();
        List<Long> amountList = new ArrayList<>();
        if (StringUtils.isNotBlank(commissionMin) || StringUtils.isNotBlank(commissionMax) || StringUtils.isNotBlank(evaluationFeeMin) || StringUtils.isNotBlank(evaluationFeeMax)
                || StringUtils.isNotBlank(subprojectFeeMin) || StringUtils.isNotBlank(subprojectFeeMax) || StringUtils.isNotBlank(serviceChargeMin) || StringUtils.isNotBlank(serviceChargeMax)
                || StringUtils.isNotBlank(otherExpensesMin) || StringUtils.isNotBlank(otherExpensesMax) || StringUtils.isNotBlank(receiptMoneyMin) || StringUtils.isNotBlank(receiptMoneyMax)
                || StringUtils.isNotBlank(nosettlementMoneyMin) || StringUtils.isNotBlank(nosettlementMoneyMax) || StringUtils.isNotBlank(invoiceMoneyMin) || StringUtils.isNotBlank(invoiceMoneyMax)
                || StringUtils.isNotBlank(virtualTaxMin) || StringUtils.isNotBlank(virtualTaxMax)
//                ||StringUtils.isNotBlank(totalMoneyMin)||StringUtils.isNotBlank(totalMoneyMax)
//                ||StringUtils.isNotBlank(netvalueMin)||StringUtils.isNotBlank(netvalueMax)
        ) {
//            projectAmountVo.setTotalMoneyMin(totalMoneyMin);
//            projectAmountVo.setTotalMoneyMax(totalMoneyMax);
//            projectAmountVo.setNetvalueMin(netvalueMin);
//            projectAmountVo.setNetvalueMax(netvalueMax);
            projectAmountVo.setCommissionMin(commissionMin);
            projectAmountVo.setCommissionMax(commissionMax);
            projectAmountVo.setEvaluationFeeMin(evaluationFeeMin);
            projectAmountVo.setEvaluationFeeMax(evaluationFeeMax);
            projectAmountVo.setSubprojectFeeMin(subprojectFeeMin);
            projectAmountVo.setSubprojectFeeMax(subprojectFeeMax);
            projectAmountVo.setServiceChargeMin(serviceChargeMin);
            projectAmountVo.setServiceChargeMax(serviceChargeMax);
            projectAmountVo.setNosettlementMoneyMin(nosettlementMoneyMin);
            projectAmountVo.setNosettlementMoneyMax(nosettlementMoneyMax);
            projectAmountVo.setInvoiceMoneyMin(invoiceMoneyMin);
            projectAmountVo.setInvoiceMoneyMax(invoiceMoneyMax);
            projectAmountVo.setOtherExpensesMin(otherExpensesMin);
            projectAmountVo.setOtherExpensesMax(otherExpensesMax);
            projectAmountVo.setReceiptMoneyMin(receiptMoneyMin);
            projectAmountVo.setReceiptMoneyMax(receiptMoneyMax);
            projectAmountVo.setVirtualTaxMin(virtualTaxMin);
            projectAmountVo.setVirtualTaxMax(virtualTaxMax);
            amountList = projectAmountService.getProjectIdsByParams(projectAmountVo);
        }

        //-------------------日期
        //项目签订日期signDate
        String startDate = (String) params.get("startDate");    //项目签订日期开始
        String endDate = (String) params.get("endDate");        //项目签订日期结束

        String commissionDateStart = (String) params.get("commissionDateStart");    //委托日期开始
        String commissionDateEnd = (String) params.get("commissionDateEnd");    //委托日期结束

        String claimEndDateStart = (String) params.get("claimEndDateStart");//要求报告完成日期开始
        String claimEndDateEnd = (String) params.get("claimEndDateEnd");    //要求报告完成日期结束

        String onsiteInvestDateStart = (String) params.get("onsiteInvestDateStart");//现场调查日期开始
        String onsiteInvestDateEnd = (String) params.get("onsiteInvestDateEnd");    //现场调查日期结束

        String projectReviewDateStart = (String) params.get("projectReviewDateStart");//项目评审日期开始
        String projectReviewDateEnd = (String) params.get("projectReviewDateEnd");    //项目评审日期结束

        String rptIssuanceDateStart = (String) params.get("rptIssuanceDateStart");//报告签发日期开始
        String rptIssuanceDateEnd = (String) params.get("rptIssuanceDateEnd");    //报告签发日期结束
//        report_cover_date
        String reportCoverDateStart = (String) params.get("reportCoverDateStart");//报告封面日期开始
        String reportCoverDateEnd = (String) params.get("reportCoverDateEnd");    //报告封面日期结束

        String rptSentDateStart = (String) params.get("rptSentDateStart");//报告发送日期开始
        String rptSentDateEnd = (String) params.get("rptSentDateEnd");    //报告发送日期结束

        String rptBindingDateStart = (String) params.get("rptBindingDateStart");//报告装订日期开始
        String rptBindingDateEnd = (String) params.get("rptBindingDateEnd");    //报告装订日期结束

        String receiptDateStart = (String) params.get("receiptDateStart");//收款日期开始
        String receiptDateEnd = (String) params.get("receiptDateEnd");    //收款日期结束

        String filingDateStart = (String) params.get("filingDateStart");//归档日期开始
        String filingDateEnd = (String) params.get("filingDateEnd");    //归档日期结束

        String planDateStart = (String) params.get("planDateStart");//采样日期开始
        String planDateEnd = (String) params.get("planDateEnd");    //采样日期结束

        String invoiceDateStart = (String) params.get("invoiceDateStart");//开票日期开始
        String invoiceDateEnd = (String) params.get("invoiceDateEnd");    //开票日期结束

        ProjectDateVo projectDateVo = new ProjectDateVo();
        List<Long> dateList = new ArrayList<>();
        if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate) || StringUtils.isNotBlank(commissionDateStart) || StringUtils.isNotBlank(commissionDateEnd) ||
                StringUtils.isNotBlank(claimEndDateStart) || StringUtils.isNotBlank(claimEndDateEnd) || StringUtils.isNotBlank(onsiteInvestDateStart) || StringUtils.isNotBlank(onsiteInvestDateEnd) ||
                StringUtils.isNotBlank(projectReviewDateStart) || StringUtils.isNotBlank(projectReviewDateEnd) || StringUtils.isNotBlank(rptIssuanceDateStart) || StringUtils.isNotBlank(rptIssuanceDateEnd) ||
                StringUtils.isNotBlank(rptSentDateStart) || StringUtils.isNotBlank(rptSentDateEnd) || StringUtils.isNotBlank(rptBindingDateStart) || StringUtils.isNotBlank(rptBindingDateEnd) ||
                StringUtils.isNotBlank(receiptDateStart) || StringUtils.isNotBlank(receiptDateEnd) || StringUtils.isNotBlank(filingDateStart) || StringUtils.isNotBlank(filingDateEnd) ||
                StringUtils.isNotBlank(planDateStart) || StringUtils.isNotBlank(planDateEnd) || StringUtils.isNotBlank(reportCoverDateStart) || StringUtils.isNotBlank(reportCoverDateEnd) ||
                StringUtils.isNotBlank(invoiceDateStart) || StringUtils.isNotBlank(invoiceDateEnd)) {
            projectDateVo.setSignDateStart(startDate);
            projectDateVo.setSignDateEnd(endDate);
            projectDateVo.setEntrustDateStart(commissionDateStart);
            projectDateVo.setEntrustDateEnd(commissionDateEnd);
            projectDateVo.setClaimEndDateStart(claimEndDateStart);
            projectDateVo.setClaimEndDateEnd(claimEndDateEnd);
            projectDateVo.setSurveyDateStart(onsiteInvestDateStart);
            projectDateVo.setSurveyDateEnd(onsiteInvestDateEnd);
            projectDateVo.setExamineStartStart(projectReviewDateStart);
            projectDateVo.setExamineStartEnd(projectReviewDateEnd);
            projectDateVo.setReportIssueStart(rptIssuanceDateStart);
            projectDateVo.setReportIssueEnd(rptIssuanceDateEnd);
            projectDateVo.setReportSendStart(rptSentDateStart);
            projectDateVo.setReportSendEnd(rptSentDateEnd);
            projectDateVo.setReportBindingStart(rptBindingDateStart);
            projectDateVo.setReportBindingEnd(rptBindingDateEnd);
            projectDateVo.setReceiveAmountStart(receiptDateStart);
            projectDateVo.setReceiveAmountEnd(receiptDateEnd);
            projectDateVo.setReportFilingStart(filingDateStart);
            projectDateVo.setReportFilingEnd(filingDateEnd);
            projectDateVo.setStartDate(planDateStart);
            projectDateVo.setEndDate(planDateEnd);
            projectDateVo.setReportCoverDateStart(reportCoverDateStart);
            projectDateVo.setReportCoverDateEnd(reportCoverDateEnd);
            projectDateVo.setInvoiceDateStart(invoiceDateStart);
            projectDateVo.setInvoiceDateEnd(invoiceDateEnd);
            dateList = projectDateService.getProjectIdsByParams(projectDateVo);

        }


        List<Long> identicalId = new ArrayList<>();
        //dateList amountList
        if (dateList.size() > 0 && amountList.size() > 0) {
            List<Long> finalAmountList = amountList;
            identicalId = dateList.stream().filter(item -> finalAmountList.contains(item)).collect(Collectors.toList());
        } else if (dateList.size() > 0 && !(amountList.size() > 0)) {
            identicalId = dateList;
        } else if (amountList.size() > 0 && !(dateList.size() > 0)) {
            identicalId = amountList;
        }

        //项目录入日期
        String createStartTime = (String) params.get("createStartTime");
        String createEndTime = (String) params.get("createEndTime");

        String status = (String) params.get("status");//项目状态
        String status100 = null;
        if ("100".equals(status)) {//但前端传来100表示需要查进行中的项目
            status100 = "100";
            status = null;
        }

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>()
                .eq(StringUtils.isNotBlank(parentid), "parentid", parentid)
                .eq(StringUtils.isNotBlank(id), "id", id)
                .eq(StringUtils.isNotBlank(isTime), "is_time", isTime)
                .like(StringUtils.isNotBlank(identifier), "identifier", identifier)
                .eq(StringUtils.isNotBlank(contractIdentifier), "contract_identifier", contractIdentifier)
                .like(StringUtils.isNotBlank(projectName), "project_name", projectName)
//    			.like(StringUtils.isNotBlank(salesmen),"salesmen", salesmen)
                .like(StringUtils.isNotBlank(telephone), "telephone", telephone)
                .like(StringUtils.isNotBlank(charge), "charge", charge)
                .like(StringUtils.isNotBlank(province), "province", province)
                .like(StringUtils.isNotBlank(city), "city", city)
                .like(StringUtils.isNotBlank(area), "area", area)
                .like(StringUtils.isNotBlank(officeAddress), "office_address", officeAddress)
                .like(StringUtils.isNotBlank(contact), "contact", contact)
                .like(StringUtils.isNotBlank(businessSource), "business_source", businessSource)
                .like(StringUtils.isNotBlank(careofUsername), "careof_username", careofUsername)
                .like(StringUtils.isNotBlank(expressnumber), "expressnumber", expressnumber)
                .like(StringUtils.isNotBlank(keyClauses), "keyClauses", keyClauses)
                .like(StringUtils.isNotBlank(username), "username", username)
                .like(StringUtils.isNotBlank(company), "company", company)
                .like(StringUtils.isNotBlank(entrustCompany), "entrust_company", entrustCompany)
                .like(StringUtils.isNotBlank(remarks), "remarks", remarks)

//    			.eq(StringUtils.checkValNotNull(totalMoney),"total_money", totalMoney)
                .ge(StringUtils.checkValNotNull(totalMoneyMin), "total_money", totalMoneyMin)
                .le(StringUtils.checkValNotNull(totalMoneyMax), "total_money", totalMoneyMax)

                .ge(StringUtils.checkValNotNull(statusMin), "status", statusMin)
                .le(StringUtils.checkValNotNull(statusMax), "status", statusMax)


//    			.eq(StringUtils.checkValNotNull(netvalue),"netvalue", netvalue)
                .ge(StringUtils.checkValNotNull(netvalueMin), "netvalue", netvalueMin)
                .le(StringUtils.checkValNotNull(netvalueMax), "netvalue", netvalueMax)

                .eq(StringUtils.checkValNotNull(urgent), "urgent", urgent)
                .eq(StringUtils.checkValNotNull(old), "old", old)
                .eq(StringUtils.checkValNotNull(careof), "careof", careof)
                .eq(StringUtils.checkValNotNull(careofType), "careof_type", careofType)

                .eq(StringUtils.checkValNotNull(status), "status", status)
                .between(StringUtils.checkValNotNull(status100), "status", 2, 69)//100为虚拟状态，代表进行中的项目（状态2-69）
                .eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
                .eq(StringUtils.checkValNotNull(type), "type", type)
                .eq(StringUtils.checkValNotNull(companyOrder), "company_order", companyOrder)
                .eq(StringUtils.checkValNotNull(contractId), "contract_id", contractId)//合同ID

                .eq(StringUtils.checkValNotNull(badDebt),"bad_debt",badDebt)

                .ge(StringUtils.checkValNotNull(createStartTime), "createtime", createStartTime)
                .le(StringUtils.checkValNotNull(createEndTime), "createtime", createEndTime)

                .in(statusList.size() > 0, "status", statusList)

//                .in(amountList.size()>0,"id",amountList)
                .in(identicalId.size() > 0, "id", identicalId)

                .in(typeList.size() > 0, "type", typeList)
                .and(StringUtils.isNotBlank(salesmen), i -> i.eq("salesmen", salesmen).or().in(nameList.size() > 0, "salesmen", nameList));
//                .and(StringUtils.isNotBlank(salesmen),i->i.eq("salesmen", salesmen).or().in(nameList.size()>0,"salesmen", nameList));


        return queryWrapper;
    }


    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     *
     * @param params
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParamsAuth2(Map<String, Object> params) {
        //数据权限控制
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long deptId = sysUserEntity.getDeptId();//登录用户部门ID
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        List<Long> projectTypes = sysRoleDeptService.queryProjectTypeListByUserId(sysUserEntity.getUserId());
        List<Long> orderIds = sysRoleDeptService.queryOrderListByUserId(sysUserEntity.getUserId());
        List<Long> sourceIds = sysRoleDeptService.querySourceListByUserId(sysUserEntity.getUserId());

        //根据ID列表查询类型信息名称列表
        List<String> projectTypeNames = new ArrayList<>();
        if (projectTypes != null && projectTypes.size() > 0) {
            projectTypeNames = categoryService.getCategoryNameByIds(projectTypes);
        } else {
            projectTypeNames.add("无项目类型权限");//项目类型权限控制,无任何权限时故意赋值0查不到任何数据
            log.error("当前用户" + sysUserEntity.getUsername() + ",部门ID：" + deptId + ",无任何项目类型权限!");
        }

        //数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams2(params)
                .in("dept_id", roleDeptIds)//部门权限控制,只根据数据权限显示数据，不根据归属部门
                .in((projectTypeNames != null && projectTypeNames.size() > 0), "type", projectTypeNames);//项目类型权限控制,>0判断逻辑上稍有漏洞

        queryWrapper.and(wr -> {
            //根据ID获取项目隶属名称列表
            List<String> companyOrderList = new ArrayList<>();
            if (orderIds != null && orderIds.size() > 0) {
                companyOrderList = orderSourceService.getOrderSourceByIds(orderIds);
            } else {
                companyOrderList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
            }

            //根据ID获取业务来源名称列表
            List<String> businessSourcesList = new ArrayList<String>();
            if (sourceIds != null && sourceIds.size() > 0) {
                businessSourcesList = orderSourceService.getOrderSourceByIds(sourceIds);
            } else {
                businessSourcesList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
            }

            wr.in("company_order", companyOrderList);
            wr.or();
            wr.in("business_source", businessSourcesList);
        });

        return queryWrapper;
    }

    /**
     * 用于项目信息分页的查询条件的处理---金额统计
     * 不限制部门数据权限及项目类型权限
     *
     * @param params
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParams2(Map<String, Object> params) {
        String parentid = (String) params.get("parentid");//父级ID,一级指令为0
        String id = (String) params.get("id");
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String contractIdentifier = (String) params.get("contractIdentifier");//合同编号 模糊查询
        String projectName = (String) params.get("projectName");//项目名称    模糊搜索
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索
        String entrustCompany = (String) params.get("entrustCompany");
        String salesmen = (String) params.get("salesmen");//业务员    模糊搜索
        String telephone = (String) params.get("telephone");//联系电话    模糊搜索
        String entrustType = (String) params.get("entrustType");//委托类型
//    	String type = (String)params.get("type");//项目类型
        String companyOrder = (String) params.get("companyOrder");//项目隶属公司
        String contractId = (String) params.get("contractId");//合同ID

        String badDebt = (String) params.get("badDebt");//回款状态 1 正常 2 坏账

        String remarks = (String) params.get("remarks");
        String urgent = (String) params.get("urgent");//加急状态(0正常，1较急、2加急)
        String old = (String) params.get("old");//新老业务(0新业务，1续签业务)
        String careof = (String) params.get("careof");//是否为转交业务(0否，1是)
        String careofType = (String) params.get("careofType");//转交类型(0意向客户，1确定合作客户)
        String careofUsername = (String) params.get("careofUsername");//转交人
        String expressnumber = (String) params.get("expressnumber");//快递单号
        String keyClauses = (String) params.get("keyClauses");//关键条款
        String username = (String) params.get("username");//录入人姓名

//        List<String> nameList = (List<String>)params.get("nameList");//中介人员数组
        List<String> nameList;
        if (params.get("nameList") != null) {
            nameList = (List<String>) params.get("nameList");//中介人员数组
        } else {
            nameList = new ArrayList<>();
        }

//    	String orderbyColumns = (String)params.get("orderby");//排序  数据库字段名 subject_fee
//        String isAscString = (String)params.get("isAsc");//正序倒叙 true为正序 false为倒叙
//        Boolean isAsc = Boolean.valueOf(isAscString);

        String statuses = (String) params.get("statuses");//项目状态 多选
        List<String> statusList = new ArrayList<>();
        if (StringUtils.isNotBlank(statuses)) {
            String[] statuse = statuses.split(",");
            statusList = Arrays.asList(statuse);
        }

        String types = (String) params.get("types");//项目类型 多选
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(types)) {
            String[] typev = types.split(",");
            typeList = Arrays.asList(typev);
        }

        String charge = (String) params.get("charge");//负责人
        String province = (String) params.get("province");//省份
        String city = (String) params.get("city");//市
        String area = (String) params.get("area");//区

        if ((String) params.get("province") != null && province.equals("省")) {
            province = "";
        }
        if ((String) params.get("city") != null && city.equals("市")) {
            city = "";
        }
        if ((String) params.get("area") != null && area.equals("区")) {
            area = "";
        }


        String officeAddress = (String) params.get("officeAddress");//受检地址
        String contact = (String) params.get("contact");//联系人
        String businessSource = (String) params.get("businessSource");//业务来源

        //-----------------------金额
//    	String totalMoney = (String)params.get("totalMoney");//项目金额
        String totalMoneyMin = (String) params.get("totalMoneyMin");//小于项目金额
        String totalMoneyMax = (String) params.get("totalMoneyMax");//大于项目金额

//    	String commission = (String)params.get("commission");//佣金
        String commissionMin = (String) params.get("commissionMin");//小于佣金
        String commissionMax = (String) params.get("commissionMax");//大于佣金

//    	String evaluationFee = (String)params.get("evaluationFee");//评审费
        String evaluationFeeMin = (String) params.get("evaluationFeeMin");//小于评审费
        String evaluationFeeMax = (String) params.get("evaluationFeeMax");//大于评审费

//    	String subprojectFee = (String)params.get("subprojectFee");//分包费
        String subprojectFeeMin = (String) params.get("subprojectFeeMin");//小于分包费
        String subprojectFeeMax = (String) params.get("subprojectFeeMax");//大于分包费

//    	String serviceCharge = (String)params.get("serviceCharge");//服务费
        String serviceChargeMin = (String) params.get("serviceChargeMin");//小于服务费
        String serviceChargeMax = (String) params.get("serviceChargeMax");//大于服务费

//    	String otherExpenses = (String)params.get("otherExpenses");//其他支出
        String otherExpensesMin = (String) params.get("otherExpensesMin");//小于其他支出
        String otherExpensesMax = (String) params.get("otherExpensesMax");//大于其他支出

//    	String netvalue = (String)params.get("netvalue");//项目净值
        String netvalueMin = (String) params.get("netvalueMin");//小于项目净值
        String netvalueMax = (String) params.get("netvalueMax");//大于项目净值

//    	String receiptMoney = (String)params.get("receiptMoney");//已收款金额
        String receiptMoneyMin = (String) params.get("receiptMoneyMin");//小于已收款金额
        String receiptMoneyMax = (String) params.get("receiptMoneyMax");//大于已收款金额

//    	String nosettlementMoney = (String)params.get("nosettlementMoney");//已收款金额
        String nosettlementMoneyMin = (String) params.get("nosettlementMoneyMin");//小于未结算金额
        String nosettlementMoneyMax = (String) params.get("nosettlementMoneyMax");//大于未结算金额

//    	String invoiceMoney = (String)params.get("invoiceMoney");//已开票金额
        String invoiceMoneyMin = (String) params.get("invoiceMoneyMin");//小于已开票金额
        String invoiceMoneyMax = (String) params.get("invoiceMoneyMax");//大于已开票金额

//    	String virtualTax = (String)params.get("virtualTax");//虚拟税费
        String virtualTaxMin = (String) params.get("virtualTaxMin");//小于虚拟税费
        String virtualTaxMax = (String) params.get("virtualTaxMax");//大于虚拟税费


        ProjectAmountVo projectAmountVo = new ProjectAmountVo();
        List<Long> amountList = new ArrayList<>();
        if (StringUtils.isNotBlank(commissionMin) || StringUtils.isNotBlank(commissionMax) || StringUtils.isNotBlank(evaluationFeeMin) || StringUtils.isNotBlank(evaluationFeeMax)
                || StringUtils.isNotBlank(subprojectFeeMin) || StringUtils.isNotBlank(subprojectFeeMax) || StringUtils.isNotBlank(serviceChargeMin) || StringUtils.isNotBlank(serviceChargeMax)
                || StringUtils.isNotBlank(otherExpensesMin) || StringUtils.isNotBlank(otherExpensesMax) || StringUtils.isNotBlank(receiptMoneyMin) || StringUtils.isNotBlank(receiptMoneyMax)
                || StringUtils.isNotBlank(nosettlementMoneyMin) || StringUtils.isNotBlank(nosettlementMoneyMax) || StringUtils.isNotBlank(invoiceMoneyMin) || StringUtils.isNotBlank(invoiceMoneyMax)
                || StringUtils.isNotBlank(virtualTaxMin) || StringUtils.isNotBlank(virtualTaxMax)) {
            projectAmountVo.setCommissionMin(commissionMin);
            projectAmountVo.setCommissionMax(commissionMax);
            projectAmountVo.setEvaluationFeeMin(evaluationFeeMin);
            projectAmountVo.setEvaluationFeeMax(evaluationFeeMax);
            projectAmountVo.setSubprojectFeeMin(subprojectFeeMin);
            projectAmountVo.setSubprojectFeeMax(subprojectFeeMax);
            projectAmountVo.setServiceChargeMin(serviceChargeMin);
            projectAmountVo.setServiceChargeMax(serviceChargeMax);
            projectAmountVo.setNosettlementMoneyMin(nosettlementMoneyMin);
            projectAmountVo.setNosettlementMoneyMax(nosettlementMoneyMax);
            projectAmountVo.setInvoiceMoneyMin(invoiceMoneyMin);
            projectAmountVo.setInvoiceMoneyMax(invoiceMoneyMax);
            projectAmountVo.setOtherExpensesMin(otherExpensesMin);
            projectAmountVo.setOtherExpensesMax(otherExpensesMax);
            projectAmountVo.setReceiptMoneyMin(receiptMoneyMin);
            projectAmountVo.setReceiptMoneyMax(receiptMoneyMax);
            projectAmountVo.setVirtualTaxMin(virtualTaxMin);
            projectAmountVo.setVirtualTaxMax(virtualTaxMax);
            amountList = projectAmountService.getProjectIdsByParams(projectAmountVo);
        }

        //-------------------日期
        //项目签订日期signDate
        String startDate = (String) params.get("startDate");    //项目签订日期开始
        String endDate = (String) params.get("endDate");        //项目签订日期结束

        String commissionDateStart = (String) params.get("commissionDateStart");    //委托日期开始
        String commissionDateEnd = (String) params.get("commissionDateEnd");    //委托日期结束

        String claimEndDateStart = (String) params.get("claimEndDateStart");//要求报告完成日期开始
        String claimEndDateEnd = (String) params.get("claimEndDateEnd");    //要求报告完成日期结束

        String onsiteInvestDateStart = (String) params.get("onsiteInvestDateStart");//现场调查日期开始
        String onsiteInvestDateEnd = (String) params.get("onsiteInvestDateEnd");    //现场调查日期结束

        String projectReviewDateStart = (String) params.get("projectReviewDateStart");//项目评审日期开始
        String projectReviewDateEnd = (String) params.get("projectReviewDateEnd");    //项目评审日期结束

        String rptIssuanceDateStart = (String) params.get("rptIssuanceDateStart");//报告签发日期开始
        String rptIssuanceDateEnd = (String) params.get("rptIssuanceDateEnd");    //报告签发日期结束

        String rptSentDateStart = (String) params.get("rptSentDateStart");//报告发送日期开始
        String rptSentDateEnd = (String) params.get("rptSentDateEnd");    //报告发送日期结束

        String rptBindingDateStart = (String) params.get("rptBindingDateStart");//报告装订日期开始
        String rptBindingDateEnd = (String) params.get("rptBindingDateEnd");    //报告装订日期结束

        String receiptDateStart = (String) params.get("receiptDateStart");//收款日期开始
        String receiptDateEnd = (String) params.get("receiptDateEnd");    //收款日期结束

        String filingDateStart = (String) params.get("filingDateStart");//归档日期开始
        String filingDateEnd = (String) params.get("filingDateEnd");    //归档日期结束

        String planDateStart = (String) params.get("planDateStart");//采样日期开始
        String planDateEnd = (String) params.get("planDateEnd");    //采样日期结束

        ProjectDateVo projectDateVo = new ProjectDateVo();
        List<Long> dateList = new ArrayList<>();
        if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate) || StringUtils.isNotBlank(commissionDateStart) || StringUtils.isNotBlank(commissionDateEnd) ||
                StringUtils.isNotBlank(claimEndDateStart) || StringUtils.isNotBlank(claimEndDateEnd) || StringUtils.isNotBlank(onsiteInvestDateStart) || StringUtils.isNotBlank(onsiteInvestDateEnd) ||
                StringUtils.isNotBlank(projectReviewDateStart) || StringUtils.isNotBlank(projectReviewDateEnd) || StringUtils.isNotBlank(rptIssuanceDateStart) || StringUtils.isNotBlank(rptIssuanceDateEnd) ||
                StringUtils.isNotBlank(rptSentDateStart) || StringUtils.isNotBlank(rptSentDateEnd) || StringUtils.isNotBlank(rptBindingDateStart) || StringUtils.isNotBlank(rptBindingDateEnd) ||
                StringUtils.isNotBlank(receiptDateStart) || StringUtils.isNotBlank(receiptDateEnd) || StringUtils.isNotBlank(filingDateStart) || StringUtils.isNotBlank(filingDateEnd) ||
                StringUtils.isNotBlank(planDateStart) || StringUtils.isNotBlank(planDateEnd)) {
            projectDateVo.setSignDateStart(startDate);
            projectDateVo.setSignDateEnd(endDate);
            projectDateVo.setEntrustDateStart(commissionDateStart);
            projectDateVo.setEntrustDateEnd(commissionDateEnd);
            projectDateVo.setClaimEndDateStart(claimEndDateStart);
            projectDateVo.setClaimEndDateEnd(claimEndDateEnd);
            projectDateVo.setSurveyDateStart(onsiteInvestDateStart);
            projectDateVo.setSurveyDateEnd(onsiteInvestDateEnd);
            projectDateVo.setExamineStartStart(projectReviewDateStart);
            projectDateVo.setExamineStartEnd(projectReviewDateEnd);
            projectDateVo.setReportIssueStart(rptIssuanceDateStart);
            projectDateVo.setReportIssueEnd(rptIssuanceDateEnd);
            projectDateVo.setReportSendStart(rptSentDateStart);
            projectDateVo.setReportSendEnd(rptSentDateEnd);
            projectDateVo.setReportBindingStart(rptBindingDateStart);
            projectDateVo.setReportBindingEnd(rptBindingDateEnd);
            projectDateVo.setReceiveAmountStart(receiptDateStart);
            projectDateVo.setReceiveAmountEnd(receiptDateEnd);
            projectDateVo.setReportFilingStart(filingDateStart);
            projectDateVo.setReportFilingEnd(filingDateEnd);
            projectDateVo.setStartDate(planDateStart);
            projectDateVo.setEndDate(planDateEnd);
            dateList = projectDateService.getProjectIdsByParams(projectDateVo);

        }

        //项目录入日期
        String createStartTime = (String) params.get("createStartTime");
        String createEndTime = (String) params.get("createEndTime");

        String status = (String) params.get("status");//项目状态
        String status100 = null;
        if ("100".equals(status)) {//但前端传来100表示需要查进行中的项目
            status100 = "100";
            status = null;
        }
        List<Long> identicalId = new ArrayList<>();
        //dateList amountList
        if (dateList.size() > 0 && amountList.size() > 0) {
            List<Long> finalAmountList = amountList;
            identicalId = dateList.stream().filter(item -> finalAmountList.contains(item)).collect(Collectors.toList());
        } else if (dateList.size() > 0 && !(amountList.size() > 0)) {
            identicalId = dateList;
        } else if (amountList.size() > 0 && !(dateList.size() > 0)) {
            identicalId = amountList;
        }

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>()
                .eq(StringUtils.isNotBlank(parentid), "parentid", parentid)
                .eq(StringUtils.isNotBlank(id), "ap.id", id)
                .like(StringUtils.isNotBlank(identifier), "identifier", identifier)
                .eq(StringUtils.isNotBlank(contractIdentifier), "contract_identifier", contractIdentifier)
                .like(StringUtils.isNotBlank(projectName), "project_name", projectName)
                .like(StringUtils.isNotBlank(salesmen), "salesmen", salesmen)
                .like(StringUtils.isNotBlank(telephone), "telephone", telephone)
                .like(StringUtils.isNotBlank(charge), "charge", charge)
                .like(StringUtils.isNotBlank(province), "province", province)
                .like(StringUtils.isNotBlank(city), "city", city)
                .like(StringUtils.isNotBlank(area), "area", area)
                .like(StringUtils.isNotBlank(officeAddress), "office_address", officeAddress)
                .like(StringUtils.isNotBlank(contact), "contact", contact)
                .like(StringUtils.isNotBlank(businessSource), "business_source", businessSource)
                .like(StringUtils.isNotBlank(careofUsername), "careof_username", careofUsername)
                .like(StringUtils.isNotBlank(expressnumber), "expressnumber", expressnumber)
                .like(StringUtils.isNotBlank(keyClauses), "keyClauses", keyClauses)
                .like(StringUtils.isNotBlank(username), "username", username)
                .like(StringUtils.isNotBlank(company), "company", company)
                .like(StringUtils.isNotBlank(entrustCompany), "entrust_company", entrustCompany)
                .like(StringUtils.isNotBlank(remarks), "remarks", remarks)

//    			.eq(StringUtils.checkValNotNull(totalMoney),"total_money", totalMoney)
                .ge(StringUtils.checkValNotNull(totalMoneyMin), "ap.total_money", totalMoneyMin)
                .le(StringUtils.checkValNotNull(totalMoneyMax), "ap.total_money", totalMoneyMax)


//    			.eq(StringUtils.checkValNotNull(netvalue),"netvalue", netvalue)
                .ge(StringUtils.checkValNotNull(netvalueMin), "ap.netvalue", netvalueMin)
                .le(StringUtils.checkValNotNull(netvalueMax), "ap.netvalue", netvalueMax)

                .eq(StringUtils.checkValNotNull(urgent), "urgent", urgent)
                .eq(StringUtils.checkValNotNull(old), "old", old)
                .eq(StringUtils.checkValNotNull(careof), "careof", careof)
                .eq(StringUtils.checkValNotNull(careofType), "careof_type", careofType)

                .eq(StringUtils.checkValNotNull(status), "status", status)
                .between(StringUtils.checkValNotNull(status100), "status", 2, 69)//100为虚拟状态，代表进行中的项目（状态2-69）
                .eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
//                .eq(StringUtils.checkValNotNull(type), "type", type)
                .eq(StringUtils.checkValNotNull(companyOrder), "company_order", companyOrder)
                .eq(StringUtils.checkValNotNull(contractId), "contract_id", contractId)//合同ID

                .eq(StringUtils.checkValNotNull(badDebt),"bad_debt",badDebt)

                .ge(StringUtils.checkValNotNull(createStartTime), "createtime", createStartTime)
                .le(StringUtils.checkValNotNull(createEndTime), "createtime", createEndTime)

                .in(statusList.size() > 0, "status", statusList)

//                .in(amountList.size()>0,"ap.id",amountList)
                .in(identicalId.size() > 0, "ap.id", identicalId)

                .in(typeList.size() > 0, "type", typeList)
                .and(StringUtils.isNotBlank(salesmen), i -> i.eq("salesmen", salesmen).or().in(nameList.size() > 0, "salesmen", nameList));
        ;

        return queryWrapper;
    }


    /**
     * 用于项目信息的查询条件的处理
     *
     * @param queryProjectVo
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParams(QueryProjectVo queryProjectVo) {
        String identifier = queryProjectVo.getIdentifier();//项目编号    模糊搜索
        String contractIdentifier = queryProjectVo.getContractIdentifier();//合同编号 模糊查询
        String projectName = queryProjectVo.getProjectName();//项目名称    模糊搜索
        String company = queryProjectVo.getCompany();//受检企业名称或者委托单位名称共用  模糊搜索
        String salesmen = queryProjectVo.getSalesmen();//业务员    模糊搜索
        String telephone = queryProjectVo.getTelephone();//联系电话    模糊搜索
        Integer status = queryProjectVo.getStatus();//项目状态
        String entrustType = queryProjectVo.getEntrustType();//委托类型
        String type = queryProjectVo.getType();//项目类型
        String companyOrder = queryProjectVo.getCompanyOrder();//项目隶属公司
        String businessSource = queryProjectVo.getBusinessSource();//业务来源
        Long contractId = queryProjectVo.getContractId(); //合同ID
        String entrustCompany = queryProjectVo.getEntrustCompany();
        String charge = queryProjectVo.getCharge();
        String planRptissuDateMin = queryProjectVo.getPlanRptissuDateMin();
        String planRptissuDateMax = queryProjectVo.getPlanRptissuDateMax();

        String subjection = queryProjectVo.getSubjection();

        String province = queryProjectVo.getProvince();
        String city = queryProjectVo.getCity();
        String area = queryProjectVo.getArea();
        if (queryProjectVo.getProvince() != null && province.equals("省")) {
            province = "";
        }
        if (queryProjectVo.getCity() != null && city.equals("市")) {
            city = "";
        }
        if (queryProjectVo.getArea() != null && area.equals("区")) {
            area = "";
        }

        //项目签订日期signDate
        String startDate = queryProjectVo.getStartDate();    //项目签订日期
        String endDate = queryProjectVo.getEndDate();        //项目签订日期

        String types = queryProjectVo.getTypes();//项目类型 多选
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(types)) {
            String[] typev = types.split(",");
            typeList = Arrays.asList(typev);
        }

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>()
                .like(StringUtils.isNotBlank(identifier), "identifier", identifier)
                .like(StringUtils.isNotBlank(contractIdentifier), "contract_identifier", contractIdentifier)
                .like(StringUtils.isNotBlank(projectName), "project_name", projectName)
                .like(StringUtils.isNotBlank(salesmen), "salesmen", salesmen)
                .like(StringUtils.isNotBlank(telephone), "telephone", telephone)

                .ge(StringUtils.checkValNotNull(planRptissuDateMin), "plan_rptissu_date", planRptissuDateMin)
                .le(StringUtils.checkValNotNull(planRptissuDateMax), "plan_rptissu_date", planRptissuDateMax)

                .eq(StringUtils.checkValNotNull(status), "status", status)
                .eq(StringUtils.checkValNotNull(province), "province", province)
                .eq(StringUtils.checkValNotNull(city), "city", city)
                .eq(StringUtils.checkValNotNull(area), "area", area)
                .eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
                .eq(StringUtils.checkValNotNull(type), "type", type)
                .eq(StringUtils.checkValNotNull(companyOrder), "company_order", companyOrder)

                .eq(StringUtils.checkValNotNull(businessSource), "business_source", businessSource)
                .eq(StringUtils.checkValNotNull(contractId), "contract_id", contractId)//合同ID
                .like(StringUtils.isNotBlank(company), "company", company)
                .like(StringUtils.isNotBlank(entrustCompany), "entrust_company", entrustCompany)
                .like(StringUtils.isNotBlank(charge), "charge", charge)
                .in(typeList.size() > 0, "type", typeList)
                .between(StringUtils.isNotBlank(endDate), "sign_date", startDate, endDate)//项目签订日期signDate
//                .and(StringUtils.isNotBlank(company),i ->
//	                i.like("company", company)
//	                .or()
//	                .like("entrust_company", company)
//                )
                .orderByDesc("id");

        if (!"ces".equals(subjection) && StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
        }else {
            queryWrapper.gt("id",0);
        }
        //else if (StringUtils.isNotBlank(companyOrder)){
        //            queryWrapper.eq("company_order", companyOrder);
        //        }else if (StringUtils.isNotBlank(businessSource)){
        //            queryWrapper.eq("business_source", businessSource);
        //        }
        return queryWrapper;
    }

    /**
     * 根据条件查询项目ID列表
     */
    public List<Long> getProjectIdsByParams(QueryProjectVo queryVo) {

        // wrapper.select 取出 ProjectEntity对象的id 字段值
        List list = baseMapper.selectObjs(queryWrapperByParams(queryVo).select("id"));

        return (List<Long>) list;
    }


    /**
     * 任务下发
     *
     * @param project
     */
    public void taskRelease(ProjectEntity project) {
        String[] PJtypes1 = new String[]{"预评", "专篇", "控评", "现状"};
        //    	projectService.updateById(project);		//修改项目信息
        Long projectId = project.getId();
        project.setStatus(2);
        //项目ID
        ProjectEntity projectEntity = this.getById(projectId);//获取项目信息对象

        //当项目状态值改变是将其ID和更改后的状态值保存到项目流程表中

        String jobNum = "";
        if (project.getChargeId() != null) {
            SysUserEntity sysUserEntity = sysUserService.getById(project.getChargeId());
            jobNum = sysUserEntity.getJobNum();
        }


        if (project.getStatus().equals(2)) {
            Integer urgent = 0;
            if (project.getUrgent() != null) {
                urgent = project.getUrgent();
            }
            Integer oldStatus = projectEntity.getStatus();
            if (oldStatus != project.getStatus()) {
                ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                proceduresEntity.setProjectId(project.getId());
                proceduresEntity.setStatus(project.getStatus());
                projectProceduresService.save(proceduresEntity);
            }
            projectEntity.setRemarks(project.getRemarks());
            projectEntity.setStatus(project.getStatus());
            projectEntity.setDeptId(project.getDeptId());
            projectEntity.setChargeId(project.getChargeId());
            projectEntity.setCharge(project.getCharge());
            projectEntity.setChargeJobNum(jobNum);
            projectEntity.setUrgent(urgent);
            this.updateById(projectEntity);
//            Long projectId = project.getId();			//项目ID
//            ProjectEntity projectEntity = baseMapper.selectById(projectId);//获取项目信息对象
            if (projectEntity.getStatus().equals(2) && projectEntity.getType().equals("来样检测")) {
                project.setStatus(10);
            }
            this.updateById(projectEntity);
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setTaskReleaseDate(new Date());
            projectDateService.updateById(projectDateEntity);
            if (Arrays.asList(PJtypes1).contains(project.getType())) {
                if (alCompanySurveyService.notExistPlanByProject(projectId)) {
                    AlCompanySurveyEntity alCompanySurveyEntity = new AlCompanySurveyEntity();
                    alCompanySurveyEntity.setCompany(project.getCompany());
                    alCompanySurveyEntity.setIdentifier(project.getIdentifier());
                    alCompanySurveyEntity.setProjectName(project.getProjectName());
                    alCompanySurveyEntity.setType(project.getType());
                    alCompanySurveyEntity.setProjectId(projectId);
                    alCompanySurveyEntity.setOfficeAddress(project.getOfficeAddress());
                    alCompanySurveyEntity.setContact(project.getContact());
                    alCompanySurveyEntity.setTelephone(project.getTelephone());
                    alCompanySurveyService.save(alCompanySurveyEntity);
                } else {
                    R.error("评价用人单位概况调查中已存在此项目信息，不能重复录入！");
                }
            }

            //生成用人单位概况调查初始化信息
            if (companySurveyService.notExistCompanySurveyByProject(projectId)) {
//        		ProjectEntity projectEntity = projectService.getById(projectId);//获取项目信息对象
                CompanyEntity companyEntity = companyService.getById(projectEntity.getCompanyId());//获取企业信息

                //将企业信息与项目信息赋值到单位概况中作为初始化数据
                CompanySurveyEntity companySurvey = new CompanySurveyEntity();
                companySurvey.setProjectId(project.getId());        //项目ID
                companySurvey.setCompany(projectEntity.getCompany());
                companySurvey.setIdentifier(projectEntity.getIdentifier());
                companySurvey.setProjectName(projectEntity.getProjectName());
                companySurvey.setOfficeAddress(projectEntity.getOfficeAddress());
                companySurvey.setEntrustAddress(projectEntity.getEntrustOfficeAddress());
                companySurvey.setEntrustCompany(projectEntity.getEntrustCompany());
                companySurvey.setContact(projectEntity.getContact());
                companySurvey.setTelephone(projectEntity.getTelephone());
                companySurvey.setRegisteredAddress(projectEntity.getOfficeAddress());

                companySurvey.setIndustryCategory(companyEntity.getIndustryCategory());
                companySurvey.setRiskLevel(companyEntity.getRiskLevel());
                companySurvey.setLaborQuota(companyEntity.getPopulation());    //劳动定员(人数)
                companySurvey.setPopulation(companyEntity.getPopulation());    //人数
                companySurvey.setProduct(companyEntity.getProducts());
                companySurvey.setYield(companyEntity.getYields());

                companySurveyService.save(companySurvey);
            } else {
                R.error("用人单位概况调查中已存在此项目信息，不能重复下发！");
            }
        }
        if ("集团发展".equals(projectEntity.getBusinessSource())){
            if (null!=projectEntity.getSalesmenid()){
                AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                abuSendNoteDTO.setProjectId(projectEntity.getId());
                abuSendNoteDTO.setIdentifier(projectEntity.getIdentifier());
                abuSendNoteDTO.setCompany(projectEntity.getCompany());
                abuSendNoteDTO.setEntrustCompany(projectEntity.getEntrustCompany());
                abuSendNoteDTO.setSalesmanId(projectEntity.getSalesmenid());
                abuSendNoteDTO.setSalesman(projectEntity.getSalesmen());
                abuSendNoteDTO.setStatus(projectEntity.getStatus());
                iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
            }
        }

        if (project.getChargeId() != null) {
            //2023/08/04 主管指定项目负责人 发送消息
            if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
                MessageEntity entity = new MessageEntity();
                entity.setTitle("新项目提醒");
                entity.setContent("您有新的项目（【"+ project.getIdentifier() + " " + project.getCompany() +"】）已下发，请及时处理");
                entity.setBusinessType(0);
                entity.setSenderType(0);
                SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
                entity.setSenderId(nowUser.getUserId());
                entity.setSenderName(nowUser.getUsername());
                messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
            }
        }
    }

    /**
     * 实验室收记录
     *
     * @param project
     */
    public boolean receiveTime(ProjectEntity project) {
        String[] huanjingTeam = new String[]{"排污许可", "环境监测", "环评监测", "环境监督", "环境验收", "公共卫生检测", "一次性用品用具检测", "学校卫生检测", "公卫监督", "洁净区域检测"};
        String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
        String[] jianpingTeam = new String[]{"检评", "职卫监督"};
        ProjectDateEntity projectDateEntity = project.getProjectDateEntity();
        boolean ret = true;
        if (projectDateEntity.getDeliverDate() != null && project.getStatus() < 10) {
            if (project != null && project.getStatus() < 10) {
                project.setStatus(10);
                this.updateById(project);
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
                proceduresEntity.setStatus(project.getStatus());
                projectProceduresService.save(proceduresEntity);

            }
        }
        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && project.getStatus() < 20) {
            if (project != null && project.getStatus() < 20) {
                project.setStatus(20);
                this.updateById(project);
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
                proceduresEntity.setStatus(project.getStatus());
                projectProceduresService.save(proceduresEntity);
            }
        }
        PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
        PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
        performanceNodeVo.setProjectId(project.getId());
        performanceNodeVo.setIsTime(project.getIsTime());
        performanceNodeVo.setGatherAcceptDate(projectDateEntity.getGatherAcceptDate());
        performanceNodeVo.setPhysicalAcceptDate(projectDateEntity.getPhysicalAcceptDate());
        performanceNodeVo.setReceivedDate(projectDateEntity.getReceivedDate());
        if (Arrays.asList(huanjingTeam).contains(project.getType())) {
            ret = performanceAllocationService.caiyangHjCommission(performanceNodeVo);
        } else if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
            if (project.getIsTime() == null || project.getIsTime() == 1) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 2) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 3) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            }
        } else if (Arrays.asList(jianpingTeam).contains(project.getType())) {
            if (project.getIsTime() == null || project.getIsTime() == 1) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 2) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 3) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            }
        }

        projectDateService.updateById(projectDateEntity);

        return ret;
    }

    /**
     * 检评/评价采样提成计算
     *
     * @param project
     */
    public void gatherCommission(ProjectEntity project) {
        CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);
        Long projectId = project.getId();
        BigDecimal netvalue = project.getNetvalue();
        ProjectDateEntity projectDateEntity = project.getProjectDateEntity();
        SysDictEntity sysDict = sysDictService.queryByTypeAndCode(TYPE_NAME, "2");
        Double commissionRatioDouble = Double.valueOf(sysDict.getValue());
        BigDecimal commissionRatio = BigDecimal.valueOf(commissionRatioDouble);//提成比例
        BigDecimal cmsAmount = netvalue.multiply(commissionRatio);//项目净值*提成比例


        Long chargeId = project.getChargeId();
        SysUserEntity sysUserEntity = sysUserService.getById(chargeId);
        String subjection = sysUserEntity.getSubjection();

        List<ProjectUserEntity> planUserList = projectUserService.list(new QueryWrapper<ProjectUserEntity>()
                .eq("project_id", projectId)
                .and(i -> i.eq("types", 1)//组员
                        .or()
                        .eq("types", 4)));//组长

        String name = "";
        if (planUserList != null && planUserList.size() > 0) {


            for (int i = 0; i < planUserList.size(); i++) {
                if (i == planUserList.size() - 1) {
                    name += planUserList.get(i).getUsername();
                    break;
                }
                name += planUserList.get(i).getUsername() + ",";

            }
        }

        CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId, "采样提成");
        if (commissionEntity == null) {
            CommissionEntity commission = new CommissionEntity();
            commission.setProjectId(projectId);
            commission.setCommissionDate(projectDateEntity.getGatherAcceptDate());
            commission.setPersonnel(name);
            commission.setType("采样提成");
            commission.setState(1);
            commission.setSubjection(subjection);
            commission.setCmsAmount(cmsAmount);
            commissionService.save(commission);
        }

    }

    /**OA-获取项目信息(包含合同下的其他项目信息)
     *
     * @param identifier 项目编号
     * @return 项目信息
     */
    @Override
    public List<ProjectEntity> getContractProjects(String identifier) {
        return baseMapper.getContractProjects(identifier);
    }

    /**
     * OA-修改项目信息
     *
     * @param projectInfo 待修改信息
     */
    @Override
    @Transactional
    public void updateProjectInfo(ProjectChangeInfoVo projectInfo) {
        //原项目编号
        String oldIdentifier = projectInfo.getOldIdentifier();
        //判断新项目编号是否被占用
        if (!notExistContractByIdentifier(projectInfo.getNewIdentifier())) {
            throw new RuntimeException("项目编号：" + projectInfo.getOldIdentifier() + " 在审批期间已被占用，请重新申请！");
        }
        //整理项目类型及归属合同数据
        List<Map<String, String>> projectTypes = SpringUtils.getBean(CategoryMapper.class).getProjectType();
        Map<String, String> typeMap = new HashMap<>();
        for (Map<String, String> projectType : projectTypes) {
            //填充数据
            typeMap.put(projectType.get("name"), projectType.get("module"));
        }
        //校验当前项目及同一合同下的项目是否允许修改
        List<ProjectEntity> contractProjects = baseMapper.getContractProjects(oldIdentifier);
        //提取原始项目信息
        ProjectEntity oldProjectInfo = contractProjects.stream().filter(projectTemp -> projectTemp.getIdentifier().equals(projectInfo.getOldIdentifier())).collect(Collectors.toList()).get(0);
        if (oldProjectInfo == null) {
            throw new RuntimeException("项目：" + projectInfo.getOldIdentifier() + " 数据已丢失，请联系开发人员排查！");
        }
        //项目进度非 录入阶段 一律禁止修改
        Integer status = oldProjectInfo.getStatus();
        if (status != 1) {
            throw new RuntimeException("项目：" + projectInfo.getOldIdentifier() + " 进度已发生变化，请联系相关人员确认！");
        }
        //1 判断合同类型是否发生变化
        String oldContractType = typeMap.get(oldProjectInfo.getType());
        String newContractType = typeMap.get(projectInfo.getNewType());
        //判断条件：合同类型与原合同类型一致，则自动给出项目编号（不能修改）；合同类型与原合同类型不一致，则手动输入合同编号，并且不能与原合同编号相同
        if (oldContractType.equals(newContractType)) {
            //合同类型未发生变化，仅改变项目信息（仅进度为录入的状态）
            baseMapper.updateProjectInfoByIdentifier(projectInfo.getOldIdentifier(), projectInfo.getNewIdentifier(), projectInfo.getNewType(), oldProjectInfo.getRemarks() + projectInfo.getRemarks());
        } else {
            //合同类型发生变化,且该合同只存在一个项目，且状态为录入阶段，  方可变更项目及合同信息
            if (contractProjects.size() != 1) {
                throw new RuntimeException("该项目所属的合同下存在其他项目，要求不允许修改！请联系相关人员确认！");
            } else {
                //原合同编号
                String contractIdentifier = oldProjectInfo.getContractIdentifier();
                //新合同编号
                String newContractIdentifier = projectInfo.getNewContractIdentifier();
                //检查合同编号是否存在
                ContractEntity contractEntity = SpringUtils.getBean(ContractMapper.class).checkContractIdentifier(newContractIdentifier);
                if (contractEntity != null) {
                    throw new RuntimeException("合同编号:" + newContractIdentifier + " 在审批期间已被占用，请联系相关人员确认或重新发起审批！");
                }
                //变更合同信息
                SpringUtils.getBean(ContractMapper.class).changeIdentifier(contractIdentifier, newContractIdentifier);
                //变更项目信息及合同信息  新信息
                oldProjectInfo.setIdentifier(projectInfo.getNewIdentifier());
                oldProjectInfo.setType(projectInfo.getNewType());
                oldProjectInfo.setRemarks(oldProjectInfo.getRemarks() + projectInfo.getRemarks());
                oldProjectInfo.setContractIdentifier(newContractIdentifier);
                oldProjectInfo.setUpdatetime(new Date());
                baseMapper.updateById(oldProjectInfo);
            }
        }
    }


    /**
     * 采样提成脚本
     *
     * @param projectIdList
     */
    public void commissionGatherByProjectIdList(List<Long> projectIdList) {
//        boolean ret = true;
        if (projectIdList.size() > 0) {
            String[] huanjingTeam = new String[]{"环境监测", "环评监测", "环境监督", "环境验收", "公共卫生检测", "一次性用品用具检测", "学校卫生检测", "公卫监督", "洁净区域检测", "排污许可"};
            String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
            String[] jianpingTeam = new String[]{"检评", "职卫监督"};
            for (Long projectId : projectIdList) {
                ProjectEntity project = baseMapper.selectById(projectId);
                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
                PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
                PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
                performanceNodeVo.setProjectId(project.getId());
                performanceNodeVo.setIsTime(project.getIsTime());
                performanceNodeVo.setGatherAcceptDate(projectDateEntity.getGatherAcceptDate());
                performanceNodeVo.setPhysicalAcceptDate(projectDateEntity.getPhysicalAcceptDate());
                performanceNodeVo.setReceivedDate(projectDateEntity.getReceivedDate());
                if (Arrays.asList(huanjingTeam).contains(project.getType())) {
                    performanceAllocationService.caiyangHjCommission(performanceNodeVo);
                } else if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
                    if (project.getIsTime() == null || project.getIsTime() == 1) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                        }
                    } else if (project.getIsTime() == 2) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                        }
                    } else if (project.getIsTime() == 3) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                        }
                    }
                } else if (Arrays.asList(jianpingTeam).contains(project.getType())) {
                    if (project.getIsTime() == null || project.getIsTime() == 1) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                        }
                    } else if (project.getIsTime() == 2) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                        }
                    } else if (project.getIsTime() == 3) {
                        if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
//                            gatherCommission(project);
                            performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                        }
                    }
                }
            }
        }
    }

    /**
     * 签发提成脚本
     *
     * @param ids
     */
    public void issueCommissionByProjectIds(List<Long> ids) {
        if (ids.size() > 0) {
            String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
            String[] jianpingTeam = new String[]{"检评", "职卫监督"};

            for (Long id : ids) {
                ProjectEntity project = baseMapper.selectById(id);
                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(id);
                if (projectDateEntity.getReportIssue() != null) {
                    PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
                    GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
                    PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
                    performanceNodeVo.setProjectId(project.getId());
                    performanceNodeVo.setReportIssue(projectDateEntity.getReportIssue());
                    if (Arrays.asList(jianpingTeam).contains(project.getType())) {
                        performanceAllocationService.issueCommission(performanceNodeVo);
                    }
                    if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
//                        System.out.println("jinlaila===");
                        gradePointService.getCommissionIssue(performanceNodeVo);
                    }
                }

            }
        }
    }

    /**
     * 归档提成脚本
     *
     * @param ids
     */
    public void fillingCommissionByIds(List<Long> ids) {
        if (ids.size() > 0) {
            String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
            String[] jianpingTeam = new String[]{"检评", "职卫监督"};
            String[] huanjing = new String[]{"环境验收", "环境应急预案", "排污许可证申请", "排污许可后管理", "环保管家", "应急预案", "环境示范", "排污许可"};
            for (Long id : ids) {
                ProjectEntity project = baseMapper.selectById(id);
                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(id);
                if (projectDateEntity.getReportFiling() != null) {
                    GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
                    PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
                    PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
                    performanceNodeVo.setProjectId(project.getId());
                    performanceNodeVo.setReportFiling(projectDateEntity.getReportFiling());
                    if (Arrays.asList(jianpingTeam).contains(project.getType())) {
                        performanceAllocationService.fillingCommission(performanceNodeVo);//TODO 新归档提成-检评
                    } else if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
                        gradePointService.getFilingFees(performanceNodeVo);
                    } else if (Arrays.asList(huanjing).contains(project.getType())) {
                        performanceAllocationService.issueHjCommission(performanceNodeVo);
                    }
                }
            }

        }
    }


    /**
     * 获取检评相关项目信息
     *
     * @param params
     * @return
     */
    public PageUtils getPage(Map<String, Object> params) {
        params = Number2Money.getPageInfo(params);
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        queryWrapper.in("type", "检评", "职卫监督");
        queryWrapper.orderByDesc("createtime");
        IPage<ProjectEntity> page = this.page(new Query<ProjectEntity>().getPage(params), queryWrapper);

        page.getRecords().forEach(action -> {
            ProjectCountEntity projectCountEntity = projectCountService.getOneByProjectId(action.getId());
            if (projectCountEntity != null) {
                action.setProjectCountId(projectCountEntity.getId());
            }
        });
        page.convert(mapper -> ObjectConversion.copy(mapper, ProjectCountVo.class));

        return new PageUtils(page);
    }

    /**
     * 项目总览导出
     *
     * @param params
     * @return
     */
    public List<ProjectCountVo> exportByParams(Map<String, Object> params) {
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapperByParams(params);
        queryWrapper.in("type", "检评", "职卫监督");
        queryWrapper.orderByDesc("createtime");
        List<ProjectCountVo> projectCountVoList = baseMapper.exportProjectCountByMyWrapper(queryWrapper);
        return projectCountVoList;
    }


    /**
     * 新检评系统采样提成接口
     *
     * @param commissionTimeNodeVo
     * @return
     */
    //TODO  新检评系统采样提成接口
    public boolean mathCommission(CommissionTimeNodeVo commissionTimeNodeVo) {
        String[] huanjingTeam = new String[]{"排污许可", "环境监测", "环评监测", "环境监督", "环境验收", "公共卫生检测", "一次性用品用具检测", "学校卫生检测", "公卫监督", "洁净区域检测"};
        String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
        String[] jianpingTeam = new String[]{"检评", "职卫监督"};
        boolean ret = true;
        ProjectEntity project = baseMapper.selectById(commissionTimeNodeVo.getProjectId());
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(commissionTimeNodeVo.getProjectId());
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getDeliverDate())) {
            projectDateEntity.setDeliverDate(commissionTimeNodeVo.getDeliverDate());
        }
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getReceivedDate())) {
            projectDateEntity.setReceivedDate(commissionTimeNodeVo.getReceivedDate());
        }
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getPhysicalSendDate())) {
            projectDateEntity.setPhysicalSendDate(commissionTimeNodeVo.getPhysicalSendDate());

        }
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getPhysicalAcceptDate())) {
            projectDateEntity.setPhysicalAcceptDate(commissionTimeNodeVo.getPhysicalAcceptDate());
        }
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getGatherSendDate())) {
            projectDateEntity.setGatherSendDate(commissionTimeNodeVo.getGatherSendDate());
        }
        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getGatherAcceptDate())) {
            projectDateEntity.setGatherAcceptDate(commissionTimeNodeVo.getGatherAcceptDate());
        }
        project.setProjectDateEntity(projectDateEntity);

        if (StringUtils.checkValNotNull(commissionTimeNodeVo.getGatherDate())) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            int count = baseMapper.getWeatherInfo(project.getId(), simpleDateFormat.format(commissionTimeNodeVo.getGatherDate()));
            if (StringUtils.checkValNotNull(projectDateEntity.getGatherSendDate()) && count > 0) {
                baseMapper.updateGatherSendDate(project.getId(), simpleDateFormat.format(commissionTimeNodeVo.getGatherDate()), simpleDateFormat.format(projectDateEntity.getGatherSendDate()));
            } else {
                return false;
            }
            if (StringUtils.checkValNotNull(projectDateEntity.getGatherAcceptDate()) && count > 0) {
                baseMapper.updateGatherAcceptDate(project.getId(), simpleDateFormat.format(commissionTimeNodeVo.getGatherDate()), simpleDateFormat.format(projectDateEntity.getGatherAcceptDate()));
            } else {
                return false;
            }
        }

        if (project.getIsTime() != null && project.getIsTime() == 3) {
            if (projectDateEntity.getPhysicalAcceptDate() != null && project.getStatus() < 20) {
                if (project != null && project.getStatus() < 20) {
                    project.setStatus(20);
                    this.updateById(project);
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
                    proceduresEntity.setStatus(project.getStatus());
                    projectProceduresService.save(proceduresEntity);
                }
            }
        } else if (project.getIsTime() != null && project.getIsTime() == 2) {
            if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null && project.getStatus() < 20) {
                if (project != null && project.getStatus() < 20) {
                    project.setStatus(20);
                    this.updateById(project);
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
                    proceduresEntity.setStatus(project.getStatus());
                    projectProceduresService.save(proceduresEntity);
                }
            }
        } else {
            if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && project.getStatus() < 20) {
                if (project != null && project.getStatus() < 20) {
                    project.setStatus(20);
                    this.updateById(project);
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
                    proceduresEntity.setStatus(project.getStatus());
                    projectProceduresService.save(proceduresEntity);
                }
            }
        }


        PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
        PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
        performanceNodeVo.setProjectId(project.getId());
        performanceNodeVo.setIsTime(project.getIsTime());
        performanceNodeVo.setGatherAcceptDate(projectDateEntity.getGatherAcceptDate());
        performanceNodeVo.setPhysicalAcceptDate(projectDateEntity.getPhysicalAcceptDate());
        performanceNodeVo.setReceivedDate(projectDateEntity.getReceivedDate());
        if (Arrays.asList(huanjingTeam).contains(project.getType())) {
            ret = performanceAllocationService.caiyangHjCommission(performanceNodeVo);
        } else if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
            if (project.getIsTime() == null || project.getIsTime() == 1) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 2) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 3) {
                if (projectDateEntity.getGatherAcceptDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangPjCommission(performanceNodeVo);
                }
            }
        } else if (Arrays.asList(jianpingTeam).contains(project.getType())) {
            if (project.getIsTime() == null || project.getIsTime() == 1) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getPhysicalAcceptDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 2) {
                if (projectDateEntity.getReceivedDate() != null && projectDateEntity.getGatherAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            } else if (project.getIsTime() == 3) {
                if (projectDateEntity.getGatherAcceptDate() != null && projectDateEntity.getPhysicalAcceptDate() != null) {
                    this.gatherCommission(project);
                    performanceAllocationService.caiyangZjCommission(performanceNodeVo);
                }
            }
        }

        projectDateService.updateById(projectDateEntity);

        return ret;

    }


    /**
     * 中止项目
     *
     * @param projectId
     */
    public void suspendProject(SuspendOrRestartProjectVo suspendOrRestartProjectVo) {

        baseMapper.suspendOrRestartProject(suspendOrRestartProjectVo.getProjectId(), 99);

        ProjectProceduresEntity projectProceduresEntity = projectProceduresService.getProceduresEntity(suspendOrRestartProjectVo.getProjectId(), 99);
        if (projectProceduresEntity != null) {
            projectProceduresEntity.setCreatetime(new Date());
            projectProceduresService.updateById(projectProceduresEntity);
        } else {
            ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
            proceduresEntity.setProjectId(suspendOrRestartProjectVo.getProjectId());
            proceduresEntity.setStatus(99);
            projectProceduresService.save(proceduresEntity);
        }
        ProjectEntity project = this.getById(suspendOrRestartProjectVo.getProjectId());
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

    }

    /**
     * 重启项目
     *
     * @param suspendOrRestartProjectVo
     * @return
     */
    public boolean restartProject(SuspendOrRestartProjectVo suspendOrRestartProjectVo) {
        boolean a;
        if (StringUtils.isNotBlank(suspendOrRestartProjectVo.getRestartType())) {
            a = true;
            List<ProjectProceduresEntity> list = projectProceduresService.listProceduresByProjectId(suspendOrRestartProjectVo.getProjectId());
            if (suspendOrRestartProjectVo.getRestartType().equals("重置")) {
                baseMapper.suspendOrRestartProject(suspendOrRestartProjectVo.getProjectId(), 1);
                if (list.size() > 0) {
                    List<BackupsProjectProceduresEntity> backupsEntityList = new ArrayList<>();
                    for (ProjectProceduresEntity proceduresEntity : list) {
                        BackupsProjectProceduresEntity backupsProjectProceduresEntity = new BackupsProjectProceduresEntity();
                        backupsProjectProceduresEntity.setProjectId(proceduresEntity.getProjectId());
                        backupsProjectProceduresEntity.setStatus(proceduresEntity.getStatus());
                        backupsEntityList.add(backupsProjectProceduresEntity);
                    }
                    backupsProjectProceduresService.saveBatch(backupsEntityList);
                    projectProceduresService.deleteByProjectId(suspendOrRestartProjectVo.getProjectId());
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(suspendOrRestartProjectVo.getProjectId());
                    proceduresEntity.setStatus(1);
                    projectProceduresService.save(proceduresEntity);
                }

            } else if (suspendOrRestartProjectVo.getRestartType().equals("恢复")) {
                if (list.size() > 0) {
                    List<ProjectProceduresEntity> statusList = list.stream().filter(i -> i.getStatus() <= 70).collect(Collectors.toList());
                    Integer status = statusList.get(statusList.size() - 1).getStatus();
                    baseMapper.suspendOrRestartProject(suspendOrRestartProjectVo.getProjectId(), status);
                }
            } else {
                a = false;
            }
        } else {
            a = false;
        }
        if (a){
            ProjectEntity project = this.getById(suspendOrRestartProjectVo.getProjectId());
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
        }

        return a;
    }


    //TODO OA查询单项目

    /**
     * 获取项目基本金额信息
     *
     * @param identifier
     * @return
     */
    public OAProjectAmountVo getInfomation(String identifier,List<String> compantList) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper();
        queryWrapper.eq("p.identifier",identifier);
        queryWrapper.in("p.company_order",compantList);
        OAProjectAmountVo oaProjectAmountVo = baseMapper.getInfomation(queryWrapper);
        return oaProjectAmountVo;
    }

    /**
     * OA修改项目金额
     *
     * @param oaProjectAmountVo
     * @param project
     */
    public void updateProAmount(OAProjectAmountVo oaProjectAmountVo, ProjectEntity project) {
        String idntifier = oaProjectAmountVo.getIdentifier();
        ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(project.getId());

        OAProjectAmountVo oaProjectAmountVo1 = new OAProjectAmountVo();
        oaProjectAmountVo1 = ObjectConversion.copy(projectAmountEntity, OAProjectAmountVo.class);

        System.out.println(" = " +oaProjectAmountVo);
//        oaProjectAmountVo1.setCompanyOrder(project.getCompanyOrder());

//        if (!CollectionUtils.isEqualCollection(list1, list2)) {
        BigDecimal netValue = oaProjectAmountVo.getTotalMoney()
                .subtract(oaProjectAmountVo.getCommission())
                .subtract(oaProjectAmountVo.getServiceCharge())
                .subtract(oaProjectAmountVo.getEvaluationFee())
                .subtract(oaProjectAmountVo.getSubprojectFee())
                .subtract(oaProjectAmountVo.getOtherExpenses())
                .subtract(oaProjectAmountVo.getVirtualTax());
        projectAmountEntity.setTotalMoney(oaProjectAmountVo.getTotalMoney());
        projectAmountEntity.setNetvalue(netValue);
        projectAmountEntity.setCommission(oaProjectAmountVo.getCommission());
        projectAmountEntity.setServiceCharge(oaProjectAmountVo.getServiceCharge());
        projectAmountEntity.setEvaluationFee(oaProjectAmountVo.getEvaluationFee());
        projectAmountEntity.setSubprojectFee(oaProjectAmountVo.getSubprojectFee());
        projectAmountEntity.setOtherExpenses(oaProjectAmountVo.getOtherExpenses());
        projectAmountEntity.setVirtualTax(oaProjectAmountVo.getVirtualTax());
        // projectamount中totalmoney和netvalue在下一步中被计算和保存
        projectMoneyMath(project.getId(), projectAmountEntity, project.getContractId());
        // 对项目表和合同表进行更新
        ProjectEntity oneByProjectId = this.getById(projectAmountEntity.getProjectId());
        oneByProjectId.setNetvalue(netValue);
        oneByProjectId.setTotalMoney(oaProjectAmountVo.getTotalMoney());
        this.updateById(oneByProjectId);

        //Todo 处理项目金额修改日志
        ProjectMoneyLogEntity projectMoneyLogEntity = new ProjectMoneyLogEntity();
        projectMoneyLogEntity.setProjectId(project.getId());
        projectMoneyLogEntity.setIdentifier(project.getIdentifier());
        //TODO 旧
        projectMoneyLogEntity.setTotalMoney(oaProjectAmountVo.getTotalMoney());
        projectMoneyLogEntity.setSubprojectFee(oaProjectAmountVo.getSubprojectFee());
        projectMoneyLogEntity.setVirtualTax(oaProjectAmountVo.getVirtualTax());
        projectMoneyLogEntity.setServiceCharge(oaProjectAmountVo.getServiceCharge());
        projectMoneyLogEntity.setOtherExpenses(oaProjectAmountVo.getOtherExpenses());
        projectMoneyLogEntity.setCommission(oaProjectAmountVo.getCommission());
        projectMoneyLogEntity.setEvaluationFee(oaProjectAmountVo.getEvaluationFee());

        //TODO 新
        projectMoneyLogEntity.setOldTotalMoney(oaProjectAmountVo1.getTotalMoney());
        projectMoneyLogEntity.setOldCommission(oaProjectAmountVo1.getCommission());
        projectMoneyLogEntity.setOldServiceCharge(oaProjectAmountVo1.getServiceCharge());
        projectMoneyLogEntity.setOldVirtualTax(oaProjectAmountVo1.getVirtualTax());
        projectMoneyLogEntity.setOldSubprojectFee(oaProjectAmountVo1.getSubprojectFee());
        projectMoneyLogEntity.setOldOtherExpenses(oaProjectAmountVo1.getOtherExpenses());
        projectMoneyLogEntity.setOldEvaluationFee(oaProjectAmountVo1.getEvaluationFee());
        projectMoneyLogService.save(projectMoneyLogEntity);
        //TODO {}
//        }
    }


    /**
     * TODO：绩效计算
     */
//    private void mathTicheng(ProjectEntity project){
//        PerCommissionService commissionService = SpringContextUtils.getBean("perCommissionService", PerCommissionService.class);
//        List<PerCommissionEntity> list = commissionService.getListByprojectId(project.getId());
//        if (list.size()>0){
//            String[] huanjingTeam = new String[]{"环境监测","环评监测","环境监督","环境验收","公共卫生检测","一次性用品用具检测","学校卫生检测","公卫监督","洁净区域检测","排污许可"};
//            String[] pingjiaTeam = new String[]{"预评","专篇","控评","现状"};
//            String[] jianpingTeam = new String[]{"检评","职卫监督"};
//
//            Map<String,List<PerCommissionEntity>> mapList = list.stream().collect(Collectors.groupingBy(PerCommissionEntity::getType));
//            if (Arrays.asList(huanjingTeam).contains(project.getType())){
//
//            }else if (Arrays.asList(huanjingTeam).contains(project.getType())){
//
//            }else if (Arrays.asList(huanjingTeam).contains(project.getType())){
//
//            }
//        }
//    }

    /**
     * 金额回填计算
     *
     * @param projectId
     * @param projectAmountEntity
     */
    private void projectMoneyMath(Long projectId, ProjectAmountEntity projectAmountEntity, Long contractId) {
        Float totalMoneySettled = 0f;            //已结算金额
        Float commissionSettled = 0f;            //已结算佣金
        Float evaluationSettled = 0f;            //已结算评审费
        Float serviceChargeSettled = 0f;        //已结算服务费
        Float subprojectSettled = 0f;            //已结算分包费
        Float otherExpensesSettled = 0f;        //已结算其他支出
        Float virtualTax = 0f;         //虚拟税费
        Float invoiceMoney = 0f;           //已开票金额

        //type的枚举值有: 1项目款、2发票、3佣金、4评审费、5服务费、6差旅招待提成、7业务提成、8采样提成、9检测提成、10报告编制提成、11报告评审提成、12采样提成(补采)、13分包费、14其它支出
        List<AccountEntity> accountList = accountService.listByProjectId(projectId);//根据项目ID获取收付款记录

        for (AccountEntity accountEntity : accountList) {
            if (accountEntity.getAcVirtualTax() != null && accountEntity.getAcVirtualTax() > 0) {
                virtualTax = virtualTax + accountEntity.getAcVirtualTax();
            }

            invoiceMoney = invoiceMoney + accountEntity.getInvoiceAmount();
            switch (accountEntity.getAcType()) {
                case 1:
                    totalMoneySettled = totalMoneySettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;
                case 3:
                    commissionSettled = commissionSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;
                case 4:
                    evaluationSettled = evaluationSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;
                case 5:
                    serviceChargeSettled = serviceChargeSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;
                case 13:
                    subprojectSettled = subprojectSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;
                case 14:
                    otherExpensesSettled = otherExpensesSettled + accountEntity.getAmount() + accountEntity.getAcVirtualTax();
                    break;

                default:
                    break;
            }
        }

        BigDecimal totalMoneyOutstanding = Number2Money.subtance(projectAmountEntity.getTotalMoney(), totalMoneySettled);    //未结算金额 项目金额totalMoney-已收款金额receiptMoney
        //支付款表中支出存储的应为负数，所以采样add
        BigDecimal commissionOutstanding = Number2Money.subtance(projectAmountEntity.getCommission(), commissionSettled);        //未结算佣金
        //System.out.println(project.getCommission()+ ",commissionOutstanding="+commissionOutstanding);
        //BigDecimal commissionOutstanding = newCommission BigDecimal(0);		//未结算佣金
        BigDecimal commissionRatio = new BigDecimal(0);//佣金比例,佣金/总金额  "0.00%"
        if (projectAmountEntity.getTotalMoney() != null && projectAmountEntity.getTotalMoney().compareTo(BigDecimal.ZERO) > 0) {
            commissionRatio = projectAmountEntity.getCommission().divide(projectAmountEntity.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
        }

        BigDecimal evaluationOutstanding = Number2Money.subtance(projectAmountEntity.getEvaluationFee(), evaluationSettled);        //未结算评审费
        BigDecimal serviceChargeOutstanding = Number2Money.subtance(projectAmountEntity.getServiceCharge(), serviceChargeSettled);        //未结算服务费
        BigDecimal subprojectOutstanding = Number2Money.subtance(projectAmountEntity.getSubprojectFee(), subprojectSettled);        //未结算分包费
        BigDecimal otherExpensesOutstanding = Number2Money.subtance(projectAmountEntity.getOtherExpenses(), otherExpensesSettled);        //未结算其他支出

        projectAmountEntity.setReceiptMoney(new BigDecimal(totalMoneySettled.toString()));            //已结算金额 已收款金额(元)

        projectAmountEntity.setNosettlementMoney(totalMoneyOutstanding);
        projectAmountEntity.setCommissionOutstanding(commissionOutstanding);
        projectAmountEntity.setCommissionRatio(commissionRatio);
        projectAmountEntity.setEvaluationOutstanding(evaluationOutstanding);
        projectAmountEntity.setServiceChargeOutstanding(serviceChargeOutstanding);
        projectAmountEntity.setSubprojectOutstanding(subprojectOutstanding);
        projectAmountEntity.setOtherExpensesOutstanding(otherExpensesOutstanding);

        projectAmountEntity.setVirtualTax(new BigDecimal(virtualTax.toString()));//虚拟税费
        projectAmountEntity.setInvoiceMoney(new BigDecimal(invoiceMoney.toString()));//已开票金额
        projectAmountService.updateById(projectAmountEntity);
        projectAmountService.saveMoneyByContractId(contractId);
    }

    /**
     * 通过项目编号获取项目信息
     *
     * @param identifier
     * @return
     */
    public ProjectEntity getProjectByIdentifier(String identifier) {
        ProjectEntity projectEntity = baseMapper.selectOne(new QueryWrapper<ProjectEntity>()
                .eq("identifier", identifier)
        );
        return projectEntity;
    }


    /**
     * 项目核算页面列表接口
     * @param projectAccountingDto
     * @return
     */
    public List<ProjectAccountingListVo> getAccountingList(ProjectAccountingDto projectAccountingDto){
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapper(projectAccountingDto);
        pageUtil2.startPage();
        List<ProjectAccountingListVo> list = baseMapper.getAccountingDataList(queryWrapper);
        dataProcessing(list);
        return list;
    }

    /**
     * 项目核算页面导出接口
     * @param projectAccountingDto
     * @return
     */
    public List<ProjectAccountingListVo> exportAccountingList(ProjectAccountingDto projectAccountingDto){
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapper(projectAccountingDto);
        List<ProjectAccountingListVo> list = baseMapper.getAccountingDataList(queryWrapper);
        dataProcessing(list);
        ProjectAccountingVo projectAccountingVo = this.getCountReturn(projectAccountingDto);
        ProjectAccountingListVo accountingListVo = new ProjectAccountingListVo();
        accountingListVo.setIdentifier("总计");
        accountingListVo.setToltalMoney(projectAccountingVo.getToltalMoney());
        accountingListVo.setNetvalue(projectAccountingVo.getNetvalue());
        accountingListVo.setCommission(projectAccountingVo.getCommission());
        accountingListVo.setEvaluationFee(projectAccountingVo.getEvaluationFee());
        accountingListVo.setSubprojectFee(projectAccountingVo.getSubprojectFee());
        accountingListVo.setServiceCharge(projectAccountingVo.getServiceCharge());
        accountingListVo.setOtherExpenses(projectAccountingVo.getOtherExpenses());
        accountingListVo.setYewu(projectAccountingVo.getYewu());
        accountingListVo.setKefu(projectAccountingVo.getKefu());
        accountingListVo.setCaiyang(projectAccountingVo.getCaiyang());
        accountingListVo.setQianfa(projectAccountingVo.getQianfa());
        accountingListVo.setBaogao(projectAccountingVo.getBaogao());
        accountingListVo.setJiance(projectAccountingVo.getJiance());
        accountingListVo.setBianzhi(projectAccountingVo.getBianzhi());
        accountingListVo.setShare(projectAccountingVo.getShare());
        accountingListVo.setEstimatedProfit(projectAccountingVo.getEstimatedProfit());
        list.add(accountingListVo);
        return list;
    }

    /**
     * 项目核算部分返回金额处理
     * @param list
     */
    private void dataProcessing(List<ProjectAccountingListVo> list){
        List<Long> projectIds = list.stream().distinct().map(ProjectAccountingListVo::getId).collect(Collectors.toList());
        Map<Long, List<NewCommissionEntity>> commissionMap = new HashMap<>();
        if (projectIds != null && projectIds.size() > 0) {
            NewCommissionService commissionService = SpringContextUtils.getBean("newCommissionService", NewCommissionService.class);
            List<NewCommissionEntity> commissionEntityList = commissionService.getListByProjectIdS(projectIds);
            if (commissionEntityList != null && commissionEntityList.size() > 0) {
                commissionMap = commissionEntityList.stream().collect(Collectors.groupingBy(NewCommissionEntity::getProjectId));
            }
        } else {
            commissionMap = new HashMap<>();
        }
        for (ProjectAccountingListVo action:list) {
            if (commissionMap.get(action.getId()) != null) {
                List<NewCommissionEntity> commissionList = commissionMap.get(action.getId());
                Map<String, List<NewCommissionEntity>> amountListMap = commissionList.stream().collect(Collectors.groupingBy(NewCommissionEntity::getCommissionType));
                action.setYewu(amountListMap.get("业务提成") != null ? amountListMap.get("业务提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//业务提成
                action.setKefu(amountListMap.get("客服提成") != null ? amountListMap.get("客服提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//客服提成
                action.setCaiyang(amountListMap.get("采样提成") != null ? amountListMap.get("采样提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//采样提成
                action.setQianfa(amountListMap.get("签发提成") != null ? amountListMap.get("签发提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//签发提成
                action.setBaogao(amountListMap.get("报告提成") != null ? amountListMap.get("报告提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//报告提成
                action.setJiance(amountListMap.get("检测提成") != null ? amountListMap.get("检测提成").get(0).getAccrualAmount() : BigDecimal.ZERO);//检测提成
                action.setBianzhi(amountListMap.get("报告编制") != null ? amountListMap.get("报告编制").get(0).getAccrualAmount() : BigDecimal.ZERO);//报告编制
            } else {
                action.setYewu(BigDecimal.ZERO);//业务提成
                action.setKefu(BigDecimal.ZERO);//采样提成
                action.setCaiyang(BigDecimal.ZERO);//报告提成
                action.setQianfa(BigDecimal.ZERO);//检测提成
                action.setBaogao(BigDecimal.ZERO);//检测提成
                action.setJiance(BigDecimal.ZERO);//检测提成
                action.setBianzhi(BigDecimal.ZERO);//检测提成
            }
            //项目金额
            BigDecimal totalMoney = StringUtils.checkValNotNull(action.getToltalMoney()) ? action.getToltalMoney() : BigDecimal.ZERO;
            //项目净值
            BigDecimal netvalue = StringUtils.checkValNotNull(action.getNetvalue()) ? action.getNetvalue() : BigDecimal.ZERO;
//            //业务费
//            BigDecimal commission = StringUtils.checkValNotNull(action.getCommission())?action.getCommission():BigDecimal.ZERO;
//            //评审费
//            BigDecimal evaluationFee = StringUtils.checkValNotNull(action.getEvaluationFee())?action.getEvaluationFee():BigDecimal.ZERO;
//            //分包费
//            BigDecimal subprojectFee = StringUtils.checkValNotNull(action.getSubprojectFee())?action.getSubprojectFee():BigDecimal.ZERO;
//            //服务费用
//            BigDecimal serviceCharge = StringUtils.checkValNotNull(action.getServiceCharge())?action.getServiceCharge():BigDecimal.ZERO;
//            //其他支出
//            BigDecimal otherExpenses = StringUtils.checkValNotNull(action.getOtherExpenses())?action.getOtherExpenses():BigDecimal.ZERO;

            //综合成本公摊
            BigDecimal proportion = new BigDecimal("0.45");
            BigDecimal share = totalMoney.multiply(proportion);//项目金额*计算比例

            //预计利润=项目净值-各类业绩提成-综合公摊
            BigDecimal estimatedProfit = netvalue.subtract(share).subtract(action.getYewu()).subtract(action.getKefu()).subtract(action.getCaiyang()).subtract(action.getQianfa()).subtract(action.getBaogao()).subtract(action.getJiance()).subtract(action.getBianzhi());
            action.setShare(share);
            action.setEstimatedProfit(estimatedProfit);
        }
    }


    /**
     * 项目核算统计
     * @param projectAccountingDto
     * @return
     */
    public ProjectAccountingVo getCountReturn(ProjectAccountingDto projectAccountingDto){
        QueryWrapper<ProjectEntity> queryWrapper = queryWrapper(projectAccountingDto);
        ProjectAccountingVo projectAccountingVo = baseMapper.getAccounting(queryWrapper);
        BigDecimal toltalMoney = projectAccountingVo.getToltalMoney();
        BigDecimal netvalue = projectAccountingVo.getNetvalue();
        //综合成本公摊
        BigDecimal proportion = new BigDecimal("0.45");
        BigDecimal share = toltalMoney.multiply(proportion);//项目金额*计算比例
        //预计利润=项目净值-各类业绩提成-综合公摊
        BigDecimal estimatedProfit = netvalue.subtract(share).subtract(projectAccountingVo.getYewu())
                .subtract(projectAccountingVo.getKefu()).subtract(projectAccountingVo.getCaiyang())
                .subtract(projectAccountingVo.getQianfa()).subtract(projectAccountingVo.getBaogao())
                .subtract(projectAccountingVo.getJiance()).subtract(projectAccountingVo.getBianzhi());
        projectAccountingVo.setShare(share);
        projectAccountingVo.setEstimatedProfit(estimatedProfit);
        return projectAccountingVo;
    }


    private QueryWrapper<ProjectEntity> queryWrapper(ProjectAccountingDto projectAccountingDto){
        String identifier = projectAccountingDto.getIdentifier();
        String companyOrder = projectAccountingDto.getCompanyOrder();
        String businessSource = projectAccountingDto.getBusinessSource();
        String signDateStart = projectAccountingDto.getSignDateStart();
        String signDateEnd = projectAccountingDto.getSignDateEnd();
        String salesmen = projectAccountingDto.getSalesmen();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(identifier),"p.identifier",identifier);
        queryWrapper.like(StringUtils.isNotBlank(salesmen),"p.salesmen",salesmen);
        queryWrapper.eq(StringUtils.isNotBlank(companyOrder),"p.company_order",companyOrder);
        queryWrapper.eq(StringUtils.isNotBlank(businessSource),"p.business_source",businessSource);
        queryWrapper.ge(StringUtils.isNotBlank(signDateStart),"pd.sign_date",signDateStart);
        queryWrapper.le(StringUtils.isNotBlank(signDateEnd),"pd.sign_date",signDateEnd);
        return queryWrapper;
    }


}
