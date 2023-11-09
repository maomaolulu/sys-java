package may.yuntian.anlian.controller;

import java.util.Map;
import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.common.utils.R;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;

import may.yuntian.anlian.entity.ContraccustomDataEntity;
import may.yuntian.anlian.service.ContraccustomDataService;


/**
 * 合同模板自定义字段数据管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@RestController
@Api(tags="合同模板自定义字段数据")
@RequestMapping("anlian/contraccustomdata")
public class ContraccustomDataController {
    @Autowired
    private ContraccustomDataService contraccustomDataService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询合同模板自定义字段数据列表")
    @RequiresPermissions("anlian:contraccustomdata:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contraccustomDataService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示合同模板自定义字段数据详情")
    @RequiresPermissions("anlian:contraccustomdata:info")
    public R info(@PathVariable("id") Long id){
        ContraccustomDataEntity contraccustomData = contraccustomDataService.getById(id);

        return R.ok().put("contraccustomData", contraccustomData);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增合同模板自定义字段数据")
    @ApiOperation("新增合同模板自定义字段数据")
    @RequiresPermissions("anlian:contraccustomdata:save")
    public R save(@RequestBody ContraccustomDataEntity contraccustomData){
        contraccustomDataService.save(contraccustomData);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改合同模板自定义字段数据")
    @ApiOperation("修改合同模板自定义字段数据")
    @RequiresPermissions("anlian:contraccustomdata:update")
    public R update(@RequestBody ContraccustomDataEntity contraccustomData){
        contraccustomDataService.updateById(contraccustomData);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除合同模板自定义字段数据") 
    @ApiOperation("删除合同模板自定义字段数据") 
    @RequiresPermissions("anlian:contraccustomdata:delete")
    public R delete(@RequestBody Long[] ids){
        contraccustomDataService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
