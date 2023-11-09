package may.yuntian.external.province.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.external.province.entity.ParticipantTable;
import may.yuntian.external.province.service.ParticipantTableService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 省报送-参与人员信息controller
 *
 * @author: liyongqiang
 * @create: 2023-04-06 10:38
 */
@RestController
@Api(tags="省报送-参与人员信息")
@RequestMapping("/province/participant")
public class ParticipantTableController {

    @Autowired
    private ParticipantTableService participantTableService;

    /**
     * 2.人员列表调用逻辑校验
     */
    @GetMapping("/assert")
    public Result assertParticipant(Long projectId){
        return Result.ok(participantTableService.assertParticipant(projectId));
    }

    /**
     * 查询参与人员信息
     */
    @GetMapping("/list")
    public Result getBasicInfoList(Long projectId){
        return Result.data(participantTableService.getListByProjectId(projectId));
    }

    /**
     * 保存参与人员信息
     */
    @PostMapping("/save")
    @SysLog("保存参与人员信息")
    @ApiOperation("保存参与人员信息")
    public Result saveParticipantList(@RequestBody List<ParticipantTable> list){
        participantTableService.saveParticipantList(list);
        return Result.ok("保存成功");
    }

}
