package may.yuntian.anlian.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.StyleSet;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;

/**
 * 文件处理工具类
 * @author MaYong
 */
public class FileUtils {

    public static final String FILENAME_PATTERN = "[a-zA-Z0-9_\\-|.\\u4e00-\\u9fa5]+";
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 创建目录
     *
     * @param dirPath
     * @return
     */
    public static boolean makeDir(String dirPath) {
        return makeDir(new File(dirPath));
    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean makeDir(File file) {
        if (!file.exists() && !file.isDirectory()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 获取当前项目路径
     *
     * @return
     */
    public static String getAbsPathOfProject(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("//");
    }

    /**
     * 获取项目文件流路径
     *
     * @return
     */
    public static InputStream getResourceAsStreamOfProject(HttpServletRequest request, String path) {
        return request.getSession().getServletContext().getResourceAsStream(path);
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFilePrefix(String fileName) {
        if (null != fileName && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取文件上级目录
     *
     * @param path 文件路径
     * @return
     */
    public static String getFileParentPath(String path) {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            return path.substring(0, path.lastIndexOf("/") + 1);
        }
        return null;
    }

    /**
     * 删除文件的方法(只适用于删除文件)
     *
     * @param files
     * @author Caratacus
     */
    public static void delFiles(List<File> files) {
        if (files != null && !files.isEmpty()) {
            for (File dir : files) {
                if (dir == null || !dir.exists()) {
                    return;
                } else {
                    dir.delete();
                }
            }
        }
    }

    /**
     * 删除目录下所有文件（包括目录和文件）
     *
     * @param dir
     */
    public static void delFile(File dir) {
        try {
            if (dir != null) {
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (ArrayUtils.isNotEmpty(files)) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                File[] listFiles = file.listFiles();
                                if (ArrayUtils.isNotEmpty(listFiles)) {
                                    delFile(file);
                                } else {
                                    file.delete();
                                }
                            } else {
                                file.delete();
                            }
                        }
                    }
                }
                dir.delete();
            }
        } catch (Exception e) {
            log.error("Path: " + dir.getAbsolutePath() + " , Delete File Result : Error");
        }

    }

    /**
     * http下载文件
     *
     * @param httpUrl
     * @param dir      文件目录
     * @param fileName 文件名称
     * @return
     */
    public static boolean httpDownload(String httpUrl, String dir, String fileName) {
        int byteRead;
        URL url;
        log.info("文件正在下载中,PATH:" + httpUrl);
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
            log.warn("Warn: MalformedURLException on httpDownload.  Cause:" + e);
            return false;
        }
        FileOutputStream fileOutputStream = null;
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            // 设置超时间为5秒
            connection.setConnectTimeout(5 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = connection.getInputStream();
            makeDir(dir);
            fileOutputStream = new FileOutputStream(dir + File.separator + fileName);
            byte[] buffer = new byte[8192];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            return true;
        } catch (FileNotFoundException e) {
            log.warn("Warn: FileNotFoundException on httpDownload.  Cause:" + e);
            return false;
        } catch (IOException e) {
            log.warn("Warn: IOException on httpDownload.  Cause:" + e);
            return false;
        } finally {
            IOUtils.close(connection);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    /**
     * 计算文件大小(适用于计算文件及文件夹大小)
     *
     * @param file 文件
     * @return 返回long类型字节(B)
     */
    public static long size(File file) {
        if (file.exists()) {
            return file.length();
        }
        return 0L;
    }

    /**
     * 计算文件大小
     *
     * @param filePath 文件路径
     * @return 返回long类型字节(B)
     */
    public static long size(String filePath) {
        return size(new File(filePath));
    }

    /**
     * 拷贝文件方法
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream outputStream = null;
        try {
            int byteread;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldPath); // 读入原文件
                outputStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[8192];
                while ((byteread = inStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            log.warn("Warn: Exception on copyFile.  Cause:" + e);
        } finally {
            IOUtils.closeQuietly(inStream, outputStream);
        }
    }

    /**
     * 读取文本文件返回字符串
     *
     * @param filePath
     * @return
     */
    public static String readFileString(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder(128);
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
            }
            return builder.toString();
        } catch (IOException e) {
            log.warn("Warn: Exception on getStringByFile.  Cause:" + e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return null;
    }

    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) {
        String tempPath =System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer= ExcelUtil.getBigWriter(file);
        // 样式类别：头部headCellStyle、普通单元格cellStyle、数字单元格cellStyleForNumber、日期单元格样式cellStyleForDate
        StyleSet styleSet = writer.getStyleSet();
        CellStyle headCellStyle = styleSet.getHeadCellStyle();
        XSSFFont xssfFont = new XSSFFont();
        xssfFont.setBold(true);
        xssfFont.setColor(Font.COLOR_NORMAL);
        headCellStyle.setFont(xssfFont);
        // 开启单元格自动调整大小
        CellStyle cellStyle = styleSet.getCellStyle();
        cellStyle.setShrinkToFit(true);
        // 一次性写出内容，强制输出标题
        writer.write(list, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=file.xlsx");
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件下载：",e.getMessage());
        }
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }


    /**
     * 下载文件
     * @param path
     * @param response
     * @return
     */
    public static HttpServletResponse downloadWord(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header new String(filename.getBytes())
            response.addHeader("Content-Disposition", "attachment;filename=" .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * 上传文件重命名
     * @return 新的文件名
     */
    public static StringBuffer fileRename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        StringBuffer buf = new StringBuffer(time);
        Random r = new Random();
        //循环取得三个不大于10的随机整数
        for (int x = 0; x < 3; x++) {
            buf.append(r.nextInt(10));
        }
        return buf;
    }

}
