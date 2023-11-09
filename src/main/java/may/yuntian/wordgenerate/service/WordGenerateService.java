package may.yuntian.wordgenerate.service;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.entity.CompanySurveyEntity;
import may.yuntian.anlian.service.CompanyService;
import may.yuntian.anlian.service.CompanySurveyService;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.wanda.util.AnnexUtil;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.wordgenerate.constant.GeneratePathConstants;
import may.yuntian.wordgenerate.entity.WordGeneratePathEntity;
import may.yuntian.wordgenerate.entity.WordTaskSubstancesEntity;
import may.yuntian.wordgenerate.mapper.WordGenerateMapper;
import may.yuntian.wordgenerate.mongoservice.EvalPlanRecordService;
import may.yuntian.wordgenerate.vo.AgreementGenerateVo;
import may.yuntian.wordgenerate.entity.WordContractTypeEntity;
import may.yuntian.wordgenerate.vo.GenerateWordReturnVo;
import may.yuntian.wordgenerate.vo.TaskGenerateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.*;

/**
 * 用于生成word文档
 */
@Slf4j
@Service
public class WordGenerateService {

    @Autowired
    private WordGenerateMapper wordGenerateMapper;
    @Autowired
    private WordTaskSubstancesService wordTaskSubstancesService;
    @Autowired
    private WordGeneratePathService wordGeneratePathService;
    @Autowired
    private EvalPlanRecordService evalPlanRecordService;
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private WordContractTypeService wordContractTypeService;


    private final static String[] TASK_PJ_TYPES = new String[]{"预评","专篇","控评","现状","放射预评价","放射预评价","放射设计专篇","放射现状评价","放射检测"};
//    private final static String[] ZJ_TYPES = new String[]{};
    private final static String[] TASK_HW_TYPES = new String[]{"环境监测","环境验收","排污许可","排污许可","清洁生产审核","场地调查",
        "环评","公共卫生检测","公共卫生学评价","一次性用品用具检测","洁净区域检测","学校卫生检测","公卫监督","环境监督",
        "应急预案","公卫示范","环境示范","环保管家"};

    private final static String[] PROTOCOL_FS_TYPES = new String[]{"放射预评价","放射预评价","放射设计专篇","放射现状评价","放射检测"};
    private final static String[] PROTOCOL_HW_LY_TYPES = new String[]{"环境监测","环境验收","排污许可","排污许可","清洁生产审核","场地调查",
            "环评","公共卫生检测","公共卫生学评价","一次性用品用具检测","洁净区域检测","学校卫生检测","公卫监督","环境监督",
            "应急预案","公卫示范","环境示范","环保管家","来样检测"};



    /**
     * 合同协议生成路径及信息处理
     * @param ip
     * @param id  合同ID
     * @param wrodType  模板文件名称
     * @return
     */
    private GenerateWordReturnVo contractFilePath(InetAddress ip,Long id,String wrodType){
        String temFilePath = "";
        String generateFilePath = "";
        String generateFileName = "";
//        Map<String,Object> returnMap = new HashMap<>();
        GenerateWordReturnVo generateWordReturnVo = new GenerateWordReturnVo();
        AgreementGenerateVo agreementGenerateVo = wordGenerateMapper.getContractInfo(id);

//        System.out.println("ip======"+ip);
//        if (ip!=null){
//            System.out.println("ip=HostName====="+ip.getHostName());
//            System.out.println("ip=HostAddress====="+ip.getHostAddress());
//            System.out.println("ip=Address====="+ip.getAddress());
//        }

        if (StringUtils.isNotEmpty(agreementGenerateVo)){
//            if (ip != null && (ip.getHostAddress().equals("47.111.249.220")||ip.getHostAddress().equals("47.114.182.181")||ip.getHostAddress().equals("192.168.0.203")||ip.getHostAddress().equals("127.0.0.1"))){
//            if (ip != null && (ip.getHostAddress().equals("39.185.236.201")||ip.getHostAddress().equals("192.168.0.203")||ip.getHostAddress().equals("127.0.0.1"))){
//                temFilePath = GeneratePathConstants.TEMP_PATH_ONLINE + "contractAgreement/" + wrodType;
//                generateFilePath = GeneratePathConstants.FILE_PATH_ONLINE + agreementGenerateVo.getIdentifier() + File.separator;
//            }else {
//                temFilePath = GeneratePathConstants.TEMP_PATH_OFFLINE + "contractAgreement\\" + wrodType;
//                generateFilePath = GeneratePathConstants.FILE_PATH_OFFLINE + agreementGenerateVo.getIdentifier() + File.separator;
//            }
//            String systemPath = WordGenerateService.class.getClassLoader().getResource("").getPath();
            temFilePath = "wordtemplate/contractAgreement/" + wrodType;
            generateFilePath = "wordtemplate";

            generateFileName = agreementGenerateVo.getIdentifier() + agreementGenerateVo.getType() + "服务合同.docx";
            if (!agreementGenerateVo.getCompanyId().equals(agreementGenerateVo.getEntrustCompanyId())
                    &&agreementGenerateVo.getCompanyId()!=null&&agreementGenerateVo.getEntrustCompanyId()!=null){
                CompanyEntity company = companyService.getById(agreementGenerateVo.getCompanyId());
                if (company!=null){
                    agreementGenerateVo.setCode(company.getCode());
                    agreementGenerateVo.setLegalname(company.getLegalname());
                    agreementGenerateVo.setContactPerson(company.getContact());
                    agreementGenerateVo.setTestPhone(company.getMobile());
                }
            }
            String moneyStr = Number2Money.format(agreementGenerateVo.getTotalMoney().toString());
            agreementGenerateVo.setTotalMoneyStr(moneyStr);

        }else {
            agreementGenerateVo = new AgreementGenerateVo();
        }
        generateWordReturnVo.setTemFilePath(temFilePath);
        generateWordReturnVo.setGenerateFilePath(generateFilePath);
        generateWordReturnVo.setGenerateFileName(generateFileName);
        generateWordReturnVo.setAgreementGenerateVo(agreementGenerateVo);
        return generateWordReturnVo;
    }

    /**
     * 根据类型生成合同word
     * @param id 合同ID
     * @param wrodType jp/**   pj/**  hw/**  fs/**     **为word合同模板名称
     */
    public void generateContract(Long id,String wrodType){
        String temFilePath = "";
        String generateFilePath = "";
        String generateFileName = "";
        AgreementGenerateVo agreementGenerateVo = new AgreementGenerateVo();
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        }catch (Exception e){
            e.printStackTrace();
        }
        GenerateWordReturnVo returnMap = contractFilePath(ip,id,wrodType);
        temFilePath = returnMap.getTemFilePath();
        generateFilePath = returnMap.getGenerateFilePath();
        generateFileName = returnMap.getGenerateFileName();
        agreementGenerateVo = returnMap.getAgreementGenerateVo();
        if (StringUtils.isEmpty(agreementGenerateVo)){
            new RRException("请确认该项目是否存在后再试！");
        }else if (!StringUtils.isNotBlank(generateFileName)){
            new RRException("请传入正确的要生成的文件类型！");
        }
        //创建行循环策略
        LoopRowTableRenderPolicy rowTableRenderPolicy = new LoopRowTableRenderPolicy();
        //告诉模板引擎，要在employees做行循环,绑定行循环策略
        Configure configure = Configure.builder().bind("substanceList", rowTableRenderPolicy).build();
//            System.out.println("list = " + list);
        //将Docx文档写入文件
//
        FileOutputStream fileOutputStream = null;
        String minioPath = "";
        File exportWord = new File( FileUtils.fileRename()+".docx");
        try {
            Resource resource = new ClassPathResource(temFilePath);
            InputStream inputStream = resource.getInputStream();

            fileOutputStream = new FileOutputStream(exportWord);
            XWPFTemplate.compile(inputStream,configure).render(agreementGenerateVo).writeAndClose(fileOutputStream);
            MultipartFile multipartFile = AnnexUtil.fileToMultipartFile(exportWord);
            String type = "contract/"+agreementGenerateVo.getIdentifier();
            minioPath = MinioUtil.upload2(multipartFile, type, generateFileName);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (exportWord != null) {
                exportWord.delete(); // 删除临时文件
            }
        }

        WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(id,3);
        if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
//            if (StringUtils.isNotBlank(wordGeneratePathEntity.getPath())){
//                MinioUtil.remove(wordGeneratePathEntity.getPath());
//            }
            wordGeneratePathEntity.setPath(minioPath);
            wordGeneratePathEntity.setGenerateTime(new Date());
            wordGeneratePathService.updateById(wordGeneratePathEntity);
        }else {
            wordGeneratePathEntity = new WordGeneratePathEntity();
            wordGeneratePathEntity.setProjectId(id);
            wordGeneratePathEntity.setGenerateTime(new Date());
            wordGeneratePathEntity.setPath(minioPath);
            wordGeneratePathEntity.setOther("");
            wordGeneratePathEntity.setTimeLimit("");
            wordGeneratePathEntity.setType(3);
            wordGeneratePathService.save(wordGeneratePathEntity);
        }

    }


    /**
     * 获取所有合同模板
     * @return
     */
    public List<WordContractTypeEntity> getWordType(){
        return wordContractTypeService.getList();
    }


    /**
     * 生成任务单处理过程提取
     * @param ip
     * @param projectId
     * @return
     */
    private GenerateWordReturnVo getPath(InetAddress ip,Long projectId,String wordType){
//        Map<String,Object> pathMap = new HashMap<>();
        GenerateWordReturnVo generateWordReturnVo = new GenerateWordReturnVo();
        String temFilePath = "";
        String generateFilePath = "";
        String generateFileName = "";
        TaskGenerateVo taskGenerateVo = wordGenerateMapper.getGenerateInfo(projectId);
        if (StringUtils.isNotEmpty(taskGenerateVo)){
//            if (ip != null && (ip.getHostAddress().equals("47.111.249.220")||ip.getHostAddress().equals("47.114.182.181")||ip.getHostAddress().equals("192.168.0.203")||ip.getHostAddress().equals("127.0.0.1"))){
//                temFilePath = GeneratePathConstants.TEMP_PATH_ONLINE;
//                generateFilePath = GeneratePathConstants.FILE_PATH_ONLINE + taskGenerateVo.getIdentifier() + File.separator;
//            }else {
//                temFilePath = GeneratePathConstants.TEMP_PATH_OFFLINE;
//                generateFilePath = GeneratePathConstants.FILE_PATH_OFFLINE + taskGenerateVo.getIdentifier() + File.separator;
//            }

            temFilePath = "wordtemplate/";
            generateFilePath = "wordtemplate";
            WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(projectId,1);
            if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
                taskGenerateVo.setTimeLimit(wordGeneratePathEntity.getTimeLimit());
                taskGenerateVo.setOther(wordGeneratePathEntity.getOther());
            }else {
                taskGenerateVo.setTimeLimit("");
                taskGenerateVo.setOther("");
            }
            if (GeneratePathConstants.TYPE_ONE.equals(wordType)){
                if (Arrays.asList(TASK_PJ_TYPES).contains(taskGenerateVo.getType())){
                    temFilePath = temFilePath + GeneratePathConstants.TEMP_NAME_TWO;
                    String typeStr = "";
                    switch (taskGenerateVo.getType()){
                        case "预评":
                            typeStr = "预评√      控评□      现状□     设计专篇□";
                            break;
                        case "专篇":
                            typeStr = "预评□      控评□      现状□     设计专篇√";
                            break;
                        case "控评":
                            typeStr = "预评□      控评√      现状□     设计专篇□";
                            break;
                        case "现状":
                            typeStr = "预评□      控评□      现状√     设计专篇□";
                            break;
                        default:
                            typeStr = "预评□      控评□      现状□     设计专篇□";
                            break;
                    }
                    taskGenerateVo.setTypeStr(typeStr);
                    taskGenerateVo.setShowType("pj");
                }else if (Arrays.asList(TASK_HW_TYPES).contains(taskGenerateVo.getType())){
                    temFilePath = temFilePath + GeneratePathConstants.TEMP_NAME_THR;
                    List<WordTaskSubstancesEntity> list = wordTaskSubstancesService.getListByProjectId(projectId);
                    taskGenerateVo.setSubstanceList(list);
                    taskGenerateVo.setShowType("hw");
                }else {
                    temFilePath = temFilePath + GeneratePathConstants.TEMP_NAME_ONE;
                    taskGenerateVo.setShowType("jp");
                }
                generateFileName = taskGenerateVo.getIdentifier() + "-任务单.docx";
            }else if (GeneratePathConstants.TYPE_TWO.equals(wordType)){
                temFilePath = temFilePath + GeneratePathConstants.TEMP_NAME_FOU;
                if (Arrays.asList(TASK_PJ_TYPES).contains(taskGenerateVo.getType())){
                    String substances = evalPlanRecordService.getSubstances(projectId);
                    taskGenerateVo.setSubstanceString(substances);
                }else if (Arrays.asList(TASK_HW_TYPES).contains(taskGenerateVo.getType())){
                    taskGenerateVo.setSubstanceString("");
                }else {
                    String substances;
                    CompanySurveyEntity companySurvey = companySurveyService.seleteByProjectId(projectId);
                    if (StringUtils.isNotEmpty(companySurvey)){
                        substances = companySurvey.getTestItems();
                    }else {
                        substances = "";
                    }

                    taskGenerateVo.setSubstanceString(substances);
                }
                generateFileName = taskGenerateVo.getIdentifier() + "-合同评审单.docx";
            }else if (GeneratePathConstants.TYPE_THR.equals(wordType)){
                if (taskGenerateVo.getEntrustCompanyId()!=null&&taskGenerateVo.getCompanyId()!=null){
                    if (taskGenerateVo.getEntrustCompanyId().equals(taskGenerateVo.getCompanyId())){
                        taskGenerateVo.setLink(taskGenerateVo.getContact());
                        taskGenerateVo.setPhone(taskGenerateVo.getTelephone());
                    }else {
                        CompanyEntity company = companyService.getById(taskGenerateVo.getCompanyId());
                        taskGenerateVo.setLink(company.getContact());
                        taskGenerateVo.setPhone(company.getTelephone());
                    }
                }
                if (Arrays.asList(PROTOCOL_FS_TYPES).contains(taskGenerateVo.getType())){
                    temFilePath = temFilePath + GeneratePathConstants.PROTOCOL_NAME_ONE;
                }else if (Arrays.asList(PROTOCOL_HW_LY_TYPES).contains(taskGenerateVo.getType())){
                    temFilePath = temFilePath + GeneratePathConstants.PROTOCOL_NAME_TWO;
                }else {
                    temFilePath = temFilePath + GeneratePathConstants.PROTOCOL_NAME_THR;
                    if ("检评".equals(taskGenerateVo.getType())){
                        String projectName = taskGenerateVo.getCompany()+"工作场所职业病危害因素检测";
                        taskGenerateVo.setProjectName(projectName);
                    }else {
                        taskGenerateVo.setProjectName("");
                    }
                }
                generateFileName = taskGenerateVo.getIdentifier() + "-委托协议.docx";
            }

            generateWordReturnVo.setTemFilePath(temFilePath);
            generateWordReturnVo.setGenerateFileName(generateFileName);
            generateWordReturnVo.setGenerateFilePath(generateFilePath);
            generateWordReturnVo.setTaskGenerateVo(taskGenerateVo);
        }else {
            generateWordReturnVo.setTaskGenerateVo(new TaskGenerateVo());
        }

        return generateWordReturnVo;
    }

    /**
     * 预览任务单
     * @param projectId
     * @return
     */
    public TaskGenerateVo previewTask(Long projectId){
        GenerateWordReturnVo pathMap = getPath(null,projectId,"task");
        TaskGenerateVo taskGenerateVo = pathMap.getTaskGenerateVo();
        return taskGenerateVo;
    }

    /**
     * 评价任务单缺失数据保存
     * @param taskGenerateVo
     */
    public void pjSaveTask(TaskGenerateVo taskGenerateVo){
        WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(taskGenerateVo.getId(),1);
        if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
            wordGeneratePathEntity.setTimeLimit(taskGenerateVo.getTimeLimit());
            wordGeneratePathEntity.setOther(taskGenerateVo.getOther());
            wordGeneratePathService.updateById(wordGeneratePathEntity);
        }else {
            wordGeneratePathEntity = new WordGeneratePathEntity();
            wordGeneratePathEntity.setProjectId(taskGenerateVo.getId());
            wordGeneratePathEntity.setTimeLimit(taskGenerateVo.getTimeLimit());
            wordGeneratePathEntity.setOther(taskGenerateVo.getOther());
            wordGeneratePathEntity.setType(1);
            wordGeneratePathService.save(wordGeneratePathEntity);
        }
    }


    /**
     * 生成任务单或评审单
     * task 任务单
     * review 评审单
     * @param projectId
     */
    public void taskGenerate(Long projectId,String wordType){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        }catch (Exception e){
            e.printStackTrace();
        }
        String temFilePath = "";
        String generateFilePath = "";
        String generateFileName = "";
        TaskGenerateVo taskGenerateVo = new TaskGenerateVo();

        GenerateWordReturnVo pathMap = getPath(ip,projectId,wordType);
        temFilePath = pathMap.getTemFilePath();
        generateFilePath = pathMap.getGenerateFilePath();
        generateFileName = pathMap.getGenerateFileName();
        taskGenerateVo = pathMap.getTaskGenerateVo();
        System.out.println("taskGenerateVo="+taskGenerateVo);
        if (StringUtils.isEmpty(taskGenerateVo)){
            new RRException("请确认该项目是否存在后再试！");
        }else if (!StringUtils.isNotBlank(generateFileName)){
            new RRException("请传入正确的要生成的文件类型！");
        }

        System.out.println("temFilePath="+temFilePath+",generateFilePath="+generateFilePath+",generateFileName="+generateFileName);

        //创建行循环策略
        LoopRowTableRenderPolicy rowTableRenderPolicy = new LoopRowTableRenderPolicy();
        //告诉模板引擎，要在employees做行循环,绑定行循环策略
        Configure configure = Configure.builder().bind("substanceList", rowTableRenderPolicy).build();
//            System.out.println("list = " + list);
        //将Docx文档写入文件
        FileOutputStream fileOutputStream = null;
        String minioPath = "";
        File exportWord = new File( FileUtils.fileRename()+".docx");
        try {
            Resource resource = new ClassPathResource(temFilePath);
            InputStream inputStream = resource.getInputStream();

            fileOutputStream = new FileOutputStream(exportWord);
            XWPFTemplate.compile(inputStream,configure).render(taskGenerateVo).writeAndClose(fileOutputStream);
            MultipartFile multipartFile = AnnexUtil.fileToMultipartFile(exportWord);
            String type = "contract/"+taskGenerateVo.getIdentifier();
            minioPath = MinioUtil.upload2(multipartFile, type, generateFileName);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (exportWord != null) {
                exportWord.delete(); // 删除临时文件
            }
        }

        if (GeneratePathConstants.TYPE_ONE.equals(wordType)){
            WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(projectId,1);
            if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathService.updateById(wordGeneratePathEntity);
            }else {
                wordGeneratePathEntity = new WordGeneratePathEntity();
                wordGeneratePathEntity.setProjectId(projectId);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setOther("");
                wordGeneratePathEntity.setTimeLimit("");
                wordGeneratePathEntity.setType(1);
                wordGeneratePathService.save(wordGeneratePathEntity);
            }
        }else if (GeneratePathConstants.TYPE_TWO.equals(wordType)){
            WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(projectId,2);
            if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathService.updateById(wordGeneratePathEntity);
            }else {
                wordGeneratePathEntity = new WordGeneratePathEntity();
                wordGeneratePathEntity.setProjectId(projectId);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setOther("");
                wordGeneratePathEntity.setTimeLimit("");
                wordGeneratePathEntity.setType(2);
                wordGeneratePathService.save(wordGeneratePathEntity);
            }
        }else if (GeneratePathConstants.TYPE_THR.equals(wordType)){
            WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(projectId,4);
            if (StringUtils.isNotEmpty(wordGeneratePathEntity)){
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathService.updateById(wordGeneratePathEntity);
            }else {
                wordGeneratePathEntity = new WordGeneratePathEntity();
                wordGeneratePathEntity.setProjectId(projectId);
                wordGeneratePathEntity.setGenerateTime(new Date());
                wordGeneratePathEntity.setPath(minioPath);
                wordGeneratePathEntity.setOther("");
                wordGeneratePathEntity.setTimeLimit("");
                wordGeneratePathEntity.setType(4);
                wordGeneratePathService.save(wordGeneratePathEntity);
            }
        }


    }

    /**
     * 下载word
     * @param projectId
     * @param type
     * @param response
     * @return
     */
    public void downLoadFile(Long projectId,Integer type,HttpServletResponse response){
        WordGeneratePathEntity wordGeneratePathEntity = wordGeneratePathService.getByProjectId(projectId,type);
        if (StringUtils.isNotEmpty(wordGeneratePathEntity)&&StringUtils.isNotEmpty(wordGeneratePathEntity.getPath())){
//            FileUtils.downloadWord(wordGeneratePathEntity.getPath(), response);
            String objName = wordGeneratePathEntity.getPath().substring(wordGeneratePathEntity.getPath().lastIndexOf("anlian-java" + "/") + "anlian-java".length() + 1);
            String name = wordGeneratePathEntity.getPath().substring(wordGeneratePathEntity.getPath().lastIndexOf( "/")+1);
            MinioUtil.download2(objName,response,name);
//            return response;
        }else {
            new RRException("未生成文件，无法下载！请生成后再下载！");
//            return null;
        }
    }




    /** 根据路径和文件名 创建文件对象*/
    private File createFile(String filePath,String fileName){
        //pdf目录对象
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        //pdf文件对象
        StringBuffer filePathBuffer = new StringBuffer();
        filePathBuffer.append(filePath).append(fileName);
        return new File(filePathBuffer.toString());
    }
}
