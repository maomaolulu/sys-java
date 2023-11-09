package may.yuntian.anliantest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anliantest.entity.MetNews;
import may.yuntian.anliantest.vo.EvaluateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 项目公示
 * 
 * @author zhanghao
 * @date 2022-04-14
 */
@Mapper
public interface MetNewsMapper extends BaseMapper<MetNews> {
    /**
     * 根据id查询公示项目需要生成的pdf的信息
     * @param projectId
     * @return
     */
    @Select("select cs.project_id,pr.type,cs.company,cs.office_address,cs.contact,pd.tech_charge,pd.qc_charge,pd.p_charge, " +
            "pd.p_prepare_person,pd.p_survey_companion,pd.p_survey_date,pd.p_sample_companion, " +
            "pd.p_sample_date,pd.p_issued_date " +
            "from al_project pr  " +
            "left join al_company_survey cs on pr.id=cs.project_id " +
            "left join al_project_declare pd on pr.id=pd.project_id  " +
            "where pr.id=#{projectId } ")
    EvaluateVo selectById(Long projectId);
    /**
     * 现场调查人员 fieldInvestigators
     */
    @Select("select group_concat(username)  from al_project_user where project_id=1  and types=110 GROUP BY  project_id  ")
    String selectFieldInvestigators (Long projectId);
    /**
     * 现场采样，现场检测人员 fieldSamplings
     */
    @Select("select group_concat(username)  from al_project_user where project_id=1  and types=120 GROUP BY  project_id  ")
    String selectFieldSamplings (Long projectId);
}
