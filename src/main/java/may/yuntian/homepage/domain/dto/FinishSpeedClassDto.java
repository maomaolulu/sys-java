package may.yuntian.homepage.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author gy
 * @Description 各环节完成时效入参
 * @date 2023-07-06 17:01
 */
@Data
public class FinishSpeedClassDto {
    /** 公司隶属 */
    private String companyOrder;
    /** 环节名称 */
    private String linkName;
    /** 查询时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sTime;
    /** 时间等级(年/月/周) */
    private String timeLevel;
}
