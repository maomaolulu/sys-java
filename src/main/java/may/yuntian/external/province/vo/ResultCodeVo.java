package may.yuntian.external.province.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结果项编码vo
 * @author: liyongqiang
 * @create: 2023-04-11 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResultCodeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 检测项目编码（省）
     */
    private String checkItemCode;
    /**
     * 检测项目名称（省）
     */
    private String itemName;
    /**
     * 结果项编码
     */
    private String resultItemCode;
    /**
     * 结果项名称
     **/
    private String resultItemName;
    /**
     * 计量单位编码
     */
    private String unit;

}
