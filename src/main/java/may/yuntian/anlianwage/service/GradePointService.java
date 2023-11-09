package may.yuntian.anlianwage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlianwage.entity.GradePointEntity;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.anlianwage.vo.ProjectPerformanceVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:44
 */
@SuppressWarnings("all")
public interface GradePointService extends IService<GradePointEntity> {

    // 显示评价项目的绩效列表
    List<ProjectPerformanceVo> listAll(ProjectPerformanceVo proPerformanceVo);

    // 计算绩效并保存
    GradePointEntity editSave(GradePointEntity gradePointEntity);

    // 批量导出
    void exportExcel(HttpServletResponse response , ProjectPerformanceVo proPerformanceVo);

    // 签发提成
    Boolean getCommissionIssue(PerformanceNodeVo performanceNodeVo);

    // 归档提成
    Boolean getFilingFees(PerformanceNodeVo performanceNodeVo);

    // 项目提成脚本：评价报告提成！
    void getCommission(List<Long> projectIds);
}
