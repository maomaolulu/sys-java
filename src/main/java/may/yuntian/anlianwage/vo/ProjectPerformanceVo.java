package may.yuntian.anlianwage.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: ANLIAN-JAVA
 * @description:    评价-项目绩效Vo
 * @author: liyongqiang
 * @create: 2022-05-29 16:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class ProjectPerformanceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 签发时间
     */
    private String reportTransfer;
    /**
     * 项目负责人
     */
    private String charge;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检单位
     */
    private String company;
    /**
     * 净值
     */
    private BigDecimal netvalue;
    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;
    /**
     * 行业分类
     */
    private String industryCategory;
    /**
     * 一线作业人数
     */
    private Integer laborQuota;
    /**
     * 项目类型：评价（现状，控评，预评，专篇）
     */
    private String type;
    /**
     * 行业基准分
     */
    private Double industryBenchmarkScore;
    /**
     * 规模基准分
     */
    private Double scaleBasisScore;
    /**
     * 项目类型分
     */
    private Double itemTypeScore;
    /**
     * 加急技术分
     */
    private Double urgentCaseScore;
    /**
     * 技术打分
     */
    private Double technicalGradeScore;
    /**
     * 项目绩点：行业基准分 * 规模基准分 * 项目类型分 * 加急技术分 * 技术打分
     */
    private Double projectGradePoint;
    /**
     * 总绩效：650 * 项目绩点
     */
    private BigDecimal totalPerformance;
    /**
     * 参考绩效：净值 * 0.06
     */
    private BigDecimal referPerformance;
    /**
     * 差值：总绩效 - 参考绩效
     */
    private BigDecimal difference;
    /**
     * 开始时间
     */
    @TableField(exist=false)
    private String startDate;
    /**
     * 结束时间
     */
    @TableField(exist=false)
    private String endDate;

    /**
     * 项目隶属公司
     */
    private String subjection;
//    /**
//     * 签发提成
//     */
//    private BigDecimal commissionIssue;
//    /**
//     * 归档提成
//     */
//    private BigDecimal filingFees;

}
