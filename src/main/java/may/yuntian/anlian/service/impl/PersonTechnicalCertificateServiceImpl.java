package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.PersonTechnicalCertificateEntity;
import may.yuntian.anlian.mapper.PersonTechnicalCertificateMapper;
import may.yuntian.anlian.service.PersonTechnicalCertificateService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 人员技能证书
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
@Service("personTechnicalCertificateService")
public class PersonTechnicalCertificateServiceImpl extends ServiceImpl<PersonTechnicalCertificateMapper, PersonTechnicalCertificateEntity> implements PersonTechnicalCertificateService {



	/**
	 *根据userId获取人员技能证书数据列表
	 */

	public List<PersonTechnicalCertificateEntity> selectByUserIdList(Long userId) {

		List<PersonTechnicalCertificateEntity> personTechnicalCertificateEntitys = baseMapper.selectList(new QueryWrapper<PersonTechnicalCertificateEntity>()
				.eq("user_id", userId)
				.orderByDesc("createtime")
		);
		return personTechnicalCertificateEntitys;
	}



}
