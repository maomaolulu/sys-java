package may.yuntian.anlianwage.newCommission.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlianwage.newCommission.vo.CommissionStatVo;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 新绩效提成表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-10-22 21:15:24
 */
@Mapper
public interface NewCommissionMapper extends BaseMapper<NewCommissionEntity> {

    @Select("SELECT SUM( IFNULL( c.accrual_amount, 0 ) ) as accrualCount, SUM( IFNULL( c.accrual_amount_month, 0 ) ) as monthCount, SUM( IFNULL( c.accrual_amount_year, 0 ) ) as yearCount FROM al_commission c " +
            " ${ew.customSqlSegment}")
    CommissionStatVo countListByWapper(@Param(Constants.WRAPPER) Wrapper<NewCommissionEntity> wrapper);
	
}
