package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.dto.ContractExportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.ContractEntity;

import java.util.List;

/**
 * 合同信息
 * 数据持久层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@Mapper
public interface ContractMapper extends BaseMapper<ContractEntity> {
	/**
	 * 根据父级合同ID修改父级合同的子集数量
	 * 用于新增子项合同时
	 * @param id
	 * @return
	 */
	@Update("UPDATE t_contract SET child_quantity = child_quantity+1 WHERE id = '${id}'")
	int addChildQuantityById(Long id);

	/**
	 * 根据父级合同ID修改父级合同的子集数量
	 * 用于删除子项合同时
	 * @param id
	 * @return
	 */
	@Update("UPDATE t_contract SET child_quantity = child_quantity-1 WHERE id = '${id}' AND child_quantity>0")
	int deleteChildQuantityById(Long id);

	/**
	 * 根据合同ID修改合同状态status
	 * @param id
	 * @param status
	 * @return
	 */
	@Update("UPDATE t_contract SET status = #{status} WHERE id = #{id}")
	int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

	/**
	 * 变更合同编号
	 *
	 * @param identifier    原合同编号
	 * @param newIdentifier 新合同编号
	 */
	@Update("UPDATE t_contract SET identifier = #{newIdentifier} WHERE identifier = #{identifier}")
	void changeIdentifier(@Param("identifier") String identifier, @Param("newIdentifier") String newIdentifier);

	/**
	 * 检查合同编号是否存在
	 *
	 * @param contractIdentifier 合同编号
	 * @return 合同信息
	 */
	@Select("SELECT identifier FROM t_contract WHERE identifier = #{identifier}")
	ContractEntity checkContractIdentifier(@Param("identifier") String contractIdentifier);

	/**
	 * 根据id更新deal_status_time
	 *
	 * @param id 合同id
	 * @return row
	 */
	@Update("UPDATE t_contract SET deal_status_time = null WHERE id = #{id}")
	int updateDealStatusTimeById(@Param("id") Long id);

	/**
	 * 根据id更新contract_status_time
	 *
	 * @param id 合同id
	 * @return row
	 */
	@Update("UPDATE t_contract SET contract_status_time = null WHERE id = #{id}")
	int updateContractStatusTimeById(@Param("id") Long id);


    @Select("SELECT id,identifier,project_name,company,entrust_company,company_order,business_source,type,contact,telephone,total_money,netvalue,commission,evaluation_fee,subcontract_fee,service_charge,other_expenses,contract_status,deal_status,salesmen,commission_date,sign_date" +
            " FROM t_contract ${ew.customSqlSegment}")
    List<ContractExportDto> exportList(@Param(Constants.WRAPPER) Wrapper<ContractEntity> wrapper);
}
