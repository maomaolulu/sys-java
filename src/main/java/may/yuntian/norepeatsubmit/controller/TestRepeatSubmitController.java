package may.yuntian.norepeatsubmit.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.common.utils.R;
import may.yuntian.exception.custom.RepeatSubmitException;
import may.yuntian.norepeatsubmit.annotion.RepeatSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description
 * @Date 2023/4/20 9:42
 * @Author maoly
 **/
@RestController
@SuppressWarnings("all")
@Api(tags="测试类")
@RequestMapping("anlian/submit")
@Slf4j
public class TestRepeatSubmitController {


    @GetMapping("/list")
    public R testNoAnnotion(){
        log.info("测试重复提交-未加注解");
        return R.ok();
    }


    @GetMapping("/test")
    @RepeatSubmit
    public R testAnnotion() throws RepeatSubmitException {
        log.info("测试重复提交-加注解");
        return R.ok();
    }

    /**
     * 免登录
     * @return
     * @throws RepeatSubmitException
     */
    @GetMapping("/testNoToken")
    @RepeatSubmit
    public R testNoToken(){
        log.info("测试重复提交-加注解,无token");
        return R.ok();
    }


    @GetMapping("/test1")
    @RepeatSubmit
    public R testAnnotion1(String name){
        log.info("测试重复提交-加注解");
        return R.ok();
    }

    @GetMapping("/test2")
    @RepeatSubmit
    public R testAnnotion1(Map<String,Object> name) {
        log.info("测试重复提交-加注解");
        return R.ok();
    }

}
