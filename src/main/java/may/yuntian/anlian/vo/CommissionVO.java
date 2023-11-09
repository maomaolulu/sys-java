package may.yuntian.anlian.vo;

import lombok.Data;


import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cwt
 * @Create 2023-4-12 11:46:17
 */
@Data
public class CommissionVO implements Serializable {

    private static final long serialVersionUID = 42L;


    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 提成类型
     */
    private String type;

    /**
     * 隶属公司
     */
    private String subjection;

    /**
     * 提成金额(元)
     */
    private BigDecimal cmsAmount;

    /**
     * 提成人(可能是多个)
     */
    private String personnel;

    /**
     * 提成日期
     */
    private Date commissionDate;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 项目编号
     */
    private String identifier;

    /**
     * 受检企业名称
     */
    private String company;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 项目状态(1项目录入，2任务分配，4.现场调查 5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private String status;

    /**
     * 业务员
     */
    private String salesmen;

    /**
     * 负责人
     */
    private String charge;

    /**
     * (正式)报告接收日期
     */
    private Date planRptissuDate;

    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;

    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;

    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;

    /**
     * 未结算金额
     */
    private BigDecimal nosettlementMoney;

    /**
     * 佣金金额(元)(业务费)
     */
    private BigDecimal commission;

    /**
     * 评审费(元)
     */
    private BigDecimal evaluationFee;

    /**
     * 项目隶属公司
     */
    private String companyOrder;

    /**
     * 服务费用(元)
     */
    private BigDecimal serviceCharge;

    /**
     * 分包费(元)
     */
    private BigDecimal subprojectFee;

    /**
     * 其他支出(元)
     */
    private BigDecimal otherExpenses;

    /**
     * 签订日期
     */
    private Date signDate;

    /**
     * 报告装订日期
     */
    private Date rptBindingDate;

    /**
     * 委托类型
     */
    private String entrustType;

    /**
     * 报告归档日期
     */
    private Date filingDate;

    /**
     * 收款日期日期
     */
    private Date receiptDate;

    /**
     * 备注
     */
    private String remarks;


}
