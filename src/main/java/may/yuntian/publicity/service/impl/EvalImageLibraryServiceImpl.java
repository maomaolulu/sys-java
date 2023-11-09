package may.yuntian.publicity.service.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import cn.hutool.log.Log;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.publicity.entity.EvalImageLibraryEntity;
import may.yuntian.publicity.mapper.EvalImageLibraryMapper;
import may.yuntian.publicity.service.EvalImageLibraryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 影像库（评价）
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-28 18:32:17
 */
@Service("evalImageLibraryService")
public class EvalImageLibraryServiceImpl extends ServiceImpl<EvalImageLibraryMapper, EvalImageLibraryEntity> implements EvalImageLibraryService {

    @Override
    public List<EvalImageLibraryEntity> queryPage(Map<String, Object> params) {
        QueryWrapper<EvalImageLibraryEntity> queryWrapper = new QueryWrapper<>();

        List<EvalImageLibraryEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }

    /**
     * 获取评价采样影像-评价公示
     * @param projectId
     * @return
     */
    @Override
    public List<EvalImageLibraryEntity> getListByProjectId(Long projectId){
        List<EvalImageLibraryEntity> list = new ArrayList<>();
        EvalImageLibraryEntity surveyInfo = baseMapper.selectOne(new QueryWrapper<EvalImageLibraryEntity>()
                .eq("project_id",projectId).eq("category",1).eq("type",0).last("limit 1"));
        if (StringUtils.isNotEmpty(surveyInfo)){
            list.add(surveyInfo);
        }
        List<EvalImageLibraryEntity> sampleList = baseMapper.selectList(new QueryWrapper<EvalImageLibraryEntity>()
                .eq("project_id",projectId).eq("category",2));
        if (StringUtils.isNotEmpty(sampleList)){
            list.addAll(sampleList);
        }
        return list;
    }

}