package may.yuntian.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.dao.SysUserTokenDao;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;
import may.yuntian.modules.sys.oauth2.TokenGenerator;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys.service.SysUserTokenService;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 1800;
    @Autowired
    private AlRedisUntil alRedisUntil;
    @Autowired
    private SysUserService sysUserService;
    private final static String prefix = "token_";

    //	@Override
    public R createToken1(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        SysUserTokenEntity tokenEntity = this.getById(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            this.save(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            this.updateById(tokenEntity);
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE).put("expireTime", expireTime);

        return r;
    }

    @Override
    public R createToken(long userId, String type) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        String oldToken = (String) alRedisUntil.hget("anlian_user", String.valueOf(userId));
        SysUserEntity sysUserEntity = sysUserService.getById(userId);
        sysUserEntity.setPassword(null);
        sysUserEntity.setSalt(null);
        HashMap<String, Object> map = new HashMap<>();
        Map<String, String> typeMap = new HashMap<>();
//        List<Map<String,String>> typeList = newCommission ArrayList<>();
        map.put("user_info", sysUserEntity);
        typeMap.put(type, type);
        // oldToken是否存在
        if (oldToken == null) {
            map.put("login_type", typeMap);
            String userInfo = alRedisUntil.toJson(map);
            alRedisUntil.hset("anlian_user", String.valueOf(userId), token);
            alRedisUntil.set(prefix + token, userInfo, EXPIRE);
        } else {
            if (alRedisUntil.get(prefix + oldToken) != null && alRedisUntil.getExpire(prefix + oldToken) >= 1) {
                token = oldToken;
                Map retMap = alRedisUntil.jsonToMap((String) alRedisUntil.get(prefix + oldToken));
                System.out.println(map);
                if (retMap.get("login_type") != null) {
                    Map<String, String> xmap = (Map<String, String>) retMap.get("login_type");
                    if (xmap.keySet().size() > 0) {
                        if (xmap.get(type) != null && xmap.get(type).equals(type)) {
                            return R.error(21, "请勿同一账号在同一系统用两台设备登陆.如需登录请将此系统登录的账号从另一台设备退出登录!");
                        } else {
                            typeMap.putAll(xmap);
                        }
                    }

                }

//                System.out.println("-=-==-===-=-=-=-=");
                map.put("login_type", typeMap);
                String userInfo = alRedisUntil.toJson(map);
                alRedisUntil.hset("anlian_user", String.valueOf(userId), token);
                alRedisUntil.set(prefix + token, userInfo, EXPIRE);
            } else {
                map.put("login_type", typeMap);
                String userInfo = alRedisUntil.toJson(map);
                alRedisUntil.hset("anlian_user", String.valueOf(userId), token);
                alRedisUntil.set(prefix + token, userInfo, EXPIRE);
            }
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE).put("expireTime", expireTime);

        return r;
    }

    /**
     * xin redis 中创建Token及缓存信息
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public R newCreateToken(Long userId, String type) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        // user_id对应value对象
        Object anUser = alRedisUntil.hget("an_user", String.valueOf(userId));
        // Object对象 强转成 String对象 再转 JsonObject对象
        JSONObject jsonUser = JSON.parseObject((String) anUser);
        //map对象
        Map<String, Object> jsonToMap = new HashMap<>(16);
        HashMap<String, Object> map = new HashMap<>(16);
        SysUserEntity sysUser = sysUserService.getById(userId);
        sysUser.setPassword(null);
        sysUser.setSalt(null);
        map.put("user_info", sysUser);
        if (jsonUser == null) {
            // 系统类型和token进行绑定成k:v结构
            jsonToMap.put(type, token);
            String userInfo = alRedisUntil.toJson(map);
            String systemTypeToken = alRedisUntil.toJson(jsonToMap);
            // 存入redis
            alRedisUntil.hset("an_user", String.valueOf(userId), systemTypeToken);
            alRedisUntil.set(prefix + token, userInfo, EXPIRE);
        } else {
            //循环转换
            Iterator it = jsonUser.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                jsonToMap.put(entry.getKey(), entry.getValue());
            }
//        Map<String, Object> jsonToMap = AlRedisUntil.jsonToMap(String.valueOf(anlian_user)); object对象转Jsonobject对象异常
            // 系统对应的redis中的ToKen
            String oldToken = (String) jsonToMap.get(type);
            // 不管oldToken是否存在
            // 都执行: 删除hash里面对应的 oldToke 以及redis最外层的缓存对应值: K=token V=userInfo
            jsonToMap.remove(type, oldToken);
            alRedisUntil.del(prefix + oldToken);
            // hash中放入新ToKen
            jsonToMap.put(type, token);
            String systemTypeToken = alRedisUntil.toJson(jsonToMap);
            String userInfo = alRedisUntil.toJson(map);
            // 存入redis
            alRedisUntil.hset("an_user", String.valueOf(userId), systemTypeToken);
            alRedisUntil.set(prefix + token, userInfo, EXPIRE);
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE).put("expireTime", expireTime);

        return r;
    }

    /**
     * xin退出
     *
     * @param userId
     * @param type
     */
    @Override
    public void logoutNew(Long userId, String type) {

        // user_id对应value对象
        Object anUser = alRedisUntil.hget("an_user", String.valueOf(userId));
        Map<String, Object> jsonToMap = AlRedisUntil.jsonToMap(String.valueOf(anUser));
        System.out.println("jsonToMap ================= " + jsonToMap);
        // 系统对应的redis中的ToKen
        String oldToken = (String) jsonToMap.get(type);
        jsonToMap.remove(type, oldToken);
        System.out.println("jsonToMap = " + jsonToMap);
        String systemTypeToken = alRedisUntil.toJson(jsonToMap);
        alRedisUntil.hset("an_user", String.valueOf(userId), systemTypeToken);
        alRedisUntil.del(prefix + oldToken);
    }


//    @Override
//    public void logout(long userId, String type) {
//        //生成一个token
//        String token = TokenGenerator.generateValue();
//
//        String oldToken = (String) alRedisUntil.hget("anlian_user", String.valueOf(userId));
//
//        HashMap<String, Object> map = new HashMap<>();
//        Map retMap = alRedisUntil.jsonToMap((String) alRedisUntil.get(prefix + oldToken));
//        String newString = alRedisUntil.toJson(retMap.get("user_info"));
//        SysUserEntity sysUserEntity = alRedisUntil.fromJson(newString, SysUserEntity.class);
//        Map<String, String> list = (Map<String, String>) retMap.get("login_type");
////        List<String> list = alRedisUntil.fromJson((String)retMap.get("login_type"),List.class);
//        list.remove(type);
//        if (list.keySet().size() > 0) {
//            map.put("user_info", sysUserEntity);
//            map.put("login_type", list);
//            String userInfo = alRedisUntil.toJson(map);
//            alRedisUntil.hset("anlian_user", String.valueOf(userId), oldToken);
//            alRedisUntil.set(prefix + oldToken, userInfo, EXPIRE);
//        } else {
//            alRedisUntil.del(prefix + oldToken);
//            alRedisUntil.hset("anlian_user", String.valueOf(userId), token);
//        }
//    }

    /**
     * 根据用户ID删除token
     *
     * @param userId
     */
    @Override
    public void deleteToken(Long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        String oldToken = (String) alRedisUntil.hget("anlian_user", String.valueOf(userId));
        alRedisUntil.del(prefix + oldToken);
        alRedisUntil.hset("anlian_user", String.valueOf(userId), token);
    }
}
