package may.yuntian.wordgenerate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("word_task_substances")
public class WordTaskSubstancesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	@TableId
	private Long id;

    /**
     * 序号
     */
    @TableField(exist = false)
    private Integer index;
    /**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 样品名称
	 */
	private String sampleName;
	/**
	 * 样品数量
	 */
	private Integer sampleNum;
	/**
	 * 检测项目
	 */
	private String substance;
	/**
	 * 检测依据
	 */
	private String testBasis;
	/**
	 * 数据创建日期
	 */
	private Date createTime;
	/**
	 * 数据更新日期
	 */
	private Date updateTime;



}
