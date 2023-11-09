package may.yuntian.publicity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目公示记录
 * 
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
@Data
@TableName("zj_publicity_info")
public class PublicityInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 项目id
	 */
	private Long projectId;
	/**
	 * 操作人
	 */
	private String username;
	/**
	 * 操作
	 */
	private String operation;
	/**
	 * 操作时间
	 */
	private Date operationTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 公示状态 （1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.待公示，7.已公示）
	 */
	private Integer status;
	/**
	 * 数据入库时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
