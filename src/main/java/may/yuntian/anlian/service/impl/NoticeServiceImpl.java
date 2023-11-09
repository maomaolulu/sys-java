package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.NoticeEntity;
import may.yuntian.anlian.entity.NoticeLikeUserEntity;
import may.yuntian.anlian.mapper.NoticeMapper;
import may.yuntian.anlian.service.NoticeLikeUserService;
import may.yuntian.anlian.service.NoticeService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysLogService;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import may.yuntian.common.utils.Query;

/**
 * 新闻公告信息
 * 业务逻辑层实现类
 *
 * @author Zhanghao
 * @date 2021-06-01
 */
@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {
	@Autowired
	private NoticeLikeUserService noticeLikeUserService;
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 分页查询
	 */

	public PageUtils queryPage(Map<String, Object> params) {
		String title = (String)params.get("title");
		String author = (String)params.get("author");
		IPage<NoticeEntity> page = this.page(
				new Query<NoticeEntity>().getPage(params),
				new QueryWrapper<NoticeEntity>()
						.eq("state", 0)
						.like(StringUtils.isNotBlank(title),"title", title)
						.like(StringUtils.isNotBlank(author),"author", author)
						.orderByDesc("createtime")

		);

		return new PageUtils(page);

	}
	
	/**
	 * 根据上一次登录日期 获取最新公告
	 * @return
	 */
	public List<NoticeEntity> getNewNotice() {
//		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
//		String lastLoginDate = sysLogService.getLastDateByUsername(sysUserEntity.getEmail());
		List<NoticeEntity> noticeList = new ArrayList<NoticeEntity>();
//		if(StringUtils.isNotBlank(lastLoginDate)) {
//			noticeList = baseMapper.selectList(newCommission QueryWrapper<NoticeEntity>()
//					.ge("createtime", lastLoginDate)
//					.orderByDesc("createtime")
//					.last("limit 5")
//					);
//		}else {
//			noticeList = baseMapper.selectList(newCommission QueryWrapper<NoticeEntity>()
//					.orderByDesc("createtime")
//					.last("limit 5")
//					);
//		}
//		if(noticeList!=null&&noticeList.size()>0) {
//			noticeList.forEach(action->{
//				action.setNumber(action.getNumber()+1);
//				this.updateById(action);
//			});
//		}
		return noticeList;
	}

	/**
	 * 修改点赞次数
	 * @param
	 */
	public void updateLikeNumbers(Long id) {

		List<NoticeLikeUserEntity> noticeLikeUserEntities = noticeLikeUserService.selectByNoticeIdList(id);
		if (noticeLikeUserEntities.size() > 0 && noticeLikeUserEntities != null) {
			//已点赞 取消点赞
			for (NoticeLikeUserEntity noticeLikeUserEntity : noticeLikeUserEntities
			) {
				noticeLikeUserService.removeById(noticeLikeUserEntity.getId());
			}

			baseMapper.updateLikeNumbersReduce(id);

		} else {
			//未点赞 点赞操作
			SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
			NoticeLikeUserEntity noticeLikeUserEntity = new NoticeLikeUserEntity();
			noticeLikeUserEntity.setNoticeId(id);
			noticeLikeUserEntity.setUserId(sysUserEntity.getUserId());
			noticeLikeUserEntity.setName(sysUserEntity.getUsername());
			noticeLikeUserService.save(noticeLikeUserEntity);

			baseMapper.updateLikeNumbersIncrease(id);

		}
	}
}
