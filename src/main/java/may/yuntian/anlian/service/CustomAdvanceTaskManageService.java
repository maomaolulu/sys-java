package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.oa.dto.CustomAdvanceTaskDto;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:45
 * @Version 1.0
 * @Description 跟进任务
 */
public interface CustomAdvanceTaskManageService extends IService<CustomAdvanceTaskEntity> {
    /**
     * 更新任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int modify(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 人员替换
     *
     * @param customAdvanceTask  任务信息
     * @return result
     */
    int replaceUserBatch(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 新建任务
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int add(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 任务分配
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int allocationBatch(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 查询跟进任务列表
     *
     * @param dto 查询条件
     * @return 结果计算
     */
    List<CustomAdvanceTaskDto> listTasks(CustomAdvanceTaskDto dto);

//    /**
//     * 查询我的任务列表
//     *
//     * @param customAdvanceTaskDto
//     * @return 集合
//     */
//    List<CustomAdvanceTaskDto> getMyTasks(CustomAdvanceTaskDto customAdvanceTaskDto);

//    /**
//     * 获取联系人信息
//     *
//     * @param companyId 公司ID
//     * @return 集合
//     */
//    List<CompanyContactEntity> getCompanyContactInfo(Long companyId);

//    /**
//     * 查询我的任务单个
//     *
//     * @param customAdvanceTaskDto
//     * @return 任务跟进信息
//     */
//    CustomAdvanceTaskDto getMyTaskOne(CustomAdvanceTaskDto customAdvanceTaskDto);

    /**
     * 公司是否关联跟进任务
     *
     * @param id 公司ID
     * @return 结果
     */
    boolean existTaskByCompany(Long id);

    /**
     * 释放跟进任务,客户至公海
     */
    boolean releaseCompany(CustomAdvanceTaskDto dto);
}
