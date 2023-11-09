package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 控制风速Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ControlWindSpeedVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 特殊字段：控制风速（毒物、粉尘限值，需拿结果去比较） **/
    private String specialDataJson;
    /** 检测结果	 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
