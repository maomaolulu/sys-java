package may.yuntian.anlian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import may.yuntian.anlian.dto.CompanyEntityDTO;
import may.yuntian.anlian.dto.ContractEntityDTO;
import may.yuntian.anlian.dto.ProjectEntityDTO;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.CategoryMapper;
import may.yuntian.anlian.service.CompanyContactService;
import may.yuntian.anlian.service.CompanyService;
import may.yuntian.anlian.service.ContractService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.utils.BeanUtils;
import may.yuntian.modules.sys_v2.utils.DateUtils;
import may.yuntian.modules.sys_v2.utils.SpringUtils;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import may.yuntian.modules.sys_v2.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel数据导入导出
 *
 * @author hjy
 * @date 2023/6/12 17:20
 */
@RestController
@RequestMapping("/sys/data")
public class DataHandleController extends BaseController {

    private final ProjectService projectService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CompanyContactService companyContactService;
    @Autowired
    private ContractService contractService;

    public DataHandleController(ProjectService projectService) {
        this.projectService = projectService;
    }


    /**
     * 模板导出-合同信息
     */
    @PostMapping("/contract/template")
    public void importContractTemplate(HttpServletResponse response) {
        ExcelUtil<ContractEntityDTO> util = new ExcelUtil<>(ContractEntityDTO.class);
        util.importTemplateExcel(response, "合同信息导入模板");
    }

    /**
     * 模板导出-项目信息
     */
    @PostMapping("/project/template")
    public void importProjectTemplate(HttpServletResponse response) {
        ExcelUtil<ProjectEntityDTO> util = new ExcelUtil<>(ProjectEntityDTO.class);
        util.importTemplateExcel(response, "项目信息导入模板");
    }

    /**
     * 模板导出-企业信息
     */
    @PostMapping("/company/template")
    public void importApparatusTemplate(HttpServletResponse response) {
        ExcelUtil<CompanyEntityDTO> util = new ExcelUtil<>(CompanyEntityDTO.class);
        util.importTemplateExcel(response, "企业信息导入模板");
    }

    /**
     * 合同信息数据导入
     */
    @SneakyThrows
    @SysLog("合同信息数据导入")
    @PostMapping("/contract/import")
    public AjaxResult importContractData(MultipartFile file) {
        //提取excel数据
        ExcelUtil<ContractEntityDTO> util = new ExcelUtil<>(ContractEntityDTO.class);
        List<ContractEntityDTO> contractList = util.importExcel(file.getInputStream());
        //返回信息
        StringBuilder resultMsg = new StringBuilder();
        //失败信息
        StringBuilder fileMsg = new StringBuilder();
        //公司缓存
        Map<String, CompanyEntity> dataMap = new HashMap<>();
        //业务员缓存
        Map<String, Integer> salesmenMap = new HashMap<>();
        //存放失败次数
        Map<String, Integer> failNumMap = new HashMap<>();
        int addNum = 0;
        int failNum = 0;
        failNumMap.put("failNum", failNum);
        //数据处理--为了不影响业务  继续延用原先的新增方式外套循环插入；  下同
        if (StringUtils.isNotEmpty(contractList)) {
            //整理项目类型及归属合同数据
            List<Map<String, String>> projectTypes = SpringUtils.getBean(CategoryMapper.class).getProjectType();
            Map<String, String> typeMap = new HashMap<>();
            for (Map<String, String> projectType : projectTypes) {
                //填充数据
                typeMap.put(projectType.get("name"), projectType.get("module"));
            }
            //循环处理数据
            for (ContractEntityDTO contractTemp : contractList) {
                //校验合同信息数据是否合理
                boolean flag = checkContractInfo(contractTemp, failNumMap, fileMsg, dataMap);
                failNum = failNumMap.get("failNum");
                if (!flag) {
                    continue;
                }
                //插入合同及项目信息
                try {
                    //补充合同类型
                    contractTemp.setType(typeMap.get(contractTemp.getProjectType()));
                    insertContractData(contractTemp, salesmenMap);
                    addNum++;
                } catch (Exception e) {
                    //失败
                    failNum++;
                    fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：异常提示信息；", failNum, contractTemp.getIdentifier(), e.getMessage()));
                }
            }
        } else {
            failNum++;
            fileMsg.append("<BR />数据异常-").append(failNum).append(" ，原因：excel表格数据异常；");
        }
        //整理返回提示信息
        if (addNum > 0) {
            resultMsg.append("成功新增合同条数：").append(addNum).append("条。");
        }
        if (failNum > 0) {
            resultMsg.append("合同信息导入失败，共").append(failNum).append("条，错误如下：");
            resultMsg.append(fileMsg);
        }
        return success(resultMsg.toString());
    }

    /**
     * 插入合同数据
     *
     * @param contractTemp 合同及项目数据
     * @param salesmenMap  业务员缓存
     */
    private void insertContractData(ContractEntityDTO contractTemp, Map<String, Integer> salesmenMap) {
        //判断是否存在业务员数据
        String salesmenName = contractTemp.getSalesmen();
        if (StringUtils.isNotEmpty(salesmenName)) {
            Integer salesmenId = salesmenMap.get(salesmenName);
            if (StringUtils.isNull(salesmenId)) {
                SysUserEntity salesmen = sysUserService.queryByUserName(salesmenName);
                if (StringUtils.isNotNull(salesmen)) {
                    int tempSalesmenId = salesmen.getUserId().intValue();
                    contractTemp.setSalesmenid(tempSalesmenId);
                    salesmenMap.put(salesmenName, tempSalesmenId);
                }
            } else {
                contractTemp.setSalesmenid(salesmenId);
            }
        }
        ContractEntity contract = new ContractEntity();
        //计算项目净值
        BigDecimal totalMoney = StringUtils.nvl(contractTemp.getTotalMoney(), BigDecimal.ZERO);
        //佣金(元)
        BigDecimal commission = StringUtils.nvl(contractTemp.getCommission(), BigDecimal.ZERO);
        //评审费(元)
        BigDecimal evaluationFee = StringUtils.nvl(contractTemp.getEvaluationFee(), BigDecimal.ZERO);
        //分包费(元)
        BigDecimal subcontractFee = StringUtils.nvl(contractTemp.getSubcontractFee(), BigDecimal.ZERO);
        //服务费用(元)
        BigDecimal serviceCharge = StringUtils.nvl(contractTemp.getServiceCharge(), BigDecimal.ZERO);
        //其他支出(元)
        BigDecimal otherExpenses = StringUtils.nvl(contractTemp.getOtherExpenses(), BigDecimal.ZERO);
        //合同净值
        BigDecimal netvalue = totalMoney.subtract(commission).subtract(evaluationFee).subtract(subcontractFee).subtract(serviceCharge).subtract(otherExpenses);
        contractTemp.setNetvalue(netvalue);
        //项目名称-默认值
        if (StringUtils.isEmpty(contractTemp.getProjectName())) {
            contractTemp.setProjectName(contractTemp.getCompany() + contractTemp.getProjectType());
        }
        BeanUtils.copyBeanProp(contract, contractTemp);
        contract.setUserid(getUserId());
        contract.setUsername(getUsername());
        //补充时间(两个)
        Integer contractStatus = contract.getContractStatus();
        if (StringUtils.isNotNull(contractStatus) && contractStatus == 1) {
            contract.setContractStatusTime(DateUtils.getNowDate());
        }
        Integer dealStatus = contract.getDealStatus();
        if (StringUtils.isNotNull(dealStatus) && dealStatus == 1) {
            contract.setDealStatusTime(DateUtils.getNowDate());
        }
        contractService.save(contract);
        ProjectEntity projectEntity = ObjectConversion.copy(contract, ProjectEntity.class);
        projectEntity.setId(null);
        projectEntity.setSubprojectFee(contract.getSubcontractFee());
        projectEntity.setIdentifier(contract.getProjectIdentifier());
        projectEntity.setStatus(contract.getProjectStatus());
        projectEntity.setDeptId(contract.getProjectDeptId());
        projectEntity.setType(contract.getProjectType());
        projectEntity.setContractId(contract.getId());
        projectEntity.setContractIdentifier(contract.getIdentifier());
        projectEntity.setEntrustDate(contract.getCommissionDate());
        projectEntity.setSignDate(contract.getSignDate());
        //项目信息初始化 true金额回填到合同，false不回填
        projectService.saveProject(projectEntity, true);
    }

    /**
     * 项目信息数据导入
     */
    @SysLog("项目信息数据导入")
    @PostMapping("/project/import")
    public AjaxResult importData(MultipartFile file, Long contractId) throws Exception {
        //校验1-合同id
        if (StringUtils.isNull(contractId)) {
            return error("合同id不能为空");
        }
        //提取excel数据
        ExcelUtil<ProjectEntityDTO> util = new ExcelUtil<>(ProjectEntityDTO.class);
        List<ProjectEntityDTO> projects = util.importExcel(file.getInputStream());
        //返回信息
        StringBuilder resultMsg = new StringBuilder();
        //失败信息
        StringBuilder fileMsg = new StringBuilder();
        int addNum = 0;
        int failNum = 0;
        //判空处理
        if (StringUtils.isNotEmpty(projects)) {
            //存放标记信息  公司名称-公司id || 业务员 - 业务员id
            Map<String, Long> dataMap = new HashMap<>();
            //循环调用接口插入数据
            for (ProjectEntityDTO project : projects) {
                // 保存时判断数据库中是否存在此项目编号 如果存在提示
                if (!projectService.notExistContractByIdentifier(project.getIdentifier())) {
                    //返回处理
                    failNum++;
                    fileMsg.append("<BR />项目信息-").append(failNum).append("：项目编号 ").append(project.getIdentifier()).append(" ，原因：该项目编号已存在；");
                    //跳过该项目
                    continue;
                }
                //受检单位
                String company = project.getCompany();
                //受检单位id
                Long companyId = dataMap.get(company);
                if (StringUtils.isNull(companyId)) {
                    //受检单位id
                    Long tempCompanyId = getCompanyId(company, dataMap);
                    if (StringUtils.isNull(tempCompanyId)) {
                        //未找到公司 跳过  并记录信息返回
                        failNum++;
                        fileMsg.append("<BR />项目信息-").append(failNum).append("：项目编号 ").append(project.getIdentifier()).append(" ，原因：未找到受检单位；");
                        continue;
                    }
                    project.setCompanyId(tempCompanyId);
                } else {
                    project.setCompanyId(companyId);
                }
                //委托单位
                String entrustCompany = project.getEntrustCompany();
                //委托单位id
                Long entrustCompanyId = dataMap.get(entrustCompany);
                if (StringUtils.isNull(entrustCompanyId)) {
                    //委托单位id
                    Long tempCompanyId = getCompanyId(company, dataMap);
                    if (StringUtils.isNotNull(tempCompanyId)) {
                        project.setCompanyId(tempCompanyId);
                    }
                } else {
                    project.setEntrustCompanyId(entrustCompanyId);
                }
                //业务员id
                Long salesmenId = dataMap.get(project.getSalesmen());
                if (StringUtils.isNull(salesmenId)) {
                    SysUserEntity salesmen = sysUserService.queryByUserName(project.getSalesmen());
                    if (StringUtils.isNotNull(salesmen)) {
                        Long tempSalesmenId = salesmen.getUserId();
                        project.setSalesmenid(tempSalesmenId);
                        dataMap.put(project.getSalesmen(), tempSalesmenId);
                    }
                } else {
                    project.setSalesmenid(salesmenId);
                }
                //项目信息初始化 true金额回填到合同，false不回填
                ProjectEntity projectEntity = new ProjectEntity();
                BeanUtils.copyBeanProp(projectEntity, project);
                //存入合同id
                projectEntity.setContractId(contractId);
                //调用接口保存
                try {
                    projectService.saveProject(projectEntity, true);
                    addNum++;
                } catch (Exception e) {
                    failNum++;
                    fileMsg.append("<BR />项目-").append(failNum).append("保存失败；详情信息如下：项目编号：").append(project.getIdentifier()).append("；异常提示信息：").append(e.getMessage());
                }
            }
        }
        //整理返回提示信息
        if (addNum > 0) {
            resultMsg.append("成功新增项目信息条数：").append(addNum).append("条。");
        }
        if (failNum > 0) {
            resultMsg.append("项目信息导入失败，共").append(failNum).append("条，错误如下：");
            resultMsg.append(fileMsg);
        }
        return success(resultMsg.toString());
    }

    /**
     * 企业信息数据导入
     */
    @SysLog("企业信息数据导入")
    @PostMapping("/company/import")
    public AjaxResult importCompanyData(MultipartFile file) throws Exception {
        //返回信息
        StringBuilder resultMsg = new StringBuilder();
        //失败信息
        StringBuilder fileMsg = new StringBuilder();
        int addNum = 0;
        int failNum = 0;
        //提取excel数据
        ExcelUtil<CompanyEntityDTO> util = new ExcelUtil<>(CompanyEntityDTO.class);
        List<CompanyEntityDTO> companyList = util.importExcel(file.getInputStream());
        if (StringUtils.isNotEmpty(companyList)) {
            //循环数据
            for (CompanyEntityDTO companyTemp : companyList) {
                //检查该企业信息是否存在
                List<CompanyEntity> companys = companyService.list(new QueryWrapper<CompanyEntity>().eq("company", companyTemp.getCompany()).eq("data_belong", getCompanyName()));
                if (StringUtils.isNotEmpty(companys)) {
                    //已存在该公司信息
                    failNum++;
                    fileMsg.append("<BR />数据异常-").append(failNum).append("---企业名称：").append(companyTemp.getCompany()).append(" ，原因：该企业信息已存在；");
                    continue;
                }
                //联系人信息
                CompanyContactEntity companyContact = companyTemp.getCompanyContact();
                companyTemp.setDataBelong(getCompanyName());
                CompanyEntity company = new CompanyEntity();
                //复制数据
                BeanUtils.copyBeanProp(company, companyTemp);
                //补充默认联系人及联系电话
                company.setContact(companyContact.getContact());
                company.setMobile(companyContact.getMobile());
                //继续延用之前的业务逻辑接口
                companyService.save(company);
                addNum++;
                List<CompanyContactEntity> contactList = collateData(company.getId(), companyTemp);
                companyContactService.saveBatch(contactList);
            }
        } else {
            failNum++;
            fileMsg.append("<BR />数据异常-").append(failNum).append(" ，原因：excel表格数据异常；");
        }
        //整理返回提示信息
        if (addNum > 0) {
            resultMsg.append("成功新增条数：").append(addNum).append("条。");
        }
        if (failNum > 0) {
            resultMsg.append("数据导入失败，共").append(failNum).append("条，错误如下：");
            resultMsg.append(fileMsg);
        }
        return AjaxResult.success(resultMsg.toString());
    }

    /**
     * 整理数据
     *
     * @param companyId   企业信息id
     * @param companyTemp 组装数据
     * @return 数据
     */
    private List<CompanyContactEntity> collateData(Long companyId, CompanyEntityDTO companyTemp) {
        //联系人去重信息
        HashSet<String> nameSet = new HashSet<>();
        //联系人信息
        List<CompanyContactEntity> list = new ArrayList<>();
        //默认联系人
        CompanyContactEntity companyContact = companyTemp.getCompanyContact();
        companyContact.setCompanyId(companyId);
        companyContact.setIsDefault(1);
        list.add(companyContact);
        //联系人2
        CompanyContactEntity companyContact2 = companyTemp.getCompanyContact2();
        if (StringUtils.isNotEmpty(companyContact2.getContact()) && nameSet.add(companyContact2.getContact())) {
            companyContact2.setCompanyId(companyId);
            companyContact2.setIsDefault(0);
            list.add(companyContact2);
        }
        //联系人3
        CompanyContactEntity companyContact3 = companyTemp.getCompanyContact3();
        if (StringUtils.isNotEmpty(companyContact3.getContact()) && nameSet.add(companyContact3.getContact())) {
            companyContact3.setCompanyId(companyId);
            companyContact3.setIsDefault(0);
            list.add(companyContact3);
        }
        //联系人4
        CompanyContactEntity companyContact4 = companyTemp.getCompanyContact4();
        if (StringUtils.isNotEmpty(companyContact4.getContact()) && nameSet.add(companyContact4.getContact())) {
            companyContact4.setCompanyId(companyId);
            companyContact4.setIsDefault(0);
            list.add(companyContact4);
        }
        //联系人5
        CompanyContactEntity companyContact5 = companyTemp.getCompanyContact5();
        if (StringUtils.isNotEmpty(companyContact5.getContact()) && nameSet.add(companyContact5.getContact())) {
            companyContact5.setCompanyId(companyId);
            companyContact5.setIsDefault(0);
            list.add(companyContact5);
        }
        return list;
    }

    /**
     * 校验合同信息
     *
     * @param contractTemp 合同信息
     * @param failNumMap   失败数量
     * @param fileMsg      失败信息
     * @return 结果
     */
    private boolean checkContractInfo(ContractEntityDTO contractTemp, Map<String, Integer> failNumMap, StringBuilder fileMsg, Map<String, CompanyEntity> dataMap) {
        //失败次数
        Integer failNum = failNumMap.get("failNum");
        //数据校验-1 合同编号
        String identifier = contractTemp.getIdentifier();
        if (StringUtils.isEmpty(identifier)) {
            failNum++;
            failNumMap.put("failNum", failNum);
            fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 原因：合同编号缺失；", failNum));
            return false;
        }
        if (!contractService.notExistContractByIdentifier(identifier)) {
            failNum++;
            failNumMap.put("failNum", failNum);
            fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：该合同编号已存在；", failNum, identifier));
            return false;
        }
        //数据校验-2 项目编号
        String projectIdentifier = contractTemp.getProjectIdentifier();
        if (StringUtils.isEmpty(projectIdentifier)) {
            failNum++;
            failNumMap.put("failNum", failNum);
            fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：项目编号缺失；", failNum, identifier));
            return false;
        }
        if (!projectService.notExistContractByIdentifier(projectIdentifier)) {
            failNum++;
            failNumMap.put("failNum", failNum);
            fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：该合同中项目编号：{} 已存在；", failNum, identifier, projectIdentifier));
            return false;
        }
        //校验-3 受检单位-及填充数据
        String companyName = contractTemp.getCompany();
        CompanyEntity company = dataMap.get(companyName);
        if (StringUtils.isNotNull(company)) {
            //补充缺失信息
            fillCompanyData(contractTemp, company);
        } else {
            //查询受检单位信息
            company = companyService.getOneCompanyInfoByCompanyName(companyName, getCompanyName());
            if (StringUtils.isNull(company)) {
                failNum++;
                failNumMap.put("failNum", failNum);
                fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：未查询到受检单位（{}）相关信息；", failNum, identifier, companyName));
                return false;
            } else {
                //补充缺失信息
                fillCompanyData(contractTemp, company);
                dataMap.put(companyName, company);
            }
        }
        //校验-4 委托单位-及填充数据
        String entrustCompanyName = contractTemp.getEntrustCompany();
        CompanyEntity entrustCompany = dataMap.get(entrustCompanyName);
        if (StringUtils.isNotNull(entrustCompany)) {
            //补充缺失信息
            contractTemp.setEntrustCompanyId(entrustCompany.getId());
            contractTemp.setEntrustOfficeAddress(entrustCompany.getOfficeAddress());
        } else {
            //查询受检单位信息
            entrustCompany = companyService.getOneCompanyInfoByCompanyName(entrustCompanyName, getCompanyName());
            if (StringUtils.isNull(entrustCompany)) {
                failNum++;
                failNumMap.put("failNum", failNum);
                fileMsg.append(StringUtils.format("<BR />合同信息-{} --- 合同编号: {}，原因：未查询到委托单位（{}）相关信息；", failNum, identifier, entrustCompanyName));
                return false;
            } else {
                //补充缺失信息
                contractTemp.setEntrustCompanyId(entrustCompany.getId());
                contractTemp.setEntrustOfficeAddress(entrustCompany.getOfficeAddress());
                dataMap.put(entrustCompanyName, entrustCompany);
            }
        }
        return true;
    }

    /**
     * 填充公司数据
     *
     * @param contractTemp 合同数据
     * @param company      公司数据
     */
    private void fillCompanyData(ContractEntityDTO contractTemp, CompanyEntity company) {
        contractTemp.setCompanyId(company.getId());
        contractTemp.setProvince(company.getProvince());
        contractTemp.setCity(company.getCity());
        contractTemp.setArea(company.getArea());
        contractTemp.setOfficeAddress(company.getOfficeAddress());
        contractTemp.setContact(company.getContact());
        contractTemp.setTelephone(company.getMobile());
    }

    /**
     * 获取公司id
     *
     * @param company 公司名称
     * @param dataMap 信息缓存
     * @return 结果
     */
    private Long getCompanyId(String company, Map<String, Long> dataMap) {
        Long tempCompanyId = null;
        CompanyEntity companyInfo = companyService.getOneCompanyInfoByCompanyName(company, getCompanyName());
        if (StringUtils.isNotNull(companyInfo)) {
            tempCompanyId = companyInfo.getId();
            dataMap.put(company, tempCompanyId);
        }
        return tempCompanyId;
    }
}
