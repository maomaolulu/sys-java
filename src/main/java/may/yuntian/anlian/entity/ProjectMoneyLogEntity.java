package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 项目金额修改日志
 * 
 * @author LiXin
 * @email ''
 * @date 2023-04-03 08:38:30
 */
@Data
@TableName("al_project_money_log")
public class ProjectMoneyLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 原项目金额
	 */
	private BigDecimal oldTotalMoney;
	/**
	 * 原佣金金额(元)(业务费)
	 */
	private BigDecimal oldCommission;
	/**
	 * 原评审费(元) 
	 */
	private BigDecimal oldEvaluationFee;
	/**
	 * 原分包费(元)
	 */
	private BigDecimal oldSubprojectFee;
	/**
	 * 原服务费用(元)
	 */
	private BigDecimal oldServiceCharge;
	/**
	 * 原其他支出(元)
	 */
	private BigDecimal oldOtherExpenses;
	/**
	 * 原虚拟税费(元)
	 */
	private BigDecimal oldVirtualTax;
	/**
	 * 项目金额(元)
	 */
	private BigDecimal totalMoney;
	/**
	 * 佣金金额(元)(业务费)
	 */
	private BigDecimal commission;
	/**
	 * 评审费(元)
	 */
	private BigDecimal evaluationFee;
	/**
	 * 分包费(元)
	 */
	private BigDecimal subprojectFee;
	/**
	 * 服务费用(元)
	 */
	private BigDecimal serviceCharge;
	/**
	 * 其他支出(元)
	 */
	private BigDecimal otherExpenses;
	/**
	 * 虚拟税费(元)
	 */
	private BigDecimal virtualTax;

	/**
	 * 设置：自增ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增ID
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
	 * 设置：项目编号
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * 获取：项目编号
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * 设置：原项目金额
	 */
	public void setOldTotalMoney(BigDecimal oldTotalMoney) {
		this.oldTotalMoney = oldTotalMoney;
	}
	/**
	 * 获取：原项目金额
	 */
	public BigDecimal getOldTotalMoney() {
		return oldTotalMoney;
	}
	/**
	 * 设置：原佣金金额(元)(业务费)
	 */
	public void setOldCommission(BigDecimal oldCommission) {
		this.oldCommission = oldCommission;
	}
	/**
	 * 获取：原佣金金额(元)(业务费)
	 */
	public BigDecimal getOldCommission() {
		return oldCommission;
	}
	/**
	 * 设置：原评审费(元) 
	 */
	public void setOldEvaluationFee(BigDecimal oldEvaluationFee) {
		this.oldEvaluationFee = oldEvaluationFee;
	}
	/**
	 * 获取：原评审费(元) 
	 */
	public BigDecimal getOldEvaluationFee() {
		return oldEvaluationFee;
	}
	/**
	 * 设置：原分包费(元)
	 */
	public void setOldSubprojectFee(BigDecimal oldSubprojectFee) {
		this.oldSubprojectFee = oldSubprojectFee;
	}
	/**
	 * 获取：原分包费(元)
	 */
	public BigDecimal getOldSubprojectFee() {
		return oldSubprojectFee;
	}
	/**
	 * 设置：原服务费用(元)
	 */
	public void setOldServiceCharge(BigDecimal oldServiceCharge) {
		this.oldServiceCharge = oldServiceCharge;
	}
	/**
	 * 获取：原服务费用(元)
	 */
	public BigDecimal getOldServiceCharge() {
		return oldServiceCharge;
	}
	/**
	 * 设置：原其他支出(元)
	 */
	public void setOldOtherExpenses(BigDecimal oldOtherExpenses) {
		this.oldOtherExpenses = oldOtherExpenses;
	}
	/**
	 * 获取：原其他支出(元)
	 */
	public BigDecimal getOldOtherExpenses() {
		return oldOtherExpenses;
	}
//	/**
//	 * 设置：原虚拟税费(元)
//	 */
//	public void setOldVirtualTax(BigDecimal oldVirtualTax) {
//		this.oldVirtualTax = oldVirtualTax;
//	}
//	/**
//	 * 获取：原虚拟税费(元)
//	 */
//	public BigDecimal getOldVirtualTax() {
//		return oldVirtualTax;
//	}
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
	 * 设置：佣金金额(元)(业务费)
	 */
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	/**
	 * 获取：佣金金额(元)(业务费)
	 */
	public BigDecimal getCommission() {
		return commission;
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
//	/**
//	 * 设置：虚拟税费(元)
//	 */
//	public void setVirtualTax(BigDecimal virtualTax) {
//		this.virtualTax = virtualTax;
//	}
//	/**
//	 * 获取：虚拟税费(元)
//	 */
//	public BigDecimal getVirtualTax() {
//		return virtualTax;
//	}
}
