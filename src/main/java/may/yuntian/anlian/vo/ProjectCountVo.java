package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LiXin
 * @email
 * @date 2022-07-26
 */
@Data
public class ProjectCountVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	private Long id;

    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private Integer status;
    /**
     * 项目类型
     */
    private String type;
    /**
     * 负责人
     */
    private String charge;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 项目报告相关信息录入表ID
     */
    private Long projectCountId;
    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    private String businessSource;


    /**
     * 调查信息录入
     */
    private Date survey;
    /**
     * 调查信息最近一次操作时间
     */
    private Date surveyLast;
    /**
     * 方案提交
     */
    private Date planCommit;
    /**
     * 方案提交最近一次操作时间
     */
    private Date planCommitLast;
    /**
     * 采样记录时间
     */
    private Date sampleRecord;
    /**
     * 采样记录最近一次操作时间
     */
    private Date sampleRecordLast;
    /**
     * 送样单生成
     */
    private Date deliverySheet;
    /**
     * 送样单最近一次生成
     */
    private Date deliverySheetLast;
    /**
     * 检测结果录入
     */
    private Date testResult;
    /**
     * 检测结果最近一次操作
     */
    private Date testResultLast;
    /**
     * 结果计算
     */
    private Date resultMath;
    /**
     * 结果计算最近一次操作
     */
    private Date resultMathLast;
    /**
     * 报告生成时间
     */
    private Date reportGenerate;
    /**
     * 报告生成最近一次操作时间
     */
    private Date reportGenerateLast;
    /**
     * 最终报告上传时间(首次上传)
     */
    private Date finalReport;
    /**
     * 最终报告最近一次上传时间
     */
    private Date finalReportLast;
	

}
