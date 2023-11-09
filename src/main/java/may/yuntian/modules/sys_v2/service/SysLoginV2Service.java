package may.yuntian.modules.sys_v2.service;

import may.yuntian.modules.sys_v2.entity.vo.LoginVo;

import java.util.Map;

/**
 * 登录接口
 *
 * @author hjy
 * @date 2023/6/9 14:13
 */
public interface SysLoginV2Service {
    /**
     * 登录
     *
     * @param loginVo 登录信息
     * @return 结果
     */
    Map<String, Object> login(LoginVo loginVo);
}
