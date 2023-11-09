package may.yuntian.socket.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.socket.consts.UrlConstants;
import may.yuntian.socket.domain.AbuProjectNote;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;
import may.yuntian.socket.mapper.AbuProjectNoteMapper;
import may.yuntian.socket.service.IAbuProjectNoteService;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.AlRedisUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 项目留言Service业务层处理
 *
 * @author yrb
 * @date 2023-04-06
 */
@Service
public class AbuProjectNoteServiceImpl extends ServiceImpl<AbuProjectNoteMapper, AbuProjectNote> implements IAbuProjectNoteService {

    //todo 通用配置
    public static final String WECOM_PROJECT_TOKEN = "wecom_project_token";
    public static final String WECOM_CORPID = "ww886f70c97df35620";
    public static final String WECOM_TOUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserid?access_token=";
    public static final String WECOM_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
    //todo 开票通知提醒
    public static final Integer WECOM_AGENT_ID = 1000018;
    public static final String WECOM_CORPSECRET = "O3U-qntG_8TMxtjZUcCdG6hervrwevC_ce8eh1QKvvQ";
    public static final String WECOM_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?" + "corpid=" + WECOM_CORPID + "&corpsecret=" + WECOM_CORPSECRET;
    //TODO 集团发展项目状态变更提醒
    public static final Integer WECOM_AGENT_ID_TWO = 1000020;
    public static final String WECOM_CORPSECRET_TWO = "twGa1h9OKWTdggWbKMUGGEEdqs9orQ2rAz3ihwIfmgM";
    public static final String WECOM_TOKEN_URL_TWO = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?" + "corpid=" + WECOM_CORPID + "&corpsecret=" + WECOM_CORPSECRET_TWO;


    protected final Logger logger = LoggerFactory.getLogger(AbuProjectNoteServiceImpl.class);
    private final AbuProjectNoteMapper abuProjectNoteMapper;
    private final SysUserService sysUserService;
    private final AlRedisUntil redisUtils;

    @Autowired
    public AbuProjectNoteServiceImpl(AbuProjectNoteMapper abuProjectNoteMapper,
                                     SysUserService sysUserService,
                                     AlRedisUntil redisUtils) {
        this.abuProjectNoteMapper = abuProjectNoteMapper;
        this.sysUserService = sysUserService;
        this.redisUtils = redisUtils;
    }

    /**
     * @param abuSendNoteDTO 留言相关信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void sendMessage(AbuSendNoteDTO abuSendNoteDTO) {
        SysUserEntity loginUser = ShiroUtils.getUserEntity();
        Long userId = loginUser.getUserId();
        abuSendNoteDTO.setWriter(loginUser.getUsername());
        // 将需要发送信息的号码存进list
        List<String> list = new ArrayList<>();
        Long receiveId = null;
        // 通过中文名获取用户信息
        if (StringUtils.isNotNull(abuSendNoteDTO.getSalesmanId())) {
            SysUserEntity sysUser = sysUserService.getById(abuSendNoteDTO.getSalesmanId());
            if (sysUser != null&&sysUser.getStatus()==1) {
                if (sysUser.getBelongUserid()!=null&&sysUser.getBelongUserid()!=0){
                    SysUserEntity sysUser2 = sysUserService.getById(sysUser.getBelongUserid());
                    if (StrUtil.isNotBlank(sysUser2.getMobile())){
                        receiveId = sysUser2.getUserId();
                        list.add(sysUser2.getMobile());
                    }
                }else {
                    if (StrUtil.isNotBlank(sysUser.getMobile())&&sysUser.getType()==1){
                        receiveId = sysUser.getUserId();
                        System.out.println(sysUser.getMobile());
                        list.add(sysUser.getMobile());
                    }
                }
            }
        }
        // 获取token
        String token = (String) redisUtils.get(WECOM_PROJECT_TOKEN);
        if (StrUtil.isBlank(token)) {
            token = getToken("");
        }
//        System.out.println("token = " + token);
        // 通过电话号码获取企微用户id
        String touser = getQiWeiTouser(token, list);
        if (StrUtil.isBlank(touser)) {
            throwException("未获取到信息接收人微信id");
        }
        // 向企业微信用户发送消息
        String content = getMessage(abuSendNoteDTO);
        // 封装并保存留言信息
        AbuProjectNote abuProjectNote = new AbuProjectNote();
        abuProjectNote.setProjectId(abuSendNoteDTO.getProjectId());
        abuProjectNote.setIdentifier(abuSendNoteDTO.getIdentifier());
        abuProjectNote.setReceiveId(receiveId);
        abuProjectNote.setNote(StringUtils.isBlank(abuSendNoteDTO.getNote())?"":content);
        abuProjectNote.setCreateTime(new Date());
        abuProjectNote.setUserId(userId);
        if (baseMapper.insert(abuProjectNote) == 0) {
            throwException("留言信息插入数据库失败");
        }
        sendMessage(token, touser, content,"");
    }

    /**
     * 查询留言相关信息
     *
     * @param identifier 项目编号
     * @return result
     */
    @Override
    public List<AbuProjectNote> selectProjectNoteList(String identifier) {
        QueryWrapper<AbuProjectNote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identifier", identifier);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    private String getToken(String type) {
        String token_url;
        if ("status".equals(type)){
            token_url = WECOM_TOKEN_URL_TWO;
        }else {
            token_url = WECOM_TOKEN_URL;
        }
        // 获取token
        String response = HttpUtil.get(token_url);
        if (StrUtil.isBlank(response)) {
            throwException("获取企微token时返回值为空,请求路径：" + token_url, "获取token时返回值为空");
        }
        // 解析返回值
        Map<String, Object> parse = (Map<String, Object>) JSON.parse(response);
        String token = (String) parse.get("access_token");
        if (StrUtil.isBlank(token)) {
            throwException("获取企微token时发生错误：" + response, "未获取到token");
        }
        // 存redis 有效期2小时
        redisUtils.set(WECOM_PROJECT_TOKEN, token, (Integer) parse.get("expires_in"));
        return token;
    }

    private String getQiWeiTouser(String token, List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String phone : list) {
            // 通过电话号码获取企微用户id
            String url = WECOM_TOUSER_URL + token;
            Map<String, Object> map = new HashMap<>();
            map.put("mobile", phone);
            String response = HttpUtil.post(url, JSON.toJSONString(map));
            if (StrUtil.isBlank(response)) {
                logger.error("通过手机号码获取touser时返回值为空，手机号码：------------------------" + phone);
                continue;
            }
            // 解析返回值
            Map<String, String> parse = (Map<String, String>) JSON.parse(response);
            String userId = parse.get("userid");
            if (StrUtil.isBlank(userId)) {
                logger.error("未能通过手机号码获取到touser------------------------" + response);
                continue;
            }
            stringBuilder.append(userId + "|");
        }
        if (stringBuilder.length() == 0) {
            return "";
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    //todo
    private void sendMessage(String token, String touser, String content,String type) {
        Integer agentid;
        if ("status".equals(type)){
            agentid = WECOM_AGENT_ID_TWO;
        }else {
            agentid = WECOM_AGENT_ID;
        }
        // 发消息
        String url = WECOM_MESSAGE_URL + token;
        // 封装数据并发送
        Map<String, Object> map2 = new HashMap<>();
        map2.put("content", content);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("touser", touser);
        map1.put("msgtype", "text");
        map1.put("agentid", agentid);
        map1.put("safe", 0);
        map1.put("text", map2);
        String response = HttpUtil.post(url, JSON.toJSONString(map1));
        if (StrUtil.isBlank(response)) {
            throwException("发送信息给企业微信用户时返回值为空，请求地址：" + url, "发送信息给企业微信用户时返回值为空");
        }
        Map<String, Object> parse = (Map<String, Object>) JSON.parse(response);
        Integer errcode = (Integer) parse.get("errcode");
        if (errcode == null || errcode != 0) {
            throwException("信息发送失败-----------------返回值为：" + response, "信息发送失败");
        }
    }

    private void throwException(String var1, String var2) {
        logger.error(var1);
        throw new RuntimeException(var2);
    }

    private void throwException(String var) {
        logger.error(var);
        throw new RuntimeException(var);
    }

    private String getMessage(AbuSendNoteDTO abuSendNoteDTO) {
        String message;
        if (StringUtils.isNotBlank(abuSendNoteDTO.getNote())){
            message = abuSendNoteDTO.getNote();
        }else {
            message = abuSendNoteDTO.getWriter() + "给你发送通知：\r\n" +
                    abuSendNoteDTO.getIdentifier()+" "+abuSendNoteDTO.getEntrustCompany()+"("+abuSendNoteDTO.getCompany()+"),"
                    + "报告已完成，开票邮寄请联系客服部。";
        }

        return message;
    }


    private String getStatusMessage(AbuSendNoteDTO abuSendNoteDTO) {
        String message;
        if (StringUtils.isNotBlank(abuSendNoteDTO.getNote())){
            message = abuSendNoteDTO.getNote();
        }else {
            message = abuSendNoteDTO.getWriter() + "给你发送通知：\r\n" +
                    abuSendNoteDTO.getIdentifier()+" "+abuSendNoteDTO.getEntrustCompany()+"("+abuSendNoteDTO.getCompany()+"),"
                    + "项目状态已变更，当前状态为 " + UrlConstants.getStatusStr(abuSendNoteDTO.getStatus());
        }

        return message;
    }

    /**
     * 状态变更消息发送
     * @param abuSendNoteDTO 留言相关信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void sendStatusMessage(AbuSendNoteDTO abuSendNoteDTO) {
        Long userId = Long.valueOf("9");
        abuSendNoteDTO.setWriter("系统管理员");
        // 将需要发送信息的号码存进list
        List<String> list = new ArrayList<>();
        Long receiveId = null;
        // 通过中文名获取用户信息
        if (StringUtils.isNotNull(abuSendNoteDTO.getSalesmanId())) {
            SysUserEntity sysUser = sysUserService.getById(abuSendNoteDTO.getSalesmanId());
            if (sysUser != null&&sysUser.getStatus()==1) {
                if (sysUser.getBelongUserid()!=null&&sysUser.getBelongUserid()!=0){
                    SysUserEntity sysUser2 = sysUserService.getById(sysUser.getBelongUserid());
                    if (StrUtil.isNotBlank(sysUser2.getMobile())){
                        receiveId = sysUser2.getUserId();
                        list.add(sysUser2.getMobile());
                    }
                }else {
                    if (StrUtil.isNotBlank(sysUser.getMobile())&&sysUser.getType()==1){
                        receiveId = sysUser.getUserId();
                        System.out.println(sysUser.getMobile());
                        list.add(sysUser.getMobile());
                    }
                }
            }
        }
        // 获取token
        String token = (String) redisUtils.get(WECOM_PROJECT_TOKEN);
        if (StrUtil.isBlank(token)) {
            token = getToken("status");
        }
//        System.out.println("token = " + token);
        // 通过电话号码获取企微用户id
        String touser = getQiWeiTouser(token, list);
        if (StrUtil.isBlank(touser)) {
            throwException("未获取到信息接收人微信id");
        }
        // 向企业微信用户发送消息
        String content = getStatusMessage(abuSendNoteDTO);
        // 封装并保存留言信息
        AbuProjectNote abuProjectNote = new AbuProjectNote();
        abuProjectNote.setProjectId(abuSendNoteDTO.getProjectId());
        abuProjectNote.setIdentifier(abuSendNoteDTO.getIdentifier());
        abuProjectNote.setReceiveId(receiveId);
        abuProjectNote.setNote(StringUtils.isBlank(abuSendNoteDTO.getNote())?"":content);
        abuProjectNote.setCreateTime(new Date());
        abuProjectNote.setUserId(userId);
        if (baseMapper.insert(abuProjectNote) == 0) {
            throwException("留言信息插入数据库失败");
        }
        sendMessage(token, touser, content,"status");
    }



}
