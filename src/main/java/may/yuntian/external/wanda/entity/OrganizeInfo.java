package may.yuntian.external.wanda.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构基本信息实体
 * @author: liyongqiang
 * @create: 2023-03-04 11:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wanda_organize_info")
public class OrganizeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    /** 主键id **/
    private Integer id;
    /** 隶属公司 **/
    private String dataBelong;
    /** 机构id **/
    private String orgId;
    /** 技术服务机构统一社会信用代码 **/
    private String orgCode;
    /** 技术服务机构名称 **/
    private String orgName;
    /** 技术服务机构密钥 **/
    private String orgKey;
    /** 技术服务机构法人名称 **/
    private String legalName;
    /** 技术服务机构法人电话 **/
    private String legalPhone;
    /** 技术服务机构注册行政区划 **/
    private String registerArea;
    /** 技术服务机构注册行政区划编码 **/
    private String registerAreaCode;
    /** 技术服务机构注册地址详情 **/
    private String registerAddress;
    /** 创建者 **/
    private String createBy;
    /** 创建时间 **/
    private Date createTime;
    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;

}
