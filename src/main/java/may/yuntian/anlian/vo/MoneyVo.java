package may.yuntian.anlian.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lixin
 */
@Data
public class MoneyVo {
    private BigDecimal subprojectFee;
    private BigDecimal netvalue;
    private BigDecimal otherExpenses;
    private BigDecimal serviceChargeOutstanding;
    private BigDecimal invoiceMoney;
    private BigDecimal subprojectOutstanding;
    private BigDecimal evaluationoutstanding;
    private BigDecimal toltalMoney;
    private BigDecimal commissionOutstanding;
    private BigDecimal serviceCharge;
    private BigDecimal otherExpensesOutstanding;
    private BigDecimal evaluationFee;
    private BigDecimal virtualTax;
    private BigDecimal commission;
    private BigDecimal nosettlementMoney;
    private BigDecimal receiptMoney;

    //收付款
    private BigDecimal amount;
    private BigDecimal totalMoney;
    private BigDecimal invoiceAmount;

    //业务提成
    private BigDecimal businessAmount;
    //采样提成
    private BigDecimal samplingAmount;
    //报告提成
    private BigDecimal reportAmount;
    //检测提成
    private BigDecimal detectionAmount;

    //综合成本公摊
    private BigDecimal share;
    //预计利润
    private BigDecimal estimatedProfit;
}
