package may.yuntian.publicity.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.publicity.vo.PublicityPageVo;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.publicity.entity.PublicityInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目公示记录
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
@Mapper
public interface PublicityInfoMapper extends BaseMapper<PublicityInfoEntity> {

//" left join t_company_survey cs on p.id = cs.project_id " +  left join al_project_date pd on p.id = pd.project_id
    @Select("SELECT p.id,p.identifier,p.type,p.company,p.pub_status,p.charge,p.publicity_last_time  FROM al_project p " +

            " ${ew.customSqlSegment}")
    List<PublicityPageVo> getAllList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

	@Select("SELECT * FROM zj_publicity_info WHERE project_id = #{projectId}")
    List<PublicityInfoEntity> getListByProjectId(@Param("projectId") Long projectId);
}
