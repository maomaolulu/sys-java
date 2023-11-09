package may.yuntian.external.province.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.external.province.entity.BasicInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 10:21
 */
@Mapper
public interface BasicInfoMapper extends BaseMapper<BasicInfo> {

    /**
     * 主管/质控：驳回（更新）
     *
     * @param viewer     1主管，2自控
     * @param reason     驳回原因
     * @param updateBy   更新者
     * @param updateTime 更新时间
     * @param projectId  项目id
     * @return row
     */
    @Update("UPDATE pro_basic_info SET `status` = 0, charge_reject_reason = IF(#{viewer} = 1, #{reason}, ''), quality_control_reject_reason = IF(#{viewer} = 2, #{reason}, ''), update_by = #{updateBy}, update_time = #{updateTime}, submit_date = NULL WHERE project_id = #{projectId} ;")
    int updateByProjectId(@Param("viewer") Integer viewer, @Param("reason") String reason, @Param("updateBy") String updateBy, @Param("updateTime") Date updateTime, @Param("projectId") Long projectId);

}
