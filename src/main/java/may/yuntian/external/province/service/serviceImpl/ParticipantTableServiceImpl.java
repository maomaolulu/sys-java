package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.province.constant.Constants;
import may.yuntian.external.province.entity.ParticipantTable;
import may.yuntian.external.province.mapper.ParticipantTableMapper;
import may.yuntian.external.province.service.ParticipantTableService;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 10:36
 */
@Service("participantTableService")
public class ParticipantTableServiceImpl extends ServiceImpl<ParticipantTableMapper, ParticipantTable> implements ParticipantTableService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ParticipantTableMapper participantTableMapper;

    /**
     * 人员信息调用逻辑：0检评；1评价；2下游
     */
    @Override
    public Map<String, Object> assertParticipant(Long projectId) {
        Map<String, Object> map = new HashMap<>(8);
        Integer count = participantTableMapper.selectCount(Wrappers.lambdaQuery(ParticipantTable.class).eq(ParticipantTable::getProjectId, projectId));
        ProjectEntity projectEntity = projectMapper.selectOne(Wrappers.lambdaQuery(ProjectEntity.class).select(ProjectEntity::getId, ProjectEntity::getIdentifier, ProjectEntity::getProvince, ProjectEntity::getType).eq(ProjectEntity::getId, projectId));
        addElementToMap(map, count, projectEntity);
        return map;
    }

    /**
     * 调用逻辑校验-往map集合里添加元素
     * @param map map
     * @param count 统计数据量
     * @param projectEntity entity
     */
    public static void addElementToMap(Map<String, Object> map, Integer count, ProjectEntity projectEntity) {
        if (ObjectUtil.isNull(projectEntity)) {
            throw new RRException("该项目不存在！");
        }
        // 项目类型
        String type = projectEntity.getType();
        if (StrUtil.isBlank(type)) {
            throw new RRException("该项目类型为空，请联系管理员！");
        }
        if (count == 0) {
            // 调上游数据接口：0检评，1评价                      || Constants.SUPERVISOR_DETECTION.equals(type)
            if (Constants.INSPECTION_EVALUATION.equals(type)) {
                map.put("flag", 0);
            } else if (Constants.CONTROL_EVALUATION.equals(type) || Constants.CURRENT_EVALUATION.equals(type)) {
                map.put("flag", 1);
            } else {
                throw new RRException("该项目类型暂不在申报范围内！");
            }
        } else {
            // 调下游数据接口：2
            map.put("flag", 2);
        }
    }


    /**
     * 保存参与人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveParticipantList(List<ParticipantTable> list) {
        if (CollUtil.isEmpty(list)) {
            throw new RRException("参与人员不能为空！");
        }
        this.remove(Wrappers.lambdaQuery(ParticipantTable.class).eq(ParticipantTable::getProjectId, list.get(0).getProjectId()));
        for (ParticipantTable participantTable : list) {
            participantTable.setCreateTime(DateUtil.dateSecond());
            participantTable.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        }
        this.saveBatch(list);
        return 1;
    }

    /**
     * 查询参与人员
     */
    @Override
    public List<ParticipantTable> getListByProjectId(Long projectId) {
        return participantTableMapper.selectList(Wrappers.lambdaQuery(ParticipantTable.class).select(ParticipantTable::getId, ParticipantTable::getProjectId, ParticipantTable::getName, ParticipantTable::getUserId, ParticipantTable::getItemCode, ParticipantTable::getItemName).eq(ParticipantTable::getProjectId, projectId));
    }

}
