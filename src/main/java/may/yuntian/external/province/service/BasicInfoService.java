package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.vo.SysUserVo;

import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 10:22
 */
public interface BasicInfoService extends IService<BasicInfo> {

    /**
     * 基本信息调用逻辑校验
     * @param projectId
     * @return
     */
    Map<String, Object> assertBasicInfo(Long projectId);

    /**
     * 查询企业员工信息
     * @return list
     */
    List<SysUserVo> getUserInfoAll();

    /**
     * 查询项目基本信息
     * @param projectId
     * @return
     */
    BasicInfo getBasicInfo(Long projectId);
    /**
     * 保存or修改项目基本信息
     * @param basicInfo
     * @return
     */
    boolean saveOrUpdateBasicInfo(BasicInfo basicInfo);

    /**
     * 业务员-提交
     * @param projectId
     * @return
     */
    int salesmanSubmit(Long projectId);

    /**
     * 业务员提交前：确认数据是否保存
     * @param projectId
     * @return
     */
    Map<Object, Object> dataInfoRecord(Long projectId);
}
