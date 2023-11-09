package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 技术服务地区类！（技术服务地区开始节点：<serviceArea>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("serviceArea")
public class ServiceArea implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 技术服务地区编码 N9
     */
    private Integer serviceAreaCode;
    /**
     * 技术服务地址
     */
    private String serviceAddress;

}
