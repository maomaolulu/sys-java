package may.yuntian.anliantest.vo;

import may.yuntian.anlian.entity.SampleImgEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评价（控评、现状）公示信息
 * @author zhanghao
 * @date 2022-04-18
 */
public class EvaluateVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 自增主键ID
	 */
	private Long id;
	/**
	 * projectId
	 */
	private Long projectId;
	/**
	 * 项目类型
	 */
	private String type;
	/**
	 * 单位名称
	 */
	private String company;
	/**
	 * 单位地址
	 */
	private String officeAddress;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 技术负责人
	 */
	private String techCharge;
	/**
	 * 质控负责人
	 */
	private String qcCharge;
	/**
	 * 项目负责人
	 */
	private String pCharge;
	/**
	 * 评价报告编制人
	 */
	private String pPreparePerson;
	/**
	 * 现场调查人员
	 */
	private String fieldInvestigators;
	/**
	 * 现场调查陪同人
	 */
	private String pSurveyCompanion;
	
	/**
	 * 现场调查时间
	 */
	private Date pSurveyDate;
	/**
	 * 现场采样。现场检测人员
	 */
	private String fieldSamplings;
	/**
	 * 现场采样陪同人
	 */
	private String pSampleCompanion;
	/**
	 * 现场采样时间
	 */
	private String pSampleDate;
	/**
	 * 评价报告提交时间
	 */
	private Date pIssuedDate;



	/**
	 * 现场检测图
	 */
	private List<SampleImgEntity> imgs;
	/**
	 * path
	 */
	private String path;
	/**
	 * pdf 参数
	 * @return
	 */
	private Map<String,Object> map;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTechCharge() {
		return techCharge;
	}

	public void setTechCharge(String techCharge) {
		this.techCharge = techCharge;
	}

	public String getQcCharge() {
		return qcCharge;
	}

	public void setQcCharge(String qcCharge) {
		this.qcCharge = qcCharge;
	}

	public String getpCharge() {
		return pCharge;
	}

	public void setpCharge(String pCharge) {
		this.pCharge = pCharge;
	}

	public String getpPreparePerson() {
		return pPreparePerson;
	}

	public void setpPreparePerson(String pPreparePerson) {
		this.pPreparePerson = pPreparePerson;
	}

	public String getFieldInvestigators() {
		return fieldInvestigators;
	}

	public void setFieldInvestigators(String fieldInvestigators) {
		this.fieldInvestigators = fieldInvestigators;
	}

	public String getpSurveyCompanion() {
		return pSurveyCompanion;
	}

	public void setpSurveyCompanion(String pSurveyCompanion) {
		this.pSurveyCompanion = pSurveyCompanion;
	}

	public Date getpSurveyDate() {
		return pSurveyDate;
	}

	public void setpSurveyDate(Date pSurveyDate) {
		this.pSurveyDate = pSurveyDate;
	}

	public String getFieldSamplings() {
		return fieldSamplings;
	}

	public void setFieldSamplings(String fieldSamplings) {
		this.fieldSamplings = fieldSamplings;
	}

	public String getpSampleCompanion() {
		return pSampleCompanion;
	}

	public void setpSampleCompanion(String pSampleCompanion) {
		this.pSampleCompanion = pSampleCompanion;
	}

	public String getpSampleDate() {
		return pSampleDate;
	}

	public void setpSampleDate(String pSampleDate) {
		this.pSampleDate = pSampleDate;
	}

	public Date getpIssuedDate() {
		return pIssuedDate;
	}

	public void setpIssuedDate(Date pIssuedDate) {
		this.pIssuedDate = pIssuedDate;
	}

	public List<SampleImgEntity> getImgs() {
		return imgs;
	}

	public void setImgs(List<SampleImgEntity> imgs) {
		this.imgs = imgs;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "EvaluateVo{" +
				"id=" + id +
				", projectId=" + projectId +
				", type='" + type + '\'' +
				", company='" + company + '\'' +
				", officeAddress='" + officeAddress + '\'' +
				", contact='" + contact + '\'' +
				", techCharge='" + techCharge + '\'' +
				", qcCharge='" + qcCharge + '\'' +
				", pCharge='" + pCharge + '\'' +
				", pPreparePerson='" + pPreparePerson + '\'' +
				", fieldInvestigators='" + fieldInvestigators + '\'' +
				", pSurveyCompanion='" + pSurveyCompanion + '\'' +
				", pSurveyDate=" + pSurveyDate +
				", fieldSamplings='" + fieldSamplings + '\'' +
				", pSampleCompanion='" + pSampleCompanion + '\'' +
				", pSampleDate='" + pSampleDate + '\'' +
				", pIssuedDate=" + pIssuedDate +
				", imgs=" + imgs +
				", map=" + map +
				'}';
	}
}
