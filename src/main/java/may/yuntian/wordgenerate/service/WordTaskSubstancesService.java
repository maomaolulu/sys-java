package may.yuntian.wordgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.wordgenerate.entity.WordTaskSubstancesEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
public interface WordTaskSubstancesService extends IService<WordTaskSubstancesEntity> {
    List<WordTaskSubstancesEntity> getListByProjectId(Long projectId);

    /**
     * 根据项目ID删除信息
     * @param projectId
     */
    public void deleteByProjectId(Long projectId);

}

