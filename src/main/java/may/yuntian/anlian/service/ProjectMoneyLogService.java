package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectMoneyLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 项目金额修改日志
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-04-03 08:38:30
 */
public interface ProjectMoneyLogService extends IService<ProjectMoneyLogEntity> {

    List<ProjectMoneyLogEntity> queryPage(Map<String, Object> params);
}

