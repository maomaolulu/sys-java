package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.vo.HistoryAdvanceVo;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:38
 * @Version 1.0
 * @Description 跟进记录
 */
@Mapper
public interface CustomAdvanceRecordManageMapper extends BaseMapper<CustomAdvanceRecordEntity> {
    @Select(" select * from custom_advance_record " +
            " ${ew.customSqlSegment} ")
    List<CustomAdvanceRecordEntity> getDetails(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 查询历史跟进信息
     *
     * @param companyId 公司id
     * @return list
     */
    @Select("SELECT tc.company, tc.`code` AS credit_code, tc.data_belong, tc.person_belong, tc.if_has_finished,\n" +
            "CASE cdt.business_status WHEN 0 THEN '待分配' WHEN 1 THEN '待跟进' WHEN 2 THEN '跟进中' WHEN 3 THEN '待合作' WHEN 4 THEN '合作中' WHEN 5 THEN '跟进结束'END AS service_status, \n" +
            "cdt.task_code, su.username AS follow_user, car.advance_date AS follow_date, car.advance_pattern AS follow_way, car.advance_information AS follow_info\n" +
            "FROM t_company AS tc\n" +
            "LEFT JOIN custom_advance_task AS cdt ON tc.id = cdt.company_id\n" +
            "LEFT JOIN sys_user AS su ON cdt.advance_id = su.user_id\n" +
            "LEFT JOIN custom_advance_record AS car ON cdt.id = car.task_id\n" +
            "WHERE tc.id = #{companyId} AND cdt.delete_flag = 0 AND car.advance_date IS NOT NULL ")
    List<HistoryAdvanceVo> selectHistoryAdvanceByCompanyId(@Param("companyId") Long companyId);

}
