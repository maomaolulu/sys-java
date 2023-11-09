package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.BackupsProjectProceduresEntity;
import may.yuntian.common.utils.PageUtils;

import java.util.Map;

/**
 * 项目流程表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-10-28 15:48:41
 */
public interface BackupsProjectProceduresService extends IService<BackupsProjectProceduresEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
}

