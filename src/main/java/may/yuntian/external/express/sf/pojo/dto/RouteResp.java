package may.yuntian.external.express.sf.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 路由查询响应体msgData
 * @author: liyongqiang
 * @create: 2023-06-05 17:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RouteResp implements Serializable {

    private static final long serialVersionUID = 2774275564551643616L;

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty("顺丰运单号")
    private String mailNo;

    @ApiModelProperty("全部路由列表信息")
    private List<Route> routes;

}
