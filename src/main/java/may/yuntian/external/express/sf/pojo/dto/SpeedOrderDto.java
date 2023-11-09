package may.yuntian.external.express.sf.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 速运类api-下单dto
 * @author: liyongqiang
 * @create: 2023-05-22 17:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpeedOrderDto implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    @ApiModelProperty(value = "语言：zh-CN（中文）")
    private String language;

    @ApiModelProperty(value = "订单编号")
    private String orderId;

    @ApiModelProperty("顺丰运单号")
    private List<WaybillNoInfo> waybillNoInfoList;

    @ApiModelProperty(value = "托寄物信息")
    private List<CargoDetail> cargoDetails;

    @ApiModelProperty(value = "收寄双方信息")
    private List<ContactInfo> contactInfoList;

    /** 沙箱联调可使用测试月结卡号7551234567（非正式，无须绑定，仅支持联调使用） **/
    @ApiModelProperty("顺丰月结卡号 月结支付时传值，现结不需传值")
    private String monthlyCard;

    @ApiModelProperty("付款方式，支持以下值： 1:寄方付 2:收方付 3:第三方付")
    private Integer payMethod;

    /** 支持附录 《快件产品类别表》 的产品编码值，仅可使用与顺丰销售约定的快件产品 **/
    @ApiModelProperty(value = "快件产品类别")
    private Integer expressTypeId;

    @ApiModelProperty("要求上门取件开始时间， 格式： YYYY-MM-DD HH24:MM:SS， 示例： 2012-7-30 09:30:00")
    private String sendStartTm;

    @ApiModelProperty("是否通过手持终端 通知顺丰收派 员上门收件，支持以下值： 1：要求 0：不要求")
    private int isDocall;

    /** 一个包裹对应一个运单号；若包裹数大于1，则返回一个母运单号和N-1个子运单号 **/
    @ApiModelProperty(value = "包裹数")
    private Integer parcelQty;

    @ApiModelProperty("是否返回路由标签：默认1，1：返回路由标签，0：不返回")
    private Integer isReturnRoutelabel;

    @ApiModelProperty("订单备注")
    private String remark;

}
