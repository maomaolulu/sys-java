package may.yuntian.modules.sys.controller;

import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@RestController
@Api(tags="数据字典")
@RequestMapping("sys/dict")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询数据字典")
    @RequiresPermissions("sys:dict:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    /**
     * 根据字典类型显示列表
     */
    @GetMapping("/listByType")
    @ApiOperation("根据字典类型显示列表")
//    @RequiresPermissions("sys:dict:list")
    public R listByType(@RequestParam String type){
    	List<SysDictEntity> list = sysDictService.listByType(type);
    	
    	return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示数据字典详情")
    @RequiresPermissions("sys:dict:info")
    public R info(@PathVariable("id") Long id){
        SysDictEntity dict = sysDictService.getById(id);

        return R.ok().put("dict", dict);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存数据字典")
    @RequiresPermissions("sys:dict:save")
    public R save(@RequestBody SysDictEntity dict){
        //校验类型
        //ValidatorUtils.validateEntity(dict);

        sysDictService.save(dict);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改数据字典")
    @RequiresPermissions("sys:dict:update")
    public R update(@RequestBody SysDictEntity dict){
        //校验类型
        //ValidatorUtils.validateEntity(dict);

        sysDictService.updateById(dict);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("根据ID数组批量删除数据字典")
    @RequiresPermissions("sys:dict:delete")
    public R delete(@RequestBody Long[] ids){
        sysDictService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
