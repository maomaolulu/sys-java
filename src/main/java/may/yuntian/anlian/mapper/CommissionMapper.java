package may.yuntian.anlian.mapper;

import may.yuntian.anlian.dto.GradeExportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import may.yuntian.anlian.entity.CommissionEntity;

import java.util.List;


/**
 * 提成记录
 * 数据持久层接口
 *
 * @author LiXin
 * @date 2020-12-09
 */
@Mapper
public interface CommissionMapper extends BaseMapper<CommissionEntity> {

    /**
     * 财务中心-绩效提成：导出
     *
     * @param wrapper 查询条件
     * @return list
     */
    @Select("SELECT c.project_id AS projectId ,c.type AS royalty_type,c.cms_amount AS royalty_amount,c.personnel AS commenter,c.commission_date AS royalty_date,c.count_date, " +
            " p.identifier AS item_code,p.company AS emp_company,p.type AS item_type,CASE p.`status` WHEN 1 THEN '项目录入' WHEN 2 THEN '任务分配' WHEN 4 THEN '现场调查' WHEN 5 THEN '采样' WHEN 10 THEN '收样' WHEN 20 THEN '检测报告' WHEN 30 THEN '报告装订' WHEN 40 THEN '质控签发' WHEN 50 THEN '报告寄送' WHEN 60 THEN '项目归档' WHEN 70 THEN '项目结束' WHEN 98 THEN '任务挂起' WHEN 99 THEN '项目中止' END AS item_status,p.salesmen AS salesman,p.charge AS charge, CONCAT_WS( '/', p.company_order, c.subjection ) AS belong_company, apd.report_accept AS report_date,apa.total_money AS item_amount, " +
            " apa.netvalue AS net_value,apa.receipt_money AS received_amount,apa.nosettlement_money AS unsettled_amount,apa.commission AS business_cost,apa.evaluation_fee AS review_cost, " +
            " apa.service_charge AS agent_cost,apa.subproject_fee AS subcontract_cost,apa.other_expenses AS other_cost,apd.sign_date AS sign_date,apd.report_binding AS report_bind_date,p.entrust_type AS entrust_type, " +
            " apd.report_filing AS report_filing_date,apd.receive_amount AS receipt_date,p.remarks AS remark\n" +
            "FROM t_commission c\n" +
            " LEFT JOIN al_project p ON c.project_id=p.id " +
            " LEFT JOIN al_project_amount apa ON c.project_id=apa.project_id " +
            " LEFT JOIN al_project_date apd ON c.project_id=apd.project_id " +
            " ${ew.customSqlSegment} AND c.state>=2 AND c.state<=3")
    List<GradeExportDto> ListCommissionAll(@Param(Constants.WRAPPER) Wrapper<CommissionEntity> wrapper);

}
