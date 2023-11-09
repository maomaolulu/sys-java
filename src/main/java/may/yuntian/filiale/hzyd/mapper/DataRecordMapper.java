package may.yuntian.filiale.hzyd.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.filiale.hzyd.dto.AmountDto;
import may.yuntian.filiale.hzyd.dto.ContractDto;
import may.yuntian.filiale.hzyd.dto.ProjectDto;
import may.yuntian.filiale.hzyd.entity.DataRecord;
import may.yuntian.filiale.hzyd.vo.ContractVo;
import may.yuntian.filiale.hzyd.vo.FinanceInfoVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 12:01
 */
@Mapper
public interface DataRecordMapper extends BaseMapper<DataRecord> {

    /**
     * 合同管理-列表
     *
     * @param wrapper 查询条件
     * @return 结果
     */
    @Select("select id as contract_id, `type` as contract_type, identifier as contract_code, entrust_company as entrust_unit, entrust_company_id as entrust_unit_id, entrust_office_address as office_address, entrust_type, company_order as belong_company, business_source, salesmenid as salesman_id, salesmen as salesman,\n" +
            "total_money as contract_amount, commission_date as entrust_date, sign_date as contract_date, deal_status, contract_status\n" +
            "from t_contract\n" +
            "${ew.customSqlSegment} ")
    List<ContractVo> selectContractList(@Param(Constants.WRAPPER) QueryWrapper<ContractVo> wrapper);

    /**
     * 根据id查询合同信息
     *
     * @param id 合同id
     * @return 结果
     */
    @Select("select type as contract_type, identifier as contract_code, entrust_company as entrust_unit, entrust_office_address as office_address, entrust_type, company_order as belong_company, business_source, salesmen as salesman, contact as contact_person, + telephone as contact_phone, " +
            "commission_date as entrust_date, sign_date as contract_date, deal_status, contract_status, total_money as contract_amount, commission as business_cost, service_charge as service_cost, evaluation_fee as review_cost, subcontract_fee as subcontract_cost, other_expenses as other_cost, netvalue as net_value\n" +
            "from t_contract\n" +
            "where id = #{id} ;")
    ContractDto selectContractInfoById(@Param("id") Long id);

    /**
     * 根据contractId查询项目金额信息
     *
     * @param contractId 合同id
     * @return list
     */
    @Select("select ap.identifier as `code`, apa.total_money as amount, apa.netvalue as net_value, apa.commission as business_cost, apa.service_charge as service_cost, apa.evaluation_fee as review_cost, apa.subproject_fee as subcontract_cost, apa.other_expenses as other_cost\n" +
            "from al_project as ap\n" +
            "left join al_project_amount as apa\n" +
            "on ap.id = apa.project_id\n" +
            "where ap.contract_id = #{contractId} " +
            "order by ap.id asc;")
    List<FinanceInfoVo> selectProjectAmountByContractId(@Param("contractId") Long contractId);

    /**
     * 根据contractId查询项目基本信息
     *
     * @param contractId 合同id
     * @return 结果
     */
    @Select("select ydr.project_id, ap.type as item_type, ap.identifier as item_code, ap.company as emp_name, ap.office_address as emp_address, ap.contact as contact_person, ap.telephone as contact_phone, apd.claim_end_date as require_finish_date, ap.remarks as remark," +
            "ydr.project_id, ydr.detect_info_ids, ydr.quantities,ydr.total, ydr.detect_type, ydr.test_item, ydr.test_start_date, ydr.test_end_date, ydr.test_number\n" +
            "from yd_data_record as ydr\n" +
            "left join al_project as ap on ydr.project_id = ap.id\n" +
            "left join al_project_date as apd on ydr.project_id = apd.project_id\n" +
            "where ydr.contract_id = #{contractId} " +
            "order by ydr.project_id asc;")
    List<ProjectDto> selectProjectBasicInfoByContractId(@Param("contractId") Long contractId);

    /**
     * 项目管理-列表
     *
     * @param wrapper 查询条件
     * @return 结果
     */
    @Select("select ap.id as project_id, ap.identifier as item_code, ap.contract_id, ap.contract_identifier as contract_code, ap.type as item_type, ap.company as emp_name, ap.total_money as item_amount, ap.charge as project_leader, ap.`status` as project_status, apd.task_release_date as issue_date, apd.project_finish_date\n" +
            "from al_project as ap\n" +
            "left join al_project_date as apd on ap.id = apd.project_id\n" +
            "${ew.customSqlSegment} ")
    List<ProjectDto> selectProjectList(@Param(Constants.WRAPPER) QueryWrapper<ProjectDto> wrapper);

    /**
     * 根据id查询项目信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Select("select ap.id as project_id, ap.contract_identifier as contract_code, ap.identifier as item_code, ap.entrust_company as entrust_unit, ap.entrust_company_id as entrust_unit_id, ap.contact as contact_person, ap.telephone as contact_phone, ap.type as item_type, ap.company as emp_name, ap.company_id as emp_name_id, ap.office_address as emp_address, ap.remarks as remark, apd.claim_end_date as require_finish_date, apa.total_money as item_amount, apa.commission as business_cost, apa.service_charge as service_cost, apa.evaluation_fee as review_cost, apa.subproject_fee as subcontract_cost, apa.other_expenses as other_cost, ydr.id as data_record_id,ydr.total, ydr.detect_info_ids, ydr.quantities, ydr.detect_type, ydr.test_item, ydr.test_start_date, ydr.test_end_date, ydr.test_number\n" +
            "from al_project as ap\n" +
            "left join al_project_date as apd on ap.id = apd.project_id\n" +
            "left join al_project_amount as apa on ap.id = apa.project_id\n" +
            "left join yd_data_record as ydr on ap.id = ydr.project_id\n" +
            "where ap.id = #{projectId} ;")
    ProjectDto selectEditInfo(@Param("projectId") Long projectId);

    /**
     * 查询合同及项目金额
     *
     * @param projectId  项目id
     * @return 结果
     */
    @Select("select tc.id as contract_id, tc.total_money as contract_amount, tc.commission as contract_business_cost, tc.evaluation_fee as contract_review_cost, tc.subcontract_fee as contract_subcontract_cost, tc.service_charge as contract_service_cost, tc.other_expenses as contract_other_cost, tc.netvalue as contract_net_value, apa.total_money as item_amount, apa.commission as item_business_cost, apa.evaluation_fee as item_review_cost, apa.subproject_fee as item_subcontract_cost, apa.service_charge as item_service_cost, apa.other_expenses as item_other_cost, apa.netvalue as item_net_value\n" +
            "from t_contract as tc\n" +
            "left join al_project_amount as apa on tc.id = apa.contract_id\n" +
            "where apa.project_id = #{projectId} ;")
    AmountDto selectContractAndProjectAmount(@Param("projectId") Long projectId);

    /**
     * 根据合同id多表删除
     */
    @Delete("DELETE t, ap, apa, ydr FROM t_contract AS t, al_project AS ap, al_project_amount AS apa, yd_data_record AS ydr WHERE t.id = #{contractId} and ap.contract_id = #{contractId} and apa.contract_id = #{contractId} and ydr.contract_id = #{contractId} ;")
    int deleteTablesByContractId(@Param("contractId") Long contractId);

}
