package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同模板自定义字段数据
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@TableName("t_contract_custom_data")
public class ContraccustomDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 合同ID
	 */
	private Long contractId;
	/**
	 * 合同模版自定义字段ID
	 */
	private Long templateCustomId;
	/**
	 * 字段名称(英文)
	 */
	private String nameEn;
	/**
	 * 字段描述(中文)
	 */
	private String nameZh;
	/**
	 * 字段的值
	 */
	private String value;

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
	 * 设置：合同ID
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	/**
	 * 获取：合同ID
	 */
	public Long getContractId() {
		return contractId;
	}
	/**
	 * 设置：合同模版自定义字段ID
	 */
	public void setTemplateCustomId(Long templateCustomId) {
		this.templateCustomId = templateCustomId;
	}
	/**
	 * 获取：合同模版自定义字段ID
	 */
	public Long getTemplateCustomId() {
		return templateCustomId;
	}
	/**
	 * 设置：字段名称(英文)
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	/**
	 * 获取：字段名称(英文)
	 */
	public String getNameEn() {
		return nameEn;
	}
	/**
	 * 设置：字段描述(中文)
	 */
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
	/**
	 * 获取：字段描述(中文)
	 */
	public String getNameZh() {
		return nameZh;
	}
	/**
	 * 设置：字段的值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：字段的值
	 */
	public String getValue() {
		return value;
	}
}
