package may.yuntian.filiale.hzyd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.service.DetectInfoService;
import may.yuntian.filiale.hzyd.vo.DetectInfoQueryVo;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 亿达仪器设备详情
 *
 * @author LIXIN
 */

@RestController
@Api(tags="亿达仪器设备详情")
@RequestMapping("yd/detectInfo")
public class DetectInfoController  extends BaseController {
	@Autowired
	private DetectInfoService detectInfoService;


    @GetMapping("/getPageList")
    @ApiOperation("根据类别查看亿达仪器设备")
    public Result listAll(DetectInfoQueryVo detectInfoQueryVo){
        if (StringUtils.checkValNull(detectInfoQueryVo.getCategory())){
            return Result.error("未指定类目，无法查询");
        }else {
            return Result.resultData(detectInfoService.getPageList(detectInfoQueryVo));
        }
    }


    @PostMapping("/add")
    @SysLog("添加设备信息")
    @ApiOperation("添加设备信息")
    public Result addSystemVersion(@RequestBody DetectInfo detectInfo){
        boolean b = detectInfoService.saveInfo(detectInfo);
        if (b){
            return Result.ok("新增成功");
        }else {
            return Result.error("新增失败");
        }
    }

    @PostMapping("/update")
    @SysLog("修改设备信息")
    @ApiOperation("修改设备信息")
    public Result updateSystemVersion(@RequestBody DetectInfo detectInfo){
        boolean b = detectInfoService.updateInfo(detectInfo);
        if (b){
            return Result.ok("修改成功");
        }else {
            return Result.error("修改失败");
        }
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除设备信息")
    @ApiOperation("删除设备信息")
    public Result delete(@RequestBody Long[] ids){
        boolean b = detectInfoService.delFlagById(ids);
        if (b){
            return Result.ok("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }

}
