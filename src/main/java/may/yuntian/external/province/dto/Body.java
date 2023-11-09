package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-04 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("body")
public class Body implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 报告列表开始节点：<reportList></reportList>
     */
    private List<Report> reportList;
}
