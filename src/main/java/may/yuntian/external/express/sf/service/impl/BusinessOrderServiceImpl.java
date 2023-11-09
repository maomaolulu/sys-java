package may.yuntian.external.express.sf.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import may.yuntian.anlian.utils.AnlianConfig;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.express.sf.constant.SFConstants;
import may.yuntian.external.express.sf.constant.ServiceCodeConstants;
import may.yuntian.external.express.sf.mapper.BusinessOrderMapper;
import may.yuntian.external.express.sf.mapper.ContactInfoMapper;
import may.yuntian.external.express.sf.pojo.dto.*;
import may.yuntian.external.express.sf.pojo.entity.BusinessOrder;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;
import may.yuntian.external.express.sf.pojo.entity.OrderStatusBack;
import may.yuntian.external.express.sf.pojo.vo.ManualOrderVo;
import may.yuntian.external.express.sf.pojo.vo.OrderListVo;
import may.yuntian.external.express.sf.service.BusinessOrderService;
import may.yuntian.external.express.sf.service.ContactInfoService;
import may.yuntian.external.express.sf.service.OrderStatusBackService;
import may.yuntian.external.express.sf.util.CallExpressServiceTools;
import may.yuntian.external.express.sf.util.HttpClientUtil;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-05-25 11:25
 */
@Service("businessOrderService")
public class BusinessOrderServiceImpl extends ServiceImpl<BusinessOrderMapper, BusinessOrder> implements BusinessOrderService {

    private static final Logger log = LoggerFactory.getLogger(BusinessOrderServiceImpl.class);

    @Autowired
    private AnlianConfig config;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ContactInfoMapper contactInfoMapper;
    @Autowired
    private ContactInfoService contactInfoService;
    @Autowired
    private OrderStatusBackService orderStatusBackService;


    /**
     * 手工建单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int manualOrder(ManualOrderVo manualOrderVo) {
        BusinessOrder businessOrder = manualOrderVo.getBusinessOrder();
        List<ContactInfo> contactInfoList = manualOrderVo.getContactInfoList();
        contactInfoList.forEach(contactInfo -> {
            contactInfo.setCreateTime(DateUtil.dateSecond());
            contactInfo.setCreateBy(ShiroUtils.getUserEntity().getUsername());
            if (contactInfo.getContactType() == 1) contactInfo.setSendCommon(0);
        });
        contactInfoService.saveBatch(contactInfoList);
        for (ContactInfo contactInfo : contactInfoList) {
            if (contactInfo.getContactType() == 1) {
                businessOrder.setSendInfoId(contactInfo.getId());
            } else {
                businessOrder.setReceiveInfoId(contactInfo.getId());
            }
        }
        businessOrder.setStatus(1);
        businessOrder.setCreateTime(DateUtil.dateSecond());
        businessOrder.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        int rows = baseMapper.insert(businessOrder);
        // 未发生异常时更新redis，并生成客户订单号
        if (rows > 0) {
            BusinessOrder order = new BusinessOrder();
            order.setId(businessOrder.getId());
            order.setOrderId(generateOrderId());
            baseMapper.updateById(order);
        }
        return rows;
    }

    /**
     * 客户订单号生成
     */
    private String generateOrderId() {
        // 类锁。多线程访问互斥
        synchronized (BusinessOrderServiceImpl.class) {
            String cacheKey = "HZAL".concat(DateUtil.today().replace("-", ""));
            int codeNum = 0;
            Object value = redisTemplate.opsForValue().get(cacheKey);
            if (ObjectUtil.isNull(value)) {
                codeNum = codeNum + 1;
            } else {
                assert value != null;
                codeNum = Integer.parseInt((String) value) + 1;
            }
            // 添加缓存，过期时间（当天）
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(codeNum), getSeconds(), TimeUnit.SECONDS);
            // 客户订单号 = cacheKey + codeNum = 前缀 + 日期 + 流水号 （ex：HZAL202305260001）
            return cacheKey.concat(String.format("%04d", codeNum));
        }
    }

    /**
     * 计算当天离结束还剩多少秒
     */
    private static int getSeconds() {
        // 获取今天当前时间
        Calendar curDate = Calendar.getInstance();
        // 获取明天凌晨0点的日期
        GregorianCalendar tomorrowDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
        //返回 两时间差值（秒）
        return (int) (tomorrowDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
    }


    /**
     * 订单列表
     */
    @Override
    public List<OrderListVo> unplacedOrderList(OrderListVo searchVo) {
        QueryWrapper<OrderListVo> wrapper = new QueryWrapper<>();
        // 根据订单编号或运单号模糊查询（orderId用于接收前端传来的单号）
        wrapper.like(StrUtil.isNotBlank(searchVo.getOrderId()), "ebo.order_id", searchVo.getOrderId());
        wrapper.or();
        wrapper.like(StrUtil.isNotBlank(searchVo.getOrderId()), "ebo.waybill_no", searchVo.getOrderId());
        wrapper.eq(ObjectUtil.isNotNull(searchVo.getStatus()), "ebo.status", searchVo.getStatus());
        wrapper.like(StrUtil.isNotBlank(searchVo.getReceiveContact()), "eci.contact", searchVo.getReceiveContact());
        wrapper.like(StrUtil.isNotBlank(searchVo.getReceiveMobile()), "eci.mobile", searchVo.getReceiveMobile());
        wrapper.like(StrUtil.isNotBlank(searchVo.getReceiveCompany()), "eci.company", searchVo.getReceiveCompany());
        wrapper.like(StrUtil.isNotBlank(searchVo.getSendContact()), "t.contact", searchVo.getSendContact());
        wrapper.like(StrUtil.isNotBlank(searchVo.getSendMobile()), "t.mobile", searchVo.getSendMobile());
        wrapper.like(StrUtil.isNotBlank(searchVo.getSendCompany()), "t.company", searchVo.getSendCompany());
        wrapper.ge(ObjectUtil.isNotNull(searchVo.getOrderStartTime()), "ebo.order_create_time", searchVo.getOrderStartTime());
        wrapper.le(ObjectUtil.isNotNull(searchVo.getOrderEndTime()), "ebo.order_create_time", searchVo.getOrderEndTime());
        wrapper.orderByAsc("ebo.status");
        pageUtil2.startPage();
        return baseMapper.getOrderListVos(wrapper);
    }

    /**
     * 未下单/已取消：查看or编辑
     */
    @Override
    public OrderListVo viewOrEdit(Long id) {
        return baseMapper.getOrderListVos(new QueryWrapper<OrderListVo>().eq(ObjectUtil.isNotNull(id), "ebo.id", id)).get(0);
    }

    /**
     * 未下单：编辑-保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editSave(OrderListVo orderListVo) {
        // 收件人信息直接update
        ContactInfo receiveInfo = new ContactInfo();
        receiveInfo.setId(orderListVo.getReceiveInfoId());
        receiveInfo.setCompany(orderListVo.getReceiveCompany());
        receiveInfo.setContact(orderListVo.getReceiveContact());
        receiveInfo.setMobile(orderListVo.getReceiveMobile());
        receiveInfo.setTelephone(orderListVo.getReceiveTelephone());
        receiveInfo.setProvince(orderListVo.getReceiveProvince());
        receiveInfo.setCity(orderListVo.getSendCity());
        receiveInfo.setCountry(orderListVo.getReceiveCounty());
        receiveInfo.setAddress(orderListVo.getReceiveAddress());
        receiveInfo.setUpdateTime(DateUtil.dateSecond());
        receiveInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
        contactInfoMapper.updateById(receiveInfo);
        // 寄件人信息新增，改sendInfoId
        ContactInfo sendInfo = new ContactInfo();
        sendInfo.setContactType(1);
        sendInfo.setCompany(orderListVo.getSendCompany());
        sendInfo.setContact(orderListVo.getSendContact());
        sendInfo.setMobile(orderListVo.getSendMobile());
        sendInfo.setTelephone(orderListVo.getSendTelephone());
        sendInfo.setProvince(orderListVo.getSendProvince());
        sendInfo.setCity(orderListVo.getSendCity());
        sendInfo.setCountry(orderListVo.getReceiveCounty());
        sendInfo.setAddress(orderListVo.getSendAddress());
        sendInfo.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        sendInfo.setCreateTime(DateUtil.dateSecond());
        contactInfoMapper.insert(sendInfo);
        // 更新业务表
        orderListVo.setSendInfoId(sendInfo.getId());
        baseMapper.updateById(orderListVo);
        return 1;
    }

    /**
     * 已取消：（批量）删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteOrder(Long[] ids) {
        // 先批量删除收件人
        List<BusinessOrder> businessOrderList = baseMapper.selectList(new LambdaQueryWrapper<BusinessOrder>().select(BusinessOrder::getReceiveInfoId).in(BusinessOrder::getId, (Object) ids));
        if (CollUtil.isNotEmpty(businessOrderList)) {
            List<Integer> receiveIds = businessOrderList.stream().map(BusinessOrder::getReceiveInfoId).collect(Collectors.toList());
            contactInfoService.removeByIds(receiveIds);
        }
        // 再批量删除业务主表
        this.removeByIds(Arrays.asList(ids));
        return 1;
    }


    /**
     * SF-寄件下单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrderSF(Long businessOrderId) {
        StringBuilder builder = new StringBuilder();
        Map<String, String> params = Maps.newHashMap();
        BusinessOrder businessOrder = baseMapper.selectById(businessOrderId);
        // 1.寄件下单dto
        SpeedOrderDto speedOrderDto = new SpeedOrderDto();
        // 托寄物信息
        List<CargoDetail> cargoDetails = new ArrayList<>();
        cargoDetails.add(new CargoDetail(businessOrder.getName(), businessOrder.getCount(), businessOrder.getAmount(), businessOrder.getGoodsCode()));
        // 收寄双方信息
        List<ContactInfo> contactInfoList = contactInfoMapper.selectList(Wrappers.lambdaQuery(ContactInfo.class).in(ContactInfo::getId, businessOrder.getReceiveInfoId(), businessOrder.getSendInfoId()));
        // data封装
        speedOrderDto.setLanguage("zh-CN");
        speedOrderDto.setOrderId(businessOrder.getOrderId());
        speedOrderDto.setCargoDetails(cargoDetails);
        speedOrderDto.setContactInfoList(contactInfoList);
        speedOrderDto.setMonthlyCard(config.monthlyCard);
        speedOrderDto.setPayMethod(businessOrder.getPayMethod() > 1 ? businessOrder.getPayMethod() : 1);
        speedOrderDto.setExpressTypeId(businessOrder.getExpressTypeId());
        speedOrderDto.setIsDocall(1);
        // 上门取件时间下午18:00:00 ~ 19:00:00      格式：YYYY-MM-DD HH24:MM:SS
        speedOrderDto.setSendStartTm(DateUtil.today().concat(" 18:00:00"));
        speedOrderDto.setParcelQty(businessOrder.getParcelQty());
        speedOrderDto.setIsReturnRoutelabel(1);
        speedOrderDto.setRemark(businessOrder.getRemark());
        // 2.dto对象 ---> json字符串
        String msgData = JSONUtil.toJsonStr(speedOrderDto);
        log.info("SF下订单msgData：" + "\n" + JSONUtil.toJsonPrettyStr(speedOrderDto));
        // 3.set common header
        String result = "";
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            params.put("partnerID", config.clientCode);
            params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
            params.put("serviceCode", ServiceCodeConstants.CREATE_ORDER);
            params.put("timestamp", timeStamp);
            params.put("msgData", msgData);
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, config.checkWord));
            // 4.调用SF-下订单接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF寄件下单异常：【" + e.getMessage() + "】请联系管理员！");
        }
        // 5.返回信息处理
        String[] split = result.split("apiResultData");
        String jsonString = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF下订单返回结果：\n" + JSONUtil.toJsonPrettyStr(jsonString));
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        // A1000：表示接口调用正常（统一接入平台校验成功，调用后端服务成功; // 注意：不代表后端业务处理成功，实际业务处理结果，需要查看响应属性apiResultData中的详细结果。）
        if (SFConstants.SUCCESS.equals(jsonObject.getStr(SFConstants.RESULT_CODE))) {
            JSONObject apiResultData = jsonObject.getJSONObject("apiResultData");
            // errorCode：S0000下单成功
            if (SFConstants.ORDER_SUCCESS.equals(apiResultData.getStr(SFConstants.ERROR_CODE))) {
                JSONObject responseMsgData = apiResultData.getJSONObject("msgData");
                JSONArray jsonArray = responseMsgData.getJSONArray("waybillNoInfoList");
                List<WaybillNoInfo> waybillNoInfoList = JSONUtil.toList(jsonArray, WaybillNoInfo.class);
                // 保存运单号、订单创建时间、运单生成时间，orderStatus = 2待揽件
                String waybillNo = waybillNoInfoList.get(0).getWaybillNo();
                baseMapper.updateById(new BusinessOrder(businessOrderId, waybillNo, 2, DateUtil.dateSecond(), DateUtil.dateSecond(), ShiroUtils.getUserEntity().getUsername(), DateUtil.dateSecond()));
                builder.append("下单成功，SF运单号：").append(waybillNo);
            } else { // 下单失败，返回相应错误信息
                builder.append("下单失败：").append(apiResultData.getStr(SFConstants.ERROR_CODE)).append(apiResultData.getStr(SFConstants.ERROR_MSG));
            }
        }
        else {
            builder.append("SF-下单接口调用异常：").append(jsonObject.getStr(SFConstants.ERROR_CODE)).append(jsonObject.getStr(SFConstants.ERROR_MSG));
        }
        return builder.toString();
    }

    /**
     * 订单取消
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderAbolish(String orderId) {
        StringBuilder builder = new StringBuilder();
        // 1.msgData
        Map<String, Object> msgDataMap = Maps.newHashMap();
        // 客户订单操作标识: 1:确认 (丰桥下订单接口默认自动确认，不需客户重复确认，该操作用在其它非自动确认的场景) 2:取消
        msgDataMap.put("dealType", 2);
        // 响应报文的语言， 缺省值为zh-CN，目前支持以下值zh-CN 表示中文简体， zh-TW或zh-HK或 zh-MO表示中文繁体， en表示英文
        msgDataMap.put("language", "zh-CN");
        msgDataMap.put("orderId", orderId);
        String msgData = JSONUtil.toJsonStr(msgDataMap);
        // 2.请求参数
        Map<String, String> params = Maps.newHashMap();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        params.put("partnerID", config.clientCode);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", ServiceCodeConstants.ABOLISH_ORDER);
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        String result = "";
        try {
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, config.checkWord));
            // 3.调SF-订单取消接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF订单取消异常：【" + e.getMessage() + "】请联系管理员！");
        }
        // 4.响应信息处理
        String[] split = result.split("apiResultData");
        String jsonString = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF-订单取消返回结果：\n" + JSONUtil.toJsonPrettyStr(jsonString));
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        if (SFConstants.SUCCESS.equals(jsonObject.getStr(SFConstants.RESULT_CODE))) {
            JSONObject apiResultData = jsonObject.getJSONObject("apiResultData");
            if (SFConstants.ORDER_SUCCESS.equals(apiResultData.getStr(SFConstants.ERROR_CODE))) {
                builder.append("success");
                // 更新状态
                BusinessOrder businessOrder = new BusinessOrder();
                businessOrder.setStatus(0);
                businessOrder.setOrderCancelTime(DateUtil.dateSecond());
                businessOrder.setUpdateTime(DateUtil.dateSecond());
                businessOrder.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                baseMapper.update(businessOrder, Wrappers.lambdaUpdate(BusinessOrder.class).eq(StrUtil.isNotBlank(orderId), BusinessOrder::getOrderId, orderId));
            } else {
                builder.append(apiResultData.getStr(SFConstants.ERROR_CODE)).append(apiResultData.getStr(SFConstants.ERROR_MSG));
            }
        }
        return builder.toString();
    }

    /**
     * 订单结果查询
     */
    @Override
    public String orderResultSearch(String orderId) {
        // 1.msgData
        Map<String, Object> msgDataMap = Maps.newHashMap();
        // 查询类型：1正向单 2退货单
        msgDataMap.put("searchType", 1);
        msgDataMap.put("orderId", orderId);
        String msgData = JSONUtil.toJsonStr(msgDataMap);
        // 2.请求参数
        Map<String, String> params = Maps.newHashMap();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        params.put("partnerID", config.clientCode);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", ServiceCodeConstants.SEARCH_ORDER);
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        String result = "";
        try {
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, config.checkWord));
            // 3.调SF-订单结果查询接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF订单结果查询异常：【" + e.getMessage() + "】请联系管理员！");
        }
        // 4.响应信息处理
        String[] split = result.split("apiResultData");
        String jsonString = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF-订单查询结果：\n" + JSONUtil.toJsonPrettyStr(jsonString));

        // Todo：懒得处理，让前端直接将查询的结果放text文本框里！

        return JSONUtil.toJsonPrettyStr(jsonString);
    }

    /**
     * 根据订单编号查询全部路由信息
     */
    @Override
    public List<Route> routeQueryResult(String orderId) {
        // 1.msgData
        Map<String, Object> msgDataMap = Maps.newHashMap();
        msgDataMap.put("orderId", orderId);
        msgDataMap.put("trackingType", 2);
        msgDataMap.put("methodType", 1);
        msgDataMap.put("trackingNumber", Collections.singletonList(orderId));
        // 2.请求参数
        Map<String, String> params = Maps.newHashMap();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        params.put("partnerID", config.clientCode);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", ServiceCodeConstants.SEARCH_ROUTES);
        params.put("timestamp", timeStamp);
        params.put("msgData", JSONUtil.toJsonStr(msgDataMap));
        String result = "";
        try {
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(JSONUtil.toJsonStr(msgDataMap), timeStamp, config.checkWord));
            // 3.调SF-路由结果查询接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF-路由结果查询异常：【" + e.getMessage() + "】请联系管理员！");
        }
        String[] split = result.split("apiResultData");
        String jsonResult = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF-路由信息：\n" + JSONUtil.toJsonPrettyStr(jsonResult));
        JSONObject parseObj = JSONUtil.parseObj(jsonResult);
        if (SFConstants.SUCCESS.equals(parseObj.getStr(SFConstants.RESULT_CODE))) {
            JSONObject apiResultData = parseObj.getJSONObject("apiResultData");
            if (SFConstants.ORDER_SUCCESS.equals(apiResultData.getStr(SFConstants.ERROR_CODE))) {
                JSONObject msg = apiResultData.getJSONObject("msgData");
                JSONArray jsonArray = msg.getJSONArray("routeResps");
                List<RouteResp> routeResps = JSONUtil.toList(jsonArray, RouteResp.class);
                if (CollUtil.isEmpty(routeResps)) {
                    return Collections.emptyList();
                }
                List<Route> routes = routeResps.get(0).getRoutes();
                if (CollUtil.isNotEmpty(routes)) {
                    routes.sort(((o1, o2) -> o2.getAcceptTime().compareTo(o1.getAcceptTime())));
                    return routes;
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * 面单打印
     *
     * @param waybillNo 运单号
     * @param remark    备注
     * @return map: 电子面单pdf版url，token
     */
    @Override
    public Map<String, Object> printExpressSheet(String waybillNo, String remark) {
        Map<String, Object> map = new HashMap<>();
        String url = "";
        String token = "";
        Map<String, String> params = new HashMap(16);
        // msgData
        ExpressSheetPrint sheetPrint = new ExpressSheetPrint();
        sheetPrint.setTemplateCode(SFConstants.TEMPLATE_CODE);
        Extend extend = new Extend();
        extend.setEncryptFlag("1100");
        sheetPrint.setExtJson(extend);
        Document[] documents = new Document[10];
        documents[0] = new Document(waybillNo, remark);
        sheetPrint.setDocuments(documents);
        String msgData = JSONUtil.toJsonStr(sheetPrint);
        String result = "";
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            params.put("partnerID", config.clientCode);
            params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
            params.put("serviceCode", ServiceCodeConstants.CLOUD_PRINT_WAYBILLS);
            params.put("timestamp", timeStamp);
            params.put("msgData", msgData);
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, config.checkWord));
            // 调SF-云打印面单2.0接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF-云打印面单接口异常：【" + e.getMessage() + "】请联系管理员！");
        }
        String[] split = result.split("apiResultData");
        String jsonResult = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF-云打印面单响应信息：\n" + JSONUtil.toJsonPrettyStr(jsonResult));
        JSONObject parseObj = JSONUtil.parseObj(jsonResult);
        if (SFConstants.SUCCESS.equals(parseObj.getStr(SFConstants.RESULT_CODE))) {
            JSONObject apiResultData = parseObj.getJSONObject("apiResultData");
            if (BooleanUtil.isTrue(apiResultData.getBool("success"))) {
                JSONObject obj = apiResultData.getJSONObject("obj");
                JSONArray files = obj.getJSONArray("files");
                Object o = files.get(0);
                if (o instanceof FaceFile) {
                    url = ((FaceFile) o).getUrl();
                    token = ((FaceFile) o).getToken();
                }
            } else {
                throw new RRException("SF云打印接口异常信息：【" + apiResultData.getStr("errorMessage") + "】");
            }
        }

        map.put("url", url);
        map.put("X-Auth-token", token);
        return map;
    }

    /**
     * 订单状态推送回调
     */
    @Override
    public Map<String, String> orderStatusBack(String content) {
        Map<String, String> respMap = new HashMap<>(8);
        JSONObject parseObj = JSONUtil.parseObj(content);
        log.info("SF-订单状态推送回调json请求报文：\n" + JSONUtil.toJsonPrettyStr(parseObj));
        JSONArray orderStateArray = parseObj.getJSONArray("orderState");
        List<OrderStatusBack> orderStates = JSONUtil.toList(orderStateArray, OrderStatusBack.class);
        if (CollUtil.isEmpty(orderStates)) {
            respMap.put("success", "false");
            respMap.put("msg", "订单状态推送信息为空！");
            return respMap;
        }
        orderStatusBackService.saveBatch(orderStates);
        respMap.put("success", "true");
        respMap.put("code", "0");
        respMap.put("msg", "");
        return respMap;
    }

    /**
     * 路由状态推送回调
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> routeBack(String content) {
        Map<String, String> respMap = new HashMap<>(8);
        JSONObject parseObj = JSONUtil.parseObj(content);
        log.info("SF-路由推送回调json请求报文：\n" + JSONUtil.toJsonPrettyStr(parseObj));
        JSONObject body = parseObj.getJSONObject("Body");
        JSONArray jsonArray = body.getJSONArray("WaybillRoute");
        List<WaybillRoute> waybillRoutes = JSONUtil.toList(jsonArray, WaybillRoute.class);
        if (CollUtil.isEmpty(waybillRoutes)) {
            respMap.put("return_code", "1000");
            respMap.put("return_msg", "路由节点信息为空！");
            return respMap;
        }
        List<WaybillRoute> collectList = waybillRoutes.stream().filter(waybillRoute -> "44".equals(waybillRoute.getOpCode()) || "50".equals(waybillRoute.getOpCode()) || "80".equals(waybillRoute.getOpCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collectList)) {
            BusinessOrder order = new BusinessOrder();
            String orderId = collectList.get(0).getOrderid();
            for (WaybillRoute waybillRoute : collectList) {
                if ("50".equals(waybillRoute.getOpCode())) {
                    order.setOrderCollectionTime(DateUtil.parse(waybillRoute.getAcceptTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                if ("44".equals(waybillRoute.getOpCode())) {
                    order.setStatus(3);
                }
                if ("80".equals(waybillRoute.getOpCode())) {
                    order.setStatus(4);
                    order.setOrderSignTime(DateUtil.parse(waybillRoute.getAcceptTime(), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            baseMapper.update(order, new LambdaUpdateWrapper<BusinessOrder>().eq(StrUtil.isNotBlank(orderId), BusinessOrder::getOrderId, orderId));
        }
        respMap.put("return_code", "0000");
        respMap.put("return_msg", "成功");
        return respMap;
    }

    /**
     * 待揽件导出
     */
    @Override
    public void waitExport(HttpServletResponse response, OrderListVo orderListVo) {
        List<OrderListVo> orderListVos = baseMapper.getOrderListVos(new QueryWrapper<BusinessOrder>()
                .eq("ebo.status", 2)
                .ge(StrUtil.isNotBlank(orderListVo.getOrderStartTime()), "ebo.order_create_time", orderListVo.getOrderStartTime())
                .le(StrUtil.isNotBlank(orderListVo.getOrderEndTime()), "ebo.order_create_time", orderListVo.getOrderEndTime()));
        List<Map<String, Object>> rowsList = new ArrayList<>();
        if (CollUtil.isEmpty(orderListVos)) {
            throw new RRException("暂无快件处于待揽件状态！");
        }
        for (OrderListVo listVo : orderListVos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单编号", listVo.getOrderId());
            map.put("运单号", listVo.getWaybillNo());
            map.put("运单状态", "待揽件");
            map.put("托寄物", listVo.getName());
            map.put("付款方式", "寄付月结");
            map.put("物流产品", "顺丰标快");
            map.put("订单类型", "内地互寄");
            map.put("手工建单时间", listVo.getCreateTime());
            map.put("下单时间", listVo.getOrderCancelTime());
            map.put("订单备注", listVo.getRemark());
            map.put("收件方信息", "收件人：" + listVo.getReceiveContact() + "\n" + "收件公司：" + listVo.getReceiveCompany());
            map.put("寄件方信息", "寄件人：" + listVo.getSendContact() + "\n" + "寄件公司：" + listVo.getSendCompany());
            rowsList.add(map);
        }
        downloadExcel(rowsList, response);
    }

    /**
     * 下载excel
     *
     * @param list
     * @param response
     */
    private static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) {
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(DateUtil.today() + "_待揽件", "UTF-8"))).concat(".xlsx"));
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("SF待揽件导出excel异常：", e.getMessage());
        }
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        IoUtil.close(out);
    }

    /**
     * 根据运单号查询预计派送时间
     */
    @Override
    public String predictDeliveryTime(String waybillNo) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> msgDataMap = new HashMap<>();
        msgDataMap.put("searchNo", waybillNo);
        msgDataMap.put("checkType", 2);
        msgDataMap.put("checkNos", Collections.singletonList(config.monthlyCard));
        String msgData = JSONUtil.toJsonStr(msgDataMap);
        Map<String, String> params = Maps.newHashMap();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        params.put("partnerID", config.clientCode);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", ServiceCodeConstants.SEARCH_PROMITM);
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        String result = "";
        try {
            params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, config.checkWord));
            // 调SF-预计派送时间接口
            result = HttpClientUtil.post(config.callUrl, params);
        } catch (UnsupportedEncodingException e) {
            throw new RRException("SF-预计派送时间查询异常：【" + e.getMessage() + "】请联系管理员！");
        }
        String[] split = result.split("apiResultData");
        String jsonResult = split[0].concat("apiResultData").concat("\":").concat(split[1].substring(3, split[1].length() - 2)).concat("}").replace("\\", "");
        log.info("SF-预计派送时间查询结果：\n" + JSONUtil.toJsonPrettyStr(jsonResult));
        JSONObject parseObj = JSONUtil.parseObj(jsonResult);
        if (SFConstants.SUCCESS.equals(parseObj.getStr(SFConstants.RESULT_CODE))) {
            JSONObject apiResultData = parseObj.getJSONObject("apiResultData");
            if (BooleanUtil.isTrue(apiResultData.getBool("success"))) {
                String promiseTm = apiResultData.getJSONObject("msgData").getStr("promiseTm");
                builder.append("运单号：").append(waybillNo).append(",").append("预计派送时间：").append(promiseTm);
            } else {
                builder.append(apiResultData.getStr(SFConstants.ERROR_CODE)).append(apiResultData.getStr(SFConstants.ERROR_MSG));
                throw new RRException("SF-预计时间接口异常：" + builder.toString());
            }
        }
        return builder.toString();
    }

}
