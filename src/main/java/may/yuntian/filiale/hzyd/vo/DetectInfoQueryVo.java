package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 亿达设备查询vo
 *
 * @author: lixin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectInfoQueryVo {

    /**
     * 类目（1：类一，2：类二，3：类三，4：类四）
     */
    private Integer category;
    /**
     * 类型（1放射治疗、2核医学、3介入放射学、4X射线影像诊断、5放射工作场所、6环境检测项目）
     */
    private Integer type;
    /**
     * (设备或房间)名称
     */
    private String name;
}
