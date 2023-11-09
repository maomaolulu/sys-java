package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目表(包含了原任务表的字段)
 * 
 * @author LiXin
 * @email
 * @date 2022-01-10 14:43:43
 */
@Data
@TableName("al_project")
public class ProjectEntity implements Serializable {
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
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
	/**
	 *0：未申请申报，1：已申请申报
	 */
	private Integer sbStatus;
	/**
	 *0：0：未申请，1：负责人申请到部门主管，2：主管申请到质控，3：主管驳回，4：质控驳回，5：公示
	 */
	private Integer applyPublicityStatus;
	/**
	 * 主管驳回
	 * @return
	 */
	private String directorReject;
	/**
	 * 质控驳回
	 * @return
	 */
	private String controlReject;
	/**
	 *0：项目公示最后操作时间
	 */
	private Date publicityLastTime;

	/**
	 * 公示状态 （1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.待公示，7.已公示,8.公示失败）
	 */
    private Integer pubStatus;

    private Integer bindingStatus;

    private Integer badDebt;//是否坏账（1 正常 ， 2 坏账）




//	/**
//	 *0：未公示，1：已公示
//	 */
//	private Integer publicityStatus;
    /**
     * 佣金金额(元)
     */
    @TableField(exist=false)
    private BigDecimal commission;
    /**
     * 佣金比例,佣金/总金额
     */
    @TableField(exist=false)
    private BigDecimal commissionRatio;
    /**
     * 评审费(元)
     */
    @TableField(exist=false)
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @TableField(exist=false)
    private BigDecimal subprojectFee;
    /**
     * 服务费用(元)
     */
    @TableField(exist=false)
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @TableField(exist=false)
    private BigDecimal otherExpenses;

    /**
     * 委托日期
     */
    @TableField(exist=false)
    private Date entrustDate;
    /**
     * 签订日期
     */
    @TableField(exist=false)
    private Date signDate;
    /**
     * 要求报告完成日期
     */
    @TableField(exist=false)
    private Date claimEndDate;

	/**
	 * 检测类别
	 */
	@TableField(exist=false)
	private String testNature;

    @TableField(exist=false)
	private ProjecarchiveEntity projecarchiveEntity;

    @TableField(exist=false)
    private CompanySurveyEntity companySurveyEntity;

    /**
     * 项目日期相关
     */
    @TableField(exist=false)
    private ProjectDateEntity projectDateEntity;
    /**
     * 项目金额相关
     */
    @TableField(exist=false)
    private ProjectAmountEntity projectAmountEntity;
    /**
     * 项目收付款相关
     */
    @TableField(exist=false)
    private List<AccountEntity> accountEntityList;

    /**
     * 是否计入产出 -- 检评绩效计算
     */
    private Integer includedOutput;

    /**
     * 项目报告相关信息录入表ID
     */
    @TableField(exist=false)
    private Long projectCountId;

    private Integer protocol;//有无委托协议（0.无，1.有）

    private Integer qualityPublicity;
    //业务提成
    @TableField(exist = false)
    private BigDecimal businessAmount;
    //采样提成
    @TableField(exist = false)
    private BigDecimal samplingAmount;
    //报告提成
    @TableField(exist = false)
    private BigDecimal reportAmount;
    //检测提成
    @TableField(exist = false)
    private BigDecimal detectionAmount;
    //综合成本公摊
    @TableField(exist = false)
    private BigDecimal share;
    //预计利润
    @TableField(exist = false)
    private BigDecimal estimatedProfit;
}
