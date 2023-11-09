package may.yuntian.external.oa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 跟进任务信息
 *
 * @Author yrb
 * @Date 2023/8/29 11:35
 * @Version 1.0
 * @Description 跟进任务信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomAdvanceTaskDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务主键ID
     */
    private Long id;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 业务状态
     */
    private Integer businessStatus;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String company;

    /**
     * 办公地址
     */
    private String officeAddress;

    /**
     * 数据所属公司（杭州、宁波、嘉兴、上海量远等）
     */
    private String dataBelong;

    /**
     * 跟进日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date advanceDate;

    /**
     * 跟进方式
     */
    private String advancePattern;

    /**
     * 跟进信息
     */
    private String advanceInformation;

    /**
     * 跟进人ID
     */
    private Long advanceId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 跟进记录ID
     */
    private Long recordId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 公司联系人
     */
    private List<CompanyContactEntity> companyContactList;

    /**
     * 跟进人
     */
    private String username;

    /**
     * 首次跟进
     */
    private Date advanceFirstTime;

    /**
     * 最近跟进
     */
    private Date advanceLastTime;

    /**
     * 跟进结果：0未成单，1已成单
     */
    private Integer advanceResult;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 首次跟进查询起始时间
     */
    private Date advanceFirstTimeStart;

    /**
     * 首次跟进查询结束时间
     */
    private Date advanceFirstTimeEnd;

    /**
     * 最近跟进查询起始时间
     */
    private Date advanceLastTimeStart;

    /**
     * 最近跟进查询结束时间
     */
    private Date advanceLastTimeEnd;

    /**
     * 注册地址
     */
    private String address;

    /**
     * 客户隶属
     */
    private String subjection;

    /**
     * 隶属人员
     */
    private String personBelong;

    /**
     * 是否有为老客(0:新客,1:老客)
     */
    private String ifHasFinished;
}
