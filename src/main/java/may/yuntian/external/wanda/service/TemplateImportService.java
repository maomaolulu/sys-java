package may.yuntian.external.wanda.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: liyongqiang
 * @create: 2023-07-07 15:23
 */
public interface TemplateImportService {
    /**
     * excel导入模板下载
     * @param response
     */
    void downloadExcelTemplate(HttpServletResponse response);

    /**
     * 导入检测结果
     * @param file
     * @param projectId
     */
    void importExcel(MultipartFile file, Long projectId);

    /**
     * 导出危害因素字典
     * @param response
     */
    void factorDictDownload(HttpServletResponse response);
}
