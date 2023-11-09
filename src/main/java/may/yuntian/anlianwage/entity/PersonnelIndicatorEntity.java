package may.yuntian.anlianwage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ANLIAN-JAVA
 * @description:   评价-2021年人员业绩指标
 * @author: liyongqiang
 * @create: 2022-06-06 20:03
 */
@Data
@TableName("co_personnel_indicator")
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelIndicatorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 提成人：al_project_charge
     */
    private String userName;
    /**
     * 级别
     */
    private String level;
    /**
     * 业绩指标
     */
    private BigDecimal performanceIndicators;
    /**
     * 累计金额：项目净值的累计
     */
    private BigDecimal aggregateAmount;
    /**
     * 归档提成：21年特殊，保存在此。
     */
    private BigDecimal filingFees;

}
