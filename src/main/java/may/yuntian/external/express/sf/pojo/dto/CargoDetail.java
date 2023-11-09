package may.yuntian.external.express.sf.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 速运类api-下单：托寄物信息
 * @author: liyongqiang
 * @create: 2023-05-22 18:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CargoDetail implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    @ApiModelProperty(value = "托寄物名称")
    private String name;

    @ApiModelProperty(value = "托寄物数量")
    private Integer count;

    @ApiModelProperty("物品单价")
    private BigDecimal amount;

    @ApiModelProperty("商品编码")
    private String goodsCode;

}
