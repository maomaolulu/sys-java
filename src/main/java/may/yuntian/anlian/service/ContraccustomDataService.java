package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ContraccustomDataEntity;

import java.util.Map;

/**
 * 合同模板自定义字段数据
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
public interface ContraccustomDataService extends IService<ContraccustomDataEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

