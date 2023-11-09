package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.AlCompanySurveyEntity;

public interface AlCompanySurveyService extends IService<AlCompanySurveyEntity> {

    /**
     * 根据项目ID查询是否已经存在于评价企业概况信息中
     * @param projectId 项目ID
     * @return boolean
     */
    Boolean notExistPlanByProject(Long projectId);

    /**
     * 根据项目ID获取一条信息
     * @param projectId
     * @return
     */
    AlCompanySurveyEntity getOneByProjectId(Long projectId);
}
