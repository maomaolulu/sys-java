package may.yuntian.homepage.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @Description 生产概览统计图入参
 * @date 2023-07-04 13:45
 */
@Data
public class ProductionOverviewDto implements Serializable {
    /** 项目隶属 */
    private String companyOrder;
    /** 项目类型 */
    private String type;
    /** 查询开始时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime;
    /** 查询结束时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;
    /** 时间等级(年/月/日) */
    private String timeLevel;
}
