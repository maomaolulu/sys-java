package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电离辐射-含源装置Vo
 * @author: liyongqiang
 * @create: 2023-03-08 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class IonizingRadiationSourceContainDeviceVo extends ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 危害因素id **/
    private Integer factorId;
    /** 危害因素名称 **/
    private String factorName;
    /** 装置名称 **/
    private String deviceName;
    /** 放射源编号 **/
    private String rayNum;
    /** 标号 **/
    private String no;
    /** 核素 **/
    private String nuclide;
    /** 当前活度（Bq） **/
    private String cuActivity;
    /** 出厂活度（Bq） **/
    private String exActivity;
    /** 安装位置	**/
    private String installPosition;
    /** 检测结果	 **/
    private String checkValue;
    /** 结果判定 **/
    private Integer judgeResult;

}
