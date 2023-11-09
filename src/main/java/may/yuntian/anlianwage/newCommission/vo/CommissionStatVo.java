package may.yuntian.anlianwage.newCommission.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommissionStatVo {
    private BigDecimal accrualCount;
    private BigDecimal monthCount;
    private BigDecimal yearCount;
}
