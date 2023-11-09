package may.yuntian.anlian.dto;

import lombok.Data;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.modules.sys_v2.annotation.Excel;
import may.yuntian.modules.sys_v2.annotation.Excels;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Date;

/**
 * 企业信息 数据导入
 *
 * @author hjy
 * @date 2023/6/13 16:33
 */
@Data
public class CompanyEntityDTO {
    /**
     * 自增主键ID
     */
    private Long id;
    /**
     * 统一社会信用代码
     */
    @Excel(name = "统一社会信用代码")
    private String code;
    /**
     * 企业名称
     */
    @Excel(name = "企业名称")
    private String company;
    /**
     * 注册地址
     */
    @Excel(name = "注册地址")
    private String address;
    /**
     * 法人代表
     */
    @Excel(name = "法人代表")
    private String legalname;
    /**
     * 经营范围
     */
    @Excel(name = "最近合作日期")
    private String scope;
    /**
     * 成立日期
     */
    @Excel(name = "成立日期")
    private Date registerDate;
    /**
     * 省份
     */
    @Excel(name = "省份")
    private String province;
    /**
     * 城市
     */
    @Excel(name = "城市")
    private String city;
    /**
     * 区县
     */
    @Excel(name = "区县")
    private String area;
    /**
     * 办公地址
     */
    @Excel(name = "办公地址")
    private String officeAddress;
    /**
     * 固定电话
     */
    @Excel(name = "固定电话")
    private String telephone;
    /**
     * 传真
     */
    @Excel(name = "传真")
    private String fax;
    /**
     * 行业类别
     */
    @Excel(name = "行业类别")
    private String industryCategory;
    /**
     * 职业病危害风险分类(0 一般、1 较重、2 严重)
     */
    @Excel(name = "职业病危害风险分类", readConverterExp = "0=一般,1=较重,2=严重", combo = {"一般", "较重", "严重"}, defaultValue = "0")
    private Integer riskLevel;
    /**
     * 人员规模(人)
     */
    @Excel(name = "人员规模")
    private Integer population;
    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String products;
    /**
     * 产量信息
     */
    @Excel(name = "产量信息")
    private String yields;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remarks;
    /**
     * 合作状态(0 潜在、1 意向、2 已合作)
     */
    @Excel(name = "合作状态", readConverterExp = "0=潜在,1=意向,2=已合作", combo = {"潜在", "意向", "已合作"}, defaultValue = "0")
    private Integer contractStatus;
    /**
     * 首次合作日期
     */
    @Excel(name = "首次合作日期")
    private Date contractFirst;
    /**
     * 最近合作日期
     */
    @Excel(name = "最近合作日期")
    private Date contractLast;
    /**
     * 数据所属公司（杭州、宁波、嘉兴、上海量远等）
     */
    private String dataBelong;
    /**
     * 公司联系人列表1
     */
    @Excels({
            @Excel(name = "姓名-默认", targetAttr = "contact", headerBackgroundColor = IndexedColors.DARK_YELLOW),
            @Excel(name = "电话-1", targetAttr = "mobile", headerBackgroundColor = IndexedColors.DARK_YELLOW),
            @Excel(name = "固话-1", targetAttr = "telephone", headerBackgroundColor = IndexedColors.DARK_YELLOW),
            @Excel(name = "邮箱-1", targetAttr = "email", headerBackgroundColor = IndexedColors.DARK_YELLOW),
            @Excel(name = "类型-1", targetAttr = "type", combo = {"商务联系人", "业务联系人", "EHS联系人", "财务联系人"}, headerBackgroundColor = IndexedColors.DARK_YELLOW),
            @Excel(name = "联系人备注-1", targetAttr = "remark", headerBackgroundColor = IndexedColors.DARK_YELLOW)
    })
    private CompanyContactEntity companyContact = new CompanyContactEntity();
    /**
     * 公司联系人列表2
     */
    @Excels({
            @Excel(name = "姓名-2", targetAttr = "contact", headerBackgroundColor = IndexedColors.BLUE_GREY),
            @Excel(name = "电话-2", targetAttr = "mobile", headerBackgroundColor = IndexedColors.BLUE_GREY),
            @Excel(name = "固话-2", targetAttr = "telephone", headerBackgroundColor = IndexedColors.BLUE_GREY),
            @Excel(name = "邮箱-2", targetAttr = "email", headerBackgroundColor = IndexedColors.BLUE_GREY),
            @Excel(name = "类型-2", targetAttr = "type", combo = {"商务联系人", "业务联系人", "EHS联系人", "财务联系人"}, headerBackgroundColor = IndexedColors.BLUE_GREY),
            @Excel(name = "联系人备注-2", targetAttr = "remark", headerBackgroundColor = IndexedColors.BLUE_GREY)
    })
    private CompanyContactEntity companyContact2 = new CompanyContactEntity();
    /**
     * 公司联系人列表3
     */
    @Excels({
            @Excel(name = "姓名-3", targetAttr = "contact", headerBackgroundColor = IndexedColors.DARK_GREEN),
            @Excel(name = "电话-3", targetAttr = "mobile", headerBackgroundColor = IndexedColors.DARK_GREEN),
            @Excel(name = "固话-3", targetAttr = "telephone", headerBackgroundColor = IndexedColors.DARK_GREEN),
            @Excel(name = "邮箱-3", targetAttr = "email", headerBackgroundColor = IndexedColors.DARK_GREEN),
            @Excel(name = "类型-3", targetAttr = "type", combo = {"商务联系人", "业务联系人", "EHS联系人", "财务联系人"}, headerBackgroundColor = IndexedColors.DARK_GREEN),
            @Excel(name = "联系人备注-3", targetAttr = "remark", headerBackgroundColor = IndexedColors.DARK_GREEN)
    })
    private CompanyContactEntity companyContact3 = new CompanyContactEntity();
    /**
     * 公司联系人列表4
     */
    @Excels({
            @Excel(name = "姓名-4", targetAttr = "contact", headerBackgroundColor = IndexedColors.DARK_RED),
            @Excel(name = "电话-4", targetAttr = "mobile", headerBackgroundColor = IndexedColors.DARK_RED),
            @Excel(name = "固话-4", targetAttr = "telephone", headerBackgroundColor = IndexedColors.DARK_RED),
            @Excel(name = "邮箱-4", targetAttr = "email", headerBackgroundColor = IndexedColors.DARK_RED),
            @Excel(name = "类型-4", targetAttr = "type", combo = {"商务联系人", "业务联系人", "EHS联系人", "财务联系人"}, headerBackgroundColor = IndexedColors.DARK_RED),
            @Excel(name = "联系人备注-4", targetAttr = "remark", headerBackgroundColor = IndexedColors.DARK_RED)
    })
    private CompanyContactEntity companyContact4 = new CompanyContactEntity();
    /**
     * 公司联系人列表5
     */
    @Excels({
            @Excel(name = "姓名-5", targetAttr = "contact", headerBackgroundColor = IndexedColors.VIOLET),
            @Excel(name = "电话-5", targetAttr = "mobile", headerBackgroundColor = IndexedColors.VIOLET),
            @Excel(name = "固话-5", targetAttr = "telephone", headerBackgroundColor = IndexedColors.VIOLET),
            @Excel(name = "邮箱-5", targetAttr = "email", headerBackgroundColor = IndexedColors.VIOLET),
            @Excel(name = "类型-5", targetAttr = "type", combo = {"商务联系人", "业务联系人", "EHS联系人", "财务联系人"}, headerBackgroundColor = IndexedColors.VIOLET),
            @Excel(name = "联系人备注-5", targetAttr = "remark", headerBackgroundColor = IndexedColors.VIOLET)
    })
    private CompanyContactEntity companyContact5 = new CompanyContactEntity();


}
