package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.vo.ThisYearReceiptVo;
import may.yuntian.anlian.vo.TotalMoneyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gy
 * @Description 财务数据看板
 * @date 2023-07-12 13:43
 */
@Mapper
public interface FinanciaDashboardMapper {

    /**
     * 查询近两年的项目金额数据
     */
    @Select("SELECT DATE_FORMAT(apd.sign_date,'%Y-%m') sign_date, SUM(ap.total_money)/10000 total_money\n" +
            "FROM al_project ap\n" +
            "LEFT JOIN al_project_date apd ON apd.project_id = ap.id\n" +
            "${ew.customSqlSegment}")
    List<TotalMoneyVo> getTotalMoney(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);

    /**
     * 查询回款
     */
    @Select("SELECT DATE_FORMAT(ta.happen_time,'%Y-%m') sign_date, SUM(ta.amount)/10000 total_money \n" +
            "FROM `t_account` ta\n" +
            "LEFT JOIN al_project ap ON ta.project_id = ap.id\n" +
            "${ew.customSqlSegment}")
    List<TotalMoneyVo> getReceiptMoney(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);

    /**
     * 查询开票
     */
    @Select("SELECT DATE_FORMAT(ta.happen_time,'%Y-%m') sign_date, SUM(ta.invoice_amount)/10000 total_money \n" +
            "FROM `t_account` ta\n" +
            "LEFT JOIN al_project ap ON ta.project_id = ap.id\n" +
            "${ew.customSqlSegment}")
    List<TotalMoneyVo> getInvoiceMoney(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);

    /**
     * 查询今年回款情况
     */
    @Select("SELECT ta.happen_time, ta.amount, ap.business_source, ap.company_order\n" +
            "FROM `t_account` ta\n" +
            "LEFT JOIN al_project ap ON ta.project_id = ap.id\n" +
            "${ew.customSqlSegment}")
    List<ThisYearReceiptVo> getThisYearReceipt(@Param(Constants.WRAPPER) Wrapper<Object> wrapper);

}
