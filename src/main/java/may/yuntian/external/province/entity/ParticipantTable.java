package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 省报送-参与人员信息表实体类
 * @author: liyongqiang
 * @create: 2023-04-06 10:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_participant_table")
@EqualsAndHashCode(callSuper = false)
public class ParticipantTable implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** projectId **/
    private Long projectId;
    /** 参与人员姓名 **/
    private String name;
    /** 参与人员用户id **/
    private Long userId;
    /** 服务事项编码（1：现场调查；2：现场采样/检测；3：实验室检测；4：评价） **/
    private String itemCode;
    /** 服务事项名称（参照：数据字典26） **/
    private String itemName;
    /** 创建者 **/
    private String createBy;
    /** 创建时间 **/
    private Date createTime;
    /** 备注 **/
    private String remark;

}
