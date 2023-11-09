package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.AlCompanySurveyEntity;
import may.yuntian.anlian.mapper.AlCompanySurveyMapper;
import may.yuntian.anlian.service.AlCompanySurveyService;
import org.springframework.stereotype.Service;

@Service("alCompanySurveyService")
public class AlCompanySurveyServiceImpl extends ServiceImpl<AlCompanySurveyMapper, AlCompanySurveyEntity> implements AlCompanySurveyService {

    /**
     * 根据项目ID查询是否已经存在于评价企业概况信息中
     * @param projectId 项目ID
     * @return boolean
     */
    public Boolean notExistPlanByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<AlCompanySurveyEntity>().eq("project_id", projectId));
        if(count>0)
            return false;
        else
            return true;
    }

    /**
     * 根据项目ID获取一条信息
     * @param projectId
     * @return
     */
    public AlCompanySurveyEntity getOneByProjectId(Long projectId){
        AlCompanySurveyEntity entity = baseMapper.selectOne(new QueryWrapper<AlCompanySurveyEntity>().eq("project_id",projectId));
        return entity;
    }

}
