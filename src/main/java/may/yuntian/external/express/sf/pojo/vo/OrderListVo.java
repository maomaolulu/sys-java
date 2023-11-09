package may.yuntian.external.express.sf.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.express.sf.pojo.entity.BusinessOrder;

import java.io.Serializable;

/**
 * 订单列表-子页面：vo
 * @author: liyongqiang
 * @create: 2023-05-29 13:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderListVo extends BusinessOrder implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 订单信息：订单状态 **/
    @ApiModelProperty("订单状态：1已确认，0已取消")
    private Integer orderStatus;
    /** 运单信息：运单状态 **/
    private Integer waybillStatus;
    /** 物流产品：月结卡号 **/
    @ApiModelProperty("月结卡号")
    private String monthlyCard;

    // 收件人信息
    /** 省、市、区 **/
    private String receiveProvince;
    private String receiveCity;
    private String receiveCounty;
    /** 收件人姓名 **/
    private String receiveContact;
    /** 收件人手机号 **/
    private String receiveMobile;
    /** 收件人电话 **/
    private String receiveTelephone;
    /** 收件人公司 **/
    private String receiveCompany;
    /** 收件人地址 **/
    private String receiveAddress;

    // 寄件人信息
    /** 省、市、区 **/
    private String sendProvince;
    private String sendCity;
    private String sendCounty;
    /** 寄件人姓名 **/
    private String sendContact;
    /** 寄件人手机号 **/
    private String sendMobile;
    /** 寄件人电话 **/
    private String sendTelephone;
    /** 寄件人公司 **/
    private String sendCompany;
    /** 寄件人地址 **/
    private String sendAddress;

    // 前端传值：年月日-时分秒
    /** 订单创建查询：开始时间 **/
    private String orderStartTime;
    /** 订单创建查询：结束时间 **/
    private String orderEndTime;

    /** 运单查询：开始时间 **/
    private String waybillStartTime;
    /** 运单查询：结束时间 **/
    private String waybillEndTime;

}
