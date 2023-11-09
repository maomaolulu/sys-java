package may.yuntian.anlian.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 *新闻评论实体类
 *
 * @author ZhangHao
 * @data 2021-05-31
 */
@TableName("t_comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 新闻Id
	 */
	private Long noticeId;
	/**
	 *评论人
	 */
	private String name;
	/**
	 *评论内容
	 */
	private String content;
	/**
	 * 用户Id
	 */
	private Long userId;
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

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
		return "CommentEntity{" +
				"id=" + id +
				", noticeId=" + noticeId +
				", name='" + name + '\'' +
				", content='" + content + '\'' +
				", userId=" + userId +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				'}';
	}
}
