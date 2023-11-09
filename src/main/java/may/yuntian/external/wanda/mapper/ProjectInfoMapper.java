package may.yuntian.external.wanda.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.external.wanda.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-03-06 11:17
 */
@Mapper
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    /**
     * 获取项目基本信息列表
     *
     * @param wrapper 查询条件
     * @return list
     */
    @Select("select wpi.project_id, wpi.com_name, wpi.project_code, wpi.project_name, wpi.check_type, wpi.node_status, wpi.push_time, ap.dept_id, ap.charge, ap.status, ap.type\n" +
            "from wanda_project_info as wpi\n" +
            "left join al_project as ap\n" +
            "on wpi.project_id = ap.id\n" +
            "${ew.customSqlSegment} ")
    List<ProjectInfo> getProjectInfoList(@Param(Constants.WRAPPER) QueryWrapper<ProjectInfo> wrapper);

    /**
     * 质控驳回：wanda_project_info表更新
     *
     * @param reason     驳回原因
     * @param updateBy   更新者
     * @param updateTime 更新时间
     * @param projectId  项目id
     * @return rows
     */
    @Update("UPDATE wanda_project_info SET node_status = 0, quality_control_reject = #{reason}, update_by = #{updateBy}, update_time = #{updateTime}, push_time = NULL WHERE project_id = #{projectId}; ")
    int updateByQualityReject(@Param("reason") String reason, @Param("updateBy") String updateBy, @Param("updateTime") Date updateTime, @Param("projectId") Long projectId);

}
