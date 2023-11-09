package may.yuntian.external.wanda.util;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;


/**
 * 附件上传工具类
 * @author: liyongqiang
 * @create: 2023-03-23 15:03
 */
public class AnnexUtil {

    /**
     * 二进制文件转换MultipartFile
     * @param name 文件名
     * @param bytes 字节数组
     * @return
     */
    public static MultipartFile getMultipartFile(String name, byte[] bytes) {
        MultipartFile mfile = null;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(bytes);
            FileItemFactory factory = new DiskFileItemFactory(16, null);
            FileItem fileItem = factory.createItem("mainFile", "text/plain", false, name);
       //     FileItem fileItem = factory.createItem("mainFile", contentType, false, name); // MediaType.MULTIPART_FORM_DATA_VALUE
            IOUtils.copy(new ByteArrayInputStream(bytes), fileItem.getOutputStream());
            mfile = new CommonsMultipartFile(fileItem);
            in.close(); // 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mfile;
    }

    /**
     * byte[]数组转File
     * @param bytes
     * @param fileFullPath
     * @return
     */
    public static File byteArrayToFile(byte[] bytes, String fileFullPath) {
        if (bytes == null) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileFullPath);
            //判断文件是否存在
            if (file.exists()) {
                file.mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }  catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * File 转 MultipartFile
     * @param file
     * @return
     */
    public static MultipartFile fileToMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }

}
