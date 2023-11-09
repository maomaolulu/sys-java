package may.yuntian.external.express.sf.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路由推送接口-速运类api：WaybillRoute
 * @author: liyongqiang
 * @create: 2023-06-08 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WaybillRoute implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 客户运单号 **/
    private String mailno;
    /** 收货地址 **/
    private String acceptAddress;
    /** 异常描述 **/
    private String reasonName;
    /** 客户订单号 **/
    private String orderid;
    /** 收货时间 **/
    private String acceptTime;
    /** 备注 **/
    private String remark;
    /** 操作码 **/
    private String opCode;
    /** ID **/
    private String id;
    /** 异常编码 **/
    private String reasonCode;

}
