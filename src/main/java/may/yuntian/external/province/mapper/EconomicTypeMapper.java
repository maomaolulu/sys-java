package may.yuntian.external.province.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.external.province.entity.EconomicType;
import may.yuntian.external.province.vo.EconomicTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 13:33
 */
@Mapper
public interface EconomicTypeMapper extends BaseMapper<EconomicType> {

    /**
     * 查询经济类型所有数据
     * @return
     */
    @Select("select * from pro_economic_type")
    List<EconomicTypeVo> getEconomicTypeAll();

}
