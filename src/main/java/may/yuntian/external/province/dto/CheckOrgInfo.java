package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 检测机构信息类！（检测机构开始节点）
 * @author: liyongqiang
 * @create: 2023-04-04 13:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("checkOrgInfo")
public class CheckOrgInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 项目负责人
     */
    private String projectDirectorName;
    /**
     * 机构负责人
     */
    private String orgDirectorName;

}
