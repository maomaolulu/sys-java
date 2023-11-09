package may.yuntian.external.oa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mi
 */
@Data
public class ProjectQueryVo implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 项目Id
     */
    private Long id;

    /**
     * 项目编号
     */
    private String identifier;

    /**
     * 受检单位
     */
    private String company;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 业务来源
     */
    private String businessSource;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目负责人
     */
    private String charge;

    /**
     * 负责人电话
     */
    private String chargePhone;

    /**
     * 负责人电话
     */
    private String salesmenPhone;

    /**
     * 项目签订时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    /**
     * 录入时间
     */
    private Date createTime;

    /**
     * 录入人
     */
    private String username;

    /**
     * 未结金额
     */
    private BigDecimal noSettlementMoney;

    /**
     * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private String status;

    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;

    /**
     * 项目净值(元)
     */
    private BigDecimal netValue;

    /**
     * 已开票金额(元)
     */
    private BigDecimal invoiceMoney;

    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;

    /**
     * 状态触发时间
     */
    private Date stateTriggerTime;

    /**
     * 合同签订状态(0未回，1已回)
     */
    private Integer contractStatus;

    /**
     * 协议签订状态(0未回，1已回)
     */
    private Integer dealStatus;

    /**
     * 合同签订状态1已回 时间
     */
    private Date contractStatusTime;

    /**
     * 协议签订状态1已回 时间
     */
    private Date dealStatusTime;

    /**
     *业务提成金额(元)
     */
    private BigDecimal businessAmount;

    /**
     *采样提成金额(元)
     */
    private BigDecimal samplingAmount;

    /**
     *报告提成金额(元)
     */
    private BigDecimal reportAmount;

    /**
     *检测提成金额(元)
     */
    private BigDecimal detectionAmount;



}
