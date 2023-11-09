package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 超高频辐射Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UltraHighFrequencyRadiationVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 波形类型 **/
    private Integer uhfrType;
    /** 接触时间 **/
    private String touchTime;
    /** 功率密度 **/
    private String powerDensity;
    /** 电场强度 **/
    private String uhfrStrength;
    /** 结果判定 **/
    private Integer judgeResult;


}
