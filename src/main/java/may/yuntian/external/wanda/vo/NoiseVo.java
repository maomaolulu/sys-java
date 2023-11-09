package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 噪声Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NoiseVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 噪声类型（1.个体，2.场所-稳态噪声，3.场所-非稳态噪声，4.场所-脉冲噪声） **/
    private Integer noiseType;
    /** 检测数值	**/
    private String checkValue;
    /** 最低值 **/
    private String checkValueMin;
    /** 最高值 **/
    private String checkValueMax;
    /** 8/40h 等效声级检测数值 **/
    private String checkValue840;
    /** 工作日接触脉冲次数 **/
    private Integer touchTimes;
    /** 声压级峰值 **/
    private String noisePeak;
    /** 结果判定	**/
    private Integer judgeResult;

}
