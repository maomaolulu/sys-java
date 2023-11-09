package may.yuntian.anliantest.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 安联官网公示结果
 * 
 * @author zhanghao
 * @data 2022-04-18
 */
@Data
@TableName("at_publicity_results")
public class PublicityResults implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/** 项目id */
	private Long projectId ;
	/** 操作人 */
	private String username ;
	/** 操作 */
	private String operation ;
	/** 操作时间 */
	private Date operationTime ;
	/** 备注 */
	private String remark ;
	/** 公示状态 */
	private Integer status ;
	/** 创建时间 */
	private Date createTime ;
	/** 更新时间 */
	private Date updateTime ;
}
