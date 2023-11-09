package may.yuntian.anlian.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 *新闻点赞实体类
 *
 * @author ZhangHao
 * @data 2021-05-31
 */
@TableName("t_notice_like_user")
public class NoticeLikeUserEntity implements Serializable {
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
	 * 用户Id
	 */
	private Long userId;
	/**
	 *点赞人
	 */
	private String name;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NoticeLikeUserEntity{" +
				"id=" + id +
				", noticeId=" + noticeId +
				", userId=" + userId +
				", name='" + name + '\'' +
				'}';
	}
}
