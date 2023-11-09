package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 项目vo：基本信息 + 检测信息
 *
 * @author: liyongqiang
 * @create: 2023-08-14 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目类型
     */
    private String itemType;
    /**
     * 项目编号
     */
    private String itemCode;
    /**
     * 受检单位
     */
    private String empName;
    /**
     * 受检地址
     */
    private String empAddress;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 要求完成日期
     */
    private String requireFinishDate;
    /**
     * (设备或房间)总数量
     */
    private String total;

    /**
     * 检测信息map：分三类
     */
    private Map<String, List<DetectInfoVo>> detectInfoMap;

}
