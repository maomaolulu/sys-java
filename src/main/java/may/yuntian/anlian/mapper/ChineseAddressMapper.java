package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ChineseAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author maoly
 * @Date 2023/8/28 9:41
 */
@Mapper
public interface ChineseAddressMapper extends BaseMapper<ChineseAddressEntity> {
}
