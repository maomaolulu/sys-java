package may.yuntian.external.express.sf.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SF快递：业务订单表
 * @author: liyongqiang
 * @create: 2023-05-25 11:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("exp_business_order")
@EqualsAndHashCode(callSuper = false)
public class BusinessOrder implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    @TableId
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("合同表id")
    private Long contractId;

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty("运单号")
    private String waybillNo;

    @ApiModelProperty("订运单状态（0：订单取消，1：已建单/未下单，2：待揽件/已下单，3：在途中，4：已完结）")
    private Integer status;

    @ApiModelProperty(value = "托寄物")
    private String name;

    @ApiModelProperty(value = "托寄物数量")
    private Integer count;

    @ApiModelProperty("物品单价")
    private BigDecimal amount;

    @ApiModelProperty("商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "物流付款方式（默认0：寄付月结，1寄付现结， 2:收方付 3:第三方付）")
    private Integer payMethod;

    @ApiModelProperty(value = "包裹数")
    private Integer parcelQty;

    @ApiModelProperty(value = "包裹总重量，单位kg")
    private String totalWeight;

    @ApiModelProperty(value = "物流产品：1顺丰特快，2顺丰标快（默认），3顺丰即日")
    private Integer expressTypeId;

    @ApiModelProperty(value = "物流公司：顺丰（默认），申通")
    private String logisticsCompany;

    @ApiModelProperty(value = "原始单号")
    private String custReferenceNo;

    @ApiModelProperty("收件方信息id")
    private Integer receiveInfoId;

    @ApiModelProperty("寄件方信息id")
    private Integer sendInfoId;

    @ApiModelProperty("订单类型：1内地互寄（默认）；2港澳台件；3寄国际")
    private Integer orderType;

    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;

    @ApiModelProperty("手工建单时间")
    private Date createTime;

    @ApiModelProperty("订单创建时间")
    private Date orderCreateTime;

    @ApiModelProperty("订单取消时间")
    private Date orderCancelTime;

    @ApiModelProperty("运单生成时间")
    private Date generateWaybillTime;

    @ApiModelProperty("订单揽收时间")
    private Date orderCollectionTime;

    @ApiModelProperty("订单签收时间")
    private Date orderSignTime;

    @ApiModelProperty(value = "更新者", hidden = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;

    @ApiModelProperty("订单备注")
    private String remark;

    public BusinessOrder(Long id, String waybillNo, Integer status, Date orderCreateTime ,Date generateWaybillTime, String updateBy, Date updateTime) {
        this.id = id;
        this.waybillNo = waybillNo;
        this.status = status;
        this.orderCreateTime = orderCreateTime;
        this.generateWaybillTime = generateWaybillTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }

}
