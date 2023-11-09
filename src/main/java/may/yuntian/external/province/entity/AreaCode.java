package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 省报送-地区编码表实体类
 * @author: liyongqiang
 * @create: 2023-04-06 13:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_area_code")
@EqualsAndHashCode(callSuper = false)
public class AreaCode implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** 地区编码 **/
    private String areaCode;
    /** 地区名称 **/
    private String areaName;
    /** 地区名全称 **/
    private String fullName;

    /** 下级所有子分类：仅到省-市-区，不具体到街道！ **/
    @TableField(exist = false)
    private List<AreaCode> childrenList;


}
