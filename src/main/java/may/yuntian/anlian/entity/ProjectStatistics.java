package may.yuntian.anlian.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目统计信息
 *
 * @author hjy
 * @date 2023/5/11 13:26
 */
@Data
public class ProjectStatistics {
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 业务员id
     */
    private Integer salesmenId;
    /**
     * 业务员姓名
     */
    private String salesmen;
    /**
     * 业务员隶属公司
     */
    private String salesmenCompanyOrder;
    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 业务隶属公司
     */
    private String businessSource;
    /**
     * 合同签订日期
     */
    private Date signDate;
    /**
     * 项目金额
     */
    private BigDecimal totalMoney;
    /**
     * 已收款金额
     */
    private BigDecimal receiptMoney;
    /**
     * 未结算金额（应崔收款）
     */
    private BigDecimal unsettledAmount;
    /**
     * 已开票金额
     */
    private BigDecimal invoiceMoney;
    /**
     * 应开票金额
     */
    private BigDecimal shouldInvoiceMoney;
    /**
     * 项目收付款相关
     */
    private List<AccountEntity> accountList;

}
