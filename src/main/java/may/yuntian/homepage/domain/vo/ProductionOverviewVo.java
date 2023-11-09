package may.yuntian.homepage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gy
 * @Description 项目签订日期,项目净值,项目状态
 * @date 2023-07-04 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOverviewVo {
    /** 年份 */
    private String year;
    /** 月份 */
    private String month;
    /** 日期 */
    private String day;
    /** 项目净值 */
    private BigDecimal netvalue;
}
