package may.yuntian.anlian.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author gy
 * @Description 项目金额vo
 * @date 2023-07-12 14:56
 */
@Data
public class TotalMoneyVo {
    /** 签订日期 */
    private String signDate;
    /** 总计金额 */
    private Double totalMoney;
}
