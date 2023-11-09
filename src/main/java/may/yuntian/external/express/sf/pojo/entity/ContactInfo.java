package may.yuntian.external.express.sf.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 收寄双方信息
 * @author: liyongqiang
 * @create: 2023-05-22 19:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("exp_contact_info")
@EqualsAndHashCode(callSuper = false)
public class ContactInfo implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    @TableId
    private Integer id;

    @ApiModelProperty(value = "地址类型： 1，寄件方信息 2，收件方信息")
    private Integer contactType;

    @ApiModelProperty(value = "公司名称")
    private String company;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "手机号（手机/电话至少填一项）")
    private String mobile;

    @ApiModelProperty("联系电话（手机/电话至少填一项）")
    private String telephone;

    @ApiModelProperty(value = "国家2位代码，CN：中国", hidden = true)
    private String country;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String county;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "是否为常用寄件人")
    private Integer sendCommon;

    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
