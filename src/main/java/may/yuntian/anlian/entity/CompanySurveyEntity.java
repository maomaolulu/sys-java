package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用人单位概况调查
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@TableName("t_company_survey")
public class CompanySurveyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 单位名称
	 */
	private String company;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 单位地址(搜集按地址)
	 */
	private String officeAddress;
	/**
	 * 委托单位名称
	 */
	private String entrustCompany;
	/**
	 * 委托单位地址
	 */
	private String entrustAddress;
	/**
	 * 注册单位地址
	 */
	private String registeredAddress;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系方式
	 */
	private String telephone;
	/**
	 * 所属行业
	 */
	private String industryCategory;
	/**
	 * 职业病危害风险分类(0一般、1较重、2严重)
	 */
	private Integer riskLevel;
	/**
	 * 检测类型(评价/定期/其它)
	 */
	private String detectionType;
	/**
	 * 劳动定员(人数)
	 */
	private Integer laborQuota;
	/**
	 * 有无卫生管理部门(0:无, 1:有)
	 */
	private Integer healthSector;
	/**
	 * 部门名称
	 */
	private String department;
	/**
	 * 人数
	 */
	private Integer population;
	/**
	 * 职业病危害人数
	 */
	private Integer hazardNum;
	/**
	 * 职业健康检查机构名称
	 */
	private String inspectionOrg;
	/**
	 * 最近一次职业健康检查时间
	 */
	private Date lastTestTime;
	/**
	 * 产品
	 */
	private String product;
	/**
	 * 产量
	 */
	private String yield;
    /**
     * 主要产品及年产量
     */
    private String productsYield;
	/**
	 * 调查陪同人
	 */
	private String accompany;
	/**
	 * 调查日期(年月日)
	 */
	private Date surveyDate;
	/**
	 *检测性质
	 */
	private String testNature;
	/**
	 *检测与评价场所
	 */
	private String testPlace;
	/**
	 * 警示标识标语告知卡是否完整（0不完整，1完整）
	 */
	private Integer warningSignCard;
	/**
	 *采样时间
	 */
	private String testDate;
	/**
	 *主要检测项目
	 */
	private String testItems;
	/**
	 * 采样时段
	 */
	private String timeFrame;
	/**
	 * 报告封面日期
	 */
	private Date reportCoverTime;
	/**
	 * 统一社会信用代码
	 */
	private String unifiedCode;
	/**
	 * 企业经济类型
	 */
	private String economy;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
	/**
	 * 0：未公司，1:已公示
	 */
	private Integer publicityStatus;
	/**
	 * 技术服务项目组人员
	 */
	private String technicalPersons;
	/**
	 * 项目采样陪同人
	 */
	private String samplingCompany;
	/**
	 * 项目采样时间
	 */
	private String samplingDate;
	/**
	 * 公示项目pdf路径
	 */
	private String publicityPath;
	/**
	 * 实验室人员（用于项目公示）
	 */
	private String laboratoryPerson;

	public String getPublicityPath() {
		return publicityPath;
	}

	public void setPublicityPath(String publicityPath) {
		this.publicityPath = publicityPath;
	}

	public String getLaboratoryPerson() {
		return laboratoryPerson;
	}

	public void setLaboratoryPerson(String laboratoryPerson) {
		this.laboratoryPerson = laboratoryPerson;
	}

	public Integer getHazardNum() {
		return hazardNum;
	}

	public void setHazardNum(Integer hazardNum) {
		this.hazardNum = hazardNum;
	}

	public Integer getPublicityStatus() {
		return publicityStatus;
	}

	public void setPublicityStatus(Integer publicityStatus) {
		this.publicityStatus = publicityStatus;
	}

	public String getTechnicalPersons() {
		return technicalPersons;
	}

	public void setTechnicalPersons(String technicalPersons) {
		this.technicalPersons = technicalPersons;
	}

	public String getSamplingCompany() {
		return samplingCompany;
	}

	public void setSamplingCompany(String samplingCompany) {
		this.samplingCompany = samplingCompany;
	}

	public String getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(String samplingDate) {
		this.samplingDate = samplingDate;
	}

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
	 * 设置：单位名称
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：单位名称
	 */
	public String getCompany() {
		return company;
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
	 * 设置：单位地址
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	/**
	 * 获取：单位地址
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
	 * 设置：联系方式
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：联系方式
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设置：所属行业类别
	 */
	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}
	/**
	 * 获取：所属行业类别
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
	 * 设置：检测类型(评价/定期/其它)
	 */
	public void setDetectionType(String detectionType) {
		this.detectionType = detectionType;
	}
	/**
	 * 获取：检测类型(评价/定期/其它)
	 */
	public String getDetectionType() {
		return detectionType;
	}
	/**
	 * 设置：劳动定员(人数)
	 */
	public void setLaborQuota(Integer laborQuota) {
		this.laborQuota = laborQuota;
	}
	/**
	 * 获取：劳动定员(人数)
	 */
	public Integer getLaborQuota() {
		return laborQuota;
	}
	/**
	 * 设置：有无卫生管理部门(0:无, 1:有)
	 */
	public void setHealthSector(Integer healthSector) {
		this.healthSector = healthSector;
	}
	/**
	 * 获取：有无卫生管理部门(0:无, 1:有)
	 */
	public Integer getHealthSector() {
		return healthSector;
	}
	/**
	 * 设置：部门名称
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：部门名称
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：人数
	 */
	public void setPopulation(Integer population) {
		this.population = population;
	}
	/**
	 * 获取：人数
	 */
	public Integer getPopulation() {
		return population;
	}

	/**
	 * 设置：职业健康检查机构名称
	 */
	public void setInspectionOrg(String inspectionOrg) {
		this.inspectionOrg = inspectionOrg;
	}
	/**
	 * 获取：职业健康检查机构名称
	 */
	public String getInspectionOrg() {
		return inspectionOrg;
	}
	/**
	 * 设置：最近一次职业健康检查时间
	 */
	public void setLastTestTime(Date lastTestTime) {
		this.lastTestTime = lastTestTime;
	}
	/**
	 * 获取：最近一次职业健康检查时间
	 */
	public Date getLastTestTime() {
		return lastTestTime;
	}
	/**
	 * 设置：产品
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * 获取：产品
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * 设置：产量
	 */
	public void setYield(String yield) {
		this.yield = yield;
	}
	/**
	 * 获取：产量
	 */
	public String getYield() {
		return yield;
	}
	/**
	 * 设置：陪同人
	 */
	public void setAccompany(String accompany) {
		this.accompany = accompany;
	}
	/**
	 * 获取：陪同人
	 */
	public String getAccompany() {
		return accompany;
	}
	/**
	 * 设置：调查日期(年月日)
	 */
	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	/**
	 * 获取：调查日期(年月日)
	 */
	public Date getSurveyDate() {
		return surveyDate;
	}
	public String getTestNature() {
		return testNature;
	}
	public void setTestNature(String testNature) {
		this.testNature = testNature;
	}
	public String getTestPlace() {
		return testPlace;
	}
	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getTestItems() {
		return testItems;
	}
	public void setTestItems(String testItems) {
		this.testItems = testItems;
	}
	/**
	 *获取 采样时段
	 */
	public String getTimeFrame() {
		return timeFrame;
	}
	/**
	 *设置 采样时段
	 */
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
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
	public Integer getWarningSignCard() {
		return warningSignCard;
	}
	public void setWarningSignCard(Integer warningSignCard) {
		this.warningSignCard = warningSignCard;
	}
	/**
	 * 委托单位地址
	 */
	public String getEntrustAddress() {
		return entrustAddress;
	}
	/**
	 * 委托单位地址
	 */
	public void setEntrustAddress(String entrustAddress) {
		this.entrustAddress = entrustAddress;
	}
	/**
	 * 委托单位名称
	 */
	public String getEntrustCompany() {
		return entrustCompany;
	}
	/**
	 * 委托单位名称
	 */
	public void setEntrustCompany(String entrustCompany) {
		this.entrustCompany = entrustCompany;
	}
	/**
	 * 注册单位地址
	 */
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	/**
	 * 注册单位地址
	 */
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public Date getReportCoverTime() {
		return reportCoverTime;
	}

	public void setReportCoverTime(Date reportCoverTime) {
		this.reportCoverTime = reportCoverTime;
	}

	public String getUnifiedCode() {
		return unifiedCode;
	}

	public void setUnifiedCode(String unifiedCode) {
		this.unifiedCode = unifiedCode;
	}

	public String getEconomy() {
		return economy;
	}

	public void setEconomy(String economy) {
		this.economy = economy;
	}

    public String getProductsYield() {
        return productsYield;
    }

    public void setProductsYield(String productsYield) {
        this.productsYield = productsYield;
    }
}