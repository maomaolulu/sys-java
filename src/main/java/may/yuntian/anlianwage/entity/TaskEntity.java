package may.yuntian.anlianwage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LiXin
 * @email ''
 * @date 2022-06-30 15:04:26
 */
@Data
@TableName("env_task")
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 项目id
	 */
	private Long projectId;
//	/**
//	 * 项目类型
//	 */
//	private String projectType;
//	/**
//	 * 项目所属公司
//	 */
//	private String affilCompany;
//	/**
//	 * 项目编号
//	 */
//	private String projectSn;
	/**
	 * 任务类型:1采样任务,2咨询任务
	 */
	private Integer taskType;
//	/**
//	 * 委托类型
//	 */
//	private String testType;
//	/**
//	 * 受检单位
//	 */
//	private String company;
//	/**
//	 * 受检单位地址
//	 */
//	private String address;
//	/**
//	 * 状态
//	 */
//	private Integer state;
	/**
	 * 负责人(排单后可改 绩效归项目原始负责人)
	 */
	private String chief;
	/**
	 * 组员
	 */
	private String members;

}
