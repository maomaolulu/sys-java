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
 * @description:   人员产出累计表实体类
 * @author: liyongqiang
 * @create: 2022-06-06 20:03
 */
@Data
@TableName("co_output_cumulative")
@AllArgsConstructor
@NoArgsConstructor
public class OutputCumulativeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 目标金额
     */
    private BigDecimal targetAmount;
    /**
     * 累计金额
     */
    private BigDecimal aggregateAmount;
    /**
     *数据入库时间
     */
    private Date createTime;
    /**
     *修改时间
     */
    private Date updateTime;

}
