package may.yuntian.anlianwage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-30 15:27
 */
@Data
@TableName("co_industry_benchmark")
@AllArgsConstructor
@NoArgsConstructor
public class IndustryBenchmarkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目id
     */
    @TableField(exist = false)
    private Long projectId;
    /**
     * 行业编码
     */
    private String code;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 行业基准分
     */
    private Double score;

}
