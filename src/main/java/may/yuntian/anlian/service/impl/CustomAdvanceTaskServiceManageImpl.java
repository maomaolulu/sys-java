package may.yuntian.anlian.service.impl;

import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.mapper.CustomAdvanceTaskManageMapper;
import may.yuntian.anlian.service.CompanyService;
import may.yuntian.anlian.service.CustomAdvanceTaskManageService;
import may.yuntian.external.oa.dto.CustomAdvanceTaskDto;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys_v2.utils.RedisCache;
import may.yuntian.untils.AdvanceTaskCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:46
 * @Version 1.0
 * @Description 跟进任务
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomAdvanceTaskServiceManageImpl extends ServiceImpl<CustomAdvanceTaskManageMapper, CustomAdvanceTaskEntity> implements CustomAdvanceTaskManageService {
    private final CustomAdvanceTaskManageMapper customAdvanceTaskManageMapper;
    private final RedisCache redisService;
    private final CompanyService companyService;

    public CustomAdvanceTaskServiceManageImpl(CustomAdvanceTaskManageMapper customAdvanceTaskManageMapper,
                                              RedisCache redisService,
                                              CompanyService companyService) {
        this.customAdvanceTaskManageMapper = customAdvanceTaskManageMapper;
        this.redisService = redisService;
        this.companyService = companyService;
    }

    /**
     * 更新任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int modify(CustomAdvanceTaskEntity customAdvanceTask) {
        if (customAdvanceTask.getAdvanceResult() != null) {
            customAdvanceTask.setBusinessStatus(5);
            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(customAdvanceTaskManageMapper.selectById(customAdvanceTask.getId()).getCompanyId());
            companyEntity.setIfHasFinished(1);
            companyService.updateById(companyEntity);
        }
        customAdvanceTask.setUpdateTime(new Date());
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        customAdvanceTask.setUpdateBy(sysUserEntity.getUsername());
        return customAdvanceTaskManageMapper.updateById(customAdvanceTask);
    }

    /**
     * 人员替换
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int replaceUserBatch(CustomAdvanceTaskEntity customAdvanceTask) {
        Long advanceId = customAdvanceTask.getAdvanceId();
        List<CustomAdvanceTaskEntity> tasksList = customAdvanceTask.getTasksList();
        for (CustomAdvanceTaskEntity task : tasksList) {
            CustomAdvanceTaskEntity advanceTask = new CustomAdvanceTaskEntity();
            advanceTask.setId(task.getId());
            advanceTask.setAdvanceId(advanceId);
            advanceTask.setUpdateTime(new Date());
            SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            advanceTask.setUpdateBy(sysUserEntity.getUsername());
            if (customAdvanceTaskManageMapper.updateById(advanceTask) == 0) {
                throw new StatefulException("替换人员发生异常");
            }
        }
        return 1;
    }

    /**
     * 新建任务
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int add(CustomAdvanceTaskEntity customAdvanceTask) {
        customAdvanceTask.setCreateTime(new Date());
        customAdvanceTask.setTaskCode(AdvanceTaskCodeUtil.getCode(redisService));
        return customAdvanceTaskManageMapper.insert(customAdvanceTask);
    }

    /**
     * 任务分配
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int allocationBatch(CustomAdvanceTaskEntity customAdvanceTask) {
        Long advanceId = customAdvanceTask.getAdvanceId();
        List<CustomAdvanceTaskEntity> tasksList = customAdvanceTask.getTasksList();
        for (CustomAdvanceTaskEntity cus : tasksList) {
            cus.setAdvanceId(advanceId);
            cus.setBusinessStatus(1);
            cus.setCreateTime(new Date());
            SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            cus.setCreateBy(sysUserEntity.getUsername());
            cus.setTaskCode(AdvanceTaskCodeUtil.getCode(redisService));
            if (customAdvanceTaskManageMapper.insert(cus) == 0) {
                throw new StatefulException("创建新任务失败");
            }
        }
        return 1;
    }

    @Override
    public List<CustomAdvanceTaskDto> listTasks(CustomAdvanceTaskDto dto) {
        QueryWrapper<CustomAdvanceTaskDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(dto.getAdvanceId() != null, "t1.advance_id", dto.getAdvanceId())
                .eq(StrUtil.isNotBlank(dto.getTaskCode()), "t1.task_code", dto.getTaskCode())
                .like(StrUtil.isNotBlank(dto.getCompany()), "t2.company", dto.getCompany())
                .like(StrUtil.isNotBlank(dto.getUsername()), "t4.username", dto.getUsername())
                .eq(dto.getAdvanceResult() != null, "t1.advance_result", dto.getAdvanceResult())
                .like(StrUtil.isNotBlank(dto.getContact()), "t2.contact", dto.getContact())
                .eq(StrUtil.isNotBlank(dto.getMobile()), "t2.mobile", dto.getMobile())
                .eq(dto.getBusinessStatus() != null, "t1.business_status", dto.getBusinessStatus());
        String subjection = dto.getSubjection();
        if (StrUtil.isNotBlank(subjection)) {
            String dataBelong = dto.getDataBelong();
            if (StrUtil.isNotBlank(dataBelong) && !dataBelong.equals(subjection)) {
                return new ArrayList<>();
            }
            queryWrapper.eq("t2.data_belong", subjection);
        } else {
            queryWrapper.eq(StrUtil.isNotBlank(dto.getDataBelong()), "t2.data_belong", dto.getDataBelong());
        }
        if (dto.getAdvanceFirstTimeStart() != null && dto.getAdvanceFirstTimeEnd() != null) {
            queryWrapper.between("t1.advance_first_time", dto.getAdvanceFirstTimeStart(), dto.getAdvanceFirstTimeEnd());
        }
        if (dto.getAdvanceLastTimeStart() != null && dto.getAdvanceLastTimeEnd() != null) {
            queryWrapper.between("t1.advance_last_time", dto.getAdvanceLastTimeStart(), dto.getAdvanceLastTimeEnd());
        }
        queryWrapper.orderByDesc("t1.task_code");
        return baseMapper.taskList(queryWrapper);
    }

    /**
     * 公司是否关联跟进任务
     *
     * @param id 公司ID
     * @return 结果
     */
    @Override
    public boolean existTaskByCompany(Long id) {
        QueryWrapper<CustomAdvanceTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", id);
        return customAdvanceTaskManageMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean releaseCompany(CustomAdvanceTaskDto dto) {
        CustomAdvanceTaskEntity taskUpdate = new CustomAdvanceTaskEntity();
        taskUpdate.setId(dto.getId());
        taskUpdate.setBusinessStatus(5);
        CompanyEntity companyUpdate = new CompanyEntity();
        companyUpdate.setId(dto.getCompanyId());
        companyUpdate.setIfHasFinished(1);
        return companyService.updateById(companyUpdate) && this.updateById(taskUpdate);
    }

//    /**
//     * 查询我的任务列表
//     *
//     * @param customAdvanceTaskDto
//     * @return 集合
//     */
//    @Override
//    public List<CustomAdvanceTaskDto> getMyTasks(CustomAdvanceTaskDto customAdvanceTaskDto) {
//        QueryWrapper<CustomAdvanceTaskDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("t1.advance_id", customAdvanceTaskDto.getAdvanceId());
//        if (StrUtil.isNotBlank(customAdvanceTaskDto.getKeyword())) {
//            queryWrapper.eq(customAdvanceTaskDto.getBusinessStatus() != null, "t1.business_status", customAdvanceTaskDto.getBusinessStatus())
//                    .or()
//                    .like("t2.company", customAdvanceTaskDto.getKeyword())
//                    .eq("t1.task_code", customAdvanceTaskDto.getKeyword());
//
//        } else {
//            queryWrapper.eq(customAdvanceTaskDto.getBusinessStatus() != null, "t1.business_status", customAdvanceTaskDto.getBusinessStatus());
//            queryWrapper.like(StrUtil.isNotBlank(customAdvanceTaskDto.getCompany()), "t2.company", customAdvanceTaskDto.getCompany());
//            queryWrapper.eq(StrUtil.isNotBlank(customAdvanceTaskDto.getTaskCode()), "t1.task_code", customAdvanceTaskDto.getTaskCode());
//        }
//        return customAdvanceTaskMapper.taskList(queryWrapper);
//    }

//    /**
//     * 获取联系人信息
//     *
//     * @param companyId 公司ID
//     * @return 集合
//     */
//    @Override
//    public List<CompanyContactEntity> getCompanyContactInfo(Long companyId) {
//        return customAdvanceTaskMapper.selectCompanyContactInfo(companyId);
//    }

//    /**
//     * 查询我的任务单个
//     *
//     * @param customAdvanceTaskDto 查询信息
//     * @return 任务跟进信息
//     */
//    @Override
//    public CustomAdvanceTaskDto getMyTaskOne(CustomAdvanceTaskDto customAdvanceTaskDto) {
//        CustomAdvanceTaskEntity customAdvanceTaskEntity = customAdvanceTaskMapper.selectById(customAdvanceTaskDto.getId());
//        QueryWrapper<CustomAdvanceTaskDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("t1.advance_id", customAdvanceTaskEntity.getAdvanceId());
//        queryWrapper.eq("t1.id", customAdvanceTaskDto.getId());
//        List<CustomAdvanceTaskDto> list = customAdvanceTaskMapper.taskList(queryWrapper);
//        if (list.size() > 0) {
//            CustomAdvanceTaskDto taskDto = list.get(0);
//            taskDto.setCompanyContactList(customAdvanceTaskMapper.selectCompanyContactInfo(customAdvanceTaskEntity.getCompanyId()));
//            return taskDto;
//        }
//        return new CustomAdvanceTaskDto();
//    }

//    /**
//     * 构建查询条件
//     */
//    private QueryWrapper<Object> getwrapper(CustomAdvanceDto dto) {
//        QueryWrapper<Object> wrapper = new QueryWrapper<>();
//        wrapper.eq(dto.getUserId() != null, "cat.advance_id", dto.getUserId());
//        wrapper.eq(dto.getTaskCode() != null, "cat.task_code", dto.getTaskCode());
//        wrapper.eq(dto.getCustomerOrder() != null, "cc.customer_order", dto.getCustomerOrder());
//        wrapper.eq(dto.getEnterpriseName() != null, "cc.enterprise_name", dto.getEnterpriseName());
//        wrapper.eq(dto.getContacterName() != null, "ccs.contacter_name", dto.getContacterName());
//        wrapper.eq(dto.getMobilePhone() != null, "ccs.mobile_phone", dto.getMobilePhone());
//        wrapper.eq(dto.getBusinessStatus() != null, "cat.business_status", dto.getBusinessStatus());
//        wrapper.eq(dto.getAdvanceResult() != null, "cat.advance_result", dto.getAdvanceResult());
//        // 查询时间段
//        wrapper.ge(dto.getAdvanceFirstTimeStart() != null, "cat.advance_first_time", dto.getAdvanceFirstTimeStart());
//        wrapper.le(dto.getAdvanceFirstTimeEnd() != null, "cat.advance_first_time", dto.getAdvanceFirstTimeEnd());
//        wrapper.ge(dto.getAdvanceLastTimeStart() != null, "cat.advance_last_time", dto.getAdvanceLastTimeStart());
//        wrapper.le(dto.getAdvanceLastTimeEnd() != null, "cat.advance_last_time", dto.getAdvanceLastTimeEnd());
//        return wrapper;
//    }
}
