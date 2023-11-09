package may.yuntian.filiale.hzyd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 合同dto
 *
 * @author: liyongqiang
 * @create: 2023-08-11 13:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto implements Serializable {
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
     * 委托单位id // Todo：委托单位下拉选择（有相关接口lixin）
     */
    private Long entrustUnitId;
    /**
     * 办公地址
     */
    private String officeAddress;
    /**
     * 隶属公司
     */
    private String belongCompany;
    /**
     * 委托类型：单位委托、政府委托、招投标项目
     */
    private String entrustType;
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
     * 委托日期
     */
    private String entrustDate;
    /**
     * （合同）签订日期
     */
    private String contractDate;
    /**
     * 合同金额
     */
    private String contractAmount;
    /**
     * 业务费
     */
    private String businessCost;
    /**
     * 服务费
     */
    private String serviceCost;
    /**
     * 评审费
     */
    private String reviewCost;
    /**
     * 分包费
     */
    private String subcontractCost;
    /**
     * 其它支出
     */
    private String otherCost;
    /**
     * 合同净值
     */
    private String netValue;
    /**
     * 协议状态：0未回，1已回
     */
    private Integer dealStatus;
    /**
     * 合同签订状态：0未回，1已回
     */
    private Integer contractStatus;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 合同回款方式
     */
    private String paymentMethod;
    /**
     * 预付款比例
     */
    private Double prepaymentRatio;


}
