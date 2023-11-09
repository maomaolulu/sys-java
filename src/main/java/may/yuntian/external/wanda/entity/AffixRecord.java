package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * wanda-附件上传信息记录实体
 * @author: liyongqiang
 * @create: 2023-03-07 13:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wanda_affix_record")
@EqualsAndHashCode(callSuper=false)
public class AffixRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 安联project_id **/
    private Long projectId;
    /** 附件类型（1：检测报告 2：检评报告 3：现状评价报告4：控制效果评价 5：检测点分布示意图 6：检测委托书） **/
    private Integer affixType;
    /** 文件名 **/
    private String fileName;
    /** 文件存放的相对路径（检评or评价服务器） **/
    private String fileUrl;
    /** 创建者 **/
    private String createBy;
    /** 创建时间 **/
    private Date createTime;
    /** 文件后缀 **/
    private String fileSuffix;
    /** 文件存储类型（1：云盘，2：调Java接口，3：调Python接口） **/
    private Integer storageType;
    /** 云盘文件id（默认0） **/
    private Long svcFileId;
    /** 上传记录id（默认0） **/
    private Long archiveFileId;
    /** 是否是系统录入 (1: 是   2: 否) **/
    private Integer isSystem;
}
