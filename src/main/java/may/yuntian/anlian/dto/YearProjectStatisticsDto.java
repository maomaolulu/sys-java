package may.yuntian.anlian.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目年统计
 *
 * @author hjy
 * @date 2023/5/11 15:27
 */
@Data
@Builder
public class YearProjectStatisticsDto {
    /**
     * 项目总额
     */
    @Builder.Default
    private BigDecimal totalAmountProject = BigDecimal.ZERO;
    /**
     * 已开票总额
     */
    @Builder.Default
    private BigDecimal totalInvoiceAmount = BigDecimal.ZERO;
    /**
     * 已回款总额
     */
    @Builder.Default
    private BigDecimal totalAmountRefunded = BigDecimal.ZERO;
    /**
     * 应催收款总额
     */
    @Builder.Default
    private BigDecimal totalAmountAccountsReceivable = BigDecimal.ZERO;
    /**
     * 未开票总额
     */
    @Builder.Default
    private BigDecimal totalAmountUnissuedInvoice = BigDecimal.ZERO;
    /**
     * 已开票未回款（应收账款）
     */
    @Builder.Default
    private BigDecimal totalAccountsReceivable = BigDecimal.ZERO;
    /**
     * 应开票总额
     */
    @Builder.Default
    private BigDecimal totalShouldInvoiceMoney = BigDecimal.ZERO;
    /**
     * 项目回款率
     */
    @Builder.Default
    private String paybackRate = "0%";
    /**
     * 应收账款回款率
     */
    @Builder.Default
    private String accountsReceivablePaybackRate = "0%";
    /**
     * 当年开票金额
     */
    @Builder.Default
    private BigDecimal totalAmountInvoiceYear = BigDecimal.ZERO;
    /**
     * 当年回款金额
     */
    @Builder.Default
    private BigDecimal totalAmountMoneyRefundedYear = BigDecimal.ZERO;

    //****************** 账龄分析 **************************
    /**
     * 账龄(a)1       a < 1 month(30)
     */
    @Builder.Default
    private BigDecimal accountAge1 = BigDecimal.ZERO;
    /**
     * 账龄(a)2      1 month <= a <= 3 month(90)
     */
    @Builder.Default
    private BigDecimal accountAge2 = BigDecimal.ZERO;
    /**
     * 账龄(a)3       3 month < a <= 1 year(365)
     */
    @Builder.Default
    private BigDecimal accountAge3 = BigDecimal.ZERO;
    /**
     * 账龄(a)4       1 year < a <= 3 year(1095)
     */
    @Builder.Default
    private BigDecimal accountAge4 = BigDecimal.ZERO;
    /**
     * 账龄(a)5       a > 3 year(1095)
     */
    @Builder.Default
    private BigDecimal accountAge5 = BigDecimal.ZERO;
}
