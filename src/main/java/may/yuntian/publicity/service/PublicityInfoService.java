package may.yuntian.publicity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.publicity.entity.PublicityInfoEntity;
import may.yuntian.publicity.vo.PublicityPageVo;

import java.util.List;
import java.util.Map;

/**
 * 项目公示记录
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
public interface PublicityInfoService extends IService<PublicityInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取最近一次操作记录
     * @param projectId
     * @param status
     * @return
     */
    PublicityInfoEntity getLastLimit(Long projectId,Integer status);

    /**
     * 获取最近一次提交审核操作记录
     *
     * @param projectId
     * @return
     */
    public PublicityInfoEntity getTjLastLimit(Long projectId);



    /**
     * 获取公示记录详细信息
     * @param projectId
     * @return
     */
    public List<PublicityInfoEntity> getInfoList(Long projectId);


    /**
     * 检评公示记录列表
     * @param params
     * @return
     */
    public List<PublicityPageVo> getZjList(Map<String,Object> params);

    /**
     * 评价公示记录列表
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getPjList(Map<String, Object> params);


    /**
     * 质控公示记录列表
     * @param params
     * @return
     */
    public List<PublicityPageVo> getSysList(Map<String,Object> params);

    /**
     * 获取最近一次操作记录--批量
     *
     * @param projectIds
     * @param status
     * @return
     */
    public List<PublicityInfoEntity> getLastLimitList(List<Long> projectIds, Integer status);
    
}

