package may.yuntian.anlian.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-09-06 11:33
 */
@Data
public class CompanyDto implements Serializable {
    /**
     * 客户id
     */
    private Long id;
    /**
     * 客户隶属
     */
    private String dataBelong;
    /**
     * 企业名称
     */
    private String company;
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
     * 最近最近合作开始
     */
    private Date contractLastStart;
    /**
     * 最近最近合作结束
     */
    private Date contractLastEnd;
    /**
     * 业务状态
     */
    private Integer businessStatus;
    /**
     * 客户状态(0:停用,1:正常)
     */
    private String status;
}
