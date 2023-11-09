package may.yuntian.publicity.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.publicity.vo.PublicPjPageVo;
import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.publicity.vo.PublictyPjInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author mi
 */
@Mapper
public interface PublicityMapper {


    /**
     * 公示相关列表页
     *
     * @param wrapper
     * @return
     */
    @Select("SELECT p.id,p.identifier,p.type,p.company,p.project_name,p.charge_id,p.pub_status,p.binding_status,p.protocol,pd.report_cover_date,pd.report_issue,DATEDIFF(NOW(),pd.report_cover_date) as issueDay,p.charge,p.publicity_last_time, u.subjection " +
            " FROM al_project p " +
            " left join al_project_date pd on p.id = pd.project_id " +
            " left join sys_user u on u.user_id = p.charge_id" +
            " ${ew.customSqlSegment}")
    List<PublicityPageVo> getPublicityList(@Param(Constants.WRAPPER) QueryWrapper wrapper);
//    @Select("SELECT pr.download_number, p.id,p.identifier,p.company,p.project_name,p.charge_id,p.pub_status,p.binding_status,p.protocol,pd.report_cover_date,pd.report_issue,DATEDIFF(NOW(),pd.report_cover_date) as issueDay,p.charge,p.publicity_last_time " +
//            " FROM al_project p " +
//            " left join al_project_date pd on p.id = pd.project_id left join al_project_report pr on p.id=pr.project_id " +
//            " ${ew.customSqlSegment}")


    /**
     * 获取单挑公示信息  and p.company_order = "杭州安联"   and cs.detection_type = "定期检测"
     *
     * @param id
     * @return
     */
    @Select("SELECT p.id,p.identifier,p.company_order,cs.report_cover_time,cs.company,cs.contact,cs.office_address,cs.detection_type,p.charge,p.pub_status,pd.report_cover_date,cs.accompany," +
            "DATE_FORMAT(pd.survey_date,\"%Y-%m-%d\") as surveyDate,DATE_FORMAT(pd.start_date,\"%Y-%m-%d\") as startDate,DATE_FORMAT(pd.end_date,\"%Y-%m-%d\") as endDate," +
            "cs.sampling_company,cs.sampling_date as testDate,cs.technical_persons,cs.laboratory_person,cs.publicity_path FROM al_project p left join al_project_date pd on p.id = pd.project_id  " +
            " left join t_company_survey cs on p.id = cs.project_id " +
            " WHERE p.type = \"检评\" and p.id = #{id}")
    PublicityInfoVo getPublicityOne(@Param("id") Long id);


    /**
     * xin 获取项目类型
     *
     * @param id
     * @return
     */
    @Select("SELECT p.type from al_project p WHERE  p.id = #{id}")
    PublicityInfoVo getProjectType(@Param("id") Long id);

    /**
     * zw/zj(非定期检测)  项目信息xin展示
     *
     * @param id
     * @return
     */
    @Select("SELECT p.id,p.identifier,p.company_order,cs.report_cover_time,cs.company,cs.contact,cs.office_address,cs.detection_type,p.charge,p.pub_status,pd.report_cover_date,cs.accompany," +
            "DATE_FORMAT(pd.survey_date,\"%Y-%m-%d\") as surveyDate,DATE_FORMAT(pd.start_date,\"%Y-%m-%d\") as startDate,DATE_FORMAT(pd.end_date,\"%Y-%m-%d\") as endDate," +
            "cs.sampling_company,cs.sampling_date as testDate,cs.technical_persons,cs.laboratory_person,cs.publicity_path FROM al_project p left join al_project_date pd on p.id = pd.project_id  " +
            " left join t_company_survey cs on p.id = cs.project_id " +
            " WHERE p.type = \"职卫监督\" and p.id = #{id}")
    PublicityInfoVo getInfoOccupationalHealthSupervision(@Param("id") Long id);


    @Select("SELECT p.id,p.identifier,cs.company,cs.contact,cs.office_address,cs.detection_type,p.charge,pd.report_cover_date,cs.accompany,pd.survey_date,pd.start_date,pd.end_date,cs.sampling_company,cs.sampling_date as testDate,cs.technical_persons,cs.laboratory_person,cs.publicity_path FROM al_project p left join al_project_date pd on p.id = pd.project_id \n" +
            "left join t_company_survey cs on p.id = cs.project_id\n" +
            "WHERE p.pub_status = 6 and p.binding_status = 2 and ((p.type = \"检评\" and cs.detection_type = \"定期检测\") or p.type = \"控评\" or p.type = \"现状\")")
    List<PublicityInfoVo> getList();


    /**
     * 评价公示分页查询
     * @param wrapper
     * @return
     */
    @Select("SELECT p.id,p.type,p.identifier,p.company,p.charge_id,p.charge,p.pub_status,p.binding_status,u.subjection FROM al_project p " +
            " left join sys_user u on u.user_id = p.charge_id" +
            " ${ew.customSqlSegment}")
    List<PublicPjPageVo> getPjPublicList(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);


    /**
     * 评价公示详情
     * @param id
     * @return
     */
    @Select("SELECT p.id,p.company,p.identifier,p.company_order,p.contact,p.charge,p.office_address,p.type,p.pub_status,p.binding_status,pd.report_issue," +
            "pd.report_cover_date,DATE_FORMAT(pd.report_start_date,\"%Y-%m-%d\") AS reportStartDate," +
            "DATE_FORMAT(pd.report_end_date,\"%Y-%m-%d\") AS reportEndDate,DATE_FORMAT(pd.report_survey_date,\"%Y-%m-%d\") AS surveyDate, cs.publicty_path FROM al_project p " +
            "LEFT JOIN al_project_date pd ON p.id = pd.project_id " +
            "LEFT JOIN eval_company_survey cs ON p.id = cs.project_id " +
            "WHERE p.id = #{id}")
    PublictyPjInfoVo getInfo(@Param("id") Long id);


}
