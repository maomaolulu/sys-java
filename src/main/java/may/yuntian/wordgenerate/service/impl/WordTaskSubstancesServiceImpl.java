package may.yuntian.wordgenerate.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.wordgenerate.mapper.WordTaskSubstancesMapper;
import may.yuntian.wordgenerate.entity.WordTaskSubstancesEntity;
import may.yuntian.wordgenerate.service.WordTaskSubstancesService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@Service("wordTaskSubstancesService")
public class WordTaskSubstancesServiceImpl extends ServiceImpl<WordTaskSubstancesMapper, WordTaskSubstancesEntity> implements WordTaskSubstancesService {


    @Override
    public List<WordTaskSubstancesEntity> getListByProjectId(Long projectId) {
        List<WordTaskSubstancesEntity> list = baseMapper.selectList(new QueryWrapper<WordTaskSubstancesEntity>().eq("project_id",projectId));
        if (StringUtils.isNotEmpty(list)){
            for (int i = 0; i < list.size();i++){
                list.get(i).setIndex(i+1);
            }
        }else {
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     * 根据项目ID删除信息
     * @param projectId
     */
    @Override
    public void deleteByProjectId(Long projectId){
        baseMapper.delete(new QueryWrapper<WordTaskSubstancesEntity>().eq("project_id",projectId));
    }
}