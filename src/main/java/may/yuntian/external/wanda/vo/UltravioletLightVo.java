package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 紫外辐射vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UltravioletLightVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 紫外光谱分类	 **/
    private Integer ultraType;
    /** 接触时间 **/
    private String touchTime;
    /** 辐照度 **/
    private String irradiance;
    /** 照射量 **/
    private String exposure;
    /** 结果判定 **/
    private Integer judgeResult;
    /** 罩内眼部辐照度 **/
    private String eyeIrradiance;
    /** 罩内眼部照射量 **/
    private String eyeExposure;
    /** 罩内眼部结果判定 **/
    private Integer eyeJudgeResult;
    /** 罩内面部辐照度 **/
    private String faceIrradiance;
    /** 罩内面部照射量 **/
    private String faceExposure;
    /** 罩内面部结果判定 **/
    private Integer faceJudgeResult;
    /** 无防护辐照度 **/
    private String noneIrradiance;
    /** 无防护照射量 **/
    private String noneExposure;
    /** 无防护结果判定 **/
    private Integer noneJudgeResult;

}
