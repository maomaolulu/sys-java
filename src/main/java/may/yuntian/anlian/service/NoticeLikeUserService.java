package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.NoticeLikeUserEntity;

import java.util.List;

/**
 * 新闻点赞评论
 * 业务逻辑层接口
 *
 * @author ZhangHao
 * @date 2021-06-01
 */
public interface NoticeLikeUserService extends IService<NoticeLikeUserEntity> {


	/**
	 *根据noticeId获取评论数据列表
	 */

	List<NoticeLikeUserEntity> selectByNoticeIdList(Long noticeId) ;

}