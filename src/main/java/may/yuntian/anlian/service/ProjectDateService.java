package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.vo.ProjectDateVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ProjectDateEntity;

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
public interface ProjectDateService extends IService<ProjectDateEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据条件查询项目ID列表
     */
    List<Long> getProjectIdsByParams(ProjectDateVo queryVo);

    /**
     * 根据项目ID获取项目日期相关信息
     * @param projctId
     * @return
     */
    ProjectDateEntity getOneByProjetId(Long projctId);
    /**
     * 根据项目ID查询是否已经存在于项目日期信息中
     * @param projectId 项目ID
     * @return boolean
     */
    Boolean notExistPlanByProject(Long projectId);

    List<ProjectDateEntity> selectListByProjectIds(List<Long> ids);
    
}

