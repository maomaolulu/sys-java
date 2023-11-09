package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlianwage.entity.TaskEntity;

import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-30 15:04:26
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据项目信息获取环境任务信息
     * @param projectId
     * @return
     */
    TaskEntity getOneByProjectId(Long projectId,Integer taskType);
    
}

