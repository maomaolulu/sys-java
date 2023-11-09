package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.province.constant.Constants;
import may.yuntian.external.province.constant.RequestHeaderConstants;
import may.yuntian.external.province.dto.*;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.entity.ParticipantTable;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.enums.DetectOrganizeInfoEnum;
import may.yuntian.external.province.mapper.BasicInfoMapper;
import may.yuntian.external.province.mapper.ParticipantTableMapper;
import may.yuntian.external.province.mapper.ResultItemMapper;
import may.yuntian.external.province.server.Main;
import may.yuntian.external.province.server.MainServer;
import may.yuntian.external.province.service.ProjectDeclarationService;
import may.yuntian.external.province.util.*;
import may.yuntian.external.province.vo.*;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 11:18
 */
@Service
public class ProjectDeclarationServiceImpl implements ProjectDeclarationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDeclarationServiceImpl.class);
    /** 省平台项目报送服务名称  **/
    private static final QName SERVICE_NAME = new QName("http://server.zhejian.com/", "main");

    @Resource
    private BasicInfoMapper basicInfoMapper;
    @Resource
    private ResultItemMapper resultItemMapper;
    @Resource
    private ParticipantTableMapper participantTableMapper;


    /**
     * 项目推送至省平台
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String push(Long projectId){
        URL wsdlURL = Main.WSDL_LOCATION;
        Main ss = new Main(wsdlURL, SERVICE_NAME);
        MainServer port = ss.getMainServerImplPort();

        // 获取请求报文
        String xmlString = getXmlString(projectId);
        logger.error("打印请求报文：\n{}", xmlString);

        // 调用MainServer接口中的transport方法，参数传递xml格式字符串
        String returnMessage = port.transport(xmlString.replace("\r\n", "").replace("\n", "").replace("\r", "").replace(" ", ""));
        logger.error(returnMessage);

        // 异常信息处理后响应给前端
        StringBuilder builder = new StringBuilder();
        try {
            // xml转Document对象
            Document document = DocumentHelper.parseText(returnMessage);
            // 获取根元素：<data>
            Element root = document.getRootElement();
            Element returnCode = root.element("returnCode");
            if (!Constants.SUCCESS.equals(returnCode.getText())) {
                builder.append("推送失败：").append(returnMessage);
            } else {
                // 推送成功修改项目状态 + 报送日期 + 更新者 + 更新时间
                basicInfoMapper.update(new BasicInfo(5, DateUtil.today(), ShiroUtils.getUserEntity().getUsername(), DateUtil.dateSecond()), Wrappers.lambdaUpdate(BasicInfo.class).eq(ObjectUtil.isNotNull(projectId), BasicInfo::getProjectId, projectId));
            }
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }

        return String.valueOf(builder);
    }

    /**
     * 生成请求报文
     * @param projectId 项目id
     * @return xml字符串
     */
    public String getXmlString(Long projectId) {
        // 1、从pro_表中依次查询项目的：基本信息、参与人员列表、结果项
        BasicInfo basicInfo = basicInfoMapper.selectOne(Wrappers.lambdaQuery(BasicInfo.class).eq(BasicInfo::getProjectId, projectId).in(BasicInfo::getStatus, 0,1,2,3,4));
        List<ParticipantTable> participantTableList = participantTableMapper.selectList(Wrappers.lambdaQuery(ParticipantTable.class).eq(ParticipantTable::getProjectId, projectId));
        List<ResultItem> resultItemList = resultItemMapper.selectList(Wrappers.lambdaQuery(ResultItem.class).eq(ResultItem::getProjectId, projectId));
        if (StringUtils.isNull(basicInfo)) {
            throw new RRException("查无此项目！");
        }
        if (CollUtil.isEmpty(participantTableList)) {
            throw new RRException("查无此项目参与人员信息！");
        }
        if (CollUtil.isEmpty(resultItemList)) {
            throw new RRException("查无此项目检测结果！");
        }
        // 获取项目隶属公司信息详情
        DetectOrganizeInfoEnum detectOrganizeInfoEnum = DetectOrganizeInfoEnum.getOrganizeInfoByLoginUser(basicInfo.getBelongCompany());
        if (ObjectUtil.isNull(detectOrganizeInfoEnum)) {
            throw new RRException("非法用户，请联系管理员！");
        }

        // 2、dto对象封装
            // 2.1 委托机构信息
        EntrustOrgInfo entrustOrgInfo = new EntrustOrgInfo();
        BeanUtils.copyProperties(basicInfo, entrustOrgInfo);
        entrustOrgInfo.setCreditCode(basicInfo.getEntrustCreditCode());
            // 2.2 受检单位信息
        EmpInfo empInfo = new EmpInfo();
        empInfo.setEmpName(basicInfo.getEmpName());
        empInfo.setCreditCode(basicInfo.getEmpCreditCode());
        empInfo.setEconomicTypeCode(Integer.valueOf(basicInfo.getEconomicTypeCode()));
        empInfo.setIndustryCategoryCode(Integer.valueOf(basicInfo.getIndustryCategoryCode()));
        empInfo.setScaleCode(Integer.valueOf(basicInfo.getScaleCode()));
        empInfo.setAreaCode(Integer.valueOf(basicInfo.getAreaCode()));
        empInfo.setAreaName(basicInfo.getAreaName());
        empInfo.setRegAddress(basicInfo.getRegAddress());
        empInfo.setEmployeesTotalNum(Integer.valueOf(basicInfo.getEmployeesTotalNum()));
        empInfo.setContactHazardNum(Integer.valueOf(basicInfo.getContactHazardNum()));
        empInfo.setFieldCode(Integer.valueOf(basicInfo.getFieldCode()));
        empInfo.setAddress(basicInfo.getAddress());
        empInfo.setPostalCode(basicInfo.getPostalCode() == null ? null : Integer.valueOf(basicInfo.getPostalCode()));
        empInfo.setLegalPerson(basicInfo.getLegalPerson());
        empInfo.setLegalPhone(basicInfo.getLegalPhone());
        empInfo.setContactPerson(basicInfo.getContactPerson());
        empInfo.setContactPhone(basicInfo.getContactPhone());
            // 2.3 技术服务地区
        ServiceArea serviceArea = new ServiceArea();
        serviceArea.setServiceAreaCode(Integer.valueOf(basicInfo.getServiceAreaCode()));
        serviceArea.setServiceAddress(basicInfo.getServiceAddress());
        List<ServiceArea> serviceAreaList = new ArrayList<>();
        serviceAreaList.add(serviceArea);
        empInfo.setServiceAreaList(serviceAreaList);
            // 2.4 检测机构信息
        CheckOrgInfo checkOrgInfo = new CheckOrgInfo();
        checkOrgInfo.setOrgName(detectOrganizeInfoEnum.getOrgName());
        checkOrgInfo.setCreditCode(detectOrganizeInfoEnum.getCreditCode());
        checkOrgInfo.setProjectDirectorName(basicInfo.getProjectDirectorName());
        checkOrgInfo.setOrgDirectorName(detectOrganizeInfoEnum.getOrgDirectorName());
            // 2.5 上报单位信息
        WriteOrgInfo writeOrgInfo = new WriteOrgInfo();
        writeOrgInfo.setCreditCode(detectOrganizeInfoEnum.getCreditCode());
        writeOrgInfo.setOrgName(detectOrganizeInfoEnum.getOrgName());
            // 2.6 检测报告开始节点
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setCode(basicInfo.getCode());
        reportInfo.setCheckType(Integer.valueOf(basicInfo.getCheckType()));
        reportInfo.setReportDate(basicInfo.getReportDate());
        reportInfo.setPreparer(basicInfo.getPreparer());
        reportInfo.setPreparerPhone(detectOrganizeInfoEnum.getPreparePhone());
        reportInfo.setIssuer(basicInfo.getIssuer());
        reportInfo.setBeginSurveyDate(basicInfo.getBeginSurveyDate());
        reportInfo.setEndSurveyDate(basicInfo.getEndSurveyDate());
        reportInfo.setBeginSamplingDate(basicInfo.getBeginSamplingDate());
        reportInfo.setEndSamplingDate(basicInfo.getEndSamplingDate());
            // 2.7 参与人员列表开始节点
        List<Participant> participantList = new ArrayList<>();
        // 根据userId_name分组
        Map<String, List<ParticipantTable>> groupMap = participantTableList.stream().collect(Collectors.groupingBy(participantTable -> participantTable.getUserId() + "_" + participantTable.getName()));
        groupMap.forEach((key, valueList) -> {
            Participant participant = new Participant();
            String name = key.substring(key.indexOf("_") + 1);
            participant.setName(name);
            List<ServiceItem> serviceItemList = new ArrayList<>();
            valueList.forEach(participantTable -> {
                ServiceItem serviceItem = new ServiceItem();
                serviceItem.setItemCode(Integer.valueOf(participantTable.getItemCode()));
                serviceItem.setItemName(participantTable.getItemName());
                serviceItemList.add(serviceItem);
            });
            participant.setServiceItemList(serviceItemList);
            participantList.add(participant);
        });
        reportInfo.setParticipantList(participantList);
            // 2.8 检测点列表开始节点
        List<DetectionPoint> detectionPointList = new ArrayList<>();
        resultItemList.forEach(resultItem -> {
            DetectionPoint detectionPoint = new DetectionPoint();
            detectionPoint.setCheckItemCode(resultItem.getCheckItemCode());
            detectionPoint.setWorkArea(resultItem.getWorkArea());
            detectionPoint.setDetectionArea(resultItem.getDetectionArea());
            detectionPoint.setDailyContactTime(Float.valueOf(resultItem.getDailyContactTime()));
            detectionPoint.setWeekWorkDay(Float.valueOf(resultItem.getWeekWorkDay()));
            detectionPoint.setPointName(resultItem.getPointName());
            detectionPoint.setCheckDate(DateUtils.parseDate(resultItem.getDetectionDate()));
            detectionPoint.setConclusion("符合".equals(resultItem.getConclusion()) ? 1 : 0);
            List<Result> resultList = new ArrayList<>();
            String[] split = resultItem.getResult().split(",");
            String[] splitCode = resultItem.getCode().split(",");
            String[] splitUnit = resultItem.getUnit().split(",");
            for (int i = 0; i < split.length; i++) {
                Result result = new Result();
                // 紫外辐射-辐照度单位换算：mJ/cm2 ---> J/cm2    // 微波辐射-短时间接触功率密度单位换算：mW/cm2	--->  W/cm2
                if ((resultItem.getFactorType() == 5 && "109".equals(splitCode[i]))
                        || (resultItem.getFactorType() == 10 && "113".equals(splitCode[i]))) {
                    BigDecimal decimal = new BigDecimal(split[i]);
                    BigDecimal divide = decimal.divide(new BigDecimal("1000"));
                    result.setResult(String.valueOf(divide));
                } else if (resultItem.getFactorType() == 10 && "110".equals(splitCode[i])) {
                    // 微波辐射-日剂量单位换算：μW·h/cm2   --> 除以日接触时间h ---> μW/cm2
                    BigDecimal decimal = new BigDecimal(split[i]);
                    BigDecimal time = new BigDecimal(resultItem.getDailyContactTime());
                    result.setResult(String.valueOf(decimal.subtract(time)));
                } else {
                    result.setResult(split[i]);
                }
                result.setCode(Integer.valueOf(splitCode[i]));
                result.setUnit(Integer.valueOf(splitUnit[i]));
                resultList.add(result);
            }
            detectionPoint.setResultList(resultList);
            detectionPointList.add(detectionPoint);
        });

        List<Report> reportList = new ArrayList<>();
        // 单条目测试（也可做批量申报，视后期需求而定！）
        Report report = new Report();
        report.setEntrustOrgInfo(entrustOrgInfo);
        report.setEmpInfo(empInfo);
        report.setCheckOrgInfo(checkOrgInfo);
        report.setWriteOrgInfo(writeOrgInfo);
        report.setReportInfo(reportInfo);
        report.setDetectionPointList(detectionPointList);
        reportList.add(report);

        // 3、响应数据格式处理
        Data data = new Data();
            // 3.2 请求体
        Body body = new Body();
        body.setReportList(reportList);
            // 3.2 请求头
        Header header = new Header();
        header.setService(RequestHeaderConstants.SERVICE);
        header.setUserId(detectOrganizeInfoEnum.getUserID());
        String requestTime = DateUtil.now().replace(" ", "");
        header.setRequestTime(requestTime);
            // 3.2.1 md5用户认证密钥
        header.setHeadSign(Md5Utils.toMD5Way(RequestHeaderConstants.SERVICE.concat(requestTime).concat(detectOrganizeInfoEnum.getUserID()).concat(detectOrganizeInfoEnum.getPassword())));
            // 3.2.2 body体数据签名（sha256WithRsa）
        try {
            String bodyXmlStr = XmlUtil.toXml(body);
            String removeNewlineBody = bodyXmlStr.replace("\r\n", "").replace("\n", "").replace("\r", "").replace(" ", "");
            // ④加签结果
            String signResult = RSAUtils.signByPrivateKey(removeNewlineBody.getBytes(), detectOrganizeInfoEnum.getSecretKey());
            header.setBodySign(signResult);
        } catch (Exception e) {
            logger.error("省报送加签异常：{}", e.getMessage());
        }
        data.setHeader(header);
        data.setBody(body);

        // 4、Data对象转xml并返回
        return XmlUtil.toXml(data);
    }

    /**
     * 主管or质控：项目列表
     */
    @Override
    public List<BasicInfo> getProjectList(BasicInfo info) {
        // viewer 1主管，2质控    // dataSource 1检评，2评价
        QueryWrapper<BasicInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(info.getCode()), "code", info.getCode());
        wrapper.like(StrUtil.isNotBlank(info.getCheckType()), "check_type", info.getCheckType());
        wrapper.like(StrUtil.isNotBlank(info.getProjectDirectorName()), "project_director_name", info.getProjectDirectorName());
        wrapper.like(StrUtil.isNotBlank(info.getEmpName()), "emp_name", info.getEmpName());
        wrapper.like(ObjectUtil.isNotNull(info.getStatus()),"status", info.getStatus());
        wrapper.ge(StrUtil.isNotBlank(info.getSubmitDate()), "submit_date", info.getSubmitDate());
        wrapper.le(StrUtil.isNotBlank(info.getSubmitEndDate()), "submit_date", info.getSubmitEndDate());
        wrapper.in(info.getViewer() == 1, "status", 1, 4, 5);
        wrapper.in(info.getViewer() == 2, "status", 3, 5);
        // 数据分流
        wrapper.eq("belong_company", ShiroUtils.getUserEntity().getSubjection());
        wrapper.in(ObjectUtil.isNotNull(info.getDataSource()) && info.getDataSource() == 1, "check_type", "10", "20");
        wrapper.in(ObjectUtil.isNotNull(info.getDataSource()) && info.getDataSource() == 2, "check_type", "30", "31", "32", "33");
        wrapper.orderByDesc("submit_date");
        pageUtil2.startPage();
        return basicInfoMapper.selectList(wrapper);
    }

    /**
     * 主管/质控：查看
     */
    @Override
    public PushDataVo viewProjectDataInfo(Long projectId, Integer viewer) {
        PushDataVo dataVo = new PushDataVo();
        BasicInfo basicInfo = basicInfoMapper.selectOne(Wrappers.lambdaQuery(BasicInfo.class).eq(BasicInfo::getProjectId, projectId).and(basicInfoLambdaQueryWrapper -> basicInfoLambdaQueryWrapper.in(viewer == 1, BasicInfo::getStatus, 1, 4, 5).in(viewer == 2, BasicInfo::getStatus, 3, 5) ));
        List<ParticipantTable> participantList = participantTableMapper.selectList(Wrappers.lambdaQuery(ParticipantTable.class).eq(ParticipantTable::getProjectId, projectId));
        List<ResultItem> resultList = resultItemMapper.selectList(Wrappers.lambdaQuery(ResultItem.class).eq(ResultItem::getProjectId, projectId));
        if (CollUtil.isEmpty(resultList)) {
            dataVo.setResultMap(Collections.emptyMap());
        } else {
            Map<Integer, Map<String, List<ResultItem>>> resultMap = new HashMap<>();
            resultList.stream().collect(Collectors.groupingBy(ResultItem::getFactorType)).forEach((type, valueList) -> {
                if (CollUtil.isNotEmpty(valueList))  resultMap.put(type, valueList.stream().collect(Collectors.groupingBy(ResultItem::getCode)));
            });
            dataVo.setResultMap(resultMap);
        }
        dataVo.setBasicInfo(basicInfo);
        dataVo.setParticipantList(participantList);
        return dataVo;
    }

    /**
     * 主管/质控：驳回
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rejectReason(RejectVo rejectVo) {
        return basicInfoMapper.updateByProjectId(rejectVo.getViewer(), rejectVo.getReason(), ShiroUtils.getUserEntity().getUsername(), DateUtil.dateSecond(), rejectVo.getProjectId());
    }

    /**
     * 主管：提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int chargeSubmit(Long projectId) {
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setStatus(3);
        basicInfo.setUpdateTime(DateUtil.dateSecond());
        basicInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        return basicInfoMapper.update(basicInfo, Wrappers.lambdaUpdate(BasicInfo.class).eq(BasicInfo::getProjectId, projectId));
    }

}
