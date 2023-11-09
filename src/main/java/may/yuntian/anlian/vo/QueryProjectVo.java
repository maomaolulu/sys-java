package may.yuntian.anlian.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 项目信息
 * 用于项目数据查询条件的参数
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-11-07
 */
@Data
public class QueryProjectVo implements Serializable {
	private static final long serialVersionUID = 1L;

//	/**
//	 * 父级ID,一级指令为0，项目隶属：主项目、子项目
//	 */
//	@ApiModelProperty(value="父级ID,一级指令为0，项目隶属：主项目、子项目")
//	private Long parentid;
	/**
	 *合同ID
	 */
	@ApiModelProperty(value="项目编号·模糊搜索")
	private Long contractId;
	/**
	 *合同编号
	 */
	@ApiModelProperty(value="合同编号·模糊搜索")
	private String contractIdentifier;
	/**
	 * 项目编号
	 */
	@ApiModelProperty(value="项目编号·模糊搜索")
	private String identifier;
	/**
	 * 受检企业名称
	 */
	@ApiModelProperty(value="受检企业名称·模糊搜索")
	private String company;
	/**
	 * 委托单位名称 
	 */
	@ApiModelProperty(value="委托单位名称·模糊搜索")
	private String entrustCompany;

	/**
	 * 联系电话
	 */
	@ApiModelProperty(value="联系电话·模糊搜索")
	private String telephone;
	/**
	 * 项目名称
	 */
	@ApiModelProperty(value="项目名称·模糊搜索")
	private String projectName;
	/**
	 * 委托类型
	 */
	@ApiModelProperty(value="委托类型")
	private String entrustType;
	
	/**
	 * 业务来源
	 */
	@ApiModelProperty(value="业务来源")
	private String businessSource;
	
	/**
	 * 项目类型
	 */
	@ApiModelProperty(value="项目类型")
	private String type;
	
	/**
	 * 项目类型(多选)
	 */
	@ApiModelProperty(value="项目类型·多选")
	private String types;
	
	/**
	 * 项目状态
	 */
	@ApiModelProperty(value="项目状态")
	private Integer status;
	/**
	 * 项目隶属公司
	 */
	@ApiModelProperty(value="项目隶属公司")
	private String companyOrder;
	
	/**
	 * 业务员
	 */
	@ApiModelProperty(value="业务员")
	private String salesmen;
	
	/**
	 * 省份
	 */
	@ApiModelProperty(value="省份")
	private String province;
	/**
	 * 城市
	 */
	@ApiModelProperty(value="城市")
	private String city;
	/**
	 * 区县
	 */
	@ApiModelProperty(value="区县")
	private String area;

    /**
     * 权限标识
     */
    @ApiModelProperty(value="权限标识")
    private String subjection;
	
	/**
	 * 检评报告归档日期开始
	 */
	@ApiModelProperty(value="检评报告归档日期开始")
	private String planRptissuDateMin;
	/**
	 * 检评报告归档日期结束
	 */
	@ApiModelProperty(value="检评报告归档日期结束")
	private String planRptissuDateMax;
	/**
	 * 项目签订开始日期signDate
	 */
	@ApiModelProperty(value="开始日期")
	private	String startDate;
	/**
	 * 项目签订开始日期signDate
	 */
	@ApiModelProperty(value="结束日期")
	private	String endDate;
    /**
     * 项目负责人
     */
    @ApiModelProperty(value="负责人")
    private	String charge;

	@Override
	public String toString() {
		return "QueryProjectVo [identifier=" + identifier + "contractId=" + contractId + ", company=" + company
				+ ", telephone=" + telephone + ", projectName=" + projectName + ", entrustType=" + entrustType
				+ ", type=" + type + ", status=" + status + ", companyOrder=" + companyOrder + ", salesmen=" + salesmen
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	
	
}
