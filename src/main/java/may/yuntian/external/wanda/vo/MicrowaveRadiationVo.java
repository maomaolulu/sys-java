package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微波辐射Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MicrowaveRadiationVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 辐射类型 **/
    private Integer radiType;
    /** 微波类型 **/
    private Integer waveType;
    /** 受辐射时间 **/
    private String radiTime;
    /** 日剂量 **/
    private String daily;
    /** 平均功率密度 **/
    private String average;
    /** 短时间接触功率密度	 **/
    private String touchDensity;
    /** 结果判定	 **/
    private Integer judgeResult;

}
