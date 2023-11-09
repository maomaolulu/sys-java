package may.yuntian.modules.sys_v2.entity.vo;

import lombok.Data;

/**
 * 登录信息体
 *
 * @author hjy
 * @date 2023/6/9 13:12
 */
@Data
public class LoginVo {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 当前系统入口密钥
     */
    private String secretKey;
    /**
     * 请求日期时间戳
     * （自发起请求开始，三分钟内有效）
     * （具体以相应服务器时间为准）
     */
    private Long timeStamp;
    /**
     * 是否需要其他信息（暂定用户信息）
     */
    private Boolean isWant;
}
