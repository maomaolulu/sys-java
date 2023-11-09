package may.yuntian.anlian.vo;

import java.io.Serializable;

/**
 * 企业名称 显示层对象
 * Web向模板渲染引擎层传输的对象
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-28
 */
public class CompanyNameVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
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
	 * 省份
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 公司地址
	 */
	private String officeAddress;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系电话
	 */
	private String mobile;
	
	/**
	 * 设置：主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
