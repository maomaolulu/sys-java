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

import may.yuntian.anlian.entity.ContractemplateEntity;
import may.yuntian.anlian.service.ContractemplateService;


/**
 * 合同模板共同信息管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@RestController
@Api(tags="合同模板共同信息")
@RequestMapping("anlian/contractemplate")
public class ContractemplateController {
    @Autowired
    private ContractemplateService contractemplateService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询合同模板共同信息列表")
    @RequiresPermissions("anlian:contractemplate:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contractemplateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示合同模板共同信息详情")
    @RequiresPermissions("anlian:contractemplate:info")
    public R info(@PathVariable("id") Long id){
        ContractemplateEntity contractemplate = contractemplateService.getById(id);

        return R.ok().put("contractemplate", contractemplate);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增合同模板共同信息")
    @ApiOperation("新增合同模板共同信息")
    @RequiresPermissions("anlian:contractemplate:save")
    public R save(@RequestBody ContractemplateEntity contractemplate){
        contractemplateService.save(contractemplate);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改合同模板共同信息")
    @ApiOperation("修改合同模板共同信息")
    @RequiresPermissions("anlian:contractemplate:update")
    public R update(@RequestBody ContractemplateEntity contractemplate){
        contractemplateService.updateById(contractemplate);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除合同模板共同信息") 
    @ApiOperation("删除合同模板共同信息") 
    @RequiresPermissions("anlian:contractemplate:delete")
    public R delete(@RequestBody Long[] ids){
        contractemplateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
