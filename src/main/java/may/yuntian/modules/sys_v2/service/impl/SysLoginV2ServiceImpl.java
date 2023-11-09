package may.yuntian.modules.sys_v2.service.impl;

import com.google.gson.Gson;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys_v2.entity.vo.LoginVo;
import may.yuntian.modules.sys_v2.mapper.SysUserMapper;
import may.yuntian.modules.sys_v2.service.SysLoginV2Service;
import may.yuntian.modules.sys_v2.utils.Convert;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录相关业务处理
 *
 * @author hjy
 * @date 2023/6/9 14:14
 */
@Service
public class SysLoginV2ServiceImpl implements SysLoginV2Service {

    private final static String prefix = "token_";

    private final SysUserMapper userMapper;

    @Autowired
    private AlRedisUntil alRedisUntil;

    public SysLoginV2ServiceImpl(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 登录
     *
     * @param loginVo 登录信息
     * @return 结果
     */
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        //返回数据
        Map<String, Object> resultMap = new HashMap<>(2);
        //用户信息
        SysUserEntity sysUser = userMapper.selectUserByEmail(loginVo.getEmail());
        if (StringUtils.isNull(sysUser)) {
            throw new RuntimeException("未查询到该用户！");
        }
        //无须校验，生成token 并存入缓存
        UUID token = UUID.randomUUID();
        //制作缓存数据集合
        Map<String, Object> cacheMap = new HashMap<>(1);
        cacheMap.put("user_info", sysUser);
        //30分钟过期（业务上每访问一次会刷新时间）
        alRedisUntil.set(prefix + token, new Gson().toJson(cacheMap), 1800);
        resultMap.put("token", token);
        //是否需要返回用户信息
        Boolean flag = Convert.toBool(loginVo.getIsWant(), false);
        if (flag) {
            resultMap.put("user", sysUser);
        }
        return resultMap;
    }
}
