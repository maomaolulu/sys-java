package may.yuntian.anlianwage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.service.PerCommissionService;
import may.yuntian.anlianwage.vo.PojectCommissionVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @description: 检评-绩效统计控制层
 * @author: lixin
 */
@RestController
@Api(tags="绩效提成")
@RequestMapping("anlianwage/perCommission")
public class PerCommissionController {

    @Autowired
    private PerCommissionService perCommissionService;


    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询提成记录信息列表")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public Result list(@RequestParam Map<String, Object> params){
        List<PojectCommissionVo> page = perCommissionService.queryPage(params);

        return Result.resultData(page);
    }

    /**
     * 列表
     */
    @GetMapping("/listType")
    @ApiOperation("根据条件分页查询提成记录信息列表")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public Result listType(@RequestParam Map<String, Object> params){
        List<PojectCommissionVo> page = perCommissionService.queryTypePage(params);

        return Result.resultData(page);
    }

    /**
     * 显示全部业务员销售目标信息列表--个人明细
     * @return
     */
    @GetMapping("/listAll")
    @SysLog("显示全部提成记录信息列表-导出")
    @ApiOperation("显示全部提成记录信息列表-导出")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public R listAll(@RequestParam Map<String, Object> params){

        List<PojectCommissionVo> list = perCommissionService.listAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 显示全部业务员销售目标信息列表--类型明细
     * @return
     */
    @GetMapping("/listTypeAll")
    @SysLog("显示全部提成记录信息列表-导出")
    @ApiOperation("显示全部提成记录信息列表-导出")
//    @RequiresPermissions("anlianwage:perCommission:list")
    public R listTypeAll(@RequestParam Map<String, Object> params){

        List<PojectCommissionVo> list = perCommissionService.listTypeAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 根据时间段生成提成记录--绩效核算
     */
    @PostMapping("/getListByCommissionDate")
    @SysLog("根据时间段生成提成记录-绩效核算")
    @ApiOperation("根据时间段生成提成记录-绩效核算")
//    @RequiresPermissions("anlianwage:perCommission:update")
    public R getListByCommissionDate(@RequestBody PerCommissionEntity commission){
        List<PojectCommissionVo> list = perCommissionService.getListByCommissionDate(commission);

        return R.ok().put("list", list);
    }

}
