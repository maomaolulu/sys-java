package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 报告开始节点类！（报告开始节点：<report></>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("report")
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 委托机构信息开始节点：<entrustOrgInfo></>
     */
    private EntrustOrgInfo entrustOrgInfo;
    /**
     * 受检单位信息开始节点：<empInfo></>
     */
    private EmpInfo empInfo;
    /**
     * 检测机构信息开始节点：<checkOrgInfo></>
     */
    private CheckOrgInfo checkOrgInfo;
    /**
     * 上报单位信息开始节点：<writeOrgInfo></>
     */
    private WriteOrgInfo writeOrgInfo;
    /**
     * 检测报告信息开始节点：<reportInfo></>
     */
    private ReportInfo reportInfo;
    /**
     * 检测点列表开始节点：<detectionPointList></>
     */
    private List<DetectionPoint> detectionPointList;

}
