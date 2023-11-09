package may.yuntian.anlian.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.mapper.CustomAdvanceRecordManageMapper;
import may.yuntian.anlian.mapper.CustomAdvanceTaskManageMapper;
import may.yuntian.anlian.service.CustomAdvanceRecordManageService;
import may.yuntian.anlian.service.CustomAdvanceTaskManageService;
import may.yuntian.anlian.vo.HistoryAdvanceVo;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.external.oa.mapper.CustomAdvanceRecordMapper;
import may.yuntian.minio.utils.DateUtils;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.untils.AlRedisUntil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:49
 * @Version 1.0
 * @Description 跟进记录
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomAdvanceRecordServiceManageImpl extends ServiceImpl<CustomAdvanceRecordMapper, CustomAdvanceRecordEntity> implements CustomAdvanceRecordManageService {
    private final CustomAdvanceRecordManageMapper customAdvanceRecordManageMapper;
    private final CustomAdvanceTaskManageService customAdvanceTaskManageService;
    private final CustomAdvanceTaskManageMapper customAdvanceTaskManageMapper;

    @Autowired
    private AlRedisUntil alRedisUntil;

    public CustomAdvanceRecordServiceManageImpl(CustomAdvanceRecordManageMapper customAdvanceRecordManageMapper,
                                                CustomAdvanceTaskManageService customAdvanceTaskManageService,
                                                CustomAdvanceTaskManageMapper customAdvanceTaskManageMapper) {
        this.customAdvanceRecordManageMapper = customAdvanceRecordManageMapper;
        this.customAdvanceTaskManageService = customAdvanceTaskManageService;
        this.customAdvanceTaskManageMapper = customAdvanceTaskManageMapper;
    }

    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    @Override
    public int add(CustomAdvanceRecordEntity customAdvanceRecord) {
        customAdvanceRecord.setCreateTime(new Date());
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        customAdvanceRecord.setCreateBy(sysUserEntity.getUsername());
        // 处理图片
        List<String> pathList = customAdvanceRecord.getPathList();
        if (CollUtil.isNotEmpty(pathList)) {
            String join = String.join(",", pathList);
            customAdvanceRecord.setPath(join);
        }
        int i = customAdvanceRecordManageMapper.insert(customAdvanceRecord);
        if (i != 0) {
            // 更新任务最新跟进记录
            CustomAdvanceTaskEntity taskEntity = customAdvanceTaskManageMapper.selectById(customAdvanceRecord.getTaskId());
            if (taskEntity == null || taskEntity.getId() == null) {
                throw new StatefulException("获取任务信息异常");
            }
            CustomAdvanceTaskEntity advanceTask = new CustomAdvanceTaskEntity();
            if (taskEntity.getAdvanceFirstTime() == null) {
                advanceTask.setAdvanceFirstTime(new Date());
            }
            advanceTask.setBusinessStatus(2);
            advanceTask.setId(customAdvanceRecord.getTaskId());
            advanceTask.setLastRecordId(customAdvanceRecord.getId());
            advanceTask.setAdvanceLastTime(new Date());
            if (!customAdvanceTaskManageService.updateById(advanceTask)) {
                throw new StatefulException("更新任务最新记录异常");
            }
            // 删除图片缓存
            if (CollUtil.isNotEmpty(pathList)) {
                for (String path : pathList) {
                    alRedisUntil.hdel("anlian-java", path);
                }
            }
        }
        return i;
    }

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 任务ID
     * @return 集合
     */
    @Override
    public List<CustomAdvanceRecordEntity> getDetail(CustomAdvanceRecordEntity customAdvanceRecord) {
        QueryWrapper<CustomAdvanceRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", customAdvanceRecord.getTaskId())
                .eq(StrUtil.isNotBlank(customAdvanceRecord.getAdvancePattern()), "advance_pattern", customAdvanceRecord.getAdvancePattern())
                .eq(customAdvanceRecord.getAdvanceDate() != null, "advance_date", customAdvanceRecord.getAdvanceDate());

        queryWrapper.orderByDesc("create_time");
        List<CustomAdvanceRecordEntity> details = customAdvanceRecordManageMapper.getDetails(queryWrapper);
        if (CollUtil.isNotEmpty(details)) {
            for (CustomAdvanceRecordEntity customAdvanceRecordEntity : details) {
                String path = customAdvanceRecordEntity.getPath();
                if (StrUtil.isNotBlank(path)) {
                    customAdvanceRecordEntity.setPathList(Arrays.asList(path.split(",")));
                }
            }
        }
        return details;
    }

    /**
     * 修改跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    @Override
    public int updateAdvanceInfo(CustomAdvanceRecordEntity customAdvanceRecord) {
        customAdvanceRecord.setUpdateTime(new Date());
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        customAdvanceRecord.setUpdateBy(sysUserEntity.getUsername());
        // 处理图片
        List<String> pathList = customAdvanceRecord.getPathList();
        if (CollUtil.isNotEmpty(pathList)) {
            String join = String.join(",", pathList);
            customAdvanceRecord.setPath(join);
        }
        int i = customAdvanceRecordManageMapper.updateById(customAdvanceRecord);
        if (i > 0) {
            List<String> delList = customAdvanceRecord.getDelList();
            if (CollUtil.isNotEmpty(delList)) {
                for (String del : delList) {
                    alRedisUntil.hset("anlian-java", del, DateUtils.getDate());
                }
            }
        }
        return i;
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录主键ID
     * @return result
     */
    @Override
    public int delete(Long id) {
        return customAdvanceRecordManageMapper.deleteById(id);
    }

    /**
     * 历史跟进
     */
    @Override
    public List<HistoryAdvanceVo> historyAdvance(Long companyId) {
        List<HistoryAdvanceVo> historyAdvanceVoList = customAdvanceRecordManageMapper.selectHistoryAdvanceByCompanyId(companyId);
        if (CollUtil.isNotEmpty(historyAdvanceVoList)) {
            for (HistoryAdvanceVo historyAdvanceVo : historyAdvanceVoList) {
                if (historyAdvanceVo.getIfHasFinished() == 0) {
                    historyAdvanceVo.setCustomerOrder(historyAdvanceVo.getPersonBelong());
                } else {
                    historyAdvanceVo.setCustomerOrder(historyAdvanceVo.getDataBelong());
                }
            }
        }
        return historyAdvanceVoList;
    }

}
