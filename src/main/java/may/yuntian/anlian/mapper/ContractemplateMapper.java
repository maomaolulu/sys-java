package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ContractemplateEntity;

/**
 * 合同模板共同信息
 * 数据持久层接口
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Mapper
public interface ContractemplateMapper extends BaseMapper<ContractemplateEntity> {
	
}
