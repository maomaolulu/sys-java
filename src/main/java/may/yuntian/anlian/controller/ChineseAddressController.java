package may.yuntian.anlian.controller;

import may.yuntian.anlian.service.IChineseAddressService;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description 全国地图数据
 * @Date 2023/8/25 14:43
 * @Author gy
 **/
@RestController
@RequestMapping("/anlian/address")
public class ChineseAddressController extends BaseController {


    @Autowired
    private IChineseAddressService chineseAddressService;

    /**
     * 获取地区树形查询
     */
    @GetMapping("/getRegions")
    public Result getRegions(String regionParentId)
    {
        return Result.ok("查询成功",chineseAddressService.getRegions(regionParentId));
    }


}
