package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 合同详情-项目检测信息vo
 *
 * @author: liyongqiang
 * @create: 2023-08-14 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 左key
     */
    private String leftKey;
    /**
     * 右value
     */
    private String rightValue;

}
