package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 万达-行政区划实体
 * @author: liyongqiang
 * @create: 2023-03-06 15:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wanda_administrative_division")
public class AdministrativeDivision implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 行政区划编码 **/
    private String divisionCode;
    /** 行政区划名称 **/
    private String divisionName;
    /** 省 **/
    private String province;
    /** 市 **/
    private String city;
    /** 区 **/
    private String area;

}
