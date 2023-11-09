package may.yuntian.external.wanda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.external.wanda.entity.ChemicalDustFactor;
import may.yuntian.external.wanda.vo.ResultPartInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-03-08 11:01
 */
@Mapper
public interface ChemicalDustFactorMapper extends BaseMapper<ChemicalDustFactor> {

    /**
     * 校验：车间-岗位-劳动定员
     *
     * @param projectId
     * @return
     */
    @Select("SELECT work_unit_name, work_job_name, work_num FROM wanda_chemical_dust_factor WHERE project_id = #{projectId} ")
    List<ResultPartInfoVo> checkWorkNum(@Param("projectId") Long projectId);

}
