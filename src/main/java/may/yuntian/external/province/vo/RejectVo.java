package may.yuntian.external.province.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 驳回vo
 * @author: liyongqiang
 * @create: 2023-04-11 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 1主管，2质控
     */
    private Integer viewer;
    /**
     * 项目id
     **/
    private Long projectId;
    /**
     * 驳回原因
     **/
    private String reason;

}
