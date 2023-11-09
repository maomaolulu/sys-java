package may.yuntian.external.province.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.province.vo.SysUserVo;

import java.io.Serializable;
import java.util.Date;

/**
 * 省报送-基本信息实体类
 * @author: liyongqiang
 * @create: 2023-04-06 09:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pro_basic_info")
@EqualsAndHashCode(callSuper = false)
public class BasicInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** projectId **/
    private Long projectId;
    /** 项目编号 **/
    private String code;
    /** 检测类别（N2:两个数字字符。10：定期检测；20：监督检测；30：评价检测；31：现状评价；32：控制效果评价；33：预评价） **/
    private String checkType;
    /** 项目负责人 **/
    private String projectDirectorName;
    /** 项目负责人id（sys_user_id） **/
    private Long projectDirectorId;
    /** 报告出具日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;
    /** 报告签发人 **/
    private String issuer;
    /** 填表人 **/
    private String preparer;
    /** 现场调查开始日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginSurveyDate;
    /** 现场调查结束日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endSurveyDate;
    /** 现场采样、测量开始日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginSamplingDate;
    /** 现场采样、测量结束日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endSamplingDate;
    /** 委托机构名称 **/
    private String entrustOrgName;
    /** 委托机构统一社会信用代码 **/
    private String entrustCreditCode;
    /** 委托时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date entrustDate;
    /** 受检单位名称 **/
    private String empName;
    /** 受检单位统一社会信用代码 **/
    private String empCreditCode;
    /** 经济类型 **/
    private String economicType;
    /** 经济类型编码 **/
    private String economicTypeCode;
    /** 行业类别 **/
    private String industryCategory;
    /** 行业类别编码 N4:4位数字字符，定长；多个行业只能给一种！ 参考国家标准：2017年国民经济行业分类与代码(GB/T 4754-2017) **/
    private String industryCategoryCode;
    /** 企业规模编码（1：大；2：中；3：小；4：微型；5：不详） **/
    private String scaleCode;
    /** 注册地区名称 **/
    private String areaName;
    /** 注册地区编码 **/
    private String areaCode;
    /** 注册地址 **/
    private String regAddress;
    /** 职工总人数 **/
    private String employeesTotalNum;
    /** 接害总人数 **/
    private String contactHazardNum;
    /** 技术服务地区 **/
    private String serviceArea;
    /** 技术服务地区编码 **/
    private String serviceAreaCode;
    /** 技术服务地址 **/
    private String serviceAddress;
    /** 技术服务领域编码（N..4：最多为4位数字字符。 1：采矿业；2：化工、石化及医药；3：冶金、建材；4：机械制造、电力、纺织、建筑和交通运输等行业领域；5：核设施；6：核技术工业应用） **/
    private String fieldCode;
    /** 通讯地址 **/
    private String address;
    /** 邮政编码 **/
    private String postalCode;
    /** 法人姓名 **/
    private String legalPerson;
    /** 法人联系电话 **/
    private String legalPhone;
    /** 联系人 **/
    private String contactPerson;
    /** 联系电话 （格式为区号-号码或者手机号）**/
    private String contactPhone;
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
    /** 项目状态（0：待提交；1：待主管审核；2：主管驳回；3：待质控审核；4：质控驳回；5：质控推送成功） **/
    private Integer status;
    /** 主管驳回原因 **/
    private String chargeRejectReason;
    /** 质控驳回原因 **/
    private String qualityControlRejectReason;
    /** 报送日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String submitDate;
    /** 项目隶属公司（杭州、嘉兴、宁波安联等） **/
    private String belongCompany;

    /** 主管or质控用户角色标识值（1主管；2质控） **/
    @TableField(exist = false)
    private Integer viewer;
    /** al_project_user表数据回填字段 **/
    @TableField(exist = false)
    private SysUserVo sysUserVo;
    /** 项目数据来源：1检评，2评价，null表示质控页面（可查看所有数据） **/
    @TableField(exist = false)
    private Integer dataSource;
    /** 报送日期（结束）**/
    @TableField(exist = false)
    private String submitEndDate;

    public BasicInfo(Integer status, String submitDate, String updateBy, Date updateTime) {
        this.status = status;
        this.submitDate = submitDate;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }

}
