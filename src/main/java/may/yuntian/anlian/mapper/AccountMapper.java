package may.yuntian.anlian.mapper;

import java.math.BigDecimal;
import java.util.Map;

import may.yuntian.anlian.vo.MoneyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import may.yuntian.anlian.entity.AccountEntity;


/**
 * 收付款记录
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-11-05
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity> {

	   /**
	    * 根据查询条件统计金额
	    * @param userWrapper
	    * @return
	    */
	   @Select("SELECT SUM(IFNULL(a.amount,0)) AS amount , SUM(IFNULL(a.invoice_amount,0)) AS invoiceAmount"
	     + " FROM t_account a ${ew.customSqlSegment}")
       MoneyVo sumAmountByMyWrapper(@Param(Constants.WRAPPER) Wrapper<AccountEntity> userWrapper);
	   
	   /**
	    * 根据查询条件统计金额
	    * @param userWrapper
	    * @return
	    */
//	   @Select("SELECT SUM(IFNULL(total_money,0)) AS totalMoney"
//	     + " FROM t_account a,t_project p1 ${ew.customSqlSegment}")
       @Select("SELECT SUM(IFNULL(total_money,0)) AS totalMoney"
               + " FROM t_account a inner join al_project p1 on p1.id = a.project_id ${ew.customSqlSegment}")
       MoneyVo sumtotalMoneyByMyWrapper(@Param(Constants.WRAPPER) Wrapper<AccountEntity> userWrapper);
	
}
