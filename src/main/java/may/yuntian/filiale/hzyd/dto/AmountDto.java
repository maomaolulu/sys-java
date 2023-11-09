package may.yuntian.filiale.hzyd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合同、项目金额dto
 *
 * @author: liyongqiang
 * @create: 2023-08-17 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 合同金额
     */
    private BigDecimal contractAmount;
    /**
     * 合同业务费
     */
    private BigDecimal contractBusinessCost;
    /**
     * 合同评审费
     */
    private BigDecimal contractReviewCost;
    /**
     * 合同分包费
     */
    private BigDecimal contractSubcontractCost;
    /**
     * 合同服务费
     */
    private BigDecimal contractServiceCost;
    /**
     * 合同其它非费用
     */
    private BigDecimal contractOtherCost;
    /**
     * 合同净值
     */
    private BigDecimal contractNetValue;

    /**
     * 项目金额
     */
    private BigDecimal itemAmount;
    /**
     * 项目业务费
     */
    private BigDecimal itemBusinessCost;
    /**
     * 项目评审费
     */
    private BigDecimal itemReviewCost;
    /**
     * 项目分包费
     */
    private BigDecimal itemSubcontractCost;
    /**
     * 项目服务费
     */
    private BigDecimal itemServiceCost;
    /**
     * 项目其它费用
     */
    private BigDecimal itemOtherCost;
    /**
     * 项目净值
     */
    private BigDecimal itemNetValue;

}
