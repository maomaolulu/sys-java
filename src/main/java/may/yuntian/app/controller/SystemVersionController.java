package may.yuntian.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.app.entity.SystemVersion;
import may.yuntian.app.service.SystemVersionService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统版本详情
 *
 * @author LIXIN
 * @data 2022-10-12
 */

@RestController
@Api(tags="系统版本详情")
@RequestMapping("app/systemVersion")
public class SystemVersionController {
	@Autowired
	private SystemVersionService systemVersionService;


    @PostMapping("/addSystemVersion")
    @SysLog("添加新系统版本详情")
    @ApiOperation("添加新系统版本详情")
    public R addSystemVersion(@RequestBody SystemVersion systemVersion){
        boolean b = systemVersionService.save(systemVersion);
        if (b){
            return R.ok();
        }else {
            return R.error("保存失败");
        }
    }

    @PostMapping("/updateSystemVersion")
    @SysLog("修改新系统版本详情")
    @ApiOperation("修改新系统版本详情")
    public R updateSystemVersion(@RequestBody SystemVersion systemVersion){
        boolean b = systemVersionService.updateById(systemVersion);
        if (b){
            return R.ok();
        }else {
            return R.error("修改失败");
        }
    }

    @GetMapping("/listAll")
    @ApiOperation("查看所有版本号")
    public R listAll(@RequestParam Map<String,Object> params){
        List<SystemVersion> listAll = systemVersionService.listAll(params);
        return R.ok().put("list",listAll);
    }

}
