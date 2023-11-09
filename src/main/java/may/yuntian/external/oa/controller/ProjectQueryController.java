package may.yuntian.external.oa.controller;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.external.oa.service.ProjectQueryService;
import may.yuntian.external.oa.vo.ProjectAllQueryVo;
import may.yuntian.external.oa.vo.ProjectQueryVo;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author mi
 */
@RestController
@RequestMapping("/oa/projects")
public class ProjectQueryController {

    @Autowired
    private ProjectQueryService projectQueryService;


    /**
     * 项目编号+受检单位筛选我的项目
     *
     * @param params
     * @param request
     * @return
     */
    @GetMapping("/selectByIdentifierOrCompany")
    public Result selectByIdentifierOrCompany(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String authCode = "d537fa8d-3ec2-48a4-916e-fa834f7f2922";
        String key = request.getHeader("authCode");
        if (!authCode.equals(key)) {
            return Result.error("系统异常");
        }
        String username = (String) params.get("username");
        if (StringUtils.isBlank(username)) {
            return Result.errorOA("参数异常");
        }
        List<ProjectQueryVo> list = projectQueryService.selectByIdentifierOrCompany(params);
        return Result.resultData(list);
    }

    /**
     * 项目编号+受检单位+业务员 全查所有公司项目
     *
     * @param params
     * @param request
     * @return
     */
    @GetMapping("/selectByIdentifierOrCompanyOrSaleMen")
    public Result selectByIdentifierOrCompanyOrSaleMen(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String authCode = "d537fa8d-3ec2-48a4-916e-fa834f7f2922";
        String key = request.getHeader("authCode");
        if (!authCode.equals(key)) {
            return Result.error("系统异常");
        }
        List<ProjectAllQueryVo> list = projectQueryService.selectByIdentifierOrCompanyOrSaleMen(params);
        return Result.resultData(list);
    }

    /**
     * 测试 获取ip
     *
     * @param request
     * @return
     */
    @GetMapping("/getIpCes")
    public Result getIpCes(HttpServletRequest request) {
        String authCode = "d537fa8d-3ec2-48a4-916e-fa834f7f2922";
        String key = request.getHeader("AuthCode");
        boolean a = authCode.equals(key);
        String ip = StringUtils.getIp(request);
        String host = request.getHeader("Host");
        projectQueryService.getIp(ip);
        return Result.ok("ip保存成功!HOST= " + host + ",IP= " + ip + "AuthCode= " + key + "是否匹配= " + a);
    }

}
