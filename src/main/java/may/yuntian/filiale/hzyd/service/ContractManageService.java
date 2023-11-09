package may.yuntian.filiale.hzyd.service;

import may.yuntian.filiale.hzyd.vo.ContractVo;
import may.yuntian.filiale.hzyd.entity.BasicRelation;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.vo.DataRecordVo;

import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 12:16
 */
public interface ContractManageService {

    /**
     * 合同管理-列表
     *
     * @param contractVo 查询条件
     * @return 结果
     */
    List<ContractVo> selectContractList(ContractVo contractVo);

    /**
     * 合同-项目-类目：列表
     *
     * @return 结果
     */
    List<BasicRelation> selectBasicRelationList();

    /**
     * 检测信息
     *
     * @param detectInfoCategory 类目（多个以英文逗号分隔）
     * @return 结果
     */
    Map<String, List<DetectInfo>> selectDetectInfo(String detectInfoCategory);

    /**
     * 合同管理-新增
     *
     * @param dataRecordVo 数据记录vo
     * @return rows
     */
    int insertDataRecord(DataRecordVo dataRecordVo);

    /**
     * 合同管理-删除
     *
     * @param contractId 合同id
     * @return rows
     */
    int removeContractById(Long contractId);

    /**
     * 合同管理-编辑
     *
     * @param contractVo 合同vo
     * @return rows
     */
    int editContractById(ContractVo contractVo);

    /**
     * 合同管理-详情
     *
     * @param contractId 合同id
     * @return map
     */
    Map<String, Object> contractDetailById(Long contractId);

    /**
     * 项目or合同：流水号
     *
     * @param code 编号
     * @param flag 标识：1合同，2项目
     * @return num
     */
    String generateCode(String code, Integer flag);

}
