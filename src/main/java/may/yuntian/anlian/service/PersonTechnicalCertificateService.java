package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.PersonTechnicalCertificateEntity;
import java.util.List;

/**
 * 人员技能证书
 * 人员技能证书数据接口
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
public interface PersonTechnicalCertificateService extends IService<PersonTechnicalCertificateEntity> {



	/**
	 *根据userId获取人员技能证书数据列表
	 */

	List<PersonTechnicalCertificateEntity> selectByUserIdList(Long userId) ;

}