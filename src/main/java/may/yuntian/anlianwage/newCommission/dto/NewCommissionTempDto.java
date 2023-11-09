package may.yuntian.anlianwage.newCommission.dto;

import lombok.Data;
import may.yuntian.modules.sys_v2.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewCommissionTempDto {

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 提成人
     */
    @Excel(name = "提成人")
    private String person;
    /**
     * 隶属公司
     */
    @Excel(name = "隶属公司" , combo = {"杭州安联"} )
    private String subjection;
    /**
     * 所属部门
     */
    @Excel(name = "所属部门" , combo = {"检评报告编制","检评采样","评价报告编制","评价采样","环境采样","环境报告编制","环境咨询","市场部","客服部","实验室","辐射事业部"})
    private String dept;
    /**
     * 提成类型
     */
    @Excel(name = "提成类型" ,
        combo = {"业务提成","客服提成","采样提成","签发提成","报告提成","检测提成","报告编制"})
    private String commissionType;
    /**
     * 项目类型
     */
    @Excel(name = "项目类型",
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
     * 项目编号
     */
    @Excel(name = "项目编号")
    private String identifier;
    /**
     * 计提日期
     */
    @Excel(name = "计提日期" , dateFormat = "yyyy-MM-dd")
    private Date accrualDate;
    /**
     * 计提总额
     */
    @Excel(name = "计提总额" , defaultValue = "0")
    private BigDecimal accrualAmount;
    /**
     * 月度提成状态
     */
    @Excel(name = "月度提成状态" , combo = {"已提成","待提成"})
    private String commissionStatusMonth;
    /**
     * 月度计提金额
     */
    @Excel(name = "月度计提金额" , defaultValue = "0")
    private BigDecimal accrualAmountMonth;
    /**
     * 年度提成状态
     */
    @Excel(name = "年度提成状态" , combo = {"已提成","待提成"})
    private String commissionStatusYear;
    /**
     * 年度计提金额
     */
    @Excel(name = "年度计提金额" , defaultValue = "0")
    private BigDecimal accrualAmountYear;
    /**
     * 月度绩效发放日期
     */
    @Excel(name = "月度绩效发放日期" , dateFormat = "yyyy-MM-dd")
    private Date commissionDateMonth;
    /**
     * 年度绩效发放日期
     */
    @Excel(name = "年度绩效发放日期" , dateFormat = "yyyy-MM-dd")
    private Date commissionDateYear;
}
