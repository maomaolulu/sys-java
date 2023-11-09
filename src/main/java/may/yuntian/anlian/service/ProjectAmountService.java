package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.vo.ProjectAmountVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ProjectAmountEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
public interface ProjectAmountService extends IService<ProjectAmountEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据条件查询项目ID列表
     */
    List<Long> getProjectIdsByParams(ProjectAmountVo queryVo);

    /**
     * 通过项目ID获取项目相关金额信息
     * @param projectId
     * @return
     */
    ProjectAmountEntity getOneByProjectId(Long projectId);

    /**
     * 将同一合同的项目各项金额的总和回填到合同信息中
     */
    void saveMoneyByContractId(Long contractId);

    /**
     * 根据项目ID查询是否已经存在于项目金额信息中
     * @param projectId 项目ID
     * @return boolean
     */
    Boolean notExistPlanByProject(Long projectId);

    List<ProjectAmountEntity> selectListByProjectIds(List<Long> ids);
    
}

