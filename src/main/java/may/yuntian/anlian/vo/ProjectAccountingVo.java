package may.yuntian.anlian.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectAccountingVo {
    //金额相关
    private BigDecimal toltalMoney;
    private BigDecimal netvalue;
    private BigDecimal commission;
    private BigDecimal subprojectFee;
    private BigDecimal serviceCharge;
    private BigDecimal evaluationFee;
    private BigDecimal otherExpenses;
    private BigDecimal yewu;
    private BigDecimal kefu;
    private BigDecimal caiyang;
    private BigDecimal qianfa;
    private BigDecimal baogao;
    private BigDecimal jiance;
    private BigDecimal bianzhi;
    private BigDecimal share;//综合成本公摊
    private BigDecimal estimatedProfit;//预计利润
}
