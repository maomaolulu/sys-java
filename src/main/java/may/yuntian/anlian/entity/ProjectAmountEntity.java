package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author LiXin
 * @email
 * @date 2022-01-10 14:43:43
 */
@TableName("al_project_amount")
public class ProjectAmountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;

     /**
     * 合同ID
     */
    private Long contractId;

    /**
	 * 项目金额(元)
	 */
	private BigDecimal totalMoney;
	/**
	 * 已收款金额(元)
	 */
	private BigDecimal receiptMoney;
	/**
	 * 未结算金额
	 */
	private BigDecimal nosettlementMoney;
	/**
	 * 佣金金额(元)
	 */
	private BigDecimal commission;
	/**
	 * 佣金比例,佣金/总金额
	 */
	private BigDecimal commissionRatio;
	/**
	 * 佣金未结算金额
	 */
	private BigDecimal commissionOutstanding;
	/**
	 * 评审费(元)
	 */
	private BigDecimal evaluationFee;
	/**
	 * 未结算评审费(元)
	 */
	private BigDecimal evaluationOutstanding;
	/**
	 * 分包费(元)
	 */
	private BigDecimal subprojectFee;
	/**
	 * 未结算分包费(元)
	 */
	private BigDecimal subprojectOutstanding;
	/**
	 * 服务费用(元)
	 */
	private BigDecimal serviceCharge;
	/**
	 * 未结算服务费用(元)
	 */
	private BigDecimal serviceChargeOutstanding;
	/**
	 * 其他支出(元)
	 */
	private BigDecimal otherExpenses;
	/**
	 * 未结算的其他支出(元)
	 */
	private BigDecimal otherExpensesOutstanding;
	/**
	 * 已开票金额(元)
	 */
	private BigDecimal invoiceMoney;
	/**
	 * 项目净值(元)
	 */
	private BigDecimal netvalue;
	/**
	 * 虚拟税费(元)
	 */
	private BigDecimal virtualTax;

	/**
	 * 设置：ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
    /**
     * 设置：合同ID
     */
    public Long getContractId() {
        return contractId;
    }
    /**
     * 获取：合同ID
     */
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
	/**
	 * 设置：项目金额(元)
	 */
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	/**
	 * 获取：项目金额(元)
	 */
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	/**
	 * 设置：已收款金额(元)
	 */
	public void setReceiptMoney(BigDecimal receiptMoney) {
		this.receiptMoney = receiptMoney;
	}
	/**
	 * 获取：已收款金额(元)
	 */
	public BigDecimal getReceiptMoney() {
		return receiptMoney;
	}
	/**
	 * 设置：未结算金额
	 */
	public void setNosettlementMoney(BigDecimal nosettlementMoney) {
		this.nosettlementMoney = nosettlementMoney;
	}
	/**
	 * 获取：未结算金额
	 */
	public BigDecimal getNosettlementMoney() {
		return nosettlementMoney;
	}
	/**
	 * 设置：佣金金额(元)
	 */
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	/**
	 * 获取：佣金金额(元)
	 */
	public BigDecimal getCommission() {
		return commission;
	}
	/**
	 * 设置：佣金比例,佣金/总金额
	 */
	public void setCommissionRatio(BigDecimal commissionRatio) {
		this.commissionRatio = commissionRatio;
	}
	/**
	 * 获取：佣金比例,佣金/总金额
	 */
	public BigDecimal getCommissionRatio() {
		return commissionRatio;
	}
	/**
	 * 设置：佣金未结算金额
	 */
	public void setCommissionOutstanding(BigDecimal commissionOutstanding) {
		this.commissionOutstanding = commissionOutstanding;
	}
	/**
	 * 获取：佣金未结算金额
	 */
	public BigDecimal getCommissionOutstanding() {
		return commissionOutstanding;
	}
	/**
	 * 设置：评审费(元)
	 */
	public void setEvaluationFee(BigDecimal evaluationFee) {
		this.evaluationFee = evaluationFee;
	}
	/**
	 * 获取：评审费(元)
	 */
	public BigDecimal getEvaluationFee() {
		return evaluationFee;
	}
	/**
	 * 设置：未结算评审费(元)
	 */
	public void setEvaluationOutstanding(BigDecimal evaluationOutstanding) {
		this.evaluationOutstanding = evaluationOutstanding;
	}
	/**
	 * 获取：未结算评审费(元)
	 */
	public BigDecimal getEvaluationOutstanding() {
		return evaluationOutstanding;
	}
	/**
	 * 设置：分包费(元)
	 */
	public void setSubprojectFee(BigDecimal subprojectFee) {
		this.subprojectFee = subprojectFee;
	}
	/**
	 * 获取：分包费(元)
	 */
	public BigDecimal getSubprojectFee() {
		return subprojectFee;
	}
	/**
	 * 设置：未结算分包费(元)
	 */
	public void setSubprojectOutstanding(BigDecimal subprojectOutstanding) {
		this.subprojectOutstanding = subprojectOutstanding;
	}
	/**
	 * 获取：未结算分包费(元)
	 */
	public BigDecimal getSubprojectOutstanding() {
		return subprojectOutstanding;
	}
	/**
	 * 设置：服务费用(元)
	 */
	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	/**
	 * 获取：服务费用(元)
	 */
	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}
	/**
	 * 设置：未结算服务费用(元)
	 */
	public void setServiceChargeOutstanding(BigDecimal serviceChargeOutstanding) {
		this.serviceChargeOutstanding = serviceChargeOutstanding;
	}
	/**
	 * 获取：未结算服务费用(元)
	 */
	public BigDecimal getServiceChargeOutstanding() {
		return serviceChargeOutstanding;
	}
	/**
	 * 设置：其他支出(元)
	 */
	public void setOtherExpenses(BigDecimal otherExpenses) {
		this.otherExpenses = otherExpenses;
	}
	/**
	 * 获取：其他支出(元)
	 */
	public BigDecimal getOtherExpenses() {
		return otherExpenses;
	}
	/**
	 * 设置：未结算的其他支出(元)
	 */
	public void setOtherExpensesOutstanding(BigDecimal otherExpensesOutstanding) {
		this.otherExpensesOutstanding = otherExpensesOutstanding;
	}
	/**
	 * 获取：未结算的其他支出(元)
	 */
	public BigDecimal getOtherExpensesOutstanding() {
		return otherExpensesOutstanding;
	}
	/**
	 * 设置：已开票金额(元)
	 */
	public void setInvoiceMoney(BigDecimal invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}
	/**
	 * 获取：已开票金额(元)
	 */
	public BigDecimal getInvoiceMoney() {
		return invoiceMoney;
	}
	/**
	 * 设置：项目净值(元)
	 */
	public void setNetvalue(BigDecimal netvalue) {
		this.netvalue = netvalue;
	}
	/**
	 * 获取：项目净值(元)
	 */
	public BigDecimal getNetvalue() {
		return netvalue;
	}
	/**
	 * 设置：虚拟税费(元)
	 */
	public void setVirtualTax(BigDecimal virtualTax) {
		this.virtualTax = virtualTax;
	}
	/**
	 * 获取：虚拟税费(元)
	 */
	public BigDecimal getVirtualTax() {
		return virtualTax;
	}
}
