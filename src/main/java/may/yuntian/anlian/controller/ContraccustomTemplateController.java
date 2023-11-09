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

import may.yuntian.anlian.entity.ContraccustomTemplateEntity;
import may.yuntian.anlian.service.ContraccustomTemplateService;


/**
 * 合同模板自定义字段管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@RestController
@Api(tags="合同模板自定义字段")
@RequestMapping("anlian/contraccustomtemplate")
public class ContraccustomTemplateController {
    @Autowired
    private ContraccustomTemplateService contraccustomTemplateService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询合同模板自定义字段列表")
    @RequiresPermissions("anlian:contraccustomtemplate:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contraccustomTemplateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示合同模板自定义字段详情")
    @RequiresPermissions("anlian:contraccustomtemplate:info")
    public R info(@PathVariable("id") Long id){
        ContraccustomTemplateEntity contraccustomTemplate = contraccustomTemplateService.getById(id);

        return R.ok().put("contraccustomTemplate", contraccustomTemplate);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增合同模板自定义字段")
    @ApiOperation("新增合同模板自定义字段")
    @RequiresPermissions("anlian:contraccustomtemplate:save")
    public R save(@RequestBody ContraccustomTemplateEntity contraccustomTemplate){
        contraccustomTemplateService.save(contraccustomTemplate);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改合同模板自定义字段")
    @ApiOperation("修改合同模板自定义字段")
    @RequiresPermissions("anlian:contraccustomtemplate:update")
    public R update(@RequestBody ContraccustomTemplateEntity contraccustomTemplate){
        contraccustomTemplateService.updateById(contraccustomTemplate);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除合同模板自定义字段") 
    @ApiOperation("删除合同模板自定义字段") 
    @RequiresPermissions("anlian:contraccustomtemplate:delete")
    public R delete(@RequestBody Long[] ids){
        contraccustomTemplateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
