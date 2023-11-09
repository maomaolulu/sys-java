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

import may.yuntian.anlian.entity.ProjectCountEntity;
import may.yuntian.anlian.service.ProjectCountService;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-07-25 14:15:40
 */
@RestController
@Api(tags="信息录入节点信息")
@RequestMapping("anlian/projectcount")
public class ProjectCountController {
    @Autowired
    private ProjectCountService projectCountService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询列表")
//    @RequiresPermissions("anlian:projectcount:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = projectCountService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示详情")
//    @RequiresPermissions("anlian:projectcount:info")
    public R info(@PathVariable("id") Long id){
        ProjectCountEntity projectCount = projectCountService.getById(id);

        return R.ok().put("projectCount", projectCount);
    }

    /**
     * 信息
     */
    @GetMapping("/getInfo/{projectId}")
    @ApiOperation("根据项目ID显示信息录入节点信息详情")
//    @RequiresPermissions("anlian:projectcount:info")
    public R getInfo(@PathVariable("projectId") Long projectId){
        ProjectCountEntity projectCount = projectCountService.getOneByProjectId(projectId);

        return R.ok().put("info", projectCount);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增")
    @ApiOperation("新增")
//    @RequiresPermissions("anlian:projectcount:save")
    public R save(@RequestBody ProjectCountEntity projectCount){
        projectCountService.save(projectCount);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改")
    @ApiOperation("修改")
//    @RequiresPermissions("anlian:projectcount:update")
    public R update(@RequestBody ProjectCountEntity projectCount){
        projectCountService.updateById(projectCount);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除") 
    @ApiOperation("删除") 
//    @RequiresPermissions("anlian:projectcount:delete")
    public R delete(@RequestBody Long[] ids){
        projectCountService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
