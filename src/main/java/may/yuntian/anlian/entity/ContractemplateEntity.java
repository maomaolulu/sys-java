package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同模板共同信息
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@TableName("t_contract_template")
public class ContractemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 合同类型
	 */
	private String type;
	/**
	 * 合同模板路径
	 */
	private String file;
	/**
	 * 状态(0草稿，1启用、2停用)
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remarks;
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
	 * 设置：模板名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：模板名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：合同类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：合同类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：合同模板路径
	 */
	public void setFile(String file) {
		this.file = file;
	}
	/**
	 * 获取：合同模板路径
	 */
	public String getFile() {
		return file;
	}
	/**
	 * 设置：状态(0草稿，1启用、2停用)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0草稿，1启用、2停用)
	 */
	public Integer getStatus() {
		return status;
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
