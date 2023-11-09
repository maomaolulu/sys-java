package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.NoticeLikeUserEntity;
import may.yuntian.anlian.mapper.NoticeLikeUserMapper;
import may.yuntian.anlian.service.NoticeLikeUserService;
import may.yuntian.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 新闻点赞信息
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2020-06-01
 */
@Service("noticeLikeUserService")
public class NoticeLikeUserServiceImpl extends ServiceImpl<NoticeLikeUserMapper, NoticeLikeUserEntity> implements NoticeLikeUserService {

	/**
	 *根据noticeId和userId获取新闻点赞数据列表
	 */

	public List<NoticeLikeUserEntity> selectByNoticeIdList(Long noticeId) {
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		Long userId = sysUserEntity.getUserId();

		List<NoticeLikeUserEntity> noticeLikeUserEntitys = baseMapper.selectList(new QueryWrapper<NoticeLikeUserEntity>()
				.eq("notice_id", noticeId)
				.eq("user_id",userId)
		);

		return noticeLikeUserEntitys;
	}
	



}
