package may.yuntian.anlianwage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlianwage.entity.GradePointEntity;
import may.yuntian.anlianwage.entity.IndustryBenchmarkEntity;
import may.yuntian.anlianwage.vo.ProjectPerformanceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:41
 */
@Mapper
@SuppressWarnings("all")
public interface IndustryBenchmarkMapper extends BaseMapper<IndustryBenchmarkEntity> {

}
