package may.yuntian.wordgenerate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.untils.Result;
import may.yuntian.wordgenerate.entity.WordContractTypeEntity;
import may.yuntian.wordgenerate.mongoservice.EvalPlanRecordService;
import may.yuntian.wordgenerate.service.WordGenerateService;
import may.yuntian.wordgenerate.vo.QueryVo;
import may.yuntian.wordgenerate.vo.TaskGenerateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "合同、任务单、评审单word生成")
@RequestMapping("generateWord")
public class WordGenerateController {

    @Autowired
    private WordGenerateService wordGenerateService;


//    /**
//     * MONGODB测试接口
//     * @param id
//     * @return
//     */
//    @GetMapping("{id}")
////    public Result getInfo(@PathVariable("id") String id){
//    public Result getInfo(@PathVariable("id") Long id){
////        return Result.data(evalPlanRecordService.getOneById(id));
//        return Result.data(evalPlanRecordService.getSubstances(id));
//    }

    /**
     * 生成合同word
     */
    @PostMapping("/contractGenerateWord")
    @ApiOperation("生成合同word")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public Result contractGenerateWord(@RequestBody QueryVo queryVo){
        Long id = queryVo.getId();
        String wordType = queryVo.getWordType();
        wordGenerateService.generateContract(id,wordType);
        return Result.ok();
    }

    /**
     * 获取所有word类型
     * @param
     * @return
     */
    @GetMapping("getAllWord")
    @ApiOperation("获取所有word类型")
    public Result getAllWord(){
        List<WordContractTypeEntity> list = wordGenerateService.getWordType();
        return Result.data(list);
    }


    /**
     * 生成任务单及评审单
     */
    @PostMapping("/testGenerateWord")
    @ApiOperation("生成任务单及评审单  task 任务单； review 评审单 protocol 委托协议")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public Result generateWord(@RequestBody QueryVo queryVo){
        Long projectId = queryVo.getProjectId();
        String wordType = queryVo.getWordType();
        wordGenerateService.taskGenerate(projectId,wordType);
        return Result.ok();
    }

    /**
     * 下载 1 任务单, 2 评审单，3 合同协议 4 协议
     */
    @PostMapping("/downLoadGenerateWord")
    @ApiOperation("下载 1 任务单, 2 评审单，3 合同协议 4 协议")
    public void downLoadWord(@RequestBody QueryVo queryVo, HttpServletResponse response){
        Long projectId = queryVo.getProjectId();
        Integer type = queryVo.getType();
        wordGenerateService.downLoadFile(projectId,type,response);
    }

    /**
     * 预览任务单
     * @param projectId
     * @return
     */
    @GetMapping("info/{projectId}")
    @ApiOperation("预览任务单")
    public Result info(@PathVariable("projectId") Long projectId){
        TaskGenerateVo taskGenerateVo = wordGenerateService.previewTask(projectId);
        return Result.data(taskGenerateVo);
    }

    /**
     * 评价任务单保存数据接口
     */
    @PostMapping("/pjSaveTask")
    @ApiOperation("评价任务单保存数据接口")
    public Result pjSaveTask(@RequestBody TaskGenerateVo taskGenerateVo){
        wordGenerateService.pjSaveTask(taskGenerateVo);
        return Result.ok();
    }
}
