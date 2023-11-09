package may.yuntian.anlian.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import may.yuntian.common.validator.group.AddGroup;
import may.yuntian.common.validator.group.UpdateGroup;

/**
 *收付款记录实体类
 * 
 * @author LiXin
 * @data 2020-11-05
 */
@TableName("t_account")
public class AccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *项目ID
	 */
	@NotBlank(message="项目ID不能为空！",groups= {AddGroup.class,UpdateGroup.class})
	private Long projectId;
	/**
	 *款项类别
	 */
	@NotBlank(message="款项类别不能为空！")
	private Integer acType;
	/**
	 *收/付时间
	 */
	private Date happenTime;
	/**
	 *收/付金额(元)
	 */
	private Float amount;
	/**
	 *结算方式
	 */
	private Integer settleStyle;
	/**
	 *开票金额(元) 
	 */
	private Float invoiceAmount;
	/**
	 * 虚拟税费(元)
	 */
	private Float acVirtualTax;
	/**
	 *发票号码
	 */
	private String invoiceNumber;
	/**
	 *备注
	 */
	private String acRemarks;
	/**
	 *录入人ID
	 */
	private Long userid;
	/**
	 *录入人姓名
	 */
	private String username;
	/**
	 *修改人ID
	 */
	private Long editorId;
	/**
	 *修改人姓名
	 */
	private String editorName;
	/**
	 *数据入库时间
	 */
	private Date createTime;
	/**
	 *修改时间
	 */
	private Date updatetime;
	/**
	 * 关联的项目信息
	 */
	@TableField(exist=false)
	private ProjectEntity project;
	/**
	 * 项目编号
	 */
	@TableField(exist=false)
	private String identifier;
	/**
	 *收/付时间字符串
	 */
	@TableField(exist=false)
	private String happenTimeStr;
	
	
	/**
	 *获取自增主键ID
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
	 *获取合同ID(唯一索引)
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 *设置合同ID(唯一索引)
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 *获取款项类别
	 */
	public Integer getAcType() {
		return acType;
	}
	/**
	 *设置款项类别
	 */
	public void setAcType(Integer acType) {
		this.acType = acType;
	}
	/**
	 *获取收/付时间
	 */
	public Date getHappenTime() {
		return happenTime;
	}
	/**
	 *设置收/付时间
	 */
	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}
	/**
	 *获取收/付金额(元)
	 */
	public Float getAmount() {
		return amount;
	}
	/**
	 *设置收/付金额(元)
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	/**
	 *获取结算方式
	 */
	public Integer getSettleStyle() {
		return settleStyle;
	}
	/**
	 *设置结算方式
	 */
	public void setSettleStyle(Integer settleStyle) {
		this.settleStyle = settleStyle;
	}
	/**
	 *获取开票金额(元) 
	 */
	public Float getInvoiceAmount() {
		return invoiceAmount;
	}
	/**
	 *设置开票金额(元) 
	 */
	public void setInvoiceAmount(Float invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	/**
	 * 获取：虚拟税费(元)
	 */
	public Float getAcVirtualTax() {
		return acVirtualTax;
	}
	/**
	 * 设置：虚拟税费(元)
	 */
	public void setAcVirtualTax(Float acVirtualTax) {
		this.acVirtualTax = acVirtualTax;
	}
	/**
	 *获取发票号码
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	/**
	 *设置发票号码
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	/**
	 *获取备注
	 */
	public String getAcRemarks() {
		return acRemarks;
	}
	/**
	 *设置备注
	 */
	public void setAcRemarks(String acRemarks) {
		this.acRemarks = acRemarks;
	}
	/**
	 *获取录入人ID
	 */
	public Long getUserid() {
		return userid;
	}
	/**
	 *获取录入人ID
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 *获取录入人姓名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 *设置录入人姓名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 *获取修改人ID
	 */
	public Long getEditorId() {
		return editorId;
	}
	/**
	 *设置修改人ID
	 */
	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}
	/**
	 *获取修改人姓名
	 */
	public String getEditorName() {
		return editorName;
	}
	/**
	 *设置修改人姓名
	 */
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	/**
	 *获取数据入库时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 *设置数据入库时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 *获取修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	/**
	 *设置修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	@Override
	public String toString() {
		return "AccountEntity [id=" + id + ", projectId=" + projectId + ", type=" + acType + ", happenTime=" + happenTime
				+ ", amount=" + amount + ", settleStyle=" + settleStyle + ", invoiceAmount=" + invoiceAmount
				+ ", acVirtualTax=" + acVirtualTax + ", invoiceNumber=" + invoiceNumber + ", remarks=" + acRemarks
				+ ", userid=" + userid + ", username=" + username + ", editorId=" + editorId + ", editorName="
				+ editorName + ", createtime=" + createTime + ", updatetime=" + updatetime + "]";
	}
	
	public ProjectEntity getProject() {
		return project;
	}
	public void setProject(ProjectEntity project) {
		this.project = project;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getHappenTimeStr() {
		return happenTimeStr;
	}
	public void setHappenTimeStr(String happenTimeStr) {
		this.happenTimeStr = happenTimeStr;
	}

}
