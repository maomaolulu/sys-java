package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.external.province.annotation.XStreamYMDDateConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 检测点类！
 * @author: liyongqiang
 * @create: 2023-04-04 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("detectionPoint")
public class DetectionPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 检测项目编码
     */
    private String checkItemCode;
    /**
     * 车间
     */
    private String workArea;
    /**
     * 岗位
     */
    private String detectionArea;
    /**
     * 日接触时间 N..4,2
     */
    private Float dailyContactTime;
    /**
     * 周工作天数 N..2,2
     */
    private Float weekWorkDay;
    /**
     * 检测点位
     */
    private String pointName;
    /**
     * 单项结论 N1 1-符合，0-不符合
     */
    private Integer conclusion;
    /**
     * 检测日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date checkDate;
    /**
     *  检测结果列表开始节点：<resultList></resultList>
     */
    private List<Result> resultList;

    /**
     *  样品列表开始节点：<sampleList></sampleList>  ToDo: 样品列表暂搁置！！！
     */
   // private List<Sample> sampleList;


}
