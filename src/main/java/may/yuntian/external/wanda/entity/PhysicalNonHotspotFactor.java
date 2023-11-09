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
 * wanda-物理因素非热点数据检测结果实体
 * @author: liyongqiang
 * @create: 2023-03-08 11:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@TableName("wanda_physical_non_hotspot_factor")
public class PhysicalNonHotspotFactor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** anlian项目id **/
    private Long projectId;
    /** 车间名称 **/
    private String workUnitName;
    /** 岗位/工种 **/
    private String workJobName;
    /** 劳动定员人数 **/
    private String workNum;
    /** 检测点 **/
    private String checkPointName;
    /** 人数 **/
    private Integer staffNum;
    /** 检测结果 单位（m/s） **/
    private String checkValue;
    /** 结果判定（1.不判定，2.符合，3.不符合）①新风量②手传振动③超高频辐射④高频电磁场⑤激光辐射⑥微波辐射 **/
    private Integer judgeResult;
    /** 物理危害因素类型（1.新风量，2.微小气候，3.手传振动4.超高频辐射） **/
    private Integer physicalFactorType;
    /** 温度检测结果 单位（℃） **/
    private String temperature;
    /** 温度结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer tempJudgeResult;
    /**  风速检测结果 单位（m/s） **/
    private String windSpeed;
    /** 风速结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer windJudgeResult;
    /** 湿度检测结果 单位（%） **/
    private String humidity;
    /** 湿度结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer humidityJudgeResult;
    /** 接触时间 单位（h）①手传振动②超高频辐射③高频电磁场④激光辐射 **/
    private String touchTime;
    /** 4h等能量频率计权振动加速度 单位（m/s²） **/
    private String accelet4h;
    /** 波形类型（1.连续波，2.脉冲波） **/
    private Integer uhfrType;
    /** 功率密度 单位（mW/cm²） **/
    private String powerDensity;
    /** 电场强度 单位（V/m）①超高频辐射②高频电磁场 **/
    private String uhfrStrength;
    /** 频率（f，MHz） **/
    private String frequency;
    /** 磁场强度 单位（A/m） **/
    private String magneticStrength;
    /** 接触部位 **/
    private String touchPart;
    /** 光谱范围 **/
    private String radiRange;
    /** 波长 单位（nm） **/
    private String waveLength;
    /** 照射时间 单位（s） **/
    private String exposTime;
    /** 照射量 单位（J/cm²） **/
    private String exposAmount;
    /** 辐射度 单位（W/cm²） **/
    private String radiance;
    /** 辐射类型（1.全身，2.肢体，局部辐射） **/
    private Integer radiType;
    /** 微波类型（1.连续微波，2.脉冲微波） **/
    private Integer waveType;
    /** 受辐射时间 单位（h） **/
    private String radiTime;
    /** 日剂量 单位（μW·h/cm²） **/
    private String daily;
    /**平均功率密度 单位（μW/cm²） **/
    private String average;
    /** 短时间接触功率密度 单位（mW/cm²） **/
    private String touchDensity;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    @TableField(exist = false)
    /** 标识该条记录是否被删除：0未，1删除 **/
    private int remove;

}
