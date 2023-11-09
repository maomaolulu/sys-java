package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


/**
 * 项目归档文件目录
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@TableName("t_project_archive")
public class ProjecarchiveEntity implements Serializable {
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
	 * 案卷名称
	 */
	private String name;
	/**
	 * 档案编号
	 */
	private String archiveNum;
	/**
	 * 序号
	 */
	private Integer orderNum;
	/**
	 * 责任者
	 */
	private String charge;
	/**
	 * 文号
	 */
	private String code;
	/**
	 * 标题名称
	 */
	private String title;
	/**
	 * 日期
	 */
	private String reportDate;
	/**
	 * 页码
	 */
	private String page;
	/**
	 * 备注
	 */
	private String note;
	
	/**
	 * 项目归档日期
	 */
	private Date projectArchiveDate;
	/**
	 * 项目档案柜编号
	 */
	private String projectArchiveNum;
	/**
	 * 项目档案盒编号
	 */
	private String projectOrderNum;
	/**
	 * 检测报告归档日期
	 */
	private Date detectionArchiveDate;
	/**
	 * 检测报告档案柜编号
	 */
	private String detectionArchiveNum;
	/**
	 * 检测报告档案盒编号
	 */
	private String detectionOrderNum;
	/**
	 * 检测报告编号
	 */
	private String detectionNum;
	/**
	 * 报告归档日期
	 */
	private Date inspectionArchiveDate;
	/**
	 * 报告档案柜编号
	 */
	private String inspectionArchiveNum;
	/**
	 * 报告档案盒编号
	 */
	private String inspectionOrderNum;
	/**
	 * 报告编号
	 */
	private String inspectionNum;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
	
	/**
	 * 报告签发日期
	 */
	@TableField(exist=false)
	private Date reportIssue;
	/**
	 * 报告装订日期
	 */
	@TableField(exist=false)
	private Date reportBinding;
	
	/**
	 * 行业类别 
	 */
	@TableField(exist=false)
	private String industryCategory;
	/**
	 * 职业病危害风险分类（0一般 1较重 2严重）
	 */
	@TableField(exist=false)
	private Integer riskLevel;
	/**
	 * 6.检测项目（识别危害因素）
	 */
	@TableField(exist=false)
	private String testItems;
	/**
	 * 报告接收日期
	 */
	@TableField(exist=false)
	private Date reportAccept;
	

	/**
	 *获取 自增主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 *设置自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 *获取 项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 *设置 项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 *获取 项目归档日期
	 */
	public Date getProjectArchiveDate() {
		return projectArchiveDate;
	}
	/**
	 *设置 项目归档日期
	 */
	public void setProjectArchiveDate(Date projectArchiveDate) {
		this.projectArchiveDate = projectArchiveDate;
	}
	/**
	 *获取 项目档案柜编号
	 */
	public String getProjectArchiveNum() {
		return projectArchiveNum;
	}
	/**
	 *设置 项目档案柜编号
	 */
	public void setProjectArchiveNum(String projectArchiveNum) {
		this.projectArchiveNum = projectArchiveNum;
	}
	/**
	 *获取 项目档案盒编号
	 */
	public String getProjectOrderNum() {
		return projectOrderNum;
	}
	/**
	 *设置 项目档案盒编号
	 */
	public void setProjectOrderNum(String projectOrderNum) {
		this.projectOrderNum = projectOrderNum;
	}
	/**
	 *获取 检测报告归档日期
	 */
	public Date getDetectionArchiveDate() {
		return detectionArchiveDate;
	}
	/**
	 *设置 检测报告归档日期
	 */
	public void setDetectionArchiveDate(Date detectionArchiveDate) {
		this.detectionArchiveDate = detectionArchiveDate;
	}
	/**
	 *获取 检测报告档案柜编号
	 */
	public String getDetectionArchiveNum() {
		return detectionArchiveNum;
	}
	/**
	 *设置 检测报告档案柜编号
	 */
	public void setDetectionArchiveNum(String detectionArchiveNum) {
		this.detectionArchiveNum = detectionArchiveNum;
	}
	/**
	 *获取 检测报告档案盒编号
	 */
	public String getDetectionOrderNum() {
		return detectionOrderNum;
	}
	/**
	 *设置 检测报告档案盒编号
	 */
	public void setDetectionOrderNum(String detectionOrderNum) {
		this.detectionOrderNum = detectionOrderNum;
	}
	/**
	 *获取 检测报告编号
	 */
	public String getDetectionNum() {
		return detectionNum;
	}
	/**
	 *设置 检测报告编号
	 */
	public void setDetectionNum(String detectionNum) {
		this.detectionNum = detectionNum;
	}
	/**
	 *获取 检评报告归档日期
	 */
	public Date getInspectionArchiveDate() {
		return inspectionArchiveDate;
	}
	/**
	 *设置 检评报告归档日期
	 */
	public void setInspectionArchiveDate(Date inspectionArchiveDate) {
		this.inspectionArchiveDate = inspectionArchiveDate;
	}
	/**
	 *获取 检评报告档案柜编号
	 */
	public String getInspectionArchiveNum() {
		return inspectionArchiveNum;
	}
	/**
	 *设置 检评报告档案柜编号
	 */
	public void setInspectionArchiveNum(String inspectionArchiveNum) {
		this.inspectionArchiveNum = inspectionArchiveNum;
	}
	/**
	 *获取 检评报告档案盒编号
	 */
	public String getInspectionOrderNum() {
		return inspectionOrderNum;
	}
	/**
	 *设置 检评报告档案盒编号
	 */
	public void setInspectionOrderNum(String inspectionOrderNum) {
		this.inspectionOrderNum = inspectionOrderNum;
	}
	/**
	 *获取 检评报告编号
	 */
	public String getInspectionNum() {
		return inspectionNum;
	}
	/**
	 *设置 检评报告编号
	 */
	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}
	/**
	 *获取 数据入库时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 *设置 数据入库时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 *获取 修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	/**
	 *设置 修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArchiveNum() {
		return archiveNum;
	}
	public void setArchiveNum(String archiveNum) {
		this.archiveNum = archiveNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIndustryCategory() {
		return industryCategory;
	}
	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}
	public Integer getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getTestItems() {
		return testItems;
	}
	public void setTestItems(String testItems) {
		this.testItems = testItems;
	}

    public Date getReportIssue() {
        return reportIssue;
    }

    public void setReportIssue(Date reportIssue) {
        this.reportIssue = reportIssue;
    }

    public Date getReportBinding() {
        return reportBinding;
    }

    public void setReportBinding(Date reportBinding) {
        this.reportBinding = reportBinding;
    }

    public Date getReportAccept() {
        return reportAccept;
    }

    public void setReportAccept(Date reportAccept) {
        this.reportAccept = reportAccept;
    }
}
