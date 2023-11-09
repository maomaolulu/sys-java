package may.yuntian.external.wanda.controller;

import io.swagger.annotations.Api;
import may.yuntian.external.wanda.service.TemplateImportService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * wanda-excel模板导入
 * @author: liyongqiang
 * @create: 2023-07-07 13:12
 */
@Api(tags="万达仓库-excel模板导入")
@RestController
@RequestMapping("/wanda")
public class TemplateImportController {

    @Autowired
    private TemplateImportService templateImportService;


    /**
     * excel导入模板下载
     */
    @GetMapping("/template/download")
    public void downloadExcelTemplate(HttpServletResponse response) {
        templateImportService.downloadExcelTemplate(response);
    }

    /**
     * 导入检测结果
     */
    @PostMapping("/import/result")
    public Result importExcel(@RequestBody MultipartFile file, @RequestParam Long projectId) {
        templateImportService.importExcel(file, projectId);
        return Result.ok("结果导入成功！");
    }

    /**
     * 下载-危害因素字典
     */
    @GetMapping("/export/factorDict")
    public void exportFactorDict(HttpServletResponse response) {
        templateImportService.factorDictDownload(response);
    }


}
