package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.mapper.AccountMapper;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.*;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.MoneyVo;
import may.yuntian.anlian.vo.QueryProjectVo;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收付款记录
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-11-05
 */
@Service("accountService")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {

    /**
     * 项目信息管理
     */
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectAmountService projectAmountService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectProceduresService projectProceduresService;

    @Override
    public List<AccountEntity> queryPage(Map<String, Object> params) {
        // 支持分流
        String subjection = (String) params.get("subjection");
        params.put("subjection", subjection);
        QueryWrapper<AccountEntity> queryWrapper = queryWrapperByParams(params);
        pageUtil2.startPage();
        List<AccountEntity> list = baseMapper.selectList(queryWrapper);
        for (AccountEntity accountEntity : list) {
            Long projectId = accountEntity.getProjectId();
            ProjectEntity project = projectMapper.selectById(projectId);
            accountEntity.setProject(project);
        }
        return list;
    }

    /**
     * 根据项目ID获取收付款记录
     *
     * @param projectId
     * @return
     */
    @Override
    public List<AccountEntity> listByProjectId(Long projectId) {

        List<AccountEntity> list = this.list(new QueryWrapper<AccountEntity>().eq("project_id", projectId).orderByDesc("id"));

        return list;
    }

    /**
     * 获取最新的项目款收款日期
     *
     * @param projectId
     * @return
     */
    @Override
    public Date getHappenTime(Long projectId) {
        Date date = null;
        List<AccountEntity> accountList = this.list(new QueryWrapper<AccountEntity>().eq("project_id", projectId).eq("ac_type", 1).orderByDesc("happen_time"));
        if (accountList != null && accountList.size() > 0) {
            date = accountList.get(0).getHappenTime();
        }
        return date;
    }


    /**
     * 根据项目ID与款项类别查询收付款记录
     *
     * @param projectId
     * @param type
     * @return 项目收款金额
     */
    @Override
    public BigDecimal getAmountByProjectIdAndType(Long projectId, Integer type) {

        Map<String, Object> map = this.getMap(new QueryWrapper<AccountEntity>()
                .select("sum(amount) as amount")
                .eq("project_id", projectId)
                .eq("ac_type", type));

        BigDecimal amount = new BigDecimal(0);
        if (map != null) {
            amount = (BigDecimal) map.get("amount");
        }
        return amount;
    }

    /**
     * 显示全部收付款的信息
     *
     * @return
     */
    @Override
    public List<AccountEntity> listAll(Map<String, Object> params) {
//        // 分流数据处理
//        String subjection = ShiroUtils.getUserEntity().getSubjection();
//        params.put("subjection", subjection);
        List<AccountEntity> list = this.list(queryWrapperByParams(params));
        List<Long> projectIds = list.stream().map(AccountEntity::getProjectId).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(projectIds)){
            List<ProjectEntity> projectEntityList = projectMapper.selectBatchIds(projectIds);
            Map<Long, List<ProjectEntity>> proMap = projectEntityList.stream().collect(Collectors.groupingBy(ProjectEntity::getId));
            list.forEach(action -> {
                Long projectId = action.getProjectId();
                action.setProject(proMap.get(projectId) == null ? new ProjectEntity() : proMap.get(projectId).get(0));
            });
        }
        return list;
    }




    /**
     * 根据查询条件统计金额
     *
     * @param map
     * @return
     */
    @Override
    public  MoneyVo sumAmountByWrapper(Map<String, Object> map){
        MoneyVo map1 = new MoneyVo();
        MoneyVo map2 = new MoneyVo();
        map1 = baseMapper.sumAmountByMyWrapper(queryWrapperByParams(map));
        map2 = baseMapper.sumtotalMoneyByMyWrapper(queryWrapperByParams(map));
//       System.out.println("==========1"+ map1);
//       System.out.println("==========111112"+ map2);
        //.apply("p1.id = a.project_id")
        if(map2!=null) {
            map1.setTotalMoney(map2.getTotalMoney());
        }
//       System.out.println("==========/-//---/-3"+ map1);
        return map1;
    }


    /**
     * 收付款查询条件
     *
     * @param params
     * @return
     */
    private QueryWrapper<AccountEntity> queryWrapperByParams(Map<String, Object> params) {
        String type = (String) params.get("acType");//款项类别
        String happenTimeStart = (String) params.get("happenTimeStart");//收/付时间 开始
        String happenTimeEnd = (String) params.get("happenTimeEnd");//收/付时间 结束
        String amountMax = (String) params.get("amountMax");//大于收付款金额
        String amountMin = (String) params.get("amountMin");//小于收付款金额
        String invoiceAmountMax = (String) params.get("invoiceAmountMax");//大于开票金额
        String invoiceAmountMin = (String) params.get("invoiceAmountMin");//小于开票金额
        String settleStyle = (String) params.get("settleStyle");//结算方式
        String invoiceNumber = (String) params.get("invoiceNumber");//发票号码
        String remarks = (String) params.get("remarks");//备注

        String subjection = (String) params.get("subjection");//权限过滤

        String province = (String) params.get("province");//省
        String city = (String) params.get("city");//市
        String area = (String) params.get("area");//区

        String businessSource = (String) params.get("businessSource");//业务来源
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索
        String salesmen = (String) params.get("salesmen");//业务员    模糊搜索
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String companyOrder = (String) params.get("companyOrder");//项目隶属公司
        String types = (String) params.get("types");//项目类型
        String entrustCompany = (String) params.get("entrustCompany");

        String orderbyColumns = (String) params.get("orderby");//排序  数据库字段名 subject_fee
        String isAscString = (String) params.get("isAsc");//正序倒叙 true为正序 false为倒叙

        Boolean isAsc = Boolean.valueOf(isAscString);


        String createTimeStart = (String) params.get("createTimeStart");//录入时间 开始
        String createTimeEnd = (String) params.get("createTimeEnd");//录入时间 结束

        ProjectService projectService = SpringContextUtils.getBean("projectService", ProjectService.class);//项目信息管理

        List<Long> projectIds = new ArrayList<>();
        if (StringUtils.isNotBlank(subjection) || StringUtils.isNotBlank(company) || StringUtils.isNotBlank(entrustCompany) || StringUtils.checkValNotNull(businessSource) || StringUtils.checkValNotNull(salesmen) || StringUtils.checkValNotNull(identifier)
                || StringUtils.checkValNotNull(companyOrder) || StringUtils.checkValNotNull(types) || StringUtils.checkValNotNull(province) || StringUtils.checkValNotNull(city) || StringUtils.checkValNotNull(area)) {
            QueryProjectVo queryVo = new QueryProjectVo();
            queryVo.setCompany(company);
            queryVo.setBusinessSource(businessSource);
            queryVo.setIdentifier(identifier);
            queryVo.setSalesmen(salesmen);
            queryVo.setCompanyOrder(companyOrder);
            queryVo.setTypes(types);
            queryVo.setProvince(province);
            queryVo.setCity(city);
            queryVo.setArea(area);
            queryVo.setSubjection(subjection);
            queryVo.setEntrustCompany(entrustCompany);
            projectIds = projectService.getProjectIdsByParams(queryVo);
        }
        if (projectIds.size() <= 0) {
            projectIds.add(Long.valueOf(0));
        }
//		System.out.println(projectIds.toString());
        QueryWrapper<AccountEntity> queryWrapper = new QueryWrapper<AccountEntity>()
                .eq(StringUtils.isNotBlank(type), "ac_type", type)
                .eq(StringUtils.isNotBlank(settleStyle), "settle_style", settleStyle)
                .ge(StringUtils.checkValNotNull(amountMin), "amount", amountMin)
                .le(StringUtils.checkValNotNull(amountMax), "amount", amountMax)
                .ge(StringUtils.checkValNotNull(invoiceAmountMin), "invoice_amount", invoiceAmountMin)
                .le(StringUtils.checkValNotNull(invoiceAmountMax), "invoice_amount", invoiceAmountMax)
                .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                .like(StringUtils.isNotBlank(remarks), "ac_remarks", remarks)
                .ge(StringUtils.checkValNotNull(happenTimeStart), "happen_time", happenTimeStart)
                .le(StringUtils.checkValNotNull(happenTimeEnd), "happen_time", happenTimeEnd)

                .ge(StringUtils.checkValNotNull(createTimeStart), "create_time", createTimeStart)
                .le(StringUtils.checkValNotNull(createTimeEnd), "create_time", createTimeEnd)
//   			.between(StringUtils.checkValNotNull(happenTimeEnd),"happen_time", happenTimeStart, happenTimeEnd)
//                .in(projectIds.size() > 0, "project_id", projectIds);
                .in("project_id", projectIds);

        if (StringUtils.checkValNull(orderbyColumns)) {
            orderbyColumns = "project_id";
            isAsc = false;
        }
        queryWrapper.orderBy(StringUtils.checkValNotNull(orderbyColumns), isAsc, orderbyColumns);

        return queryWrapper;
    }

    /**
     * 收付款收完款项后金额回填以供统计
     *
     * @param projectId
     */
    @Override
    public void amountBackfill(Long projectId) {
        Float totalMoneySettled = 0f;            //已结算金额
        Float commissionSettled = 0f;            //已结算佣金
        Float evaluationSettled = 0f;            //已结算评审费
        Float serviceChargeSettled = 0f;        //已结算服务费
        Float subprojectSettled = 0f;            //已结算分包费
        Float otherExpensesSettled = 0f;        //已结算其他支出
        Float virtualTax = 0f;         //虚拟税费
        Float invoiceMoney = 0f;           //已开票金额

        ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
        //type的枚举值有: 1项目款、2发票、3佣金、4评审费、5服务费、6差旅招待提成、7业务提成、8采样提成、9检测提成、10报告编制提成、11报告评审提成、12采样提成(补采)、13分包费、14其它支出
        List<AccountEntity> accountList = this.listByProjectId(projectId);//根据项目ID获取收付款记录

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

//		BigDecimal totalMoneyOutstanding = project.getTotalMoney().subtract(newCommission BigDecimal(totalMoneySettled));	//未结算金额 项目金额totalMoney-已收款金额receiptMoney
        BigDecimal totalMoneyOutstanding = Number2Money.subtance(projectAmountEntity.getTotalMoney(), totalMoneySettled);    //未结算金额 项目金额totalMoney-已收款金额receiptMoney
        //支付款表中支出存储的应为负数，所以采样add
//		BigDecimal commissionOutstanding = project.getCommission().subtract(newCommission BigDecimal(commissionSettled));		//未结算佣金
        BigDecimal commissionOutstanding = Number2Money.subtance(projectAmountEntity.getCommission(), commissionSettled);        //未结算佣金
        //System.out.println(project.getCommission()+ ",commissionOutstanding="+commissionOutstanding);
        //BigDecimal commissionOutstanding = newCommission BigDecimal(0);		//未结算佣金
//		BigDecimal commissionRatio = project.getCommission().divide(project.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(newCommission BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
        BigDecimal commissionRatio = new BigDecimal(0);//佣金比例,佣金/总金额  "0.00%"
        if (projectAmountEntity.getTotalMoney() != null && projectAmountEntity.getTotalMoney().compareTo(BigDecimal.ZERO) > 0) {
            commissionRatio = projectAmountEntity.getCommission().divide(projectAmountEntity.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
        }

//		BigDecimal evaluationOutstanding = project.getEvaluationFee().subtract(newCommission BigDecimal(evaluationSettled));		//未结算评审费
        BigDecimal evaluationOutstanding = Number2Money.subtance(projectAmountEntity.getEvaluationFee(), evaluationSettled);        //未结算评审费
//		BigDecimal serviceChargeOutstanding = project.getServiceCharge().subtract(newCommission BigDecimal(serviceChargeSettled));		//未结算服务费
        BigDecimal serviceChargeOutstanding = Number2Money.subtance(projectAmountEntity.getServiceCharge(), serviceChargeSettled);        //未结算服务费
//		BigDecimal subprojectOutstanding = project.getSubprojectFee().subtract(newCommission BigDecimal(subprojectSettled));		//未结算分包费
        BigDecimal subprojectOutstanding = Number2Money.subtance(projectAmountEntity.getSubprojectFee(), subprojectSettled);        //未结算分包费
//		BigDecimal otherExpensesOutstanding = project.getOtherExpenses().subtract(newCommission BigDecimal(otherExpensesSettled));		//未结算其他支出
        BigDecimal otherExpensesOutstanding = Number2Money.subtance(projectAmountEntity.getOtherExpenses(), otherExpensesSettled);        //未结算其他支出

        projectAmountEntity.setReceiptMoney(new BigDecimal(totalMoneySettled.toString()));            //已结算金额 已收款金额(元)
//        projectAmountEntity.setCommissionSettled(newCommission BigDecimal(commissionSettled.toString()));			//已结算佣金
//        projectAmountEntity.setEvaluationSettled(newCommission BigDecimal(evaluationSettled.toString()));			//已结算评审费
//        projectAmountEntity.setServiceChargeSettled(newCommission BigDecimal(serviceChargeSettled.toString()));		//已结算服务费
//        projectAmountEntity.setSubprojectSettled(newCommission BigDecimal(subprojectSettled.toString()));			//已结算分包费
//        projectAmountEntity.setOtherExpensesSettled(newCommission BigDecimal(otherExpensesSettled.toString()));		//已结算其他支出

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

        if (totalMoneyOutstanding.compareTo(BigDecimal.ZERO) == 0) {
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (projectDateEntity != null && projectDateEntity.getReportFiling() != null) {
                ProjectEntity project = projectMapper.selectById(projectId);
                if (project.getStatus() < 70) {
                    project.setStatus(70);
                    projectMapper.updateById(project);
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(70);
                    projectProceduresService.save(proceduresEntity);
                }
            }
        }

    }

}
