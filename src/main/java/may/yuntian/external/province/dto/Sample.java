package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 样品开始节点：<sample></sample>
 * @author: liyongqiang
 * @create: 2023-04-04 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("sample")
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 样品编号
     */
    private String sampleNo;
    /**
     * 样品性状
     */
    private String sampleProperties;
    /**
     * 采样日期
     */
    private String samplingDate;
    /**
     * 样品类别
     */
    private String sampleCategory;
    /**
     * 采样方式
     */
    private String samplingMethod;
    /**
     * 结果项编码
     */
    private String code;
    /**
     * 结果
     */
    private String result;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 接触限值
     */
    private String limit;
    /**
     * 单项结论
     */
    private String isExceed;
}
