package may.yuntian.anlian.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 *新闻公告实体类
 *
 * @author ZhangHao
 * @data 2021-05-06
 */
@TableName("t_notice")
public class NoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;

	/**
	 *标题
	 */
	private String title;
	/**
	 *内容
	 */
	private String content;
	/**
	 *作者
	 */
	private String author;
	/**
	 *访问次数
	 */
	private Integer number;
	/**
	 *点赞次数
	 */
	private Integer likeNumbers;
	/**
	 *是否点赞
	 */
	private Integer likeState;
	/**
	 *状态
	 */
	private Integer state;
	/**
	 *部门名称
	 */
	private String deptName;

	/**
	 *数据入库时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getLikeNumbers() {
		return likeNumbers;
	}

	public void setLikeNumbers(Integer likeNumbers) {
		this.likeNumbers = likeNumbers;
	}

	public Integer getLikeState() {
		return likeState;
	}

	public void setLikeState(Integer likeState) {
		this.likeState = likeState;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 *部门名称
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 *部门名称
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Override
	public String toString() {
		return "NoticeEntity{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", author='" + author + '\'' +
				", number=" + number +
				", likeNumbers=" + likeNumbers +
				", likeState=" + likeState +
				", state=" + state +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				'}';
	}
}
