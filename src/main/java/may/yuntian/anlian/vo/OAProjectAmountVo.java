package may.yuntian.anlian.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import may.yuntian.anlian.utils.OMSBigDecimalFormat;

import java.math.BigDecimal;

@Data
public class OAProjectAmountVo {

    private String identifier;

    private String companyOrder;

    private String projectName;
    private String salesmen;
    private String type;
    private String businessSource;

    /**
     * 佣金金额(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal commission;
    /**
     * 评审费(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal subprojectFee;
    /**
     * 服务费用(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal otherExpenses;
    /**
     * 虚拟税费(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal virtualTax;
    /**
     * 项目金额(元)
     */
    @OMSBigDecimalFormat
    private BigDecimal totalMoney;
}
