package may.yuntian.filiale.hzyd.controller;

import io.swagger.annotations.Api;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.filiale.hzyd.dto.ProjectDto;
import may.yuntian.filiale.hzyd.service.ProjectManageService;
import may.yuntian.filiale.hzyd.vo.OperateVo;
import may.yuntian.untils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 亿达-业务中心：项目管理controller
 *
 * @author: liyongqiang
 * @create: 2023-08-11 12:14
 */
@RestController
@Api(tags = "亿达-业务中心-项目管理")
@RequestMapping("/yd/project/manage")
public class ProjectManageController {

    private final ProjectManageService projectManageService;

    public ProjectManageController(ProjectManageService projectManageService) {
        this.projectManageService = projectManageService;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result projectList(ProjectDto projectDto) {
        return Result.resultData(projectManageService.selectProjectList(projectDto));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    @SysLog("YD-新增项目")
    public Result add(@RequestBody ProjectDto projectDto) {
        return Result.ok(projectManageService.addProject(projectDto) > 0 ? "项目新增成功" : "项目新增失败");
    }

    /**
     * （批量）下发
     */
    @PostMapping("/issue")
    @SysLog("YD-项目（批量）下发")
    public Result batchIssue(@RequestBody OperateVo operateVo) {
        projectManageService.batchIssue(operateVo);
        return Result.ok("项目下发成功");
    }

    /**
     * （批量）确认
     */
    @PostMapping("/confirm")
    @SysLog("YD-项目（批量）确认")
    public Result batchConfirm(@RequestBody OperateVo operateVo) {
        projectManageService.batchConfirm(operateVo);
        return Result.ok("项目确认成功");
    }

    /**
     * 详情
     */
    @GetMapping("/details")
    public Result viewDetails(Long projectId) {
        return Result.data(projectManageService.viewDetails(projectId));
    }

    /**
     * 编辑-保存
     */
    @PostMapping("/edit")
    @SysLog("YD-项目编辑保存")
    public Result editAfterSave(@RequestBody ProjectDto projectDto) {
        projectManageService.editAfterSave(projectDto);
        return Result.ok("保存成功");
    }

    /**
     * 中止
     */
    @GetMapping("/abort")
    @SysLog("YD-项目中止")
    public Result abort(Long projectId) {
        return Result.ok(projectManageService.abort(projectId) > 0 ? "中止成功！" : "中止失败！");
    }

}
