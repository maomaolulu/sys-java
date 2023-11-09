package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 生物危害Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BiohazardVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 危害因素id **/
    private Integer factorId;
    /** 危害因素名称 **/
    private String factorName;
    /** CME/CMAC值 **/
    private Integer cmeCmac;
    /** CTWA值 **/
    private Integer ctwa;
    /** CSTE/CSTEL值 **/
    private Integer csteCstel;
    /** 折减因子类型（1.日折减，2.周折减） **/
    private Integer reductionFactorType;
    /** 折减因子 **/
    private String reductionFactor;
    /** 结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer judgeResult;
}
