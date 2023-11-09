package may.yuntian.external.wanda.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.AnlianConfig;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.wanda.constant.UrlConstants;
import may.yuntian.external.wanda.entity.OrganizeInfo;
import may.yuntian.external.wanda.entity.ProjectInfo;
import may.yuntian.external.wanda.mapper.OrganizeInfoMapper;
import may.yuntian.external.wanda.mapper.ProjectInfoMapper;
import may.yuntian.external.wanda.service.OrganizeInfoService;
import may.yuntian.external.wanda.vo.ItemBackVo;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-03-04 11:49
 */
@Service("organizeInfoService")
public class OrganizeInfoServiceImpl extends ServiceImpl<OrganizeInfoMapper, OrganizeInfo> implements OrganizeInfoService {

    /** 响应成功状态码 **/
    private static final String CODE = "200";
    /** code常量 **/
    private static final String JSON_KEY = "code";

    @Resource
    private AnlianConfig config;
    @Resource
    private RestTemplate restTemplate;
    @Autowired
    private OrganizeInfoMapper organizeInfoMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 查询技术服务机构基本信息
     */
    @Override
    public OrganizeInfo getOrgIfo() {
        return organizeInfoMapper.selectOne(new LambdaQueryWrapper<OrganizeInfo>().eq(OrganizeInfo::getDataBelong, ShiroUtils.getUserEntity().getSubjection()));
    }

    /**
     * 技术服务机构基本信息：新增 or 编辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addOrEdit(OrganizeInfo organizeInfo) {
        JSONObject jsonObject = null;
        Map<String, Object> paramMap = new HashMap<>(30);
        // 获取技术服务机构基本信息
        OrganizeInfo info = organizeInfoMapper.selectOne(new LambdaQueryWrapper<OrganizeInfo>().eq(OrganizeInfo::getDataBelong, organizeInfo.getDataBelong()));
        // 新增（保存）
        if (info == null){
            // 封装请求体参数
            String body = getOrgInfoRequestBodyParams(organizeInfo, paramMap);
            String responseBody = HttpUtil.post(config.wandaPath + "/zywsThirdOrgInfo/add", body);
            // json字符串转JSONObject对象
            jsonObject = JSON.parseObject(responseBody);
            // 当code=200时，获取{"body": xxx}即机构id的值；否则，抛出第三方接口返回的信息！
            if (CODE.equals(jsonObject.get(JSON_KEY))){
                // 向wanda_organize_info表中插入数据
                organizeInfo.setOrgId(jsonObject.getString("body"));
                organizeInfo.setOrgKey(config.orgKey);
                organizeInfo.setCreateTime(DateUtils.getNowDate());
                organizeInfo.setCreateBy(ShiroUtils.getUserEntity().getUsername());
                organizeInfoMapper.insert(organizeInfo);
            }
        }
        // 编辑（更新）
        if (info != null){
            paramMap.put("id", info.getOrgId());
            String body = getOrgInfoRequestBodyParams(organizeInfo, paramMap);
            String responseBody = HttpUtil.post(config.wandaPath + "/zywsThirdOrgInfo/edit", body);
            jsonObject = JSON.parseObject(responseBody);
            if (CODE.equals(jsonObject.get(JSON_KEY))){
                // 更新表：wanda_organize_info
                organizeInfo.setUpdateTime(DateUtils.getNowDate());
                organizeInfo.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                organizeInfoMapper.update(organizeInfo, new LambdaUpdateWrapper<OrganizeInfo>().eq(OrganizeInfo::getId, info.getId()));
            }
        }
        return jsonObject;
    }

    /**
     * 健康在线-项目退回原因
     */
    @Override
    public List<ItemBackVo> itemBackReason(String projectCode) {
        ProjectInfo info = projectInfoMapper.selectOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectCode, projectCode));
        if (ObjectUtil.isNull(info)) {
            throw new RRException("要查询的项目不存在！");
        }
        if (info.getNodeStatus() != 5) {
            throw new RRException("该项目尚未进行报送！");
        }
        OrganizeInfo orgIfo = getOrgIfo();
        // 构建body参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orgCode", orgIfo.getOrgCode());
        jsonObject.put("orgName", orgIfo.getOrgName());
        jsonObject.put("orgKey", orgIfo.getOrgKey());
        jsonObject.put("orgId", orgIfo.getOrgId());
        // 添加请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        // 组装请求头和参数
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(jsonObject), httpHeaders);
        // 发送post请求
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(UrlConstants.ITEM_BACK_URL, httpEntity, String.class);
        cn.hutool.json.JSONObject parseObj = JSONUtil.parseObj(stringResponseEntity.getBody());
        if (CODE.equals(parseObj.get(JSON_KEY))) {
            JSONArray body = parseObj.getJSONArray("body");
            log.warn("arrayBody: " + body);
            List<ItemBackVo> itemBackVos = JSONUtil.toList(body, ItemBackVo.class);
            if (CollUtil.isNotEmpty(itemBackVos)) {
                return itemBackVos.stream().filter(itemBackVo -> itemBackVo.getCheckId().equals(info.getBelongThirdPrjId())).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 方法抽取：wanda-技术服务机构信息请求体参数
     */
    private String getOrgInfoRequestBodyParams(OrganizeInfo organizeInfo, Map<String, Object> paramMap) {
        paramMap.put("orgCode", organizeInfo.getOrgCode());
        paramMap.put("orgName", organizeInfo.getOrgName());
        paramMap.put("orgKey", config.orgKey);
        paramMap.put("legalName", organizeInfo.getLegalName());
        paramMap.put("legalPhone", organizeInfo.getLegalPhone());
        paramMap.put("registerArea", organizeInfo.getRegisterArea());
        paramMap.put("registerAddress", organizeInfo.getRegisterAddress());
        paramMap.put("registerAreaCode", organizeInfo.getRegisterAreaCode());
        // map转JSON字符串
        return JSON.toJSONString(paramMap);
    }

}
