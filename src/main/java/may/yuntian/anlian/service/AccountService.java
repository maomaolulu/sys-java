package may.yuntian.anlian.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import may.yuntian.anlian.entity.AccountEntity;
import may.yuntian.anlian.vo.MoneyVo;
import may.yuntian.common.utils.PageUtils;

/**
 * 收付款记录
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-11-05 
 */
public interface AccountService extends IService<AccountEntity> {

	List<AccountEntity> listByProjectId(Long projectId);

	List<AccountEntity> queryPage(Map<String, Object> params);
	
	/**
	* 获取最新的项目款收款日期
	* @param projectId
	* @return
	*/
	Date getHappenTime(Long projectId);

	/**
    * 根据项目ID与款项类别查询收付款记录
    * @param projectId
    * @param type
    * @return 项目收款金额
    */
	BigDecimal getAmountByProjectIdAndType(Long projectId, Integer type);
	
	   /**
	    * 显示全部收付款的信息
	    * @return
	    */
	List<AccountEntity> listAll(Map<String, Object> params);
	
	
   /**
    * 根据查询条件统计金额
    * @param map
    * @return
    */
   MoneyVo sumAmountByWrapper(Map<String, Object> map);


    /**
     * 收付款收完款项后金额回填以供统计
     * @param projectId
     */
    void amountBackfill(Long projectId);
}