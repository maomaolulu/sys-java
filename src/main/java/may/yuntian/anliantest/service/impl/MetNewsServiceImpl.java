package may.yuntian.anliantest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.service.SampleImgService;
import may.yuntian.anlian.utils.RequestHolder;
import may.yuntian.anlian.vo.CompanySurveyVo;
import may.yuntian.anliantest.entity.MetNews;
import may.yuntian.anliantest.entity.PublicityResults;
import may.yuntian.anliantest.ftpUntils.FtpHelper;
import may.yuntian.anliantest.mapper.MetNewsMapper;
import may.yuntian.anliantest.service.MetNewsService;
import may.yuntian.anliantest.service.PublicityResultsService;
import may.yuntian.anliantest.vo.EvaluateVo;
import may.yuntian.common.exception.RRException;
import may.yuntian.datasources.annotation.DataSource;
import may.yuntian.anlian.entity.SampleImgEntity;
import may.yuntian.anlian.service.SampleImgService;
import may.yuntian.external.wanda.util.AnnexUtil;
import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublictyPjInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * 项目公示
 * 业务逻辑层实现类
 *
 * @author zhanghao
 * @date 2022-04-12
 */
@SuppressWarnings("all")
@Service("metNewsService")
public class MetNewsServiceImpl extends ServiceImpl<MetNewsMapper, MetNews> implements MetNewsService {

    @Autowired
    private FtpHelper ftpHelper;
    @Autowired
    private PublicityResultsService publicityResultsService;
    @Autowired
    private SampleImgService sampleImgService;

//
    public void getTestMetNews(String path){
        HttpServletRequest httpRequest = RequestHolder.getHttpServletRequest();
        Map<String,Object> map = new HashMap<>();
        map.put("file_path", path);
        JSONObject josmmap = JSONUtil.parseObj(map);

        byte[] bytes = HttpRequest.post("http://47.111.249.220:84/proxyAnlianPjPython/project_declare/annex/get_info_publicity ")
                .header("Content-Type", "application/json")
                .header("token", httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().bodyBytes();
        File file1 = null;
        try {
            file1 =  File.createTempFile("abc",".pdf");
//            file1.setWritable(true);
            OutputStream output = new FileOutputStream(file1);

            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);

            bufferedOutput.write(bytes);
            bufferedOutput.flush();
            bufferedOutput.close();
            System.out.println("f:" + file1.getCanonicalFile());


//            System.out.println("f:" + file1);
        }catch (Exception e){

            System.out.println("e:" + e);

        }finally {
//            if (file1!=null){
//                file1.delete();
//            }

        }
    }

//
    /**
     * 检评公示接口
     * @param publicityInfoVo
     * @return
     */
    @Override
    @DataSource(name = "second")
    public Boolean newSaveZjMetNews(PublicityInfoVo publicityInfoVo) {
//        try {
        HttpServletRequest httpRequest = RequestHolder.getHttpServletRequest();
        Map<String,Object> map = new HashMap<>();
        map.put("file_path",publicityInfoVo.getPublicityPath());
        JSONObject josmmap = JSONUtil.parseObj(map);

        byte[] bytes = HttpRequest.post("http://47.111.249.220:84/proxyAnlianPjPython/project_declare/annex/get_info_publicity")
                .header("Content-Type", "application/json")
                .header("token", httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().bodyBytes();
        File file1 = null;
        try {
            file1 = this.byteToFile(bytes);

            Date date = new Date();

            int year = DateUtil.year(date);
            int month = DateUtil.month(date)+1;
            String format ="";
            if(month<10){
                format=String.valueOf(year)+"0"+String.valueOf(month);
            }else {
                format=String.valueOf(year)+String.valueOf(month);
            }

            //登录ftp服务器，使用用户名 和密码
            ftpHelper.login("47.114.191.58",21,"anliantest_com", "yEwpREaHHjKzhX5T");//

            FileInputStream inputStream = new FileInputStream(file1);

            String id2 = IdUtil.objectId();
            //文件名称
            String ftpFileName=id2+".pdf";
            //文件路径
            String ftpDirName="/upload/file/"+format;
            System.out.println(inputStream+"-"+ftpDirName+"-"+ftpFileName);
            boolean b = ftpHelper.uploadFile2(inputStream, ftpDirName, ftpFileName);
            if(b){
                MetNews metNews = new MetNews();

                //路径
                String s = ftpDirName + "/" + ftpFileName;
                /**
                 * <p style="text-align:center;line-height: 16px;"><img style="vertical-align: middle; margin-right: 2px;"
                 * src="https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif"/>
                 * <a style="font-size:12px; color:#0066cc;" href="../upload/file/202203/1648541381897275.pdf"
                 * title="浙江昕兴科技有限公司职业病危害定期检测报告信息公示.pdf">浙江昕兴科技有限公司职业病危害定期检测报告信息公示.pdf</a></p><p><br/></p>
                 */
                //上传成功
                //单位名称
                String company = publicityInfoVo.getCompany();
                //检测类型
                String detectionType = publicityInfoVo.getDetectionType();
                String str=company+detectionType+"报告信息公示";
                //内容
//                String content2="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href="+s+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                String content="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\""+s+"\" title=\""+".."+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                metNews.setTitle(str);
                metNews.setContent(content);
                metNews.setClass1(96);
                metNews.setClass2(24);
                metNews.setClass3(93);
                Date addtime = publicityInfoVo.getAddtime();
                Date updatetime = publicityInfoVo.getUpdatetime();
                metNews.setIdentifier(publicityInfoVo.getIdentifier());
                metNews.setAddtime(addtime!=null?addtime:new Date());
                metNews.setUpdatetime(updatetime!=null?updatetime:new Date());
                metNews.setPublisher("newanlian");
                metNews.setDescription("");
                metNews.setIssue("newanlian");
                metNews.setTag("");
                metNews.setOtherInfo("");
                metNews.setCustomInfo("");
                metNews.setLang("cn");
                this.save(metNews);
            }else {
                return false;
            }
            return true;
        }catch (Exception e){
            log.error("项目公示",e);
            return false;
        }finally {
            ftpHelper.close();
            if (file1!=null){
                file1.delete();
            }
        }

    }

    @Override
    @DataSource(name = "second")
    public boolean savePjNews(PublictyPjInfoVo pjInfoVo){

        HttpServletRequest httpRequest = RequestHolder.getHttpServletRequest();
        Map<String,Object> map = new HashMap<>();
        map.put("file_path",pjInfoVo.getPublictyPath());
        JSONObject josmmap = JSONUtil.parseObj(map);

        byte[] bytes = HttpRequest.post("http://47.111.249.220:84/proxyAnlianPjPython/project_declare/annex/get_info_publicity ")
                .header("Content-Type", "application/json")
                .header("token", httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().bodyBytes();
        File file1 = null;
        try {

            file1 = this.byteToFile(bytes);
            System.out.println("file= " + file1);

            //        File file1 = new File(evaluateVo.getPath());
            Date date = new Date();

            int year = DateUtil.year(date);
            int month = DateUtil.month(date) + 1;
            String format = "";
            if (month < 10) {
                format = String.valueOf(year) + "0" + String.valueOf(month);
            } else {
                format = String.valueOf(year) + String.valueOf(month);
            }
            //登录ftp服务器，使用用户名 和密码
            ftpHelper.login("47.114.191.58", 21, "anliantest_com", "yEwpREaHHjKzhX5T");//改成自己的

            FileInputStream inputStream = new FileInputStream(file1);
            String name = file1.getName();
            String substring = name.substring(name.lastIndexOf("."));
            String id2 = IdUtil.objectId();
            //文件名称
            String ftpFileName = id2 + substring;
            //文件路径
            String ftpDirName = "/upload/file/" + format;

            boolean b = ftpHelper.uploadFile(inputStream, ftpDirName, ftpFileName);
            if (b) {
                MetNews metNews = new MetNews();

                //路径
                String s = ftpDirName + "/" + ftpFileName;

                //上传成功
                //单位名称
                String company = pjInfoVo.getCompany();
                //检测类型 职业病危害现状评价报告信息公示  //控评
                //       职业病危害控制效果评价报告信息公示 //现状
                String type = pjInfoVo.getType();
                String str = "";
                if (type.equals("控制效果评价")) {
                    str = company + "职业病危害控制效果评价报告信息公示";
                } else if (type.equals("现状评价")) {
                    str = company + "职业病危害控制效果评价报告信息公示";
                }
                //内容
                //                String content2="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href="+s+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                String content = "<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"" + s + "\" title=\"" + ".." + str + ".pdf\">" + str + ".pdf</a></p><p><br/></p>";
                metNews.setTitle(str);
                metNews.setContent(content);
                metNews.setClass1(96);
                metNews.setClass2(24);
                if (type.equals("控制效果评价")) {
                    metNews.setClass3(92);
                } else if (type.equals("现状评价")) {
                    metNews.setClass3(91);
                }

                metNews.setAddtime(new Date());
                metNews.setUpdatetime(new Date());
                metNews.setPublisher("newanlian");
                metNews.setDescription("");
                metNews.setIssue("newanlian");
                metNews.setTag("");
                metNews.setOtherInfo("");
                metNews.setCustomInfo("");
                metNews.setLang("cn");
                System.err.println(metNews);
                this.save(metNews);
            } else {
                return false;
            }

        }catch (Exception e){
            log.error("评价FTP公示失败：="+e);
            return false;
        }finally {
            ftpHelper.close();
            if (file1!=null){
                file1.delete();
            }
        }
        return true;
    }


    @Override
    @DataSource(name = "second")
    public Boolean saveMetNews(CompanySurveyVo companySurveyVo)  {
//        try {
        HttpServletRequest httpRequest = RequestHolder.getHttpServletRequest();
        Map<String,Object> map = new HashMap<>();
        map.put("file_path",companySurveyVo.getPath());
        JSONObject josmmap = JSONUtil.parseObj(map);

        byte[] bytes = HttpRequest.post("http://47.111.249.220:84/proxyAnlianPjPython/project_declare/annex/get_info_publicity ")
                .header("Content-Type", "application/json")
                .header("token", httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().bodyBytes();
        File file1 = null;
        try {


            file1 = this.byteToFile(bytes);
//        File file1 = new File(companySurveyVo.getPath());
//        File file1 = newCommission File("D:\\张豪/ZJ2203144publicity.pdf");
        System.out.println(file1.getName());
        System.out.println(file1.length());
        Date date = new Date();

        int year = DateUtil.year(date);
        int month = DateUtil.month(date)+1;
        String format ="";
        if(month<10){
            format=String.valueOf(year)+"0"+String.valueOf(month);
        }else {
            format=String.valueOf(year)+String.valueOf(month);
        }

            //登录ftp服务器，使用用户名 和密码
            ftpHelper.login("47.114.191.58",21,"anliantest_com", "yEwpREaHHjKzhX5T");//

            FileInputStream inputStream = new FileInputStream(file1);
            String name = file1.getName();
            String substring = name.substring(name.lastIndexOf("."));
            String id2 = IdUtil.objectId();
            //文件名称
            String ftpFileName=id2+substring;
            //文件路径
            String ftpDirName="/upload/file/"+format;
            System.out.println(inputStream+"-"+ftpDirName+"-"+ftpFileName);
            boolean b = ftpHelper.uploadFile2(inputStream, ftpDirName, ftpFileName);
            if(b){
                MetNews metNews = new MetNews();

                //路径
                String s = ftpDirName + "/" + ftpFileName;
                /**
                 * <p style="text-align:center;line-height: 16px;"><img style="vertical-align: middle; margin-right: 2px;"
                 * src="https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif"/>
                 * <a style="font-size:12px; color:#0066cc;" href="../upload/file/202203/1648541381897275.pdf"
                 * title="浙江昕兴科技有限公司职业病危害定期检测报告信息公示.pdf">浙江昕兴科技有限公司职业病危害定期检测报告信息公示.pdf</a></p><p><br/></p>
                 */
                //上传成功
                //单位名称
                String company = companySurveyVo.getCompany();
                //检测类型
                String detectionType = companySurveyVo.getDetectionType();
                String str=company+detectionType+"报告信息公示";
                //内容
//                String content2="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href="+s+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                String content="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\""+s+"\" title=\""+".."+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                metNews.setTitle(str);
                metNews.setContent(content);
                metNews.setClass1(96);
                metNews.setClass2(24);
                metNews.setClass3(93);
                Date addtime = companySurveyVo.getAddtime();
                Date updatetime = companySurveyVo.getUpdatetime();

                metNews.setAddtime(addtime!=null?addtime:new Date());
                metNews.setUpdatetime(updatetime!=null?updatetime:new Date());
                metNews.setPublisher("newanlian");
                metNews.setDescription("");
                metNews.setIssue("newanlian");
                metNews.setTag("");
                metNews.setOtherInfo("");
                metNews.setCustomInfo("");
                metNews.setLang("cn");
                System.err.println(metNews);
                this.save(metNews);
            }else {
                return false;
            }
            return true;
        }catch (Exception e){
            log.error("项目公示",e);
            return false;
        }finally {
            ftpHelper.close();
            if (file1!=null){
                file1.delete();
            }
        }

    }

    @Override
    @DataSource(name = "second")
    public Object ces(String path) throws IOException {
        File file1 = new File(path);
//        File file1 = newCommission File(companySurveyVo.getPath());
//        File file1 = newCommission File("D:\\张豪/ZJ2203144publicity.pdf");
        Date date = new Date();

        int year = DateUtil.year(date);
        int month = DateUtil.month(date)+1;
        String format ="";
        if(month<10){
            format=String.valueOf(year)+"0"+String.valueOf(month);
        }else {
            format=String.valueOf(year)+String.valueOf(month);
        }
        //登录ftp服务器，使用用户名 和密码
        ftpHelper.login("47.114.191.58",21,"anliantest_com", "yEwpREaHHjKzhX5T");//


            FileInputStream inputStream = new FileInputStream(file1);
            String name = file1.getName();
            String substring = name.substring(name.lastIndexOf("."));
            String id2 = IdUtil.objectId();
            //文件名称
            String ftpFileName=id2+substring;
            //文件路径
            String ftpDirName="/upload/file/"+format;
        System.out.println(inputStream+"----"+ftpDirName+"-----"+ftpFileName);
            boolean b = ftpHelper.uploadFile2(inputStream, ftpDirName, ftpFileName);



        ftpHelper.close();
        return file1.getName();
    }

    @Override
    /**
     * 评价、控评项目公示
     */
    public void publicityEvaluate(Long projectId) {
//        PublicityResults publicityResults = newCommission PublicityResults();
//        publicityResults.setProjectId(projectId);
//        //publicityResults.setUserId();当前登录人id
//        publicityResults.setUsername("当前登录人");
//        try {
//            EvaluateVo evaluateVo = baseMapper.selectById(projectId);
//            Map<String, Object> map = newCommission HashMap<>();
//            //现场调查陪同时间 pSurveyDate
//            if(evaluateVo.getpSurveyDate()!=null){
//                String format = DateUtil.format(evaluateVo.getpSurveyDate(), "yyyy-MM-dd");
//                map.put("pSurveyDate",format);
//            }else {
//                map.put("pSurveyDate","");
//            }
//            //评价报告提交时间 pIssuedDate
//            if(evaluateVo.getpIssuedDate()!=null){
//                String format = DateUtil.format(evaluateVo.getpIssuedDate(), "yyyy-MM-dd");
//                map.put("pIssuedDate",format);
//            }else {
//                map.put("pIssuedDate","");
//            }
//            evaluateVo.setMap(map);
//            // 现场调查人员
//            String fieldInvestigators = baseMapper.selectFieldInvestigators(projectId);
//            //现场采样，现场检测人员
//            String fieldSamplings = baseMapper.selectFieldSamplings(projectId);
//            //检测图
//            List<SampleImgEntity> sampleImgEntityList = sampleImgService.list(newCommission QueryWrapper<SampleImgEntity>().eq("project_id", evaluateVo.getProjectId()));
//            evaluateVo.setImgs(sampleImgEntityList);
//            evaluateVo.setFieldInvestigators(fieldInvestigators);
//            evaluateVo.setFieldSamplings(fieldSamplings);
//
//            Map<String, Object> mappdf = newCommission HashMap<>();
//            JSONObject jsonObject2 = newCommission JSONObject(evaluateVo);
//            mappdf.put("data",jsonObject2);
//            mappdf.put("projectId",evaluateVo.getProjectId());
//            JSONObject josmmap = JSONUtil.parseObj(mappdf);
//            Object object = HttpUtil.post("http://192.168.0.203:9898/zj_quality_control/generate_info_publicity",josmmap.toString());
//            JSONObject jsonObject = newCommission JSONObject(object);
//            Object o = jsonObject.get("data");
//            Object o1 = jsonObject.get("code");
//            if(!(o1!=null&&o1.toString().equals("200"))){
//                publicityResults.setResult("pdf失败");
//                throw newCommission Exception();
//            }
//            String path=o.toString();
//            evaluateVo.setPath(path);
//            saveEvaluateVoMetNews(evaluateVo);
//            //公示记录
//            publicityResults.setResult("成功");
//            publicityResults.setPath(path);
//            publicityResults.setCompany(evaluateVo.getCompany());
//        }catch (Exception e){
//            publicityResults.setResult("失败");
//        }
//        publicityResultsService.save(publicityResults);

    }


    private  void saveEvaluateVoMetNews(EvaluateVo evaluateVo){

        HttpServletRequest httpRequest = RequestHolder.getHttpServletRequest();
        Map<String,Object> map = new HashMap<>();
        map.put("file_path",evaluateVo.getPath());
        JSONObject josmmap = JSONUtil.parseObj(map);

        byte[] bytes = HttpRequest.post("http://47.111.249.220:84/proxyAnlianPjPython/project_declare/annex/get_info_publicity ")
                .header("Content-Type", "application/json")
                .header("token", httpRequest.getHeader("token"))
                .body(josmmap.toString())
                .execute().bodyBytes();
        File file1 = null;
        try {

            file1 = this.byteToFile(bytes);
            System.out.println("file= " + file1);

            //        File file1 = new File(evaluateVo.getPath());
            Date date = new Date();

            int year = DateUtil.year(date);
            int month = DateUtil.month(date) + 1;
            String format = "";
            if (month < 10) {
                format = String.valueOf(year) + "0" + String.valueOf(month);
            } else {
                format = String.valueOf(year) + String.valueOf(month);
            }
            //登录ftp服务器，使用用户名 和密码
            ftpHelper.login("47.114.191.58", 21, "anliantest_com", "yEwpREaHHjKzhX5T");//改成自己的

            FileInputStream inputStream = new FileInputStream(file1);
            String name = file1.getName();
            String substring = name.substring(name.lastIndexOf("."));
            String id2 = IdUtil.objectId();
            //文件名称
            String ftpFileName = id2 + substring;
            //文件路径
            String ftpDirName = "/upload/file/" + format;

            boolean b = ftpHelper.uploadFile(inputStream, ftpDirName, ftpFileName);
            if (b) {
                MetNews metNews = new MetNews();

                //路径
                String s = ftpDirName + "/" + ftpFileName;

                //上传成功
                //单位名称
                String company = evaluateVo.getCompany();
                //检测类型 职业病危害现状评价报告信息公示  //控评
                //       职业病危害控制效果评价报告信息公示 //现状
                String type = evaluateVo.getType();
                String str = "";
                if (type.equals("控评")) {
                    str = company + "职业病危害控制效果评价报告信息公示";
                } else if (type.equals("现状")) {
                    str = company + "职业病危害控制效果评价报告信息公示";
                }
                //内容
                //                String content2="<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href="+s+str+".pdf\">"+str+".pdf</a></p><p><br/></p>";
                String content = "<p style=\"text-align:center;line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"https://www.anliantest.com/public/plugins/ueditor/dialogs/attachment/fileTypeImages/icon_pdf.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"" + s + "\" title=\"" + ".." + str + ".pdf\">" + str + ".pdf</a></p><p><br/></p>";
                metNews.setTitle(str);
                metNews.setContent(content);
                metNews.setClass1(96);
                metNews.setClass2(24);
                if (type.equals("控评")) {
                    metNews.setClass3(92);
                } else if (type.equals("现状")) {
                    metNews.setClass3(91);
                }

                metNews.setAddtime(new Date());
                metNews.setUpdatetime(new Date());
                metNews.setPublisher("newanlian");
                metNews.setDescription("");
                metNews.setIssue("newanlian");
                metNews.setTag("");
                metNews.setOtherInfo("");
                metNews.setCustomInfo("");
                metNews.setLang("cn");
                System.err.println(metNews);
                this.save(metNews);
            } else {
                throw new Exception();
            }
        }catch (Exception e){

        }finally {
            ftpHelper.close();
            if (file1!=null){
                file1.delete();
            }
        }
    }


    /**
     * byte[]转file
     * @param bytes
     * @return
     * @throws IOException
     */
    private File byteToFile(byte[] bytes) throws IOException {
        File file1 =  File.createTempFile("publicty",".pdf");
        OutputStream output = new FileOutputStream(file1);

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);

        bufferedOutput.write(bytes);
        bufferedOutput.flush();
        bufferedOutput.close();
        return file1;
    }

}
