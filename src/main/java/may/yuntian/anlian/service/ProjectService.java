package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.dto.ProjectAccountingDto;
import may.yuntian.anlian.vo.*;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ProjectEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目表(包含了原任务表的字段)
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
public interface ProjectService extends IService<ProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 实验室
     * @param params
     * @return
     */
    PageUtils queryPageByChilden(Map<String, Object> params);
    /**
     * 项目归档列表
     * @param params
     * @return
     */
    PageUtils queryMyProjectAchive(Map<String, Object> params);
    /**
     *   项目信息初始化
     * @param project
     * @param saveMoney true金额回填到合同，false不回填
     * @return
     */
    void saveProject(ProjectEntity project,boolean saveMoney);
    /**
     * 根据项目编号查询是否已经存在于项目信息中
     * @param identifier 项目编号
     * @return boolean
     */
    Boolean notExistContractByIdentifier(String identifier);

    /**
     * 通过合同ID查询相关的项目信息
     */
    List<ProjectEntity> selectListByContractId(Long contractId);
    /**
     * 根据合同ID查询是否已经下发或者正在进行中
     * @param contractId 项目编号
     * @return boolean
     */
    Boolean notExistContractByContractId(Long contractId);

    /**
     * 根据条件查询项目ID列表
     */
    List<Long> getProjectIdsByParams(QueryProjectVo queryVo);

    /**
     * 项目信息导出
     * @param params
     * @return
     */
    List<ProjectEntity> exportOut(Map<String, Object> params);

    /**
     * 项目修改连带项目信息日期表和项目金额表一起修改
     * @param projectEntity
     */
    void updateProject(ProjectEntity projectEntity);

    /**
     * 通过项目编号获取项目id
     * @param identifier
     * @return
     */
    Long getIdByIdentifier(String identifier);

    /**
     * 根据查询条件统计金额
     * @param params
     * @return
     */
    MoneyVo sumMoneyByParams(Map<String, Object> params);

    /**
     * 根据查询条件统计金额
     * @param params
     * @return
     */
    MoneyVo sumMoneyByParams2(Map<String,Object> params);

    /**
     * 任务下发
     * @param project
     */
    void taskRelease(ProjectEntity project);



    /**
     * 实验室收记录
     * @param project
     */
    boolean receiveTime(ProjectEntity project);

    /**
     * 报告接收时间填写
     * @param projectAchiveVo
     */
    boolean signIssue(ProjectAchiveVo projectAchiveVo);

    /**
     * 采样提成脚本
     * @param projectIdList
     */
    void commissionGatherByProjectIdList(List<Long> projectIdList);

    /**
     * 签发提成脚本
     * @param ids
     */
    void issueCommissionByProjectIds(List<Long> ids);

    /**
     * 归档提成脚本
     * @param ids
     */
    void fillingCommissionByIds(List<Long> ids);


    /**
     * 获取检评相关项目信息
     * @param params
     * @return
     */
    PageUtils getPage(Map<String,Object> params);

    /**
     * 项目总览导出
     * @param params
     * @return
     */
    List<ProjectCountVo> exportByParams(Map<String,Object> params);


    /**
     * 优化分页测试
     * @param params
     * @return
     */
    List<ProjectEntity> getPageList(Map<String, Object> params);

    /**
     * 我的项目分页列表
     * @param params
     * @return
     */
    List<ProjectEntity> getMyPageList(Map<String, Object> params);



    /**
     * 新检评系统采样提成接口
     * @param commissionTimeNodeVo
     * @return
     */
    //TODO  新检评系统采样提成接口
    boolean mathCommission(CommissionTimeNodeVo commissionTimeNodeVo);


    /**
     * 中止项目
     * @param suspendOrRestartProjectVo
     */
    void suspendProject(SuspendOrRestartProjectVo suspendOrRestartProjectVo);

    /**
     * 重启项目
     * @param suspendOrRestartProjectVo
     * @return
     */
    boolean restartProject(SuspendOrRestartProjectVo suspendOrRestartProjectVo);


    /**
     * 获取项目基本金额信息
     * @param identifier
     * @return
     */
    OAProjectAmountVo getInfomation(String identifier,List<String> compantList);

    /**
     * OA修改项目金额
     * @param oaProjectAmountVo
     * @param project
     */
    void updateProAmount(OAProjectAmountVo oaProjectAmountVo,ProjectEntity project);

    /**
     * 收付款项页面列表
     * @param params
     * @return
     */
    List<ProjectEntity> getPaymentReceiptList(Map<String, Object> params);

    /**
     * 收付款项页面列表
     * @param params
     * @return
     */
    MoneyVo getPaymentReceiptSumMoneyByParams(Map<String, Object> params);

    /**
     * 任务下发页面
     * @param params
     * @return
     */
    PageUtils taskDistribution(Map<String, Object> params);

    /**
     * 项目录入页面
     * @param params
     * @return
     */
    PageUtils projectEntry(Map<String, Object> params);


//    采样提成接口
    public void gatherCommission(ProjectEntity project);

    /**OA-获取项目信息(包含合同下的其他项目信息)
     *
     * @param identifier 项目编号
     * @return 项目信息
     */
    List<ProjectEntity> getContractProjects(String identifier);

    /**
     * OA-修改项目信息
     *
     * @param projectInfo 待修改信息
     */
    void updateProjectInfo(ProjectChangeInfoVo projectInfo);
    /**
     * 项目核算页面列表接口
     * @param projectAccountingDto
     * @return
     */
    public List<ProjectAccountingListVo> getAccountingList(ProjectAccountingDto projectAccountingDto);
    /**
     * 项目核算统计
     * @param projectAccountingDto
     * @return
     */
    public ProjectAccountingVo getCountReturn(ProjectAccountingDto projectAccountingDto);

    /**
     * 项目核算页面导出接口
     * @param projectAccountingDto
     * @return
     */
    public List<ProjectAccountingListVo> exportAccountingList(ProjectAccountingDto projectAccountingDto);
}

