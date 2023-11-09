package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-08-22 13:29
 */
@Data
public class CustomAdvanceVo implements Serializable {

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 业务编号
     */
    private String taskCode;

    /**
     * 客户隶属
     */
    private String customerOrder;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;

    /**
     * 跟进人
     */
    private String advanceName;

    /**
     * 首次跟进
     */
    private Date advanceFirstTime;

    /**
     * 最近跟进
     */
    private Date advanceLastTime;

    /**
     * 业务状态
     */
    private Integer businessStatus;

    /**
     * 跟进结果：0未成单，1已成单
     */
    private Integer advanceResult;

    /**
     * 注册地址
     */
    private String registeredAddress;

    /**
     * 联系人姓名
     */
    private String contacterName;

    /**
     * 移动电话
     */
    private Integer mobilePhone;

}
