package may.yuntian.filiale.hzyd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ContractMapper;
import may.yuntian.anlian.mapper.ProjectAmountMapper;
import may.yuntian.anlian.mapper.ProjectDateMapper;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.filiale.hzyd.dto.AmountDto;
import may.yuntian.filiale.hzyd.dto.ProjectDto;
import may.yuntian.filiale.hzyd.entity.DataRecord;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.mapper.DataRecordMapper;
import may.yuntian.filiale.hzyd.mapper.DetectInfoMapper;
import may.yuntian.filiale.hzyd.service.ProjectManageService;
import may.yuntian.filiale.hzyd.vo.DetectInfoVo;
import may.yuntian.filiale.hzyd.vo.OperateVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 12:20
 */
@Service("projectManageService")
public class ProjectManageServiceImpl implements ProjectManageService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectDateMapper projectDateMapper;
    @Resource
    private ProjectAmountMapper projectAmountMapper;
    @Resource
    private DataRecordMapper dataRecordMapper;
    @Resource
    private DetectInfoMapper detectInfoMapper;

    /**
     * 列表
     */
    @Override
    public List<ProjectDto> selectProjectList(ProjectDto projectDto) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        QueryWrapper<ProjectDto> wrapper = new QueryWrapper<>();
        if ("杭州亿达".equals(subjection)){
            wrapper.likeRight("ap.contract_identifier", "YD");
        }else{
            wrapper.likeRight("ap.contract_identifier", "WK");
        }
//        wrapper.likeRight("ap.contract_identifier", "YD");
        wrapper.like(StrUtil.isNotBlank(projectDto.getContractCode()), "ap.contract_identifier", projectDto.getContractCode());
        wrapper.like(StrUtil.isNotBlank(projectDto.getItemCode()), "ap.identifier", projectDto.getItemCode());
        wrapper.eq(StrUtil.isNotBlank(projectDto.getItemType()), "ap.type", projectDto.getItemType());
        wrapper.like(StrUtil.isNotBlank(projectDto.getEmpName()), "ap.company", projectDto.getEmpName());
        wrapper.eq(ObjectUtil.isNotNull(projectDto.getProjectStatus()), "ap.`status`", projectDto.getProjectStatus());
        wrapper.ge(StrUtil.isNotBlank(projectDto.getIssueDate()), "apd.task_release_date", projectDto.getIssueDate());
        wrapper.le(StrUtil.isNotBlank(projectDto.getIssueEndDate()), "apd.task_release_date", projectDto.getIssueEndDate());
        wrapper.ge(StrUtil.isNotBlank(projectDto.getProjectFinishDate()), "apd.project_finish_date", projectDto.getProjectFinishDate());
        wrapper.le(StrUtil.isNotBlank(projectDto.getProjectFinishEndDate()), "apd.project_finish_date", projectDto.getProjectFinishEndDate());
        wrapper.orderByDesc("apd.task_release_date");
        pageUtil2.startPage();
        return dataRecordMapper.selectProjectList(wrapper);
    }

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addProject(ProjectDto projectDto) {
        if (StrUtil.isBlank(projectDto.getContractCode())) {
            throw new RRException("关联合同编号不能为空！");
        }
        ContractEntity contractEntity = contractMapper.selectOne(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getIdentifier, projectDto.getContractCode()));
        if (ObjectUtil.isNull(contractEntity)) {
            throw new RRException("抱歉，查无此合同，请输入正确的编号！");
        }
        // al_project
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setContractId(contractEntity.getId());
        projectEntity.setContractIdentifier(contractEntity.getIdentifier());
        projectEntity.setType(projectDto.getItemType());
        projectEntity.setIdentifier(projectDto.getItemCode());
        projectEntity.setCompany(projectDto.getEmpName());
        projectEntity.setCompanyId(projectDto.getEmpNameId());
        projectEntity.setOfficeAddress(projectDto.getEmpAddress());
        projectEntity.setEntrustCompany(projectDto.getEntrustUnit());
        projectEntity.setEntrustCompanyId(projectDto.getEntrustUnitId());
        projectEntity.setEntrustOfficeAddress(projectDto.getEntrustAddress());
        projectEntity.setStatus(1); // 1项目录入
        projectEntity.setContact(projectDto.getContactPerson());
        projectEntity.setTelephone(projectDto.getContactPhone());
        projectEntity.setCompanyOrder(contractEntity.getCompanyOrder());
        projectEntity.setBusinessSource(contractEntity.getBusinessSource());
        projectEntity.setEvaluationFee(new BigDecimal(projectDto.getReviewCost()));
        projectEntity.setOtherExpenses(new BigDecimal(projectDto.getOtherCost()));
        projectEntity.setServiceCharge(new BigDecimal(projectDto.getServiceCost()));
        projectEntity.setTotalMoney(new BigDecimal(projectDto.getItemAmount()));
        projectEntity.setCommission(new BigDecimal(projectDto.getBusinessCost()));
        projectEntity.setSubprojectFee(new BigDecimal(projectDto.getSubcontractCost()));
        projectEntity.setNetvalue(new BigDecimal(projectDto.getItemAmount())
                .subtract(new BigDecimal(projectDto.getBusinessCost()))
                .subtract(new BigDecimal(projectDto.getServiceCost()))
                .subtract(new BigDecimal(projectDto.getReviewCost()))
                .subtract(new BigDecimal(projectDto.getSubcontractCost()))
                .subtract(new BigDecimal(projectDto.getOtherCost())));
        projectEntity.setUserid(ShiroUtils.getUserId());
        projectEntity.setUsername(ShiroUtils.getUserEntity().getUsername());
        projectEntity.setCreatetime(DateUtil.dateSecond());
        projectEntity.setRemarks(projectDto.getRemark());
        projectService.getBaseMapper().insert(projectEntity);
        // al_project_date
        ProjectDateEntity projectDateEntity = new ProjectDateEntity();
        projectDateEntity.setProjectId(projectEntity.getId());
        projectDateEntity.setEntrustDate(contractEntity.getCommissionDate());
        projectDateEntity.setSignDate(contractEntity.getSignDate());
        projectDateEntity.setClaimEndDate(StringUtils.isNotBlank(projectDto.getRequireFinishDate())?DateUtil.parse(projectDto.getRequireFinishDate(), "yyyy-MM-dd"):null);
        projectDateMapper.insert(projectDateEntity);
        // al_project_amount
        ProjectAmountEntity projectAmountEntity = new ProjectAmountEntity();
        setProjectAmountFieldValue(projectDto, projectAmountEntity);
        projectAmountEntity.setProjectId(projectEntity.getId());
        projectAmountEntity.setContractId(contractEntity.getId());
        projectAmountMapper.insert(projectAmountEntity);
        // t_contract   新增项目时，需计算更新合同相关金额！！
        ContractEntity contract = new ContractEntity();
        contract.setTotalMoney(contractEntity.getTotalMoney().add(new BigDecimal(projectDto.getItemAmount())));
        contract.setCommission(contractEntity.getCommission().add(new BigDecimal(projectDto.getBusinessCost())));
        contract.setServiceCharge(contractEntity.getServiceCharge().add(new BigDecimal(projectDto.getServiceCost())));
        contract.setSubcontractFee(contractEntity.getSubcontractFee().add(new BigDecimal(projectDto.getSubcontractCost())));
        contract.setEvaluationFee(contractEntity.getEvaluationFee().add(new BigDecimal(projectDto.getReviewCost())));
        contract.setOtherExpenses(contractEntity.getOtherExpenses().add(new BigDecimal(projectDto.getOtherCost())));
        contract.setNetvalue(contractEntity.getNetvalue().add(projectEntity.getNetvalue()));
        contract.setUpdatetime(DateUtil.dateSecond());
        contractMapper.update(contract, new LambdaUpdateWrapper<ContractEntity>().eq(ContractEntity::getIdentifier, projectDto.getContractCode()));
        // yd_data_record
        DataRecord dataRecord = new DataRecord();
        dataRecord.setContractId(contractEntity.getId());
        dataRecord.setProjectId(projectEntity.getId());
        dataRecord.setDetectInfoIds(projectDto.getDetectInfoIds());
        dataRecord.setQuantities(projectDto.getQuantities());
        dataRecord.setTotal(projectDto.getTotal());
        dataRecord.setDetectType(projectDto.getDetectType());
        dataRecord.setTestItem(projectDto.getTestItem());
        dataRecord.setItemType(projectDto.getItemType());
        dataRecord.setTestStartDate(projectDto.getTestStartDate());
        dataRecord.setTestEndDate(projectDto.getTestEndDate());
        dataRecord.setTestNumber(projectDto.getTestNumber());
        dataRecord.setCreateTime(DateUtil.dateSecond());
        dataRecord.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        return dataRecordMapper.insert(dataRecord);
    }

    /**
     * 为projectAmountEntity部分属性赋值
     */
    private void setProjectAmountFieldValue(ProjectDto projectDto, ProjectAmountEntity projectAmountEntity) {
        projectAmountEntity.setTotalMoney(new BigDecimal(projectDto.getItemAmount()));
        projectAmountEntity.setNosettlementMoney(new BigDecimal(projectDto.getItemAmount()));
        projectAmountEntity.setCommission(new BigDecimal(projectDto.getBusinessCost()));
        projectAmountEntity.setCommissionOutstanding(new BigDecimal(projectDto.getBusinessCost()));
        BigDecimal commissionRatio = new BigDecimal(0);
        if (projectDto.getItemAmount() != null && new BigDecimal(projectDto.getItemAmount()).compareTo(BigDecimal.ZERO) > 0 && projectDto.getBusinessCost() != null) {
            commissionRatio = new BigDecimal(projectDto.getBusinessCost()).divide(new BigDecimal(projectDto.getItemAmount()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
        }
        projectAmountEntity.setCommissionRatio(commissionRatio);
        projectAmountEntity.setEvaluationFee(new BigDecimal(projectDto.getReviewCost()));
        projectAmountEntity.setEvaluationOutstanding(new BigDecimal(projectDto.getReviewCost()));
        projectAmountEntity.setSubprojectFee(new BigDecimal(projectDto.getSubcontractCost()));
        projectAmountEntity.setSubprojectOutstanding(new BigDecimal(projectDto.getSubcontractCost()));
        projectAmountEntity.setServiceCharge(new BigDecimal(projectDto.getServiceCost()));
        projectAmountEntity.setServiceChargeOutstanding(new BigDecimal(projectDto.getServiceCost()));
        projectAmountEntity.setOtherExpenses(new BigDecimal(projectDto.getOtherCost()));
        projectAmountEntity.setOtherExpensesOutstanding(new BigDecimal(projectDto.getOtherCost()));
        projectAmountEntity.setNetvalue(new BigDecimal(projectDto.getItemAmount())
                .subtract(new BigDecimal(projectDto.getBusinessCost()))
                .subtract(new BigDecimal(projectDto.getServiceCost()))
                .subtract(new BigDecimal(projectDto.getReviewCost()))
                .subtract(new BigDecimal(projectDto.getSubcontractCost()))
                .subtract(new BigDecimal(projectDto.getOtherCost())));
    }

    /**
     * （批量）下发
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchIssue(OperateVo operateVo) {
        List<Long> projectIds = operateVo.getProjectIds();
        if (CollUtil.isEmpty(projectIds)) {
            throw new RRException("（批量）下发的项目id不能为空！");
        }
        ProjectEntity project = new ProjectEntity();
        project.setStatus(2);
        project.setCharge(operateVo.getProjectLeader().getUsername());
        project.setChargeId(operateVo.getProjectLeaderId());
        project.setUpdatetime(DateUtil.dateSecond());
        projectService.getBaseMapper().update(project, new LambdaUpdateWrapper<ProjectEntity>().in(ProjectEntity::getId, projectIds));

        ProjectDateEntity projectDate = new ProjectDateEntity();
        projectDate.setTaskReleaseDate(DateUtils.getNowDate());
        projectDateMapper.update(projectDate, new LambdaUpdateWrapper<ProjectDateEntity>().in(ProjectDateEntity::getProjectId, projectIds));
        return 1;
    }

    /**
     * （批量）确认
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchConfirm(OperateVo operateVo) {
        if (CollUtil.isEmpty(operateVo.getProjectIds())) {
            throw new RRException("（批量）确认的项目id不能为空！");
        }
        ProjectEntity project = new ProjectEntity();
        project.setStatus(70);
        project.setUpdatetime(DateUtil.dateSecond());
        projectService.getBaseMapper().update(project, new LambdaUpdateWrapper<ProjectEntity>().in(ProjectEntity::getId, operateVo.getProjectIds()));

        ProjectDateEntity projectDate = new ProjectDateEntity();
        projectDate.setProjectFinishDate(DateUtil.parse(operateVo.getProjectFinishDate(), "yyyy-MM-dd"));
        projectDateMapper.update(projectDate, new LambdaUpdateWrapper<ProjectDateEntity>().in(ProjectDateEntity::getProjectId, operateVo.getProjectIds()));
        return 1;
    }

    /**
     * 编辑
     */
    @Override
    public ProjectDto itemEdit(Long projectId) {
        return dataRecordMapper.selectEditInfo(projectId);
    }

    /**
     * 保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editAfterSave(ProjectDto projectDto) {
        // 编辑前的合同及项目金额数据
        AmountDto amountDto = dataRecordMapper.selectContractAndProjectAmount(projectDto.getProjectId());
        // al_project：根据project_id更新contract_identifier、identifier、entrust_company、type、company、office_address、remarks、total_money
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectDto.getProjectId());
        projectEntity.setContractIdentifier(projectDto.getContractCode());
        projectEntity.setIdentifier(projectDto.getItemCode());
        projectEntity.setEntrustCompany(projectDto.getEntrustUnit());
        projectEntity.setEntrustCompanyId(projectDto.getEntrustUnitId());
        projectEntity.setEntrustOfficeAddress(projectDto.getEntrustAddress());
        projectEntity.setType(projectDto.getItemType());
        projectEntity.setCompany(projectDto.getEmpName());
        projectEntity.setCompanyId(projectDto.getEmpNameId());
        projectEntity.setOfficeAddress(projectDto.getEmpAddress());
        projectEntity.setTotalMoney(new BigDecimal(projectDto.getItemAmount()));
        projectEntity.setRemarks(projectDto.getRemark());
        projectEntity.setUpdatetime(DateUtil.dateSecond());
        projectService.getBaseMapper().updateById(projectEntity);
        // al_project_date：根据project_id更新claim_end_date
        ProjectDateEntity dateEntity = new ProjectDateEntity();
        dateEntity.setClaimEndDate(DateUtil.parse(projectDto.getRequireFinishDate(), "yyyy-MM-dd"));
        projectDateMapper.update(dateEntity, new LambdaUpdateWrapper<ProjectDateEntity>().eq(ObjectUtil.isNotNull(projectDto.getProjectId()), ProjectDateEntity::getProjectId, projectDto.getProjectId()));
        // al_project_amount：根据project_id更新total_money、commission、service_charge、evaluation_fee、subproject_fee、other_expenses、netvalue
        ProjectAmountEntity amountEntity = new ProjectAmountEntity();
        setProjectAmountFieldValue(projectDto, amountEntity);
        projectAmountMapper.update(amountEntity, new LambdaUpdateWrapper<ProjectAmountEntity>().eq(ObjectUtil.isNotNull(projectDto.getProjectId()), ProjectAmountEntity::getProjectId, projectDto.getProjectId()));
        // t_contract: 需更新合同相关金额     Todo: 合同 - 原项目金额 + 修改后项目金额！
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(amountDto.getContractId());
        contractEntity.setTotalMoney(amountDto.getContractAmount().subtract(amountDto.getItemAmount()).add(new BigDecimal(projectDto.getItemAmount())));
        contractEntity.setCommission(amountDto.getContractBusinessCost().subtract(amountDto.getItemBusinessCost()).add(new BigDecimal(projectDto.getBusinessCost())));
        contractEntity.setServiceCharge(amountDto.getContractServiceCost().subtract(amountDto.getItemServiceCost()).add(new BigDecimal(projectDto.getServiceCost())));
        contractEntity.setSubcontractFee(amountDto.getContractSubcontractCost().subtract(amountDto.getItemSubcontractCost()).add(new BigDecimal(projectDto.getSubcontractCost())));
        contractEntity.setEvaluationFee(amountDto.getContractReviewCost().subtract(amountDto.getItemReviewCost()).add(new BigDecimal(projectDto.getReviewCost())));
        contractEntity.setOtherExpenses(amountDto.getContractOtherCost().subtract(amountDto.getItemOtherCost()).add(new BigDecimal(projectDto.getOtherCost())));
        contractEntity.setNetvalue(amountDto.getContractNetValue().subtract(amountDto.getItemNetValue())
                .add(new BigDecimal(projectDto.getItemAmount()).subtract(new BigDecimal(projectDto.getBusinessCost())).subtract(new BigDecimal(projectDto.getServiceCost())).subtract(new BigDecimal(projectDto.getSubcontractCost())).subtract(new BigDecimal(projectDto.getReviewCost())).subtract(new BigDecimal(projectDto.getOtherCost()))));
        contractEntity.setUpdatetime(DateUtil.dateSecond());
        contractMapper.updateById(contractEntity);
        // yd_data_record：根据id更新item_type、detect_info_ids、quantities、detect_type、test_item、update_by、update_time
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(projectDto.getDataRecordId());
        dataRecord.setItemType(projectDto.getItemType());
        dataRecord.setDetectInfoIds(projectDto.getDetectInfoIds());
        dataRecord.setQuantities(projectDto.getQuantities());
        dataRecord.setTotal(projectDto.getTotal());
        dataRecord.setDetectType(projectDto.getDetectType());
        dataRecord.setTestItem(projectDto.getTestItem());
        dataRecord.setTestStartDate(projectDto.getTestStartDate());
        dataRecord.setTestEndDate(projectDto.getTestEndDate());
        dataRecord.setTestNumber(projectDto.getTestNumber());
        dataRecord.setUpdateTime(DateUtil.dateSecond());
        dataRecord.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        dataRecordMapper.updateById(dataRecord);
        return 1;
    }

    /**
     * 详情
     */
    @Override
    public ProjectDto viewDetails(Long projectId) {
        ProjectDto projectDto = dataRecordMapper.selectEditInfo(projectId);
        List<DetectInfo> detectInfoList = detectInfoMapper.selectList(new LambdaQueryWrapper<DetectInfo>().eq(DetectInfo::getDelFlag, 0));
        Map<String, List<DetectInfoVo>> detectInfoMap = new HashMap<>();
        List<DetectInfoVo> voListFJ = new ArrayList<>();
        List<DetectInfoVo> voListHJ = new ArrayList<>();
        List<DetectInfoVo> voListOT = new ArrayList<>();
        // 检测信息
        if ("个人剂量监测".equals(projectDto.getItemType())){
            DetectInfoVo voFJIndexZero = new DetectInfoVo();
            voFJIndexZero.setLeftKey("检测周期");
            voFJIndexZero.setRightValue(DateUtil.format(projectDto.getTestStartDate(),"yyyy-MM-dd")+"~"+DateUtil.format(projectDto.getTestEndDate(),"yyyy-MM-dd"));
            voListOT.add(voFJIndexZero);

            DetectInfoVo voFJIndexOne = new DetectInfoVo();
            voFJIndexOne.setLeftKey("检测人数");
            voFJIndexOne.setRightValue(String.valueOf(projectDto.getTestNumber()));
            voListOT.add(voFJIndexOne);
        }else {
            ContractManageServiceImpl.getDetectInfoVoList(detectInfoList, voListFJ, voListHJ, voListOT, projectDto.getDetectInfoIds(), projectDto.getQuantities(), projectDto.getDetectType(), projectDto.getTestItem(), projectDto.getItemType());
        }
        detectInfoMap.put("FJ", voListFJ);
        detectInfoMap.put("HJ", voListHJ);
        detectInfoMap.put("OT", voListOT);
        projectDto.setDetectInfoMap(detectInfoMap);
        return projectDto;
    }

    /**
     * 中止
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int abort(Long projectId) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setStatus(99);
        projectEntity.setUpdatetime(DateUtil.dateSecond());
        return projectService.getBaseMapper().updateById(projectEntity);
    }


}
