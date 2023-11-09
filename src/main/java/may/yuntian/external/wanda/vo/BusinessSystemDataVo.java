package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.wanda.entity.AffixRecord;
import may.yuntian.external.wanda.entity.ProjectInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 业务上游数据Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BusinessSystemDataVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** projectId **/
    private Long projectId;
    /** 第三方同步成功的项目id **/
    private String belongThirdPrjId;
    /** 冗余字段：区分危害因素类型
     * （1化学，2粉尘，3.生物危害，4.电离辐射-射线装置，5.电离辐射-含源装置；
     *  6.噪声，7.高温，8.紫外辐射，9.工频电场，10.照度，11.控制风速；
     *  12.新风量，13.微小气候，14.手传振动，15.超高频辐射，16.高频电磁场，17.激光辐射，18.微波辐射）
     **/
    private Integer distinguishFactorType;
    /** 项目基本信息 **/
    private ProjectInfo projectInfo;
    /** 附件列表 **/
    private List<AffixRecord> affixRecordList;
    /** 化学危害因素 **/
    private List<ChemicalVo> chemicalVoList;
    /** 粉尘 **/
    private List<StiveVo> stiveVoList;
    /** 噪声 **/
    private List<NoiseVo> noiseVoList;
    /** 高温 **/
    private List<HighTemperatureVo> highTemperatureVoList;
    /** 工频电场 **/
    private List<PowerFrequencyElectricVo> powerFrequencyElectricVoList;
    /** 照度 **/
    private List<IlluminationVo> illuminationVoList;
    /** 控制风速 **/
    private List<ControlWindSpeedVo> controlWindSpeedVoList;
    /** 新风量 **/
    private List<FreshAirVolumeVo> freshAirVolumeVoList;
    /** 微小气候 **/
    private List<MicroclimateVo> microclimateVoList;
    /** 紫外辐射 **/
    private List<UltravioletLightVo> ultravioletLightVoList;
    /** 手传振动 **/
    private List<HandVibrationVo> handVibrationVoList;
    /** 超高频辐射 **/
    private List<UltraHighFrequencyRadiationVo> ultraHighFrequencyRadiationVoList;
    /** 高频电磁场 **/
    private List<HighFrequencyElectromagneticFieldVo> highFrequencyElectromagneticFieldVoList;
    /** 激光辐射 **/
    private List<LaserRadiationVo> laserRadiationVoList;
    /** 微波辐射 **/
    private List<MicrowaveRadiationVo> microwaveRadiationVoList;
    /** 生物危害 **/
    private List<BiohazardVo> biohazardVoList;
    /** 电离辐射-射线装置 **/
    private List<IonizingRadiationRayDeviceVo> ionizingRadiationRayDeviceVoList;
    /** 电离辐射-含源装置 **/
    private List<IonizingRadiationSourceContainDeviceVo> ionizingRadiationSourceContainDeviceVoList;

}
