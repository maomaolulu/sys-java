package may.yuntian.external.province.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.external.province.entity.SubstanceContrastResult;
import may.yuntian.external.province.vo.ResultCodeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-23 15:31
 */
@Mapper
public interface SubstanceContrastResultMapper extends BaseMapper<SubstanceContrastResult> {

    /**
     * 结果项及编码列表
     * @return list
     */
    @Select("select DISTINCT result_item_code, result_item_name from pro_substance_contrast_result ORDER BY result_item_code asc;")
    List<ResultCodeVo> getResultCodeList();

}
