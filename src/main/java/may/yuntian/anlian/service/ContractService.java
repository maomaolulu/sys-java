package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.vo.ContractStatisticVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 合同信息
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
public interface ContractService extends IService<ContractEntity> {
	
	/**
	 * 用于普通用户的分页查询
	 */
    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据条件查询所有信息并导出
     * @param params
     * @return
     */
    void exportList(HttpServletResponse response, Map<String, Object> params);
    
    
    /**
     * 根据企业ID查询是否已经存在于合同信息中
     * @param companyId 企业ID
     * @return boolean
     */
    Boolean notExistContractByCompany(Long companyId);
    
    /**
     * 根据合同编号查询是否已经存在于合同信息中
     * @param identifier 合同编号
     * @return boolean
     */
    public Boolean notExistContractByIdentifier(String identifier);
    
    /**
     * 根据合同ID获得合同编号后缀号
     * 用于生成样品编号
     * @param id
     * @return
     */
    String queryIdentifierSuffixById(Long id);
    
	/**
	 * 修改合同信息
	 * 当合同状态status为合同签订1、合同中止99时
	 */
	boolean updateContractById(ContractEntity contractEntity);
	
	/**
	 * 统计报表：根据业务员分组显示合同数量及总额
	 * @param queryVo
	 * @return
	 */
	List<Map<String, Object>> getReportAmountBySalesmen(ContractStatisticVo queryVo);
	
	/**
	 * 统计报表：根据合同签定日期分组显示合同数量及总额
	 * 统计日期类型(年Y,月M,周W,日D)
	 * @param queryVo
	 * @return
	 */
	List<Map<String, Object>> getReportAmountBySignDate(ContractStatisticVo queryVo);
	
	/**
	 * 统计报表：根据合同签定日期与业务员分组显示合同数量及总额
	 * 统计日期类型(年Y,月M,周W,日D)
	 * @param queryVo
	 * @return
	 */
	List<Map<String, Object>> getReportAmountBySignDateAndSalesmen(ContractStatisticVo queryVo);
	
	/**
	 * 统计报表：根据合同签定日期与合同类型分组显示合同数量及总额
	 * 统计日期类型(年Y,月M,周W,日D)
	 */
	List<Map<String, Object>> getReportAmountBySignDateAndType(ContractStatisticVo queryVo);
	
	/**
	 * 统计报表：根据合同签定日期与委托类型分组显示合同数量及总额
	 * 统计日期类型(年Y,月M,周W,日D)
	 */
	List<Map<String, Object>> getReportAmountBySignDateAndEntrustType(ContractStatisticVo queryVo);
    
}

