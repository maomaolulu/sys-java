package may.yuntian.anlianwage.newCommission.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 新绩效提成表
 * 
 * @author LiXin
 * @email ''
 * @date 2023-10-22 21:15:24
 */
@Data
@TableName("al_commission")
public class NewCommissionEntity implements Serializable {
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
	 * 提成人
	 */
	private String person;
	/**
	 * 隶属公司
	 */
	private String subjection;
	/**
	 * 所属部门
	 */
	private String dept;
	/**
	 * 提成类型
	 */
	private String commissionType;
    /**
     * 项目类型
     */
    private String projectType;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 计提日期
	 */
	private Date accrualDate;
	/**
	 * 计提总额
	 */
	private BigDecimal accrualAmount;
	/**
	 * 月度提成状态
	 */
	private String commissionStatusMonth;
	/**
	 * 月度计提金额
	 */
	private BigDecimal accrualAmountMonth;
	/**
	 * 年度提成状态
	 */
	private String commissionStatusYear;
	/**
	 * 年度计提金额
	 */
	private BigDecimal accrualAmountYear;
	/**
	 * 月度绩效发放日期
	 */
	private Date commissionDateMonth;
	/**
	 * 年度绩效发放日期
	 */
	private Date commissionDateYear;

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
	 * 设置：提成人
	 */
	public void setPerson(String person) {
		this.person = person;
	}
	/**
	 * 获取：提成人
	 */
	public String getPerson() {
		return person;
	}
	/**
	 * 设置：隶属公司
	 */
	public void setSubjection(String subjection) {
		this.subjection = subjection;
	}
	/**
	 * 获取：隶属公司
	 */
	public String getSubjection() {
		return subjection;
	}
	/**
	 * 设置：所属部门
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}
	/**
	 * 获取：所属部门
	 */
	public String getDept() {
		return dept;
	}
	/**
	 * 设置：提成类型
	 */
	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}
	/**
	 * 获取：提成类型
	 */
	public String getCommissionType() {
		return commissionType;
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
	 * 设置：计提日期
	 */
	public void setAccrualDate(Date accrualDate) {
		this.accrualDate = accrualDate;
	}
	/**
	 * 获取：计提日期
	 */
	public Date getAccrualDate() {
		return accrualDate;
	}
	/**
	 * 设置：计提总额
	 */
	public void setAccrualAmount(BigDecimal accrualAmount) {
		this.accrualAmount = accrualAmount;
	}
	/**
	 * 获取：计提总额
	 */
	public BigDecimal getAccrualAmount() {
		return accrualAmount;
	}
	/**
	 * 设置：月度提成状态
	 */
	public void setCommissionStatusMonth(String commissionStatusMonth) {
		this.commissionStatusMonth = commissionStatusMonth;
	}
	/**
	 * 获取：月度提成状态
	 */
	public String getCommissionStatusMonth() {
		return commissionStatusMonth;
	}
	/**
	 * 设置：月度计提金额
	 */
	public void setAccrualAmountMonth(BigDecimal accrualAmountMonth) {
		this.accrualAmountMonth = accrualAmountMonth;
	}
	/**
	 * 获取：月度计提金额
	 */
	public BigDecimal getAccrualAmountMonth() {
		return accrualAmountMonth;
	}
	/**
	 * 设置：年度提成状态
	 */
	public void setCommissionStatusYear(String commissionStatusYear) {
		this.commissionStatusYear = commissionStatusYear;
	}
	/**
	 * 获取：年度提成状态
	 */
	public String getCommissionStatusYear() {
		return commissionStatusYear;
	}
	/**
	 * 设置：年度计提金额
	 */
	public void setAccrualAmountYear(BigDecimal accrualAmountYear) {
		this.accrualAmountYear = accrualAmountYear;
	}
	/**
	 * 获取：年度计提金额
	 */
	public BigDecimal getAccrualAmountYear() {
		return accrualAmountYear;
	}
	/**
	 * 设置：月度绩效发放日期
	 */
	public void setCommissionDateMonth(Date commissionDateMonth) {
		this.commissionDateMonth = commissionDateMonth;
	}
	/**
	 * 获取：月度绩效发放日期
	 */
	public Date getCommissionDateMonth() {
		return commissionDateMonth;
	}
	/**
	 * 设置：年度绩效发放日期
	 */
	public void setCommissionDateYear(Date commissionDateYear) {
		this.commissionDateYear = commissionDateYear;
	}
	/**
	 * 获取：年度绩效发放日期
	 */
	public Date getCommissionDateYear() {
		return commissionDateYear;
	}
}
