package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.mapper.ResultItemMapper;
import may.yuntian.external.province.mapper.SubstanceContrastResultMapper;
import may.yuntian.external.province.service.ResultItemService;
import may.yuntian.external.province.vo.ResultCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 11:12
 */
@Service("resultItemService")
public class ResultItemServiceImpl extends ServiceImpl<ResultItemMapper, ResultItem> implements ResultItemService{

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ResultItemMapper resultItemMapper;
    @Resource
    private SubstanceContrastResultMapper substanceContrastResultMapper;

    /**
     * 结果项调用逻辑：0检评；1评价；2下游
     */
    @Override
    public Map<String, Object> assertResult(Long projectId) {
        Map<String, Object> map = new HashMap<>(8);
        Integer count = resultItemMapper.selectCount(Wrappers.lambdaQuery(ResultItem.class).eq(ResultItem::getProjectId, projectId));
        ProjectEntity projectEntity = projectMapper.selectOne(Wrappers.lambdaQuery(ProjectEntity.class).select(ProjectEntity::getId, ProjectEntity::getIdentifier, ProjectEntity::getProvince, ProjectEntity::getType).eq(ProjectEntity::getId, projectId));
        ParticipantTableServiceImpl.addElementToMap(map, count, projectEntity);
        return map;
    }

    /**
     * 查询结果
     */
    @Override
    public Map<Integer, Map<String, List<ResultItem>>> resultItemList(Long projectId) {
        Map<Integer, Map<String, List<ResultItem>>> map = new HashMap<>(25);
        for (int i = 1; i <= 13; i++) {
            map.put(i, Collections.emptyMap());
        }
        List<ResultItem> resultItemList = baseMapper.selectList(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId));
        if (CollUtil.isNotEmpty(resultItemList)) {
            resultItemList.stream().collect(Collectors.groupingBy(ResultItem::getFactorType)).forEach((type, valueList) -> {
                if (CollUtil.isNotEmpty(valueList)) {
                    Map<String, List<ResultItem>> codeMap = valueList.stream().sorted((Comparator.comparingInt(o -> o.getCode().split(",").length))).collect(Collectors.groupingBy(ResultItem::getCode));
                    map.put(type, codeMap);
                }
            });
        }
        return map;
    }

    /**
     * 批量保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveResult(Map<Integer, Map<String, List<ResultItem>>> resultMap) {
        List<ResultItem> resultItems = new ArrayList<>();
        resultMap.forEach((key, map) -> {
            if (CollUtil.isNotEmpty(map)) {
                map.forEach((codeStr, valueList) -> { resultItems.addAll(valueList); });
            }
        });
        baseMapper.delete(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, resultItems.get(0).getProjectId()));
        resultItems.forEach(resultItem -> {
            resultItem.setCreateTime(DateUtil.dateSecond());
            resultItem.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        });
        this.saveBatch(resultItems);
        return 1;
    }

    /**
     * 结果项：名称-编码
     */
    @Override
    public List<ResultCodeVo> getResultNameCodeList() {
        return substanceContrastResultMapper.getResultCodeList();
    }

}
