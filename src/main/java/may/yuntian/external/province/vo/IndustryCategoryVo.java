package may.yuntian.external.province.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 省报送-行业分类vo
 * @author: liyongqiang
 * @create: 2023-04-18 18:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class IndustryCategoryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    private Long id;
    /** 父级id **/
    private Long pid;
    /** 门类字母 **/
    private String letter;
    /** 行业类别 **/
    private String industryCategory;
    /** 行业类别编码 **/
    private String industryCategoryCode;

    /** 子类别树 **/
    private List<IndustryCategoryVo> childrenCategoryList;

}
