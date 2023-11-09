package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 合同查询vo
 *
 * @author: liyongqiang
 * @create: 2023-08-11 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 委托单位
     */
    private String entrustUnit;
    /**
     * 委托单位id
     */
    private Long entrustUnitId;
    /**
     * 办公地址
     */
    private String officeAddress;
    /**
     * 委托类型
     */
    private String entrustType;
    /**
     * 隶属公司
     */
    private String belongCompany;
    /**
     * 业务来源
     */
    private String businessSource;
    /**
     * 业务员
     */
    private String salesman;
    /**
     * 业务员id
     */
    private Integer salesmanId;
    /**
     * 委托（开始）日期
     */
    private String entrustDate;
    /**
     * 委托结束日期
     */
    private String entrustEndDate;
    /**
     * （合同）签订（开始）日期
     */
    private String contractDate;
    /**
     * （合同）签订结束日期
     */
    private String contractEndDate;
    /**
     * 合同金额
     */
    private String contractAmount;
    /**
     * 协议状态
     */
    private Integer dealStatus;
    /**
     * 协议状态（编辑后）
     */
    private Integer dealStatusAfter;
    /**
     * 合同签订状态
     */
    private Integer contractStatus;
    /**
     * 合同签订状态（编辑后）
     */
    private Integer contractStatusAfter;

}
