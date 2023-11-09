package may.yuntian.external.wanda.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.wanda.entity.OrganizeInfo;
import may.yuntian.external.wanda.vo.ItemBackVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-03-04 11:48
 */
public interface OrganizeInfoService extends IService<OrganizeInfo> {

    /**
     * 查询技术服务机构基本信息
     * @return
     */
    OrganizeInfo getOrgIfo();

    /**
     * 技术服务机构基本信息：新增 or 编辑
     * @param organizeInfo 机构信息对象
     * @return jsonObject
     */
    JSONObject addOrEdit(OrganizeInfo organizeInfo);

    /**
     * 健康在线-项目退回原因
     *
     * @param projectCode 项目编号
     * @return str
     */
    List<ItemBackVo> itemBackReason(String projectCode);
}
