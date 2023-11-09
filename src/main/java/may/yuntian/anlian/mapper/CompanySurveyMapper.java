package may.yuntian.anlian.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.vo.CityCompanySurveyVo;
import may.yuntian.anlian.vo.CompanySurveyVo;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import may.yuntian.anlian.entity.CompanySurveyEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用人单位概况调查
 * 数据持久层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Mapper
public interface CompanySurveyMapper extends BaseMapper<CompanySurveyEntity> {
    @Select("select cs.id,cs.project_id,cs.company,cs.contact,cs.office_address,cs.detection_type,apd.report_cover_date report_issue,cs.accompany,apd.survey_date " +
            ",cs.publicity_status,cs.technical_persons,cs.sampling_company,cs.sampling_date,publicity_path path,ap.apply_publicity_status " +
            ",ap.charge,cs.test_date samplingDate,SUBSTRING_INDEX(cs.test_date,',', 1) samplingDate1, apd.report_accept,ap.director_reject,ap.control_reject,ap.identifier,ap.publicity_last_time,cs.laboratory_person,ap.publicity_remark " +
            "from  al_project ap  " +
            "left join  t_company_survey cs on ap.id= cs.project_id " +
            "left join al_project_date apd on cs.project_id=apd.project_id " +
            "${ew.customSqlSegment} order by samplingDate1 ")
    List<CompanySurveyVo> publicityList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("SELECT cs.company,cs.unified_code,cs.economy,cs.industry_category,cs.labor_quota,\n" +
            "cs.registered_address,cs.contact,cs.telephone,cs.identifier,cs.detection_type,\n" +
            "cs.survey_date,cs.test_date,cs.entrust_company,pd.entrust_date\n" +
            "FROM `t_company_survey` cs\n" +
            "left join al_project_date pd on cs.project_id=pd.project_id\n" +
            "where cs.project_id=#{projectId } " +
            "  ")
    CityCompanySurveyVo getCityCompanySurveyVo(Long projectId);

    /**
     * xin 评价项目公示-信息公示列表
     *
     * @param wrapper
     * @return
     */
    @Select("SELECT ap.id,ap.id projectId ,ap.company,ap.contact,ap.project_name,ap.office_address,ap.type,\n" +
            "ad.tech_charge,ad.qc_charge,ap.charge pCharge,apd.report_cover_date ,apd.report_issue \n" +
            ",ad.p_survey_companion,ad.p_survey_date,ad.p_sample_companion,ad.p_sample_date,ap.path,\n" +
            "ap.apply_publicity_status,ap.control_reject,ap.director_reject,ap.identifier,ap.publicity_last_time,ap.publicity_remark \n" +
            "from al_project ap\n" +
            "left join al_project_declare ad on ap.id=ad.project_id\n" +
            "left join al_project_date apd on ap.id=apd.project_id " +
            "${ew.customSqlSegment}  ")
    List<CompanySurveyVo> publicityListPj(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 宁波的评价公示项目
     **/
    @Select("SELECT ap.id,ap.id projectId ,ap.company,ap.contact,ap.project_name,ap.office_address,ap.type,\n" +
            "ad.tech_charge,ad.qc_charge,ap.charge pCharge,apd.report_cover_date ,apd.report_issue \n" +
            ",ad.p_survey_companion,ad.p_survey_date,ad.p_sample_companion,ad.p_sample_date,ap.path,\n" +
            "ap.apply_publicity_status,ap.control_reject,ap.director_reject,ap.identifier,ap.publicity_last_time,ap.publicity_remark \n" +
            "from al_project ap\n" +
            "left join al_project_declare ad on ap.id=ad.project_id\n" +
            "left join al_project_date apd on ap.id=apd.project_id " +
            "left join sys_user us on us.username=ap.charge " +
            "${ew.customSqlSegment}  ")
    List<CompanySurveyVo> publicityListPjNb(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
