package may.yuntian.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-09-11 16:30
 */
@Data
public class UserVo implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String username;
}
