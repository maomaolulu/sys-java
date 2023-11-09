package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-10-25 17:30
 */
@Data
public class CompanyPublicVo implements Serializable {
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
     * 最近合作
     */
    private Date contractLast;
    /**
     * 客户状态(0:停用,1:正常)
     */
    private String status;
}
