package may.yuntian.homepage.domain.vo;

import lombok.Data;

/**
 * @author gy
 * @Description 项目类型和花费天数
 * @date 2023-07-05 11:18
 */
@Data
public class FinishSpeedVo {
    /** 项目类型 */
    private String type;
    /** 花费天数 */
    private Integer useDate;
}
