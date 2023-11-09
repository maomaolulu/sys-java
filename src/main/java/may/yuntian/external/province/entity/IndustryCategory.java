package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 省报送-行业分类实体
 * @author: liyongqiang
 * @create: 2023-04-18 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_industry_category")
@EqualsAndHashCode(callSuper = false)
public class IndustryCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** 父级id **/
    private Long pid;
    /** 门类字母 **/
    private String letter;
    /** 行业类别 **/
    private String industryCategory;
    /** 行业类别编码 **/
    private String industryCategoryCode;
    /** 全称（门类字母、编码、名称拼接） **/
    private String fullName;
    /** 备注 **/
    private String remark;

}
