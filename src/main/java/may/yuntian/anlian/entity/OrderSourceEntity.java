package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 *项目隶属来源表
 * 实体类
 * @author LiXin
 * @data 2021-03-22
 */
@TableName("t_order_source")
public class OrderSourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目隶属来源
	 */
	private String orderSource;
	/**
	 * 类型（3项目隶属，4业务来源）
	 */
	private Integer type;
	
	/**
	 * 设置：自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 *获取 项目隶属来源
	 */
	public String getOrderSource() {
		return orderSource;
	}
	/**
	 *设置 项目隶属来源
	 */
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	/**
	 *获取 类型（3项目隶属，4业务来源）
	 */
	public Integer getType() {
		return type;
	}
	/**
	 *设置 类型（3项目隶属，4业务来源）
	 */
	public void setType(Integer type) {
		this.type = type;
	}

}
