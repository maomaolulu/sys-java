package may.yuntian.anlian.vo;

import lombok.Data;
import may.yuntian.anliantest.pojo.Participant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用于市级项目申报
 * @author zhanghao
 * @date 2022-05-18
 */
@Data
public class CityCompanySurveyVo implements Serializable {
	private static final long serialVersionUID = 1L;
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
	 * 统一社会信用代码
	 */
	private String creditCode;
	/**
	 * 经济类型编码
	 */
	private String economy;
	/**
	 * 行业类别
	 */
	private String industryCategory;
//	/**
//	 * 行业类别编码
//	 */
//	private Integer industryCategoryCode;
	/**
	 * 企业规模
	 */
	private Integer laborQuota;
//	/**
//	 * 注册地区编码
//	 */
//	private String districtRegAdd;
	/**
	 * 注册地区详细地址
	 */
	private String registeredAddress;
//	/**
//	 * 法人姓名
//	 */
//	private Integer contact;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系人电话
	 */
	private String telephone;
	/**
	 * 检测报告编号
	 */
	private String identifier;
	/**
	 * 检测类别编码
	 */
	private String detectionType;
////	/**
////	 * 检测类别编码
////	 */
//	private String detectionTypeCode;
	/**
	 * 服务的用人单位现场调查日期
	 */
	private Date surveyDate;
	/**
	 * 服务的用人单位现场采样、测量日期
	 */
	private Date testDate;
	/**
	 * 服务的用人单位现场采样、测量日期
	 */
	private List<Participant> participantList;
	/**
	 * 服务的用人单位现场采样、测量日期
	 */
	private Date year;
	/**
	 * 委托单位名称
	 */
	private String entrustCompany;
	/**
	 * 委托时间
	 */
	private Date entrustDate;


}
