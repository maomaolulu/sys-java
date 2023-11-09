package may.yuntian.anliantest.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: anliantest
 * @description:    服务事项类！（服务事项开始节点：<serviceItem></>）
 * @author: liyongqiang
 * @create: 2022-04-26 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@XStreamAlias("serviceItem")
@TableName("sb_service_item")
public class ServiceItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *自增主键ID
     */
    @TableId
    @XStreamOmitField
    private Long id;

    @XStreamOmitField // 隐藏字段：在生成的xml报文中不出现此元素。
    private Long projectId;

    @XStreamOmitField
    private String name; // 参与人员的姓名

    /**
     * 服务事项编码
     */
    private String itemCode;
    /**
     * 服务事项名称
     */
    private String itemName;

    public ServiceItem(Long projectId, String itemCode, String itemName) {
        this.projectId = projectId;
        this.itemCode = itemCode;
        this.itemName = itemName;
    }
}
