package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * @Description 企业微信消息记录表
 * @Date 2023/4/11 13:35
 * @Author maoly
 **/
@Data
@TableName("wecom_message_record")
public class WeComMessageEntity {

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 留言发送人姓名
     */
    private String sendUser;
    /**
     * 留言发送人id
     */
    private String sendUserId;
    /**
     * 留言接收人
     */
    private String receiveUser;
    /**
     * 留言接收人id
     */
    private String receiveUserId;
    /**
     * 录入人姓名
     */
    private String receiveLeaderUser;
    /**
     * 录入人id
     */
    private String receiveLeaderUserId;
    /**
     * 留言时间
     */
    private String messageDate;
    /**
     * 留言内容
     */
    private String messageContent;
    /**
     * 创建时间
     */
    private Date createDate;
}
