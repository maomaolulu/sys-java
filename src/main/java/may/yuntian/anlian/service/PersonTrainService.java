package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.PersonTrainEntity;
import java.util.List;

/**
 * 人员培训
 * 人员培训数据接口
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
public interface PersonTrainService extends IService<PersonTrainEntity> {



	/**
	 *根据userId获取人员培训数据列表
	 */

	List<PersonTrainEntity> selectByUserIdList(Long userId) ;

}