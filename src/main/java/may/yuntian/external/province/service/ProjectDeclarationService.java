package may.yuntian.external.province.service;

import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.vo.PushDataVo;
import may.yuntian.external.province.vo.RejectVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-04 13:50
 */
public interface ProjectDeclarationService {

    /**
     * 推送（单个）项目至省平台系统
     * @param projectId
     * @return
     */
    String push(Long projectId);

    /**
     * 主管or质控：项目列表
     * @param info
     * @return
     */
    List<BasicInfo> getProjectList(BasicInfo info);

    /**
     * 主管or质控：查看所有数据信息
     * @param projectId 项目id
     * @param viewer 用户角色标识值
     * @return dataVo
     */
    PushDataVo viewProjectDataInfo(Long projectId, Integer viewer);

    /**
     * 主管or质控：驳回
     * @param rejectVo
     * @return
     */
    int rejectReason(RejectVo rejectVo);

    /**
     * 主管：提交
     * @param projectId
     * @return
     */
    int chargeSubmit(Long projectId);


}
