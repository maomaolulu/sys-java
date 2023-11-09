package may.yuntian.external.province.controller;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.service.ProjectDeclarationService;
import may.yuntian.external.province.vo.RejectVo;
import may.yuntian.untils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 省报送-主管+质控Controller
 * @author: liyongqiang
 * @create: 2023-04-04 13:47
 */
@RestController
@RequestMapping("/province/quality")
public class ProjectDeclarationController {

    @Resource
    private ProjectDeclarationService projectDeclarationService;

    /**
     * 质控：推送
     */
    @GetMapping("/push")
    public Result push(Long projectId){
        String message = projectDeclarationService.push(projectId);
        return StringUtils.isBlank(message) ? Result.ok("推送成功！") : Result.error(message);
    }

    /**
     * 主管or质控：项目列表
     */
    @GetMapping("/list")
    public Result list(BasicInfo info) {
        return Result.resultData(projectDeclarationService.getProjectList(info));
    }

    /**
     * 主管or质控：查看
     */
    @GetMapping("/view")
    public Result viewProjectDataInfo(Long projectId, Integer viewer){
        return Result.ok("查询成功", projectDeclarationService.viewProjectDataInfo(projectId, viewer));
    }

    /**
     * 主管or质控：驳回
     */
    @PostMapping("/reject")
    public Result rejectReason(@RequestBody RejectVo rejectVo){
        projectDeclarationService.rejectReason(rejectVo);
        return Result.ok("驳回成功！");
    }

    /**
     * 主管：提交
     */
    @GetMapping("/charge/submit")
    public Result chargeSubmit(Long projectId){
        projectDeclarationService.chargeSubmit(projectId);
        return Result.ok("提交成功！");
    }

}
