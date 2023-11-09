package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.PersonTrainEntity;
import may.yuntian.anlian.mapper.PersonTrainMapper;
import may.yuntian.anlian.service.PersonTrainService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 人员培训
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
@Service("personTrainService")
public class PersonTrainServiceImpl extends ServiceImpl<PersonTrainMapper, PersonTrainEntity> implements PersonTrainService {




	/**
	 *根据userId获取人员培训数据列表
	 */

	public List<PersonTrainEntity> selectByUserIdList(Long userId) {

		List<PersonTrainEntity> personTrainEntitys = baseMapper.selectList(new QueryWrapper<PersonTrainEntity>()
				.eq("user_id", userId)
				.orderByDesc("createtime")
		);
		return personTrainEntitys;
	}



}
