package may.yuntian.wordgenerate.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementGenerateVo {
    private Long id;//项目ID
    private String identifier;//合同编号
    private String projectName;//项目名称
    private Long companyId;//受检单位ID
    private String company;//受检单位
    private String officeAddress;//受检单位地址
    private String legalname;//法定代表人-受检单位
    private String code;//统一社会信用代码||税号-受检单位
    private String contactPerson;//联系人-受检单位
    private String testPhone;//联系电话-受检单位
    private Long entrustCompanyId;//委托单位ID
    private String entrustCompany;//委托单位
    private String entrustOfficeAddress;//委托单位地址
    private String entrustLegalname;//法定代表人-委托单位
    private String entrustCode;//统一社会信用代码||税号-委托单位
    private String contact;//联系人-委托单位
    private String telephone;//联系电话-委托单位
    private String salesmen;//业务员
    private String phoneNumber;//业务员电话
    private String type;//项目类型
    private String signDate;//合同签订日期
    private BigDecimal totalMoney;
    private String totalMoneyStr;
}
