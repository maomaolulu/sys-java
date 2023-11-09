package may.yuntian.external.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:38
 * @Version 1.0
 * @Description 跟进记录
 */
@Mapper
public interface CustomAdvanceRecordMapper extends BaseMapper<CustomAdvanceRecordEntity> {
    @Select(" select * from custom_advance_record " +
            " ${ew.customSqlSegment} ")
    List<CustomAdvanceRecordEntity> getDetails(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
