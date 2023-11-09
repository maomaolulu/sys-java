package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.NoticeEntity;
import may.yuntian.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 新闻公告
 * 业务逻辑层接口
 *
 * @author ZhangHao
 * @date 2021-05-06
 */
public interface NoticeService extends IService<NoticeEntity> {


	/**
	 *分页查询 现在不用
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 修改点赞次数
	 * @param
	 */
	void updateLikeNumbers(Long id) ;
	
	/**
	 * 根据上一次登录日期 获取最新公告
	 * @return
	 */
	List<NoticeEntity> getNewNotice();

}