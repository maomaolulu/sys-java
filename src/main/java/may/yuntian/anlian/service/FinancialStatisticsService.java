package may.yuntian.anlian.service;

import java.util.Map;

/**
 * 财务统计-接口层
 *
 * @author hjy
 * @date 2023/5/11 11:52
 */
public interface FinancialStatisticsService {

    /**
     * 获取财务统计数据-公司维度
     *
     * @param company   项目隶属公司
     * @param startTime 统计起始时间
     * @param endTime   统计结束时间
     * @return 财务统计信息
     */
    Map<String, Object> selectCompanyFinancialStatisticsInfo(String company, String businessSource, String startTime, String endTime);

    /**
     * 获取财务统计数据-业务员维度
     *
     * @param salesmenId 业务员id
     * @param company    项目隶属公司
     * @param startTime  统计起始时间
     * @param endTime    统计结束时间
     * @return 财务统计信息
     */
    Map<String, Object> selectSalesmenFinancialStatisticsInfo(Integer salesmenId, String company, String startTime, String endTime);

    /**
     * 清空缓存
     *
     * @param type 类型
     */
    void clearCaching(String type);
}
