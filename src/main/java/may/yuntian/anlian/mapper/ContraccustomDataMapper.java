package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ContraccustomDataEntity;

/**
 * 合同模板自定义字段数据
 * 数据持久层接口
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@Mapper
public interface ContraccustomDataMapper extends BaseMapper<ContraccustomDataEntity> {
	
}
