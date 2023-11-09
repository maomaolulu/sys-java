package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@Mapper
public interface ProjectAmountMapper extends BaseMapper<ProjectAmountEntity> {

    /**
     * 根据合同id 统计关联项目的各个金额回填至对应的合同信息中
     * @param contractId
     * @return
     */
    @Select("SELECT SUM(IFNULL(total_money,0)) AS toltalMoney,SUM(IFNULL(commission,0)) AS commission,SUM(IFNULL(evaluation_fee,0)) AS evaluationFee," +
            "SUM(IFNULL(subproject_fee,0)) AS subprojectFee,SUM(IFNULL(service_charge,0)) AS serviceCharge,SUM(IFNULL(other_expenses,0)) AS otherExpenses,SUM(IFNULL(netvalue,0)) AS netvalue " +
            "FROM al_project_amount WHERE contract_id = #{contractId}")
    Map<String, BigDecimal> selectSumMoneyByContractId(Long contractId);

	
}
