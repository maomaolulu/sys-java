package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.PersonBasicFilesEntity;
import may.yuntian.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 技术人员基本档案
 * 技术人员基本档案数据接口
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
public interface PersonBasicFilesService extends IService<PersonBasicFilesEntity> {


	/**
	 * 获取建档人员列表
	 * @param params
	 * @return
	 */
	PageUtils queryBookBuildingPage(Map<String, Object> params);

	/**
	 *根据userId获取评论数据列表
	 */

	List<PersonBasicFilesEntity> selectByUserIdList(Long userId) ;

}