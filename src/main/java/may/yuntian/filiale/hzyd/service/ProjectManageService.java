package may.yuntian.filiale.hzyd.service;

import may.yuntian.filiale.hzyd.dto.ProjectDto;
import may.yuntian.filiale.hzyd.vo.OperateVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 12:20
 */
public interface ProjectManageService {

    /**
     * 列表
     *
     * @param projectDto 查询条件
     * @return 结果
     */
    List<ProjectDto> selectProjectList(ProjectDto projectDto);

    /**
     * 新增
     *
     * @param projectDto dto
     * @return rows
     */
    int addProject(ProjectDto projectDto);

    /**
     * （批量）下发
     *
     * @param operateVo vo
     * @return rows
     */
    int batchIssue(OperateVo operateVo);

    /**
     * （批量）确认
     *
     * @param operateVo 操作vo
     * @return rows
     */
    int batchConfirm(OperateVo operateVo);

    /**
     * 编辑
     *
     * @param projectId 项目id
     * @return 项目信息
     */
    ProjectDto itemEdit(Long projectId);

    /**
     * 保存
     *
     * @param projectDto dto
     * @return rows
     */
    int editAfterSave(ProjectDto projectDto);

    /**
     * 详情
     *
     * @param projectId 项目id
     * @return 结果
     */
    ProjectDto viewDetails(Long projectId);

    /**
     * 中止
     *
     * @param projectId 项目id
     * @return row
     */
    int abort(Long projectId);

}
