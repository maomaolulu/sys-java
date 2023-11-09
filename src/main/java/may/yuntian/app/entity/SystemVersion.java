package may.yuntian.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统版本详情
 * 
 * @author LIXIN
 * @data 2022-10-12
 */
@TableName("al_system_version")
@Data
public class SystemVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;

    /**
     * 详细版本号(2.0.1)
     */
    private String detailedVersion;
    /**
     * 大版本(2)
     */
    private String largeVersion;

	/**
	 *java代码发布日期
	 */
	private Date javaReleaseDate;
	/**
	 *python代码发布日期
	 */
	private Date pythonReleaseDate;
    /**
     *前端代码发布日期
     */
    private Date htmlReleaseDate;
    /**
     *ipad端发布日期
     */
    private Date ipadReleaseDate;
    /**
     *内测完成日期
     */
    private Date closedBetaDate;
    /**
     *发布日期
     */
    private Date releaseDate;


    /**
     *更新内容
     */
    private String updateContent;
    /**
     * 系统(检评/评价/sys)
     */
    private String systemType;


	/**
	 *数据入库时间
	 */
	private Date createTime;
	/**
	 *修改时间
	 */
	private Date updateTime;


}
