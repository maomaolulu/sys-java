package may.yuntian.external.province.controller;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.service.SubstanceContrastResultService;
import may.yuntian.external.province.service.ResultItemService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 省报送-结果项信息controller
 *
 * @author: liyongqiang
 * @create: 2023-04-06 11:13
 */
@Slf4j
@RestController
@Api(tags="省报送-结果项")
@RequestMapping("/province/result")
public class ResultItemController {

    @Autowired
    private ResultItemService resultItemService;
    @Autowired
    private SubstanceContrastResultService substanceContrastResultService;

    /**
     * 3.结果项调用逻辑校验
     */
    @GetMapping("/assert")
    public Result assertResult(Long projectId){
        return Result.ok(resultItemService.assertResult(projectId));
    }

    /**
     * 结果查询
     */
    @GetMapping("/list")
    public Result list(Long projectId){
        return Result.data(resultItemService.resultItemList(projectId));
    }

    /**
     * 结果保存
     */
    @PostMapping("save")
    public Result batchSaveResult(@RequestBody Map<Integer, Map<String, List<ResultItem>>> resultMap){
        if (CollUtil.isEmpty(resultMap)) {
            throw new RRException("结果列表不能为空！");
        }
        resultItemService.batchSaveResult(resultMap);
        return Result.ok("保存成功");
    }

    /**
     * 结果项：名称-编码
     */
    @GetMapping("/code")
    public Result resultNameCode(){
        return Result.data(resultItemService.getResultNameCodeList());
    }

    /**
     * 安联-物质列表
     */
    @GetMapping("/substance")
    public Result subStanceCodeList(){
        return Result.data(substanceContrastResultService.getSubstanceCodeList());
    }

    /**
     * 物质: 结果项-编码-计量单位
     */
    @GetMapping("/checkItemCode")
    public Result checkItemCode(Long subId){
        return Result.ok("物质：结果项、编码、计量单位", substanceContrastResultService.getCheckItemCode(subId));
    }

}
