package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工频电场vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PowerFrequencyElectricVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 接触时间 **/
    private String touchTime;
    /** 电场强度	 **/
    private String freqStrength;
    /** 结果判定 **/
    private Integer judgeResult;

}
