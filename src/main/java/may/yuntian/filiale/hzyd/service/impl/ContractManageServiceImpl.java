package may.yuntian.filiale.hzyd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ContractMapper;
import may.yuntian.anlian.service.ProjectAmountService;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.filiale.hzyd.dto.ProjectDto;
import may.yuntian.filiale.hzyd.entity.DataRecord;
import may.yuntian.filiale.hzyd.service.DataRecordService;
import may.yuntian.filiale.hzyd.util.NumUtils;
import may.yuntian.filiale.hzyd.vo.*;
import may.yuntian.filiale.hzyd.entity.BasicRelation;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.mapper.BasicRelationMapper;
import may.yuntian.filiale.hzyd.mapper.DataRecordMapper;
import may.yuntian.filiale.hzyd.mapper.DetectInfoMapper;
import may.yuntian.filiale.hzyd.service.ContractManageService;
import may.yuntian.filiale.hzyd.dto.ContractDto;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 12:17
 */
@Service("contractManageService")
public class ContractManageServiceImpl implements ContractManageService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private DataRecordMapper dataRecordMapper;
    @Resource
    private DetectInfoMapper detectInfoMapper;
    @Resource
    private BasicRelationMapper basicRelationMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectAmountService projectAmountService;
    @Resource
    private ProjectDateService projectDateService;
    @Resource
    private DataRecordService dataRecordService;

    /**
     * 自定义map常量
     */
    public static final Map<Integer, String> DETECT_TYPE_MAP = new HashMap<>();
    public static final Map<Integer, String> TEST_ITEM_MAP = new HashMap<>();

    static {
        DETECT_TYPE_MAP.put(1, "年检");
        DETECT_TYPE_MAP.put(2, "验收");
        DETECT_TYPE_MAP.put(3, "复测");
        DETECT_TYPE_MAP.put(4, "本底");

        TEST_ITEM_MAP.put(1, "性能");
        TEST_ITEM_MAP.put(2, "防护");
        TEST_ITEM_MAP.put(3, "性能，防护");
    }


    /**
     * 合同列表
     */
    @Override
    public List<ContractVo> selectContractList(ContractVo contractVo) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        QueryWrapper<ContractVo> wrapper = new QueryWrapper<>();
        if ("杭州亿达".equals(subjection)){
            wrapper.likeRight("identifier", "YD");
        }else{
            wrapper.likeRight("identifier", "WK");
        }
        wrapper.like(StrUtil.isNotBlank(contractVo.getContractType()), "type", contractVo.getContractType());
        wrapper.like(StrUtil.isNotBlank(contractVo.getContractCode()), "identifier", contractVo.getContractCode());
        wrapper.like(StrUtil.isNotBlank(contractVo.getEntrustUnit()), "entrust_company", contractVo.getEntrustUnit());
        wrapper.like(StrUtil.isNotBlank(contractVo.getBelongCompany()), "company_order", contractVo.getBelongCompany());
        wrapper.like(StrUtil.isNotBlank(contractVo.getBusinessSource()), "business_source", contractVo.getBusinessSource());
        wrapper.like(ObjectUtil.isNotNull(contractVo.getSalesmanId()), "salesmenid", contractVo.getSalesmanId());
        wrapper.ge(StrUtil.isNotBlank(contractVo.getEntrustDate()), "commission_date", contractVo.getEntrustDate());
        wrapper.le(StrUtil.isNotBlank(contractVo.getEntrustEndDate()), "commission_date", contractVo.getEntrustEndDate());
        wrapper.ge(StrUtil.isNotBlank(contractVo.getContractDate()), "sign_date", contractVo.getContractDate());
        wrapper.le(StrUtil.isNotBlank(contractVo.getContractEndDate()), "sign_date", contractVo.getContractEndDate());
        wrapper.eq(ObjectUtil.isNotNull(contractVo.getDealStatus()), "deal_status", contractVo.getDealStatus());
        wrapper.eq(ObjectUtil.isNotNull(contractVo.getContractStatus()), "contract_status", contractVo.getContractStatus());
        wrapper.orderByDesc("commission_date");
        pageUtil2.startPage();
        return dataRecordMapper.selectContractList(wrapper);
    }

    /**
     * 合同-项目-类目：列表
     */
    @Override
    public List<BasicRelation> selectBasicRelationList() {
        return basicRelationMapper.selectList(null);
    }

    /**
     * 检测信息
     */
    @Override
    public Map<String, List<DetectInfo>> selectDetectInfo(String detectInfoCategory) {
        if (StrUtil.isBlank(detectInfoCategory)) {
            return Collections.emptyMap();
        }
        List<Integer> categoryList = Stream.of(detectInfoCategory.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<DetectInfo> detectInfos = detectInfoMapper.selectList(new LambdaQueryWrapper<DetectInfo>().in(DetectInfo::getCategory, categoryList).eq(DetectInfo::getDelFlag, 0));
        if (CollUtil.isNotEmpty(detectInfos)) {
            return detectInfos.stream().collect(Collectors.groupingBy(detectInfo -> detectInfo.getCategory() + "_" + detectInfo.getType()));
        }
        // "2_1" ---> 指：类二-放射治疗
        return Collections.emptyMap();
    }

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDataRecord(DataRecordVo dataRecordVo) {
        // t_contract新增（1条）
        ContractDto contractDto = dataRecordVo.getContractDto();
        Integer countA = contractMapper.selectCount(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getIdentifier, contractDto.getContractCode()));
        if (countA > 0) {
            throw new RRException("该合同编号已存在！");
        }
        ContractEntity contract = new ContractEntity();
        contract.setIdentifier(contractDto.getContractCode());
        contract.setCompany("");
        contract.setCompanyId(0L); // 亿达：合同中不关联受检单位，故单位及id为""和0。
        contract.setEntrustCompany(contractDto.getEntrustUnit());
        contract.setEntrustCompanyId(contractDto.getEntrustUnitId());
        contract.setEntrustOfficeAddress(contractDto.getOfficeAddress());
        contract.setEntrustType(contractDto.getEntrustType());
        contract.setType(contractDto.getContractType());
        contract.setContractStatus(contractDto.getContractStatus());
        contract.setDealStatus(contractDto.getDealStatus());
        contract.setCompanyOrder(contractDto.getBelongCompany());
        contract.setBusinessSource(contractDto.getBusinessSource());
        contract.setSalesmen(contractDto.getSalesman());
        contract.setSalesmenid(contractDto.getSalesmanId());
        contract.setTotalMoney(new BigDecimal(contractDto.getContractAmount()));
        contract.setCommission(new BigDecimal(contractDto.getBusinessCost()));
        contract.setServiceCharge(new BigDecimal(contractDto.getServiceCost()));
        contract.setEvaluationFee(new BigDecimal(contractDto.getReviewCost()));
        contract.setSubcontractFee(new BigDecimal(contractDto.getSubcontractCost()));
        contract.setOtherExpenses(new BigDecimal(contractDto.getOtherCost()));
        // Todo: 合同净值 = 合同金额 - 业务费 - 服务费 - 评审费 - 分包费 - 其它支出
        contract.setNetvalue(new BigDecimal(contractDto.getContractAmount())
                .subtract(new BigDecimal(StrUtil.isBlank(contractDto.getBusinessCost()) ? "0" : contractDto.getBusinessCost()))
                .subtract(new BigDecimal(StrUtil.isBlank(contractDto.getServiceCost()) ? "0" : contractDto.getServiceCost()))
                .subtract(new BigDecimal(StrUtil.isBlank(contractDto.getReviewCost()) ? "0" : contractDto.getReviewCost()))
                .subtract(new BigDecimal(StrUtil.isBlank(contractDto.getSubcontractCost()) ? "0" : contractDto.getSubcontractCost()))
                .subtract(new BigDecimal(StrUtil.isBlank(contractDto.getOtherCost()) ? "0" : contractDto.getOtherCost())));
        contract.setCommissionDate(DateUtil.parse(contractDto.getEntrustDate(), "yyyy-MM-dd"));
        contract.setSignDate(DateUtil.parse(contractDto.getContractDate(), "yyyy-MM-dd"));
        if (contractDto.getContractStatus() == 1) {
            contract.setContractStatusTime(DateUtil.dateSecond());
        }
        if (contractDto.getDealStatus() == 1) {
            contract.setDealStatusTime(DateUtil.dateSecond());
        }
        contract.setUserid(ShiroUtils.getUserId());
        contract.setContact(contractDto.getContactPerson());
        contract.setTelephone(contractDto.getContactPhone());
        contract.setUsername(ShiroUtils.getUserEntity().getUsername());
        contract.setCreatetime(DateUtil.dateSecond());
        contract.setPaymentMethod(contractDto.getPaymentMethod());
        contract.setPrepaymentRatio(contractDto.getPrepaymentRatio());
        contractMapper.insert(contract);

        // al_project批量新增（多条）
        List<ProjectDto> projectDtoList = dataRecordVo.getProjectDtoList();
        if (CollUtil.isNotEmpty(projectDtoList)) {
            List<String> itemCodeList = new ArrayList<>();
            List<ProjectEntity> projectEntityList = new ArrayList<>();
            for (ProjectDto projectDto : projectDtoList) {
                ProjectEntity project = new ProjectEntity();
                itemCodeList.add(projectDto.getItemCode());
                project.setIdentifier(projectDto.getItemCode());
                project.setContractId(contract.getId());
                project.setContractIdentifier(contractDto.getContractCode());
                project.setCompany(projectDto.getEmpName());
                project.setProjectName(projectDto.getEmpName()+projectDto.getItemType()+"项目");
                project.setCompanyId(projectDto.getEmpNameId());
                project.setOfficeAddress(projectDto.getEmpAddress());
                project.setStatus(1);
                project.setType(projectDto.getItemType());
//                project.setCompany(contractDto.getEntrustUnit());
//                project.setCompanyId(contractDto.getEntrustUnitId());
//                project.setOfficeAddress(contractDto.getOfficeAddress());
                project.setEntrustCompany(contractDto.getEntrustUnit());
                project.setEntrustCompanyId(contractDto.getEntrustUnitId());
                project.setEntrustOfficeAddress(contractDto.getOfficeAddress());
                project.setContact(projectDto.getContactPerson());
                project.setTelephone(projectDto.getContactPhone());
                project.setEntrustType(contractDto.getEntrustType());
                project.setSalesmen(contractDto.getSalesman());
                project.setSalesmenid(Long.valueOf(contractDto.getSalesmanId()));
                project.setCompanyOrder(contractDto.getBelongCompany());
                project.setBusinessSource(contractDto.getBusinessSource());
                project.setTotalMoney(new BigDecimal(projectDto.getItemAmount()));
                // Todo: 项目净值？
                // Todo: 项目净值 = 合同金额 - 业务费 - 服务费 - 评审费 - 分包费 - 其它支出
                project.setNetvalue(new BigDecimal(projectDto.getItemAmount())
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getBusinessCost()) ? "0" : projectDto.getBusinessCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getServiceCost()) ? "0" : projectDto.getServiceCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getReviewCost()) ? "0" : projectDto.getReviewCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getSubcontractCost()) ? "0" : projectDto.getSubcontractCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getOtherCost()) ? "0" : projectDto.getOtherCost())));
                project.setUserid(ShiroUtils.getUserId());
                project.setUsername(ShiroUtils.getUserEntity().getUsername());
                project.setProtocol(contractDto.getDealStatus());
                project.setRemarks(projectDto.getRemark());
                projectEntityList.add(project);
            }
            // 项目编号唯一性校验
            List<ProjectEntity> selectList = projectService.getBaseMapper().selectList(new LambdaQueryWrapper<ProjectEntity>().select(ProjectEntity::getIdentifier).in(ProjectEntity::getIdentifier, itemCodeList));
            if (CollUtil.isNotEmpty(selectList)) {
                StringBuilder builder = new StringBuilder();
                selectList.forEach(projectEntity -> builder.append(projectEntity.getIdentifier()).append("，"));
                throw new RRException("项目编号已存在：" + builder.substring(0, builder.length() - 1));
            }
            projectService.saveBatch(projectEntityList);

            for (int i = 0; i < projectEntityList.size(); i++) {
                projectDtoList.get(i).setProjectId(projectEntityList.get(i).getId());
            }
        }

        // al_project_amount、al_project_date批量插入数据（仅项目）
        if (CollUtil.isNotEmpty(projectDtoList)) {
            List<ProjectAmountEntity> amountEntityList = new ArrayList<>();
            List<ProjectDateEntity> dateEntityList = new ArrayList<>();
            List<DataRecord> dataRecordList = new ArrayList<>();
            for (ProjectDto projectDto : projectDtoList) {
                ProjectAmountEntity projectAmount = new ProjectAmountEntity();
                projectAmount.setProjectId(projectDto.getProjectId());
                projectAmount.setContractId(contract.getId());
                projectAmount.setTotalMoney(new BigDecimal(StrUtil.isBlank(projectDto.getItemAmount()) ? "0" : projectDto.getItemAmount()));
                projectAmount.setNosettlementMoney(new BigDecimal(StrUtil.isBlank(projectDto.getItemAmount()) ? "0" : projectDto.getItemAmount()));
                projectAmount.setCommission(new BigDecimal(StrUtil.isBlank(projectDto.getBusinessCost()) ? "0" : projectDto.getBusinessCost()));
                projectAmount.setCommissionOutstanding(new BigDecimal(StrUtil.isBlank(projectDto.getBusinessCost()) ? "0" : projectDto.getBusinessCost()));
                BigDecimal commissionRatio = new BigDecimal(0);
                if (projectDto.getItemAmount() != null && new BigDecimal(projectDto.getItemAmount()).compareTo(BigDecimal.ZERO) > 0 && projectDto.getBusinessCost() != null) {
                    commissionRatio = new BigDecimal(projectDto.getBusinessCost()).divide(new BigDecimal(projectDto.getItemAmount()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
                }
                projectAmount.setCommissionRatio(commissionRatio);
                projectAmount.setServiceCharge(new BigDecimal(StrUtil.isBlank(projectDto.getServiceCost()) ? "0" : projectDto.getServiceCost()));
                projectAmount.setServiceChargeOutstanding(new BigDecimal(StrUtil.isBlank(projectDto.getServiceCost()) ? "0" : projectDto.getServiceCost()));
                projectAmount.setEvaluationFee(new BigDecimal(StrUtil.isBlank(projectDto.getReviewCost()) ? "0" : projectDto.getReviewCost()));
                projectAmount.setEvaluationOutstanding(new BigDecimal(StrUtil.isBlank(projectDto.getReviewCost()) ? "0" : projectDto.getReviewCost()));
                projectAmount.setSubprojectFee(new BigDecimal(StrUtil.isBlank(projectDto.getSubcontractCost()) ? "0" : projectDto.getSubcontractCost()));
                projectAmount.setSubprojectOutstanding(new BigDecimal(StrUtil.isBlank(projectDto.getSubcontractCost()) ? "0" : projectDto.getSubcontractCost()));
                projectAmount.setOtherExpenses(new BigDecimal(StrUtil.isBlank(projectDto.getOtherCost()) ? "0" : projectDto.getOtherCost()));
                projectAmount.setOtherExpensesOutstanding(new BigDecimal(StrUtil.isBlank(projectDto.getOtherCost()) ? "0" : projectDto.getOtherCost()));
                projectAmount.setNetvalue(new BigDecimal(StrUtil.isBlank(projectDto.getItemAmount()) ? "0" : projectDto.getItemAmount())
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getBusinessCost()) ? "0" : projectDto.getBusinessCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getServiceCost()) ? "0" : projectDto.getServiceCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getReviewCost()) ? "0" : projectDto.getReviewCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getSubcontractCost()) ? "0" : projectDto.getSubcontractCost()))
                        .subtract(new BigDecimal(StrUtil.isBlank(projectDto.getOtherCost()) ? "0" : projectDto.getOtherCost())));
                amountEntityList.add(projectAmount);

                ProjectDateEntity projectDate = new ProjectDateEntity();
                projectDate.setProjectId(projectDto.getProjectId());
                projectDate.setEntrustDate(DateUtil.parse(contractDto.getEntrustDate(), "yyyy-MM-dd"));
                projectDate.setSignDate(DateUtil.parse(contractDto.getContractDate(), "yyyy-MM-dd"));
                projectDate.setClaimEndDate(StringUtils.isNotBlank(projectDto.getRequireFinishDate())?DateUtil.parse(projectDto.getRequireFinishDate(), "yyyy-MM-dd"):null);
                dateEntityList.add(projectDate);

                DataRecord dataRecord = new DataRecord();
                dataRecord.setContractId(contract.getId());
                dataRecord.setProjectId(projectDto.getProjectId());
                dataRecord.setItemType(projectDto.getItemType());
                dataRecord.setDetectInfoIds(projectDto.getDetectInfoIds());
                dataRecord.setQuantities(projectDto.getQuantities());
                dataRecord.setTotal(projectDto.getTotal());
                dataRecord.setDetectType(projectDto.getDetectType());
                dataRecord.setTestItem(projectDto.getTestItem());
                dataRecord.setTestStartDate(projectDto.getTestStartDate());
                dataRecord.setTestEndDate(projectDto.getTestEndDate());
                dataRecord.setTestNumber(projectDto.getTestNumber());
                dataRecord.setCreateTime(DateUtil.dateSecond());
                dataRecord.setCreateBy(ShiroUtils.getUserEntity().getUsername());
                dataRecordList.add(dataRecord);
            }
            projectAmountService.saveBatch(amountEntityList);
            projectDateService.saveBatch(dateEntityList);
            // yd_data_record批量新增（多条）
            dataRecordService.saveBatch(dataRecordList);
        }

        return 1;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeContractById(Long contractId) {
        Integer count = projectService.getBaseMapper().selectCount(new LambdaQueryWrapper<ProjectEntity>()
                .eq(ObjectUtil.isNotNull(contractId), ProjectEntity::getContractId, contractId)
                .notIn(ProjectEntity::getStatus, 1, 99));
        if (count > 0) {
            throw new RRException("该合同关联的项目已下发，不可删除！");
        }
        return dataRecordMapper.deleteTablesByContractId(contractId);
    }

    /**
     * 编辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editContractById(ContractVo contractVo) {
        ContractEntity contract = new ContractEntity();
        contract.setId(contractVo.getContractId());
        contract.setCommissionDate(DateUtil.parse(contractVo.getEntrustDate(), "yyyy-MM-dd"));
        contract.setSignDate(DateUtil.parse(contractVo.getContractDate(), "yyyy-MM-dd"));
        contract.setEntrustCompany(contractVo.getEntrustUnit());
        contract.setEntrustCompanyId(contractVo.getEntrustUnitId());
        contract.setEntrustOfficeAddress(contractVo.getOfficeAddress());
        contract.setCompanyOrder(contractVo.getBelongCompany());
        contract.setEntrustType(contractVo.getEntrustType());
        contract.setBusinessSource(contractVo.getBusinessSource());
        contract.setSalesmen(contractVo.getSalesman());
        contract.setSalesmenid(contractVo.getSalesmanId());
        contract.setContractStatus(contractVo.getContractStatusAfter());
        contract.setDealStatus(contractVo.getDealStatusAfter());
        contract.setUpdatetime(DateUtil.dateSecond());
        if(!contractVo.getDealStatus().equals(contractVo.getDealStatusAfter())) {
            if (contractVo.getDealStatus() == 0) {
                contract.setDealStatusTime(DateUtil.dateSecond());
            } else {
                contractMapper.updateDealStatusTimeById(contractVo.getContractId());
            }
        }
        if(!contractVo.getContractStatus().equals(contractVo.getContractStatusAfter())) {
            if (contractVo.getContractStatus() == 0) {
                contract.setContractStatusTime(DateUtil.dateSecond());
            } else {
                contractMapper.updateContractStatusTimeById(contractVo.getContractId());
            }
        }

        return contractMapper.updateById(contract);
    }


    /**
     * 详情
     */
    @Override
    public Map<String, Object> contractDetailById(Long contractId) {
        Map<String, Object> map = Maps.newHashMap();
        // 合同信息
        ContractDto contractDto = dataRecordMapper.selectContractInfoById(contractId);
        if (ObjectUtil.isNull(contractDto)) {
            throw new RRException("抱歉，查无此合同，请联系管理员！");
        }
        map.put("contractDto", contractDto);
        // 财务信息
        List<FinanceInfoVo> financeInfoVoList = new ArrayList<>();
        FinanceInfoVo contractInfo = new FinanceInfoVo();
        contractInfo.setName("合同");
        contractInfo.setCode(contractDto.getContractCode());
        contractInfo.setAmount(contractDto.getContractAmount());
        contractInfo.setNetValue(contractDto.getNetValue());
        contractInfo.setBusinessCost(contractDto.getBusinessCost());
        contractInfo.setServiceCost(contractDto.getServiceCost());
        contractInfo.setReviewCost(contractDto.getReviewCost());
        contractInfo.setSubcontractCost(contractDto.getSubcontractCost());
        contractInfo.setOtherCost(contractDto.getOtherCost());
        financeInfoVoList.add(contractInfo);
        List<FinanceInfoVo> projectInfoList = dataRecordMapper.selectProjectAmountByContractId(contractId);
        if (CollUtil.isNotEmpty(projectInfoList)) {
            for (int i = 0; i < projectInfoList.size(); i++) {
                projectInfoList.get(i).setName("项目" + NumUtils.arabicNumToChineseNum(i + 1));
            }
            financeInfoVoList.addAll(projectInfoList);
        }
        map.put("financeInfo", financeInfoVoList);
        // 项目：基本信息、检测信息
        List<ProjectDto> basicInfoList = dataRecordMapper.selectProjectBasicInfoByContractId(contractId);
        List<DataRecord> dataRecordList = dataRecordMapper.selectList(new LambdaQueryWrapper<DataRecord>().eq(DataRecord::getContractId, contractId).orderByAsc(DataRecord::getProjectId));
        List<DetectInfo> detectInfoList = detectInfoMapper.selectList(new LambdaQueryWrapper<DetectInfo>().eq(DetectInfo::getDelFlag, 0));
        List<ProjectInfoVo> projectInfoVos = new ArrayList<>();
        if (CollUtil.isNotEmpty(basicInfoList)) {
            for (int i = 0; i < basicInfoList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, List<DetectInfoVo>> detectInfoMap = new HashMap<>();
                List<DetectInfoVo> voListFJ = new ArrayList<>();
                List<DetectInfoVo> voListHJ = new ArrayList<>();
                List<DetectInfoVo> voListOT = new ArrayList<>();
                ProjectInfoVo projectInfoVo = new ProjectInfoVo();
                BeanUtils.copyProperties(basicInfoList.get(i), projectInfoVo);

                DataRecord dataRecord = dataRecordList.get(i);
                if ("个人剂量监测".equals(dataRecord.getItemType())){

                    DetectInfoVo voFJIndexZero = new DetectInfoVo();
                    voFJIndexZero.setLeftKey("检测周期");
                    voFJIndexZero.setRightValue(DateUtil.format(dataRecord.getTestStartDate(),"yyyy-MM-dd")+"~"+DateUtil.format(dataRecord.getTestEndDate(),"yyyy-MM-dd"));
                    voListOT.add(voFJIndexZero);

                    DetectInfoVo voFJIndexOne = new DetectInfoVo();
                    voFJIndexOne.setLeftKey("检测人数");
                    voFJIndexOne.setRightValue(String.valueOf(dataRecord.getTestNumber()));
                    voListOT.add(voFJIndexOne);
                }else {
                    getDetectInfoVoList(detectInfoList, voListFJ, voListHJ, voListOT, dataRecord.getDetectInfoIds(), dataRecord.getQuantities(), dataRecord.getDetectType(), dataRecord.getTestItem(), dataRecord.getItemType());
                }
                detectInfoMap.put("FJ", voListFJ);
                detectInfoMap.put("HJ", voListHJ);
                detectInfoMap.put("OT", voListOT);
                projectInfoVo.setDetectInfoMap(detectInfoMap);
                projectInfoVos.add(projectInfoVo);
            }
        }
        map.put("projectInfo", projectInfoVos);
        return map;
    }

    /**
     * 合同or项目编号：当日流水号
     */
    @Override
    public String generateCode(String code, Integer flag) {
        if (flag == 1) {
            List<ContractEntity> contractEntities = contractMapper.selectList(new LambdaQueryWrapper<ContractEntity>().select(ContractEntity::getIdentifier).likeRight(ContractEntity::getIdentifier, code).orderByDesc(ContractEntity::getIdentifier));
            if (CollUtil.isEmpty(contractEntities)) {
                return "001";
            }
            String identifier = contractEntities.get(0).getIdentifier();
//            String codeStr = String.format("%3s", Integer.parseInt(identifier.substring(6)) + 1);

            return String.format("%3s", Integer.parseInt(identifier.substring(6)) + 1);
        }
        List<ProjectEntity> projectEntities = projectService.getBaseMapper().selectList(new LambdaQueryWrapper<ProjectEntity>().select(ProjectEntity::getIdentifier).likeRight(ProjectEntity::getIdentifier, code).orderByDesc(ProjectEntity::getIdentifier));
        if (CollUtil.isEmpty(projectEntities)) {
            return "001";
        }
        String itemCode = projectEntities.get(0).getIdentifier();
//        Integer.parseInt(itemCode.substring(4)) + 1
        return String.format("%3s", Integer.parseInt(itemCode.substring(4)) + 1);
    }

    /**
     * 配置检测信息
     *
     * @param detectInfoList 检测信息list
     * @param voListFJ 放射卫生检测
     * @param voListHJ 环境检测
     * @param voListOT 其它
     * @param detectInfoIds 检测信息表ids
     * @param quantities （设备or房间）数量
     * @param detectType 检测类别
     * @param testItem 检测项目
     * @param itemType 项目类型
     */
    public static void getDetectInfoVoList(List<DetectInfo> detectInfoList, List<DetectInfoVo> voListFJ, List<DetectInfoVo> voListHJ, List<DetectInfoVo> voListOT, String detectInfoIds, String quantities, Integer detectType, Integer testItem, String itemType) {
        Map<Long, List<DetectInfo>> infoMap = detectInfoList.stream().collect(Collectors.groupingBy(DetectInfo::getId));
        int[] detectIds = Arrays.stream(detectInfoIds.split(",")).mapToInt(Integer::parseInt).toArray();
        String[] quality = quantities.split(",");
        if ("放射卫生检测".equals(itemType)) {
            DetectInfoVo voFJIndexZero = new DetectInfoVo();
            voFJIndexZero.setLeftKey("检测类别");
            voFJIndexZero.setRightValue(DETECT_TYPE_MAP.get(detectType));
            voListFJ.add(voFJIndexZero);

            DetectInfoVo voFJIndexOne = new DetectInfoVo();
            voFJIndexOne.setLeftKey("检测项目");
            voFJIndexOne.setRightValue(TEST_ITEM_MAP.get(testItem));
            voListFJ.add(voFJIndexOne);
        }
        if ("环境检测".equals(itemType)) {
            DetectInfoVo voHJIndexZero = new DetectInfoVo();
            voHJIndexZero.setLeftKey("检测类别");
            voHJIndexZero.setRightValue(DETECT_TYPE_MAP.get(detectType));
            voListHJ.add(voHJIndexZero);
        }
        for (int i = 0; i < detectIds.length; i++) {
            DetectInfoVo detectInfoVo = new DetectInfoVo();
            DetectInfo detectInfo = infoMap.get((long) detectIds[i]).get(0);
            if ("放射卫生检测".equals(itemType)) {
                detectInfoVo.setLeftKey(detectInfo.getName());
                detectInfoVo.setRightValue(quality[i] + (detectInfo.getCategory() == 3 ? "间" : "台"));
                voListFJ.add(detectInfoVo);
            } else if ("环境检测".equals(itemType)) {
                detectInfoVo.setLeftKey("检测项目");
                detectInfoVo.setRightValue(detectInfo.getName());
                voListHJ.add(detectInfoVo);
            } else {
                detectInfoVo.setLeftKey(detectInfo.getName());
                detectInfoVo.setRightValue(quality[i] + "台");
                voListOT.add(detectInfoVo);
            }
        }
    }


}
