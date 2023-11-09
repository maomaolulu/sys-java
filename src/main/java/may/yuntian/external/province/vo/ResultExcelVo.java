package may.yuntian.external.province.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 省平台-导入检测结果vo
 * @author: liyongqiang
 * @create: 2023-07-17 09:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResultExcelVo {

    @ExcelProperty("*车间名称")
    private String workArea;
    @ExcelProperty("*岗位")
    private String detectionArea;
    @ExcelProperty("*点位名称")
    private String pointName;
    @ExcelProperty("*检测项目")
    private String subName;
    @ExcelProperty("*采样/检测日期（2022/04/05）")
    private String detectionDate;
    @ExcelProperty("*周工作天数(d)")
    private String weekWorkDay;
    @ExcelProperty("*日接触时长(h)")
    private String dailyContactTime;
    /** 0：符合，1：不符合 **/
    @ExcelProperty("*单项结论")
    private String conclusion;

    // 化学
    @ExcelProperty("*PC-TWA")
    private String pcTwa = "";
    @ExcelProperty("*PC-STEL")
    private String pcStel = "";
    @ExcelProperty("*峰接触浓度")
    private String peak = "";
    @ExcelProperty("*MAC")
    private String mac = "";

    // 粉尘
    @ExcelProperty("C-PE(总尘)")
    private String cPe = "";
    @ExcelProperty("PC-TWA（总尘）")
    private String pcTwaTotal = "";
    @ExcelProperty(value = "C-PE(呼尘)")
    private String cPeCall = "";
    @ExcelProperty(value = "PC-TWA（呼尘）")
    private String pcTwaCall = "";
//    @ExcelProperty("*游离二氧化硅含量")
//    private String freeSilica;

    // 噪声
    @ExcelProperty("*8h等效声级")
    private String soundLevel8 = "";
    @ExcelProperty("*40h等效声级")
    private String soundLevel40 = "";
    @ExcelProperty("*声压级峰值")
    private String peakSound = "";

    // 高温
    @ExcelProperty("*接触时间率")
    private String contactTimeRate = "";
    @ExcelProperty("*体力劳动强度")
    private String laborIntensity = "";
    @ExcelProperty("*WBGT指数（℃）")
    private String wbgt = "";
    @ExcelProperty("*接触限值（℃）")
    private String exposureLimit = "";

    // 工频电场
    @ExcelProperty("*电场强度(kV/m)")
    private String electricIntensity;

    // 紫外辐射
    @ExcelProperty("*辐照度")
    private String irradiance = "";
    @ExcelProperty("*照射量")
    private String exposure = "";

}
