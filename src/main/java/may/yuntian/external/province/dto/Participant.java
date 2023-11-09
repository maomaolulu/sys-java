package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 参与人员类！（参与人员开始节点：<participant>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("participant")
public class Participant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;
    /**
     *  服务事项列表开始节点：<serviceItemList></>
     */
    private List<ServiceItem> serviceItemList;

}
