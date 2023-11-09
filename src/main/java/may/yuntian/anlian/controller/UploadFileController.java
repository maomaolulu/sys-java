package may.yuntian.anlian.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.R;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.anlian.utils.AnlianConfig;
import may.yuntian.anlian.utils.FileUploadUtils;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.anlian.utils.MimeTypes;

/**
 * 文件上传管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-17
 */
@Slf4j
@RestController
@Api(tags="文件上传接口,支持：工艺流程图、设备布局测点布置图、采样影像记录")
@RequestMapping("anlian/uploadFile")
@SuppressWarnings("all")
public class UploadFileController {
    @Autowired
    private AnlianConfig anlianConfig;

    /**
     * 上传图片接口
     */
    @PostMapping("/uploadImage")
    @SysLog("上传图片接口")
    @ApiOperation("上传图片接口")
//    @RequiresPermissions("anlian:uploadFile:image")
    public R uploadImage(HttpServletRequest request,HttpSession session,String module,MultipartFile file){
//    	System.out.println("上传工艺流程图getModule："+module);
//
//    	//String uploadPath = anlianConfig.filePath+anlianConfig.craftProcessPath;//获取工艺流程图上传路径
//    	String uploadPath = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
//    	//项目部署在tomcat下为anlian的文件夹，此时替换为uploadFile,目的是项目与资源文件分开
//    	//比如后台项目访问路径是http://81.68.89.219/anlian，前端请求图片路径为http://81.68.89.219/uploadFile/craftProcess/**.jpg
//    	uploadPath = uploadPath.replace("anlian_sys", "uploadFile")+File.separator+module;	//module文件存储的模块名称
////        String uploadPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile/"+File.separator+module;	//module文件存储的模块名称  jar包
////    	uploadPath = uploadPath.replace("anlian_sys", "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile")+File.separator+module;	//module文件存储的模块名称
//
//    	System.out.println("上传工艺流程图路径1："+uploadPath);
//    	System.out.println("上传工艺流程图路径2：filePath="+anlianConfig.filePath+"	craftProcessPath="+anlianConfig.craftProcessPath);
//
//    	//获取上传文件的路径
//        String path1 = session.getServletContext().getRealPath("photo");
//        System.out.println("session获取上传文件的路径："+path1);
//
//    	if(file.isEmpty()) {
//    		return R.error("上传工艺流程图为空！");
//    	}else {
//    		String path = null;
//            try {
//                path = FileUploadUtils.upload(uploadPath, file, MimeTypes.IMAGE_EXTENSION);
//                //事例数据	uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
//                //页面访问时请求地址为http://81.68.89.219/uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
//                String retPath = "uploadFile/"+module+"/"+path;
////                String retPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile/"+module+"/"+path;  //jar包
//                System.out.println("上传工艺流程图路径返回值："+retPath);
//                return R.ok().put("uploadPath", retPath);
//            } catch (IOException e) {
//                return R.error(e.getLocalizedMessage());
//            }
//    	}
        return R.error("无法上传，联系管理员");
    }
    
    /**
     * 上传文件接口
     */
    @PostMapping("/uploadFile")
    @SysLog("上传文件接口")
    @ApiOperation("上传文件接口，支持图片、文档、压缩文件等")
//    @RequiresPermissions("anlian:uploadFile:file")
    public R uploadFile(HttpServletRequest request,String module,MultipartFile file){

//    	String uploadPath = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
//
////        System.out.println("1111111111111="+uploadPath);
//
////        String uploadPath = "D:\\ruoyi\\uploadPath\\uploadFile";
//    //	String uploadPath = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
//
//    	//项目部署在tomcat下为anlian的文件夹，此时替换为uploadFile,目的是项目与资源文件分开
//    	//比如后台项目访问路径是http://81.68.89.219/anlian，前端请求图片路径为http://81.68.89.219/uploadFile/craftProcess/**.jpg
//    	uploadPath = uploadPath.replace("anlian_sys", "uploadFile")+File.separator+module;	//module文件存储的模块名称
////        String uploadPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile/"+File.separator+module;	//module文件存储的模块名称  new jar包
////        uploadPath = uploadPath.replace("anlian_sys", "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile")+File.separator+module;	//module文件存储的模块名称
//
//        System.out.println("22222222222222="+uploadPath);
//        if(file.isEmpty()) {
//    		return R.error("上传的文件不能为空！");
//    	}else {
//    		String path = null;
//    		try {
//    			path = FileUploadUtils.upload(uploadPath, file, MimeTypes.DEFAULT_ALLOWED_EXTENSION);
//    			//事例数据	uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
//    			//页面访问时请求地址为http://81.68.89.219/uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
//    			String retPath = "uploadFile/"+module+"/"+path;
////                String retPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile/"+module+"/"+path;
//    			return R.ok().put("uploadPath", retPath);
//    		} catch (IOException e) {
//    			return R.error(e.getLocalizedMessage());
//    		}
//    	}
        return R.error("无法上传，联系管理员");
    }
    
    /**
     * 上传工艺流程图路径获取测试
     */
    @PostMapping("/getUploadPath")
    @ApiOperation("上传工艺流程图路径获取测试")
    public R getUploadPath(HttpServletRequest request){
    	String uploadPath ;//= anlianConfig.filePath+anlianConfig.craftProcessPath;//获取工艺流程图上传路径
    	uploadPath = "uploadFile"+System.getProperty("file.separator")+anlianConfig.craftProcessPath;
    	
    	String absPathOfProject = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
    	System.out.println("获取当前项目路径getServletPath："+request.getServletPath());	//	/anlian/crafprocess/getUploadPath
    	System.out.println("获取当前项目路径getContextPath："+request.getContextPath());	//	/anlian
    	
    	
    	System.out.println("springboot获取当前项目路径的地址："+System.getProperty("user.dir"));	//	Windows D:\\Tomcat9.0\\bin
    	System.out.println("获取当前项目路径："+absPathOfProject);
    	
   
    	return R.ok("获取当前项目路径："+absPathOfProject).put("springboot获取当前项目路径的地址：", System.getProperty("user.dir"));
    	
    }

    /**
     * 访问图片pdf
     */
    @GetMapping("/getByte")
    @ApiOperation("访问图片pdf")
    public byte[] getByte(HttpServletResponse response, String fileUrl) {
//<<<<<<< HEAD

        fileUrl = "/opt/module/apache-tomcat-9.0.37/webapps/"+fileUrl;

//=======
//        fileUrl = fileUrl.replace("%5C", "\\");
//>>>>>>> c4cc145c786775a5a56bcef16a8db1c23b8c616c
        FileInputStream inputStream = null;
        byte[] bytes = null;
        try {
            File file = new File(fileUrl);
            inputStream = new FileInputStream(file);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes,0,inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;

    }


    /**
     * 下载pdf
     */
    @GetMapping("/downLoadPdf")
    @ApiOperation("下载pdf")
    public void downLoad(HttpServletResponse response, String pdfName) {

        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        String filName = null;
        try {
            if (pdfName.equals("1.pdf")){
                filName = "L025杭州萧山国际机场有限公司.pdf";
            }else if(pdfName.equals("2.pdf")){
                filName = "杭州萧山国际机场有限公司（902）.pdf";
            }else {
                filName = "杭州萧山国际机场有限公司.pdf";
            }
//            String path = ":C\\Users\\ThinkPad\\Desktop"+"\\"+pdfName; //文件所在路径
            String path = "/usr/local/src/server/anlian_zj/jar/uploadFile/"+pdfName; //文件所在路径
//            String path = "http://47.111.249.220:81/imgApi/uploadFile/loadModule/"+pdfName; //文件所在路径
            file = new File(path);
            fin = new FileInputStream(file);


            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;fileName="
                    .concat(String.valueOf(URLEncoder.encode(filName, "UTF-8"))));

            out = response.getOutputStream();

            // 缓冲区
            byte[] buffer = new byte[512];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            out.flush();
            log.info("pdf文件下载完成.....");
        } catch (Exception e) {

            log.error("pdf文件下载异常,e = {}", e);
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }


}
