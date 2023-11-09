package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 粉尘Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StiveVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 危害因素id **/
    private Integer factorId;
    /** 危害因素名称 **/
    private String factorName;
    /** 总尘CTWA **/
    private String totalCtwa;
    /** 总尘浓度范围 **/
    private String totalRange;
    /** 总尘超限倍数/峰接触浓度 **/
    private String totalExceedPeak;
    /** 总尘折减因子类型（0.不考虑折减，1.日折减，2.周折减） **/
    private Integer totalReductionFactorType;
    /** 总尘折减因子 **/
    private String totalReductionFactor;
    /** 总尘结果判定 **/
    private Integer totalJudgeResult;
    /** 呼尘CTWA（单位mg/m³） **/
    private String respirableCtwa;
    /** 呼尘浓度范围 **/
    private String respirableRange;
    /** 呼尘超限倍数/峰接触浓度 **/
    private String respirableExceedPeak;
    /** 呼尘折减因子类型（0.不考虑折减，1.日折减，2.周折减） **/
    private Integer respirableReductionFactorType;
    /** 呼尘折减因子 **/
    private String respirableReductionFactor;
    /** 呼尘结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer respirableJudgeResult;

}
