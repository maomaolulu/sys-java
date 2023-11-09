package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.ParticipantTable;

import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 10:36
 */
public interface ParticipantTableService extends IService<ParticipantTable> {

    /**
     * 人员列表调用逻辑校验
     * @param projectId
     * @return
     */
    Map<String, Object> assertParticipant(Long projectId);

    /**
     * 保存参与人员信息
     * @param list
     * @return
     */
    int saveParticipantList(List<ParticipantTable> list);

    /**
     * 查询参与人员信息
     * @param projectId 项目id
     * @return list
     */
    List<ParticipantTable> getListByProjectId(Long projectId);
}
