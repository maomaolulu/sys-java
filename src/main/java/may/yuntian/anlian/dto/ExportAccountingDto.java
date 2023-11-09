package may.yuntian.anlian.dto;

import lombok.Data;
import may.yuntian.modules.sys_v2.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportAccountingDto {
    @Excel(name = "项目编号")
    private String identifier;
    @Excel(name = "项目隶属")
    private String companyOrder;
    @Excel(name = "业务来源")
    private String businessSource;
    @Excel(name = "签订日期" , dateFormat = "yyyy-MM-dd")
    private Date signDate;
    @Excel(name = "项目状态" , readConverterExp = "1=录入,2=下发,3=排单,4=现场调查,5=采样,10=送样,20=检测报告," +
            "22=检测报告发送,35=报告编制,36=审核,37=专家评审,38=出版前效核,40=质控签发,50=报告寄送,60=质控归档,70=项目结束,99=项目中止")
    private Integer status;
    @Excel(name = "业务员")
    private String salesmen;
    //金额相关
    @Excel(name = "项目金额（元）" , defaultValue = "0")
    private BigDecimal toltalMoney;
    @Excel(name = "项目净值（元）" , defaultValue = "0")
    private BigDecimal netvalue;
    @Excel(name = "业务费（元）" , defaultValue = "0")
    private BigDecimal commission;
    @Excel(name = "分包费（元）" , defaultValue = "0")
    private BigDecimal subprojectFee;
    @Excel(name = "服务费用（元）" , defaultValue = "0")
    private BigDecimal serviceCharge;
    @Excel(name = "评审费（元）" , defaultValue = "0")
    private BigDecimal evaluationFee;
    @Excel(name = "其他支出（元）" , defaultValue = "0")
    private BigDecimal otherExpenses;
    @Excel(name = "业务提成（元）" , defaultValue = "0")
    private BigDecimal yewu;
    @Excel(name = "客服提成（元）" , defaultValue = "0")
    private BigDecimal kefu;
    @Excel(name = "采样提成（元）" , defaultValue = "0")
    private BigDecimal caiyang;
    @Excel(name = "签发提成（元）" , defaultValue = "0")
    private BigDecimal qianfa;
    @Excel(name = "报告提成（元）" , defaultValue = "0")
    private BigDecimal baogao;
    @Excel(name = "检测提成（元）" , defaultValue = "0")
    private BigDecimal jiance;
    @Excel(name = "报告编制提成（元）" , defaultValue = "0")
    private BigDecimal bianzhi;
    @Excel(name = "综合成本公摊（元）" , defaultValue = "0")
    private BigDecimal share;//综合成本公摊
    @Excel(name = "预计利润（元）" , defaultValue = "0")
    private BigDecimal estimatedProfit;//预计利润
}
