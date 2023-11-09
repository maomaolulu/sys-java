package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微小气候Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MicroclimateVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 温度检测结果 **/
    private String temperature;
    /** 温度结果判定 **/
    private Integer tempJudgeResult;
    /** 风速检测结果 **/
    private String windSpeed;
    /** 风速结果判定 **/
    private Integer windJudgeResult;
    /** 湿度检测结果 **/
    private String humidity;
    /** 湿度结果判定 **/
    private Integer humidityJudgeResult;

}
