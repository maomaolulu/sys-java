package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import may.yuntian.anlian.vo.*;
import may.yuntian.homepage.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ProjectEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目表(包含了原任务表的字段)
 * 数据持久层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {
    /**
     * 根据查询条件统计金额
     *
     * @param userWrapper
     * @return
     */
    @Select("SELECT SUM(IFNULL(ap.total_money,0)) AS toltalMoney, SUM(IFNULL(p1.commission,0)) AS commission, SUM(IFNULL(p1.evaluation_fee,0)) AS evaluationFee, SUM(IFNULL(p1.subproject_fee,0)) AS subprojectFee, " +
            " SUM(IFNULL(p1.service_charge,0)) AS serviceCharge, SUM(IFNULL(p1.other_expenses,0)) AS otherExpenses, SUM(IFNULL(ap.netvalue,0)) AS netvalue, SUM(IFNULL(p1.nosettlement_money,0)) AS nosettlementMoney,  " +
            " SUM(IFNULL(p1.receipt_money,0)) AS receiptMoney, SUM(IFNULL(p1.commission_outstanding,0)) AS commissionOutstanding, SUM(IFNULL(p1.evaluation_outstanding,0)) AS evaluationoutstanding, " +
            " SUM(IFNULL(p1.subproject_outstanding,0)) AS subprojectOutstanding, SUM(IFNULL(p1.service_charge_outstanding,0)) AS serviceChargeOutstanding, SUM(IFNULL(p1.service_charge_outstanding,0)) AS serviceChargeOutstanding,  " +
            " SUM(IFNULL(p1.other_expenses_outstanding,0)) AS otherExpensesOutstanding , SUM(IFNULL(p1.virtual_tax,0)) AS virtualTax , SUM(IFNULL(p1.invoice_money,0)) AS invoiceMoney " +
            " FROM al_project_amount p1, al_project ap ${ew.customSqlSegment}")
    MoneyVo sumMoneyByMyWrapper(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

    /**
     * 根据查询条件统计金额
     *
     * @param userWrapper
     * @return
     */
    @Select("SELECT SUM(IFNULL(p1.total_money,0)) AS toltalMoney, SUM(IFNULL(p1.commission,0)) AS commission, SUM(IFNULL(p1.evaluation_fee,0)) AS evaluationFee, SUM(IFNULL(p1.subproject_fee,0)) AS subprojectFee, " +
            " SUM(IFNULL(p1.service_charge,0)) AS serviceCharge, SUM(IFNULL(p1.other_expenses,0)) AS otherExpenses, SUM(IFNULL(p1.netvalue,0)) AS netvalue, SUM(IFNULL(p1.nosettlement_money,0)) AS nosettlementMoney,  " +
            " SUM(IFNULL(p1.receipt_money,0)) AS receiptMoney, SUM(IFNULL(p1.commission_outstanding,0)) AS commissionOutstanding, SUM(IFNULL(p1.evaluation_outstanding,0)) AS evaluationoutstanding, " +
            " SUM(IFNULL(p1.subproject_outstanding,0)) AS subprojectOutstanding, SUM(IFNULL(p1.service_charge_outstanding,0)) AS serviceChargeOutstanding, SUM(IFNULL(p1.service_charge_outstanding,0)) AS serviceChargeOutstanding,  " +
            " SUM(IFNULL(p1.other_expenses_outstanding,0)) AS otherExpensesOutstanding , SUM(IFNULL(p1.virtual_tax,0)) AS virtualTax , SUM(IFNULL(p1.invoice_money,0)) AS invoiceMoney " +
            " FROM al_project_amount p1 ${ew.customSqlSegment}")
    MoneyVo sumMoneyByMyWrapper2(@Param(Constants.WRAPPER) Wrapper<ProjectAmountEntity> userWrapper);


    /**
     * 项目总览导出
     *
     * @param userWrapper
     * @return
     */
    @Select("SELECT p.id,p.identifier,p.company,p.status,p.type,p.charge,p.project_name as projectName,p.createtime,p.company_order as companyOrder,p.business_source as businessSource,pc.id as projectCountId," +
            " pc.survey,pc.survey_last assurveyLast,pc.plan_commit as planCommit,pc.plan_commit_last as planCommitLast,pc.sample_record as sampleRecord,pc.sample_record_last as sampleRecordLast,pc.delivery_sheet as deliverySheet," +
            " pc.delivery_sheet_last as deliverySheetLast,pc.test_result as testResult,pc.test_result_last as testResultLast,pc.result_math as resultMath,pc.result_math_last as resultMathLast,pc.report_generate as reportGenerate," +
            " pc.report_generate_last as reportGenerateLast,pc.final_report as finalReport,pc.final_report_last as finalReportLast " +
            " FROM al_project p left join al_project_count pc on p.id=pc.project_id ${ew.customSqlSegment}")
    List<ProjectCountVo> exportProjectCountByMyWrapper(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);


    @Select("SELECT p.id,p.identifier,p.contract_id,p.contract_identifier,p.company_id,p.company,p.province,p.city,p.area,p.office_address,p.status,p.type,p.dept_id,p.charge_id,p.charge,p.charge_job_num,p.contact,p.telephone," +
            " p.project_name,p.entrust_type,p.entrust_company,p.entrust_company_id,p.entrust_office_address,p.company_order,p.business_source,p.salesmenid,p.salesmen,p.urgent,p.old,p.expressnumber,p.total_money,p.netvalue,p.remarks," +
            " p.userid,p.username,p.createtime,p.updatetime,pd.entrust_date,pd.sign_date,pd.claim_end_date,pd.start_date,pd.end_date,pd.survey_date,pd.examine_start,pd.report_binding,pd.report_cover_date,pd.report_send,pd.report_filing," +
            " pd.report_issue,pa.receipt_money,pa.nosettlement_money,pa.commission,pa.commission_ratio,pa.commission_outstanding,pa.evaluation_fee,pa.evaluation_outstanding,pa.subproject_fee,pa.subproject_outstanding,pa.service_charge," +
            " pa.service_charge_outstanding,pa.other_expenses,pa.other_expenses_outstanding,pa.invoice_money,pa.virtual_tax FROM al_project p " +
            " left join al_project_date pd on p.id = pd.project_id " +
            " left join al_project_amount pa on p.id = pa.project_id " +
            " ${ew.customSqlSegment}")
    List<QueryProjectAmountDateVo> queryAllprojectAmountDate(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);


    /**
     * -------------   天气情况 --------------------------
     */
    @Select("SELECT count(*) FROM al_weather WHERE project_id = #{projectId} and gather_date = #{gatherDate}")
    int getWeatherInfo(@Param("projectId") Long projectId, @Param("gatherDate") String gatherDate);

    @Update("UPDATE al_weather set gather_send_date = #{gatherSendDate} WHERE project_id = #{projectId} and gather_date = #{gatherDate}")
    void updateGatherSendDate(@Param("projectId") Long projectId, @Param("gatherDate") String gatherDate, @Param("gatherSendDate") String gatherSendDate);


    @Update("UPDATE al_weather set gather_accept_date = #{gatherAcceptDate} WHERE project_id = #{projectId} and gather_date = #{gatherDate}")
    void updateGatherAcceptDate(@Param("projectId") Long projectId, @Param("gatherDate") String gatherDate, @Param("gatherAcceptDate") String gatherAcceptDate);


    /**
     * 项目中止或重启
     *
     * @param projectId
     * @param status
     */
    @Update("UPDATE al_project set status = #{status} WHERE id = #{projectId}")
    void suspendOrRestartProject(@Param("projectId") Long projectId, @Param("status") Integer status);

    /**
     * 业务员：1.新老客户分析饼
     *
     * @param startDate   起始日期
     * @param endDate     结束日期
     * @param loginUserId 当前登录用户id
     * @return
     */
    @Select("select ap.old \n" +
            "from al_project as ap\n" +
            "left join al_project_date as apd on ap.id = apd.project_id\n" +
            "where apd.sign_date >= #{startDate} and apd.sign_date < #{endDate} and ap.salesmenid = #{loginUserId} ;")
    List<AnalyseCakeVo> getNewOldClientList(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("loginUserId") Long loginUserId);

    /**
     * 业务员：2.业务费打款 and 本年度回款信息
     *
     * @param startDate   起始日期
     * @param endDate     截止日期
     * @param loginUserId 当前登录用户id
     * @return
     */
    @Select("select SUM(IFNULL(t2.commission,0)) as deal_with_fees, SUM(IFNULL(t2.commission_outstanding,0)) as unsettled_commission, SUM(IFNULL(t2.total_money,0)) as contract_amount, SUM(IFNULL(t2.receipt_money,0)) as pay_in_return\n" +
            "from al_project as t1\n" +
            "left join al_project_amount as t2 on t1.id = t2.project_id\n" +
            "left join al_project_date as t3 on t1.id = t3.project_id\n" +
            "where t3.sign_date >= #{startDate} and t3.sign_date < #{endDate}  and t1.salesmenid = #{loginUserId} ;")
    FeesCollectionVo getTotalFees(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("loginUserId") Long loginUserId);

    /**
     * 业务员：3.项目节点开票提醒  and 已开票未回款情况
     *
     * @param loginUserId 当前登录用户id
     * @param startDate   起始日期
     * @param endDate     截止日期
     * @return
     */
    @Select("select ap.id, ap.project_name, apd.report_issue, apa.total_money, apa.receipt_money, apa.invoice_money, MID(apd.report_issue,6,2) as 'month'\n" +
            "from al_project as ap\n" +
            "left join al_project_date as apd on ap.id = apd.project_id\n" +
            "left join al_project_amount as apa on ap.id = apa.project_id\n" +
            "where apa.total_money = apa.invoice_money and ap.salesmenid = #{loginUserId} and apd.report_issue >= #{startDate} and apd.report_issue < #{endDate} " +
            "ORDER BY apd.report_issue DESC;")
    List<AnalyseCakeVo> getItemNodeInvoiceRemind(@Param("loginUserId") Long loginUserId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 业务员：4.合同指标 + 当年回款
     *截止日期
     * @return aaa
     */
    @Select("select ap.total_money, apa.receipt_money, IF(\"createtime\" = #{data},MID(ap.createtime,6,2),MID(apd.sign_date,6,2)) as 'month'\n" +
            "from al_project as ap \n" +
            "left join al_project_date as apd on ap.id = apd.project_id\n" +
            "left join al_project_amount as apa on ap.id = apa.project_id\n" +
            "${ew.customSqlSegment}")
    List<AnalyseCakeVo> getContractIndex(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper,@Param("data") String data);


    @Select("SELECT\n" +
            "\tROUND( SUM( IFNULL( pa.receipt_money, 0 ) ) / 10000, 2 ) as receipt_money\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id " +
            "${ew.customSqlSegment}")
    BigDecimal getTypeIncome(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);


    /**
     * 每月回款完成率及签订合同完成率
     * 业务所属占比 业务来源占比 各类型业务占比
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("SELECT\n" +
            "\tMID( pd.sign_date, 6, 2 ) AS 'month',\n" +
            "\tROUND( SUM( IFNULL( pa.receipt_money, 0 ) ) / 10000, 2 ) AS receipt_money,\n" +
            "\tROUND( SUM( IFNULL( pa.total_money, 0 ) ) / 10000, 2 ) AS total_money\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id " +
            "WHERE pd.sign_date >= #{startDate} and pd.sign_date < #{endDate}" +
            "GROUP BY month")
    List<BusinessDirectorVo> getBudgetAndContractMoney1(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Select("\tSELECT \n" +
            "\tDISTINCT\n" +
            "\tp.business_source,\n" +
            "\tROUND( SUM( IFNULL(pa.total_money , 0 ) )/ 10000, 2 ) AS total_money \n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id \n" +
            "WHERE\n" +
            "\tpd.sign_date >= #{startDate} and pd.sign_date < #{endDate}\n" +
            "\tGROUP BY p.business_source")
    List<BusinessDirectorVo> getBusinessSource(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Select("SELECT\n" +
            "\tDISTINCT p.company_order,\n" +
            "\tROUND(SUM(IFNULL(pa.total_money , 0 ))/10000,2) AS total_money \n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id \n" +
            "WHERE\n" +
            "\tpd.sign_date >=  #{startDate} and pd.sign_date < #{endDate}\n" +
            "\tGROUP BY p.company_order")
    List<BusinessDirectorVo> getCompanyOrder(@Param("startDate") Date startDate, @Param("endDate") Date endDate);



    @Select("SELECT\n" +
            "\tROUND( SUM( IFNULL( pa.total_money, 0 ) ) / 10000, 2 ) as total_money\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id " +
            "${ew.customSqlSegment}")
    BigDecimal getTypes(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

    @Select("\tSELECT DISTINCT\n" +
            "\tp.salesmen,\n" +
            "\tCOUNT(p.id) as quantity\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id " +
            " ${ew.customSqlSegment}")
    List<BusinessDirectorVo> getSignRanking(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

    @Select("SELECT DISTINCT\n" +
            "\tp.salesmen,\n" +
            "\tROUND( SUM( IFNULL( pa.receipt_money, 0 )),2) AS receipt_money\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id " +
            "${ew.customSqlSegment}")
    List<BusinessDirectorVo> getBudgetRanking1(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);



    @Select("SELECT DISTINCT\n" +
            "\tMID( pd.sign_date, 1, 4 ) AS 'year',\n" +
            "\tROUND(SUM( IFNULL(pa.receipt_money , 0 ) )/10000,2) AS receipt_money,\n" +
            "\tROUND(SUM( IFNULL(pa.total_money , 0 ) )/10000,2) AS total_money \n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id \n" +
            "GROUP BY\n" +
            "YEAR \n" +
            "ORDER BY\n" +
            "YEAR")
    List<BusinessDirectorVo> getAllRanking1();

    //TODO OA获取项目金额信息
    @Select("SELECT p.identifier,p.project_name,p.salesmen,p.type,p.business_source,p.company_order,pa.total_money,pa.commission,pa.evaluation_fee,pa.subproject_fee,pa.service_charge,pa.other_expenses,pa.virtual_tax " +
            "FROM al_project p " +
            "left join al_project_amount pa on p.id = pa.project_id " +
            " ${ew.customSqlSegment}")
//            "WHERE p.identifier = #{identifier}")
    OAProjectAmountVo getInfomation(@Param(Constants.WRAPPER) Wrapper<Object> userWrapper);

    /**
     * 根据查询条件统计提成金额
     *
     * @param userWrapper
     * @return
     */
    @Select("select type,sum(cms_amount) cms_amount from t_commission  ${ew.customSqlSegment}  GROUP BY type")
    List<CommissionEntity> selectCommissionList(@Param(Constants.WRAPPER) Wrapper<Object> userWrapper);

    /**
     * OA-获取项目信息(包含合同下的其他项目信息)
     *
     * @param identifier 项目编号
     * @return 项目信息
     */
    @Select("SELECT ap.id,ap.identifier,ap.contract_id,ap.contract_identifier,ap.`status`,ap.type \n" +
            "FROM al_project ap \n" +
            "WHERE ap.contract_id = ( SELECT ap1.contract_id FROM al_project ap1 WHERE ap1.identifier = #{identifier} )")
    List<ProjectEntity> getContractProjects(@Param("identifier") String identifier);

    /**
     * 变更项目信息（项目编号及项目类型）
     *
     * @param oldIdentifier 原项目编号
     * @param newIdentifier 新项目编号
     * @param newType       新项目类型
     * @param remarks       备注
     */
    @Update("UPDATE al_project SET identifier = #{newIdentifier}, type = #{newType}, remarks = #{remarks} WHERE identifier = #{oldIdentifier}")
    void updateProjectInfoByIdentifier(@Param("oldIdentifier") String oldIdentifier,
                                       @Param("newIdentifier") String newIdentifier,
                                       @Param("newType") String newType, @Param("remarks") String remarks);

    /**
     * 条件查询已签订项目签订日期,项目净值,项目状态
     * @param wrapper 查询条件
     * @return List<ProductionOverviewVo>
     */
    @Select("SELECT DATE_FORMAT(apd.sign_date, '%Y-01-01') `year`,DATE_FORMAT(apd.sign_date, '%Y-%m-01') `month`,DATE_FORMAT(apd.sign_date, '%Y-%m-%d') `day`, ap.netvalue FROM `al_project` ap \n" +
            "LEFT JOIN `al_project_date` apd ON apd.project_id = ap.id " +
            "${ew.customSqlSegment}")
    List<ProductionOverviewVo> getProductionOverviewNetValue(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);

    /**
     * 条件查询已签订项目签订日期,项目净值,项目状态
     * @param wrapper 查询条件
     * @return List<ProductionOverviewVo>
     */
    @Select("SELECT DATE_FORMAT(apd.report_issue, '%Y-01-01') `year`,DATE_FORMAT(apd.report_issue, '%Y-%m-01') `month`,DATE_FORMAT(apd.report_issue, '%Y-%m-%d') `day`, ap.netvalue FROM `al_project` ap \n" +
            "LEFT JOIN `al_project_date` apd ON apd.project_id = ap.id " +
            "${ew.customSqlSegment}")
    List<ProductionOverviewVo> getProductionOverviewPublicityInfo(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);


    /**
     * 条件查询项目类型和项目完成天数
     * @param companyOrder 查询条件
     * @param sTime 查询开始时间
     * @param eTime 查询结束时间
     * @return List<ProductionOverviewVo>
     */
    @Select("SELECT ap.type, DATEDIFF(apd.report_issue,apd.task_release_date) use_date FROM `al_project` ap\n" +
            "LEFT JOIN al_project_date apd ON apd.project_id = ap.id\n" +
            "WHERE ap.`status` >= 40 AND ap.`status` NOT IN (98,99)\n" +
            "AND ap.company_order = #{companyOrder} AND apd.sign_date >= #{sTime} AND apd.sign_date < #{eTime}")
    List<FinishSpeedVo> getFinishSpeedList(@Param("companyOrder") String companyOrder,@Param("sTime") Date sTime,@Param("eTime") Date eTime);

    /**
     * 项目完成时效详情
     * @param wrapper 查询条件
     * @return List<ProductionOverviewVo>
     */
    @Select("SELECT ap.company_order,\n" +
            "ap.type,\n" +
            "ap.identifier,\n" +
            "ap.project_name,\n" +
            "apd.sign_date,\n" +
            "IF(ap.type IN ('检评','控评','现状'),DATEDIFF(tpp.createtime, apd.task_release_date),NULL) time1,\n" +
            "IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apc.plan_commit, tpp.createtime),DATEDIFF(tpp.createtime, apd.task_release_date)) time2,\n" +
            "DATEDIFF(apc.sample_record, apc.plan_commit) time3,\n" +
            "DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record) time4,\n" +
            "DATEDIFF(lrr.audit_date, IF(apd.deliver_date < apd.physical_send_date,apd.deliver_date,apd.physical_send_date)) time5,\n" +
            "IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apd.report_issue, lrr.audit_date),DATEDIFF(apd.report_issue, tpp.createtime)) time6\n" +
            "FROM `al_project` ap \n" +
            "LEFT JOIN al_project_date apd ON apd.project_id = ap.id\n" +
            "LEFT JOIN t_project_procedures tpp ON tpp.project_id = ap.id AND tpp.`status` = 4\n" +
            "LEFT JOIN al_project_count apc ON apc.project_id = ap.id \n" +
            "LEFT JOIN (\n" +
            "SELECT project_id, MIN(audit_date) audit_date\n" +
            "FROM lab_report_record \n" +
            "GROUP BY project_id \n" +
            ") lrr ON lrr.project_id = ap.id \n" +
            "${ew.customSqlSegment}")
    List<ProjectSpeedDetailVo> getProjectSpeedList(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);


    /**
     * 项目核算页面统计接口
     * @param userWrapper
     * @return
     */
    @Select("SELECT\n" +
            "\tSUM(IFNULL( pa.total_money, 0 )) AS toltalMoney,\n" +
            "\tSUM(IFNULL( pa.netvalue,0 )) AS netvalue,\n" +
            "\tSUM(IFNULL( pa.commission, 0 )) AS commission,\n" +
            "\tSUM(IFNULL( pa.evaluation_fee, 0 )) AS evaluationFee,\n" +
            "\tSUM(IFNULL( pa.subproject_fee, 0 )) AS subprojectFee,\n" +
            "\tSUM(IFNULL( pa.service_charge,0 )) AS serviceCharge,\n" +
            "\tSUM(IFNULL( pa.other_expenses,0 )) AS otherExpenses,\n" +
            "\tSUM(IFNULL( c2.yewuticheng,0 )) AS yewu,\n" +
            "\tSUM(IFNULL( c2.kefu,0 )) AS kefu,\n" +
            "\tSUM(IFNULL( c2.caiyang,0 )) AS caiyang,\n" +
            "\tSUM(IFNULL( c2.qianfa,0 )) AS qianfa,\n" +
            "\tSUM(IFNULL( c2.baogao,0 )) AS baogao,\n" +
            "\tSUM(IFNULL( c2.jiance,0 )) AS jiance,\n" +
            "\tSUM(IFNULL( c2.bianzhi,0 )) AS bianzhi\n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id\n" +
            "\tLEFT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tc1.project_id,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"业务提成\" THEN amounts ELSE 0 END ) AS yewuticheng,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"客服提成\" THEN amounts ELSE 0 END ) AS kefu,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"采样提成\" THEN amounts ELSE 0 END ) AS caiyang,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"签发提成\" THEN amounts ELSE 0 END ) AS qianfa,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"报告提成\" THEN amounts ELSE 0 END ) AS baogao,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"检测提成\" THEN amounts ELSE 0 END ) AS jiance,\n" +
            "\t\tMAX( CASE c1.commission_type WHEN \"报告编制\" THEN amounts ELSE 0 END ) AS bianzhi \n" +
            "\tFROM\n" +
            "\t\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tc.project_id,\n" +
            "\t\t\tc.commission_type,\n" +
            "\t\t\tSUM(IFNULL( c.accrual_amount, 0 )) AS amounts \n" +
            "\t\tFROM\n" +
            "\t\t\tal_commission c \n" +
            "\t\tGROUP BY\n" +
            "\t\t\tc.project_id,\n" +
            "\t\t\tc.commission_type \n" +
            "\t\t) AS c1 \n" +
            "\tGROUP BY\n" +
            "\t\tc1.project_id \n" +
            "\t) AS c2 ON p.id = c2.project_id ${ew.customSqlSegment}")
    ProjectAccountingVo getAccounting(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

    /**
     * 项目核算列表
     * @param userWrapper
     * @return
     */
    @Select("SELECT\n" +
            "\tp.id,\n" +
            "\tp.identifier,\n" +
            "\tp.company_order,\n" +
            "\tp.business_source,\n" +
            "\tpd.sign_date,\n" +
            "\tp.`status`,\n" +
            "\tp.salesmen,\n" +
            "\tpa.total_money as toltalMoney,\n" +
            "\tpa.netvalue,\n" +
            "\tpa.commission,\n" +
            "\tpa.evaluation_fee,\n" +
            "\tpa.subproject_fee,\n" +
            "\tpa.service_charge,\n" +
            "\tpa.other_expenses \n" +
            "FROM\n" +
            "\tal_project p\n" +
            "\tLEFT JOIN al_project_amount pa ON p.id = pa.project_id\n" +
            "\tLEFT JOIN al_project_date pd ON p.id = pd.project_id ${ew.customSqlSegment}")
    List<ProjectAccountingListVo> getAccountingDataList(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);
}
