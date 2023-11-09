package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.CommentEntity;
import may.yuntian.anlian.mapper.CommentMapper;
import may.yuntian.anlian.service.CommentService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 技术人员信息
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-11-17
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {


	/**
	 *分页查询
	 */

	public PageUtils queryPage(Map<String, Object> params) {
		String noticeId = (String)params.get("noticeId");
		IPage<CommentEntity> page = this.page(
				new Query<CommentEntity>().getPage(params),
				new QueryWrapper<CommentEntity>()
						.eq("notice_id",Long.valueOf(noticeId))
						.orderByDesc("createtime")

		);

		return new PageUtils(page);

	}

	/**
	 *根据noticeId获取评论数据列表
	 */

	public List<CommentEntity> selectByNoticeIdList(Long noticeId) {

		List<CommentEntity> areaEntityList = baseMapper.selectList(new QueryWrapper<CommentEntity>()
				.eq("notice_id", noticeId)
				.orderByDesc("createtime")
		);
		return areaEntityList;
	}



}
