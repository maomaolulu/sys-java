package may.yuntian.exception.test;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.common.utils.R;
import may.yuntian.exception.custom.NeverLoginException;
import may.yuntian.exception.custom.TokenCheckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Date 2023/4/18 13:39
 * @Author maoly
 **/
@RestController
@SuppressWarnings("all")
@Api(tags="测试类")
@RequestMapping("anlian/test")
@Slf4j
public class TestControllere {

    @Autowired
    private TestService testService;

    /**
     * 异常测试
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    public R testException(@PathVariable String id){
        Integer count = testService.list(id);
        //Integer count = testService.list1(id);
        return R.ok().put("count",count);
    }

    @GetMapping(value = "/exception/neverlogin")
    public void exceptionNeverLogin(){
        throw new NeverLoginException("未登录，请登录后再试", "Never Login userId: 10010");
    }

    @GetMapping(value = "/exception/token")
    public void exceptionToken(){
        throw new TokenCheckException("token校验失败", "token: 11122233444");
    }

    @GetMapping(value = "/exception/te")
    public R exceptionR() {
        log.error("token校验失败");
        return R.error(10003,"token校验失败");
    }
}
