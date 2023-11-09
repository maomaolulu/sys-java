package may.yuntian.external.province.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.service.BasicInfoService;
import may.yuntian.external.province.service.EconomicTypeService;
import may.yuntian.external.province.service.IndustryCategoryService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 省报送-项目基本信息controller
 *
 * @author: liyongqiang
 * @create: 2023-04-06 10:24
 */
@RestController
@Api(tags="省报送-基本信息")
@RequestMapping("/province/basic")
public class BasicInfoController {

    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private EconomicTypeService economicTypeService;
    @Autowired
    private IndustryCategoryService industryCategoryService;

    /**
     * 1.基本信息调用逻辑校验
     */
    @GetMapping("/assert")
    public Result assertBasicInfo(Long projectId){
        return Result.ok(basicInfoService.assertBasicInfo(projectId));
    }

    /**
     * 2.经济类型与编码（二级菜单）
     */
    @GetMapping("/economic")
    public Result economicTypeCode(){
        return Result.data(economicTypeService.getEconomicTypeCode());
    }

    /**
     * 3.行业类别与编码（四级菜单）
     */
    @GetMapping("/industry")
    public Result industryTypeCode(){
        return Result.data(industryCategoryService.getIndustryTypeCode());
    }

    /**
     * 4.查询企业员工信息
     */
    @GetMapping("/staff")
    public Result userInfoAll(){
        return Result.data(basicInfoService.getUserInfoAll());
    }

    /**
     * 查询项目基本信息
     */
    @GetMapping("/info")
    @ApiOperation("查询项目基本信息")
    public Result getBasicInfo(Long projectId){
        return Result.ok("查询成功",basicInfoService.getBasicInfo(projectId));
    }

    /**
     * 新增或修改项目基本信息
     */
    @PostMapping("/saveOrUpdate")
    @SysLog("新增或修改基本信息")
    @ApiOperation("新增或修改基本信息")
    public Result saveOrUpdateBasicInfo(@RequestBody BasicInfo basicInfo){
        boolean boo = basicInfoService.saveOrUpdateBasicInfo(basicInfo);
        return boo ? Result.ok("保存成功") : Result.error("保存失败");
    }

    /**
     * 业务员提交前：数据确认
     */
    @GetMapping("/dataRecord")
    public Result dataInfoRecord(Long projectId) {
        return Result.ok("项目信息保存记录", basicInfoService.dataInfoRecord(projectId));
    }

    /**
     * 业务员-提交
     */
    @GetMapping("/submit")
    public Result salesmanSubmit(Long projectId) {
        basicInfoService.salesmanSubmit(projectId);
        return Result.ok("提交成功");
    }

}
