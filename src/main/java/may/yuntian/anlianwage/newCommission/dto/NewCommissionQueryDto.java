package may.yuntian.anlianwage.newCommission.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewCommissionQueryDto {
    /**
     * 提成人
     */
    private String person;
    /**
     * 隶属公司
     */
    private String subjection;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 所属部门
     */
    private String dept;
    /**
     * 提成类型
     */
    private String commissionType;
    /**
     * 提成状态
     */
    private String commissionStatus;
    /**
     * 月度提成状态
     */
    private String commissionStatusMonth;
    /**
     * 年度提成状态
     */
    private String commissionStatusYear;
    /**
     * 计提日期-开始
     */
    private String accrualDateStart;
    /**
     * 计提日期-结束
     */
    private String accrualDateEnd;
    /**
     * 计提类型 month 月度  year 年度
     */
    private String accrualType;

}
