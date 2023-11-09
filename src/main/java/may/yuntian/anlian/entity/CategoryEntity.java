package may.yuntian.anlian.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 *类型信息记录实体类
 * 
 * @author LiXin
 * @data 2020-12-10
 */
@TableName("t_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 父级类别ID，一级类别为0
	 */
	private Long pid;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 英文缩写
	 */
	private String nameEn;
	/**
	 * 模块
	 */
	private String module;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	private Integer delFlag;
	
	/**
	 * 是否是老数据 1是 2不是
	 */
	private Integer isOld;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Integer getIsOld() {
		return isOld;
	}
	public void setIsOld(Integer isOld) {
		this.isOld = isOld;
	}

}
