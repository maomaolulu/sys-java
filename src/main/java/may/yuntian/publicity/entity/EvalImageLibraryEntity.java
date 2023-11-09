package may.yuntian.publicity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 影像库（评价）
 * 
 * @author LiXin
 * @email ''
 * @date 2023-05-28 18:32:17
 */
@TableName("eval_image_library")
public class EvalImageLibraryEntity implements Serializable {
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
	 * 影像类别（1：调查、2：采样）
	 */
	private Integer category;
	/**
	 * 影像路径
	 */
	private String url;
	/**
	 * 类型(0车间/岗位, 1厂区大门)
	 */
	private Integer type;
	/**
	 * 车间/岗位/厂区大门
	 */
	private String point;
	/**
	 * 编制人
	 */
	private String editor;
	/**
	 * 数据入库时间(日期)
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
	 * 设置：影像类别（1：调查、2：采样）
	 */
	public void setCategory(Integer category) {
		this.category = category;
	}
	/**
	 * 获取：影像类别（1：调查、2：采样）
	 */
	public Integer getCategory() {
		return category;
	}
	/**
	 * 设置：影像路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：影像路径
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：类型(0车间/岗位, 1厂区大门)
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型(0车间/岗位, 1厂区大门)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：车间/岗位/厂区大门
	 */
	public void setPoint(String point) {
		this.point = point;
	}
	/**
	 * 获取：车间/岗位/厂区大门
	 */
	public String getPoint() {
		return point;
	}
	/**
	 * 设置：编制人
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/**
	 * 获取：编制人
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * 设置：数据入库时间(日期)
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：数据入库时间(日期)
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
