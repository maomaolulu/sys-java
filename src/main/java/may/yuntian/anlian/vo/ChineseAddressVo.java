package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-09-06 15:26
 */
@Data
public class ChineseAddressVo implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 地区级别 地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县 4-乡镇街道
     */
    private int regionLevel;
}
