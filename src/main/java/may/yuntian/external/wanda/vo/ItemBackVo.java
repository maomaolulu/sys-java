package may.yuntian.external.wanda.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 健康在线-项目退回vo
 *
 * @author: liyongqiang
 * @create: 2023-09-05 16:41
 */
@Data
public class ItemBackVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 第三方同步成功的项目id
     */
    private String checkId;
    /**
     * 退回时间戳
     */
    private String returnTime;
    /**
     * 退回原因
     */
    private String reason;
    /**
     * 加载时间
     */
    private String loadTime;

}
