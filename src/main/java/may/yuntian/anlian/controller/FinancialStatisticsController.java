package may.yuntian.anlian.controller;

import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.service.FinancialStatisticsService;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.modules.sys_v2.utils.DateUtils;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务统计
 *
 * @author hjy
 * @date 2023/5/11 11:48
 */
@RestController
@RequestMapping("/financial/statistics/")
public class FinancialStatisticsController {

    private final FinancialStatisticsService statisticsService;

    public FinancialStatisticsController(FinancialStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 获取财务统计数据-公司维度
     */
    @GetMapping("/company/list")
    @AuthCode(url = "financialStatistics", system = "sys")
    public AjaxResult companyList(String company, String businessSource, String startTime, String endTime, AuthCodeVo authCodeVo) {
        startTime = StringUtils.nvl(startTime, "2020-01-01");
        endTime = StringUtils.nvl(endTime, DateUtils.getDate());
        //权限职能
        if (IntellectConstants.BRANCH_PERMISSIONS.equals(authCodeVo.getAuthCode())) {
            company = ShiroUtils.getUserEntity().getSubjection();
        } else if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())) {
            if (StringUtils.isEmpty(company)) {
                company = ShiroUtils.getUserEntity().getSubjection();
            } else if ("所有公司".equals(company)) {
                company = null;
            }
        } else {
            company = IntellectConstants.BRANCH_PERMISSIONS;
        }
        return AjaxResult.success(statisticsService.selectCompanyFinancialStatisticsInfo(company, businessSource, startTime, endTime));
    }

    /**
     * 获取财务统计数据-业务员维度
     */
    @GetMapping("/salesmen/list")
    @AuthCode(url = "financialStatistics", system = "sys")
    public AjaxResult salesmenList(Integer salesmenId, String company, String startTime, String endTime, AuthCodeVo authCodeVo) {
        startTime = StringUtils.nvl(startTime, "2020-01-01");
        endTime = StringUtils.nvl(endTime, DateUtils.getDate());
        //权限职能
        if (IntellectConstants.BRANCH_PERMISSIONS.equals(authCodeVo.getAuthCode())) {
            company = ShiroUtils.getUserEntity().getSubjection();
        } else if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())) {
            if (StringUtils.isEmpty(company)) {
                company = ShiroUtils.getUserEntity().getSubjection();
            } else if ("所有公司".equals(company)) {
                company = null;
            }
        } else {
            company = IntellectConstants.BRANCH_PERMISSIONS;
        }
        return AjaxResult.success(statisticsService.selectSalesmenFinancialStatisticsInfo(salesmenId, company, startTime, endTime));
    }

    /**
     * 财务统计数据-清空缓存
     */
    @GetMapping("/clear/Caching")
    public AjaxResult clearCaching(String type) {
        statisticsService.clearCaching(type);
        return AjaxResult.success();
    }
}
