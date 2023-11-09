package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同模板自定义字段
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@TableName("t_contract_custom_template")
public class ContraccustomTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 合同模版ID
	 */
	private Long contractTemplateId;
	/**
	 * 字段名称(英文)
	 */
	private String nameEn;
	/**
	 * 字段描述(中文)
	 */
	private String nameZh;
	/**
	 * 状态(0字符，1数值、2单选、2复选)
	 */
	private Integer type;
	/**
	 * 字段内容
	 */
	private String note;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

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
	 * 设置：状态(0字符，1数值、2单选、2复选)
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：状态(0字符，1数值、2单选、2复选)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：字段内容
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：字段内容
	 */
	public String getNote() {
		return note;
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
}
