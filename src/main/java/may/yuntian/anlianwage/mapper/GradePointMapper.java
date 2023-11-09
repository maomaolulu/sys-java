package may.yuntian.anlianwage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlianwage.entity.GradePointEntity;
import may.yuntian.anlianwage.vo.ProjectPerformanceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface GradePointMapper extends BaseMapper<GradePointEntity> {

    // 绩点绩效信息列表
    @Select("select pd.project_id , p.charge , p.total_money as totalMoney, cs.identifier as identifier  , p.company as company, p.netvalue as netvalue , p.type as type, \n" +
            "cs.industry_category as industry_category , cs.labor_quota as labor_quota , DATE_FORMAT(pd.report_issue,'%Y/%m/%d') as report_transfer ,\n" +
            "case p.type when '现状' then 1.3 when '控评' then 1.5 when '预评' then 1.2 when '专篇' then 1 end as item_type_score\n" +
            "from al_project_date pd \n" +
            "left join eval_company_survey cs on cs.project_id = pd.project_id\n" +
            "left join al_project p on p.id = cs.project_id\n" +
            "left join sys_user u on u.user_id = p.charge_id\n" +
            "${ew.customSqlSegment} and p.type in('现状','控评','预评','专篇') and u.subjection = '杭州安联' order by pd.report_issue asc;")
    List<ProjectPerformanceVo> getPerformanceInfoList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
