package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.PersonHonorCrtificateEntity;
import java.util.List;

/**
 * 人员荣誉证书
 * 人员荣誉证书数据接口
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
public interface PersonHonorCrtificateService extends IService<PersonHonorCrtificateEntity> {



	/**
	 *根据userId获取评论数据列表
	 */

	List<PersonHonorCrtificateEntity> selectByUserIdList(Long userId) ;

}