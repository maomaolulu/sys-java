package may.yuntian.external.oa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-08-22 11:39
 */
@Data
public class CustomAdvanceDto implements Serializable {
    /**
     * 用户id(用于查询我的客户数据)
     */
    private Long userId;

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
     * 联系人姓名
     */
    private String contacterName;

    /**
     * 移动电话
     */
    private Integer mobilePhone;

    /**
     * 业务状态
     */
    private Integer businessStatus;

    /**
     * 跟进结果：0未成单，1已成单
     */
    private Integer advanceResult;

    /**
     * 首次跟进查询起始时间
     */
    private Date advanceFirstTimeStart;

    /**
     * 首次跟进查询结束时间
     */
    private Date advanceFirstTimeEnd;

    /**
     * 最近跟进查询起始时间
     */
    private Date advanceLastTimeStart;

    /**
     * 最近跟进查询结束时间
     */
    private Date advanceLastTimeEnd;
}
