package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 财务信息vo
 *
 * @author: liyongqiang
 * @create: 2023-08-14 11:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同or项目标识排序
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 金额
     */
    private String amount;
    /**
     * 净值
     */
    private String netValue;
    /**
     * 业务费
     */
    private String businessCost;
    /**
     * 服务费
     */
    private String serviceCost;
    /**
     * 评审费
     */
    private String reviewCost;
    /**
     * 分包费
     */
    private String subcontractCost;
    /**
     * 其它支出
     */
    private String otherCost;

}
