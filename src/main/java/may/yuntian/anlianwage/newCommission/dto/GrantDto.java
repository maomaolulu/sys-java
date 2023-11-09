package may.yuntian.anlianwage.newCommission.dto;

import lombok.Data;
import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;

import java.util.List;

@Data
public class GrantDto {
    private String grantType;// month 月度 year 年度
    private List<NewCommissionEntity> commissionList;//需发放绩效列表
}
