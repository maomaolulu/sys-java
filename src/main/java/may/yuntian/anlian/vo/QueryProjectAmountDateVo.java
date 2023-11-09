package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import may.yuntian.anlian.entity.AccountEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目信息
 * 用于项目数据查询条件的参数
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-11-07
 */
@Data
public class QueryProjectAmountDateVo implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 合同ID
     */
    private Long contractId;
    /**
     * 合同编号
     */
    private String contractIdentifier;
    /**
     * 受检企业信息表ID
     */
    private Long companyId;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区县
     */
    private String area;
    /**
     * 受检详细地址
     */
    private String officeAddress;
    /**
     * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private Integer status;
    /**
     * 项目类型
     */
    private String type;
    /**
     * 所属部门ID
     */
    private Long deptId;
    /**
     * 负责人id
     */
    private Long chargeId;
    /**
     * 负责人
     */
    private String charge;
    /**
     * 负责人工号
     */
    private String chargeJobNum;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 委托类型
     */
    private String entrustType;
    /**
     * 委托单位，企业信息表ID
     */
    private Long entrustCompanyId;
    /**
     * 委托单位名称
     */
    private String entrustCompany;
    /**
     * 委托单位详细地址
     */
    private String entrustOfficeAddress;
    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    private String businessSource;
    /**
     * 业务员ID
     */
    private Long salesmenid;
    /**
     * 业务员
     */
    private String salesmen;
    /**
     * 加急状态(0正常，1较急、2加急)
     */
    private Integer urgent;
    /**
     * 新老业务(0新业务，1续签业务)
     */
    private Integer old;
    /**
     * 快递单号
     */
    private String expressnumber;
    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;
    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 录入人ID
     */
    private Long userid;
    /**
     * 录入人姓名
     */
    private String username;
    /**
     * 是否是有效时间（1全部，2送样时间，3物理时间）
     */
    private Integer isTime;
    /**
     * 是否计入产出 -- 检评绩效计算
     */
    private Integer includedOutput;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;

//    /**
//     * ID
//     */
//    @TableId
//    private Long id;
//    /**
//     * 项目ID
//     */
//    private Long projectId;
    /**
     * 委托日期
     */
    private Date entrustDate;
    /**
     * 签订日期
     */
    private Date signDate;
    /**
     * 要求报告完成日期
     */
    private Date claimEndDate;
    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;
    /**
     * 计划完成日期
     */
    private Date planFinishDate;
    /**
     * 计划调查日期
     */
    private Date planSurveyDate;
    /**
     * 计划采样开始日期
     */
    private Date planStartDate;
    /**
     * 计划采样结束日期
     */
    private Date planEndDate;
    /**
     * 实际调查日期
     */
    private Date surveyDate;
    /**
     * 实际采样开始日期
     */
    private Date startDate;
    /**
     * 实际采样结束日期
     */
    private Date endDate;
    /**
     * 采样方案制定日期
     */
    private Date makePlanDate;
    /**
     * 报告调查日期(报告上展示)
     */
    private Date reportSurveyDate;
    /**
     * 报告采样计划制定日期(报告上展示)
     */
    private Date reportLayoutDate;
    /**
     * 报告采样开始日期(报告上展示)
     */
    private Date reportStartDate;
    /**
     * 报告采样结束日期(报告上展示)
     */
    private Date reportEndDate;
    /**
     * 送样日期
     */
    private Date deliverDate;
    /**
     * 收样日期
     */
    private Date receivedDate;
    /**
     * 物理因素发送日期
     */
    private Date physicalSendDate;
    /**
     * 物理因素接收日期
     */
    private Date physicalAcceptDate;
    /**
     * 采样记录发送日期
     */
    private Date gatherSendDate;
    /**
     * 采样记录接收日期
     */
    private Date gatherAcceptDate;
    /**
     * 数据报告出具日期(报告上展示)
     */
    private Date labReportIssue;
    /**
     * 检测报告发送日期(实验室报告)
     */
    private Date labReportSend;
    /**
     * 检测报告接收日期
     */
    private Date labReportAccept;
    /**
     * 检测结果处理过程记录日期(报告上展示)
     */
    private Date labResultDeal;
    /**
     * 审核开始日期(内审)
     */
    private Date examineStart;
    /**
     * 技术审核日期
     */
    private Date technicalAudit;
    /**
     * 评审开始日期(专家)(报告上展示)
     */
    private Date reviewStart;
    /**
     * 出版前校核(开始)日期(报告上展示)
     */
    private Date checkStart;
    /**
     * 报告移交日期(项目负责人移交)
     */
    private Date reportTransfer;
    /**
     * (正式)报告接收日期
     */
    private Date reportAccept;
    /**
     * 报告签发日期
     */
    private Date reportIssue;
    /**
     * 报告装订日期
     */
    private Date reportBinding;
    /**
     * 报告寄送日期
     */
    private Date reportSend;
    /**
     * 报告归档日期
     */
    private Date reportFiling;
    /**
     * 收款日期日期
     */
    private Date receiveAmount;
    /**
     * 报告封面日期
     */
    private Date reportCoverDate;

//    /**
//     * 项目金额(元)
//     */
//    private BigDecimal totalMoney;
    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;
    /**
     * 未结算金额
     */
    private BigDecimal nosettlementMoney;
    /**
     * 佣金金额(元)
     */
    private BigDecimal commission;
    /**
     * 佣金比例,佣金/总金额
     */
    private BigDecimal commissionRatio;
    /**
     * 佣金未结算金额
     */
    private BigDecimal commissionOutstanding;
    /**
     * 评审费(元)
     */
    private BigDecimal evaluationFee;
    /**
     * 未结算评审费(元)
     */
    private BigDecimal evaluationOutstanding;
    /**
     * 分包费(元)
     */
    private BigDecimal subprojectFee;
    /**
     * 未结算分包费(元)
     */
    private BigDecimal subprojectOutstanding;
    /**
     * 服务费用(元)
     */
    private BigDecimal serviceCharge;
    /**
     * 未结算服务费用(元)
     */
    private BigDecimal serviceChargeOutstanding;
    /**
     * 其他支出(元)
     */
    private BigDecimal otherExpenses;
    /**
     * 未结算的其他支出(元)
     */
    private BigDecimal otherExpensesOutstanding;
    /**
     * 已开票金额(元)
     */
    private BigDecimal invoiceMoney;
//    /**
//     * 项目净值(元)
//     */
//    private BigDecimal netvalue;
    /**
     * 虚拟税费(元)
     */
    private BigDecimal virtualTax;

    /**
     * 项目收付款相关
     */
    @TableField(exist=false)
    private List<AccountEntity> accountEntityList;


}
