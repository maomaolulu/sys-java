package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 省报送-经济类型表实体类
 * @author: liyongqiang
 * @create: 2023-04-18 13:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_economic_type")
@EqualsAndHashCode(callSuper = false)
public class EconomicType implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Integer id;
    /** 经济类型编码 **/
    private String economicTypeCode;
    /** 经济类型名称 **/
    private String economicTypeName;
    /** 父级id：0表示最顶层分类。 **/
    private Integer parentId;

}
