package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.PersonBasicFilesEntity;
import may.yuntian.anlian.entity.PersonHonorCrtificateEntity;
import may.yuntian.anlian.entity.PersonSupervisionRecordsEntity;
import may.yuntian.anlian.entity.PersonTechnicalCertificateEntity;
import may.yuntian.anlian.entity.PersonTrainEntity;
import may.yuntian.anlian.mapper.PersonBasicFilesMapper;
import may.yuntian.anlian.service.PersonBasicFilesService;
import may.yuntian.anlian.service.PersonHonorCrtificateService;
import may.yuntian.anlian.service.PersonSupervisionRecordsService;
import may.yuntian.anlian.service.PersonTechnicalCertificateService;
import may.yuntian.anlian.service.PersonTrainService;
import may.yuntian.common.utils.Constant;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDeptService;
import may.yuntian.modules.sys.service.SysUserService;

import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 技术人员基本档案
 * 业务逻辑层实现类
 *
 * @author ZhangHao
 * @date 2021-06-04
 */
@Service("personBasicFilesService")
public class PersonBasicFilesServiceImpl extends ServiceImpl<PersonBasicFilesMapper, PersonBasicFilesEntity> implements PersonBasicFilesService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private PersonHonorCrtificateService personHonorCrtificateService;
    @Autowired
    private PersonSupervisionRecordsService personSupervisionRecordsService;
    @Autowired
    private PersonTechnicalCertificateService personTechnicalCertificateService;
    @Autowired
    private PersonTrainService personTrainService;

    /**
     * 获取建档人员列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryBookBuildingPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        String jobNum = (String) params.get("jobNum");
        Long createUserId = (Long) params.get("createUserId");
        Integer type = (Integer) params.get("type");
        Long deptIdStr = (Long) params.get("deptId");
        // 支持分流
        String subjection = (String) params.get("subjection");
        IPage<SysUserEntity> page = sysUserService.page(new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(jobNum), "job_num", jobNum)
                        .eq(StringUtils.isNotBlank(subjection), "subjection", subjection)
                        .eq(createUserId != null, "dept_id", deptIdStr)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
                        .eq(createUserId != null, "create_user_id", createUserId)
                        .eq("is_bookbuilding", 1)
                        .eq(type != null, "type", type)
                        //不显示超级管理员的账户
                        .gt(createUserId == null, "create_user_id", Constant.SUPER_ADMIN)
        );
        for (SysUserEntity sysUserEntity : page.getRecords()) {
            //为了安全将密码与加密盐隐藏不返回页面
            sysUserEntity.setPassword(null);
            sysUserEntity.setSalt(null);

            List<PersonBasicFilesEntity> pbfList = this.selectByUserIdList(sysUserEntity.getUserId());
            Integer personBasicFilesCount = pbfList.size();
            List<PersonHonorCrtificateEntity> phcList = personHonorCrtificateService.selectByUserIdList(sysUserEntity.getUserId());
            Integer personHonorCrtificateCount = phcList.size();
            List<PersonSupervisionRecordsEntity> psrList = personSupervisionRecordsService.selectByUserIdList(sysUserEntity.getUserId());
            Integer personSupervisionRecordsCount = psrList.size();
            List<PersonTechnicalCertificateEntity> ptcList = personTechnicalCertificateService.selectByUserIdList(sysUserEntity.getUserId());
            Integer personTechnicalCertificateCount = ptcList.size();
            List<PersonTrainEntity> ptList = personTrainService.selectByUserIdList(sysUserEntity.getUserId());
            Integer personTrainCount = ptList.size();

            sysUserEntity.setPersonBasicFilesCount(personBasicFilesCount);
            sysUserEntity.setPersonHonorCrtificateCount(personHonorCrtificateCount);
            sysUserEntity.setPersonSupervisionRecordsCount(personSupervisionRecordsCount);
            sysUserEntity.setPersonTechnicalCertificateCount(personTechnicalCertificateCount);
            sysUserEntity.setPersonTrainCount(personTrainCount);

            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            if (sysDeptEntity != null) {
                sysUserEntity.setDeptName(sysDeptEntity.getName());
            }
            if (sysUserEntity.getBelongUserid() != null) {
                SysUserEntity sysUser = sysUserService.getById(sysUserEntity.getBelongUserid());
                if (sysUser != null) {
                    sysUserEntity.setBelongUsername(sysUser.getUsername());
                }
            }

        }
        return new PageUtils(page);
    }


    /**
     * 根据userId获取技术人员基本档案数据列表
     */

    public List<PersonBasicFilesEntity> selectByUserIdList(Long userId) {

        List<PersonBasicFilesEntity> personBasicFilesEntityList = baseMapper.selectList(new QueryWrapper<PersonBasicFilesEntity>()
                .eq("user_id", userId)
                .orderByDesc("createtime")
        );
        return personBasicFilesEntityList;
    }


}
