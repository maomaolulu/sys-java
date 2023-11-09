package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.vo.CityCompanySurveyVo;
import may.yuntian.anlian.vo.CompanySurveyVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.CompanySurveyEntity;

import java.util.List;
import java.util.Map;

/**
 * 用人单位概况调查
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
public interface CompanySurveyService extends IService<CompanySurveyEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据项目ID查询是否已经存在于用人单位概况调查信息中
     * @param projectId 任务排单ID
     * @return boolean
     */
    Boolean notExistCompanySurveyByProject(Long projectId);
    
    /**
     * 通过项目id查询用人单位概况调查信息
     * @param projectId
     * @return
     */
    CompanySurveyEntity seleteByProjectId(Long projectId);
    
    
    /**
     * 根据项目id删除用人单位概况调查信息
     * @param projectId
     */
    void deleteByProjectId(Long projectId);
    /**
     *查询市级申报详情xin 评价项目公示-信息公示列表
     */
    CityCompanySurveyVo selectCityCompanySurveyList(Long projectId);
    /**
     * 公示信息
     * @param
     */
    List<CompanySurveyVo> publicityList(CompanySurveyVo companySurveyVo);

    /**
     * xin 评价项目公示-公示记录列表
     * @param params
     * @return
     */
    List<ProjectEntity> listSys(Map<String, Object> params);

    /**
     * xin 评价项目公示-信息公示列表
     * @param company
     * @return
     */
    List<CompanySurveyVo> publicityListPj(CompanySurveyVo company);
}

