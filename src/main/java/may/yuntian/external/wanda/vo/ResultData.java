package may.yuntian.external.wanda.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.wanda.converter.JudgeResultIntegerConverter;
import may.yuntian.external.wanda.converter.NoiseTypeIntegerConverter;
import may.yuntian.external.wanda.converter.PhysicalStrengthIntegerConverter;
import may.yuntian.external.wanda.converter.UltraTypeIntegerConverter;

/**
 * wanda-excel导入检测结果实体
 * @author: liyongqiang
 * @create: 2023-07-10 11:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResultData {

    @ExcelProperty("*车间名称")
    private String workUnitName;
    @ExcelProperty("*岗位")
    private String workJobName;
    @ExcelProperty("*劳动定员人数")
    private String workNum;
    @ExcelProperty("*检测点")
    private String checkPointName;
    @ExcelProperty("*危害因素名称")
    private String factorName;
    @ExcelIgnore // 危害因素id
    private Integer factorId;

    // 化学因素
    @ExcelProperty("CME/CMAC")
    private String cmeCmac;
    @ExcelProperty("CTWA")
    private String ctwa;
    @ExcelProperty("CSTE/CSTEL")
    private String csteCstel;
    @ExcelProperty("超限倍数/峰接触浓度")
    private String exceedPeak;

    // 粉尘
    @ExcelProperty("*总尘CTWA（mg/m³）")
    private String totalCtwa;
    @ExcelProperty("总尘浓度范围")
    private String totalRange;
    @ExcelProperty("总尘超限倍数/峰接触浓度")
    private String totalExceedPeak;
    @ExcelProperty(value = "*总尘结果判定", converter = JudgeResultIntegerConverter.class)
    private Integer totalJudgeResult;
    @ExcelProperty("*呼尘CTWA（mg/m³）")
    private String respirableCtwa;
    @ExcelProperty("呼尘浓度范围")
    private String respirableRange;
    @ExcelProperty("呼尘超限倍数/峰接触浓度")
    private String respirableExceedPeak;
    @ExcelProperty(value = "*呼尘结果判定", converter = JudgeResultIntegerConverter.class)
    private Integer respirableJudgeResult;

    // 噪声
    @ExcelProperty(value = "*噪声类型", converter = NoiseTypeIntegerConverter.class)
    private Integer noiseType;
    /** ①噪声：检测数值（单位dB(A)）②高温：检测结果（单位：℃）③照度：检测结果 ④控制风速：检测结果 **/
    @ExcelProperty("检测数值")
    private String checkValue;
    @ExcelProperty("最低值")
    private String checkValueMin;
    @ExcelProperty("最高值")
    private String checkValueMax;
    @ExcelProperty("8/40h等效声级检测数值")
    private String checkValue840;
    @ExcelProperty("工作日接触脉冲次数")
    private Integer touchTimes;
    @ExcelProperty("声压级峰值")
    private String noisePeak;

    // 高温
    /** ①高温：接触时间率（%）（不要大于100%不能为0或者负数）②工频电场：接触时间（单位：h）③紫外辐射：接触时间（单位：h） **/
    @ExcelProperty("*接触时间率（%）")
    private String touchTime;
    @ExcelProperty(value = "*体力劳动强度", converter = PhysicalStrengthIntegerConverter.class)
    private Integer physicalStrength;
    @ExcelProperty("*检测结果（℃）")
    private String checkValueT;

    // 工频电场
    @ExcelProperty("*电场强度")
    private String freqStrength;
    @ExcelProperty("*接触时间")
    private String touchTimeP;

    // 紫外辐射
    @ExcelProperty(value = "*紫外光谱分类", converter = UltraTypeIntegerConverter.class)
    private Integer ultraType;
    @ExcelProperty("*接触时间（h）")
    private String touchTimeU;
    @ExcelProperty("辐照度")
    private String irradiance;
    @ExcelProperty("照射量")
    private String exposure;
    @ExcelProperty("罩内眼部辐照度")
    private String eyeIrradiance;
    @ExcelProperty("罩内眼部照射量")
    private String eyeExposure;
    @ExcelProperty(value = "罩内眼部结果判定", converter = JudgeResultIntegerConverter.class)
    private Integer eyeJudgeResult;

    @ExcelProperty(value = "*结果判定", converter = JudgeResultIntegerConverter.class)
    private Integer judgeResult;

}
