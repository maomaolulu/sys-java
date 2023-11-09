package may.yuntian.external.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.vo.CustomAdvanceVo;
import may.yuntian.external.oa.dto.CustomAdvanceTaskDto;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Author yrb
 * @Date 2023/8/21 11:42
 * @Version 1.0
 * @Description 客户跟进任务
 */
@Mapper
public interface CustomAdvanceTaskMapper extends BaseMapper<CustomAdvanceTaskEntity> {

//    /**
//     * 查询跟进任务列表
//     *
//     * @param wrapper 查询条件
//     * @return result
//     */
//    @Select("SELECT cat.id task_id, cc.id customer_id, cat.task_code, cc.customer_order, cc.enterprise_name, cc.province, cc.city, cc.district, u.nick_name advance_name, cat.advance_first_time, cat.advance_last_time, cat.business_status, cat.advance_result, cc.registered_address\n" +
//            "FROM custom_advance_task cat \n" +
//            "LEFT JOIN custom_customer cc ON cat.custom_id = cc.id\n" +
//            "LEFT JOIN custom_contacters ccs ON cat.custom_id = ccs.customer_id AND ccs.if_default = 1\n" +
//            "LEFT JOIN sys_user u ON u.user_id = cat.advance_id " +
//            "${ew.customSqlSegment}")
//    List<CustomAdvanceVo> listTasks(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 查询跟进任务列表
     *
     * @param wrapper 查询条件
     * @return result
     */
    @Select("select t1.id,t1.task_code,t1.business_status,t1.advance_id, " +
            "t1.advance_first_time,t1.advance_last_time,t1.advance_result, " +
            "t2.id as company_id,t2.company,t2.office_address,t2.data_belong, " +
            "t2.province,t2.city,t2.area,t2.contact,t2.mobile,t2.address, " +
            "t2.person_belong,t2.if_has_finished,t2.address, " +
            "t3.id as record_id,t3.advance_date,t3.advance_pattern,t3.advance_information, " +
            "t4.username " +
            "from custom_advance_task t1 " +
            "left join t_company t2 on t1.company_id = t2.id " +
            "left join custom_advance_record t3 on t1.last_record_id = t3.id " +
            "left join sys_user t4 on t1.advance_id = t4.user_id " +
            "${ew.customSqlSegment}")
    List<CustomAdvanceTaskDto> taskList(@Param(Constants.WRAPPER) QueryWrapper<CustomAdvanceTaskDto> wrapper);

    @Select("select * from t_company_contact where company_id = #{companyId} ")
    List<CompanyContactEntity> selectCompanyContactInfo(@Param("companyId") Long companyId);
}
