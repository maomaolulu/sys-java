package may.yuntian.anlianwage.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlianwage.mapper.TaskMapper;
import may.yuntian.anlianwage.entity.TaskEntity;
import may.yuntian.anlianwage.service.TaskService;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-30 15:04:26
 */
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据项目信息获取环境任务信息
     * @param projectId
     * @return
     */
    public TaskEntity getOneByProjectId(Long projectId,Integer taskType){
        TaskEntity taskEntity = baseMapper.selectOne(new QueryWrapper<TaskEntity>().eq("project_id",projectId)
                        .eq("task_type",taskType)
                        .last("limit 1"));
        return taskEntity;
    }

}
