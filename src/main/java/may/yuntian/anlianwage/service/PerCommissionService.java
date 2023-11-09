package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.vo.PojectCommissionVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:44
 */
@SuppressWarnings("all")
public interface PerCommissionService extends IService<PerCommissionEntity> {

    /**
     * 分页列表
     * @param params
     * @return
     */
    List<PojectCommissionVo> queryPage(Map<String, Object> params);


    /**
     * 分页列表--提成类型明细
     * @param params
     * @return
     */
    List<PojectCommissionVo> queryTypePage(Map<String, Object> params);

    /**
     * 根据检评绩效分配ID获取列表信息
     * @param performanceAllocationId
     * @return
     */
    List<PerCommissionEntity> getListByPerformanceAllocationId(Long performanceAllocationId,Long projectId);


    /**
     * 根据检评绩效分配ID获取信息
     * @param performanceAllocationId
     * @return
     */
    PerCommissionEntity getByPerformanceAllocationId(Long performanceAllocationId,Long projectId,String name);

    /**
     * 检评绩效分配页面所需获取列表接口
     * @param performanceAllocationId
     * @param projectId
     * @param type
     * @return
     */
    List<PerCommissionEntity> getListByIdAndType(Long performanceAllocationId,Long projectId,String type);


    /**
     * 显示已提成和异常的信息列表--导出
     * 状态大于1
     * @return
     */
    List<PojectCommissionVo> listAll(Map<String, Object> params);

    /**
     * 显示已提成和异常的信息列表--导出--类型明细
     * 状态大于1
     * @return
     */
    List<PojectCommissionVo> listTypeAll(Map<String, Object> params);


    /**
     * 根据时间段生成提成记录
     */
    List<PojectCommissionVo> getListByCommissionDate(PerCommissionEntity commission);

    /**
     * 获取环境任务采样提成
     * @param planId
     * @param projectId
     * @return
     */
    PerCommissionEntity getByPlanId(Long projectId,String types);


    /**
     * g根据提成人查询
     * @param personnel
     * @return
     */
    List<Long> getListByPersonnel(String personnel);


    /**
     * 根据项目类型获取提成列表
     * @param projectId
     * @return
     */
    List<PerCommissionEntity> getListByprojectId(Long projectId);
}
