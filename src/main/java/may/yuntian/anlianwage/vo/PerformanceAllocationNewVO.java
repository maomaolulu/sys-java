package may.yuntian.anlianwage.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统计相关图表
 *
 * @author cwt
 * @Create 2023-4-12 15:09:13
 */
@Data
public class PerformanceAllocationNewVO implements Serializable {

    private static final long serialVersionUID = 42L;


    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;

    /**
     * 绩效提成类型:签发提成
     */
    private String issueCommission;

    /**
     * 绩效提成类型:归档提成
     */
    private String fillingCommission;

}
