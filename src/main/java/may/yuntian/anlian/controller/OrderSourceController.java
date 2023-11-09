package may.yuntian.anlian.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import may.yuntian.common.annotation.SysLog;
import may.yuntian.anlian.entity.OrderSourceEntity;
import may.yuntian.anlian.service.OrderSourceService;


/**
 *项目隶属来源表
 * WEB请求处理层
 *
 * @author LiXin
 * @data 2021-03-22
 */
@RestController
@Api(tags="项目隶属来源信息")
@RequestMapping("anlian/orderSource")
public class OrderSourceController {
    @Autowired
    private OrderSourceService orderSourceService;

    
	/**
	 *获取项目隶属 type为3的列表
	 */
    @GetMapping("/getOrderList")
    @ApiOperation("获取项目隶属 type为3的列表")
//    @RequiresPermissions("anlian:orderSource:list")
    public R getOrderList(){
       List<OrderSourceEntity> orderList = orderSourceService.getOrderList();

        return R.ok().put("orderList", orderList);
    }
    
	/**
	 *获取业务来源 type为4的列表
	 */
    @GetMapping("/getSourceListList")
    @ApiOperation("获取业务来源 type为4的列表")
//    @RequiresPermissions("anlian:orderSource:list")
    public R getSourceListList(){
       List<OrderSourceEntity> sourceList = orderSourceService.getSourceListList();

        return R.ok().put("sourceList", sourceList);
    }
    

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示项目隶属来源信息详情")
    @RequiresPermissions("anlian:orderSource:info")
    public R info(@PathVariable("id") Long id){
    	OrderSourceEntity orderSource = orderSourceService.getById(id);

        return R.ok().put("orderSource", orderSource);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增职业项目隶属来源信息")
    @ApiOperation("新增项目隶属来源信息")
    @RequiresPermissions("anlian:orderSource:save")
    public R save(@RequestBody OrderSourceEntity orderSource){
    	orderSourceService.save(orderSource);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改项目隶属来源信息")
    @ApiOperation("修改项目隶属来源信息")
    @RequiresPermissions("anlian:orderSource:update")
    public R update(@RequestBody OrderSourceEntity orderSource){
    	orderSourceService.updateById(orderSource);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除项目隶属来源信息") 
    @ApiOperation("删除项目隶属来源信息") 
    @RequiresPermissions("anlian:orderSource:delete")
    public R delete(@RequestBody Long[] ids){
    	orderSourceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
