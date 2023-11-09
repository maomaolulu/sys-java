package may.yuntian.external.express.sf.controller;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.express.sf.pojo.dto.Route;
import may.yuntian.external.express.sf.pojo.vo.ManualOrderVo;
import may.yuntian.external.express.sf.pojo.vo.OrderListVo;
import may.yuntian.external.express.sf.service.BusinessOrderService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 物流-顺丰controller
 * @author: liyongqiang
 * @create: 2023-05-25 11:26
 */
@RestController
@Api(tags="SF-快递")
@RequestMapping("/external/sf")
public class BusinessOrderController {

    @Autowired
    private BusinessOrderService businessOrderService;


    /**
     * 手工建单：初始化数据，生成订单编号，保存。
     */
    @PostMapping("/manual")
    @ApiOperation("手工建单-保存")
    @SysLog("SF-手工建单日志")
    public Result manualOrder(@RequestBody @ApiParam(name = "manualOrderVo", value = "json格式", required = true) ManualOrderVo manualOrderVo) {
        businessOrderService.manualOrder(manualOrderVo);
        return Result.ok("创建成功！");
    }

    /**
     * 订单列表
     */
    @ApiOperation("SF订单列表：已分页")
    @GetMapping("/unplaced")
    public Result unplacedOrderList(OrderListVo searchVo) {
        return Result.resultData(businessOrderService.unplacedOrderList(searchVo));
    }

    /**
     * 查看or编辑
     */
    @ApiOperation("订单列表：查看/编辑")
    @GetMapping("/view")
    public Result viewOrEdit(Long id) {
        return Result.ok("查询成功！", businessOrderService.viewOrEdit(id));
    }

    /**
     * 编辑-保存
     */
    @ApiOperation("订单列表：编辑后保存")
    @PostMapping("/save")
    @SysLog("SF-订单列表，编辑保存日志")
    public Result editSave(@RequestBody OrderListVo orderListVo) {
        businessOrderService.editSave(orderListVo);
        return Result.ok();
    }

    /**
     * 已取消：（批量）删除
     */
    @ApiOperation("已取消-删除")
    @GetMapping("/delete")
    @SysLog("SF-订单取消后删除日志")
    public Result batchDelete(Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            throw new RRException("请至少选择一条需要操作的数据");
        }
        businessOrderService.batchDeleteOrder(ids);
        return Result.ok("删除成功！");
    }

    /**
     * sf-寄件下单：得到运单号、路由信息
     */
    @ApiOperation("寄件下单")
    @GetMapping("/create")
    @SysLog("SF-寄件下单日志")
    public Result createOrder(Long id) {
        String message = businessOrderService.createOrderSF(id);
        return message.contains("运单号") ? Result.ok(message) : Result.error(message);
    }

    /**
     * sf-订单取消
     */
    @ApiOperation("订单取消")
    @GetMapping("/abolish")
    @SysLog("SF-订单取消日志")
    public Result orderAbolish(@ApiParam(value = "订单编号", required = true) String orderId) {
        String result = businessOrderService.orderAbolish(orderId);
        return result.contains("success") ? Result.ok("取消成功！") : Result.error("取消失败：".concat(result));
    }

    /**
     * sf-订单结果查询:
     *     因Internet环境下，网络不是绝对可靠，用户系统下订单到顺丰后，不一定可以收到顺丰系统返回的数据，此接口用于在未收到返回数据时，查询订单创建接口客户订单当前的处理情况。
     */
    @ApiOperation("订单结果查询")
    @GetMapping("/search")
    public Result orderResultSearch(@ApiParam(value = "订单编号", required = true) String orderId ) {
        return Result.ok(businessOrderService.orderResultSearch(orderId));
    }

    /**
     * 路由查询
     */
    @ApiOperation("根据订单编号查询路由信息")
    @GetMapping("/route/query")
    public Result routeQueryResult(String orderId) {
        List<Route> routes = businessOrderService.routeQueryResult(orderId);
        return Result.ok("查询成功！", routes);
    }

    /**
     * 订单状态推送
     */
    @ApiOperation("SF-订单状态推送回调接口")
    @PostMapping("/order/status/back")
    //@SysLog("SF-订单状态推送回调日志")
    public Map<String, String> orderStatusBack(@RequestBody String content) {
        return businessOrderService.orderStatusBack(content);
    }

    /**
     * 路由状态推送
     */
    @ApiOperation("SF-路由推送回调接口")
    @PostMapping("/order/route/push")
    //@SysLog("SF-路由推送回调日志")
    public Map<String, String> routeBack(@RequestBody String content) {
        return businessOrderService.routeBack(content);
    }

    /**
     * 云打印面单打印2.0接口-面单类API：(Post请求，Body体form-data)
     * @param waybillNo 运单号
     * @param remark 自定义备注
     * @return map（返回：pdf版面单url和访问token）
     */
    @PostMapping("/print")
    @ApiOperation("打印")
    @SysLog("SF-面单打印日志")
    public Result printExpressSheet(@ApiParam(value = "运单号", required = true) String waybillNo, @ApiParam(value = "备注", required = true) String remark) {
        return Result.ok(businessOrderService.printExpressSheet(waybillNo, remark));
    }

    /**
     * 待揽件导出excel
     */
    @ApiOperation("待揽件导出")
    @PostMapping("/wait/export")
    @SysLog("SF-待揽件导出日志")
    public void waitExport(@RequestBody OrderListVo orderListVo, HttpServletResponse response) {
        businessOrderService.waitExport(response, orderListVo);
    }

    /**
     * 预计派送时间查询
     */
    @ApiOperation("根据运单号查询预计派送时间")
    @GetMapping("/search/promitm")
    public Result predictDeliveryTime(String waybillNo) {
        return Result.ok(businessOrderService.predictDeliveryTime(waybillNo));
    }

}
