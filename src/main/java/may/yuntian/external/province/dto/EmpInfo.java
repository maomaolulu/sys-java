package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 受检单位信息类!（受检单位信息开始节点：<empInfo>）
 * @author: liyongqiang
 * @create: 2023-04-04 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("empInfo")
public class EmpInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 受检单位名称
     */
    private String empName;
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 经济类型编码 N3
     */
    private Integer economicTypeCode;
    /**
     * 行业类别编码 N4
     */
    private Integer industryCategoryCode;
    /**
     * 企业规模编码：1大 2中 3小 4微型 5不详 N1
     */
    private Integer scaleCode;
    /**
     * 注册地区编码 N9
     */
    private Integer areaCode;
    /**
     * 注册地区名称
     */
    private String areaName;
    /**
     * 注册地址
     */
    private String regAddress;
    /**
     * 在职职工总人数 N..6
     */
    private Integer employeesTotalNum;
    /**
     * 接触危害因素员工总人数 N..6
     */
    private Integer contactHazardNum;

    /**
     *  技术服务地区列表开始节点：<serviceAreaList>
     */
    private List<ServiceArea> serviceAreaList;

    /**
     * 服务的用人单位技术服务领域编码 N..4
     */
    private Integer fieldCode;
    /**
     * 通讯地址
     */
    private String address;
    /**
     * 邮政编码 N6
     */
    private Integer postalCode;
    /**
     * 法人姓名
     */
    private String legalPerson;
    /**
     * 法人联系电话
     */
    private String legalPhone;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;

}
