package may.yuntian.filiale.hzyd.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 亿达-检测信息表实体
 *
 * @author: liyongqiang
 * @create: 2023-08-11 11:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("yd_detect_info")
@EqualsAndHashCode(callSuper = false)
public class DetectInfo implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 类目（1：类一，2：类二，3：类三，4：类四）
     */
    private Integer category;
    /**
     * 类型（1放射治疗、2核医学、3介入放射学、4X射线影像诊断、5放射工作场所、6环境检测项目）
     */
    private Integer type;
    /**
     * (设备或房间)名称
     */
    private String name;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 逻辑删除：0代表存在，1代表删除
     */
    private Integer delFlag;

}
