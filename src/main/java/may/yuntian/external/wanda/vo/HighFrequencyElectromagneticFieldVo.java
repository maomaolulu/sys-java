package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 高频电磁场Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HighFrequencyElectromagneticFieldVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 频率（f，MHz） **/
    private String frequency;
    /** 接触时间 **/
    private String touchTime;
    /** 电场强度	**/
    private String eletricStrength;
    /** 磁场强度 **/
    private String magneticStrength;
    /** 结果判定 **/
    private Integer judgeResult;

}
