package may.yuntian.external.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 跟进记录表
 * @author gy
 * @date 2023-08-18 16:32
 */
@Data
@TableName("custom_advance_record")
public class CustomAdvanceRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 跟进日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date advanceDate;

    /**
     * 跟进方式
     */
    private String advancePattern;

    /**
     * 跟进信息
     */
    private String advanceInformation;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 删除标记
     */
    private Integer deleteFlag;

    /**
     * 定位信息
     */
    private String position;

    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片上传
     */
    @TableField(exist = false)
    private List<String> pathList;

    /**
     * 图片上传（编辑时已删除）
     */
    @TableField(exist = false)
    private List<String> delList;
}
