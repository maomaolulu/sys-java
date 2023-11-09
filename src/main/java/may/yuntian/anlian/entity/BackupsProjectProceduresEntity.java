package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目流程表
 * 
 * @author LiXin
 * @email ''
 * @date 2022-10-28 15:48:41
 */
@TableName("backups_project_procedures")
public class BackupsProjectProceduresEntity implements Serializable {
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
	 * 项目状态
	 */
	private Integer status;
	/**
	 * 项目状态名称
	 */
	private String statusname;
	/**
	 * 数据入库时间
	 */
	private Date createtime;

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
	 * 设置：项目状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：项目状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：项目状态名称
	 */
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	/**
	 * 获取：项目状态名称
	 */
	public String getStatusname() {
		return statusname;
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
}
