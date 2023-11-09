package may.yuntian.external.province.controller;

import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.service.DemoService;
import may.yuntian.external.wanda.entity.ProjectInfo;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目报送对外演示层：省平台、万达仓
 *
 * @author: liyongqiang
 * @create: 2023-11-06 13:10
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    /** Todo: 省报送 **/
    /**
     * 省报送：项目列表（负责人）
     */
    @GetMapping("/province/list")
    public Result list(BasicInfo info) {
        return Result.resultData(demoService.selectProvinceProjectList(info));
    }

    /** Todo：万达仓 **/
    /**
     * 万达仓：项目列表（负责人）
     */
    @GetMapping("/wanda/list")
    public Result warehouseList(ProjectInfo info) {
        return Result.resultData(demoService.selectWarehouseList(info));
    }


}
