package may.yuntian.filiale.hzyd.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 亿达-项目数据记录表实体
 *
 * @author: liyongqiang
 * @create: 2023-08-11 11:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("yd_data_record")
@EqualsAndHashCode(callSuper = false)
public class DataRecord implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目类型（放射卫生检测，放射卫生预评，放射卫生控评，辐射环评登记表，辐射环评报告表，年度评估，退役环评，一般环评验收报告，辐射安全许可证，环保竣工验收，环境检测，个人剂量监测）
     */
    private String itemType;
    /**
     * 检测信息表id（多个以英文逗号分隔）
     */
    private String detectInfoIds;
    /**
     * (设备或房间)数量（与detectInfoIds对应）
     */
    private String quantities;
    /**
     * (设备或房间)总数量
     */
    private String total;
    /**
     * 检测类别：1年检（默认），2验收，3复测，4本底
     */
    private Integer detectType;
    /**
     * 检测项目：1性能（默认），2防护，3性能+防护
     */
    private Integer testItem;

    /**
     * 检测开始日期
     */
    private Date testStartDate;
    /**
     * 检测结束日期
     */
    private Date testEndDate;
    /**
     * 检测人数
     */
    private Integer testNumber;

    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;

}
