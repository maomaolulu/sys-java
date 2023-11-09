package may.yuntian.anlian.mapper;

import may.yuntian.anlian.entity.ProjectStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 财务统计-数据处理层
 *
 * @author hjy
 * @date 2023/5/11 11:53
 */
@Mapper
@Repository
public interface FinancialStatisticsMapper {
    /**
     * 获取项目财务统计信息
     *
     * @param salesmenId     业务员
     * @param companyOrder   项目隶属公司
     * @param businessSource 业务隶属公司
     * @param startTime      统计起始时间
     * @param endTime        统计结束时间
     * @return 信息
     */
    List<ProjectStatistics> selectProjectInfo(@Param("salesmenId") Integer salesmenId,
                                              @Param("companyOrder") String companyOrder,
                                              @Param("businessSource") String businessSource,
                                              @Param("startTime") String startTime, @Param("endTime") String endTime);
}
