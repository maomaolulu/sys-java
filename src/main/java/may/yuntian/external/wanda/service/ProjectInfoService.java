package may.yuntian.external.wanda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.wanda.entity.AffixRecord;
import may.yuntian.external.wanda.entity.FactorDictionary;
import may.yuntian.external.wanda.entity.ProjectInfo;
import may.yuntian.external.wanda.vo.AnnexVo;
import may.yuntian.external.wanda.vo.BusinessSystemDataVo;
import may.yuntian.external.wanda.vo.RejectVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-03-06 11:18
 */
public interface ProjectInfoService extends IService<ProjectInfo> {

    /**
     * 判断：该项目数据在wanda表中是否已保存：0否（调python）；1是（调Java）
     * @param projectId
     * @return
     */
    int getInfoByProjectId(Long projectId);

    /**
     * 判断：该项目检测结果在wanda表中是否已保存
     * @param projectId 项目id
     * @param factorType 危害因素类型
     * @return
     */
    int assertResultSave(Long projectId, Integer factorType);

    /**
     * 判断：附件是否保存
     * @param projectId 项目id
     * @return 0调python；1调Java
     */
    int assertAnnex(Long projectId);

    /**
     * 根据projectId获取项目基本信息
     * @param projectId
     * @return
     */
    ProjectInfo getBasicInfo(Long projectId);

    /**
     * 保存项目基本信息
     * @param info
     * @return
     */
    int saveInfo(ProjectInfo info);

    /**
     * 检测结果：数据显示
     * @param projectId
     * @param factorType
     * @return
     */
    List<?> getDetectionResultData(Long projectId, Integer factorType);

    /**
     * 检测结果保存
     * @param dataVo
     * @return
     */
    int saveDetectionResultData(BusinessSystemDataVo dataVo);

    /**
     * 项目负责人提交给主管审核
     * @param projectId
     * @return
     */
    StringBuilder submit(Long projectId);

    /**
     * 主管驳回
     * @param rejectVo
     * @return
     */
    int chargeReject(RejectVo rejectVo);

    /**
     * 主管提交
     * @param projectId
     * @return
     */
    int chargeRefer(Long projectId);

    /**
     * 质控驳回
     * @param rejectVo
     * @return
     */
    int qualityReject(RejectVo rejectVo);

    /**
     * 质控推送
     * @param projectId
     * @return
     */
    String qualityPush(HttpServletRequest request, Long projectId);

    /**
     * 主管、质控：查看所有数据信息
     * @param projectId
     * @param viewer
     * @return
     */
    BusinessSystemDataVo selectAllDataInfo(Long projectId, Integer viewer);

    /**
     * 主管 + 质控：项目列表
     * @param info
     * @return
     */
    List<ProjectInfo> selectWarehouseList(ProjectInfo info);

    /**
     * 危害因素列表
     * @param factorDictionary
     * @return
     */
    List<FactorDictionary> factorList(FactorDictionary factorDictionary);

    /**
     * 信息保存记录
     * @param projectId
     * @return
     */
    Map<Object, Object> selectInfoRecordList(Long projectId);

    /**
     * 附件-保存
     * @param annexVo
     * @return
     */
    Boolean saveAnnex(AnnexVo annexVo);

    /**
     * 附件-数据回显
     * @param projectId
     * @return
     */
    List<AffixRecord> dataEcho(Long projectId);

    /**
     * 质控-推送基本信息
     * @param projectId
     * @return
     */
    String pushBasicInfo(Long projectId);
}
