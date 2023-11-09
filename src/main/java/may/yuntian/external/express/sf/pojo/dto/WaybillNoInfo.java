package may.yuntian.external.express.sf.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 速运类api-下单：顺丰运单号
 * @author: liyongqiang
 * @create: 2023-05-22 19:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WaybillNoInfo implements Serializable {

    private static final long serialVersionUID = 2774275564551643616L;

    @ApiModelProperty("运单号类型1：母单 2 :子单 3 : 签回单")
    private Integer waybillType;

    @ApiModelProperty("运单号")
    private String waybillNo;

    @ApiModelProperty("箱号")
    private String boxNo;

    @ApiModelProperty("长(cm)")
    private double length;

    @ApiModelProperty("宽(cm)")
    private double width;

    @ApiModelProperty("高(cm)")
    private double height;

    @ApiModelProperty("重量(kg)")
    private double weight;

    @ApiModelProperty("体积（立方厘米）")
    private double volume;

}
