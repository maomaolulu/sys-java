package may.yuntian.untils;

import cn.hutool.core.date.DateUtil;
import may.yuntian.modules.sys_v2.utils.RedisCache;

import java.util.Date;

/**
 * 客户跟进任务编号
 *
 * @Author yrb
 * @Date 2023/9/11 16:43
 * @Version 1.0
 * @Description 客户跟进任务编号
 */
public class AdvanceTaskCodeUtil {
    public static final String CODE_KEY = "cus_adv_code";

    public synchronized static String getCode(RedisCache redisService) {
        Object object = redisService.getCacheObject(CODE_KEY);
        if (object == null) {
            redisService.setCacheObject(CODE_KEY, 2);
            object = 1;
        } else {
            int i = Integer.parseInt(object.toString());
            redisService.setCacheObject(CODE_KEY, ++i);
        }
        return (DateUtil.format(new Date(), "yyyyMMdd") + object).substring(2);
    }
}
