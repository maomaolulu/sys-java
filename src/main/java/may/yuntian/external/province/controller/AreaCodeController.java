package may.yuntian.external.province.controller;

import may.yuntian.external.province.service.AreaCodeService;
import may.yuntian.untils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 省报送-行政区划编码controller
 * @author: liyongqiang
 * @create: 2023-04-14 10:54
 */
@RestController
@RequestMapping("/province/area")
public class AreaCodeController {

    @Resource
    private AreaCodeService areaCodeService;

    /**
     * 技术服务地区-二级下拉菜单（市、区）
     */
    @GetMapping("/service")
    public Result serviceAreaList(String province){
        return Result.ok("查询成功", areaCodeService.getServiceAreaMenu(province));
    }


}
