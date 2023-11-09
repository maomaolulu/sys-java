package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 省报送-结果项信息表实体类
 * @author: liyongqiang
 * @create: 2023-04-06 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_result_item")
@EqualsAndHashCode(callSuper = false)
public class ResultItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** projectId **/
    private Long projectId;
    /** 检测项目编码（参照：数据字典24） **/
    private String checkItemCode;
    /** 检测项目名称（当检测项目为其它粉尘时，必填！） **/
    private String itemName;
    /** 车间 **/
    private String workArea;
    /** 岗位 **/
    private String detectionArea;
    /** 日接触时间 （N..4,2：总长度最多为4位数字字符，小数点后保留2位数字） **/
    private String dailyContactTime;
    /** 周工作天数(0 ~ 7天) **/
    private String weekWorkDay;
    /** 检测点位 **/
    private String pointName;
    /** 检测日期 **/
    private String detectionDate;
    /** 结果项编码（参照：数据字典9） 特殊字段，加``，防止sql语句报错！ **/
    @TableField(value = "`code`")
    private String code;
    /** 结果 **/
    private String result;
    /** 计量单位 **/
    private String unit;
    /** 单项结论（1：符合；0：不符合） **/
    private String conclusion;
    /** 危害因素类型：1.化学（包含co/co2） 2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传振动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 **/
    private Integer factorType;
    /** 创建者 **/
    private String createBy;
    /** 创建时间 **/
    private Date createTime;
    /** 备注 **/
    private String remark;
    /** al_substance表id） **/
    private Long subId;
    /** al_substance表name **/
    private String subName;

}
