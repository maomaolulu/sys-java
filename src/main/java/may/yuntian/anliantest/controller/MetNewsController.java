package may.yuntian.anliantest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anliantest.service.MetNewsService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 项目公示
 * @author zhanghao
 * @date 2022-04-14
 * @menu 项目公示
 */

@RestController
@Api(tags="项目公示")
@RequestMapping("anliantest/metNews")
public class MetNewsController {
	@Autowired
	private MetNewsService metNewsService;

//    @GetMapping("/getNewestAppVersion")
//    @ApiOperation("获取最新版本")
//    public R getNewestAppVersion(){
//        AppVersion appVersion = appVersionService.getOne(newCommission QueryWrapper<AppVersion>().orderByDesc("id").last("limit 1"));
//        return R.ok().put("data",appVersion);
//    }
    @GetMapping("/cs")
//    @ApiOperation("cs")
    public Result getNewestAppVersion2(String path){

            metNewsService.getTestMetNews(path);

            return Result.ok();


    }


}
