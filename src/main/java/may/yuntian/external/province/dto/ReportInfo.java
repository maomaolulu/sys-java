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
 * 检测报告信息类！（检测报告开始节点：<reportInfo>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("reportInfo")
public class ReportInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 检测报告编号
     */
    private String code;
    /** N2
     * 检测类别编码：10 定期检测，20 监督检测，30 评价检测，31 现状评价，32 控制效果评价，33 预评价。
     */
    private Integer checkType;
    /**
     * 报告出具日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date reportDate;
    /**
     * 填表人
     */
    private String preparer;
    /**
     * 填表人联系电话
     */
    private String preparerPhone;
    /**
     * 报告签发人
     */
    private String issuer;
    /**
     * 服务的用人单位现场调查开始日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date beginSurveyDate;
    /**
     * 服务的用人单位现场调查结束日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date endSurveyDate;
    /**
     * 服务的用人单位现场采样、测量开始日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date beginSamplingDate;
    /**
     * 服务的用人单位现场采样、测量结束日期
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date endSamplingDate;

    /**
     *  参与人员列表开始节点：<participantList>
     */
    private List<Participant> participantList;

}
