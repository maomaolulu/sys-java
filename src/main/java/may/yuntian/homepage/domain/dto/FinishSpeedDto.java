package may.yuntian.homepage.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @Description
 * @date 2023-07-05 10:11
 */
@Data
public class FinishSpeedDto implements Serializable {
    /** 项目隶属 */
    private String companyOrder;
    /** 查询时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sTime;
    /** 时间等级(年/月/周) */
    private String timeLevel;
}
