package may.yuntian.homepage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务员指标实体类
 * @author: liyongqiang
 * @create: 2023-02-28 11:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("al_salesman_index")
public class SalesmanIndexEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 业务员姓名 **/
    private String salesmanName;
    /** 业务员id **/
    private Long salesmanId;
    /** 各月指标(1~12月份，之间以英文逗号分隔，单位万元) **/
    private String monthTarget;
    /** 当前年度总指标（单位：万元） **/
    private BigDecimal yearTarget;
    /** 回款额月指标(1~12月份，之间以英文逗号分隔，单位万元) **/
    private String returnMonthIndex;
    /** 回款额年度总指标（单位：万元） **/
    private BigDecimal returnYearIndex;
    /** 创建时间 **/
    private Date createTime;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    /** 备注 **/
    private String remark;

}
