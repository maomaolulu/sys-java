package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gy
 * @date 2023-08-25 14:00
 */
@Data
public class ThisYearReceiptVo implements Serializable {
    /**
     * 回款时间
     */
    private Date happenTime;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 业务来源
     */
    private String businessSource;
    /**
     * 项目隶属
     */
    private String companyOrder;
}
