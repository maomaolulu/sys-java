package may.yuntian.publicity.vo;

import lombok.Data;
import may.yuntian.publicity.entity.ZjSampleImgEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PublicityInfoVo {
    private static final long serialVersionUID = 1L;

    private Long id;//项目ID

    private String company;//受检单位

    private String identifier;//项目编号

    private String contact;//联系人

    private String officeAddress;//企业地址

    /**
     * 检测类型
     */
    private String detectionType;//检测性质

    /**
     * 报告编制人（负责人）  注： 若负责人是张纯替换成戴文雅 若负责人是欧阳婷替换成王玲玲
     */
    private String charge;

    private Date reportCoverDate;//报告签发日期（报告封面日期）--F

    /**
     * 报告封面日期
     */
    private Date reportCoverDate2;

    private String accompany;//调查陪同人

    private String surveyDate;//调查日期

    private String samplingCompany;//采样陪同人--

    private String testDate;//采样日期--

    private String technicalPersons;//技术组服务人员--

    private String laboratoryPerson;//实验室人员--

    private String publicityPath;//公示pdf地址--

    private Integer pubStatus;//公示状态 （1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.待公示，7.已公示,8.公示失败）



    private List<ZjSampleImgEntity> sampleImageList;//现场采样影像图片地址数组

    /**
     * 现场调查人员
     */
    private String surveyUser;

    private String samplingUser;//现场采样人员

    private Map<String, Object> map;//生成pdf所需

    private String startDate;

    private String endDate;


    //todo 生成pdf所需
    private List<ZjSampleImgEntity> imgs;//现场采样影像图片地址数组

    /**
     * projectId
     */
    private Long projectId;

    /**
     * 报告签发日期
     */
    private Date reportIssue;

    /**
     * 现场调查人员
     */
    private String fieldInvestigators;

    /**
     * 现场检测。现场检测人员
     */
    private String fieldSampling;

    /**
     * 项目采样时间(项目采样陪同时间)
     */
    private String samplingDate;

    /**
     * 修改时间
     */
    private Date updatetime;
    /**
     * 入库时间
     */
    private Date addtime;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目隶属
     */
    private String companyOrder;

}
