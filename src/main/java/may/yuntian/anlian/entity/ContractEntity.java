package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 合同信息
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@Data
@TableName("t_contract")
public class ContractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
//	/**
//	 * 父级ID,一级指令为0，合同隶属：主合同、子合同
//	 */
//	private Long parentid;
	/**
	 * 合同编号
	 */
	private String identifier;
	
	
	/**
	 * 受检企业信息表ID
	 */
	private Long companyId;
	/**
	 * 受检企业名称
	 */
	private String company;
	/**
	 * 委托单位，企业信息表ID
	 */
	private Long entrustCompanyId;
	/**
	 * 委托单位名称
	 */
	private String entrustCompany;
	/**
	 * 委托单位详细地址
	 */
	private String entrustOfficeAddress;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 区县
	 */
	private String area;
	/**
	 * 受检详细地址
	 */
	private String officeAddress;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 委托类型
	 */
	private String entrustType;
	/**
	 * 合同类型
	 */
	private String type;
	/**
	 * 合同模版ID
	 */
	private Long contractTemplateId;
	/**
	 * 合同状态
	 */
	private Integer status;
	/**
	 * 合同签订状态(0未回，1已回)
	 */
	private Integer contractStatus;

    /**
     * 合同签订状态1已回 时间
     */
    private Date contractStatusTime;
	/**
	 * 协议签订状态(0未回，1已回)
	 */
	private Integer dealStatus;
    /**
     * 协议签订状态1已回 时间
     */
    private Date dealStatusTime;
	/**
	 * 项目隶属公司
	 */
	private String companyOrder;
	/**
	 * 杭州隶属(业务来源)
	 */
	private String businessSource;
	/**
	 * 业务员ID
	 */
	private Integer salesmenid;
	/**
	 * 业务员
	 */
	private String salesmen;
	/**
	 * 合同金额(元)
	 */
	private BigDecimal totalMoney;
	/**
	 * 佣金(元)
	 */
	private BigDecimal commission;
	/**
	 * 评审费(元)
	 */
	private BigDecimal evaluationFee;
	/**
	 * 分包费(元)
	 */
	private BigDecimal subcontractFee;
	/**
	 * 服务费用(元)
	 */
	private BigDecimal serviceCharge;
	/**
	 * 其他支出(元)
	 */
	private BigDecimal otherExpenses;
	/**
	 * 合同净值(元)
	 */
	private BigDecimal netvalue;

	/**
	 * 委托日期
	 */
	private Date commissionDate;
	/**
	 * 合同签订日期
	 */
	private Date signDate;

	/**
	 * 新老业务(0新业务，1续签业务)
	 */
	private Integer old;

	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 录入人ID
	 */
	private Long userid;
	/**
	 * 录入人姓名
	 */
	private String username;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

    /**
     * 合同回款方式
     */
    private String paymentMethod;
    /**
     * 预付款比例
     */
    private Double prepaymentRatio;

    /**
	 * 项目编号
	 */
	@TableField(exist=false)
	private String projectIdentifier;
	/**
	 * 项目状态
	 */
	@TableField(exist=false)
	private Integer projectStatus;
	/**
	 * 所属部门ID
	 */
	@TableField(exist=false)
	private Long projectDeptId;
	/**
	 * 项目类型
	 */
	@TableField(exist=false)
	private String projectType;


	/**
	 * 设置：自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：合同编号
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * 获取：合同编号
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * 设置：受检企业信息表ID
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	/**
	 * 获取：受检企业信息表ID
	 */
	public Long getCompanyId() {
		return companyId;
	}
	/**
	 * 设置：受检企业名称
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：受检企业名称
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置：委托单位，企业信息表ID
	 */
	public void setEntrustCompanyId(Long entrustCompanyId) {
		this.entrustCompanyId = entrustCompanyId;
	}
	/**
	 * 获取：委托单位，企业信息表ID
	 */
	public Long getEntrustCompanyId() {
		return entrustCompanyId;
	}
	/**
	 * 设置：委托单位名称
	 */
	public void setEntrustCompany(String entrustCompany) {
		this.entrustCompany = entrustCompany;
	}
	/**
	 * 获取：委托单位名称
	 */
	public String getEntrustCompany() {
		return entrustCompany;
	}
	/**
	 * 设置：省份
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：省份
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：城市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：城市
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：区县
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 获取：区县
	 */
	public String getArea() {
		return area;
	}
	/**
	 * 设置：受检详细地址
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	/**
	 * 获取：受检详细地址
	 */
	public String getOfficeAddress() {
		return officeAddress;
	}
	/**
	 * 设置：联系人
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * 获取：联系人
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * 设置：联系电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：项目名称
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 获取：项目名称
	 */
	public String getProjectName() {
		return projectName;
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
	 * 设置：合同模版ID
	 */
	public void setContractTemplateId(Long contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}
	/**
	 * 获取：合同模版ID
	 */
	public Long getContractTemplateId() {
		return contractTemplateId;
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
	public Integer getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}
	public Integer getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}
	/**
	 * 设置：项目隶属公司
	 */
	public void setCompanyOrder(String companyOrder) {
		this.companyOrder = companyOrder;
	}
	/**
	 * 获取：项目隶属公司
	 */
	public String getCompanyOrder() {
		return companyOrder;
	}
	/**
	 * 设置：杭州隶属(业务来源)
	 */
	public void setBusinessSource(String businessSource) {
		this.businessSource = businessSource;
	}
	/**
	 * 获取：杭州隶属(业务来源)
	 */
	public String getBusinessSource() {
		return businessSource;
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
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	/**
	 * 获取：合同金额(元)
	 */
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	/**
	 * 设置：佣金(元)
	 */
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	/**
	 * 获取：佣金(元)
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
	public void setSubcontractFee(BigDecimal subcontractFee) {
		this.subcontractFee = subcontractFee;
	}
	/**
	 * 获取：分包费(元)
	 */
	public BigDecimal getSubcontractFee() {
		return subcontractFee;
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
	/**
	 * 设置：合同净值(元)
	 */
	public void setNetvalue(BigDecimal netvalue) {
		this.netvalue = netvalue;
	}
	/**
	 * 获取：合同净值(元)
	 */
	public BigDecimal getNetvalue() {
		return netvalue;
	}
	/**
	 * 设置：委托日期
	 */
	public void setCommissionDate(Date commissionDate) {
		this.commissionDate = commissionDate;
	}
	/**
	 * 获取：委托日期
	 */
	public Date getCommissionDate() {
		return commissionDate;
	}
	/**
	 * 设置：合同签订日期
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * 获取：合同签订日期
	 */
	public Date getSignDate() {
		return signDate;
	}

	/**
	 * 设置：新老业务(0新业务，1续签业务)
	 */
	public void setOld(Integer old) {
		this.old = old;
	}
	/**
	 * 获取：新老业务(0新业务，1续签业务)
	 */
	public Integer getOld() {
		return old;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置：录入人ID
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 * 获取：录入人ID
	 */
	public Long getUserid() {
		return userid;
	}
	/**
	 * 设置：录入人姓名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：录入人姓名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：数据入库时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：数据入库时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}

	
	public String getProjectIdentifier() {
		return projectIdentifier;
	}
	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}
	
	public Integer getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}
	public Long getProjectDeptId() {
		return projectDeptId;
	}
	public void setProjectDeptId(Long projectDeptId) {
		this.projectDeptId = projectDeptId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	/**
	 * 委托单位详细地址
	 */
	public String getEntrustOfficeAddress() {
		return entrustOfficeAddress;
	}
	/**
	 * 委托单位详细地址
	 */
	public void setEntrustOfficeAddress(String entrustOfficeAddress) {
		this.entrustOfficeAddress = entrustOfficeAddress;
	}
	@Override
	public String toString() {
		return "ContractEntity [id=" + id + ", parentid=" + ", identifier=" + identifier + ", companyId="
				+ companyId + ", company=" + company + ", contact=" + contact + ", projectName=" + projectName
				+ ", entrustType=" + entrustType + ", status=" + status + ", username=" + username + "]";
	}
	
	
}
