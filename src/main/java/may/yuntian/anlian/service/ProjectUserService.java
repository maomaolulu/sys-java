package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectUserEntity;

import java.util.List;


/**
 *采样人员人员实体类
 *
 * @author LiXin
 * @data 2022-02-21
 */
public interface ProjectUserService extends IService<ProjectUserEntity> {

    /**
     * 通过项目Id获取采样人员与组长列表
     * @param projectId
     * @return
     */
    List<ProjectUserEntity> getListByProjectId(Long projectId);

    /**
     * 获取组员及组长列表
     * @param projectId
     * @return
     */
    String getListByTypeAndProjectId(Long projectId);

    /**
     * 根据类型和项目ID获取人员列表
     * @param type
     * @param projectId
     * @return
     */
    List<ProjectUserEntity> getListByType(Integer type, Long projectId);

    List<Long> getPlanIdListByUsername(String username);


    /**
     * 获取评价公示人员信息
     * @return
     */
    public List<ProjectUserEntity> getPjPublictyUser(Long projectId);
}
