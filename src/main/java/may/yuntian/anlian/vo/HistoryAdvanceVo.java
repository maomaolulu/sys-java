package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 历史跟进vo
 *
 * @author: liyongqiang
 * @create: 2023-10-25 08:46
 */
@Data
public class HistoryAdvanceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    private String company;
    /**
     * 社会统一信用代码
     */
    private String creditCode;
    /**
     * 数据所属公司
     */
    private String dataBelong;
    /**
     * 隶属人员
     */
    private String personBelong;
    /**
     * 是否为老客(0:新客,1:老客)
     */
    private Integer ifHasFinished;
    /**
     * 客户隶属
     */
    private String customerOrder;
    /**
     * 业务状态
     */
    private String serviceStatus;

    /**
     * 业务编号
     */
    private String taskCode;
    /**
     * 跟进人
     */
    private String followUser;
    /**
     * 跟进日期
     */
    private String followDate;
    /**
     * 跟进方式
     */
    private String followWay;
    /**
     * 跟进信息
     */
    private String followInfo;

}
