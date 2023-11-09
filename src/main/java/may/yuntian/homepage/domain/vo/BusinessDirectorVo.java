package may.yuntian.homepage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDirectorVo {

    private Long id;//项目ID/合同ID

    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;

    private String salesmen;

    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;

    private Integer month;//月份

    private Integer year;//年份

    private String type;//项目/合同类型

    private Long quantity;

    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    private String businessSource;

}
