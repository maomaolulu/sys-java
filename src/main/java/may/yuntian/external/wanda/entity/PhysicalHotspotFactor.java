package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * wanda-物理因素热点数据检测结果实体
 * @author: liyongqiang
 * @create: 2023-03-08 11:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@TableName("wanda_physical_hotspot_factor")
public class PhysicalHotspotFactor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 项目id **/
    private Long projectId;
    /** 车间名称 **/
    private String workUnitName;
    /** 岗位/工种 **/
    private String workJobName;
    /** 劳动定员人数 **/
    private String workNum;
    /** 检测点 **/
    private String checkPointName;
    /** 噪声类型（1.个体，2.场所-稳态噪声，3.场所-非稳态噪声，4.场所-脉冲噪声） **/
    private Integer noiseType;
    /** ①噪声：检测数值（单位dB(A)）②高温：检测结果（单位：℃）③照度：检测结果 ④控制风速：检测结果 **/
    private String checkValue;
    /** 最低值（单位dB(A)） **/
    private String checkValueMin;
    /** 最高值（单位dB(A)） **/
    private String checkValueMax;
    /** 8/40h 等效声级检测数值 **/
    private String checkValue840;
    /** 工作日接触脉冲次数 **/
    private Integer touchTimes;
    /** 声压级峰值 **/
    private String noisePeak;
    /** 结果判定（1.不判定，2.符合，3.不符合）包含：①噪声②高温③工频电场④照度⑤控制风速⑥紫外辐射 **/
    private Integer judgeResult;
    /** 物理危害因素类型（3：噪声，4：高温，5、紫外辐射，6：工频电场，7：照度，8：控制风速） **/
    private Integer physicalFactorType;
    /** ①高温：接触时间率（%）（不要大于100%不能为0或者负数）②工频电场：接触时间（单位：h）③紫外辐射：接触时间（单位：h） **/
    private String touchTime;
    /** 体力劳动强度（0：1级，1：2级，2：3级，3：4级） **/
    private Integer physicalStrength;
    /** 电场强度 单位（kV/m） **/
    private String freqStrength;
    /** 紫外光谱分类（1.中波紫外线，2.短波紫外线，3.电焊弧光） **/
    private Integer ultraType;
    /** 辐照度 单位（μW/cm²） **/
    private String irradiance;
    /** 照射量 单位（mj/cm²）  **/
    private String exposure;
    /** 罩内眼部辐照度 单位（μW/cm²） **/
    private String eyeIrradiance;
    /** 罩内眼部照射量 单位（mj/cm²） **/
    private String eyeExposure;
    /** 罩内眼部结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer eyeJudgeResult;
    /** 罩内面部辐照度（单位（μW/cm²）） **/
    private String faceIrradiance;
    /** 罩内面部照射量（单位（mj/cm²）） **/
    private String faceExposure;
    /** 罩内面部结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer faceJudgeResult;
    /** 无防护辐照度（单位（μW/cm²）） **/
    private String noneIrradiance;
    /** 无防护照射量（单位（mj/cm²）） **/
    private String noneExposure;
    /** 无防护结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer noneJudgeResult;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    /** 特殊字段：控制风速（毒物、粉尘限值，需拿结果去比较） **/
    private String specialDataJson;
    @TableField(exist = false)
    /** 标识该条记录是否被删除：0未，1删除 **/
    private int remove;

}
