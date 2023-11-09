package may.yuntian.anlian.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
@HeadRowHeight(20) // 指定头部行高
@ContentRowHeight(15) // 指定内容行高
@ColumnWidth(15) // 指定内容宽度
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT) // 设置单元格水平对齐方式：LEFT
public class ContractExportDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private Long id;
    /**
     * 合同编号
     */
    @ExcelProperty("合同编号")
    private String identifier;
    /**
     * 项目名称
     */
    @ExcelProperty("项目名称")
    private String projectName;
    /**
     * 受检单位
     */
    @ExcelProperty("受检单位")
    @ColumnWidth(30)
    private String company;
    /**
     * 委托单位
     */
    @ExcelProperty("委托单位")
    @ColumnWidth(30)
    private String entrustCompany;
    /**
     * 项目隶属公司
     */
    @ExcelProperty("项目隶属公司")
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    @ExcelProperty("业务来源")
    private String businessSource;
    /**
     * 合同类型
     */
    @ExcelProperty("合同类型")
    private String type;
    /**
     * 联系人
     */
    @ExcelProperty("联系人")
    private String contact;
    /**
     * 联系电话
     */
    @ExcelProperty("联系电话")
    private String telephone;
    @ExcelProperty("合同金额(元)")
    private BigDecimal totalMoney;
    /**
     * 合同净值(元)
     */
    @ExcelProperty("合同净值(元)")
    private BigDecimal netvalue;
    /**
     * 佣金(元)
     */
    @ExcelProperty("业务费(元)")
    private BigDecimal commission;
    /**
     * 评审费(元)
     */
    @ExcelProperty("评审费(元)")
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @ExcelProperty("分包费(元)")
    private BigDecimal subcontractFee;
    /**
     * 服务费用(元)
     */
    @ExcelProperty("服务费用(元)")
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @ExcelProperty("其他支出(元)")
    private BigDecimal otherExpenses;
    /**
     * 合同签订状态(0 未回，1 已回 )
     */
    @ExcelProperty("合同签订状态")
    private Integer contractStatus;
    /**
     * 协议签订状态(0 未回，1 已回)
     */
    @ExcelProperty("协议状态")
    private Integer dealStatus;
    /**
     * 业务员
     */
    @ExcelProperty("业务员")
    private String salesmen;
    /**
     * 委托日期
     */
    @ExcelProperty("委托日期")
    private Date commissionDate;
    /**
     * 合同签订日期
     */
    @ExcelProperty("合同签订日期")
    private Date signDate;

}
