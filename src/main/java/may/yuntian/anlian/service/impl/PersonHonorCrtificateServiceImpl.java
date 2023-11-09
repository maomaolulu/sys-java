package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.PersonHonorCrtificateEntity;
import may.yuntian.anlian.mapper.PersonHonorCrtificateMapper;
import may.yuntian.anlian.service.PersonHonorCrtificateService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 人员荣誉证书
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
@Service("personHonorCrtificateService")
public class PersonHonorCrtificateServiceImpl extends ServiceImpl<PersonHonorCrtificateMapper, PersonHonorCrtificateEntity> implements PersonHonorCrtificateService {



	/**
	 *根据userId获取人员荣誉证书数据列表
	 */

	public List<PersonHonorCrtificateEntity> selectByUserIdList(Long userId) {

		List<PersonHonorCrtificateEntity> personHonorCrtificateEntityList = baseMapper.selectList(new QueryWrapper<PersonHonorCrtificateEntity>()
				.eq("user_id", userId)
				.orderByDesc("createtime")
		);
		return personHonorCrtificateEntityList;
	}



}
