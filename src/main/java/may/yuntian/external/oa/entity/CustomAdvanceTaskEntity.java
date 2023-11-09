package may.yuntian.external.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户跟进任务表
 * @author gy
 * @date 2023-08-18 16:28
 */
@Data
@TableName("custom_advance_task")
public class CustomAdvanceTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 跟进人id
     */
    private Long advanceId;

    /**
     * 业务状态
     */
    private Integer businessStatus;

    /**
     * 跟进结果：0未成单，1已成单
     */
    private Integer advanceResult;

    /**
     * 首次跟进
     */
    private Date advanceFirstTime;

    /**
     * 最近跟进
     */
    private Date advanceLastTime;

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
     * 任务编号
     */
    private String taskCode;

    /**
     * 最近跟进记录ID
     */
    private Long lastRecordId;

//    /**
//     * 跟进任务ID
//     */
//    @TableField(exist = false)
//    private List<Long> taskList;

    /**
     * 任务信息集合
     */
    @TableField(exist = false)
    private List<CustomAdvanceTaskEntity> tasksList;
}
