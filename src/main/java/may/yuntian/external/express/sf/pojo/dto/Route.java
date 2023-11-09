package may.yuntian.external.express.sf.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路由信息
 * @author: liyongqiang
 * @create: 2023-06-05 17:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Route implements Serializable {

    private static final long serialVersionUID = 2774275564551643616L;

    @ApiModelProperty("路由节点发生的时间，格式：YYYY-MM-DD HH24:MM:SS，示例：2012-7-30 09:30:00")
    private String acceptTime;

    @ApiModelProperty("路由节点发生的地点")
    private String acceptAddress;

    @ApiModelProperty("路由节点具体描述")
    private String remark;

    @ApiModelProperty("路由节点操作码")
    private String opCode;

}
