package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.PersonSupervisionRecordsEntity;
import may.yuntian.anlian.mapper.PersonSupervisionRecordsMapper;
import may.yuntian.anlian.service.PersonSupervisionRecordsService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 人员质量监督记录
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
@Service("personSupervisionRecordsService")
public class PersonSupervisionRecordsServiceImpl extends ServiceImpl<PersonSupervisionRecordsMapper, PersonSupervisionRecordsEntity> implements PersonSupervisionRecordsService {



	/**
	 *根据userId获取技术人员基本档案数据列表
	 */

	public List<PersonSupervisionRecordsEntity> selectByUserIdList(Long userId) {

		List<PersonSupervisionRecordsEntity> personSupervisionRecordsEntitys = baseMapper.selectList(new QueryWrapper<PersonSupervisionRecordsEntity>()
				.eq("user_id", userId)
				.orderByDesc("createtime")
		);
		return personSupervisionRecordsEntitys;
	}



}
