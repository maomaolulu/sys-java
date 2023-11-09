package may.yuntian.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.app.entity.SystemVersion;

import java.util.List;
import java.util.Map;

/**
 * 系统版本详情
 * 业务逻辑层接口
 *
 * @author LIXIN
 * @data 2022-10-12
 */
public interface SystemVersionService extends IService<SystemVersion> {
    List<SystemVersion> listAll(Map<String, Object> params);
}