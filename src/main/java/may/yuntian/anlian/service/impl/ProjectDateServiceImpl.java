package may.yuntian.anlian.service.impl;

import java.util.List;
import java.util.Map;

import may.yuntian.anlian.vo.ProjectDateVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ProjectDateMapper;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.service.ProjectDateService;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@Service("projectDateService")
public class ProjectDateServiceImpl extends ServiceImpl<ProjectDateMapper, ProjectDateEntity> implements ProjectDateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	
        IPage<ProjectDateEntity> page = this.page(
                new Query<ProjectDateEntity>().getPage(params),
                new QueryWrapper<ProjectDateEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据项目ID查询是否已经存在于项目日期信息中
     * @param projectId 项目ID
     * @return boolean
     */
    public Boolean notExistPlanByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ProjectDateEntity>().eq("project_id", projectId));
        if(count>0)
            return false;
        else
            return true;
    }

    /**
     * 根据项目ID获取项目日期相关信息
     * @param projctId
     * @return
     */
    public ProjectDateEntity getOneByProjetId(Long projctId){
        ProjectDateEntity projectDateEntity = baseMapper.selectOne(new QueryWrapper<ProjectDateEntity>().eq("project_id",projctId));
        return projectDateEntity;
    }

    public List<ProjectDateEntity> selectListByProjectIds(List<Long> ids){
        List<ProjectDateEntity> list = baseMapper.selectList(new QueryWrapper<ProjectDateEntity>().in("project_id",ids));
        return list;
    }

    /**
     * 根据条件查询项目ID列表
     */
    public List<Long> getProjectIdsByParams(ProjectDateVo queryVo) {
        List list = baseMapper.selectObjs(queryWrapperByParams(queryVo).select("project_id"));

        return (List<Long>)list;
    }

    /**
     * 用于项目信息的查询条件的处理
     * @param params
     * @return
     */
    private QueryWrapper<ProjectDateEntity> queryWrapperByParams(ProjectDateVo params) {
        //项目签订日期signDate
        String signDateStart = params.getSignDateStart();    //项目签订日期开始
        String signDateEnd = params.getSignDateEnd();        //项目签订日期结束

        String entrustDateStart = params.getEntrustDateStart();    //委托日期开始
        String entrustDateEnd = params.getEntrustDateEnd();    //委托日期结束

        String claimEndDateStart = params.getClaimEndDateStart();//要求报告完成日期开始
        String claimEndDateEnd = params.getClaimEndDateEnd();    //要求报告完成日期结束

        String surveyDateStart = params.getSurveyDateStart();//现场调查日期开始
        String surveyDateEnd = params.getSurveyDateEnd();    //现场调查日期结束

        String examineStartStart = params.getExamineStartStart();//项目评审日期开始
        String examineStartEnd = params.getExamineStartEnd();    //项目评审日期结束

        String reportIssueStart = params.getReportIssueStart();//报告签发日期开始
        String reportIssueEnd = params.getReportIssueEnd();    //报告签发日期结束

        String rptSentDateStart = params.getReportSendStart();//报告发送日期开始
        String rptSentDateEnd = params.getReportSendEnd();    //报告发送日期结束

        String rptBindingDateStart = params.getReportBindingStart();//报告装订日期开始
        String rptBindingDateEnd = params.getReportBindingEnd();    //报告装订日期结束

        String receiptDateStart = params.getReceiveAmountStart();//收款日期开始
        String receiptDateEnd = params.getReceiveAmountEnd();    //收款日期结束

        String filingDateStart = params.getReportFilingStart();//归档日期开始
        String filingDateEnd = params.getReportFilingEnd();    //归档日期结束

        String reportCoverDateStart = params.getReportCoverDateStart();
        String reportCoverDateEnd = params.getReportCoverDateEnd();

        String planDateStart = params.getStartDate();//采样日期开始
        String planDateEnd = params.getEndDate();    //采样日期结束

        String invoiceDateStart = params.getInvoiceDateStart();
        String invoiceDateEnd = params.getInvoiceDateEnd();

        QueryWrapper<ProjectDateEntity> queryWrapper = new QueryWrapper<ProjectDateEntity>()
//    			.between(StringUtils.isNotBlank(endDate), "sign_date", startDate, endDate)//项目签订日期signDate
                .ge(StringUtils.checkValNotNull(signDateStart), "sign_date", signDateStart)
                .le(StringUtils.checkValNotNull(signDateEnd), "sign_date", signDateEnd)

                .ge(StringUtils.checkValNotNull(reportCoverDateStart), "report_cover_date", reportCoverDateStart)
                .le(StringUtils.checkValNotNull(reportCoverDateEnd), "report_cover_date", reportCoverDateEnd)

//    			.between(StringUtils.isNotBlank(commissionDateEnd), "commission_date", commissionDateStart, commissionDateEnd)//项目签订日期signDate
                .ge(StringUtils.checkValNotNull(entrustDateStart), "entrust_date", entrustDateStart)
                .le(StringUtils.checkValNotNull(entrustDateEnd), "entrust_date", entrustDateEnd)

//    			.between(StringUtils.isNotBlank(claimEndDateEnd), "claimEnd_date", claimEndDateStart, claimEndDateEnd)//要求报告完成日期
                .ge(StringUtils.checkValNotNull(claimEndDateStart), "claim_end_date", claimEndDateStart)
                .le(StringUtils.checkValNotNull(claimEndDateEnd), "claim_end_date", claimEndDateEnd)

//    			.between(StringUtils.isNotBlank(onsiteInvestDateEnd), "onsite_invest_date", onsiteInvestDateStart, onsiteInvestDateEnd)//现场调查日期
                .ge(StringUtils.checkValNotNull(surveyDateStart), "survey_date", surveyDateStart)
                .le(StringUtils.checkValNotNull(surveyDateEnd), "survey_date", surveyDateEnd)

//    			.between(StringUtils.isNotBlank(projectReviewDateEnd), "project_review_date", projectReviewDateStart, projectReviewDateEnd)//项目评审日期
                .ge(StringUtils.checkValNotNull(examineStartStart), "examine_start", examineStartStart)
                .le(StringUtils.checkValNotNull(examineStartEnd), "examine_start", examineStartEnd)

//    			.between(StringUtils.isNotBlank(rptIssuanceDateEnd), "rpt_issuance_date", rptIssuanceDateStart, rptIssuanceDateEnd)//报告签发日期
                .ge(StringUtils.checkValNotNull(reportIssueStart), "report_issue", reportIssueStart)
                .le(StringUtils.checkValNotNull(reportIssueEnd), "report_issue", reportIssueEnd)

//    			.between(StringUtils.isNotBlank(rptSentDateEnd), "rpt_sent_date", rptSentDateStart, rptSentDateEnd)//报告发送日期
                .ge(StringUtils.checkValNotNull(rptSentDateStart), "report_send", rptSentDateStart)
                .le(StringUtils.checkValNotNull(rptSentDateEnd), "report_send", rptSentDateEnd)

//    			.between(StringUtils.isNotBlank(rptBindingDateEnd), "rpt_binding_date", rptBindingDateStart, rptBindingDateEnd)//报告装订日期
                .ge(StringUtils.checkValNotNull(rptBindingDateStart), "report_binding", rptBindingDateStart)
                .le(StringUtils.checkValNotNull(rptBindingDateEnd), "report_binding", rptBindingDateEnd)

//    			.between(StringUtils.isNotBlank(receiptDateEnd), "receipt_date", receiptDateStart, receiptDateEnd)//收款日期
                .ge(StringUtils.checkValNotNull(receiptDateStart), "receive_amount", receiptDateStart)
                .le(StringUtils.checkValNotNull(receiptDateEnd), "receive_amount", receiptDateEnd)

//    			.between(StringUtils.isNotBlank(filingDateEnd), "filing_date", filingDateStart, filingDateEnd)//归档日期
                .ge(StringUtils.checkValNotNull(filingDateStart), "report_filing", filingDateStart)
                .le(StringUtils.checkValNotNull(filingDateEnd), "report_filing", filingDateEnd)

                .ge(StringUtils.checkValNotNull(planDateStart), "start_date", planDateStart)
                .le(StringUtils.checkValNotNull(planDateEnd), "end_date", planDateEnd)

                .ge(StringUtils.checkValNotNull(invoiceDateStart), "invoice_date", invoiceDateStart)
                .le(StringUtils.checkValNotNull(invoiceDateEnd), "invoice_date", invoiceDateEnd);

        return queryWrapper;
    }
}
