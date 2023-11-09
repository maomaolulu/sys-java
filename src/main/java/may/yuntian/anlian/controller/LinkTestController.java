package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import may.yuntian.anlian.service.LinkTestService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
@RestController
@Api(tags="连接测试")
public class LinkTestController {
    @Autowired
    private LinkTestService linkTestService;
//    @Autowired
//    private TaskUtils  taskUtils;


    @GetMapping("/ping")
    public Result ping() throws UnknownHostException {
        Map<String,Object> map = new HashMap<>();
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("-----------"+ip.getHostAddress());
        try {
            int count = linkTestService.count();
            map.put("db_status","OK");
        }catch (Exception e){
            map.put("db_status","FAILD");
        }

        return Result.data(map);
    }

//    @GetMapping("/getRedisValues")
//    public Result getRedisValues(){
////        taskUtils.minioFilePath();
//        return Result.ok("操作成功",taskUtils.minioFilePath());
//    }

}
