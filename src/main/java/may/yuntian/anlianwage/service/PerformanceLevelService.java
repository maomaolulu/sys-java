package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlianwage.entity.PerformanceLevelEntity;

import java.util.List;
import java.util.Map;

/**
 * 目标产出等级表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
public interface PerformanceLevelService extends IService<PerformanceLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据类型返回数组
     * @param type
     * @return
     */
    List<PerformanceLevelEntity> list(String type);
    
}

