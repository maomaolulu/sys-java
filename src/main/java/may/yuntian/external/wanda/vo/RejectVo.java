package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 驳回vo
 *
 * @author: liyongqiang
 * @create: 2023-03-16 10:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     **/
    private Long projectId;
    /**
     * 驳回原因
     **/
    private String reason;

}
