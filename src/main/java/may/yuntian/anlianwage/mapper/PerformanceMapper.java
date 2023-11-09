package may.yuntian.anlianwage.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlianwage.entity.PerformanceEntity;

/**
 * 人员绩效表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Mapper
public interface PerformanceMapper extends BaseMapper<PerformanceEntity> {
	
}
