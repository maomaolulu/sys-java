package may.yuntian.external.express.sf.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 快件产品类别表
 * @author: liyongqiang
 * @create: 2023-05-24 10:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("exp_product_type")
@EqualsAndHashCode(callSuper = false)
public class ProductType implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** id **/
    @TableId
    private Integer id;
    /** 产品编码 **/
    private Integer expressTypeId;
    /** 时效类型 **/
    private String agingType;
    /** 描述 **/
    private String remark;

}
