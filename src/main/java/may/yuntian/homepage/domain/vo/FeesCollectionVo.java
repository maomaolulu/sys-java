package may.yuntian.homepage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 业务员：2.业务费等回款信息Vo
 * @author: liyongqiang
 * @create: 2023-02-24 15:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeesCollectionVo {
    /** (总)应付业务费 **/
    private BigDecimal dealWithFees;
    /** (总)佣金未结算金额 **/
    private BigDecimal unsettledCommission;
    /** (总)合同项目额 or 项目金额 **/
    private BigDecimal contractAmount;
    /** (总)回款额 **/
    private BigDecimal payInReturn;
}
