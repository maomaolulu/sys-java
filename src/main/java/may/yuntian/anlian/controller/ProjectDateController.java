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

import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.service.ProjectDateService;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@RestController
@Api(tags="项目日期相关表")
@RequestMapping("anlian/projectdate")
public class ProjectDateController {
    @Autowired
    private ProjectDateService projectDateService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询列表")
    @RequiresPermissions("anlian:project:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = projectDateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示详情")
    @RequiresPermissions("anlian:project:info")
    public R info(@PathVariable("id") Long id){
        ProjectDateEntity projectDate = projectDateService.getById(id);

        return R.ok().put("projectDate", projectDate);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增项目日期相关信息")
    @ApiOperation("新增")
    @RequiresPermissions("anlian:project:save")
    public R save(@RequestBody ProjectDateEntity projectDate){
        projectDateService.save(projectDate);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改项目日期相关信息")
    @ApiOperation("修改")
    @RequiresPermissions("anlian:project:update")
    public R update(@RequestBody ProjectDateEntity projectDate){
        projectDateService.updateById(projectDate);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除项目日期相关信息")
    @ApiOperation("删除") 
    @RequiresPermissions("anlian:project:delete")
    public R delete(@RequestBody Long[] ids){
        projectDateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
