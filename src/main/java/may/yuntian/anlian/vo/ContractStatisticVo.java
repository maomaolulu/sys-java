package may.yuntian.anlian.vo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;

/**
 * 合同信息统计报表
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-11-28
 */
public class ContractStatisticVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 统计日期类型(年Y,月M,周W,日D)
	 */
	@ApiModelProperty(value="统计日期类型(年Y,月M,周W,日D)",example = "D")
	private String dateType;
	/**
	 * 委托类型
	 */
	@ApiModelProperty(value="委托类型")
	private String entrustType;
	/**
	 * 合同类型
	 */
	@ApiModelProperty(value="合同类型")
	private String type;
	
	/**
	 * 合同状态
	 */
	@ApiModelProperty(value="合同状态")
	private Integer status;
	
	/**
	 * 业务员ID
	 */
	@ApiModelProperty(value="业务员ID")
	private Integer salesmenid;
	/**
	 * 业务员
	 */
	@ApiModelProperty(value="业务员")
	private String salesmen;
	
	/**
	 * 项目负责人ID
	 */
	@ApiModelProperty(value="项目负责人ID")
	private Long chargeId;
	/**
	 * 项目负责人
	 */
	@ApiModelProperty(value="项目负责人")
	private String charge;
	
	/**
	 * 部门ID
	 */
	@ApiModelProperty(value="部门ID")
	private Long deptId;
	
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value="部门名称")
	private String deptname;
	
	/**
	 * 根据某列进行统计分组(列的参数前端传值，用于灵活统计)
	 */
	@ApiModelProperty(value="根据某列进行统计分组(列的参数前端传值，用于灵活统计)",example = "salesmen,p.type,status,charge,dept_id")
	private String groupByColumns;
	
	
	/**
	 * 合同金额(元)
	 */
//	@ApiModelProperty(value="合同金额(元)")
//	private BigDecimal totalMoney;

	/**
	 * 合同签订日期
	 */
//	@ApiModelProperty(value="合同签订日期")
//	private Date signDate;
	
	/**
	 * 数量
	 */
//	@ApiModelProperty(value="数量")
//	private Integer amount;
	
	/**
	 * 查询条件开始日期
	 */
	@ApiModelProperty(value="查询条件开始日期",example = "2020-10-16 00:00:00")
	private	String startDate;
	/**
	 * 查询条件结束日期
	 */
	@ApiModelProperty(value="查询条件结束日期",example = "2020-12-31 23:59:59")
	private	String endDate;
	
	/**
	 * 获取：统计日期类型(年Y,月M,周W,日D)
	 */
	public String getDateType() {
		return dateType;
	}
	/**
	 * 设置：统计日期类型(年Y,月M,周W,日D)
	 */
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	/**
	 * 设置：委托类型
	 */
	public void setEntrustType(String entrustType) {
		this.entrustType = entrustType;
	}
	/**
	 * 获取：委托类型
	 */
	public String getEntrustType() {
		return entrustType;
	}
	/**
	 * 设置：合同类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：合同类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：合同状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：合同状态
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 设置：业务员ID
	 */
	public void setSalesmenid(Integer salesmenid) {
		this.salesmenid = salesmenid;
	}
	/**
	 * 获取：业务员ID
	 */
	public Integer getSalesmenid() {
		return salesmenid;
	}
	/**
	 * 设置：业务员
	 */
	public void setSalesmen(String salesmen) {
		this.salesmen = salesmen;
	}
	/**
	 * 获取：业务员
	 */
	public String getSalesmen() {
		return salesmen;
	}
	/**
	 * 设置：合同金额(元)
	 */
//	public void setTotalMoney(BigDecimal totalMoney) {
//		this.totalMoney = totalMoney;
//	}
	/**
	 * 获取：合同金额(元)
	 */
//	public BigDecimal getTotalMoney() {
//		return totalMoney;
//	}

	public Long getChargeId() {
		return chargeId;
	}
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	/**
	 * 设置：合同签订日期
	 */
//	public void setSignDate(Date signDate) {
//		this.signDate = signDate;
//	}
	/**
	 * 获取：合同签订日期
	 */
//	public Date getSignDate() {
//		return signDate;
//	}
	/**
	 * 获取：合同数量
	 */
//	public Integer getAmount() {
//		return amount;
//	}
	/**
	 * 设置：合同数量
	 */
//	public void setAmount(Integer amount) {
//		this.amount = amount;
//	}
	/**
	 * 获取：查询条件开始日期
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * 设置：查询条件开始日期
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * 获取：查询条件结束日期
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * 设置：查询条件结束日期
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	
	public String getGroupByColumns() {
		return groupByColumns;
	}
	public void setGroupByColumns(String groupByColumns) {
		this.groupByColumns = groupByColumns;
	}
	@Override
	public String toString() {
		return "ContractStatisticVo [startDate=" + startDate + ", endDate=" + endDate 
				+ ", entrustType=" + entrustType + ", type=" + type + ", status=" + status
				+ ", salesmenid=" + salesmenid + ", salesmen=" + salesmen 
				+"]";
	}
	
	
}
