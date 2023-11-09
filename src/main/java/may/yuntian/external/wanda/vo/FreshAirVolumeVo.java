package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新风量Vo
 * @author: liyongqiang
 * @create: 2023-03-08 14:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FreshAirVolumeVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 人数 **/
    private Integer staffNum;
    /** 检测结果	 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
