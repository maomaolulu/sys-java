package may.yuntian.anlian.dto;

import lombok.Data;
import may.yuntian.modules.sys_v2.annotation.Excel;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目数据实体
 *
 * @author hjy
 * @date 2023/6/12 17:22
 */
@Data
public class ProjectEntityDTO {
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", align = HorizontalAlignment.LEFT, headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private String identifier;
    /**
     * 项目类型
     */
    @Excel(name = "项目类型", headerBackgroundColor = IndexedColors.DARK_YELLOW,
            combo = {"检评",
                    "预评", "专篇", "控评", "现状",
                    "环境监测", "环境验收", "排污许可", "环评监测", "清洁生产审核", "场地调查", "环评",
                    "放射检测",
                    "公共卫生检测", "公共卫生学评价", "一次性用品用具检测", "洁净区域检测", "学校卫生检测",
                    "职卫监督", "公卫监督", "环境监督",
                    "来样检测",
                    "应急预案", "风险评估", "职卫示范", "公卫示范", "环境示范", "其他示范", "环保管家",
                    "放射预评价", "放射控制效果评价", "放射设计专篇", "放射现状评价"})
    private String type;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private String projectName;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    private String contractIdentifier;
    /**
     * 项目状态(1 录入，2 任务下发，90 挂起)
     */
    @Excel(name = "项目状态", headerBackgroundColor = IndexedColors.DARK_YELLOW, readConverterExp = "1=录入,2=下发,90=挂起", combo = {"录入", "下发", "挂起"}, defaultValue = "1")
    private Integer status;
    /**
     * 下发部门id
     */
    @Excel(name = "下发部门", prompt = "注意：项目状态为下发时填写！", headerBackgroundColor = IndexedColors.RED, width = 25,
            readConverterExp = "12=实验室（杭州安联）,14=公共卫生组（杭州安联）,15=环境咨询组（杭州安联）,16=环境检测组（杭州安联）," +
                    "51=中介组（杭州安联）,18=检评组（杭州安联）,23=评价组（杭州安联）,27=辐射事业部（杭州安联）," +
                    "32=职业卫生事业部-嘉兴（嘉兴安联）,39=检评组（宁波安联）",
            combo = {
                    "实验室（杭州安联）", "公共卫生组（杭州安联）", "环境咨询组（杭州安联）", "环境检测组（杭州安联）",
                    "中介组（杭州安联）", "检评组（杭州安联）", "评价组（杭州安联）", "辐射事业部（杭州安联）", "职业卫生事业部-嘉兴（嘉兴安联）", "检评组（宁波安联）"
            })
    private Long deptId;
    /**
     * 合同ID
     */
    private Long contractId;
    /**
     * 受检企业信息表ID
     */
    private Long companyId;
    /**
     * 受检企业名称
     */
    @Excel(name = "受检企业名称", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String company;
    /**
     * 省份
     */
    @Excel(name = "省份", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String province;
    /**
     * 城市
     */
    @Excel(name = "城市", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String city;
    /**
     * 区县
     */
    @Excel(name = "区县", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String area;
    /**
     * 受检详细地址
     */
    @Excel(name = "受检详细地址", headerBackgroundColor = IndexedColors.DARK_BLUE, width = 50)
    private String officeAddress;
    /**
     * 联系人
     */
    @Excel(name = "联系人", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String contact;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话", headerBackgroundColor = IndexedColors.DARK_BLUE)
    private String telephone;
    /**
     * 委托类型
     */
    @Excel(name = "委托类型", combo = {"政府委托", "单位委托", "招投标项目"}, headerBackgroundColor = IndexedColors.SEA_GREEN)
    private String entrustType;
    /**
     * 委托单位，企业信息表ID
     */
    private Long entrustCompanyId;
    /**
     * 委托单位名称
     */
    @Excel(name = "委托单位名称", headerBackgroundColor = IndexedColors.SEA_GREEN)
    private String entrustCompany;
    /**
     * 委托单位详细地址
     */
    @Excel(name = "委托单位详细地址", headerBackgroundColor = IndexedColors.SEA_GREEN, width = 50)
    private String entrustOfficeAddress;
    /**
     * 委托日期
     */
    @Excel(name = "委托日期", headerBackgroundColor = IndexedColors.SEA_GREEN)
    private Date entrustDate;
    /**
     * 签订日期
     */
    @Excel(name = "签订日期", headerBackgroundColor = IndexedColors.SEA_GREEN)
    private Date signDate;
    /**
     * 要求报告完成日期
     */
    @Excel(name = "要求报告完成日期", headerBackgroundColor = IndexedColors.SEA_GREEN)
    private Date claimEndDate;
    /**
     * 项目隶属公司
     */
    @Excel(name = "项目隶属公司", combo = {"杭州安联", "嘉兴安联", "宁波安联", "上海量远", "集团发展", "杭州亿达"})
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    @Excel(name = "业务来源", combo = {"杭州安联", "嘉兴安联", "宁波安联", "上海量远", "集团发展", "杭州亿达"})
    private String businessSource;
    /**
     * 业务员ID
     */
    private Long salesmenid;
    /**
     * 业务员
     */
    @Excel(name = "业务员")
    private String salesmen;
    /**
     * 加急状态(0正常，1较急、2加急)
     */
    @Excel(name = "加急状态", readConverterExp = "0=正常,1=较急,2=加急", combo = {"正常", "较急", "加急"}, defaultValue = "0")
    private Integer urgent;
    /**
     * 新老业务(0新业务，1续签业务)
     */
    @Excel(name = "新老业务", readConverterExp = "0=新业务,1=续签业务", combo = {"新业务", "续签业务"}, defaultValue = "0")
    private Integer old;
    /**
     * 项目金额(元)
     */
    @Excel(name = "项目金额", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal totalMoney;
    /**
     * 业务费(元)
     */
    @Excel(name = "业务费", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal commission;
    /**
     * 评审费(元)
     */
    @Excel(name = "评审费", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @Excel(name = "分包费", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal subprojectFee;
    /**
     * 服务费用(元)
     */
    @Excel(name = "服务费用", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @Excel(name = "其他支出", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal otherExpenses;
    /**
     * 项目净值(元)
     */
    @Excel(name = "项目净值", headerBackgroundColor = IndexedColors.DARK_GREEN)
    private BigDecimal netvalue;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 60)
    private String remarks;
}
