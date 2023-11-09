package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 检测结果类！（检测结果开始节点：<result></result>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("result")
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 结果项编码
     */
    private Integer code;
    /**
     * 结果
     */
    private String result;
    /**
     * 计量单位
     */
    private Integer unit;

}
