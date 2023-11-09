package may.yuntian.anlian.dto;

import lombok.Data;
import may.yuntian.modules.sys_v2.annotation.Excel;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同数据实体
 *
 * @author hjy
 * @date 2023/6/15 8:22
 */
@Data
public class ContractEntityDTO {
    /**
     * 项目类型
     */
    @Excel(name = "项目类型", headerBackgroundColor = IndexedColors.BLUE_GREY, headerColor = IndexedColors.RED, prompt = "注意：合同类型会根据项目类型自动匹配！",
            combo = {"检评",
                    "预评", "专篇", "控评", "现状",
                    "环境监测", "环境验收", "排污许可", "环评监测", "清洁生产审核", "场地调查", "环评",
                    "放射检测",
                    "公共卫生检测", "公共卫生学评价", "一次性用品用具检测", "洁净区域检测", "学校卫生检测",
                    "职卫监督", "公卫监督", "环境监督",
                    "来样检测",
                    "应急预案", "风险评估", "职卫示范", "公卫示范", "环境示范", "其他示范", "环保管家",
                    "放射预评价", "放射控制效果评价", "放射设计专篇", "放射现状评价"})
    private String projectType;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", align = HorizontalAlignment.LEFT, headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private String identifier;
    /**
     * 合同回款方式
     */
    @Excel(name = "合同回款方式", combo = {"无预付款按项目回款", "无预付款按合同回款", "有预付款按项目回款", "有预付款按合同回款"}, defaultValue = "无预付款按项目回款")
    private String paymentMethod;
    /**
     * 预付款比例
     */
    @Excel(name = "预付款比例")
    private Double prepaymentRatio;
    /**
     * 合同类型
     */
    private String type;
    /**
     * 合同签订日期
     */
    @Excel(name = "合同签订日期", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private Date signDate;
    /**
     * 合同签订状态(0 未回，1 已回 )
     */
    @Excel(name = "合同签订状态", readConverterExp = "0=未回,1=已回", combo = {"未回", "已回"}, defaultValue = "0", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private Integer contractStatus;
    /**
     * 合同签订状态 1 已回 时间
     */
    private Date contractStatusTime;
    /**
     * 协议签订状态(0 未回，1 已回)
     */
    @Excel(name = "协议状态", readConverterExp = "0=未回,1=已回", combo = {"未回", "已回"}, defaultValue = "0", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private Integer dealStatus;
    /**
     * 协议签订状态 1 已回 时间
     */
    private Date dealStatusTime;
    /**
     * 受检企业信息表ID
     */
    private Long companyId;
    /**
     * 受检单位
     */
    @Excel(name = "受检单位", headerBackgroundColor = IndexedColors.DARK_YELLOW, width = 50, align = HorizontalAlignment.LEFT, prompt = "注意：受检单位相关信息自动匹配！如果存在重复数据，取最新数据，请注意核查。")
    private String company;
    /**
     * 委托单位，企业信息表ID
     */
    private Long entrustCompanyId;
    /**
     * 委托单位
     */
    @Excel(name = "委托单位", headerBackgroundColor = IndexedColors.DARK_YELLOW, width = 50, align = HorizontalAlignment.LEFT, prompt = "注意：委托单位相关信息自动匹配！如果存在重复数据，取最新数据，请注意核查。")
    private String entrustCompany;
    /**
     * 委托单位详细地址
     */
    private String entrustOfficeAddress;
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
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 委托类型
     */
    @Excel(name = "委托类型", headerBackgroundColor = IndexedColors.DARK_YELLOW, combo = {"政府委托", "单位委托", "招投标项目"})
    private String entrustType;
    /**
     * 委托日期
     */
    @Excel(name = "委托日期", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private Date commissionDate;
    /**
     * 新老业务(0新业务，1续签业务)
     */
    @Excel(name = "新老业务", readConverterExp = "0=新业务,1=续签业务", combo = {"新业务", "续签业务"}, defaultValue = "0", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private Integer old;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", headerBackgroundColor = IndexedColors.BLUE_GREY, prompt = "可不填，默认：受检单位+项目类型")
    private String projectName;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", headerBackgroundColor = IndexedColors.BLUE_GREY)
    private String projectIdentifier;
    /**
     * 项目状态
     */
    @Excel(name = "项目状态", headerBackgroundColor = IndexedColors.BLUE_GREY, readConverterExp = "1=录入,2=下发,90=挂起", combo = {"录入", "下发", "挂起"}, defaultValue = "1")
    private Integer projectStatus;
    /**
     * 所属部门ID
     */
    @Excel(name = "下发部门", prompt = "注意：项目状态为下发时填写！", headerBackgroundColor = IndexedColors.RED, width = 25,
            readConverterExp = "12=实验室（杭州安联）,14=公共卫生组（杭州安联）,15=环境咨询组（杭州安联）,16=环境检测组（杭州安联）," +
                    "51=中介组（杭州安联）,18=检评组（杭州安联）,23=评价组（杭州安联）,27=辐射事业部（杭州安联）," +
                    "32=职业卫生事业部-嘉兴（嘉兴安联）,39=检评组（宁波安联）",
            combo = {
                    "实验室（杭州安联）", "公共卫生组（杭州安联）", "环境咨询组（杭州安联）", "环境检测组（杭州安联）",
                    "中介组（杭州安联）", "检评组（杭州安联）", "评价组（杭州安联）", "辐射事业部（杭州安联）", "职业卫生事业部-嘉兴（嘉兴安联）", "检评组（宁波安联）"
            })
    private Long projectDeptId;
    /**
     * 项目隶属公司
     */
    @Excel(name = "项目隶属公司", combo = {"杭州安联", "嘉兴安联", "宁波安联", "上海量远", "集团发展", "杭州亿达", "杭州卫康", "金华职康"})
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    @Excel(name = "业务来源", combo = {"杭州安联", "嘉兴安联", "宁波安联", "上海量远", "集团发展", "杭州亿达", "杭州卫康", "金华职康"})
    private String businessSource;
    /**
     * 业务员ID
     */
    private Integer salesmenid;
    /**
     * 业务员
     */
    @Excel(name = "业务员")
    private String salesmen;
    /**
     * 合同金额(元)
     */
    @Excel(name = "合同金额(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal totalMoney;
    /**
     * 佣金(元)
     */
    @Excel(name = "佣金(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal commission;
    /**
     * 评审费(元)
     */
    @Excel(name = "评审费(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @Excel(name = "分包费(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal subcontractFee;
    /**
     * 服务费用(元)
     */
    @Excel(name = "服务费用(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @Excel(name = "其他支出(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal otherExpenses;
    /**
     * 合同净值(元)
     */
//    @Excel(name = "合同净值(元)", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal netvalue;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 60)
    private String remarks;
}
