package may.yuntian.external.express.sf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.express.sf.pojo.dto.Route;
import may.yuntian.external.express.sf.pojo.entity.BusinessOrder;
import may.yuntian.external.express.sf.pojo.vo.ManualOrderVo;
import may.yuntian.external.express.sf.pojo.vo.OrderListVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-05-25 11:23
 */
public interface BusinessOrderService extends IService<BusinessOrder> {

    /**
     * SF-寄件下单
     * @param businessOrderId
     * @return
     */
    String createOrderSF(Long businessOrderId);

    /**
     * 手工建单
     * @param manualOrderVo
     * @return
     */
    int manualOrder(ManualOrderVo manualOrderVo);

    /**
     * 未下单/已取消：列表
     * @param searchVo 筛选条件
     * @return list
     */
    List<OrderListVo> unplacedOrderList(OrderListVo searchVo);

    /**
     * 根据id查询
     * @param id exp_business_order表id
     * @return
     */
    OrderListVo viewOrEdit(Long id);

    /**
     * 未下单：编辑-保存
     * @param orderListVo
     * @return
     */
    int editSave(OrderListVo orderListVo);

    /**
     * 已取消：删除
     * @param ids exp_business_order表ids
     * @return
     */
    int batchDeleteOrder(Long[] ids);

    /**
     * 订单取消
     * @param orderId 客户订单编号
     * @return 响应信息
     */
    String orderAbolish(String orderId);

    /**
     * 订单结果查询
     * @param orderId 客户订单编号
     * @return 响应的信息
     */
    String orderResultSearch(String orderId);

    /**
     * 根据订单编号查询路由信息
     * @param orderId 订单编号
     * @return 全部路由信息
     */
    List<Route> routeQueryResult(String orderId);

    /**
     * 云打印面单打印2.0接口
     * @param waybillNo 运单号
     * @param remark 备注
     * @return
     */
    Map<String, Object> printExpressSheet(String waybillNo, String remark);

    /**
     * 路由状态推送回调
     * @param content SF-json请求报文
     * @return map
     */
    Map<String, String> routeBack(String content);

    /**
     * 待揽件导出excel
     * @param response
     * @param orderListVo
     */
    void waitExport(HttpServletResponse response, OrderListVo orderListVo);

    /**
     * 订单状态推送回调
     * @param content SF-json请求报文
     * @return map
     */
    Map<String, String> orderStatusBack(String content);

    /**
     * 根据运单号查询预计派送时间
     * @param waybillNo 运单号
     * @return str
     */
    String predictDeliveryTime(String waybillNo);
}
