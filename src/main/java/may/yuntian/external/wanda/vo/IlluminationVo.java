package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 照度Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class IlluminationVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 检测结果	 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
