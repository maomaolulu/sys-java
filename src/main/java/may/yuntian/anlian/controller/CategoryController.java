package may.yuntian.anlian.controller;

import java.util.Arrays;
import java.util.List;

import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.CategoryEntity;
import may.yuntian.anlian.service.CategoryService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;

/**
 *类型信息记录
 * @author LiXin
 * @date 2020-12-10
 */

@RestController
@Api(tags="类型信息记录")
@RequestMapping("anlian/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
    /**
     * 显示全部事业部业绩目标信息列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("显示全部类型信息记录列表")
//    @RequiresPermissions("anlian:category:list")
    public R listAll(){
        List<CategoryEntity> list = categoryService.listAll();
       

        return R.ok().put("list", list);
    }
    
    /**
     * 根据模块获取收付款记录列表
     */
    @GetMapping("/listByModule/{module}")
    @ApiOperation("根据模块获取类型信息记录列表")
//  @RequiresPermissions("anlian:category:list")
    public R listByModule(@PathVariable("module") String module){
    	List<CategoryEntity> list = categoryService.listByModule(module);
    	
    	return R.ok().put("list", list);
    }
    
    /**
	 * 通过模块查询出没有子集的子集列表
	 * @param module
	 * @return
	 */
	@GetMapping("/getList/{module}")
	@ApiOperation("根据模块获取类型信息记录列表")
//	@RequiresPermissions("anlian:category:list")
	public R getList(@PathVariable("module") String module){
		List<CategoryEntity> list = categoryService.getList(module);
		
		return R.ok().put("list", list);
	}
    
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示类型信息记录详情")
//	@RequiresPermissions("anlian:category:info")
	public R info(@PathVariable("id") Long id) {
		CategoryEntity category = categoryService.getById(id);

		return R.ok().put("category", category);
	}
	
	
    

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增类型信息记录")
    @ApiOperation("新增类型信息记录")
//    @RequiresPermissions("anlian:category:save")
    public R save(@RequestBody CategoryEntity category){
    	categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改类型信息记录")
    @ApiOperation("修改类型信息记录")
//    @RequiresPermissions("anlian:category:update")
    public R update(@RequestBody CategoryEntity category){
    	categoryService.updateById(category);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除类型信息记录") 
    @ApiOperation("删除类型信息记录") 
//    @RequiresPermissions("anlian:category:delete")
    public R delete(@RequestBody Long[] ids){
    	categoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    
    /**
     * 获取无子集列表
     * @param module
     * @return
     */
	@GetMapping("/getListByPid/{module}")
	@ApiOperation("根据模块获取类型信息记录列表")
//	@RequiresPermissions("anlian:category:list")
	public R getListByPid(@PathVariable("module") String module){
	    String subjection = ShiroUtils.getUserEntity().getSubjection();
	    String companyEn = "";
		List<CategoryEntity> list = categoryService.getListByPid(module);
		switch (subjection){
            case "宁波安联":
                companyEn = "NB";
                break;
            case "嘉兴安联":
                companyEn = "JX";
                break;
            case "上海量远":
                companyEn = "GS";
                break;
            case "上海研晰":
                companyEn = "YX";
                break;
            case "杭州亿达":
                companyEn = "YD";
                break;
            case "金华职康":
                companyEn = "ZK";
                break;
            default:
                break;
        }
		return R.ok().put("list", list).put("companyEn",companyEn);
	}

}
