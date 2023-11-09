package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价企业概况表
 */
@Data
@TableName("eval_company_survey")
public class AlCompanySurveyEntity implements Serializable {
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
     * 项目编号
     */
    private String identifier;
    /**
     * 项目类型
     */
    private String type;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 用人单位名称
     */
    private String company;
    /**
     * 单位地址
     */
    private String officeAddress;
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 委托单位名称
     */
    private String entrustCompany;
    /**
     * 委托单位详细地址
     */
    private String entrustOfficeAddress;
    /**
     * 委托单位内部职业卫生管理部门名称
     */
    private String entrustInsOrg;
    /**
     * 职业卫生管理人员人数
     */
    private Integer occHealthManagerNum;
    /**
     * 劳动定员/项目在册职工总数
     */
    private Integer laborQuota;
    /**
     * 职业健康检查机构名称
     */
    private String inspectionOrg;
    /**
     * 企业联系人/项目负责人/职业卫生管理人员
     */
    private String contact;
    /**
     * 联系方式
     */
    private String telephone;
    /**
     * 企业法人
     */
    private String legalPerson;
    /**
     * 法人联系方式
     */
    private String legalPersonPhone;
    /**
     * 项目性质
     */
    private String projectNature;
    /**
     * 经济类型
     */
    private String economicType;
    /**
     * 行业类别
     */
    private String industryCategory;
    /**
     * 企业规模  (大/中/小/微)
     */
    private String enterpriseScale;
    /**
     * 资质业务范围(多个值之间以@分割)
     可选项
     □采矿业，□化工、石化及医药，□冶金、建材，□机械制造、电力、纺织、建筑和交通运输等行业领域，□核设施，□核技术应用。
     */
    private String businessScope;
    /**
     * 项目总投资
     */
    private String totalInvestment;
    /**
     * 危害风险分类（0：一般、1：严重）
     */
    private Integer riskLevel;
    /**
     * 主要职业病危害因素
     */
    private String mainHazards;
    /**
     * 计划采样日期(多个日期合并的字符串)
     */
    private String planSampleDate;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 最后修改时间
     */
    private Date updatetime;

    /**
     * 评价公示PDF路径
     */
    private String publictyPath;

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
     * 设置：项目类型
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 获取：项目类型
     */
    public String getType() {
        return type;
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
     * 设置：用人单位名称
     */
    public void setCompany(String company) {
        this.company = company;
    }
    /**
     * 获取：用人单位名称
     */
    public String getCompany() {
        return company;
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
     * 设置：统一社会信用代码
     */
    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }
    /**
     * 获取：统一社会信用代码
     */
    public String getCreditCode() {
        return creditCode;
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
     * 设置：委托单位详细地址
     */
    public void setEntrustOfficeAddress(String entrustOfficeAddress) {
        this.entrustOfficeAddress = entrustOfficeAddress;
    }
    /**
     * 获取：委托单位详细地址
     */
    public String getEntrustOfficeAddress() {
        return entrustOfficeAddress;
    }
    /**
     * 设置：委托单位内部职业卫生管理部门名称
     */
    public void setEntrustInsOrg(String entrustInsOrg) {
        this.entrustInsOrg = entrustInsOrg;
    }
    /**
     * 获取：委托单位内部职业卫生管理部门名称
     */
    public String getEntrustInsOrg() {
        return entrustInsOrg;
    }
    /**
     * 设置：职业卫生管理人员人数
     */
    public void setOccHealthManagerNum(Integer occHealthManagerNum) {
        this.occHealthManagerNum = occHealthManagerNum;
    }
    /**
     * 获取：职业卫生管理人员人数
     */
    public Integer getOccHealthManagerNum() {
        return occHealthManagerNum;
    }
    /**
     * 设置：劳动定员/项目在册职工总数
     */
    public void setLaborQuota(Integer laborQuota) {
        this.laborQuota = laborQuota;
    }
    /**
     * 获取：劳动定员/项目在册职工总数
     */
    public Integer getLaborQuota() {
        return laborQuota;
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
     * 设置：企业联系人/项目负责人/职业卫生管理人员
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
    /**
     * 获取：企业联系人/项目负责人/职业卫生管理人员
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
     * 设置：企业法人
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
    /**
     * 获取：企业法人
     */
    public String getLegalPerson() {
        return legalPerson;
    }
    /**
     * 设置：法人联系方式
     */
    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }
    /**
     * 获取：法人联系方式
     */
    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }
    /**
     * 设置：项目性质
     */
    public void setProjectNature(String projectNature) {
        this.projectNature = projectNature;
    }
    /**
     * 获取：项目性质
     */
    public String getProjectNature() {
        return projectNature;
    }
    /**
     * 设置：经济类型
     */
    public void setEconomicType(String economicType) {
        this.economicType = economicType;
    }
    /**
     * 获取：经济类型
     */
    public String getEconomicType() {
        return economicType;
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
     * 设置：企业规模  (大/中/小/微)
     */
    public void setEnterpriseScale(String enterpriseScale) {
        this.enterpriseScale = enterpriseScale;
    }
    /**
     * 获取：企业规模  (大/中/小/微)
     */
    public String getEnterpriseScale() {
        return enterpriseScale;
    }
    /**
     * 设置：资质业务范围(多个值之间以@分割)
     可选项
     □采矿业，□化工、石化及医药，□冶金、建材，□机械制造、电力、纺织、建筑和交通运输等行业领域，□核设施，□核技术应用。
     */
    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }
    /**
     * 获取：资质业务范围(多个值之间以@分割)
     可选项
     □采矿业，□化工、石化及医药，□冶金、建材，□机械制造、电力、纺织、建筑和交通运输等行业领域，□核设施，□核技术应用。
     */
    public String getBusinessScope() {
        return businessScope;
    }
    /**
     * 设置：项目总投资
     */
    public void setTotalInvestment(String totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
    /**
     * 获取：项目总投资
     */
    public String getTotalInvestment() {
        return totalInvestment;
    }
    /**
     * 设置：危害风险分类（0：一般、1：严重）
     */
    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }
    /**
     * 获取：危害风险分类（0：一般、1：严重）
     */
    public Integer getRiskLevel() {
        return riskLevel;
    }
    /**
     * 设置：主要职业病危害因素
     */
    public void setMainHazards(String mainHazards) {
        this.mainHazards = mainHazards;
    }
    /**
     * 获取：主要职业病危害因素
     */
    public String getMainHazards() {
        return mainHazards;
    }
    /**
     * 设置：计划采样日期(多个日期合并的字符串)
     */
    public void setPlanSampleDate(String planSampleDate) {
        this.planSampleDate = planSampleDate;
    }
    /**
     * 获取：计划采样日期(多个日期合并的字符串)
     */
    public String getPlanSampleDate() {
        return planSampleDate;
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
     * 设置：最后修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    /**
     * 获取：最后修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }
}

