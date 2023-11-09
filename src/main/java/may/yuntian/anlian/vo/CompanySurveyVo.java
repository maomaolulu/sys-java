package may.yuntian.anlian.vo;

import lombok.Data;
import may.yuntian.anlian.entity.SampleImgEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 检评公示信息
 * @author zhanghao
 * @date 2022-04-14
 */
@Data
public class CompanySurveyVo implements Serializable {
	private static final long serialVersionUID = 1L;
//ap.id,cs.company,cs.contact,cs.office_address,cs.detection_type,apd.report_issue,cs.accompany,cs.survey_date
	/**
	 * 自增主键ID
	 */
	private Long id;
	/**
	 * projectId
	 */
	private Long projectId;
	/**
	 * 单位名称
	 */
	private String company;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 单位地址
	 */
	private String officeAddress;
	/**
	 * 检测类型
	 */
	private String detectionType;

    /**
     * 类型
     */
    private String type;

    /**
     * 评价报告编制人PJ
     */
    private String pPreparePerson;

    /**
     * 现场调查人员pj
     */
    private String fieldInvestigatorsPj;

    /**
     * 现场检测。现场检测人员pj
     */
    private String fieldSamplingPj;

	/**
	 * 报告签发日期
	 */
	private Date reportIssue;
	/**
	 * 陪同人
	 */
	private String accompany;
	/**
	 * 陪同时间
	 */
	private Date surveyDate;
	/**
	 * 现场调查人员
	 */
	private String fieldInvestigators;

	/**
	 * 现场检测。现场检测人员
	 */
	private String fieldSampling;

	/**
	 * 现场检测图
	 */
	private List<SampleImgEntity> imgs;
//	/**
//	 * 0：未公司，1:已公示
//	 */
//	private Integer publicityStatus;
	/**
	 * 技术服务项目组人员
	 */
	private String technicalPersons;
	/**
	 * 项目采样陪同人
	 */
	private String samplingCompany;
	/**
	 * 项目采样时间(项目采样陪同时间)
	 */
	private String samplingDate;
	/**
	 * 项目采样开始时间
	 */
	private Date reportStartDate;
	/**
	 * 项目采样结束时间
	 */
	private Date reportEndDate;

	/**
	 * 0：未申请，1：负责人申请到部门主管，2：主管申请到质控，3：主管驳回，4：质控驳回，5：公示
	 */
	private Integer applyPublicityStatus;
	/**
 	* 主管驳回
	 * @return
	*/
	private String directorReject;
	/**
 	* 质控驳回
	 * @return
	*/
	private String controlReject;

	/**
	 * pdf 参数
	 * @return
	 */
	private Map<String,Object> map;
	/**
	 * pdf 路径
	 * @return
	 */
	private String path;/**
	 * 项目负责人
	 * @return
	 */
	private String charge;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 *0：项目公示最后操作时间
	 */
	private Date publicityLastTime;
	/**
	 * 实验室人员（用于项目公示）
	 */
	private String laboratoryPerson;
	/**
	 * 采样陪同时间排序
	 */
	private String samplingDate1;
	/**
	 * 隶属公司
	 */
	private String hideRemark;
	/**
	 * 备注
	 */
	private String publicityRemark;
	/**
	 *修改时间
	 */
	private Date updatetime;
	/**
	 *入库时间
	 */
	private Date addtime;
}
