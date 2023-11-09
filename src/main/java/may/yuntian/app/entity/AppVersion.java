package may.yuntian.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * app版本管理
 * 
 * @author 张豪
 * @data 2022-04-12
 */
@TableName("al_app_version")
@Data
public class AppVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * APP类型  ZJ: 检评   PJ: 评价
	 */
	private String types;
	/**
	 *版本名称
	 */
	private String name;
	/**
	 *地址
	 */
	private String path;
	/**
	 *版本
	 */
	private String version;
	/**
	 *版本号
	 */
	private String versionNum;

    /**
     *更新内容
     */
    private String updateContent;


	/**
	 *数据入库时间
	 */
	private Date createTime;
	/**
	 *修改时间
	 */
	private Date updateTime;


}
