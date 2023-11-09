package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.SampleImgEntity;
import may.yuntian.anlian.vo.CompanySurveyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采样影像记录
 * 数据持久层接口
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-14 22:13:15
 */
@Mapper
public interface SampleImgMapper extends BaseMapper<SampleImgEntity> {

}
