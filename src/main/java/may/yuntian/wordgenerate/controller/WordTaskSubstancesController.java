package may.yuntian.wordgenerate.controller;

import java.util.List;
import java.util.Arrays;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.wordgenerate.vo.TaskGenerateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.wordgenerate.entity.WordTaskSubstancesEntity;
import may.yuntian.wordgenerate.service.WordTaskSubstancesService;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@RestController
@Api(tags="环卫任务单物质列表")
@RequestMapping("/wordTaskSubstances")
public class WordTaskSubstancesController {
    @Autowired
    private WordTaskSubstancesService wordTaskSubstancesService;


    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增")
    @ApiOperation("新增")
//    @RequiresPermissions("wordgenerate:wordtasksubstances:save")
    public AjaxResult save(@RequestBody WordTaskSubstancesEntity wordTaskSubstances){
        wordTaskSubstancesService.save(wordTaskSubstances);

        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改")
    @ApiOperation("修改")
//    @RequiresPermissions("wordgenerate:wordtasksubstances:update")
    public AjaxResult update(@RequestBody WordTaskSubstancesEntity wordTaskSubstances){
        wordTaskSubstancesService.updateById(wordTaskSubstances);
        
        return AjaxResult.success();
    }

    /**
     * 批量保存或修改
     */
    @PostMapping("/saveOrUpdateBatch")
    @SysLog("批量保存或修改")
    @ApiOperation("批量保存或修改")
//    @RequiresPermissions("wordgenerate:wordtasksubstances:update")
    public AjaxResult saveOrUpdateBatch(@RequestBody TaskGenerateVo taskGenerateVo){
        wordTaskSubstancesService.deleteByProjectId(taskGenerateVo.getId());

        if (StringUtils.isNotEmpty(taskGenerateVo.getSubstanceList())){
            wordTaskSubstancesService.saveOrUpdateBatch(taskGenerateVo.getSubstanceList());
        }
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除")
    @ApiOperation("删除")
//    @RequiresPermissions("wordgenerate:wordtasksubstances:delete")
    public AjaxResult delete(@RequestBody Long[] ids){
        wordTaskSubstancesService.removeByIds(Arrays.asList(ids));

        return AjaxResult.success();
    }

}
