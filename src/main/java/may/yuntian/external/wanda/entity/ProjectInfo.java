package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目基本信息实体
 * @author: liyongqiang
 * @create: 2023-03-06 10:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wanda_project_info")
public class ProjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Long id;
    /** 项目id **/
    private Long projectId;
    /** 同步成功的项目id（由万达接口返回） **/
    private String belongThirdPrjId;
    /** 检测类型（2：定期检测，3：现状评价，4：控制效果评价） **/
    private String checkType;
    /** 受检单位统一社会信用代码 **/
    private String comCode;
    /** 受检单位名称 **/
    private String comName;
    /** 项目编号 **/
    private String projectCode;
    /** 项目名称 **/
    private String projectName;
    /** 项目行政区划 **/
    private String projectArea;
    /** 项目行政区划代码（参考“行政区域-字典”） **/
    private String projectAreaCode;
    /** 项目详细地址 **/
    private String projectAddress;
    /** 采样开始日期 **/
    private String checkDateStart;
    /** 采样结束日期 **/
    private String checkDateEnd;
    /** 对应的gbz2.1标准（1:2007标准，2:2019标准） **/
    private Integer targetStandardId;
    /** 报告出具日期 **/
    private String reportDate;
    /** 职业病危害风险类别（1：严重，2：一般） **/
    private Integer riskLevel;
    /** 接害人数 **/
    private Integer victimsNum;
    /** 劳动定员统计人数 **/
    private Integer labourCountNum;
    /** 登记人姓名（检测报告签发人） **/
    private String registerName;
    /** 登记时间（签发时间） **/
    private String registerTime;
    // 项目节点状态：项目负责人：0，2	5（页面不允许再进行编辑操作）	主管：1，4	质控：3
    /** 记录项目节点状态（0：待提交(默认)，1：待主管审核，2：主管驳回，3：待质控审核，4：质控驳回，5：已推送。） **/
    private Integer nodeStatus;
    /** 主管驳回原因 **/
    private String chargeReject;
    /** 质控驳回原因 **/
    private String qualityControlReject;
    /** 创建者 **/
    private String createBy;
    /** 创建时间 **/
    private Date createTime;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    /** 备注 **/
    private String remark;
    /** 项目隶属公司 **/
    private String belongCompany;
    /** 推送时间 **/
    private Date pushTime;

    /** 备用冗余字段：是否提交（默认0未提交，1已提交） **/
    @TableField(exist = false)
    private Integer isSubmit;
    /** 负责人 **/
    @TableField(exist = false)
    private String charge;
    /** 区分：1主管，2质控 **/
    @TableField(exist = false)
    private Integer viewer;
    /** 项目状态 **/
    @TableField(exist = false)
    private Integer status;
    /** 1检评项目，2评价项目 **/
    @TableField(exist = false)
    private Integer systemFlag;
    /** 查询开始时间 **/
    @TableField(exist = false)
    private String startTime;
    /** 查询截止时间 **/
    @TableField(exist = false)
    private String endTime;
    /** 项目类型 **/
    @TableField(exist = false)
    private String type;


}
