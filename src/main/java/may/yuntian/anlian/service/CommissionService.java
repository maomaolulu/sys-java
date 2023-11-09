package may.yuntian.anlian.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.common.utils.PageUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 提成记录
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-09
 */
public interface CommissionService extends IService<CommissionEntity> {

	PageUtils queryPage(Map<String, Object> params);

    /**
     * 显示全部业务员销售目标信息列表
     * @return
     */
	void listAll(HttpServletResponse response, Map<String, Object> params);

	 /**
     * 根据时间段生成提成记录
     */
	List<CommissionEntity> getListByCommissionDate(CommissionEntity commission);
	
    /**
     * 通过项目id和提成类型获取列表
     */
    CommissionEntity getCommissionByProjectIdAndType(Long projectId, String type);

    /**
     * 通过项目id和获取列表
     */
    public List<CommissionEntity> getCommissionListByProjectId(List<Long> projectIds);
    
    /**
     * 根据查询条件软删除绩效提成
     * @param params
     */
    void updateStateByParams(Map<String, Object> params);
    
    /**
     * 获取已提成项目id
     */
    List<Long> getNotNullIdList();
    
    
    /**
     * 根据提成日期查询提成列表的项目ID数组
     * @param commissionStratDate
     * @param commissionEndDate
     * @return
     */
    List<Long> getListByCommissionDate(String commissionStratDate, String commissionEndDate);

}