package may.yuntian.filiale.hzyd.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 亿达-合同、项目、检测信息基准关系表实体
 *
 * @author: liyongqiang
 * @create: 2023-08-11 11:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("yd_basic_relation")
@EqualsAndHashCode(callSuper = false)
public class BasicRelation implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 项目编号
     */
    private String itemCode;
    /**
     * 项目类型
     */
    private String itemType;
    /**
     * 检测信息类目
     */
    private String detectInfoCategory;

}
