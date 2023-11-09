package may.yuntian.anlian.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.service.CompanyContactService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;

/**
 *公司联系人管理
 * @author LiXin
 * @date 2020-11-28
 */

@RestController
@Api(tags="公司联系人管理")
@RequestMapping("anlian/companyContact")
public class CompanyContactController {
	@Autowired
	private CompanyContactService companyContactService;
	
	/**
	 * 显示全部列表
	 * 
	 * @return
	 */
	@GetMapping("/listAll")
	@ApiOperation("显示全部公司联系人列表")
	public R listAll() {
		List<CompanyContactEntity> list = companyContactService.list();

		return R.ok().put("list", list);
	}
    

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增公司联系人")
    @ApiOperation("新增公司联系人")
    @RequiresPermissions("anlian:company:save")
    public R save(@RequestBody CompanyContactEntity CompanyContact){
    	companyContactService.save(CompanyContact);

        return R.ok();
    }
    
    /**
     * 批量保存或修改公司联系人
     */
    @PostMapping("/saveOrUpdateBatch")
    @SysLog("批量保存或修改公司联系人")
    @ApiOperation("批量保存或修改公司联系人")
    @RequiresPermissions("anlian:company:save")
    public R saveOrUpdateBatch(@RequestBody List<CompanyContactEntity> companyContactList){

    	companyContactService.saveOrUpdateBatch(companyContactList);
    	
    	return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改公司联系人")
    @ApiOperation("修改公司联系人")
    @RequiresPermissions("anlian:company:update")
    public R update(@RequestBody CompanyContactEntity CompanyContact){		
    	companyContactService.updateById(CompanyContact);
        
        return R.ok();
    }

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除公司联系人")
	@ApiOperation("删除公司联系人")
	@RequiresPermissions("anlian:company:delete")
	public R delete(@RequestBody Long[] ids) {
		companyContactService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
