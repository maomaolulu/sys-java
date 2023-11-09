package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 高温vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HighTemperatureVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 接触时间率（%） **/
    private String touchTime;
    /** 体力劳动强度 **/
    private Integer physicalStrength;
    /** 检测结果	 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
