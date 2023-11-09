package may.yuntian.anlianwage.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlianwage.entity.TaskEntity;

/**
 * 
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-30 15:04:26
 */
@Mapper
public interface TaskMapper extends BaseMapper<TaskEntity> {
	
}
