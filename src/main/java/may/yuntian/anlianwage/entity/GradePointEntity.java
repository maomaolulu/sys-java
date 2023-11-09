package may.yuntian.anlianwage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ANLIAN-JAVA
 * @description: 绩点表实体类
 * @author: liyongqiang
 * @create: 2022-05-29 18:35
 */
@Data
@TableName("co_grade_point")
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class GradePointEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
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
     * 项目绩点：行业基准分 + 规模基准分 + 项目类型分 + 加急技术分 + 技术打分
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
   /* *//**
     * 签发提成
     *//*
    private BigDecimal commissionIssue;
    *//**
     * 归档提成
     *//*
    private BigDecimal filingFees;*/
//    /**
//     * 数据入库时间
//     */
//    private Date createTime;
//    /**
//     * 数据修改时间
//     */
//    private Date updateTime;

}
