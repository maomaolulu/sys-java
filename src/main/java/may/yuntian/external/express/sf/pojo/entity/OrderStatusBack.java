package may.yuntian.external.express.sf.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态推送信息表
 * @author: liyongqiang
 * @create: 2023-05-22 19:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("exp_order_status_back")
@EqualsAndHashCode(callSuper = false)
public class OrderStatusBack implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    @TableId
    private Integer id;

    @ApiModelProperty("客户订单号")
    private Integer orderNo;

    @ApiModelProperty("顺丰运单号")
    private String waybillNo;

    @ApiModelProperty("订单状态")
    private String orderStateCode;

    @ApiModelProperty("订单状态描述")
    private String orderStateDesc;

    @ApiModelProperty("收件员工工号")
    private String empCode;

    @ApiModelProperty("收件员工手机号")
    private String empPhone;

    @ApiModelProperty("网点")
    private String netCode;

    @ApiModelProperty("最晚上门时间")
    private Date lastTime;

    @ApiModelProperty("客户预约时间")
    private Date bookTime;

    @ApiModelProperty("承运商代码(SF)")
    private String carrierCode;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
