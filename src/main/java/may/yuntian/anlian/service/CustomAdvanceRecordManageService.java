package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.vo.HistoryAdvanceVo;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:49
 * @Version 1.0
 * @Description 跟进记录
 */
public interface CustomAdvanceRecordManageService extends IService<CustomAdvanceRecordEntity> {
    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    int add(CustomAdvanceRecordEntity customAdvanceRecord);

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 任务ID
     * @return 集合
     */
    List<CustomAdvanceRecordEntity> getDetail(CustomAdvanceRecordEntity customAdvanceRecord);

    /**
     * 修改跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    int updateAdvanceInfo(CustomAdvanceRecordEntity customAdvanceRecord);

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录主键ID
     * @return result
     */
    int delete(Long id);

    /**
     * 历史跟进
     *
     * @param companyId 公司id
     * @return list
     */
    List<HistoryAdvanceVo> historyAdvance(Long companyId);

}
