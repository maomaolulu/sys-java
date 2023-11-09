package may.yuntian.wordgenerate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@Data
@TableName("word_generate_path")
public class WordGeneratePathEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 生成word存储地址
	 */
	private String path;
	/**
	 * 完成期限
	 */
	private String timeLimit;
	/**
	 * 其他要求
	 */
	private String other;
	/**
	 * word类型（1.任务单 2.评审单 3合同协议）
	 */
	private Integer type;
	/**
	 * word最近一次生成日期
	 */
	private Date generateTime;
	/**
	 * 数据创建日期
	 */
	private Date createTime;
	/**
	 * 数据更新日期
	 */
	private Date updateTime;

	/**
	 * 设置：自增ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增ID
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
	 * 设置：生成word存储地址
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：生成word存储地址
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：完成期限
	 */
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * 获取：完成期限
	 */
	public String getTimeLimit() {
		return timeLimit;
	}
	/**
	 * 设置：其他要求
	 */
	public void setOther(String other) {
		this.other = other;
	}
	/**
	 * 获取：其他要求
	 */
	public String getOther() {
		return other;
	}
	/**
	 * 设置：word类型（1.任务单 2.评审单 3合同协议）
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：word类型（1.任务单 2.评审单 3合同协议）
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：word最近一次生成日期
	 */
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}
	/**
	 * 获取：word最近一次生成日期
	 */
	public Date getGenerateTime() {
		return generateTime;
	}
	/**
	 * 设置：数据创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：数据创建日期
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：数据更新日期
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：数据更新日期
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
