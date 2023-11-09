package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlianwage.entity.PerformanceEntity;

import java.util.Map;

/**
 * 人员绩效表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
public interface PerformanceService extends IService<PerformanceEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据人员ID获取人员产出信息
     * @param userid
     * @return
     */
    PerformanceEntity getByUserid(Long userid);
    
}

