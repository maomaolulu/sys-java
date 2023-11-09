package may.yuntian.homepage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务员：新老客户分析饼Vo对象
 * @author: liyongqiang
 * @create: 2023-02-23 17:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseCakeVo {

    /** 项目id **/
    private Long projectId;
    /** 项目名称 **/
    private String projectName;
    /** 业务员id **/
    private Long salesmenid;
    /** 业务员（当前登录用户id） **/
    private String salesmen;
    /** 新老业务(0新业务，1续签业务) **/
    private Integer old;
    /** 项目签订日期 **/
    private String signDate;
    /** 项目签发日期 **/
    private String reportIssue;
    /** 项目金额 **/
    private BigDecimal totalMoney;
    /** 已收款金额（项目回款额） **/
    private BigDecimal receiptMoney;
    /** 已开票金额（项目开票额） **/
    private BigDecimal invoiceMoney;
    /** 月份：01,02,03...11,12 **/
    private Integer month;

    /** 查询条件的起始日期 **/
    private Date startDate;
    /** 查询条件的截止日期 **/
    private Date endDate;
}
