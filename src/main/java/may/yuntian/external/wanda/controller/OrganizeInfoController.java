package may.yuntian.external.wanda.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import may.yuntian.external.wanda.entity.OrganizeInfo;
import may.yuntian.external.wanda.service.OrganizeInfoService;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 万达仓库-技术服务机构基本信息controller层
 * @author: liyongqiang
 * @create: 2023-03-08 13:16
 */
@RestController
@Api(tags="万达仓库-技术服务机构基本信息")
@RequestMapping("/wanda/org")
public class OrganizeInfoController {

    @Autowired
    private OrganizeInfoService organizeInfoService;

    /**
     * 1.1查询技术服务机构基本信息
     */
    @GetMapping("/getOrgInfo")
    public Result getOrgInfo(){
        return Result.ok("查询成功", organizeInfoService.getOrgIfo());
    }

    /**
     * 1.2技术服务机构基本信息：新增 or 编辑
     */
    @PostMapping("/orgInfoAdd")
    public Result addOrEdit(@RequestBody OrganizeInfo organizeInfo){
        organizeInfo.setDataBelong(ShiroUtils.getUserEntity().getSubjection());
        JSONObject json = organizeInfoService.addOrEdit(organizeInfo);
        if ("200".equals(json.get("code"))){
            return Result.ok((String) json.get("message"));
        } else {
            return Result.error(Integer.parseInt(json.getString("code")), json.getString("body"));
        }
    }

    /**
     * 健康在线-项目退回原因
     */
    @GetMapping("/back")
    public Result itemBackReason(String projectCode) {
        return Result.resultData(organizeInfoService.itemBackReason(projectCode));
    }

}
