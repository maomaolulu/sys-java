package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * wanda-化学、粉尘、生物危害、电离辐射-射线装置、电离辐射-含源装置检测结果信息表实体
 * @author: liyongqiang
 * @create: 2023-03-08 10:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@TableName("wanda_chemical_dust_factor")
public class ChemicalDustFactor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 项目id **/
    private Long projectId;
    /** 车间名称 **/
    private String workUnitName;
    /** 岗位/工种 **/
    private String workJobName;
    /** 劳动定员人数 **/
    private String workNum;
    /** 检测点 **/
    private String checkPointName;
    /** 危害因素id **/
    private Integer factorId;
    /** 危害因素名称 **/
    private String factorName;
    /** 危害因素类型（1：化学，2：粉尘，3：生物危害，4：电离辐射-射线装置，5：电离辐射-含源装置） **/
    private Integer factorType;
    /** ①化学：CME/CMAC值 单位mg/m³ ②生物危害：CME/CMAC值 单位（孢子数/m³）/（ng/m³） **/
    private String cmeCmac;
    /** ①化学：CTWA值 单位mg/m³ ②生物危害：CTWA值 单位（孢子数/m³）/（ng/m³） **/
    private String ctwa;
    /** ①化学：CSTE/CSTEL值 单位mg/m³ ②生物危害：CSTE/CSTEL值 单位（孢子数/m³）/（ng/m³） **/
    private String csteCstel;
    /** 超限倍数/峰接触浓度 **/
    private String exceedPeak;
    /** 折减因子类型（0.不考虑折减，1.日折减，2.周折减） ①化学 ②生物危害 **/
    private Integer reductionFactorType;
    /** 折减因子 ①化学 ②生物危害 **/
    private String reductionFactor;
    /** 结果判定（1.不判定，2.符合，3.不符合）①化学②生物危害③电离辐射-射线装置④电离辐射-含源装置 **/
    private Integer judgeResult;
    /** 总尘CTWA（单位mg/m³） **/
    private String totalCtwa;
    /** 总尘浓度范围 **/
    private String totalRange;
    /** 总尘超限倍数/峰接触浓度 **/
    private String totalExceedPeak;
    /** 总尘折减因子类型（0.不考虑折减，1.日折减，2.周折减） **/
    private Integer totalReductionFactorType;
    /** 总尘折减因子 **/
    private String totalReductionFactor;
    /** 总尘结果判定（1.不判定，2.符合，3.不符合） **/
    private Integer totalJudgeResult;
    /** 呼尘CTWA（单位mg/m³） **/
    private String respirableCtwa;
    /** 呼尘浓度范围 **/
    private String respirableRange;
    /** 呼尘超限倍数/峰接触浓度 **/
    private String respirableExceedPeak;
    /** 呼尘折减因子类型（0.不考虑折减，1.日折减，2.周折减） **/
    private Integer respirableReductionFactorType;
    /** 呼尘折减因子 **/
    private String respirableReductionFactor;
    /** 呼尘结果判定（1.不判定，2.符合，3.不符合） **/
    @NotNull(message = "呼尘结果判定不能为空")
    private Integer respirableJudgeResult;
    /** 装置名称（①电离辐射-射线装置②电离辐射-含源装置） **/
    private String deviceName;
    /** ①电离辐射-射线装置：装置型号 ②电离辐射-含源装置：放射源编号 **/
    private String deviceModel;
    /** ①电离辐射-射线装置：额定容量 ②电离辐射-含源装置：标号 **/
    private String apacity;
    /** ①电离辐射-射线装置：场所名称 ②电离辐射-含源装置：核素 **/
    private String placeName;
    /** ①电离辐射-射线装置：检测条件 ②电离辐射-含源装置：安装位置 **/
    private String checkCondition;
    /** 检测结果①电离辐射-射线装置②电离辐射-含源装置 **/
    private String checkValue;
    /** 当前活度(Bq) **/
    private String cuActivity;
    /** 出厂活度(Bq) **/
    private String exActivity;
    /** 结果推送状态（默认0：待提交(默认)，1：待主管审核，2：主管驳回，3：待质控审核，4：质控驳回，5：已报送。） **/
//    private Integer resultPushStatus;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    @TableField(exist = false)
    /** 标识该条记录是否被删除：0未，1删除 **/
    private int remove;

}
