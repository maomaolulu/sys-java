package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 激光辐射Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LaserRadiationVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 接触部位 **/
    private String touchPart;
    /** 接触时间 **/
    private String touchTime;
    /** 光谱范围	 **/
    private String radiRange;
    /** 波长 **/
    private String waveLength;
    /** 照射时间 **/
    private String exposTime;
    /** 照射量 **/
    private String exposAmount;
    /** 辐射度 **/
    private String radiance;
    /** 结果判定 **/
    private Integer judgeResult;

}
