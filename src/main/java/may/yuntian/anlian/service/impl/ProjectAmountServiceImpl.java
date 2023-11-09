package may.yuntian.anlian.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.service.ContractService;
import may.yuntian.anlian.vo.ProjectAmountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;


import may.yuntian.anlian.mapper.ProjectAmountMapper;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import may.yuntian.anlian.service.ProjectAmountService;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@Service("projectAmountService")
public class ProjectAmountServiceImpl extends ServiceImpl<ProjectAmountMapper, ProjectAmountEntity> implements ProjectAmountService {

    @Autowired
    private ContractService contractService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	
        IPage<ProjectAmountEntity> page = this.page(
                new Query<ProjectAmountEntity>().getPage(params),
                new QueryWrapper<ProjectAmountEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 将同一合同的项目各项金额的总和回填到合同信息中
     */
    public void saveMoneyByContractId(Long contractId) {
        Map<String, BigDecimal> projectMap = baseMapper.selectSumMoneyByContractId(contractId);
        if(projectMap!=null) {
            ContractEntity contract = contractService.getById(contractId);
            contract.setTotalMoney(projectMap.get("toltalMoney"));
            contract.setCommission(projectMap.get("commission"));
            contract.setEvaluationFee(projectMap.get("evaluationFee"));
            contract.setSubcontractFee(projectMap.get("subprojectFee"));
            contract.setServiceCharge(projectMap.get("serviceCharge"));
            contract.setOtherExpenses(projectMap.get("otherExpenses"));
            contract.setNetvalue(projectMap.get("netvalue"));
            contractService.updateById(contract);
        }

    }

    /**
     * 根据项目ID查询是否已经存在于项目金额信息中
     * @param projectId 项目ID
     * @return boolean
     */
    public Boolean notExistPlanByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ProjectAmountEntity>().eq("project_id", projectId));
        if(count>0)
            return false;
        else
            return true;
    }

    /**
     * 通过项目ID获取项目相关金额信息
     * @param projectId
     * @return
     */
    public ProjectAmountEntity getOneByProjectId(Long projectId){
        ProjectAmountEntity projectAmountEntity = baseMapper.selectOne(new QueryWrapper<ProjectAmountEntity>().eq("project_id",projectId));
        return projectAmountEntity;
    }

    public List<ProjectAmountEntity> selectListByProjectIds(List<Long> ids){
        List<ProjectAmountEntity> list = baseMapper.selectList(new QueryWrapper<ProjectAmountEntity>().in("project_id",ids));
        return list;
    }

    /**
     * 根据条件查询项目ID列表
     */
    public List<Long> getProjectIdsByParams(ProjectAmountVo queryVo) {
        List list = baseMapper.selectObjs(queryWrapperByParams(queryVo).select("project_id"));

        return (List<Long>)list;
    }


    /**
     * 用于项目金额信息的查询条件的处理
     * @param params
     * @return
     */
    private QueryWrapper<ProjectAmountEntity> queryWrapperByParams(ProjectAmountVo params){
//        String totalMoney = params.("totalMoney");//项目金额
        String totalMoneyMin = params.getTotalMoneyMin();//小于项目金额
        String totalMoneyMax = params.getTotalMoneyMax();//大于项目金额

//    	String commission = params.("commission");//佣金
        String commissionMin = params.getCommissionMin();//小于佣金
        String commissionMax = params.getCommissionMax();//大于佣金

//    	String evaluationFee = params.("evaluationFee");//评审费
        String evaluationFeeMin = params.getEvaluationFeeMin();//小于评审费
        String evaluationFeeMax = params.getEvaluationFeeMax();//大于评审费

//    	String subprojectFee = params.("subprojectFee");//分包费
        String subprojectFeeMin = params.getSubprojectFeeMin();//小于分包费
        String subprojectFeeMax = params.getSubprojectFeeMax();//大于分包费

//    	String serviceCharge = params.("serviceCharge");//服务费
        String serviceChargeMin = params.getServiceChargeMin();//小于服务费
        String serviceChargeMax = params.getServiceChargeMax();//大于服务费

//    	String otherExpenses = params.("otherExpenses");//其他支出
        String otherExpensesMin = params.getOtherExpensesMin();//小于其他支出
        String otherExpensesMax = params.getOtherExpensesMax();//大于其他支出

//    	String netvalue = params.("netvalue");//项目净值
        String netvalueMin = params.getNetvalueMin();//小于项目净值
        String netvalueMax = params.getNetvalueMax();//大于项目净值

//    	String receiptMoney = params.("receiptMoney");//已收款金额
        String receiptMoneyMin = params.getReceiptMoneyMin();//小于已收款金额
        String receiptMoneyMax = params.getReceiptMoneyMax();//大于已收款金额

//    	String nosettlementMoney = params.("nosettlementMoney");//已收款金额
        String nosettlementMoneyMin = params.getNosettlementMoneyMin();//小于未结算金额
        String nosettlementMoneyMax = params.getNosettlementMoneyMax();//大于未结算金额

//    	String invoiceMoney = params.("invoiceMoney");//已开票金额
        String invoiceMoneyMin = params.getInvoiceMoneyMin();//小于已开票金额
        String invoiceMoneyMax = params.getInvoiceMoneyMax();//大于已开票金额

//    	String virtualTax = params.("virtualTax");//虚拟税费
        String virtualTaxMin = params.getVirtualTaxMin();//小于虚拟税费
        String virtualTaxMax = params.getVirtualTaxMax();//大于虚拟税费



        QueryWrapper<ProjectAmountEntity> queryWrapper = new QueryWrapper<ProjectAmountEntity>()
                //    			.eq(StringUtils.checkValNotNull(totalMoney),"total_money", totalMoney)
                .ge(StringUtils.checkValNotNull(totalMoneyMin),"total_money", totalMoneyMin)
                .le(StringUtils.checkValNotNull(totalMoneyMax),"total_money", totalMoneyMax)

//    			.eq(StringUtils.checkValNotNull(commission),"commission", commission)
                .ge(StringUtils.checkValNotNull(commissionMin),"commission", commissionMin)
                .le(StringUtils.checkValNotNull(commissionMax),"commission", commissionMax)

//    			.eq(StringUtils.checkValNotNull(evaluationFee),"evaluation_fee", evaluationFee)
                .ge(StringUtils.checkValNotNull(evaluationFeeMin),"evaluation_fee", evaluationFeeMin)
                .le(StringUtils.checkValNotNull(evaluationFeeMax),"evaluation_fee", evaluationFeeMax)

//    			.eq(StringUtils.checkValNotNull(subprojectFee),"subproject_fee", subprojectFee)
                .ge(StringUtils.checkValNotNull(subprojectFeeMin),"subproject_fee", subprojectFeeMin)
                .le(StringUtils.checkValNotNull(subprojectFeeMax),"subproject_fee", subprojectFeeMax)

//    			.eq(StringUtils.checkValNotNull(serviceCharge),"service_charge", serviceCharge)
                .ge(StringUtils.checkValNotNull(serviceChargeMin),"service_charge", serviceChargeMin)
                .le(StringUtils.checkValNotNull(serviceChargeMax),"service_charge", serviceChargeMax)

//    			.eq(StringUtils.checkValNotNull(otherExpenses),"other_expenses", otherExpenses)
                .ge(StringUtils.checkValNotNull(otherExpensesMin),"other_expenses", otherExpensesMin)
                .le(StringUtils.checkValNotNull(otherExpensesMax),"other_expenses", otherExpensesMax)

//    			.eq(StringUtils.checkValNotNull(netvalue),"netvalue", netvalue)
                .ge(StringUtils.checkValNotNull(netvalueMin),"netvalue", netvalueMin)
                .le(StringUtils.checkValNotNull(netvalueMax),"netvalue", netvalueMax)

//    			.eq(StringUtils.checkValNotNull(receiptMoney),"receipt_money", receiptMoney)
                .ge(StringUtils.checkValNotNull(receiptMoneyMin),"receipt_money", receiptMoneyMin)
                .le(StringUtils.checkValNotNull(receiptMoneyMax),"receipt_money", receiptMoneyMax)

//    			.eq(StringUtils.checkValNotNull(nosettlementMoney),"nosettlement_money", nosettlementMoney)
                .ge(StringUtils.checkValNotNull(nosettlementMoneyMin),"nosettlement_money", nosettlementMoneyMin)
                .le(StringUtils.checkValNotNull(nosettlementMoneyMax),"nosettlement_money", nosettlementMoneyMax)

//    			.eq(StringUtils.checkValNotNull(invoiceMoney),"invoice_money", invoiceMoney)
                .ge(StringUtils.checkValNotNull(invoiceMoneyMin),"invoice_money", invoiceMoneyMin)
                .le(StringUtils.checkValNotNull(invoiceMoneyMax),"invoice_money", invoiceMoneyMax)

//    			.eq(StringUtils.checkValNotNull(virtualTax),"virtual_tax", virtualTax)
                .ge(StringUtils.checkValNotNull(virtualTaxMin),"virtual_tax", virtualTaxMin)
                .le(StringUtils.checkValNotNull(virtualTaxMax),"virtual_tax", virtualTaxMax);
        return queryWrapper;
    }

}
