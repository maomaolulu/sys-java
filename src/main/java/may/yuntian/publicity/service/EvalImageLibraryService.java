package may.yuntian.publicity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.publicity.entity.EvalImageLibraryEntity;

import java.util.List;
import java.util.Map;

/**
 * 影像库（评价）
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-28 18:32:17
 */
public interface EvalImageLibraryService extends IService<EvalImageLibraryEntity> {

    List<EvalImageLibraryEntity> queryPage(Map<String, Object> params);

    /**
     * 获取评价采样影像
     * @param projectId
     * @return
     */
    public List<EvalImageLibraryEntity> getListByProjectId(Long projectId);
}

