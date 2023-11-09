package may.yuntian.socket.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import may.yuntian.modules.sys_v2.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录
 * 
 * @author yrb
 * @date 2023-04-06
 */
@Data
@TableName("al_note")
public class AbuProjectNote implements Serializable
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目ID */
    private Long projectId;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String identifier;

    /**
     * 接收人ID
     */
    @Excel(name = "接收人")
    private Long receiveId;

    /** 留言内容 */
    @Excel(name = "消息内容")
    private String note;

    /** 提交用户 */
    @Excel(name = "提交用户")
    private Long userId;

    /** 提交时间 */
    @Excel(name = "提交时间")
    private Date createTime;
}
