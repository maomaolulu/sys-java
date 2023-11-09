package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.external.province.annotation.XStreamYMDDateConverter;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托机构信息类!（委托机构信息开始节点：<entrustOrgInfo>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("entrustOrgInfo")
public class EntrustOrgInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 委托机构名称
     */
    private String entrustOrgName;
    /**
     * 委托机构统一社会信用代码
     */
    private String creditCode;
    /**
     * 委托时间
     */
    @XStreamConverter(value = XStreamYMDDateConverter.class)
    private Date entrustDate;

}
