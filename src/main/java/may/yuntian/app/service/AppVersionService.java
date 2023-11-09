package may.yuntian.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.app.entity.AppVersion;

import java.util.List;
import java.util.Map;

/**
 * app版本管理
 * 业务逻辑层接口
 *
 * @author zhanghao
 * @date 2022-04-12
 */
public interface AppVersionService extends IService<AppVersion> {
    Integer ce();
    List<AppVersion> listAll(Map<String, Object> params);
}