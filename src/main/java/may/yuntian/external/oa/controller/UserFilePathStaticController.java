//package may.yuntian.external.oa.controller;
//
//import may.yuntian.anlian.entity.*;
//import may.yuntian.anlian.service.*;
//import may.yuntian.anlian.utils.StringUtils;
//import may.yuntian.external.oa.service.ProjectQueryService;
//import may.yuntian.external.oa.vo.ProjectAllQueryVo;
//import may.yuntian.external.oa.vo.ProjectQueryVo;
//import may.yuntian.minio.utils.MinioUtil;
//import may.yuntian.untils.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @author mi
// */
//@RestController
//public class UserFilePathStaticController {
//
//    @Autowired
//    private PersonHonorCrtificateService personHonorCrtificateService;
//    @Autowired
//    private PersonBasicFilesService personBasicFilesService;
//    @Autowired
//    private PersonSupervisionRecordsService personSupervisionRecordsService;
//    @Autowired
//    private PersonTechnicalCertificateService personTechnicalCertificateService;
//    @Autowired
//    private PersonTrainService personTrainService;
//    @Value("${anlian.path.staticPath}")
//    private String anlian;
//
//    /**
//     * 项目编号+受检单位筛选我的项目
//     *
//     * @return
//     */
//    @PostMapping("/personBasicFiles")
//    public Result personBasicFiles() {
//        List<PersonBasicFilesEntity> personBasicFilesList = personBasicFilesService.list();
//        for (PersonBasicFilesEntity entity:personBasicFilesList){
//            if (StringUtils.isNotBlank(entity.getPath())){
//                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
//                List<String> urls = new ArrayList<>();
//                for (String s:paths) {
//                    System.out.println("s = " + s);
//                    if (StringUtils.isNotBlank(s)) {
//                        MultipartFile file = MinioUtil.createMfileByPath(anlian + s);
//                        String type = "user/" + entity.getUserId() + "/personBasicFiles";
//                        if (null != file) {
//                            String url = MinioUtil.upload(file, type);
//                            urls.add(url);
//                        } else {
//                            urls.add("---");
//                        }
//                    }
//                }
//                entity.setMinioPath(urls.stream().collect(Collectors.joining(",")));
//                personBasicFilesService.updateById(entity);
//            }
//        }
//
//        return Result.ok();
//    }
//
//
//    @PostMapping("/personHonorCrtificate")
//    public Result personHonorCrtificate() {
//        List<PersonHonorCrtificateEntity> personHonorCrtificateList = personHonorCrtificateService.list();
//        for (PersonHonorCrtificateEntity entity:personHonorCrtificateList){
//            if (StringUtils.isNotBlank(entity.getPath())){
//                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
//                List<String> urls = new ArrayList<>();
//                for (String s:paths) {
//                    System.out.println("s = " + s);
//                    if (StringUtils.isNotBlank(s)) {
//                        MultipartFile file = MinioUtil.createMfileByPath(anlian + s);
//                        String type = "user/"+entity.getUserId()+"/personHonorCrtificate";
//                        if (null != file) {
//                            String url = MinioUtil.upload(file, type);
//                            urls.add(url);
//                        } else {
//                            urls.add("---");
//                        }
//                    }
//                }
//                entity.setMinioPath(urls.stream().collect(Collectors.joining(",")));
//                personHonorCrtificateService.updateById(entity);
//            }
//        }
//        return Result.ok();
//    }
//    @PostMapping("/personSupervisionRecords")
//    public Result personSupervisionRecords() {
//        List<PersonSupervisionRecordsEntity> personSupervisionRecordsList = personSupervisionRecordsService.list();
//        for (PersonSupervisionRecordsEntity entity:personSupervisionRecordsList){
//            if (StringUtils.isNotBlank(entity.getPath())){
//                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
//                List<String> urls = new ArrayList<>();
//                for (String s:paths) {
//                    System.out.println("s = " + s);
//                    if (StringUtils.isNotBlank(s)) {
//                        MultipartFile file = MinioUtil.createMfileByPath(anlian + s);
//                        String type = "user/"+entity.getUserId()+"/personSupervisionRecords";
//                        if (null != file) {
//                            String url = MinioUtil.upload(file, type);
//                            urls.add(url);
//                        } else {
//                            urls.add("---");
//                        }
//                    }
//                }
//                entity.setMinioPath(urls.stream().collect(Collectors.joining(",")));
//                personSupervisionRecordsService.updateById(entity);
//            }
//        }
//        return Result.ok();
//    }
//    @PostMapping("/personTechnicalCertificate")
//    public Result personTechnicalCertificate() {
//        List<PersonTechnicalCertificateEntity> personTechnicalCertificate = personTechnicalCertificateService.list();
//        for (PersonTechnicalCertificateEntity entity:personTechnicalCertificate){
//            if (StringUtils.isNotBlank(entity.getPath())&&entity.getPath().contains("uploadFile")){
//                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
//                List<String> urls = new ArrayList<>();
//                for (String s:paths) {
//                    System.out.println("s = " + s);
//                    if (StringUtils.isNotBlank(s)) {
//                        MultipartFile file = MinioUtil.createMfileByPath(anlian + s);
//                        String type = "user/"+entity.getUserId()+"/personTechnicalCertificate";
//                        if (null != file) {
//                            String url = MinioUtil.upload(file, type);
//                            urls.add(url);
//                        } else {
//                            urls.add("---");
//                        }
//                    }
//                }
//                entity.setMinioPath(urls.stream().collect(Collectors.joining(",")));
//                personTechnicalCertificateService.updateById(entity);
//            }
//        }
//        return Result.ok();
//    }
//    @PostMapping("/personTrain")
//    public Result personTrain() {
//        List<PersonTrainEntity> personTrainList = personTrainService.list();
//        for (PersonTrainEntity entity:personTrainList){
//            if (StringUtils.isNotBlank(entity.getPath())){
//                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
//                List<String> urls = new ArrayList<>();
//                for (String s:paths) {
//                    System.out.println("s = " + s);
//                    if (StringUtils.isNotBlank(s)) {
//                        MultipartFile file = MinioUtil.createMfileByPath(anlian + s);
//                        String type = "user/"+entity.getUserId()+"/personTrain";
//                        if (null != file) {
//                            String url = MinioUtil.upload(file, type);
//                            urls.add(url);
//                        } else {
//                            urls.add("---");
//                        }
//                    }
//                }
//                entity.setMinioPath(urls.stream().collect(Collectors.joining(",")));
//                personTrainService.updateById(entity);
//            }
//        }
//        return Result.ok();
//    }
//}
