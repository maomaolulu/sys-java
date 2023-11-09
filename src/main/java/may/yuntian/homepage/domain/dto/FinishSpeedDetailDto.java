package may.yuntian.homepage.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author gy
 * @Description 项目完成时效详情查询参数
 * @date 2023-07-06 15:29
 */
@Data
public class FinishSpeedDetailDto {
    /** 公司隶属 */
    private String companyOrder;
    /** 项目类型 */
    private String type;
    /** 项目编号 */
    private String identifier;
    /** 项目名称 */
    private String projectName;
    /** 查询开始时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    /** 查询结束时间 */
    private Date endTime;
}
