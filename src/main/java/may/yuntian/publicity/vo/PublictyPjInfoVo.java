package may.yuntian.publicity.vo;

import lombok.Data;
import may.yuntian.publicity.entity.EvalImageLibraryEntity;
import may.yuntian.publicity.entity.PublicityInfoEntity;

import java.util.Date;
import java.util.List;

@Data
public class PublictyPjInfoVo {

    private Long id;//项目ID

    private String company;//受检单位

    private String identifier;//项目编号
    /**
     * 项目隶属
     */
    private String companyOrder;

    private String contact;//联系人

    private String charge;//项目负责人

    private String officeAddress;//企业地址

    private String type;//项目类型

    private String projectName;//项目名称

    private String skillCharge;//技术负责人

    private String qualityCharge;//质控负责人

    private String projectCharge;//声明页项目负责人

    private String reportPreparation;//报告编制人

    private String surveyPeople;//报告现场调查人员

    private String samplePeople;//报告现场采样人员、现场检测人员

    private String surveyAccompany;//现场调查陪同人

    private String sampleAccompany;//现场采样陪同人

    private String surveyDate;//陪同调查日期

    private String reportStartDate;//报告采样开始日期(报告上展示)

    private String reportEndDate;//报告采样结束日期(报告上展示)

    private String sampleDate;//陪同采样日期

    private Date  reportCoverDate;//报告封面日期

    private String publictyPath;//公示文件路径

    private Date reportIssue;

    private List<EvalImageLibraryEntity> imgList;//评价采集影像数组

    private List<PublicityInfoEntity> publicityInfoList;//公示记录列表

    private Integer pubStatus;//公示状态 （6.待公示，7.已公示,8.公示失败）

    private Integer bindingStatus;//胶装状态（0.不显示，1.待胶装，2.已胶装）

    private Integer examineStatus;//审核状态（1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.审核通过）


}
