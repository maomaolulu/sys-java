package may.yuntian.external.express.sf.constant;

/**
 * SF：接口服务码常量
 *
 * @author: liyongqiang
 * @create: 2023-05-24 14:29
 */
public class ServiceCodeConstants {

    /**
     * 下单服务名称
     **/
    public static final String CREATE_ORDER = "EXP_RECE_CREATE_ORDER";
    /**
     * 订单结果查询服务名称
     **/
    public static final String SEARCH_ORDER = "EXP_RECE_SEARCH_ORDER_RESP";
    /**
     * 订单取消服务名称
     **/
    public static final String ABOLISH_ORDER = "EXP_RECE_UPDATE_ORDER";
    /**
     * 路由查询服务名称
     **/
    public static final String SEARCH_ROUTES = "EXP_RECE_SEARCH_ROUTES";
    /**
     * 路由注册服务名称
     **/
    public static final String REGISTER_ROUTE = "EXP_RECE_REGISTER_ROUTE";
    /**
     * 派件通知接口名称
     **/
    public static final String DELIVERY_NOTICE = "EXP_RECE_DELIVERY_NOTICE";
    /**
     * 云打印面单接口2.0名称
     **/
    public static final String CLOUD_PRINT_WAYBILLS = "COM_RECE_CLOUD_PRINT_WAYBILLS";
    /**
     * 路由上传接口名称
     **/
    public static final String UPLOAD_ROUTE = "EXP_RECE_UPLOAD_ROUTE";
    /**
     * 预计派送时间查询接口名称
     **/
    public static final String SEARCH_PROMITM = "EXP_RECE_SEARCH_PROMITM";
    /**
     * 揽件服务时间查询接口名称
     **/
    public static final String CHECK_PICKUP_TIME = "EXP_EXCE_CHECK_PICKUP_TIME";
    /**
     * 运单号合法性校验接口名称
     **/
    public static final String VALIDATE_WAYBILLNO = "EXP_RECE_VALIDATE_WAYBILLNO";

}
