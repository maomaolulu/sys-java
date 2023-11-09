package may.yuntian.external.wanda.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * wanda-危害因素数据字典表实体
 * @author: liyongqiang
 * @create: 2023-03-08 10:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wanda_factor_dictionary")
public class FactorDictionary implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 危害因素id **/
    @ExcelProperty("危害因素id")
    private Integer factorId;
    /** 危害因素名称 **/
    @ExcelProperty("wanda-危害因素名称")
    private String factorName;
    /** 危害因素类型 **/
    private Integer factorType;
    /** 物资表id al_substance **/
    private Integer subId;
    /** 物资名称 al_substance **/
    @ExcelProperty("安联-检测物质名称")
    private String subName;
    /** 是否为万达仓初始数据  1.是  2.自定义 **/
    private Integer isWanda;
    /** 有无资质 1：有，2：无 **/
    private Integer hasCredential;

}
