package may.yuntian.publicity.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.service.*;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anliantest.service.MetNewsService;
import may.yuntian.anlianwage.service.GradePointService;
import may.yuntian.anlianwage.service.PerformanceAllocationService;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.common.exception.RRException;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.modules.sys.dao.SysDeptDao;
import may.yuntian.modules.sys.entity.EmailVo;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.EmailVoService;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.publicity.dto.PublicPjDto;
import may.yuntian.publicity.entity.EvalImageLibraryEntity;
import may.yuntian.publicity.entity.ProjectDownloadNumberEntity;
import may.yuntian.publicity.entity.PublicityInfoEntity;
import may.yuntian.publicity.entity.ZjSampleImgEntity;
import may.yuntian.publicity.mapper.ProjectDownloadNumberMapper;
import may.yuntian.publicity.mapper.PublicityMapper;
import may.yuntian.publicity.vo.PublicPjPageVo;
import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.publicity.vo.PublictyPjInfoVo;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;
import may.yuntian.socket.service.IAbuProjectNoteService;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@SuppressWarnings("all")
public class PublicityService {

    @Autowired
    private ProjectDownloadNumberMapper projectDownloadNumberMapper;
    @Autowired
    private PublicityMapper publicityMapper;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private ZjSampleImgService sampleImgService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private OrderSourceService orderSourceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private PublicityInfoService publicityInfoService;
    @Autowired
    private ProjectProceduresService projectProceduresService;
    @Autowired
    private MetNewsService metNewsService;
    @Autowired
    private EmailVoService emailVoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private EvalImageLibraryService evalImageLibraryService;
    @Autowired
    private AlCompanySurveyService alCompanySurveyService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private SysDeptDao deptDao;
    @Autowired
    private IAbuProjectNoteService iAbuProjectNoteService;

    @Value("${apiPath.pythonPjPdfApi}")
    private String pythonPjApiPath;
    @Value("${apiPath.pythonZjPdfApi}")
    private String pythonZjApiPath;


    /**
     * 获取单条公示信息--检评
     *
     * @param projectId
     * @return
     */
    public PublicityInfoVo getInfo(Long projectId) {
        PublicityInfoVo publicityInfoVo = publicityMapper.getPublicityOne(projectId);
        if (publicityInfoVo != null) {
            // 采样陪同人
            if (StringUtils.isBlank(publicityInfoVo.getSamplingCompany())) {
                // 调查陪同人
                publicityInfoVo.setSamplingCompany(publicityInfoVo.getAccompany());
            }
            // 采样日期
            if (StringUtils.isBlank(publicityInfoVo.getTestDate())) {
                if (StringUtils.isNotBlank(publicityInfoVo.getStartDate()) && StringUtils.isNotBlank(publicityInfoVo.getEndDate())) {
                    if (publicityInfoVo.getStartDate().equals(publicityInfoVo.getEndDate())) {
                        publicityInfoVo.setTestDate(publicityInfoVo.getStartDate());
                    } else {
                        publicityInfoVo.setTestDate(publicityInfoVo.getStartDate() + "~" + publicityInfoVo.getEndDate());
                    }
                }
            }
            //技术服务项目组人员
            Set<String> strings1 = new HashSet<>();
            String[] stringss;
            if ("嘉兴安联".equals(publicityInfoVo.getCompanyOrder())){
                stringss = new String[]{"顾燕君", "朱慧花"};
            }else {
                stringss = new String[]{"虞爱旭", "孙春花", "赵鑫"};
            }
            strings1.addAll(Arrays.asList(stringss));

            // 报告编制人（负责人）  注： 若负责人是张纯替换成戴文雅 若负责人是欧阳婷替换成王玲玲
            if (StringUtils.isNotBlank(publicityInfoVo.getCharge())) {
                if (publicityInfoVo.getCharge().equals("张纯")) {
                    publicityInfoVo.setCharge("戴文雅");
                    strings1.add("戴文雅");
                } else if (publicityInfoVo.getCharge().equals("欧阳婷")) {
                    publicityInfoVo.setCharge("王玲玲");
                    strings1.add("王玲玲");
                } else {
                    strings1.add(publicityInfoVo.getCharge());
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getSurveyUser())) {
                //现场调查人员
                List<ProjectUserEntity> list = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 110));
                if (list != null && list.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setSurveyUser(join);
                } else {
                    publicityInfoVo.setSurveyUser("");
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getSamplingUser())) {
                //现场采样人员
                List<ProjectUserEntity> list2 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 120));
                if (list2 != null && list2.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list2
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setSamplingUser(join);
                } else {
                    publicityInfoVo.setSamplingUser("");
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getLaboratoryPerson())) {//实验室人员
                List<ProjectUserEntity> list3 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 130));
                if (list3 != null && list3.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list3
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setLaboratoryPerson(join);
                } else {
                    publicityInfoVo.setLaboratoryPerson("");
                }

            }

            if (StringUtils.isBlank(publicityInfoVo.getTechnicalPersons())) {
                String userStr = strings1.stream().distinct().collect(Collectors.joining("、"));
                publicityInfoVo.setTechnicalPersons(userStr);
            }
            //检测图
            List<ZjSampleImgEntity> sampleImgEntityList = sampleImgService.list(new QueryWrapper<ZjSampleImgEntity>().eq("project_id", projectId));
            if (sampleImgEntityList != null && sampleImgEntityList.size() > 0) {
                publicityInfoVo.setSampleImageList(sampleImgEntityList);
            } else {
                publicityInfoVo.setSampleImageList(new ArrayList<ZjSampleImgEntity>());
            }
        }
        return publicityInfoVo;
    }

    /**
     * 获取公示xin详细信息--检评
     *
     * @param projectId
     * @return
     */
    public PublicityInfoVo getNewInfo(Long projectId) {
        PublicityInfoVo publicityInfoVo = publicityMapper.getProjectType(projectId);
        // 此项目有关联信息
        if (publicityInfoVo != null) {
            if (publicityInfoVo.getType().equals("职卫监督")) {
                // 只提供报告签发日期字段信息
                PublicityInfoVo healthSupervision = publicityMapper.getInfoOccupationalHealthSupervision(projectId);
                dataHanding(healthSupervision, projectId);
                healthSupervision.setType(publicityInfoVo.getType());
                return healthSupervision;
            } else if (publicityInfoVo.getType().equals("检评")) {
                PublicityInfoVo inspectionEvaluation = publicityMapper.getPublicityOne(projectId);
                dataHanding(inspectionEvaluation, projectId);
                inspectionEvaluation.setType(publicityInfoVo.getType());
                return inspectionEvaluation;
//                if (inspectionEvaluation.getDetectionType().equals("定期")) {
//                    // 全提供字段信息
//
//                }
            }
        }
        return publicityInfoVo;
    }

    /**
     * 封装方法 返回前段数据处理
     *
     * @param publicityInfoVo
     * @param projectId
     * @return
     */
    public PublicityInfoVo dataHanding(PublicityInfoVo publicityInfoVo, Long projectId) {
        if (publicityInfoVo != null) {
            // 采样陪同人
            if (StringUtils.isBlank(publicityInfoVo.getSamplingCompany())) {
                // 调查陪同人
                publicityInfoVo.setSamplingCompany(publicityInfoVo.getAccompany());
            }
            // 采样日期
            if (StringUtils.isBlank(publicityInfoVo.getTestDate())) {
                if (StringUtils.isNotBlank(publicityInfoVo.getStartDate()) && StringUtils.isNotBlank(publicityInfoVo.getEndDate())) {
                    if (publicityInfoVo.getStartDate().equals(publicityInfoVo.getEndDate())) {
                        publicityInfoVo.setTestDate(publicityInfoVo.getStartDate());
                    } else {
                        publicityInfoVo.setTestDate(publicityInfoVo.getStartDate() + "~" + publicityInfoVo.getEndDate());
                    }
                }
            }
            //技术服务项目组人员
            // 获取报告封面日期
            Set<String> strings1 = new HashSet<>();
            Date reportCoverDate = publicityInfoVo.getReportCoverDate();
            String[] stringss;
            if ("嘉兴安联".equals(publicityInfoVo.getCompanyOrder())) {
                stringss = new String[]{"顾燕君", "朱慧花"};
            }else {
                if (reportCoverDate != null) {
                    String time = new String("2023-03-01 00:00:00");
                    // 时区
                    ZoneId zoneId = ZoneId.systemDefault();
                    // 报告封面日期
                    LocalDateTime localreportCoverDate = LocalDateTime.ofInstant(reportCoverDate.toInstant(), zoneId);
                    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    // 2023年3月1号
                    LocalDateTime localDateTime = LocalDateTime.parse(time, dtf2);
                    if (localreportCoverDate.isBefore(localDateTime)) {
                        stringss = new String[]{"虞爱旭", "孙春花", "赵鑫"};
                        strings1.addAll(Arrays.asList(stringss));
                    } else {
                        stringss = new String[]{"虞爱旭", "赵鑫"};
                        strings1.addAll(Arrays.asList(stringss));
                    }
                } else {
                    stringss = new String[]{"虞爱旭", "赵鑫"};
                    strings1.addAll(Arrays.asList(stringss));
                }
            }
            // 报告编制人（负责人）  注： 若负责人是张纯替换成戴文雅 若负责人是欧阳婷替换成王玲玲
            if (StringUtils.isNotBlank(publicityInfoVo.getCharge())) {
                if (publicityInfoVo.getCharge().equals("方婷")||publicityInfoVo.getCharge().equals("梁晶")) {
                    publicityInfoVo.setCharge("戴文雅");
                    strings1.add("戴文雅");
                } else if (publicityInfoVo.getCharge().equals("魏绍玲")) {
                    publicityInfoVo.setCharge("杨梦蝶");
                    strings1.add("杨梦蝶");
                }else if (publicityInfoVo.getCharge().equals("李嘉威")||publicityInfoVo.getCharge().equals("王智翔")){
                    publicityInfoVo.setCharge("李涛涛");
                    strings1.add("李涛涛");
                }
                else {
                    strings1.add(publicityInfoVo.getCharge());
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getSurveyUser())) {
                //现场调查人员
                List<ProjectUserEntity> list = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 110));
                if (list != null && list.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setSurveyUser(join);
                } else {
                    publicityInfoVo.setSurveyUser("");
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getSamplingUser())) {
                //现场采样人员
                List<ProjectUserEntity> list2 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 120));
                if (list2 != null && list2.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list2
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setSamplingUser(join);
                } else {
                    publicityInfoVo.setSamplingUser("");
                }
            }
            if (StringUtils.isBlank(publicityInfoVo.getLaboratoryPerson())) {//实验室人员
                List<ProjectUserEntity> list3 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", publicityInfoVo.getId()).eq("types", 130));
                if (list3 != null && list3.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list3
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    publicityInfoVo.setLaboratoryPerson(join);
                } else {
                    publicityInfoVo.setLaboratoryPerson("");
                }

            }
            if (StringUtils.isBlank(publicityInfoVo.getTechnicalPersons())) {
                String userStr = strings1.stream().distinct().collect(Collectors.joining("、"));
                publicityInfoVo.setTechnicalPersons(userStr);
            }
            //检测图
            List<ZjSampleImgEntity> sampleImgEntityList = sampleImgService.list(new QueryWrapper<ZjSampleImgEntity>().eq("project_id", projectId));
            if (sampleImgEntityList != null && sampleImgEntityList.size() > 0) {
                publicityInfoVo.setSampleImageList(sampleImgEntityList);
            } else {
                publicityInfoVo.setSampleImageList(new ArrayList<ZjSampleImgEntity>());
            }
        }
        return publicityInfoVo;
    }

    /**
     * 保存公示信息
     *
     * @param publicityInfoVo
     */
    public void saveInfo(PublicityInfoVo publicityInfoVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityInfoVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);
        projectDateEntity.setReportCoverDate(publicityInfoVo.getReportCoverDate());
        projectDateService.updateById(projectDateEntity);
        companySurveyEntity.setAccompany(publicityInfoVo.getAccompany());
        companySurveyEntity.setSamplingCompany(publicityInfoVo.getSamplingCompany());
        companySurveyEntity.setSamplingDate(publicityInfoVo.getTestDate());
        companySurveyEntity.setTestDate(publicityInfoVo.getTestDate());
        companySurveyEntity.setTechnicalPersons(publicityInfoVo.getTechnicalPersons());
        companySurveyEntity.setLaboratoryPerson(publicityInfoVo.getLaboratoryPerson());
        companySurveyEntity.setPublicityPath(publicityInfoVo.getPublicityPath());
        companySurveyService.updateById(companySurveyEntity);
    }


    /**
     * 提交审核
     *
     * @param publicityInfoVo
     */
    public void saveReview(PublicityInfoVo publicityInfoVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityInfoVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        if (!publicityInfoVo.getDetectionType().equals("定期检测")) {
            project.setPubStatus(4);
//            project.setStatus(40);
            project.setPublicityLastTime(new Date());
            projectService.updateById(project);
        } else {
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);
            projectDateEntity.setReportCoverDate(publicityInfoVo.getReportCoverDate());
            projectDateService.updateById(projectDateEntity);
            companySurveyEntity.setSamplingCompany(publicityInfoVo.getSamplingCompany());
            companySurveyEntity.setTestDate(publicityInfoVo.getTestDate());
            companySurveyEntity.setTechnicalPersons(publicityInfoVo.getTechnicalPersons());
            companySurveyEntity.setLaboratoryPerson(publicityInfoVo.getLaboratoryPerson());
            companySurveyEntity.setPublicityPath(publicityInfoVo.getPublicityPath());
            companySurveyService.updateById(companySurveyEntity);
            project.setPubStatus(2);
//            project.setStatus(40);
            project.setPublicityLastTime(new Date());
            projectService.updateById(project);
            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            publicityInfo.setProjectId(projectId);
            publicityInfo.setStatus(2);
            publicityInfo.setUsername(sysUserEntity.getUsername());
            publicityInfo.setOperation("提交审核");
            publicityInfo.setOperationTime(new Date());
            publicityInfoService.save(publicityInfo);
        }


    }

    /**
     * xin提交审核
     *
     * @param publicityInfoVo
     */
    public void saveNewReview(PublicityInfoVo publicityInfoVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityInfoVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        if (publicityInfoVo.getDetectionType() != null) {
            if (!publicityInfoVo.getDetectionType().equals("定期检测")) {
                // 4:主管通过/待审核（质控显示‘待审核’）
                project.setPubStatus(4);
                // 40:质控签发
//                project.setStatus(40);
                project.setPublicityLastTime(new Date());
                projectService.updateById(project);
            } else {
                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
                CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);
                projectDateEntity.setReportCoverDate(publicityInfoVo.getReportCoverDate());
                projectDateService.updateById(projectDateEntity);
                companySurveyEntity.setSamplingCompany(publicityInfoVo.getSamplingCompany());
                companySurveyEntity.setTestDate(publicityInfoVo.getTestDate());
                companySurveyEntity.setTechnicalPersons(publicityInfoVo.getTechnicalPersons());
                companySurveyEntity.setLaboratoryPerson(publicityInfoVo.getLaboratoryPerson());
                companySurveyEntity.setPublicityPath(publicityInfoVo.getPublicityPath());
                companySurveyService.updateById(companySurveyEntity);
                // 2:待审核
                project.setPubStatus(2);
                // 40:质控签发
//                project.setStatus(40);
                project.setPublicityLastTime(new Date());
                projectService.updateById(project);
                PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
                publicityInfo.setProjectId(projectId);
                publicityInfo.setStatus(2);
                publicityInfo.setUsername(sysUserEntity.getUsername());
                publicityInfo.setOperation("提交审核");
                publicityInfo.setOperationTime(new Date());
                publicityInfoService.save(publicityInfo);
            }
        } else {
            // 4:主管通过/待审核（质控显示‘待审核’）
            project.setPubStatus(4);
            // 40:质控签发
//            project.setStatus(40);
            project.setPublicityLastTime(new Date());
            projectService.updateById(project);
        }
    }


    public void sendMail() {
        String[] emalis = new String[]{"lixin@anliantest.com"};
        EmailVo emailVo = new EmailVo();
        emailVo.setSubject("公示失败发送邮件测试");
        emailVo.setEmails(emalis);
        emailVo.setContent("提交审核失败");
        emailVoService.sendSimpleMail(emailVo);
    }

    /**
     * 主管页面--待审核
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getHeadPageList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "u.subjection", subjection);
//        queryWrapper.between("p.status", 20, 80);
        queryWrapper.in("p.type", "检评","职卫监督","职卫示范");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.in("p.pub_status", 2, 5);
        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StrUtil.isNotBlank(charge), "p.charge", charge);

//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);

        return list;
    }

    /**
     * 主管页面--已审核
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getHeadPageList2(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }
        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "u.subjection", subjection);
//        queryWrapper.between("p.status", 20, 80);
        queryWrapper.in("p.type", "检评","职卫监督","职卫示范");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.in("p.pub_status", 3, 4, 6, 7, 8);
        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);

//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);

        return list;
    }

    /**
     * 主管页面-审核通过
     */
    public void passHeadReview(PublicityPageVo publicityPageVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        project.setPubStatus(4);
        project.setPublicityLastTime(new Date());
        projectService.updateById(project);
        PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
        publicityInfo.setProjectId(projectId);
        publicityInfo.setStatus(4);
        publicityInfo.setUsername(sysUserEntity.getUsername());
        publicityInfo.setOperation("主管通过（质控待审核）");
        publicityInfo.setOperationTime(new Date());
        publicityInfoService.save(publicityInfo);
    }

    /**
     * 主管页面-审核通过(批量)
     */
    public void passHeadReviewBatch(List<PublicityPageVo> publicityPageVos) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        for (PublicityPageVo publicityPageVo : publicityPageVos) {
            this.passHeadReview(publicityPageVo);
        }
    }


    /**
     * 主管驳回
     *
     * @param publicityPageVo
     */
    public void rejectHeadReview(PublicityPageVo publicityPageVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        project.setPubStatus(3);
        project.setPublicityLastTime(new Date());
        project.setDirectorReject(publicityPageVo.getDirectorReject());
        projectService.updateById(project);
        PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
        publicityInfo.setProjectId(projectId);
        publicityInfo.setStatus(3);
        publicityInfo.setUsername(sysUserEntity.getUsername());
        publicityInfo.setOperation("主管驳回");
        publicityInfo.setOperationTime(new Date());
        publicityInfo.setRemark(publicityPageVo.getDirectorReject());
        publicityInfoService.save(publicityInfo);
        //2023/08/07 报告签发主管驳回 发送消息
        if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
            MessageEntity entity = new MessageEntity();
            entity.setTitle("签发失败提醒");
            entity.setContent("【" + project.getIdentifier() + "+" + project.getCompany() + "】签发主管审核失败，请修改后重新提交审核！");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
        }
    }


    /**
     * 质控页面--待审核
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getQualityPageList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();

        // 报告签发日期-时间段字段
        String reportIssueStart = (String) params.get("reportIssueStart");
        String reportIssueEnd = (String) params.get("reportIssueEnd");
        // 报告封面日期-时间段字段
        String reportCoverStart = (String) params.get("reportCoverStart");
        String reportCoveEnd = (String) params.get("reportCoveEnd");

        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.between("p.status", 20, 80);
//        queryWrapper.in("p.type", "检评","现状","控评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.eq("p.pub_status", 4);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StrUtil.isNotBlank(charge), "p.charge", charge);
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "p.company_order", subjection);
        queryWrapper.ge(StringUtils.isNotBlank(reportIssueStart), "pd.report_issue", reportIssueStart);
        queryWrapper.le(StringUtils.isNotBlank(reportIssueEnd), "pd.report_issue", reportIssueEnd);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverStart), "pd.report_cover_date", reportCoverStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoveEnd), "pd.report_cover_date", reportCoveEnd);
        queryWrapper.orderByDesc("p.publicity_last_time");
//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);

        return list;
    }

    /**
     * 质控页面--已审核
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getQualityPageList2(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");

        String typesStr = (String)params.get("types");
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(typesStr)){
            String[] types = typesStr.split("、");
            typeList.addAll(Arrays.asList(types));
        }

        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 报告签发日期-时间段字段
        String reportIssueStart = (String) params.get("reportIssueStart");
        String reportIssueEnd = (String) params.get("reportIssueEnd");
        // 报告封面日期-时间段字段
        String reportCoverStart = (String) params.get("reportCoverStart");
        String reportCoveEnd = (String) params.get("reportCoveEnd");
//        // 审核状态
//        String pubStatus = (String) params.get("pubStatus");
//        Integer pubStatusInt = Integer.valueOf(pubStatus);
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(StringUtils.isNotEmpty(typeList),"p.type",typeList);
//        queryWrapper.between("p.status", 20, 80);
//        queryWrapper.in("p.type", "检评","现状","控评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.between("p.pub_status", 5, 8);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);

        queryWrapper.ge(StringUtils.isNotBlank(reportIssueStart), "pd.report_issue", reportIssueStart);
        queryWrapper.le(StringUtils.isNotBlank(reportIssueEnd), "pd.report_issue", reportIssueEnd);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverStart), "pd.report_cover_date", reportCoverStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoveEnd), "pd.report_cover_date", reportCoveEnd);
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "p.company_order", subjection);

        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        queryWrapper.eq(StringUtils.checkValNotNull(status), "p.pub_status", status);
//        queryWrapper.eq(StringUtils.checkValNotNull(pubStatusInt), "p.pub_status", pubStatusInt);
        queryWrapper.orderByDesc("p.publicity_last_time");
//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);
        return list;
    }


    /**
     * 质控页面--已审核
     *
     * @param params
     * @return
     */
    public Map<String, Object> export(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");

        String typesStr = (String)params.get("types");
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(typesStr)){
            String[] types = typesStr.split("、");
            typeList.addAll(Arrays.asList(types));
        }


        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();
//        // 审核状态
//        String pubStatus = (String) params.get("pubStatus");
//        Integer pubStatusInt = Integer.valueOf(pubStatus);
        // 报告签发日期-时间段字段
        String reportIssueStart = (String) params.get("reportIssueStart");
        String reportIssueEnd = (String) params.get("reportIssueEnd");
        // 报告封面日期-时间段字段
        String reportCoverStart = (String) params.get("reportCoverStart");
        String reportCoveEnd = (String) params.get("reportCoveEnd");
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(StringUtils.isNotEmpty(typeList),"p.type",typeList);
//        queryWrapper.between("p.status", 20, 80);
//        queryWrapper.eq("p.type", "检评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.between("p.pub_status", 5, 8);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "p.company_order", subjection);
        queryWrapper.ge(StringUtils.isNotBlank(reportIssueStart), "pd.report_issue", reportIssueStart);
        queryWrapper.le(StringUtils.isNotBlank(reportIssueEnd), "pd.report_issue", reportIssueEnd);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverStart), "pd.report_cover_date", reportCoverStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoveEnd), "pd.report_cover_date", reportCoveEnd);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);
//        queryWrapper.like(StringUtils.checkValNotNull(pubStatusInt), "p.pub_status", pubStatusInt);
        queryWrapper.orderByAsc("p.publicity_last_time");

//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
//        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);

        //导出数据处理
        List<PublicityPageVo> hangzhouList = new ArrayList<>();
        List<PublicityPageVo> ningboList = new ArrayList<>();
        List<PublicityPageVo> jiaxingList = new ArrayList<>();
        List<PublicityPageVo> shanghaiList = new ArrayList<>();
        String[] pjTypes = new String[]{"预评","现状","专篇","控评"};
        Map<String, Object> map = new LinkedHashMap<>();
        for (PublicityPageVo publicityPageVo : list) {
            if (publicityPageVo.getChargeId() != null) {
                SysUserEntity user = sysUserService.getById(publicityPageVo.getChargeId());
                switch (user.getSubjection()) {
                    case "杭州安联":
                        hangzhouList.add(publicityPageVo);
                        break;
                    case "宁波安联":
                        ningboList.add(publicityPageVo);
                        break;
                    case "嘉兴安联":
                        jiaxingList.add(publicityPageVo);
                        break;
                    case "上海量远":
                        shanghaiList.add(publicityPageVo);
                        break;
                    case "上海研晰":
                        shanghaiList.add(publicityPageVo);
                        break;
                    default:
                        break;
                }
            }
        }
        List<PublicityPageVo> hzPjList = new ArrayList<>();
        List<PublicityPageVo> nbPjList = new ArrayList<>();
        List<PublicityPageVo> jxPjList = new ArrayList<>();
        List<PublicityPageVo> shPjList = new ArrayList<>();
        if (hangzhouList.size() > 0) {
            hzPjList = hangzhouList.stream().filter(i->Arrays.asList(pjTypes).contains(i.getType())).collect(Collectors.toList());
//            map.put("杭州评价",hzPjList);
            hangzhouList.removeAll(hzPjList);
            map.put("杭州检评", hangzhouList);
        }
        if (ningboList.size() > 0) {
            nbPjList = ningboList.stream().filter(i->Arrays.asList(pjTypes).contains(i.getType())).collect(Collectors.toList());
//            map.put("宁波评价",nbPjList);
            ningboList.removeAll(nbPjList);
            map.put("宁波检评", ningboList);
        }
        if (jiaxingList.size() > 0) {
            jxPjList = jiaxingList.stream().filter(i->Arrays.asList(pjTypes).contains(i.getType())).collect(Collectors.toList());
//            map.put("嘉兴评价",jxPjList);
            jiaxingList.removeAll(jxPjList);
            map.put("嘉兴检评", jiaxingList);
        }
        if (shanghaiList.size() > 0) {
            shPjList = shanghaiList.stream().filter(i->Arrays.asList(pjTypes).contains(i.getType())).collect(Collectors.toList());
//            map.put("上海评价",shPjList);
            shanghaiList.removeAll(shPjList);
            map.put("上海检评", shanghaiList);
        }
        if (hzPjList.size()>0){map.put("杭州评价",hzPjList);}
        if (nbPjList.size()>0){map.put("宁波评价",nbPjList);}
        if (jxPjList.size()>0){map.put("嘉兴评价",jxPjList);}
        if (shPjList.size()>0){map.put("上海评价",shPjList);}
        return map;
    }


    /**
     * 质控页面-审核通过
     */
    @Transactional(rollbackFor = Exception.class)
    public void passQualityReview(PublicityPageVo publicityPageVo) {
        String[] pingjiaTeam = new String[]{"预评", "专篇", "控评", "现状"};
        String[] jianpingTeam = new String[]{"检评", "职卫监督"};
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityPageVo.getId();

        CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);

        ProjectEntity project = projectService.getById(projectId);
        project.setPubStatus(6);
        project.setBindingStatus(1);
        project.setStatus(40);
        project.setPublicityLastTime(new Date());
//        project.setProtocol(publicityPageVo.getProtocol());

        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        PublicityInfoEntity publicityInfoEntity = publicityInfoService.getLastLimit(projectId, 2);

        if (publicityInfoEntity == null) {
            projectDateEntity.setReportAccept(new Date());
            projectDateEntity.setReportIssue(new Date());
        } else {
            projectDateEntity.setReportAccept(publicityInfoEntity.getOperationTime());
            projectDateEntity.setReportIssue(publicityInfoEntity.getOperationTime());
        }
        projectService.updateById(project);
//        if ("集团发展".equals(project.getBusinessSource())){
//            if (null!=project.getSalesmenid()){
//                AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
//                abuSendNoteDTO.setProjectId(project.getId());
//                abuSendNoteDTO.setIdentifier(project.getIdentifier());
//                abuSendNoteDTO.setCompany(project.getCompany());
//                abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
//                abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
//                abuSendNoteDTO.setSalesman(project.getSalesmen());
//                abuSendNoteDTO.setStatus(project.getStatus());
//                iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
//            }
//        }
        projectDateService.updateById(projectDateEntity);
        //TODO 计算提成---start
        PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
        GradePointService gradePointService = SpringContextUtils.getBean("gradePointService", GradePointService.class);
        PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
        performanceNodeVo.setProjectId(projectId);
        performanceNodeVo.setReportIssue(projectDateEntity.getReportIssue());
        if (Arrays.asList(jianpingTeam).contains(project.getType())) {
            performanceAllocationService.issueCommission(performanceNodeVo);
        } else if (Arrays.asList(pingjiaTeam).contains(project.getType())) {
            gradePointService.getCommissionIssue(performanceNodeVo);
        }
//        performanceAllocationService.issueCommission(performanceNodeVo);
        //TODO 计算提成---end
//        if ("定期检测".equals(companySurveyEntity.getDetectionType())) {
            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            publicityInfo.setProjectId(projectId);
            publicityInfo.setStatus(6);
            publicityInfo.setUsername(sysUserEntity.getUsername());
            publicityInfo.setOperation("质控通过(待公示)");
            publicityInfo.setOperationTime(new Date());
            publicityInfoService.save(publicityInfo);
//        }
        ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
        ProjectProceduresEntity procedures = new ProjectProceduresEntity();
        if (proceduresEntity != null) {
            projectProceduresService.removeById(proceduresEntity.getId());
            procedures.setProjectId(projectId);
            procedures.setStatus(40);
            if (publicityInfoEntity == null) {
                procedures.setCreatetime(new Date());
            } else {
                procedures.setCreatetime(publicityInfoEntity.getOperationTime());
            }

            projectProceduresService.save(procedures);
        } else {
            procedures.setProjectId(projectId);
            procedures.setStatus(40);
            if (publicityInfoEntity == null) {
                procedures.setCreatetime(new Date());
            } else {
                procedures.setCreatetime(publicityInfoEntity.getOperationTime());
            }
            projectProceduresService.save(procedures);
        }

        //2023/08/07 报告签发质控审核通过 发送消息
        if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
            MessageEntity entity = new MessageEntity();
            entity.setTitle("签发成功提醒");
            entity.setContent("【" + project.getIdentifier() + "+" + project.getCompany() + "】报告已签发，等待胶装");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
        }
    }

    /**
     * 质控页面-审核通过(批量)
     */
    public void passQualityReviewBatch(List<PublicityPageVo> publicityPageVos) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        for (PublicityPageVo publicityPageVo : publicityPageVos) {
            this.passQualityReview(publicityPageVo);
        }
    }


    /**
     * 质控驳回
     *
     * @param publicityPageVo
     */
    public void rejectQualityReview(PublicityPageVo publicityPageVo) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        project.setPubStatus(5);
        project.setPublicityLastTime(new Date());
        project.setControlReject(publicityPageVo.getControlReject());
        projectService.updateById(project);
        PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
        publicityInfo.setProjectId(projectId);
        publicityInfo.setStatus(5);
        publicityInfo.setUsername(sysUserEntity.getUsername());
        publicityInfo.setOperation("质控驳回");
        publicityInfo.setOperationTime(new Date());
        publicityInfo.setRemark(publicityPageVo.getControlReject());
        publicityInfoService.save(publicityInfo);
        //2023/08/07 报告签发质控驳回 发送消息
        if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
            MessageEntity entity = new MessageEntity();
            entity.setTitle("签发失败提醒");
            entity.setContent("【" + project.getIdentifier() + "+" + project.getCompany() + "】签发质控审核失败，请修改后重新提交审核！");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
        }
    }

    /**
     * 胶装页面
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getBindingPageList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");
        String protocol = (String) params.get("protocol");

        String typesStr = (String)params.get("types");
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(typesStr)){
            String[] types = typesStr.split("、");
            typeList.addAll(Arrays.asList(types));
        }

        // 支持分流
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 报告封面时间  report_cover_date 前段传递过来的 开始时间+结束时间
        String reportCoverStart = (String) params.get("reportCoverStart");
        String reportCoverEnd = (String) params.get("reportCoverEnd");
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(StringUtils.isNotEmpty(typeList),"p.type",typeList);
//        queryWrapper.between("p.status", 35, 80);
//        queryWrapper.in("p.type", "检评","现状","控评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.between("p.binding_status", 1, 2);
//        queryWrapper.eq("p.binding_status",6);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        queryWrapper.eq(StringUtils.checkValNotNull(status), "p.binding_status", status);
        queryWrapper.eq(StringUtils.checkValNotNull(subjection), "p.company_order", subjection);
        queryWrapper.eq(StringUtils.isNotBlank(protocol), "p.protocol", protocol);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverStart), "pd.report_cover_date", reportCoverStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoverEnd), "pd.report_cover_date", reportCoverEnd);
        queryWrapper.orderByDesc("p.publicity_last_time");

        pageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);

        // 新增下载次数信息
        List<ProjectDownloadNumberEntity> entityList = projectDownloadNumberMapper.selectList(null);
        Map<Long, Integer> integerMap = entityList.stream().collect(Collectors
                .toMap(ProjectDownloadNumberEntity::getProjectId, ProjectDownloadNumberEntity::getDownloadNumber
                        , (k1, k2) -> k2));
        list.forEach(publicityPageVo -> {
            Long id = publicityPageVo.getId();
            Integer integer = integerMap.get(id);
            if (integer == null) {
                integer = 0;
                publicityPageVo.setDownloadNumber(integer);
            } else {
                publicityPageVo.setDownloadNumber(integer);
            }
        });
        return list;
    }


    /**
     * 质控主管胶装 可填日期
     *
     * @param publicityPageVo
     * @return
     */
    public Integer superBuiding(PublicityPageVo publicityPageVo) {
        if (publicityPageVo.getProtocol()==0){
            new RRException("无委托协议，无法胶装");
        }
        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);


        CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);

        Long days = DateUtils.getDays(publicityPageVo.getPublicityDate(), publicityPageVo.getReportCoverDate());
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        projectDateEntity.setReportBinding(new Date());
        projectDateService.updateById(projectDateEntity);
        if ((StringUtils.isNotBlank(companySurveyEntity.getDetectionType()) && companySurveyEntity.getDetectionType().equals("定期检测"))||project.getType().equals("现状")||project.getType().equals("控评")) {
            if (days <= 15 && days >= 0) {
                PublicityInfoVo publicityInfoVo;
                if (project.getType().equals("现状")||project.getType().equals("控评")){
                    PublictyPjInfoVo pjInfoVo = this.getPjInfo(projectId);
                    publicityInfoVo = ObjectConversion.copy(pjInfoVo,PublicityInfoVo.class);
                    publicityInfoVo.setDetectionType(pjInfoVo.getType());
                }else {
                    publicityInfoVo = this.getInfo(projectId);
                }
//                PublicityInfoVo publicityInfoVo = this.getInfo(projectId);
                publicityInfoVo.setAddtime(publicityPageVo.getPublicityDate());
                publicityInfoVo.setUpdatetime(publicityPageVo.getPublicityDate());
                boolean a = metNewsService.newSaveZjMetNews(publicityInfoVo);
                if (a) {
                    project.setPubStatus(7);
                    project.setBindingStatus(2);
                    project.setStatus(40);
                    project.setQualityPublicity(1);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                    ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                    if (proceduresEntity != null) {
                        projectProceduresService.removeById(proceduresEntity.getId());
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    } else {
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    }
                    PublicityInfoEntity publicityInfo2 = new PublicityInfoEntity();
                    publicityInfo2.setProjectId(projectId);
                    publicityInfo2.setStatus(7);
                    publicityInfo2.setUsername("韦华优");
                    publicityInfo2.setOperation("公示成功");
                    publicityInfo2.setOperationTime(new Date());
                    publicityInfoService.save(publicityInfo2);
                    return 1;
                } else {
                    PublicityInfoEntity publicityInfo2 = new PublicityInfoEntity();
                    publicityInfo2.setProjectId(projectId);
                    publicityInfo2.setStatus(8);
                    publicityInfo2.setUsername("韦华优");
                    publicityInfo2.setOperation("官网服务异常，公示失败");
                    publicityInfo2.setOperationTime(new Date());
                    publicityInfoService.save(publicityInfo2);
                    return 2;//公示失败
                }
            } else {
                if (days > 15) {
                    return 3;//无法公示
                } else {
                    project.setBindingStatus(2);
                    project.setStatus(40);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                    ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                    if (proceduresEntity != null) {
                        projectProceduresService.removeById(proceduresEntity.getId());
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    } else {
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    }
                    return 1;//成功
                }
            }
        } else {
            project.setBindingStatus(2);
            project.setStatus(40);
            projectService.updateById(project);
            if ("集团发展".equals(project.getBusinessSource())){
                if (null!=project.getSalesmenid()){
                    AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                    abuSendNoteDTO.setProjectId(project.getId());
                    abuSendNoteDTO.setIdentifier(project.getIdentifier());
                    abuSendNoteDTO.setCompany(project.getCompany());
                    abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                    abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                    abuSendNoteDTO.setSalesman(project.getSalesmen());
                    abuSendNoteDTO.setStatus(project.getStatus());
                    iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                }
            }
            ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
            ProjectProceduresEntity procedures = new ProjectProceduresEntity();
            if (proceduresEntity != null) {
                projectProceduresService.removeById(proceduresEntity.getId());
                procedures.setProjectId(projectId);
                procedures.setStatus(40);
                projectProceduresService.save(procedures);
            } else {
                procedures.setProjectId(projectId);
                procedures.setStatus(40);
                projectProceduresService.save(procedures);
            }
            return 1;//成功
        }


    }

    /**
     * 修改有无委托协议
     *
     * @param publicityPageVo
     */
    public void saveProtocol(PublicityPageVo publicityPageVo) {
        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        project.setProtocol(publicityPageVo.getProtocol());
        projectService.updateById(project);
    }


    /**
     * 质控胶装
     *
     * @param publicityPageVo
     * @return
     */
    public Integer passBinding(PublicityPageVo publicityPageVo) {
        String[] types = new String[]{"控评","现状"};

        if (publicityPageVo.getProtocol()==0){
            new RRException("无委托协议，无法胶装");
        }

        Long projectId = publicityPageVo.getId();
        ProjectEntity project = projectService.getById(projectId);

        CompanySurveyEntity companySurveyEntity = companySurveyService.seleteByProjectId(projectId);
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        projectDateEntity.setReportBinding(new Date());
        projectDateService.updateById(projectDateEntity);
        if ((StringUtils.isNotBlank(companySurveyEntity.getDetectionType()) && companySurveyEntity.getDetectionType().equals("定期检测"))||Arrays.asList(types).contains(project.getType())) {
            if (publicityPageVo.getIssueDay() <= 15 && publicityPageVo.getIssueDay() >= 0) {
                boolean a;
                if (Arrays.asList(types).contains(project.getType())){
                    PublictyPjInfoVo pjInfoVo = this.getPjInfo(projectId);
                    a = metNewsService.savePjNews(pjInfoVo);
                }else {
                    PublicityInfoVo publicityInfoVo = this.getInfo(projectId);
                    a = metNewsService.newSaveZjMetNews(publicityInfoVo);
                }
                if (a) {
                    project.setPubStatus(7);
                    project.setBindingStatus(2);
                    project.setStatus(40);
                    project.setQualityPublicity(0);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                    ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                    if (proceduresEntity != null) {
                        projectProceduresService.removeById(proceduresEntity.getId());
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    } else {
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    }
                    PublicityInfoEntity publicityInfo2 = new PublicityInfoEntity();
                    publicityInfo2.setProjectId(projectId);
                    publicityInfo2.setStatus(7);
                    publicityInfo2.setUsername("韦华优");
                    publicityInfo2.setOperation("公示成功");
                    publicityInfo2.setOperationTime(new Date());
                    publicityInfoService.save(publicityInfo2);
                    return 1;
                } else {
                    PublicityInfoEntity publicityInfo2 = new PublicityInfoEntity();
                    publicityInfo2.setProjectId(projectId);
                    publicityInfo2.setStatus(8);
                    publicityInfo2.setUsername("韦华优");
                    publicityInfo2.setOperation("官网服务异常，公示失败");
                    publicityInfo2.setOperationTime(new Date());
                    publicityInfoService.save(publicityInfo2);
                    return 2;//公示失败
                }
            } else {
                if (publicityPageVo.getIssueDay() > 15) {
                    return 3;//无法公示
                } else {
                    project.setBindingStatus(2);
                    project.setStatus(40);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
                    ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                    if (proceduresEntity != null) {
                        projectProceduresService.removeById(proceduresEntity.getId());
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    } else {
                        procedures.setProjectId(projectId);
                        procedures.setStatus(40);
                        projectProceduresService.save(procedures);
                    }
                    return 1;//成功
                }
            }
        } else {
            project.setBindingStatus(2);
            project.setStatus(40);
            projectService.updateById(project);
            if ("集团发展".equals(project.getBusinessSource())){
                if (null!=project.getSalesmenid()){
                    AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                    abuSendNoteDTO.setProjectId(project.getId());
                    abuSendNoteDTO.setIdentifier(project.getIdentifier());
                    abuSendNoteDTO.setCompany(project.getCompany());
                    abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                    abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                    abuSendNoteDTO.setSalesman(project.getSalesmen());
                    abuSendNoteDTO.setStatus(project.getStatus());
                    iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
                }
            }
            ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectId, 40);
            ProjectProceduresEntity procedures = new ProjectProceduresEntity();
            if (proceduresEntity != null) {
                projectProceduresService.removeById(proceduresEntity.getId());
                procedures.setProjectId(projectId);
                procedures.setStatus(40);
                projectProceduresService.save(procedures);
            } else {
                procedures.setProjectId(projectId);
                procedures.setStatus(40);
                projectProceduresService.save(procedures);
            }
            return 1;//成功
        }


    }


    /**
     * 手动公示--公示记录页面
     *
     * @param publicityPageVo
     * @return
     */
    public boolean publicity(PublicityPageVo publicityPageVo) {
        Long projectId = publicityPageVo.getId();
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        ProjectEntity project = projectService.getById(projectId);
        PublicityInfoVo publicityInfoVo = this.getInfo(projectId);
        boolean a = metNewsService.newSaveZjMetNews(publicityInfoVo);
        if (a) {
            project.setPubStatus(7);
            projectService.updateById(project);
            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            publicityInfo.setProjectId(projectId);
            publicityInfo.setStatus(7);
            publicityInfo.setUsername("韦华优");
            publicityInfo.setOperation("公示成功");
            publicityInfo.setOperationTime(new Date());
            publicityInfoService.save(publicityInfo);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 自动公示接口
     */
    public void automaticPublicity() {
        List<PublicityInfoVo> list = publicityMapper.getList();
//        List<PublicityInfoVo> list = publicityMapper.getList1(id);
        List<String> failList = new ArrayList<>();

        String[] emalis = new String[]{"wangzhenna@anliantest.com", "weihuayou@anliantest.com"};
        EmailVo emailVo = new EmailVo();
        emailVo.setSubject("项目公示");
        emailVo.setEmails(emalis);

        for (PublicityInfoVo publicityInfoVo : list) {
            Long projectId = publicityInfoVo.getId();
            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            ProjectEntity project = projectService.getById(projectId);
            boolean a = metNewsService.newSaveZjMetNews(publicityInfoVo);
            if (a) {
                project.setPubStatus(7);
                projectService.updateById(project);
                publicityInfo.setProjectId(projectId);
                publicityInfo.setStatus(7);
                publicityInfo.setUsername("韦华优");
                publicityInfo.setOperation("公示成功");
                publicityInfo.setOperationTime(new Date());
                publicityInfoService.save(publicityInfo);
            } else {
                publicityInfo.setProjectId(projectId);
                publicityInfo.setStatus(8);
                publicityInfo.setUsername("韦华优");
                publicityInfo.setOperation("公示失败");
                publicityInfo.setOperationTime(new Date());
                publicityInfoService.save(publicityInfo);
                failList.add(publicityInfoVo.getIdentifier() + publicityInfoVo.getCompany());
                continue;
            }
        }
        if (failList.size() > 0) {
            String s = failList.stream().collect(Collectors.joining("、"));
            String content = "系统自动公示项目，以下项目公示失败：\r\n" + s;
            emailVo.setContent(content);
            emailVoService.sendSimpleMail(emailVo);
        }
    }


    /**
     * 生成PDF
     *
     * @param publicityInfoVo
     * @return
     */
    public String publicityPdf(HttpServletRequest httpRequest, PublicityInfoVo publicityInfoVo) {
        Map<String, Object> map = new HashMap<>();
        //项目组人员+实验室人员
        if (StrUtil.isNotBlank(publicityInfoVo.getLaboratoryPerson())) {
            String addString = publicityInfoVo.getTechnicalPersons() + "、" + publicityInfoVo.getLaboratoryPerson();
            String[] addStrings = addString.split("、");
            String technicalPersons = Arrays.asList(addStrings).stream().distinct().collect(Collectors.joining("、"));
            publicityInfoVo.setTechnicalPersons(technicalPersons);
        } else {
            publicityInfoVo.setTechnicalPersons(publicityInfoVo.getTechnicalPersons());
        }
        //现场调查 陪同时间
        String surveyDate = publicityInfoVo.getSurveyDate();
        if (surveyDate != null) {
//            String format = DateUtil.format(surveyDate, "yyyy-MM-dd");
            map.put("surveyDate", surveyDate);
        } else {
            map.put("surveyDate", "");
        }
        //现场采样 陪同时间
        String samplingDate = publicityInfoVo.getTestDate();
        map.put("samplingDate", samplingDate);
        //报告签发日期
        Date reportIssue = publicityInfoVo.getReportCoverDate();//报告封面日期
        if (reportIssue != null) {
            String format = DateUtil.format(reportIssue, "yyyy-MM-dd");
            map.put("reportIssue", format);
        } else {
            map.put("reportIssue", "");
        }
        List<ZjSampleImgEntity> imgs = publicityInfoVo.getSampleImageList();
        publicityInfoVo.setImgs(imgs);
        publicityInfoVo.setProjectId(publicityInfoVo.getId());
        publicityInfoVo.setFieldInvestigators(publicityInfoVo.getSurveyUser());
        publicityInfoVo.setFieldSampling(publicityInfoVo.getSamplingUser());
        publicityInfoVo.setSamplingDate(publicityInfoVo.getTestDate());
        publicityInfoVo.setReportIssue(publicityInfoVo.getReportCoverDate());

        publicityInfoVo.setMap(map);

        Map<String, Object> mappdf = new HashMap<>();
        JSONObject jsonObject2 = new JSONObject(publicityInfoVo);
        mappdf.put("data", jsonObject2);
        mappdf.put("projectId", publicityInfoVo.getId());
        JSONObject josmmap = JSONUtil.parseObj(mappdf);
//        Object object = HttpUtil.post("http://47.111.249.220:84/proxyAnlianZjPython/zj_quality_control/generate_info_publicity", josmmap.toString());
//        Object object = HttpUtil.post(" http://192.168.0.224:7000/zj_quality_control/generate_info_publicity", josmmap.toString());
        // 测试:公示文件生成pdf格式 调用Python接口
//        Object object = HttpUtil.get("http://47.111.249.220:84/proxyAnlianZjPython/zj_quality_control/ping");

        String apiPath = pythonZjApiPath;
        System.out.println("apiPath = " + apiPath);
        System.out.println("josmmap = " + josmmap.toString());
        String object = HttpRequest.post(apiPath +"zj_quality_control/generate_info_publicity")
                .header("Content-Type", "application/json")
                .header("token",httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().body();

//        System.out.println(object);

        JSONObject jsonObject = new JSONObject(object);
        System.out.println(jsonObject);
        Object o = jsonObject.get("data");
        Object o1 = jsonObject.get("code");
        if (!(o1 != null && o1.toString().equals("200"))) {
            log.error("项目公示生成pdf" + object);
            return null;
        } else {
            return o.toString();
        }
    }

    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     *
     * @param
     * @return
     * TODO 负责人提交审核 负责人所属公司部门主管审核 审核通过后 有项目隶属公司的质控审核
     */
    public QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(QueryWrapper<ProjectEntity> wappr) {
        // 隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 公司旗下所有部门及其子部门
        QueryWrapper<SysDeptEntity> deptWrapper = new QueryWrapper<>();
        deptWrapper.eq("name", subjection);
        Long subjectionDeptId = deptDao.selectOne(deptWrapper).getDeptId();
        QueryWrapper<SysDeptEntity> childrenDeptWrapper = new QueryWrapper<>();
        String deptStructure = "," + subjectionDeptId + ",";
        childrenDeptWrapper.like("dept_structure", deptStructure);
        List<Long> deptList = deptDao.selectList(childrenDeptWrapper).stream().distinct().map(SysDeptEntity::getDeptId).collect(Collectors.toList());
        deptList.add(subjectionDeptId);
        List<String> projectTypeNames = new ArrayList<>();
        projectTypeNames.add("检评");
        projectTypeNames.add("职卫监督");
        projectTypeNames.add("职卫示范");
        // 数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = wappr
                .and(i->i.in("p.dept_id", deptList).or().eq("p.company_order",subjection))
                // 部门权限控制
//                .in("dept_id", deptList)
                // 项目类型权限控制
                .in("p.type", projectTypeNames);

        return queryWrapper;
    }


    /**
     * 更新下载次数
     *
     * @param projectId
     * @return
     */
    public void updateDownloadNumber(Long projectId) {
        LambdaQueryWrapper<ProjectDownloadNumberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectDownloadNumberEntity::getProjectId, projectId);
        ProjectDownloadNumberEntity numberEntity = projectDownloadNumberMapper.selectOne(wrapper);
        ProjectDownloadNumberEntity downloadNumberEntity = new ProjectDownloadNumberEntity();
        // 表里是否有原始数据
        if (numberEntity == null) {
            downloadNumberEntity.setProjectId(projectId);
            downloadNumberEntity.setDownloadNumber(1);
            projectDownloadNumberMapper.insert(downloadNumberEntity);
        } else {
            downloadNumberEntity.setDownloadNumber(numberEntity.getDownloadNumber() + 1);
            downloadNumberEntity.setProjectId(projectId);
            projectDownloadNumberMapper.update(downloadNumberEntity, wrapper);
        }

    }

    /**
     * 获取项目类型
     *
     * @param projectId
     * @return
     */
    public PublicityInfoVo getType(Long projectId) {
        return publicityMapper.getProjectType(projectId);
    }

    //TODO 检评公示结束
    //TODO -----------------------------------------------------------------
    //TODO 评价公示开始

    /**
     * 负责人公示项目列表
     * @param publicPjDto
     * @return
     */
    public List<PublicPjPageVo> getPjChargePublicList(PublicPjDto publicPjDto){
        String identifier = publicPjDto.getIdentifier();
        String type = publicPjDto.getType();
        String company = publicPjDto.getCompany();
        Integer pubStatus = publicPjDto.getPubStatus();
        Integer bindingStatus = publicPjDto.getBindingStatus();
        Integer examineStatus = publicPjDto.getExamineStatus();

        QueryWrapper<Object> queryWrapper = new QueryWrapper();
        queryWrapper.eq("p.charge_id",ShiroUtils.getUserEntity().getUserId());
        queryWrapper.like(StringUtils.isNotBlank(identifier),"p.identifier",identifier);

        queryWrapper.like(StringUtils.isNotBlank(company),"p.company",company);
        if (StringUtils.isNotBlank(type)){
            queryWrapper.like(StringUtils.isNotBlank(type),"p.type",type);
        }else {
            queryWrapper.in("p.type","现状","预评","控评","专篇");
        }

        if (StringUtils.isNotEmpty(pubStatus)||StringUtils.isNotEmpty(bindingStatus)||StringUtils.isNotEmpty(examineStatus)){
            queryWrapper.and(StringUtils.isNotEmpty(pubStatus)||StringUtils.isNotEmpty(bindingStatus)||StringUtils.isNotEmpty(examineStatus),
                    i -> i.eq(StringUtils.isNotEmpty(pubStatus),"p.pub_status",pubStatus).or().eq(StringUtils.isNotEmpty(examineStatus),"p.pub_status",pubStatus).or().eq(StringUtils.isNotEmpty(bindingStatus),"p.binding_status",bindingStatus));
        }
        pageUtil2.startPage();
        List<PublicPjPageVo> publicPjPageVoList = publicityMapper.getPjPublicList(queryWrapper);
        List<Long> projectIdList = publicPjPageVoList.stream().map(PublicPjPageVo::getId).collect(Collectors.toList());
        Map<Long,List<PublicityInfoEntity>> publicityTiJiaoMap = new HashMap<>();
        Map<Long,List<PublicityInfoEntity>> publicityTongGuoMap = new HashMap<>();
        if (StringUtils.isNotEmpty(projectIdList)){
            List<PublicityInfoEntity> publicityTiJiaoList = publicityInfoService.getLastLimitList(projectIdList,2);
            publicityTiJiaoMap = publicityTiJiaoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));
            List<PublicityInfoEntity> publicityTongGuoList = publicityInfoService.getLastLimitList(projectIdList,6);
            publicityTongGuoMap = publicityTongGuoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));
        }
//        List<PublicityInfoEntity> publicityTiJiaoList = publicityInfoService.getLastLimitList(projectIdList,2);
//        Map<Long,List<PublicityInfoEntity>> publicityTiJiaoMap = publicityTiJiaoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));
//        List<PublicityInfoEntity> publicityTongGuoList = publicityInfoService.getLastLimitList(projectIdList,6);
//        Map<Long,List<PublicityInfoEntity>> publicityTongGuoMap = publicityTongGuoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));

        for (PublicPjPageVo publicPjPageVo:publicPjPageVoList){
            Long id = publicPjPageVo.getId();
            publicPjPageVo.setRenderDate(StringUtils.isNotEmpty(publicityTiJiaoMap.get(id))?publicityTiJiaoMap.get(id).get(0).getOperationTime():null);
            publicPjPageVo.setPassDate(StringUtils.isNotEmpty(publicityTongGuoMap.get(id))?publicityTongGuoMap.get(id).get(0).getOperationTime():null);
            if (publicPjPageVo.getPubStatus()<=6){
                publicPjPageVo.setExamineStatus(publicPjPageVo.getPubStatus());
                publicPjPageVo.setPubStatus(6);
            }else {
                publicPjPageVo.setPubStatus(publicPjPageVo.getPubStatus());
                publicPjPageVo.setExamineStatus(6);
            }
        }

        return publicPjPageVoList;
    }

    public PublictyPjInfoVo getPjInfo(Long id){
        PublictyPjInfoVo pjInfoVo = publicityMapper.getInfo(id);
        return returnInfo(pjInfoVo);
    }

    /**
     * 评价公示详情返回值处理
     * @param pjInfoVo
     * @return
     */
    private PublictyPjInfoVo returnInfo(PublictyPjInfoVo pjInfoVo){
        if (pjInfoVo.getPubStatus()<=6){
            pjInfoVo.setExamineStatus(pjInfoVo.getPubStatus());
            pjInfoVo.setPubStatus(6);
        }else {
            pjInfoVo.setPubStatus(pjInfoVo.getPubStatus());
            pjInfoVo.setExamineStatus(6);
        }
        String type = "";
        String typeStr = "";
        switch (pjInfoVo.getType()){
            case "预评":
                type = "职业病危害预评价";
                typeStr = "预评价";
                break;
            case "专篇":
                type = "职业病防护设施设计专篇";
                typeStr = "设计专篇";
                break;
            case "控评":
                type = "职业病危害控制效果评价";
                typeStr = "控制效果评价";
                break;
            case "现状":
                type = "职业病危害现状评价";
                typeStr = "现状评价";
                break;
            default:
                break;
        }
        pjInfoVo.setType(typeStr);
        List<EvalImageLibraryEntity> imgs = evalImageLibraryService.getListByProjectId(pjInfoVo.getId());
        pjInfoVo.setImgList(imgs);
        pjInfoVo.setProjectName(pjInfoVo.getCompany()+type);
        if ("嘉兴安联".equals(pjInfoVo.getCompanyOrder())){
            pjInfoVo.setSkillCharge("戴佳丽");
            pjInfoVo.setQualityCharge("朱慧花");
        }else {
            if (StringUtils.isNotNull(pjInfoVo.getReportCoverDate())){
                Date date = new DateTime("2023-03-01","yyyy-MM-dd");
                if (pjInfoVo.getReportCoverDate().after(date)){
                    pjInfoVo.setSkillCharge("王勇");
                }else {
                    pjInfoVo.setSkillCharge("孙春花");
                }
            }else {
                pjInfoVo.setSkillCharge("");
            }
            pjInfoVo.setQualityCharge("虞爱旭");
        }

        List<ProjectUserEntity> list = projectUserService.getPjPublictyUser(pjInfoVo.getId());
        if (StringUtils.isNotEmpty(list)){
            Map<Integer,List<ProjectUserEntity>> userMap = list.stream().collect(Collectors.groupingBy(ProjectUserEntity::getTypes));
            if (StringUtils.isNotEmpty(userMap.get(110))){
                String surveyPeople = userMap.get(110).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setSurveyPeople(surveyPeople);
            }
            if (StringUtils.isNotEmpty(userMap.get(120))){
                String samplePeople = userMap.get(120).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setSamplePeople(samplePeople);
            }
            if (StringUtils.isNotEmpty(userMap.get(140))){
                String projectCharge = userMap.get(140).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setProjectCharge(projectCharge);
            }
            if (StringUtils.isNotEmpty(userMap.get(150))){
                String reportPreparation = userMap.get(150).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setReportPreparation(reportPreparation);
            }
            if (StringUtils.isNotEmpty(userMap.get(160))){
                String surveyAccompany = userMap.get(160).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setSurveyAccompany(surveyAccompany);
            }else {
                pjInfoVo.setSurveyAccompany(pjInfoVo.getContact());
            }
            if (StringUtils.isNotEmpty(userMap.get(170))){
                String sampleAccompany = userMap.get(170).stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));
                pjInfoVo.setSampleAccompany(sampleAccompany);
            }else {
                pjInfoVo.setSampleAccompany(pjInfoVo.getContact());
            }
        }else {
            pjInfoVo.setSurveyAccompany(pjInfoVo.getContact());
            pjInfoVo.setSampleAccompany(pjInfoVo.getContact());
        }
        if (StringUtils.isNotBlank(pjInfoVo.getReportStartDate()) && StringUtils.isNotBlank(pjInfoVo.getReportEndDate())) {
            if (pjInfoVo.getReportStartDate().equals(pjInfoVo.getReportEndDate())) {
                pjInfoVo.setSampleDate(pjInfoVo.getReportStartDate());
            } else {
                pjInfoVo.setSampleDate(pjInfoVo.getReportStartDate() + "~" + pjInfoVo.getReportEndDate());
            }
        }
        List<PublicityInfoEntity> publicityInfoList = publicityInfoService.getInfoList(pjInfoVo.getId());
        pjInfoVo.setPublicityInfoList(publicityInfoList);
        return pjInfoVo;
    }


    /**
     * 评价公示信息保存
     * @param pjInfoVo
     */
    public void savePjInfo(PublictyPjInfoVo pjInfoVo){
        AlCompanySurveyEntity companySurveyEntity = alCompanySurveyService.getOneByProjectId(pjInfoVo.getId());
        companySurveyEntity.setPublictyPath(pjInfoVo.getPublictyPath());
        alCompanySurveyService.updateById(companySurveyEntity);
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(pjInfoVo.getId());
        projectDateEntity.setReportCoverDate(pjInfoVo.getReportCoverDate());
//        projectDateEntity.setSurveyDate(DateUtil.parse(pjInfoVo.getSurveyDate(),"yyyy-MM-dd HH:mm:ss"));
        projectDateEntity.setReportSurveyDate(DateUtils.parseDate(pjInfoVo.getSurveyDate()));
        projectDateService.updateById(projectDateEntity);
        List<ProjectUserEntity> projectUserEntityList = new ArrayList<>();
        if (StringUtils.isNotBlank(pjInfoVo.getSurveyPeople())){
            String[] split = pjInfoVo.getSurveyPeople().split("、");
            List<ProjectUserEntity> chargeList = projectUserService.getListByType(110,pjInfoVo.getId());
            if (StringUtils.isNotEmpty(chargeList)){
                List<Long> idList = chargeList.stream().map(ProjectUserEntity::getId).collect(Collectors.toList());
                projectUserService.removeByIds(idList);
            }
            for (String s:split){
                ProjectUserEntity projectUser = new ProjectUserEntity();
                SysUserEntity sysUserEntity = sysUserService.queryByUserName(s);
                projectUser.setProjectId(pjInfoVo.getId());
                projectUser.setUserId(sysUserEntity.getUserId());
                projectUser.setUsername(s);
                projectUser.setJobNum(sysUserEntity.getJobNum());
                projectUser.setTypes(110);
                projectUserEntityList.add(projectUser);
            }
        }

        if (StringUtils.isNotBlank(pjInfoVo.getProjectCharge())){
            String[] split = pjInfoVo.getProjectCharge().split("、");
            List<ProjectUserEntity> chargeList = projectUserService.getListByType(140,pjInfoVo.getId());
            if (StringUtils.isNotEmpty(chargeList)){
                List<Long> idList = chargeList.stream().map(ProjectUserEntity::getId).collect(Collectors.toList());
                projectUserService.removeByIds(idList);
            }
            for (String s:split){
                ProjectUserEntity projectUser = new ProjectUserEntity();
                SysUserEntity sysUserEntity = sysUserService.queryByUserName(s);
                projectUser.setProjectId(pjInfoVo.getId());
                projectUser.setUserId(sysUserEntity.getUserId());
                projectUser.setUsername(s);
                projectUser.setJobNum(sysUserEntity.getJobNum());
                projectUser.setTypes(140);
                projectUserEntityList.add(projectUser);
            }
        }
        if (StringUtils.isNotBlank(pjInfoVo.getReportPreparation())){
            String[] split = pjInfoVo.getReportPreparation().split("、");
            List<ProjectUserEntity> chargeList = projectUserService.getListByType(150,pjInfoVo.getId());
            if (StringUtils.isNotEmpty(chargeList)){
                List<Long> idList = chargeList.stream().map(ProjectUserEntity::getId).collect(Collectors.toList());
                projectUserService.removeByIds(idList);
            }
            for (String s:split){
                ProjectUserEntity projectUser = new ProjectUserEntity();
                SysUserEntity sysUserEntity = sysUserService.queryByUserName(s);
                projectUser.setProjectId(pjInfoVo.getId());
                projectUser.setUserId(sysUserEntity.getUserId());
                projectUser.setUsername(s);
                projectUser.setJobNum(sysUserEntity.getJobNum());
                projectUser.setTypes(150);
                projectUserEntityList.add(projectUser);
            }
        }
        if (StringUtils.isNotBlank(pjInfoVo.getSurveyAccompany())){
            String[] split = pjInfoVo.getSurveyAccompany().split("、");
            List<ProjectUserEntity> chargeList = projectUserService.getListByType(160,pjInfoVo.getId());
            if (StringUtils.isNotEmpty(chargeList)){
                List<Long> idList = chargeList.stream().map(ProjectUserEntity::getId).collect(Collectors.toList());
                projectUserService.removeByIds(idList);
            }
            for (String s:split){
                ProjectUserEntity projectUser = new ProjectUserEntity();
//                SysUserEntity sysUserEntity = sysUserService.queryByUserName(s);
                projectUser.setProjectId(pjInfoVo.getId());
//                projectUser.setUserId(sysUserEntity.getUserId());
                projectUser.setUsername(s);
//                projectUser.setJobNum(sysUserEntity.getJobNum());
                projectUser.setTypes(160);
                projectUserEntityList.add(projectUser);
            }
        }
        if (StringUtils.isNotBlank(pjInfoVo.getSampleAccompany())){
            String[] split = pjInfoVo.getSampleAccompany().split("、");
            List<ProjectUserEntity> chargeList = projectUserService.getListByType(170,pjInfoVo.getId());
            if (StringUtils.isNotEmpty(chargeList)){
                List<Long> idList = chargeList.stream().map(ProjectUserEntity::getId).collect(Collectors.toList());
                projectUserService.removeByIds(idList);
            }
            for (String s:split){
                ProjectUserEntity projectUser = new ProjectUserEntity();
//                SysUserEntity sysUserEntity = sysUserService.queryByUserName(s);
                projectUser.setProjectId(pjInfoVo.getId());
//                projectUser.setUserId(sysUserEntity.getUserId());
                projectUser.setUsername(s);
//                projectUser.setJobNum(sysUserEntity.getJobNum());
                projectUser.setTypes(170);
                projectUserEntityList.add(projectUser);
            }
        }
        if (StringUtils.isNotEmpty(projectUserEntityList)){
            projectUserService.saveOrUpdateBatch(projectUserEntityList);
        }
    }


    /**
     * 评价公示提交审核
     * @param pjInfoVo
     */
    public void tiJiaoPublicty(PublictyPjInfoVo pjInfoVo){
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = pjInfoVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        if ("预评".equals(project.getType())||"专篇".equals(project.getType()) ){
            project.setPubStatus(4);
//            project.setStatus(40);
            project.setPublicityLastTime(new Date());
            projectService.updateById(project);

            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            publicityInfo.setProjectId(projectId);
            publicityInfo.setStatus(4);
            publicityInfo.setUsername(sysUserEntity.getUsername());
            publicityInfo.setOperation("提交审核");
            publicityInfo.setOperationTime(new Date());
            publicityInfoService.save(publicityInfo);
        }else {
            project.setPubStatus(2);
//            project.setStatus(40);
            project.setPublicityLastTime(new Date());
            projectService.updateById(project);
            PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
            publicityInfo.setProjectId(projectId);
            publicityInfo.setStatus(2);
            publicityInfo.setUsername(sysUserEntity.getUsername());
            publicityInfo.setOperation("提交审核");
            publicityInfo.setOperationTime(new Date());
            publicityInfoService.save(publicityInfo);
        }
    }

    /**
     * 评价公示撤销审核
     * @param pjInfoVo
     */
    public void cheXiaoPublicty(PublictyPjInfoVo pjInfoVo){
        if (pjInfoVo.getPubStatus()>=6){
            new RRException("质控已通过审核无法撤销审核！");
        }
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long projectId = pjInfoVo.getId();
        ProjectEntity project = projectService.getById(projectId);
        project.setPubStatus(1);
//        projectProceduresService.deleteByProjectIdAndType(projectId,40);
//        ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresLast(projectId);
//        if (proceduresEntity!=null){
//            project.setStatus(proceduresEntity.getStatus());
//        }else {
//            project.setStatus(35);
//            ProjectProceduresEntity proceduresEntity2 = new ProjectProceduresEntity();
//            proceduresEntity2.setProjectId(projectId);
//            proceduresEntity2.setStatus(35);
//            projectProceduresService.save(proceduresEntity2);
//        }
        project.setPublicityLastTime(new Date());
        projectService.updateById(project);
//        if ("集团发展".equals(project.getBusinessSource())){
//            if (null!=project.getSalesmenid()){
//                AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
//                abuSendNoteDTO.setProjectId(project.getId());
//                abuSendNoteDTO.setIdentifier(project.getIdentifier());
//                abuSendNoteDTO.setCompany(project.getCompany());
//                abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
//                abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
//                abuSendNoteDTO.setSalesman(project.getSalesmen());
//                abuSendNoteDTO.setStatus(project.getStatus());
//                iAbuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
//            }
//        }
        PublicityInfoEntity publicityInfo = new PublicityInfoEntity();
        publicityInfo.setProjectId(projectId);
        publicityInfo.setStatus(1);
        publicityInfo.setUsername(sysUserEntity.getUsername());
        publicityInfo.setOperation("撤销审核");
        publicityInfo.setOperationTime(new Date());
        publicityInfoService.save(publicityInfo);
    }

    /**
     * 主管公示项目列表
     * @param publicPjDto
     * @return
     */
    public List<PublicPjPageVo> getPjHeadPublicList(PublicPjDto publicPjDto){
        String identifier = publicPjDto.getIdentifier();
        String type = publicPjDto.getType();
        String company = publicPjDto.getCompany();
        Integer pubStatus = publicPjDto.getPubStatus();
        Integer bindingStatus = publicPjDto.getBindingStatus();
        Integer examineStatus = publicPjDto.getExamineStatus();

        // 隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();

        QueryWrapper<Object> queryWrapper = new QueryWrapper();
        queryWrapper.in("p.type","预评","专篇","控评","现状");
        queryWrapper.eq(StrUtil.isNotBlank(subjection), "u.subjection", subjection);
        queryWrapper.like(StringUtils.isNotBlank(identifier),"p.identifier",identifier);
        queryWrapper.like(StringUtils.isNotBlank(type),"p.type",type);
        queryWrapper.like(StringUtils.isNotBlank(company),"p.company",company);
        queryWrapper.ge("p.pub_status",2);
        if (StringUtils.isNotEmpty(pubStatus)||StringUtils.isNotEmpty(bindingStatus)||StringUtils.isNotEmpty(examineStatus)){
            queryWrapper.and(StringUtils.isNotEmpty(pubStatus)||StringUtils.isNotEmpty(bindingStatus)||StringUtils.isNotEmpty(examineStatus),
                    i -> i.eq(StringUtils.isNotEmpty(pubStatus),"p.pub_status",pubStatus).or().eq(StringUtils.isNotEmpty(examineStatus),"p.pub_status",pubStatus).or().eq(StringUtils.isNotEmpty(bindingStatus),"p.binding_status",bindingStatus));
        }
//        QueryWrapper<Object> authWapper = queryWrapperByParamsAuthPj(queryWrapper);

        pageUtil2.startPage();
        List<PublicPjPageVo> publicPjPageVoList = publicityMapper.getPjPublicList(queryWrapper);
        List<Long> projectIdList = publicPjPageVoList.stream().map(PublicPjPageVo::getId).collect(Collectors.toList());
        Map<Long,List<PublicityInfoEntity>> publicityTiJiaoMap = new HashMap<>();
        Map<Long,List<PublicityInfoEntity>> publicityTongGuoMap = new HashMap<>();
        if (StringUtils.isNotEmpty(projectIdList)){
            List<PublicityInfoEntity> publicityTiJiaoList = publicityInfoService.getLastLimitList(projectIdList,2);
            publicityTiJiaoMap = publicityTiJiaoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));
            List<PublicityInfoEntity> publicityTongGuoList = publicityInfoService.getLastLimitList(projectIdList,6);
            publicityTongGuoMap = publicityTongGuoList.stream().collect(Collectors.groupingBy(PublicityInfoEntity::getProjectId));
        }

        for (PublicPjPageVo publicPjPageVo:publicPjPageVoList){
            Long id = publicPjPageVo.getId();
            publicPjPageVo.setRenderDate(StringUtils.isNotEmpty(publicityTiJiaoMap.get(id))?publicityTiJiaoMap.get(id).get(0).getOperationTime():null);
            publicPjPageVo.setPassDate(StringUtils.isNotEmpty(publicityTongGuoMap.get(id))?publicityTongGuoMap.get(id).get(0).getOperationTime():null);
            if (publicPjPageVo.getPubStatus() <= 6){
                publicPjPageVo.setExamineStatus(publicPjPageVo.getPubStatus());
                publicPjPageVo.setPubStatus(6);
            }else {
                publicPjPageVo.setPubStatus(publicPjPageVo.getPubStatus());
                publicPjPageVo.setExamineStatus(6);
            }
        }
        return publicPjPageVoList;
    }

    /**
     * 评价生成PDF
     * @param httpRequest
     * @param pjInfoVo
     * @return
     */
    public String generatePjPdf(HttpServletRequest httpRequest,PublictyPjInfoVo pjInfoVo){
        List<EvalImageLibraryEntity> imgList = pjInfoVo.getImgList();
        if (StringUtils.isNotEmpty(imgList)){
            new RRException("采集影像为空无法生成pdf");
        }
        Map<String,Object> pdfMap = BeanUtil.beanToMap(pjInfoVo);
//        System.out.println("pdfMap1111 = " + pdfMap);
        if (pjInfoVo.getReportCoverDate()!=null){
            String format = DateUtil.format(pjInfoVo.getReportCoverDate(), "yyyy-MM-dd");
            pdfMap.put("reportCoverDate",format);
        }
        if (pjInfoVo.getReportIssue()!=null){
            String format = DateUtil.format(pjInfoVo.getReportIssue(), "yyyy-MM-dd");
            pdfMap.put("reportIssue",format);
        }
//        System.out.println("pdfMap2222 = " + pdfMap);
        Map<String,List<EvalImageLibraryEntity>> listMap = imgList.stream().collect(Collectors.groupingBy(EvalImageLibraryEntity::getPoint));
        for (String key:listMap.keySet()){
            EvalImageLibraryEntity entity = listMap.get(key).get(0);
            if (entity.getCategory()==1&&entity.getType()==0){
                pdfMap.put("现场调查过程",entity.getUrl());
            }else if (entity.getCategory()==2&&entity.getType()==0){
                if (pdfMap.keySet().contains("现场采样过程1")){
                    pdfMap.put("现场采样过程2",entity.getUrl());
                }else {
                    pdfMap.put("现场采样过程1",entity.getUrl());
                }
            }else if (entity.getCategory()==2&&entity.getType()==1){
                pdfMap.put("厂区大门",entity.getUrl());
            }
        }
        JSONObject josmmap = JSONUtil.parseObj(pdfMap);
//        System.out.println("josmmap = " + josmmap);
        //TODO 获取PythonApi
        String apiPath = pythonPjApiPath;
        System.out.println("apiPath = " + apiPath);

        String object = HttpRequest.post(apiPath+"v2/publicity/generate_file/pdf")
                .header("Content-Type", "application/json")
                .header("token",httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().body();

//        System.out.println(object);
//        String res = StringEscapeUtils.unescapeJava(object);
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(object);
        }catch (Exception e){
            new RRException("python接口返回值异常",500);
        }
        System.out.println("jsonObject = " + jsonObject);
        String str = null;
        if (!(jsonObject == null)){
            Object o = jsonObject.get("data");
            Object o1 = jsonObject.get("code");
            if (!(o1 != null && o1.toString().equals("200"))) {
                log.error("项目公示生成pdf" + object);
                new RRException(object,500);
            } else {
                str = o.toString();
            }
        }else {
            new RRException("python接口返回值异常",500);
        }
        return str;
    }

    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     *
     * @param
     * @return
     */
    public QueryWrapper<Object> queryWrapperByParamsAuthPj(QueryWrapper<Object> wappr) {
        // 隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 公司旗下所有部门及其子部门
        QueryWrapper<SysDeptEntity> deptWrapper = new QueryWrapper<>();
        deptWrapper.eq("name", subjection);
        Long subjectionDeptId = deptDao.selectOne(deptWrapper).getDeptId();
        QueryWrapper<SysDeptEntity> childrenDeptWrapper = new QueryWrapper<>();
        String deptStructure = "," + subjectionDeptId + ",";
        childrenDeptWrapper.like("dept_structure", deptStructure);
        List<Long> deptList = deptDao.selectList(childrenDeptWrapper).stream().distinct().map(SysDeptEntity::getDeptId).collect(Collectors.toList());
        deptList.add(subjectionDeptId);
        List<String> projectTypeNames = new ArrayList<>();
        projectTypeNames.add("预评");
        projectTypeNames.add("专篇");
        projectTypeNames.add("控评");
        projectTypeNames.add("现状");
        // 数据权限控制
        QueryWrapper<Object> queryWrapper = wappr
                .and(i->i.in("p.dept_id", deptList).or().eq("p.company_order",subjection))
                // 部门权限控制
                //隶属公司
                // 项目类型权限控制
                .in("p.type", projectTypeNames);

        return queryWrapper;
    }

    /**
     * 用于发送胶状公示成功的信息
     */
    public void sendSuccessfulMessage(Long projectId){
        //2023/08/07 项目公示后 发送消息
        ProjectEntity project = projectService.getById(projectId);
        if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
            MessageEntity entity = new MessageEntity();
            entity.setTitle("公示成功提醒");
            entity.setContent("【" + project.getIdentifier() + "+" + project.getCompany() + "】已公示。");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
        }
    }
}
