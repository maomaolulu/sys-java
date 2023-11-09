package may.yuntian.external.province.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.external.province.entity.IndustryCategory;
import may.yuntian.external.province.vo.IndustryCategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 16:49
 */
@Mapper
public interface IndustryCategoryMapper extends BaseMapper<IndustryCategory> {

    /**
     * 查询行业分类表所有数据
     * @return
     */
    @Select("select  id, pid, letter, industry_category_code, industry_category from pro_industry_category; ")
    List<IndustryCategoryVo> getCategoryTreeAll();

}
