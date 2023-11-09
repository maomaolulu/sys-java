package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上报单位信息类（上报单位开始节点：<writeOrgInfo>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("writeOrgInfo")
public class WriteOrgInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 上报机构名称
     */
    private String orgName;

}
