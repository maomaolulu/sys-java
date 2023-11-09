package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电离辐射-射线装置Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class IonizingRadiationRayDeviceVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 危害因素id **/
    private Integer factorId;
    /** 危害因素名称 **/
    private String factorName;
    /** 装置名称 **/
    private String deviceName;
    /** 装置型号 **/
    private String deviceModel;
    /** 额定容量 **/
    private String apacity;
    /** 场所名称 **/
    private String placeName;
    /** 检测条件 **/
    private String checkCondition;
    /** 检测结果 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
