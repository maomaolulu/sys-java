package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务事项类（服务事项开始节点：<serviceItem></>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("serviceItem")
public class ServiceItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务事项编码 N..2
     */
    private Integer itemCode;
    /**
     * 服务事项名称
     */
    private String itemName;

}
