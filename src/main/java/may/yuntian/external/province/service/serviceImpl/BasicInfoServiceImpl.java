package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.ProjectUserEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.ProjectUserService;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.entity.ParticipantTable;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.mapper.BasicInfoMapper;
import may.yuntian.external.province.mapper.ParticipantTableMapper;
import may.yuntian.external.province.mapper.ResultItemMapper;
import may.yuntian.external.province.service.BasicInfoService;
import may.yuntian.external.province.vo.SysUserVo;
import may.yuntian.modules.sys.dao.SysUserDao;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 10:23
 */
@Service("basicInfoService")
public class BasicInfoServiceImpl extends ServiceImpl<BasicInfoMapper, BasicInfo> implements BasicInfoService {

    @Resource
    private BasicInfoMapper basicInfoMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private ProjectUserService projectUserService;
    @Resource
    private ParticipantTableMapper participantMapper;
    @Resource
    private ResultItemMapper resultItemMapper;


    /**
     * 基本信息调用逻辑：0上游检评；1上游评价；2下游
     */
    @Override
    public Map<String, Object> assertBasicInfo(Long projectId) {
        Map<String, Object> map = new HashMap<>(8);
        Integer count = basicInfoMapper.selectCount(Wrappers.lambdaQuery(BasicInfo.class).eq(BasicInfo::getProjectId, projectId));
        ProjectEntity projectEntity = projectMapper.selectOne(Wrappers.lambdaQuery(ProjectEntity.class).select(ProjectEntity::getId, ProjectEntity::getIdentifier, ProjectEntity::getProvince, ProjectEntity::getType).eq(ProjectEntity::getId, projectId));
        ParticipantTableServiceImpl.addElementToMap(map, count, projectEntity);
        // 项目隶属省
        map.put("province", projectEntity.getProvince());
        return map;
    }

    /**
     * 查询企业员工信息
     */
    @Override
    public List<SysUserVo> getUserInfoAll() {
        List<SysUserEntity> userList = sysUserDao.selectList(Wrappers.lambdaQuery(SysUserEntity.class)
                .select(SysUserEntity::getUserId, SysUserEntity::getUsername, SysUserEntity::getJobNum, SysUserEntity::getSubjection, SysUserEntity::getDeptId, SysUserEntity::getStatus, SysUserEntity::getRemark)
                .gt(SysUserEntity::getUserId, 9)
                .notIn(SysUserEntity::getUserId, 618, 619, 620, 621));
        List<SysUserVo> userVoList = new ArrayList<>();
        for (SysUserEntity sysUserEntity : userList) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(sysUserEntity, sysUserVo);
            userVoList.add(sysUserVo);
        }
        return userVoList;
    }

    /**
     * 查询项目基本信息
     */
    @Override
    public BasicInfo getBasicInfo(Long projectId) {
        BasicInfo basicInfo = basicInfoMapper.selectOne(Wrappers.lambdaQuery(BasicInfo.class).eq(BasicInfo::getProjectId, projectId));
        basicInfo.setSysUserVo(new SysUserVo());
        return basicInfo;
    }

    /**
     * 保存or修改项目基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBasicInfo(BasicInfo basicInfo) {
        SysUserVo sysUserVo = basicInfo.getSysUserVo();
        sysUserVo.setProjectId(basicInfo.getProjectId());
        // 数据回填: al_project_user
        if (ObjectUtil.isNotNull(sysUserVo.getUserId())) {
            projectUserService.remove(Wrappers.lambdaQuery(ProjectUserEntity.class).eq(ProjectUserEntity::getProjectId, sysUserVo.getProjectId()).eq(ProjectUserEntity::getTypes, 140));

            ProjectUserEntity projectUserEntity = new ProjectUserEntity();
            projectUserEntity.setProjectId(sysUserVo.getProjectId());
            projectUserEntity.setUserId(sysUserVo.getUserId());
            projectUserEntity.setUsername(sysUserVo.getUsername());
            projectUserEntity.setJobNum(sysUserVo.getJobNum());
            projectUserEntity.setTypes(140);
            projectUserEntity.setCreateTime(DateUtil.dateSecond());
            projectUserService.getBaseMapper().insert(projectUserEntity);
        }
        if (ObjectUtil.isNull(basicInfo.getId())) {
            basicInfo.setCreateTime(DateUtil.dateSecond());
            basicInfo.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        } else {
            basicInfo.setUpdateTime(DateUtil.dateSecond());
            basicInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        }
        ProjectEntity projectEntity = projectMapper.selectOne(new LambdaQueryWrapper<ProjectEntity>().eq(ObjectUtil.isNotNull(basicInfo.getProjectId()), ProjectEntity::getId, basicInfo.getProjectId()));
        basicInfo.setBelongCompany(projectEntity.getCompanyOrder());
        return this.saveOrUpdate(basicInfo);
    }

    /**
     * 业务员-提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int salesmanSubmit(Long projectId) {
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setStatus(1);
        basicInfo.setChargeRejectReason("");
        basicInfo.setQualityControlRejectReason("");
        basicInfo.setUpdateTime(DateUtil.dateSecond());
        basicInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        return basicInfoMapper.update(basicInfo, Wrappers.lambdaUpdate(BasicInfo.class).eq(BasicInfo::getProjectId, projectId));
    }

    /**
     * 业务员提交前：确认数据是否保存
     */
    @Override
    public Map<Object, Object> dataInfoRecord(Long projectId) {
        Map<Object, Object> map = new HashMap<>(10);
        // 基本信息：0未保存，1已保存
        Integer basicInfoCount = basicInfoMapper.selectCount(Wrappers.lambdaQuery(new BasicInfo()).eq(BasicInfo::getProjectId, projectId));
        map.put("basicInfo", basicInfoCount);
        // 人员信息
        Integer personListCount = participantMapper.selectCount(Wrappers.lambdaQuery(ParticipantTable.class).eq(ParticipantTable::getProjectId, projectId));
        if (personListCount > 0) {
            map.put("personList", 1);
        } else {
            map.put("personList", 0);
        }
        // 结果项
        Integer resultCount = resultItemMapper.selectCount(Wrappers.lambdaQuery(ResultItem.class).eq(ResultItem::getProjectId, projectId));
        if (resultCount > 0) {
            map.put("resultItem", 1);
        } else {
            map.put("resultItem", 0);
        }
        return map;
    }


}
