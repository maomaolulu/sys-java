package may.yuntian.anlian.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import may.yuntian.common.validator.group.AddGroup;
import may.yuntian.common.validator.group.UpdateGroup;

/**
 *公司联系人实体类
 * 
 * @author LiXin
 * @data 2020-11-28
 */
@TableName("t_company_contact")
public class CompanyContactEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *企业ID
	 */
	private Long companyId;
	/**
	 *联系人
	 */
	private String contact;
	/**
	 *联系方式
	 */
	private String mobile;

	/**
	 *固定电话
	 */
	private String telephone;
	/**
	 *邮箱
	 */
	private String email;
	/**
	 *是否默认（0否，1是）
	 */
	private Integer isDefault;
	/**
	 *联系人类型
	 */
	private String type;
	/**
	 *备注
	 */
	private String remark;

	
	
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
	 *获取企业ID
	 */
	public Long getCompanyId() {
		return companyId;
	}
	/**
	 *设置企业ID
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	/**
	 *设置联系人
	 */
	public String getContact() {
		return contact;
	}
	/**
	 *获取联系人
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 *设置联系方式
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 *获取联系方式
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 *设置固定电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 *获取固定电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 *设置邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 *获取邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 *设置是否默认（0否，1是）
	 */
	public Integer getIsDefault() {
		return isDefault;
	}
	/**
	 *获取是否默认（0否，1是）
	 */
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 *设置联系人类型
	 */
	public String getType() {
		return type;
	}
	/**
	 *获取联系人类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 *获取备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 *设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
