package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ProjectMoneyLogEntity;

/**
 * 项目金额修改日志
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-04-03 08:38:30
 */
@Mapper
public interface ProjectMoneyLogMapper extends BaseMapper<ProjectMoneyLogEntity> {
	
}
