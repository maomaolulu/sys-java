package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企业信息
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_company")
public class CompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 统一社会信用代码
	 */
	private String code;
	/**
	 * 企业名称
	 */
	private String company;
	/**
	 * 注册地址
	 */
	private String address;
	/**
	 * 法人代表
	 */
	private String legalname;
	/**
	 * 经营范围
	 */
	private String scope;
	/**
	 * 成立日期
	 */
	private Date registerDate;
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
	 * 办公地址
	 */
	private String officeAddress;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系电话(手机)
	 */
	private String mobile;
	/**
	 * 固定电话
	 */
	private String telephone;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 行业类别
	 */
	private String industryCategory;
	/**
	 * 职业病危害风险分类(0一般、1较重、2严重)
	 */
	private Integer riskLevel;
	/**
	 * 人员规模(人)
	 */
	private Integer population;
	/**
	 * 产品名称
	 */
	private String products;
	/**
	 * 产量信息
	 */
	private String yields;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 录入人ID
	 */
	private Integer userid;
	/**
	 * 录入人姓名
	 */
	private String username;
	/**
	 * 合作状态(0潜在、1意向、2已合作)
	 */
	private Integer contractStatus;
	/**
	 * 首次合作日期
	 */
	private Date contractFirst;
	/**
	 * 最近合作日期
	 */
	private Date contractLast;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
	/**
	 * 数据所属公司（杭州、宁波、嘉兴、上海量远等）
	 */
	private String dataBelong;
	/**
	 * 客户状态(0:正常、1:停用)
	 */
	private Integer status;
	/**
	 * 隶属人员
	 */
	private String personBelong;
	/**
	 * 是否属于公海
	 */
	private Integer ifHasFinished;
	/**
	 * 线索来源
	 */
	private String clueFrom;
	/**
	 *公司联系人列表
	 */
	@TableField(exist=false)
	private List<CompanyContactEntity> companyContactList;
	/**
	 * 业务员id
	 */
	@TableField(exist=false)
	private Long advanceId;
	/**
	 * 业务状态
	 */
	@TableField(exist=false)
	private Integer businessStatus;

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
	 * 设置：统一社会信用代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：统一社会信用代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：企业名称
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：企业名称
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置：注册地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：注册地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：法人代表
	 */
	public void setLegalname(String legalname) {
		this.legalname = legalname;
	}
	/**
	 * 获取：法人代表
	 */
	public String getLegalname() {
		return legalname;
	}
	/**
	 * 设置：经营范围
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	/**
	 * 获取：经营范围
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * 设置：成立日期
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	/**
	 * 获取：成立日期
	 */
	public Date getRegisterDate() {
		return registerDate;
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
	 * 设置：办公地址
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	/**
	 * 获取：办公地址
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
	 * 设置：联系电话(手机)
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：联系电话(手机)
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：固定电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：固定电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：传真
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * 获取：传真
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * 设置：行业类别
	 */
	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}
	/**
	 * 获取：行业类别
	 */
	public String getIndustryCategory() {
		return industryCategory;
	}
	/**
	 * 设置：职业病危害风险分类(0一般、1较重、2严重)
	 */
	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}
	/**
	 * 获取：职业病危害风险分类(0一般、1较重、2严重)
	 */
	public Integer getRiskLevel() {
		return riskLevel;
	}
	/**
	 * 设置：人员规模(人)
	 */
	public void setPopulation(Integer population) {
		this.population = population;
	}
	/**
	 * 获取：人员规模(人)
	 */
	public Integer getPopulation() {
		return population;
	}
	/**
	 * 设置：产品名称
	 */
	public void setProducts(String products) {
		this.products = products;
	}
	/**
	 * 获取：产品名称
	 */
	public String getProducts() {
		return products;
	}
	/**
	 * 设置：产量信息
	 */
	public void setYields(String yields) {
		this.yields = yields;
	}
	/**
	 * 获取：产量信息
	 */
	public String getYields() {
		return yields;
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
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	/**
	 * 获取：录入人ID
	 */
	public Integer getUserid() {
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
	 * 设置：合作状态(0潜在、1意向、2已合作)
	 */
	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}
	/**
	 * 获取：合作状态(0潜在、1意向、2已合作)
	 */
	public Integer getContractStatus() {
		return contractStatus;
	}
	/**
	 * 设置：首次合作日期
	 */
	public void setContractFirst(Date contractFirst) {
		this.contractFirst = contractFirst;
	}
	/**
	 * 获取：首次合作日期
	 */
	public Date getContractFirst() {
		return contractFirst;
	}
	/**
	 * 设置：最近合作日期
	 */
	public void setContractLast(Date contractLast) {
		this.contractLast = contractLast;
	}
	/**
	 * 获取：最近合作日期
	 */
	public Date getContractLast() {
		return contractLast;
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
	public List<CompanyContactEntity> getCompanyContactList() {
		return companyContactList;
	}
	public void setCompanyContactList(List<CompanyContactEntity> companyContactList) {
		this.companyContactList = companyContactList;
	}
}
