package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-09-06 11:43
 */
@Data
public class CompanyVo implements Serializable {
    /**
     * 客户id
     */
    private Long id;
    /**
     * 跟进任务id
     */
    private Long taskId;
    /**
     * 客户隶属
     */
    private String dataBelong;
    /**
     * 企业名称
     */
    private String company;
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
    private String area;
    /**
     * 最近合作
     */
    private Date contractLast;
    /**
     * 业务状态
     */
    private Integer businessStatus;
    /**
     * 隶属人员
     */
    private String personBelong;
    /**
     * 客户状态(0:停用,1:正常)
     */
    private String status;
    /**
     * 是否有完成任务(0:停用,1:正常)
     */
    private Integer ifHasFinished;
}
