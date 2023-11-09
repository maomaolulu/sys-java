package may.yuntian.anlian.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.ProjectProceduresEntity;
import may.yuntian.anlian.service.ProjectProceduresService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;

/**
 *项目流程管理
 * @author LiXin
 * @date 2020-12-02
 */

@RestController
@Api(tags="项目流程管理")
@RequestMapping("anlian/projectProcedures")
public class ProjectProceduresController {
	@Autowired
	private ProjectProceduresService projectProceduresService;
	
    /**
     * 根据项目ID获取项目流程列表
     */
    @GetMapping("/listProceduresByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取收付款记录列表")
    @RequiresPermissions("anlian:projectProcedures:list")
    public R listProceduresByProjectId(@PathVariable("projectId") Long projectId){
    	List<ProjectProceduresEntity> list = projectProceduresService.listProceduresByProjectId(projectId);;
    	
    	return R.ok().put("list", list);
    }
    
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示项目流程详情")
	@RequiresPermissions("anlian:projectProcedures:info")
	public R info(@PathVariable("id") Long id) {
		ProjectProceduresEntity projectProcedures = projectProceduresService.getById(id);

		return R.ok().put("projectProcedures", projectProcedures);
	}
    

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增项目流程")
    @ApiOperation("新增项目流程")
    @RequiresPermissions("anlian:projectProcedures:save")
    public R save(@RequestBody ProjectProceduresEntity projectProcedures){
    	projectProceduresService.save(projectProcedures);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改项目流程")
    @ApiOperation("修改项目流程")
    @RequiresPermissions("anlian:projectProcedures:update")
    public R update(@RequestBody ProjectProceduresEntity projectProcedures){		
    	projectProceduresService.updateById(projectProcedures);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除项目流程") 
    @ApiOperation("删除项目流程") 
    @RequiresPermissions("anlian:projectProcedures:delete")
    public R delete(@RequestBody Long[] ids){
    	projectProceduresService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
