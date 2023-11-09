package may.yuntian.anlian.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Date 2023/4/11 14:04
 * @Author maoly
 **/
@Data
public class WeComMessageDTO implements Serializable {
    private static final long serialVersionUID = 186431641915975120L;

    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 留言时间
     */
    private String messageDate;
    /**
     * 留言内容
     */
    private String messageContent;
}
