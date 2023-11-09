package may.yuntian.anlian.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 财务中心-绩效提成：导出dto
 *
 * @author: liyongqiang
 * @create: 2023-08-25 10:33
 */
@Data
@EqualsAndHashCode
@HeadRowHeight(20) // 指定头部行高
@ContentRowHeight(15) // 指定内容行高
@ColumnWidth(15) // 指定内容宽度
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT) // 设置单元格水平对齐方式：LEFT
public class GradeExportDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private Long projectId;

    @ExcelProperty("项目编号")
    private String itemCode;

    @ExcelProperty("受检单位")
    @ColumnWidth(30)
    private String empCompany;

    @ExcelProperty("项目类别")
    private String itemType;

    @ExcelProperty("项目状态")
    private String itemStatus;

    @ExcelProperty("业务员")
    private String salesman;

    @ExcelProperty("项目负责人")
    private String charge;

    @ExcelProperty("合同隶属公司/绩效归属公司")
    @ColumnWidth(33)
    private String belongCompany;

    @ExcelProperty("项目金额")
    private String itemAmount;

    @ExcelProperty("已收金额")
    private String receivedAmount;

    @ExcelProperty("未结算金额")
    private String unsettledAmount;

    @ExcelProperty("业务费")
    private String businessCost;

    @ExcelProperty("评审费")
    private String reviewCost;

    @ExcelProperty("中介费")
    private String agentCost;

    @ExcelProperty("分包费")
    private String subcontractCost;

    @ExcelProperty("其它支出")
    private String otherCost;

    @ExcelProperty("净值")
    private String netValue;

    @ExcelProperty("提成类型")
    private String royaltyType;

    @ExcelProperty("提成金额")
    private String royaltyAmount;

    @ExcelProperty("提成人")
    private String commenter;

    @ExcelProperty("提成日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date royaltyDate;

    @ExcelProperty("统计日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date countDate;

    @ExcelProperty("项目签订日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(17)
    private Date signDate;

    @ExcelProperty("报告签发日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(17)
    private Date reportDate;

    @ExcelProperty("报告装订日期")
    @ColumnWidth(17)
    @DateTimeFormat("yyyy-MM-dd")
    private Date reportBindDate;

    @ExcelProperty("报告归档日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(17)
    private Date reportFilingDate;

    @ExcelProperty("收款日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date receiptDate;

    @ExcelProperty("委托类型")
    private String entrustType;

    @ExcelProperty("备注")
    private String remark;

}
