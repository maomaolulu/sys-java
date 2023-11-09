package may.yuntian.socket.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;
import may.yuntian.socket.service.IAbuProjectNoteService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目留言Controller
 *
 * @author yrb
 * @date 2023-04-06
 */
@RestController
@Api(tags = "A~消息发送")
@RequestMapping("/note")
public class AbuProjectNoteController extends BaseController {

    private final IAbuProjectNoteService abuProjectNoteService;

    @Autowired
    public AbuProjectNoteController(IAbuProjectNoteService abuProjectNoteService) {
        this.abuProjectNoteService = abuProjectNoteService;
    }

    @PostMapping("/sendMessage")
    @SysLog("开票通知")
    @ApiOperation("消息发送")
    public Result sendMessage(@RequestBody AbuSendNoteDTO abuSendNoteDTO) {
        try {

            if (StrUtil.isBlank(abuSendNoteDTO.getIdentifier())) {
                return Result.error("项目编号不能为空");
            }
            if (StrUtil.isBlank(abuSendNoteDTO.getSalesman())){
                return Result.error("业务员姓名不能为空");
            }
            abuProjectNoteService.sendMessage(abuSendNoteDTO);
            return Result.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error("留言失败");
        }
    }


    @PostMapping("/sendStatusMessage")
    @SysLog("状态变更消息发送")
    @ApiOperation("状态变更消息发送")
    public Result sendStatusMessage(@RequestBody AbuSendNoteDTO abuSendNoteDTO) {
        try {

            if (StrUtil.isBlank(abuSendNoteDTO.getIdentifier())) {
                return Result.error("项目编号不能为空");
            }
            if (StrUtil.isBlank(abuSendNoteDTO.getSalesman())){
                return Result.error("业务员姓名不能为空");
            }
            abuProjectNoteService.sendStatusMessage(abuSendNoteDTO);
            return Result.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.ok("消息发送失败");
        }
    }

    @GetMapping("/getAbuProjectNoteList")
    public Result getAbuProjectNoteList(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return Result.error("项目id不能为空");
        }
        return Result.data(abuProjectNoteService.selectProjectNoteList(identifier));
    }
}
