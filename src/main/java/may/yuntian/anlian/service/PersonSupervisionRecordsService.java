package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.PersonSupervisionRecordsEntity;
import java.util.List;

/**
 * 人员质量监督记录
 * 人员质量监督记录数据接口
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
public interface PersonSupervisionRecordsService extends IService<PersonSupervisionRecordsEntity> {



	/**
	 *根据userId获取人员质量监督记录列表
	 */

	List<PersonSupervisionRecordsEntity> selectByUserIdList(Long userId) ;

}