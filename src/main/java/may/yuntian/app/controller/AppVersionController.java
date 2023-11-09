package may.yuntian.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anliantest.service.MetNewsService;
import may.yuntian.app.entity.AppVersion;
import may.yuntian.app.service.AppVersionService;
import may.yuntian.common.utils.R;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * app版本管理
 * @author zhanghao
 * @date 2022-04-12
 * @menu app版本管理
 * @menu app版本管理
 */

@RestController
@Api(tags="app版本管理")
@RequestMapping("app/appVersion")
public class AppVersionController {
	@Autowired
	private AppVersionService appVersionService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    @GetMapping("/getNewestAppVersion")
    @ApiOperation("获取最新版本")
    public R getNewestAppVersion(String types){
        if (StringUtils.isBlank(types)){
            types = "ZJ";
        }
        AppVersion appVersion = appVersionService.getOne(new QueryWrapper<AppVersion>().eq(StringUtils.isNotBlank(types), "types", types).orderByDesc("id").last("limit 1"));
        return R.ok().put("data",appVersion);
    }

    @PostMapping("/addAppVersion")
    @ApiOperation("添加新版本")
    public R addAppVersion(@RequestBody AppVersion appVersion){
        if (StringUtils.isNotBlank(appVersion.getPath())){
            Object o = alRedisUntil.hget("anlian-java",appVersion.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",appVersion.getPath());
            }
        }
        boolean b = appVersionService.save(appVersion);
        if (b){
            return R.ok().put("message", "保存成功");
        }else {
            return R.ok().put("message", "保存失败");
        }
    }

    @GetMapping("listAll")
    @ApiOperation("查看所有版本号")
    public R listAll(@RequestParam Map<String,Object> params){
        List<AppVersion> listAll = appVersionService.listAll(params);
        return R.ok().put("list",listAll);
    }

}
