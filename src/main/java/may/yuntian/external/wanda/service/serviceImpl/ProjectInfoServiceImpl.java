package may.yuntian.external.wanda.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.utils.AnlianConfig;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.wanda.constant.OrganizeInfoConstants;
import may.yuntian.external.wanda.constant.UrlConstants;
import may.yuntian.external.wanda.entity.*;
import may.yuntian.external.wanda.mapper.*;
import may.yuntian.external.wanda.service.*;
import may.yuntian.external.wanda.vo.*;
import may.yuntian.modules.sys.dao.SysDeptDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.AlRedisUntil;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-03-06 11:18
 */
@Service("projectInfoService")
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectInfoService {

    /** 响应成功状态码 **/
    private static final String CODE = "200";

    /** code常量 **/
    private static final String JSON_KEY = "code";

    /** 附件类型Map常量 **/
    protected static final Map<Integer, String> AFFIX_TYPE_MAP = new HashMap<>();
    /** 禁止对元素进行增删操作 **/
    private static final Map<Integer, String> AFFIX_UNMODIFIABLE_MAP;
    static {
        AFFIX_TYPE_MAP.put(1, "检测报告");
        AFFIX_TYPE_MAP.put(2, "检评报告");
        AFFIX_TYPE_MAP.put(3, "现状评价报告");
        AFFIX_TYPE_MAP.put(4, "控制效果评价");
        AFFIX_TYPE_MAP.put(5, "检测点分布示意图");
        AFFIX_TYPE_MAP.put(6, "检测委托书");
        AFFIX_UNMODIFIABLE_MAP = Collections.unmodifiableMap(AFFIX_TYPE_MAP);
    }

    /**
     * 自定义危害因素类型List:
     * 1化学，2粉尘，3.生物危害，4.电离辐射-射线装置，5.电离辐射-含源装置；
     * 6.噪声，7.高温，8.紫外辐射，9.工频电场，10.照度，11.控制风速；
     * 12.新风量，13.微小气候，14.手传振动，15.超高频辐射，16.高频电磁场，17.激光辐射，18.微波辐射
     */
    private static final List<Integer> CHEMICAL_WAIT_TYPES = Arrays.asList(1, 2, 3, 4, 5);
    private static final List<Integer> PHYSICAL_HOTSPOT_TYPES = Arrays.asList(6, 7, 8, 9, 10, 11);
    private static final List<Integer> PHYSICAL_NON_HOTSPOT_TYPES = Arrays.asList(12, 13, 14, 15, 16, 17, 18);

    /** 生产环境-服务器文件下载url前缀（GET） **/
    private static final String SERVICE_FILE_DOWNLOAD_URL_PREFIX = "http://47.111.249.220:81/proxyAnlianSysJava/minio/file/download?fileName=";
    /** 生产环境-lab检测报告下载url前缀（GET） **/
    private static final String PROD_LAB_REPORT_DOWNLOAD_URL_PREFIX = "http://47.111.249.220:85/proxyAnlianLabJava/common/minio/download?fileUrl=";
    /** 生产环境-云盘文件下载接口url前缀（GET）**/
    private static final String CLOUD_DISK_FILE_DOWNLOAD_URL_PREFIX = "http://47.111.249.220:82/proxyAnlianPjPython/file_storage/file_export/";

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private AnlianConfig config;
    @Resource
    private ProjectInfoMapper projectInfoMapper;
    @Resource
    private ChemicalDustFactorMapper chemicalDustFactorMapper;
    @Resource
    private ChemicalDustFactorService chemicalDustFactorService;
    @Resource
    private PhysicalHotspotFactorMapper hotspotFactorMapper;
    @Resource
    private PhysicalHotspotFactorService hotspotFactorService;
    @Resource
    private PhysicalNonHotspotFactorMapper nonHotspotFactorMapper;
    @Resource
    private PhysicalNonHotspotFactorService nonHotspotFactorService;
    @Resource
    private FactorDictionaryMapper factorDictionaryMapper;
    @Resource
    private AffixRecordMapper affixRecordMapper;
    @Resource
    private AffixRecordService affixRecordService;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private SysDeptDao sysDeptDao;
    @Resource
    private AlRedisUntil alRedisUntil;

    /**
     * 1.1、基本信息-接口调用：0否（调python）；1是（调Java）
     */
    @Override
    public int getInfoByProjectId(Long projectId) {
        ProjectInfo projectInfo = projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        if (projectInfo == null) return 0;
        return 1;
    }

    /**
     * 1.2、检测结果-接口调用：0否（调python）；1是（调Java）
     */
    @Override
    public int assertResultSave(Long projectId, Integer factorType) {
        if (CHEMICAL_WAIT_TYPES.contains(factorType)) {
            List<ChemicalDustFactor> chemicalDustFactors = chemicalDustFactorMapper.selectList(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId).eq(ChemicalDustFactor::getFactorType, factorType));
            if (CollUtil.isNotEmpty(chemicalDustFactors))  return 1;
        }
        if (PHYSICAL_HOTSPOT_TYPES.contains(factorType)) {
            List<PhysicalHotspotFactor> hotspotFactors = hotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, factorType));
            if (CollUtil.isNotEmpty(hotspotFactors))  return 1;
        }
        if (PHYSICAL_NON_HOTSPOT_TYPES.contains(factorType)) {
            List<PhysicalNonHotspotFactor> nonHotspotFactors = nonHotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalNonHotspotFactor>().eq(PhysicalNonHotspotFactor::getProjectId, projectId).eq(PhysicalNonHotspotFactor::getPhysicalFactorType, factorType));
            if (CollUtil.isNotEmpty(nonHotspotFactors))  return 1;
        }
        return 0;
    }

    /**
     * 1.3、附件-接口调用：0否（调python）；1是（调Java）
     */
    @Override
    public int assertAnnex(Long projectId) {
        Integer count = affixRecordMapper.selectCount(new LambdaQueryWrapper<AffixRecord>().eq(AffixRecord::getProjectId, projectId));
        if (count == 0)  return 0;
        return 1;
    }

    /**
     * 基本信息：查看
     */
    @Override
    public ProjectInfo getBasicInfo(Long projectId) {
        return projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
    }

    /**
     * 基本信息：保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveInfo(ProjectInfo info) {
        if (info.getCreateBy() == null) {
            info.setCreateTime(DateUtils.getNowDate());
            info.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        } else {
            info.setUpdateTime(DateUtils.getNowDate());
            info.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        }
        // 获取并添加项目隶属公司
        ProjectEntity projectEntity = projectMapper.selectOne(new LambdaQueryWrapper<ProjectEntity>().eq(ObjectUtil.isNotNull(info.getProjectId()), ProjectEntity::getId, info.getProjectId()));
        info.setBelongCompany(projectEntity.getCompanyOrder());
        this.saveOrUpdate(info);
        return 1;
    }

    /**
     * 检测结果：查看
     * factorType
     * *  1化学，2粉尘，3.生物危害，4.电离辐射-射线装置，5.电离辐射-含源装置；
     * *  6.噪声，7.高温，8.紫外辐射，9.工频电场，10.照度，11.控制风速；
     * *  12.新风量，13.微小气候，14.手传振动，15.超高频辐射，16.高频电磁场，17.激光辐射，18.微波辐射
     */
    @Override
    public List<?> getDetectionResultData(Long projectId, Integer factorType) {
        // 1、化学，粉尘等检测结果
        if (CHEMICAL_WAIT_TYPES.contains(factorType)) {
            List<ChemicalDustFactor> chemicalDustFactorList = chemicalDustFactorMapper.selectList(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId).eq(ChemicalDustFactor::getFactorType, factorType));
            return getChemicalWaitResultList(factorType, chemicalDustFactorList);
        }
        // 2、物理因素热点数据检测结果
        if (PHYSICAL_HOTSPOT_TYPES.contains(factorType)) {
            List<PhysicalHotspotFactor> physicalHotspotFactors = hotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, factorType));
            return getPhysicalHotspotResultList(factorType, physicalHotspotFactors);
        }
        // 3、物理因素非热点数据检测结果
        if (PHYSICAL_NON_HOTSPOT_TYPES.contains(factorType)) {
            List<PhysicalNonHotspotFactor> physicalNonHotspotFactors = nonHotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalNonHotspotFactor>().eq(PhysicalNonHotspotFactor::getProjectId, projectId).eq(PhysicalNonHotspotFactor::getPhysicalFactorType, factorType));
            return getNonHotspotResultList(factorType, physicalNonHotspotFactors);
        }
        return Collections.emptyList();
    }

    /**
     * 检测结果：查看（物理非热点数据结果）
     */
    private List<?> getNonHotspotResultList(Integer factorType, List<PhysicalNonHotspotFactor> physicalNonHotspotFactors) {
        if (CollUtil.isNotEmpty(physicalNonHotspotFactors)) {
            if (factorType == 12) {
                List<FreshAirVolumeVo> freshAirVolumeVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    FreshAirVolumeVo freshAirVolumeVo = new FreshAirVolumeVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, freshAirVolumeVo);
                    freshAirVolumeVoList.add(freshAirVolumeVo);
                });
                return freshAirVolumeVoList;
            }
            if (factorType == 13) {
                List<MicroclimateVo> microclimateVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    MicroclimateVo microclimateVo = new MicroclimateVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, microclimateVo);
                    microclimateVoList.add(microclimateVo);
                });
                return microclimateVoList;
            }
            if (factorType == 14) {
                List<HandVibrationVo> handVibrationVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    HandVibrationVo handVibrationVo = new HandVibrationVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, handVibrationVo);
                    handVibrationVoList.add(handVibrationVo);
                });
                return handVibrationVoList;
            }
            if (factorType == 15) {
                List<UltraHighFrequencyRadiationVo> ultraHighFrequencyRadiationVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    UltraHighFrequencyRadiationVo ultraHighFrequencyRadiationVo = new UltraHighFrequencyRadiationVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, ultraHighFrequencyRadiationVo);
                    ultraHighFrequencyRadiationVoList.add(ultraHighFrequencyRadiationVo);
                });
                return ultraHighFrequencyRadiationVoList;
            }
            if (factorType == 16) {
                List<HighFrequencyElectromagneticFieldVo> highFrequencyElectromagneticFieldVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    HighFrequencyElectromagneticFieldVo highFrequencyElectromagneticFieldVo = new HighFrequencyElectromagneticFieldVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, highFrequencyElectromagneticFieldVo);
                    highFrequencyElectromagneticFieldVo.setEletricStrength(physicalNonHotspotFactor.getUhfrStrength());
                    highFrequencyElectromagneticFieldVoList.add(highFrequencyElectromagneticFieldVo);
                });
                return highFrequencyElectromagneticFieldVoList;
            }
            if (factorType == 17) {
                List<LaserRadiationVo> laserRadiationVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    LaserRadiationVo laserRadiationVo = new LaserRadiationVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, laserRadiationVo);
                    laserRadiationVoList.add(laserRadiationVo);
                });
                return laserRadiationVoList;
            }
            if (factorType == 18) {
                List<MicrowaveRadiationVo> microwaveRadiationVoList = new ArrayList<>();
                physicalNonHotspotFactors.forEach(physicalNonHotspotFactor -> {
                    MicrowaveRadiationVo microwaveRadiationVo = new MicrowaveRadiationVo();
                    BeanUtils.copyProperties(physicalNonHotspotFactor, microwaveRadiationVo);
                    microwaveRadiationVoList.add(microwaveRadiationVo);
                });
                return microwaveRadiationVoList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 检测结果：查看（物理热点数据结果）
     */
    private List<?> getPhysicalHotspotResultList(Integer factorType, List<PhysicalHotspotFactor> physicalHotspotFactors) {
        if (CollUtil.isNotEmpty(physicalHotspotFactors)) {
            if (factorType == 6) {
                List<NoiseVo> noiseVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    NoiseVo noiseVo = new NoiseVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, noiseVo);
                    noiseVoList.add(noiseVo);
                });
                return noiseVoList;
            }
            if (factorType == 7) {
                List<HighTemperatureVo> highTemperatureVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    HighTemperatureVo highTemperatureVo = new HighTemperatureVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, highTemperatureVo);
                    highTemperatureVoList.add(highTemperatureVo);
                });
                return highTemperatureVoList;
            }
            if (factorType == 8) {
                List<UltravioletLightVo> ultravioletLightVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    UltravioletLightVo ultravioletLightVo = new UltravioletLightVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, ultravioletLightVo);
                    ultravioletLightVoList.add(ultravioletLightVo);
                });
                return ultravioletLightVoList;
            }
            if (factorType == 9) {
                List<PowerFrequencyElectricVo> powerFrequencyElectricVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    PowerFrequencyElectricVo powerFrequencyElectricVo = new PowerFrequencyElectricVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, powerFrequencyElectricVo);
                    powerFrequencyElectricVoList.add(powerFrequencyElectricVo);
                });
                return powerFrequencyElectricVoList;
            }
            if (factorType == 10) {
                List<IlluminationVo> illuminationVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    IlluminationVo illuminationVo = new IlluminationVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, illuminationVo);
                    illuminationVoList.add(illuminationVo);
                });
                return illuminationVoList;
            }
            if (factorType == 11) {
                List<ControlWindSpeedVo> controlWindSpeedVoList = new ArrayList<>();
                physicalHotspotFactors.forEach(physicalHotspotFactor -> {
                    ControlWindSpeedVo controlWindSpeedVo = new ControlWindSpeedVo();
                    BeanUtils.copyProperties(physicalHotspotFactor, controlWindSpeedVo);
                    controlWindSpeedVoList.add(controlWindSpeedVo);
                });
                return controlWindSpeedVoList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 检测结果：查看（化学粉尘等结果）
     */
    private List<?> getChemicalWaitResultList(Integer factorType, List<ChemicalDustFactor> chemicalDustFactorList) {
        if (CollUtil.isNotEmpty(chemicalDustFactorList)) {
            if (factorType == 1) {
                List<ChemicalVo> chemicalVoList = new ArrayList<>();
                chemicalDustFactorList.forEach(chemicalDustFactor -> {
                    ChemicalVo chemicalVo = new ChemicalVo();
                    BeanUtils.copyProperties(chemicalDustFactor, chemicalVo);
                    chemicalVoList.add(chemicalVo);
                });
                return chemicalVoList;
            }
            if (factorType == 2) {
                List<StiveVo> stiveVoList = new ArrayList<>();
                chemicalDustFactorList.forEach(chemicalDustFactor -> {
                    StiveVo stiveVo = new StiveVo();
                    BeanUtils.copyProperties(chemicalDustFactor, stiveVo);
                    stiveVoList.add(stiveVo);
                });
                return stiveVoList;
            }
            if (factorType == 3) {
                List<BiohazardVo> biohazardVoList = new ArrayList<>();
                chemicalDustFactorList.forEach(chemicalDustFactor -> {
                    BiohazardVo biohazardVo = new BiohazardVo();
                    BeanUtils.copyProperties(chemicalDustFactor, biohazardVo);
                    biohazardVoList.add(biohazardVo);
                });
                return biohazardVoList;
            }
            if (factorType == 4) {
                List<IonizingRadiationRayDeviceVo> ionizingRadiationRayDeviceVoList = new ArrayList<>();
                chemicalDustFactorList.forEach(chemicalDustFactor -> {
                    IonizingRadiationRayDeviceVo ionizingRadiationRayDeviceVo = new IonizingRadiationRayDeviceVo();
                    BeanUtils.copyProperties(chemicalDustFactor, ionizingRadiationRayDeviceVo);
                    ionizingRadiationRayDeviceVoList.add(ionizingRadiationRayDeviceVo);
                });
                return ionizingRadiationRayDeviceVoList;
            }
            if (factorType == 5) {
                List<IonizingRadiationSourceContainDeviceVo> ionizingRadiationSourceContainDeviceVoList = new ArrayList<>();
                chemicalDustFactorList.forEach(chemicalDustFactor -> {
                    IonizingRadiationSourceContainDeviceVo containDeviceVo = new IonizingRadiationSourceContainDeviceVo();
                    BeanUtils.copyProperties(chemicalDustFactor, containDeviceVo);
                    containDeviceVo.setRayNum(chemicalDustFactor.getDeviceModel());
                    containDeviceVo.setNo(chemicalDustFactor.getApacity());
                    containDeviceVo.setNuclide(chemicalDustFactor.getPlaceName());
                    containDeviceVo.setInstallPosition(chemicalDustFactor.getCheckCondition());
                    ionizingRadiationSourceContainDeviceVoList.add(containDeviceVo);
                });
                return ionizingRadiationSourceContainDeviceVoList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 检测结果：（逐个）保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveDetectionResultData(BusinessSystemDataVo dataVo) {
        Long projectId = dataVo.getProjectId();
        Integer distinguishFactorType = dataVo.getDistinguishFactorType();
        // 结果保存之前先删除
        deleteAllRecordByProjectId(projectId, distinguishFactorType);
        // 1、化学等检测结果修改保存
        if (CHEMICAL_WAIT_TYPES.contains(distinguishFactorType)) {
            List<ChemicalDustFactor> chemicalDustFactorList = new ArrayList<>();
            chemicalAndSoOnResultData(projectId, dataVo, distinguishFactorType, chemicalDustFactorList);
            if (CollUtil.isNotEmpty(chemicalDustFactorList)) {
                // 页面允许删除结果记录行
                List<Long> idRemoveList = chemicalDustFactorList.stream().filter(chemicalDustFactor -> chemicalDustFactor.getRemove() == 1).map(ChemicalDustFactor::getId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(idRemoveList)) {
                    chemicalDustFactorMapper.deleteBatchIds(idRemoveList);
                }
                // 需要更新的数据list
                List<ChemicalDustFactor> needUpdateList = chemicalDustFactorList.stream().filter(chemicalDustFactor -> chemicalDustFactor.getRemove() == 0).collect(Collectors.toList());
                chemicalDustFactorService.saveOrUpdateBatch(needUpdateList);
            }
        }
        // 2、物理因素热点数据检测结果保存
        if (PHYSICAL_HOTSPOT_TYPES.contains(distinguishFactorType)) {
            List<PhysicalHotspotFactor> physicalHotspotFactorList = new ArrayList<>();
            hotspotFactorResultData(projectId, dataVo, distinguishFactorType, physicalHotspotFactorList);
            if (CollUtil.isNotEmpty(physicalHotspotFactorList)) {
                // 页面允许删除结果记录行
                List<Long> idRemoveList = physicalHotspotFactorList.stream().filter(hotspotFactor -> hotspotFactor.getRemove() == 1).map(PhysicalHotspotFactor::getId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(idRemoveList)) {
                    hotspotFactorMapper.deleteBatchIds(idRemoveList);
                }
                // 需要更新的数据list
                List<PhysicalHotspotFactor> needUpdateList = physicalHotspotFactorList.stream().filter(hotspotFactor -> hotspotFactor.getRemove() == 0).collect(Collectors.toList());
                hotspotFactorService.saveOrUpdateBatch(needUpdateList);
            }
        }
        // 3、物理因素非热点数据检测结果保存
        if (PHYSICAL_NON_HOTSPOT_TYPES.contains(distinguishFactorType)) {
            List<PhysicalNonHotspotFactor> physicalNonHotspotFactorList = new ArrayList<>();
            nonHotspotFactorResultData(projectId, dataVo, distinguishFactorType, physicalNonHotspotFactorList);
            if (CollUtil.isNotEmpty(physicalNonHotspotFactorList)) {
                // 页面允许删除结果记录行
                List<Long> idRemoveList = physicalNonHotspotFactorList.stream().filter(nonHotspotFactor -> nonHotspotFactor.getRemove() == 1).map(PhysicalNonHotspotFactor::getId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(idRemoveList)) {
                    nonHotspotFactorMapper.deleteBatchIds(idRemoveList);
                }
                // 需要更新的数据list
                List<PhysicalNonHotspotFactor> needUpdateList = physicalNonHotspotFactorList.stream().filter(nonHotspotFactor -> nonHotspotFactor.getRemove() == 0).collect(Collectors.toList());
                nonHotspotFactorService.saveOrUpdateBatch(needUpdateList);
            }
        }
        return 1;
    }

    /**
     * 项目负责人：提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StringBuilder submit(Long projectId) {
        // 提交前判断：车间-岗位相同，劳动定员是否相等
        List<ResultPartInfoVo> checkWorkNumA = chemicalDustFactorMapper.checkWorkNum(projectId);
        List<ResultPartInfoVo> checkWorkNumB = hotspotFactorMapper.checkWorkNum(projectId);
        List<ResultPartInfoVo> checkWorkNumC = nonHotspotFactorMapper.checkWorkNum(projectId);
        List<ResultPartInfoVo> resultPartInfoVos = new ArrayList<>();
        resultPartInfoVos.addAll(checkWorkNumA);
        resultPartInfoVos.addAll(checkWorkNumB);
        resultPartInfoVos.addAll(checkWorkNumC);
        HashSet<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(resultPartInfoVos)) {
            for (int i = 0, len = resultPartInfoVos.size(); i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (resultPartInfoVos.get(i).getWorkUnitName().equals(resultPartInfoVos.get(j).getWorkUnitName())
                            && resultPartInfoVos.get(i).getWorkJobName().equals(resultPartInfoVos.get(j).getWorkJobName())
                            && (!resultPartInfoVos.get(i).getWorkNum().equals(resultPartInfoVos.get(j).getWorkNum())) ) {
                        // 车间岗位相同，劳动定员不同
                        set.add(resultPartInfoVos.get(i).getWorkUnitName() + "-" + resultPartInfoVos.get(i).getWorkJobName());
                    }
                }
            }
            if (!set.isEmpty())  throw new RRException("相同车间岗位，劳动定员不同，请核对：" + set.toString());
        }
        // 车间岗位劳动定员：统计人数
        StringBuilder builder = new StringBuilder();
        int labourCountNum = resultPartInfoVos.stream().distinct().mapToInt(resultPartInfoVo -> Integer.parseInt(resultPartInfoVo.getWorkNum())).sum();
        Integer victimsNum = projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().select(ProjectInfo::getVictimsNum).eq(ProjectInfo::getProjectId, projectId)).getVictimsNum();
        if (victimsNum != null && labourCountNum != victimsNum) {
            builder.append("但你所申报的接害总人数与实际报告中的接害总人数不同!");
        }
        ProjectInfo info = new ProjectInfo();
        info.setUpdateTime(DateUtils.getNowDate());
        info.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        info.setNodeStatus(1);
        info.setChargeReject("");
        info.setQualityControlReject("");
        info.setLabourCountNum(labourCountNum);
        projectInfoMapper.update(info, new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        return builder;
    }

    /**
     * 主管：驳回
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int chargeReject(RejectVo rejectVo) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setUpdateTime(DateUtils.getNowDate());
        projectInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        projectInfo.setNodeStatus(2);
        projectInfo.setChargeReject(rejectVo.getReason());
        return projectInfoMapper.update(projectInfo, new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, rejectVo.getProjectId()));
    }

    /**
     * 主管：提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int chargeRefer(Long projectId) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setUpdateTime(DateUtils.getNowDate());
        projectInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        projectInfo.setNodeStatus(3);
        return projectInfoMapper.update(projectInfo, new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
    }

    /**
     * 质控：驳回（跳过主管直接到项目负责人）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int qualityReject(RejectVo rejectVo) {
        return projectInfoMapper.updateByQualityReject(rejectVo.getReason(), ShiroUtils.getUserEntity().getUsername(), DateUtils.getNowDate(), rejectVo.getProjectId());
    }

    /**
     * 质控推送: 基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pushBasicInfo(Long projectId) {
        StringBuilder builder = new StringBuilder();
        ProjectInfo info = projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        return projectInfoPush(info, info.getBelongThirdPrjId(), builder);
    }

    /**
     * 质控推送: 附件、结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String qualityPush(HttpServletRequest request, Long projectId) {
        // 项目所有数据信息
        BusinessSystemDataVo dataVo = selectAllDataInfo(projectId, 2);
        List<AffixRecord> affixRecordList = dataVo.getAffixRecordList();
        if (CollUtil.isEmpty(affixRecordList)) {
            throw new RRException("缺少项目附件!");
        }
        Map<String, Object> paramMap = new HashMap<>(40);
        // 记录推送失败接口返回的异常信息
        StringBuilder builder = new StringBuilder();
        ProjectInfo projectInfo = dataVo.getProjectInfo();
        String belongThirdPrjId = projectInfo.getBelongThirdPrjId();
        String belongCompany = projectInfo.getBelongCompany();

        // 2、附件推送
        affixPush(request, affixRecordList, belongThirdPrjId, builder, belongCompany);

        // 3.1、化学结果上传
        List<ChemicalVo> chemicalVoList = dataVo.getChemicalVoList();
        if (CollUtil.isNotEmpty(chemicalVoList)) {
            chemicalVoList.forEach(chemicalVo -> {
                chemicalVo.setBelongThirdPrjId(belongThirdPrjId);
                if (StringUtils.isNotNull(chemicalVo.getReductionFactorType()) && chemicalVo.getReductionFactorType() == 0) {
                    chemicalVo.setReductionFactorType(null);
                }
            });
            invokeDetectionResultInterface(belongThirdPrjId, chemicalVoList, paramMap, 1, builder, belongCompany);
        }
        // 3.2、粉尘结果上传
        List<StiveVo> dustVoList = dataVo.getStiveVoList();
        if (CollUtil.isNotEmpty(dustVoList)) {
            dustVoList.forEach(dustVo -> {
                dustVo.setBelongThirdPrjId(belongThirdPrjId);
                if (StringUtils.isNotNull(dustVo.getTotalReductionFactorType()) && dustVo.getTotalReductionFactorType() == 0) {
                    dustVo.setTotalReductionFactorType(null);
                }
                if (StringUtils.isNotNull(dustVo.getRespirableReductionFactorType()) && dustVo.getRespirableReductionFactorType() == 0) {
                    dustVo.setRespirableReductionFactorType(null);
                }
            });
            invokeDetectionResultInterface(belongThirdPrjId, dustVoList, paramMap, 2, builder, belongCompany);
        }
        // 3.3、噪声结果上传
        List<NoiseVo> noiseVoList = dataVo.getNoiseVoList();
        if (CollUtil.isNotEmpty(noiseVoList)) {
            noiseVoList.forEach(noiseVo -> noiseVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, noiseVoList, paramMap, 3, builder, belongCompany);
        }
        // 3.4、高温结果上传
        List<HighTemperatureVo> highTemperatureVoList = dataVo.getHighTemperatureVoList();
        if (CollUtil.isNotEmpty(highTemperatureVoList)) {
            highTemperatureVoList.forEach(highTemperatureVo -> highTemperatureVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, highTemperatureVoList, paramMap, 4, builder, belongCompany);
        }
        // 3.5、工频电场结果上传
        List<PowerFrequencyElectricVo> powerFrequencyElectricVoList = dataVo.getPowerFrequencyElectricVoList();
        if (CollUtil.isNotEmpty(powerFrequencyElectricVoList)) {
            powerFrequencyElectricVoList.forEach(powerFrequencyElectricVo -> powerFrequencyElectricVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, powerFrequencyElectricVoList, paramMap, 5, builder, belongCompany);
        }
        // 3.6、照度结果上传
        List<IlluminationVo> illuminationVoList = dataVo.getIlluminationVoList();
        if (CollUtil.isNotEmpty(illuminationVoList)) {
            illuminationVoList.forEach(illuminationVo -> illuminationVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, illuminationVoList, paramMap, 6, builder, belongCompany);
        }
        // 3.7、控制风速结果上传
        List<ControlWindSpeedVo> controlWindSpeedVoList = dataVo.getControlWindSpeedVoList();
        if (CollUtil.isNotEmpty(controlWindSpeedVoList)) {
            controlWindSpeedVoList.forEach(controlWindSpeedVo -> controlWindSpeedVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, controlWindSpeedVoList, paramMap, 7, builder, belongCompany);
        }
        // 3.8、新风量结果上传
        List<FreshAirVolumeVo> freshAirVolumeVoList = dataVo.getFreshAirVolumeVoList();
        if (CollUtil.isNotEmpty(freshAirVolumeVoList)) {
            freshAirVolumeVoList.forEach(freshAirVolumeVo -> freshAirVolumeVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, freshAirVolumeVoList, paramMap, 8, builder, belongCompany);
        }
        // 3.9、微小气候结果上传
        List<MicroclimateVo> microclimateVoList = dataVo.getMicroclimateVoList();
        if (CollUtil.isNotEmpty(microclimateVoList)) {
            microclimateVoList.forEach(microclimateVo -> microclimateVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, microclimateVoList, paramMap, 9, builder, belongCompany);
        }
        // 3.10、紫外辐射结果上传
        List<UltravioletLightVo> ultravioletLightVoList = dataVo.getUltravioletLightVoList();
        if (CollUtil.isNotEmpty(ultravioletLightVoList)) {
            ultravioletLightVoList.forEach(ultravioletLightVo -> ultravioletLightVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, ultravioletLightVoList, paramMap, 10, builder, belongCompany);
        }
        // 3.11、手传振动结果上传
        List<HandVibrationVo> handVibrationVoList = dataVo.getHandVibrationVoList();
        if (CollUtil.isNotEmpty(handVibrationVoList)) {
            handVibrationVoList.forEach(handVibrationVo -> handVibrationVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, handVibrationVoList, paramMap, 11, builder, belongCompany);
        }
        // 3.12、超高频辐射结果上传
        List<UltraHighFrequencyRadiationVo> ultraHighFrequencyRadiationVoList = dataVo.getUltraHighFrequencyRadiationVoList();
        if (CollUtil.isNotEmpty(ultraHighFrequencyRadiationVoList)) {
            ultraHighFrequencyRadiationVoList.forEach(ultraHighFrequencyRadiationVo -> ultraHighFrequencyRadiationVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, ultraHighFrequencyRadiationVoList, paramMap, 12, builder, belongCompany);
        }
        // 3.13、高频电磁场结果上传
        List<HighFrequencyElectromagneticFieldVo> highFrequencyElectromagneticFieldVoList = dataVo.getHighFrequencyElectromagneticFieldVoList();
        if (CollUtil.isNotEmpty(highFrequencyElectromagneticFieldVoList)) {
            highFrequencyElectromagneticFieldVoList.forEach(highFrequencyElectromagneticFieldVo -> highFrequencyElectromagneticFieldVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, highFrequencyElectromagneticFieldVoList, paramMap, 13, builder, belongCompany);
        }
        // 3.14、激光辐射结果上传
        List<LaserRadiationVo> laserRadiationVoList = dataVo.getLaserRadiationVoList();
        if (CollUtil.isNotEmpty(laserRadiationVoList)) {
            laserRadiationVoList.forEach(laserRadiationVo -> laserRadiationVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, laserRadiationVoList, paramMap, 14, builder, belongCompany);
        }
        // 3.15、微波辐射结果上传
        List<MicrowaveRadiationVo> microwaveRadiationVoList = dataVo.getMicrowaveRadiationVoList();
        if (CollUtil.isNotEmpty(microwaveRadiationVoList)) {
            microwaveRadiationVoList.forEach(microclimateVo -> microclimateVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, microwaveRadiationVoList, paramMap, 15, builder, belongCompany);
        }
        // 3.16、生物危害结果上传
        List<BiohazardVo> biohazardVoList = dataVo.getBiohazardVoList();
        if (CollUtil.isNotEmpty(biohazardVoList)) {
            biohazardVoList.forEach(biohazardVo -> {
                biohazardVo.setBelongThirdPrjId(belongThirdPrjId);
                if (biohazardVo.getReductionFactorType() == 0) {
                    biohazardVo.setReductionFactorType(null);
                }
            });
            invokeDetectionResultInterface(belongThirdPrjId, biohazardVoList, paramMap, 16, builder, belongCompany);
        }
        // 3.17、电离辐射-射线装置结果上传
        List<IonizingRadiationRayDeviceVo> ionizingRadiationRayDeviceVoList = dataVo.getIonizingRadiationRayDeviceVoList();
        if (CollUtil.isNotEmpty(ionizingRadiationRayDeviceVoList)) {
            ionizingRadiationRayDeviceVoList.forEach(ionizingRadiationRayDeviceVo -> ionizingRadiationRayDeviceVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, ionizingRadiationRayDeviceVoList, paramMap, 17, builder, belongCompany);
        }
        // 3.18、电离辐射-含源装置结果上传
        List<IonizingRadiationSourceContainDeviceVo> containDeviceVoList = dataVo.getIonizingRadiationSourceContainDeviceVoList();
        if (CollUtil.isNotEmpty(containDeviceVoList)) {
            containDeviceVoList.forEach(containDeviceVo -> containDeviceVo.setBelongThirdPrjId(belongThirdPrjId));
            invokeDetectionResultInterface(belongThirdPrjId, containDeviceVoList, paramMap, 18, builder, belongCompany);
        }
        // 调用对方接口无异常时，更改推送节点状态
        if (builder.toString().length() == 0) {
            projectInfo.setNodeStatus(5);
            projectInfo.setUpdateTime(DateUtil.dateSecond());
            projectInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
            projectInfo.setPushTime(DateUtil.dateSecond());
            projectInfoMapper.updateById(projectInfo);
        }
        return builder.toString();
    }

    /**
     * 质控-附件推送
     */
    public void affixPush(HttpServletRequest request, List<AffixRecord> affixRecordList, String belongThirdPrjId, StringBuilder builder, String belongCompany) {
        String token = request.getHeader("token");
        for (AffixRecord affixRecord : affixRecordList) {
            if (!affixRecord.getFileName().contains(".")) {
                affixRecord.setFileName(affixRecord.getFileName().concat(".").concat(affixRecord.getFileSuffix()));
            }
        }
        Map<Integer, List<AffixRecord>> typeMap = affixRecordList.stream().collect(Collectors.groupingBy(AffixRecord::getAffixType));
        typeMap.forEach((affixType, valueList) -> {
            // 请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            // 请求参数
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            if("杭州安联".equals(belongCompany)) {
                paramMap.add("orgCode", config.orgCode);
                paramMap.add("orgName", config.orgName);
                paramMap.add("orgKey", config.orgKey);
            }
            if ("宁波安联".equals(belongCompany)) {
                paramMap.add("orgCode", OrganizeInfoConstants.ORG_CODE_NB);
                paramMap.add("orgName", OrganizeInfoConstants.ORG_NAME_NB);
                paramMap.add("orgKey", OrganizeInfoConstants.ORG_KEY_NB);
            }
            if ("嘉兴安联".equals(belongCompany)) {
                paramMap.add("orgCode", OrganizeInfoConstants.ORG_CODE_JX);
                paramMap.add("orgName", OrganizeInfoConstants.ORG_NAME_JX);
                paramMap.add("orgKey", OrganizeInfoConstants.ORG_KEY_JX);
            }
            // 其它安联等子公司待定！！！
            paramMap.add("belongThirdPrjId", Long.valueOf(belongThirdPrjId));
            // 参数-附件类型
            paramMap.add("affixType", affixType);
            // 参数-files：value值并不会造成覆盖！
            valueList.forEach(affixRecord -> {
                ByteArrayResource byteArrayResource = null;
                // 1.云盘接口下载
                if (affixRecord.getStorageType() == 1) {
                    if (affixRecord.getArchiveFileId() == 0)  throw new RRException("缺少文件id，无法从云盘获取文件！");
                    byte[] bytes = HttpRequest.get(CLOUD_DISK_FILE_DOWNLOAD_URL_PREFIX + affixRecord.getArchiveFileId()).header("token", token).execute().bodyBytes(); // 生产环境prod
                    if (ArrayUtil.isEmpty(bytes))  throw new RRException("该项目附件存在空文件，请重新上传");
                    byteArrayResource = new ByteArrayResource(bytes){
                        @Override
                        public String getFilename() throws IllegalStateException {
                            return affixRecord.getFileName();
                        }
                    };
                }
                // 2.sys接口下载
                if (affixRecord.getStorageType() == 2) {
                    if (StringUtils.isBlank(affixRecord.getFileUrl())) throw new RRException("文件相对路径为空，无法从服务器获取文件！");
                    byte[] bytes = HttpRequest.get(SERVICE_FILE_DOWNLOAD_URL_PREFIX + affixRecord.getFileUrl()).header("token", token).execute().bodyBytes();  // 生产环境prod
                    if (ArrayUtil.isEmpty(bytes))  throw new RRException("该项目附件存在空文件，请重新上传！");
                    byteArrayResource = new ByteArrayResource(bytes){
                        @Override
                        public String getFilename() throws IllegalStateException {
                            return affixRecord.getFileName();
                        }
                    };
                }
                // 3.python接口下载文件！！
                // 4.lab接口下载
                if (affixRecord.getStorageType() == 4) {
                    if (StringUtils.isBlank(affixRecord.getFileUrl())) throw new RRException("文件相对路径为空，无法从服务器获取文件！");
                    byte[] bytes = HttpRequest.get(PROD_LAB_REPORT_DOWNLOAD_URL_PREFIX + affixRecord.getFileUrl()).header("token", token).execute().bodyBytes(); // lab生产环境prod
                    if (ArrayUtil.isEmpty(bytes))  throw new RRException("该项目附件存在空文件，请重新上传。");
                    byteArrayResource = new ByteArrayResource(bytes){
                        @Override
                        public String getFilename() throws IllegalStateException {
                            return affixRecord.getFileName();
                        }
                    };
                }
                paramMap.add("files", byteArrayResource);
            });
            // 使用RestTemplate调用第三方接口
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(config.wandaPath + UrlConstants.AFFIX_UPLOAD_URL_SUFFIX, httpEntity, String.class);
            JSONObject parseObject = JSON.parseObject(responseEntity.getBody());
            if (!CODE.equals(parseObject.get(JSON_KEY))) {
                // 组装wanda附件上传接口异常信息
                builder.append("[").append(parseObject.getString("message")).append("]").append(";");
            }
        });
    }

    /**
     * 检测结果：删除
     */
    private void deleteAllRecordByProjectId(Long projectId, Integer factorType) {
        if (CHEMICAL_WAIT_TYPES.contains(factorType)) {
            chemicalDustFactorMapper.delete(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId).eq(ChemicalDustFactor::getFactorType, factorType));
        }
        if (PHYSICAL_HOTSPOT_TYPES.contains(factorType)) {
            hotspotFactorMapper.delete(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, factorType));
        }
        if (PHYSICAL_NON_HOTSPOT_TYPES.contains(factorType)) {
            nonHotspotFactorMapper.delete(new LambdaQueryWrapper<PhysicalNonHotspotFactor>().eq(PhysicalNonHotspotFactor::getProjectId, projectId).eq(PhysicalNonHotspotFactor::getPhysicalFactorType, factorType));
        }
    }

    /**
     * 主管、质控：查看
     */
    @Override
    public BusinessSystemDataVo selectAllDataInfo(Long projectId, Integer viewer) {
        BusinessSystemDataVo dataVo = new BusinessSystemDataVo();
        ProjectInfo info = projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        if (info == null) {
            throw new RRException("该项目基本信息不存在，请联系管理员！");
        }
        // 1、基本信息
        dataVo.setProjectInfo(info);

        // 2、附件列表
        List<AffixRecord> annexList = affixRecordMapper.selectList(new LambdaQueryWrapper<AffixRecord>().eq(AffixRecord::getProjectId, projectId));
        dataVo.setAffixRecordList(annexList);

        // 3、检测结果
        // 3.1：化学粉尘等结果
        List<ChemicalDustFactor> chemicalDustFactors = chemicalDustFactorMapper.selectList(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(chemicalDustFactors)) {
            viewFirstResultDataInfo(dataVo, chemicalDustFactors);
        }
        // 3.2：物理热点数据
        List<PhysicalHotspotFactor> hotspotFactors = hotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(hotspotFactors)) {
            viewSecondResultDataInfo(dataVo, hotspotFactors);
        }
        // 3.3：物理非热点数据
        List<PhysicalNonHotspotFactor> nonHotspotFactors = nonHotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalNonHotspotFactor>().eq(PhysicalNonHotspotFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(nonHotspotFactors)) {
            viewThirdResultDataInfo(dataVo, nonHotspotFactors);
        }

        return dataVo;
    }

    /**
     * 主管、质控：列表
     */
    @Override
    public List<ProjectInfo> selectWarehouseList(ProjectInfo info) {
        String company = ShiroUtils.getUserEntity().getSubjection();
        Set<Long> idSet = new HashSet<>();
        List<SysDeptEntity> sysDeptList = sysDeptDao.selectList(new LambdaQueryWrapper<SysDeptEntity>().eq(SysDeptEntity::getDelFlag, 0));
        Long deptId = sysDeptList.stream().filter(sysDeptEntity -> company.equals(sysDeptEntity.getName())).findFirst().get().getDeptId();
        idSet.add(deptId);
        Map<Long, List<SysDeptEntity>> groupMap = sysDeptList.stream().collect(Collectors.groupingBy(SysDeptEntity::getParentId));
        List<SysDeptEntity> sysDeptEntityList = groupMap.get(deptId);
        getDeptIdSet(idSet, groupMap, sysDeptEntityList);

        QueryWrapper<ProjectInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(info.getProjectCode()), "wpi.project_code", info.getProjectCode());
        wrapper.like(StringUtils.isNotBlank(info.getComName()), "wpi.com_name", info.getComName());
        wrapper.like(StringUtils.isNotBlank(info.getCharge()), "ap.charge", info.getCharge());
        wrapper.eq(StringUtils.isNotNull(info.getNodeStatus()), "wpi.node_status", info.getNodeStatus());
        wrapper.ge(StrUtil.isNotBlank(info.getStartTime()), "wpi.push_time", info.getStartTime());
        wrapper.le(StrUtil.isNotBlank(info.getEndTime()), "wpi.push_time", info.getEndTime());
        wrapper.in(info.getViewer() == 2, "wpi.node_status", 3, 5);
        // 数据分流：主管-根据al_project.dept_id去匹配当前登录公司所有部门id;   质控-根据belong_company过滤。
        wrapper.in(info.getViewer() == 1, "ap.dept_id", idSet);
        wrapper.eq(info.getViewer() == 2, "wpi.belong_company", company);
        // 系统数据隔离：检评2：定期检测；评价3：现状评价，4：控制效果评价
        if (info.getSystemFlag() != null) {
            wrapper.eq(info.getSystemFlag() == 1, "wpi.check_type", 2);
            wrapper.in(info.getSystemFlag() == 2, "wpi.check_type", 3, 4);
        }
        // 质控：状态升序、推送时间倒序
        wrapper.orderByAsc(info.getViewer() == 2, "wpi.node_status");
        wrapper.orderByDesc(info.getViewer() == 2, "wpi.push_time");
        pageUtil2.startPage();
        return projectInfoMapper.getProjectInfoList(wrapper);
    }

    /**
     *  递归获取当前登录公司所有deptId
     */
    private void getDeptIdSet(Set<Long> idSet, Map<Long, List<SysDeptEntity>> groupMap, List<SysDeptEntity> deptEntities) {
        if (CollUtil.isNotEmpty(deptEntities)) {
            for (SysDeptEntity deptEntity : deptEntities) {
                idSet.add(deptEntity.getDeptId());
                List<SysDeptEntity> sysDeptEntities = groupMap.get(deptEntity.getDeptId());
                getDeptIdSet(idSet, groupMap, sysDeptEntities);
            }
        }
    }

    /**
     * 万达-危害因素列表
     */
    @Override
    public List<FactorDictionary> factorList(FactorDictionary factorDictionary) {
        return factorDictionaryMapper.selectList(new LambdaQueryWrapper<FactorDictionary>().like(StringUtils.isNotBlank(factorDictionary.getFactorName()), FactorDictionary::getFactorName, factorDictionary.getFactorName()));
    }

    /**
     * 数据保存记录
     */
    @Override
    public Map<Object, Object> selectInfoRecordList(Long projectId) {
        Map<Object, Object> map = new HashMap<>();
        // 基本信息：0未同步，1已同步
        map.put("projectInfo", 1);
        for (int i = 1; i <= 18; i++) {
            map.put(i, 0);
        }
        ProjectInfo info = projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().eq("project_id", projectId));
        if (info == null) {
            map.put("projectInfo", 0);
        }
        // 该项目附件记录
        Integer count = affixRecordMapper.selectCount(new LambdaQueryWrapper<AffixRecord>().eq(AffixRecord::getProjectId, projectId));
        map.put("affixRecord", count);
        // 各检测结果记录
        List<ChemicalDustFactor> chemicalWaitFactorList = chemicalDustFactorMapper.selectList(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(chemicalWaitFactorList)) {
            Map<Integer, List<ChemicalDustFactor>> typeMap = chemicalWaitFactorList.stream().collect(Collectors.groupingBy(ChemicalDustFactor::getFactorType));
            typeMap.forEach((type, valueList) -> map.put(type, valueList.size()));
        }
        List<PhysicalHotspotFactor> hotspotFactorList = hotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(hotspotFactorList)) {
            Map<Integer, List<PhysicalHotspotFactor>> typeMap = hotspotFactorList.stream().collect(Collectors.groupingBy(PhysicalHotspotFactor::getPhysicalFactorType));
            typeMap.forEach((type, valueList) -> map.put(type, valueList.size()));
        }
        List<PhysicalNonHotspotFactor> nonHotspotFactorList = nonHotspotFactorMapper.selectList(new LambdaQueryWrapper<PhysicalNonHotspotFactor>().eq(PhysicalNonHotspotFactor::getProjectId, projectId));
        if (CollUtil.isNotEmpty(nonHotspotFactorList)) {
            Map<Integer, List<PhysicalNonHotspotFactor>> typeMap = nonHotspotFactorList.stream().collect(Collectors.groupingBy(PhysicalNonHotspotFactor::getPhysicalFactorType));
            typeMap.forEach((type, valueList) -> map.put(type, valueList.size()));
        }
        return map;
    }

    /**
     * 附件-保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAnnex(AnnexVo annexVo) {
        List<AffixRecord> annexRecordList = annexVo.getAffixRecordList();
        if (CollUtil.isEmpty(annexRecordList)) {
            throw new RRException("请先上传附件！");
        }
        annexRecordList.forEach(affixRecord -> {
            if (StrUtil.isBlank(affixRecord.getFileUrl())) {
                throw new RRException("文件信息缺失，请重新上传【" + AFFIX_UNMODIFIABLE_MAP.get(affixRecord.getAffixType()) + "!】");
            }
            // 去除多余的文件名后缀
            String[] strArr = affixRecord.getFileName().split("\\.");
            affixRecord.setFileName(strArr[0].concat(".").concat(strArr[strArr.length -1]));
        });
        // 先删除，并清空缓存
        affixRecordService.remove(new LambdaQueryWrapper<AffixRecord>().eq(AffixRecord::getProjectId, annexVo.getProjectId()));
        annexRecordList.forEach(affixRecord -> {
            alRedisUntil.hdel("anlian-java", affixRecord.getFileUrl());
            affixRecord.setProjectId(annexVo.getProjectId());
            affixRecord.setCreateTime(DateUtils.getNowDate());
            affixRecord.setCreateBy(ShiroUtils.getUserEntity().getUsername());
            if (affixRecord.getAffixType() == 5 && !(affixRecord.getFileName().contains("."))) {
                affixRecord.setFileName(affixRecord.getFileName().concat(".").concat(affixRecord.getFileSuffix()));
            }
        });
        // 再保存
        return affixRecordService.saveBatch(annexRecordList);
    }

    /**
     * 附件：列表
     */
    @Override
    public List<AffixRecord> dataEcho(Long projectId) {
        return affixRecordMapper.selectList(new LambdaQueryWrapper<AffixRecord>().eq(AffixRecord::getProjectId, projectId));
    }

    /**
     * 主管、质控：查看结果③数据信息
     */
    private void viewThirdResultDataInfo(BusinessSystemDataVo dataVo, List<PhysicalNonHotspotFactor> nonHotspotFactors) {
        Map<Integer, List<PhysicalNonHotspotFactor>> typeMap = nonHotspotFactors.stream().collect(Collectors.groupingBy(PhysicalNonHotspotFactor::getPhysicalFactorType));
        typeMap.forEach((factorType, valueList) -> {
            if (factorType == 12) {
                List<FreshAirVolumeVo> freshAirVolumeVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    FreshAirVolumeVo freshAirVolumeVo = new FreshAirVolumeVo();
                    BeanUtils.copyProperties(value, freshAirVolumeVo);
                    freshAirVolumeVoList.add(freshAirVolumeVo);
                });
                dataVo.setFreshAirVolumeVoList(freshAirVolumeVoList);
            } else if (factorType == 13) {
                List<MicroclimateVo> microclimateVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    MicroclimateVo microclimateVo = new MicroclimateVo();
                    BeanUtils.copyProperties(value, microclimateVo);
                    microclimateVoList.add(microclimateVo);
                });
                dataVo.setMicroclimateVoList(microclimateVoList);
            } else if (factorType == 14) {
                List<HandVibrationVo> handVibrationVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    HandVibrationVo handVibrationVo = new HandVibrationVo();
                    BeanUtils.copyProperties(value, handVibrationVo);
                    handVibrationVoList.add(handVibrationVo);
                });
                dataVo.setHandVibrationVoList(handVibrationVoList);
            } else if (factorType == 15) {
                List<UltraHighFrequencyRadiationVo> ultraHighFrequencyRadiationVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    UltraHighFrequencyRadiationVo ultraHighFrequencyRadiationVo = new UltraHighFrequencyRadiationVo();
                    BeanUtils.copyProperties(value, ultraHighFrequencyRadiationVo);
                    ultraHighFrequencyRadiationVoList.add(ultraHighFrequencyRadiationVo);
                });
                dataVo.setUltraHighFrequencyRadiationVoList(ultraHighFrequencyRadiationVoList);
            } else if (factorType == 16) {
                List<HighFrequencyElectromagneticFieldVo> highFrequencyElectromagneticFieldVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    HighFrequencyElectromagneticFieldVo highFrequencyElectromagneticFieldVo = new HighFrequencyElectromagneticFieldVo();
                    BeanUtils.copyProperties(value, highFrequencyElectromagneticFieldVo);
                    highFrequencyElectromagneticFieldVo.setEletricStrength(value.getUhfrStrength());
                    highFrequencyElectromagneticFieldVoList.add(highFrequencyElectromagneticFieldVo);
                });
                dataVo.setHighFrequencyElectromagneticFieldVoList(highFrequencyElectromagneticFieldVoList);
            } else if (factorType == 17) {
                List<LaserRadiationVo> laserRadiationVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    LaserRadiationVo laserRadiationVo = new LaserRadiationVo();
                    BeanUtils.copyProperties(value, laserRadiationVo);
                    laserRadiationVoList.add(laserRadiationVo);
                });
                dataVo.setLaserRadiationVoList(laserRadiationVoList);
            } else {
                List<MicrowaveRadiationVo> microwaveRadiationVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    MicrowaveRadiationVo microwaveRadiationVo = new MicrowaveRadiationVo();
                    BeanUtils.copyProperties(value, microwaveRadiationVo);
                    microwaveRadiationVoList.add(microwaveRadiationVo);
                });
                dataVo.setMicrowaveRadiationVoList(microwaveRadiationVoList);
            }
        });
    }

    /**
     * 主管、质控：查看结果②数据信息
     */
    private void viewSecondResultDataInfo(BusinessSystemDataVo dataVo, List<PhysicalHotspotFactor> hotspotFactors) {
        Map<Integer, List<PhysicalHotspotFactor>> typeMap = hotspotFactors.stream().collect(Collectors.groupingBy(PhysicalHotspotFactor::getPhysicalFactorType));
        typeMap.forEach((factorType, valueList) -> {
            if (factorType == 6) {
                List<NoiseVo> noiseVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    NoiseVo noiseVo = new NoiseVo();
                    BeanUtils.copyProperties(value, noiseVo);
                    noiseVoList.add(noiseVo);
                });
                dataVo.setNoiseVoList(noiseVoList);
            } else if (factorType == 7) {
                List<HighTemperatureVo> highTemperatureVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    HighTemperatureVo highTemperatureVo = new HighTemperatureVo();
                    BeanUtils.copyProperties(value, highTemperatureVo);
                    highTemperatureVoList.add(highTemperatureVo);
                });
                dataVo.setHighTemperatureVoList(highTemperatureVoList);
            } else if (factorType == 8) {
                List<UltravioletLightVo> ultravioletLightVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    UltravioletLightVo ultravioletLightVo = new UltravioletLightVo();
                    BeanUtils.copyProperties(value, ultravioletLightVo);
                    ultravioletLightVoList.add(ultravioletLightVo);
                });
                dataVo.setUltravioletLightVoList(ultravioletLightVoList);
            } else if (factorType == 9) {
                List<PowerFrequencyElectricVo> powerFrequencyElectricVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    PowerFrequencyElectricVo powerFrequencyElectricVo = new PowerFrequencyElectricVo();
                    BeanUtils.copyProperties(value, powerFrequencyElectricVo);
                    powerFrequencyElectricVoList.add(powerFrequencyElectricVo);
                });
                dataVo.setPowerFrequencyElectricVoList(powerFrequencyElectricVoList);
            } else if (factorType == 10) {
                List<IlluminationVo> illuminationVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    IlluminationVo illuminationVo = new IlluminationVo();
                    BeanUtils.copyProperties(value, illuminationVo);
                    illuminationVoList.add(illuminationVo);
                });
                dataVo.setIlluminationVoList(illuminationVoList);
            } else {
                List<ControlWindSpeedVo> controlWindSpeedVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    ControlWindSpeedVo controlWindSpeedVo = new ControlWindSpeedVo();
                    BeanUtils.copyProperties(value, controlWindSpeedVo);
                    controlWindSpeedVoList.add(controlWindSpeedVo);
                });
                dataVo.setControlWindSpeedVoList(controlWindSpeedVoList);
            }
        });
    }

    /**
     * 主管、质控：查看结果①数据信息
     */
    private void viewFirstResultDataInfo(BusinessSystemDataVo dataVo, List<ChemicalDustFactor> chemicalDustFactors) {
        Map<Integer, List<ChemicalDustFactor>> typeMap = chemicalDustFactors.stream().collect(Collectors.groupingBy(ChemicalDustFactor::getFactorType));
        typeMap.forEach((factorType, valueList) -> {
            if (factorType == 1) {
                List<ChemicalVo> chemicalVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    ChemicalVo chemicalVo = new ChemicalVo();
                    BeanUtils.copyProperties(value, chemicalVo);
                    chemicalVoList.add(chemicalVo);
                });
                dataVo.setChemicalVoList(chemicalVoList);
            } else if (factorType == 2) {
                List<StiveVo> stiveVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    StiveVo stiveVo = new StiveVo();
                    BeanUtils.copyProperties(value, stiveVo);
                    stiveVoList.add(stiveVo);
                });
                dataVo.setStiveVoList(stiveVoList);
            } else if (factorType == 3) {
                List<BiohazardVo> biohazardVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    BiohazardVo biohazardVo = new BiohazardVo();
                    BeanUtils.copyProperties(value, biohazardVo);
                    biohazardVoList.add(biohazardVo);
                });
                dataVo.setBiohazardVoList(biohazardVoList);
            } else if (factorType == 4) {
                List<IonizingRadiationRayDeviceVo> ionizingRadiationRayDeviceVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    IonizingRadiationRayDeviceVo ionizingRadiationRayDeviceVo = new IonizingRadiationRayDeviceVo();
                    BeanUtils.copyProperties(value, ionizingRadiationRayDeviceVo);
                    ionizingRadiationRayDeviceVoList.add(ionizingRadiationRayDeviceVo);
                });
                dataVo.setIonizingRadiationRayDeviceVoList(ionizingRadiationRayDeviceVoList);
            } else {
                List<IonizingRadiationSourceContainDeviceVo> containDeviceVoList = new ArrayList<>();
                valueList.forEach(value -> {
                    IonizingRadiationSourceContainDeviceVo containDeviceVo = new IonizingRadiationSourceContainDeviceVo();
                    BeanUtils.copyProperties(value, containDeviceVo);
                    containDeviceVo.setRayNum(value.getDeviceModel());
                    containDeviceVo.setNo(value.getApacity());
                    containDeviceVo.setNuclide(value.getPlaceName());
                    containDeviceVo.setInstallPosition(value.getCheckCondition());
                    containDeviceVoList.add(containDeviceVo);
                });
                dataVo.setIonizingRadiationSourceContainDeviceVoList(containDeviceVoList);
            }
        });
    }

    /**
     * 质控：c、wanda-检测结果接口上传
     */
    private void invokeDetectionResultInterface(String belongThirdPrjId, List<?> voList, Map<String, Object> paramMap, Integer factorType, StringBuilder builder, String belongCompany) {
        // map通过参数传递，进来先清空集合，再进行put操作。
        paramMap.clear();
        if("杭州安联".equals(belongCompany)) {
            paramMap.put("orgCode", config.orgCode);
            paramMap.put("orgName", config.orgName);
            paramMap.put("orgKey", config.orgKey);
        }
        if ("宁波安联".equals(belongCompany)) {
            paramMap.put("orgCode", OrganizeInfoConstants.ORG_CODE_NB);
            paramMap.put("orgName", OrganizeInfoConstants.ORG_NAME_NB);
            paramMap.put("orgKey", OrganizeInfoConstants.ORG_KEY_NB);
        }
        if ("嘉兴安联".equals(belongCompany)) {
            paramMap.put("orgCode", OrganizeInfoConstants.ORG_CODE_JX);
            paramMap.put("orgName", OrganizeInfoConstants.ORG_NAME_JX);
            paramMap.put("orgKey", OrganizeInfoConstants.ORG_KEY_JX);
        }
        paramMap.put("belongThirdPrjId", belongThirdPrjId);
        paramMap.put("children", voList);
        // map转JSON字符串
        String requestBody = JSON.toJSONString(paramMap);
        // 拼接wanda结果接口请求路径
        String urlString = config.wandaPath;
        switch (factorType) {
            case 1:
                urlString = urlString.concat(UrlConstants.CHEMISTRY_URL_SUFFIX);
                break;
            case 2:
                urlString = urlString.concat(UrlConstants.DUST_URL_SUFFIX);
                break;
            case 3:
                urlString = urlString.concat(UrlConstants.NOISE_URL_SUFFIX);
                break;
            case 4:
                urlString = urlString.concat(UrlConstants.TEMPERATURE_URL_SUFFIX);
                break;
            case 5:
                urlString = urlString.concat(UrlConstants.POWER_ELECTRIC_URL_SUFFIX);
                break;
            case 6:
                urlString = urlString.concat(UrlConstants.ILLUMINATION_URL_SUFFIX);
                break;
            case 7:
                urlString = urlString.concat(UrlConstants.WIND_SPEED_URL_SUFFIX);
                break;
            case 8:
                urlString = urlString.concat(UrlConstants.FRESH_AIR_URL_SUFFIX);
                break;
            case 9:
                urlString = urlString.concat(UrlConstants.MICROCLIMATE_URL_SUFFIX);
                break;
            case 10:
                urlString = urlString.concat(UrlConstants.ULTRAVIOLET_LIGHT_URL_SUFFIX);
                break;
            case 11:
                urlString = urlString.concat(UrlConstants.HAND_VIBRATION_URL_SUFFIX);
                break;
            case 12:
                urlString = urlString.concat(UrlConstants.HIGH_FREQUENCY_URL_SUFFIX);
                break;
            case 13:
                urlString = urlString.concat(UrlConstants.ELECTROMAGNETIC_URL_SUFFIX);
                break;
            case 14:
                urlString = urlString.concat(UrlConstants.LASER_URL_SUFFIX);
                break;
            case 15:
                urlString = urlString.concat(UrlConstants.MICROWAVE_URL_SUFFIX);
                break;
            case 16:
                urlString = urlString.concat(UrlConstants.BIOHAZARD_URL_SUFFIX);
                break;
            case 17:
                urlString = urlString.concat(UrlConstants.RAY_DEVICE_URL_SUFFIX);
                break;
            case 18:
                urlString = urlString.concat(UrlConstants.CONTAIN_DEVICE_URL_SUFFIX);
                break;
            default:
                break;
        }
        // 调用wanda-结果上传接口
        String responseBody = HttpUtil.post(urlString, requestBody);
        JSONObject parseObject = JSON.parseObject(responseBody);
        if (!CODE.equals(parseObject.get(JSON_KEY))) {
            // 组装异常信息
            builder.append("[").append(parseObject.getString("message")).append(parseObject.getString("body")).append("]").append(";");
            log.error("危害因素类型：" + factorType + "，结果上传接口异常信息：" + builder.toString());
        }
    }

    /**
     * 质控：a、项目基本信息推送
     */
    public String projectInfoPush(ProjectInfo projectInfo, String belongThirdPrjId, StringBuilder builder) {
        String requestBody;
        JSONObject parseObject;
        Map<String, Object> paramMap = getProjectInfoRequestBodyParams(projectInfo);
        if (StringUtils.isBlank(belongThirdPrjId)) {
            // map转JSON字符串
            requestBody = JSON.toJSONString(paramMap);
            // 调wanda-项目基本信息新增接口
            String responseBody = HttpUtil.post(config.wandaPath + UrlConstants.PROJECT_INFO_ADD_URL_SUFFIX, requestBody);
            parseObject = JSON.parseObject(responseBody);
            if (CODE.equals(parseObject.get(JSON_KEY))) {
                // 得到对方同步成功的项目id
                belongThirdPrjId = parseObject.getString("body");
                projectInfo.setBelongThirdPrjId(belongThirdPrjId);
                projectInfoMapper.updateById(projectInfo);
            } else {
                builder.append("[").append(parseObject.getString("message")).append(parseObject.getString("body")).append("];");
            }
        } else {
            paramMap.put("id", belongThirdPrjId);
            requestBody = JSON.toJSONString(paramMap);
            // 调wanda-项目基本信息编辑接口
            String responseBody = HttpUtil.post(config.wandaPath + UrlConstants.PROJECT_INFO_EDIT_URL_SUFFIX, requestBody);
            parseObject = JSON.parseObject(responseBody);
            if (CODE.equals(parseObject.get(JSON_KEY))) {
                projectInfo.setUpdateTime(DateUtils.getNowDate());
                projectInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                this.saveOrUpdate(projectInfo);
            } else {
                builder.append("[").append(parseObject.getString("message")).append("];");
            }
        }
        // 抛出第三方接口调用异常信息：
        if (builder.toString().length() > 0) {
            throw new RRException("第三方接口异常信息：" + builder);
        }
        return belongThirdPrjId;
    }

    /**
     * wanda-项目基本信息请求体封装
     */
    public Map<String, Object> getProjectInfoRequestBodyParams(ProjectInfo info) {
        Map<String, Object> paramMap = new HashMap<>(30);
        if("杭州安联".equals(info.getBelongCompany())) {
            paramMap.put("orgCode", config.orgCode);
            paramMap.put("orgName", config.orgName);
            paramMap.put("orgKey", config.orgKey);
            paramMap.put("belongThirdOrgId", config.orgId);
            paramMap.put("registerName", config.legalName);
        }
        if ("宁波安联".equals(info.getBelongCompany())) {
            paramMap.put("orgCode", OrganizeInfoConstants.ORG_CODE_NB);
            paramMap.put("orgName", OrganizeInfoConstants.ORG_NAME_NB);
            paramMap.put("orgKey", OrganizeInfoConstants.ORG_KEY_NB);
            paramMap.put("belongThirdOrgId", OrganizeInfoConstants.ORG_ID_NB);
            paramMap.put("registerName", OrganizeInfoConstants.LEGAL_NAME_NB);
        }
        if ("嘉兴安联".equals(info.getBelongCompany())) {
            paramMap.put("orgCode", OrganizeInfoConstants.ORG_CODE_JX);
            paramMap.put("orgName", OrganizeInfoConstants.ORG_NAME_JX);
            paramMap.put("orgKey", OrganizeInfoConstants.ORG_KEY_JX);
            paramMap.put("belongThirdOrgId", OrganizeInfoConstants.ORG_ID_JX);
            paramMap.put("registerName", OrganizeInfoConstants.LEGAL_NAME_JX);
        }
        paramMap.put("checkType", info.getCheckType());
        paramMap.put("comCode", info.getComCode());
        paramMap.put("comName", info.getComName());
        paramMap.put("projectCode", info.getProjectCode());
        paramMap.put("projectName", info.getProjectName());
        paramMap.put("projectArea", info.getProjectArea());
        paramMap.put("projectAreaCode", info.getProjectAreaCode());
        paramMap.put("projectAddress", info.getProjectAddress());
        paramMap.put("checkDateStart", info.getCheckDateStart());
        paramMap.put("checkDateEnd", info.getCheckDateEnd());
        paramMap.put("targetStandardId", 2);
        paramMap.put("reportDate", info.getReportDate());
        paramMap.put("riskLevel", info.getRiskLevel());
        paramMap.put("registerTime", info.getRegisterTime());
        return paramMap;
    }

    /**
     * 方法抽取：1、化学等结果数据处理
     */
    private void chemicalAndSoOnResultData(Long projectId, BusinessSystemDataVo dataVo, Integer distinguishFactorType, List<ChemicalDustFactor> chemicalDustFactors) {
        List<?> voList;
        if (distinguishFactorType == 1) {
            voList = dataVo.getChemicalVoList();
        } else if (distinguishFactorType == 2) {
            voList = dataVo.getStiveVoList();
        } else if (distinguishFactorType == 3) {
            voList = dataVo.getBiohazardVoList();
        } else if (distinguishFactorType == 4) {
            voList = dataVo.getIonizingRadiationRayDeviceVoList();
        } else {
            voList = dataVo.getIonizingRadiationSourceContainDeviceVoList();
        }
        if (CollUtil.isNotEmpty(voList)) {
            voList.forEach(vo -> {
                ChemicalDustFactor chemicalDustFactor = new ChemicalDustFactor();
                BeanUtils.copyProperties(vo, chemicalDustFactor);
                chemicalDustFactor.setProjectId(projectId);
                chemicalDustFactor.setFactorType(distinguishFactorType);
                chemicalDustFactor.setUpdateTime(DateUtils.getNowDate());
                chemicalDustFactor.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                if (distinguishFactorType == 5) {
                    IonizingRadiationSourceContainDeviceVo containDeviceVo = (IonizingRadiationSourceContainDeviceVo) vo;
                    chemicalDustFactor.setDeviceModel(containDeviceVo.getRayNum());
                    chemicalDustFactor.setApacity(containDeviceVo.getNo());
                    chemicalDustFactor.setPlaceName(containDeviceVo.getNuclide());
                    chemicalDustFactor.setCheckCondition(containDeviceVo.getInstallPosition());
                }
                chemicalDustFactors.add(chemicalDustFactor);
            });
        }
    }

    /**
     * 方法抽取：2、物理热点数据结果处理
     */
    private void hotspotFactorResultData(Long projectId, BusinessSystemDataVo dataVo, Integer distinguishFactorType, List<PhysicalHotspotFactor> physicalHotspotFactors) {
        List<?> voList;
        if (distinguishFactorType == 6) {
            voList = dataVo.getNoiseVoList();
        } else if (distinguishFactorType == 7) {
            voList = dataVo.getHighTemperatureVoList();
        } else if (distinguishFactorType == 8) {
            voList = dataVo.getUltravioletLightVoList();
        } else if (distinguishFactorType == 9) {
            voList = dataVo.getPowerFrequencyElectricVoList();
        } else if (distinguishFactorType == 10) {
            voList = dataVo.getIlluminationVoList();
        } else {
            voList = dataVo.getControlWindSpeedVoList();
        }
        if (CollUtil.isNotEmpty(voList)) {
            voList.forEach(vo -> {
                PhysicalHotspotFactor hotspotFactor = new PhysicalHotspotFactor();
                BeanUtils.copyProperties(vo, hotspotFactor);
                // 评价-控制风速6：特殊处理（跟前端对下字段specialDataJson）
                if (distinguishFactorType == 11) {
                    // 待比较的限值
                    String limitValue = null;
                    // 检测结果值
                    String checkValue = ((ControlWindSpeedVo) vo).getCheckValue();
                    BigDecimal result = new BigDecimal(checkValue);
                    String specialDataJson = ((ControlWindSpeedVo) vo).getSpecialDataJson();
                    hotspotFactor.setSpecialDataJson(specialDataJson);
                    JSONObject jsonObject = JSON.parseObject(specialDataJson);
                    // 0: 未选择（由前端限值必填x），1：化学气体，2：粉尘
                    int limitType = Integer.parseInt(jsonObject.getString("limitType"));
                    if (limitType == 1) {
                        limitValue = jsonObject.getString("toxicGas");
                    } else if (limitType == 2) {
                        limitValue = jsonObject.getString("dust");
                    }
                    if (StringUtils.isBlank(limitValue)) {
                        throw new RRException("请下拉选择风速类型！");
                    }
                    if (result.compareTo(new BigDecimal(limitValue)) > 0) {
                        hotspotFactor.setJudgeResult(3);
                    } else {
                        hotspotFactor.setJudgeResult(2);
                    }
                }
                hotspotFactor.setProjectId(projectId);
                hotspotFactor.setPhysicalFactorType(distinguishFactorType);
                hotspotFactor.setUpdateTime(DateUtils.getNowDate());
                hotspotFactor.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                physicalHotspotFactors.add(hotspotFactor);
            });
        }
    }

    /**
     * 方法抽取：3、物理非热点数据结果处理
     */
    private void nonHotspotFactorResultData(Long projectId, BusinessSystemDataVo dataVo, Integer distinguishFactorType, List<PhysicalNonHotspotFactor> physicalNonHotspotFactors) {
        List<?> voList;
        if (distinguishFactorType == 12) {
            voList = dataVo.getFreshAirVolumeVoList();
        } else if (distinguishFactorType == 13) {
            voList = dataVo.getMicroclimateVoList();
        } else if (distinguishFactorType == 14) {
            voList = dataVo.getHandVibrationVoList();
        } else if (distinguishFactorType == 15) {
            voList = dataVo.getUltraHighFrequencyRadiationVoList();
        } else if (distinguishFactorType == 16) {
            voList = dataVo.getHighFrequencyElectromagneticFieldVoList();
        } else if (distinguishFactorType == 17) {
            voList = dataVo.getLaserRadiationVoList();
        } else {
            voList = dataVo.getMicrowaveRadiationVoList();
        }
        if (CollUtil.isNotEmpty(voList)) {
            voList.forEach(vo -> {
                PhysicalNonHotspotFactor nonHotspotFactor = new PhysicalNonHotspotFactor();
                BeanUtils.copyProperties(vo, nonHotspotFactor);
                if (distinguishFactorType == 16) {
                    nonHotspotFactor.setUhfrStrength(((HighFrequencyElectromagneticFieldVo) vo).getEletricStrength());
                }
                nonHotspotFactor.setProjectId(projectId);
                nonHotspotFactor.setPhysicalFactorType(distinguishFactorType);
                nonHotspotFactor.setUpdateTime(DateUtils.getNowDate());
                nonHotspotFactor.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                physicalNonHotspotFactors.add(nonHotspotFactor);
            });
        }
    }

}
