package may.yuntian.homepage.domain.vo;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author gy
 * @Description 项目完成时效详情vo
 * @date 2023-07-06 15:12
 */
@Data
public class ProjectSpeedDetailVo {
    /** 公司隶属 */
    private String companyOrder;
    /** 项目类型 */
    private String type;
    /** 项目编号 */
    private String identifier;
    /** 项目名称 */
    private String projectName;
    /** 签订日期 */
    private String signDate;
    /** 任务安排时效 */
    private Integer time1;
    /** 反应时间时效 */
    private Integer time2;
    /** 方案制定时效 */
    private Integer time3;
    /** 采样完成时效 */
    private Integer time4;
    /** 数据完成时效 */
    private Integer time5;
    /** 报告完成时效 */
    private Integer time6;
}
