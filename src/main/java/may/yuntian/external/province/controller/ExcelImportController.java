package may.yuntian.external.province.controller;

import io.swagger.annotations.Api;
import may.yuntian.external.province.service.ExcelImportService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 省平台-excel模板导入controller
 * @author: liyongqiang
 * @create: 2023-07-14 13:18
 */
@Api(tags="省报送-excel模板导入")
@RestController
@RequestMapping("/province")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * excel模板下载
     */
    @GetMapping("/template/download")
    public void downloadExcelTemplate(HttpServletResponse response) {
        excelImportService.downloadExcelTemplate(response);
    }

    /**
     * 导入检测结果
     */
    @PostMapping("/import/result")
    public Result importExcel(@RequestParam MultipartFile file, @RequestParam Long projectId) {
        excelImportService.importExcel(file, projectId);
        return Result.ok("结果导入成功！");
    }

    /**
     * 下载-检测项目字典
     */
    @GetMapping("/export/factorDict")
    public void exportFactorDict(HttpServletResponse response) {
        excelImportService.factorDictDownload(response);
    }


}
