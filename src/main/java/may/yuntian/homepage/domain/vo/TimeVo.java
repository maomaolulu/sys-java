package may.yuntian.homepage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gy
 * @Description 质控报告审核通过日期
 * @date 2023-07-04 17:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeVo {
    /** 审核通过年份 */
    private String year;
    /** 审核通过月份 */
    private String month;
    /** 审核通过日期 */
    private String day;
}
