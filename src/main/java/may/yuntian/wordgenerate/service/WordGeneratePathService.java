package may.yuntian.wordgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.wordgenerate.entity.WordGeneratePathEntity;

import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
public interface WordGeneratePathService extends IService<WordGeneratePathEntity> {
    WordGeneratePathEntity getByProjectId(Long projectId,Integer type);
}

