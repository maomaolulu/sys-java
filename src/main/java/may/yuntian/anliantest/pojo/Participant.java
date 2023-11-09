package may.yuntian.anliantest.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: anliantest
 * @description:    参与人员类！（参与人员开始节点：<participant>）
 * @author: liyongqiang
 * @create: 2022-04-26 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@XStreamAlias("participant")
@TableName("sb_participant")
public class Participant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *自增主键ID
     */
    @TableId
    @XStreamOmitField
    private Long id;

    @XStreamOmitField // 隐藏字段：在生成的xml报文中不出现此元素。
    private Long projectId;

    /**
     * 姓名
     */
    private String name;

    /**
     *  服务事项列表开始节点：<serviceItemList></>
     */
    @TableField(exist = false)
    private List<ServiceItem> serviceItemList;

    /**
     * 参与的服务事项名称：此处仅用于接收查询出来的数据，需要加 @TableField(exist = false) 忽略。
     */
    @XStreamOmitField // 隐藏字段：在生成的xml报文中不出现此元素。
    @TableField(exist = false)
    private String itemName;
    /**
     * 参与的服务事项编码
     */
    @XStreamOmitField // 隐藏字段：在生成的xml报文中不出现此元素。
    @TableField(exist = false)
    private String itemCode;

    public Participant(Long projectId, String name, List<ServiceItem> serviceItemList) {
        this.projectId = projectId;
        this.name = name;
        this.serviceItemList = serviceItemList;
    }
}
