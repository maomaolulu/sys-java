package may.yuntian.wordgenerate.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.wordgenerate.mapper.WordGeneratePathMapper;
import may.yuntian.wordgenerate.entity.WordGeneratePathEntity;
import may.yuntian.wordgenerate.service.WordGeneratePathService;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@Service("wordGeneratePathService")
public class WordGeneratePathServiceImpl extends ServiceImpl<WordGeneratePathMapper, WordGeneratePathEntity> implements WordGeneratePathService {

    @Override
    public WordGeneratePathEntity getByProjectId(Long projectId,Integer type){

        return super.getOne(new QueryWrapper<WordGeneratePathEntity>().eq("project_id",projectId).eq("type",type));
    }

}