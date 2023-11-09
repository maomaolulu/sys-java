package may.yuntian.filiale.hzyd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.filiale.hzyd.vo.DetectInfoVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目dto
 *
 * @author: liyongqiang
 * @create: 2023-08-11 14:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 委托单位
     */
    private String entrustUnit;
    /**
     * 委托单位id
     */
    private Long entrustUnitId;
    /**
     * 委托地址
     */
    private String entrustAddress;
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
     * 项目负责人
     */
    private String projectLeader;
    /**
     * 受检单位
     */
    private String empName;
    /**
     * 受检单位id
     */
    private Long empNameId;
    /**
     * 受检地址
     */
    private String empAddress;
    /**
     * 备注
     */
    private String remark;
    /**
     * 要求完成日期
     */
    private String requireFinishDate;
    /**
     * 项目下发日期
     */
    private String issueDate;
    /**
     * 查询：下发结束日期
     */
    private String issueEndDate;
    /**
     * 项目完成日期
     */
    private String projectFinishDate;
    /**
     * 查询：完成结束日期
     */
    private String projectFinishEndDate;
    /**
     * 项目状态：1待下发，2进行中，70项目完成，99项目中止
     */
    private Integer projectStatus;
    /**
     * 项目金额
     */
    private String itemAmount;
    /**
     * 业务费
     */
    private String businessCost;
    /**
     * 服务费
     */
    private String serviceCost;
    /**
     * 评审费
     */
    private String reviewCost;
    /**
     * 分包费
     */
    private String subcontractCost;
    /**
     * 其它支出
     */
    private String otherCost;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * ydr_data_record.id
     */
    private Long dataRecordId;
    /**
     * 检测信息表ids
     */
    private String detectInfoIds;
    /**
     * 设备或房间数量
     */
    private String quantities;
    /**
     * (设备或房间)总数量
     */
    private String total;
    /**
     * 检测类别
     */
    private Integer detectType;
    /**
     * 检测项目
     */
    private Integer testItem;
    /**
     * 检测开始日期
     */
    private Date testStartDate;
    /**
     * 检测结束日期
     */
    private Date testEndDate;
    /**
     * 检测人数
     */
    private Integer testNumber;



    /**
     * 检测信息map：分三类
     */
    private Map<String, List<DetectInfoVo>> detectInfoMap;

}
