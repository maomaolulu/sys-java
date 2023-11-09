package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 手传振动vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HandVibrationVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 接触时间 **/
    private String touchTime;
    /** 4h等能量频率计权振动加速度	 **/
    private String accelet4h;
    /** 结果判定 **/
    private Integer judgeResult;

}
