package may.yuntian.anlianwage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 目标产出等级表
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@TableName("co_performance_level")
public class PerformanceLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 等级
	 */
	private String level;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 金额
	 */
	private BigDecimal money;

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
	 * 设置：等级
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * 获取：等级
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getMoney() {
		return money;
	}
}
