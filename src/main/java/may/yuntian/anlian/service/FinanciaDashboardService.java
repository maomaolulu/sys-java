package may.yuntian.anlian.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author gy
 * @Description 财务数据看板
 * @date 2023-07-12 13:43
 */
public interface FinanciaDashboardService {
    /**
     * 查询近两年的项目金额数据
     *
     * @param companyOrder 公司隶属
     */
    Map<String, Map<String, Object>> getTotalMoney(String companyOrder);

    /**
     * 运营数据对比导出
     */
    void exportExcel(Map<String, Map<String, Object>> map, HttpServletResponse response, String companyOrder);

    /**
     * 查询本年、本月、本周、本日项目回款情况
     */
    Map<String, Object> getThisYearReceipt(String companyOrder);
}
