package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.vo.PerformanceAllocationNewVO;
import may.yuntian.anlianwage.vo.PerformanceAllocationVo;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlianwage.entity.PerformanceAllocationEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 绩效分配表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
public interface PerformanceAllocationService extends IService<PerformanceAllocationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PerformanceAllocationVo> export(Map<String, Object> params);

    /**
     * 绩效分配分页列表
     * @param params
     * @return
     */
    List<PerformanceAllocationVo> getPageList(Map<String, Object> params);

    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    PerformanceAllocationEntity getInfo(Long id);

    /**
     * 提成分配接口-获取
     * @param id
     * @return
     */
    List<PerCommissionEntity> royaltyDistribution(Long id);

    /**
     * 提成分配--修改
     * @param commissionEntityList
     * @return
     */
    int updateCaiYangCommission(List<PerCommissionEntity> commissionEntityList);

    /**
     * 统计相关接口
     * @param params
     * @return
     */
    PerformanceAllocationNewVO statisticalCorrelation(Map<String,Object> params);

    /**
     * 采样编辑
     * @param performanceAllocationEntity
     * @return
     */
    Map<Integer,List<PerCommissionEntity>> getEditInformation(PerformanceAllocationEntity performanceAllocationEntity);

    /**
     * 采样编辑提交
     * @param commissionEntityList
     */
    void saveOrUpdateBatchCommission(List<PerCommissionEntity> commissionEntityList);



    /**
     * 评价采样提成
     * @param performanceNodeVo
     */
    void caiyangPjCommission(PerformanceNodeVo performanceNodeVo);
    /**
     * 归档提成--检评
     * @param performanceNodeVo
     */
    void fillingCommission(PerformanceNodeVo performanceNodeVo);

    /**
     * 签发提成--检评
     * @param performanceNodeVo
     */
    void issueCommission(PerformanceNodeVo performanceNodeVo);

    /**
     * 环境采样提成
     * @param performanceNodeVo
     */
    boolean caiyangHjCommission(PerformanceNodeVo performanceNodeVo);

    /**
     * 检评采样提成
     * @param performanceNodeVo
     */
    void caiyangZjCommission(PerformanceNodeVo performanceNodeVo);

    /**
     * 环境签发提车
     * @param performanceNodeVo
     */
    void issueHjCommission(PerformanceNodeVo performanceNodeVo);

    
}

