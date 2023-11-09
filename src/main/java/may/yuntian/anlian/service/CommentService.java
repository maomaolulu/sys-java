package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.CommentEntity;
import may.yuntian.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 新闻评论
 * 业务逻辑层接口
 *
 * @author ZhangHao
 * @date 2021-05-31
 */
public interface CommentService extends IService<CommentEntity> {


	/**
	 *分页查询
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 *根据noticeId获取评论数据列表
	 */

	List<CommentEntity> selectByNoticeIdList(Long noticeId) ;

}