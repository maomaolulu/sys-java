package may.yuntian.anlian.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.vo.CompanyPublicVo;
import may.yuntian.anlian.vo.CompanyVo;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.CompanyEntity;

import java.util.List;

/**
 * 企业信息
 * 数据持久层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Mapper
public interface CompanyMapper extends BaseMapper<CompanyEntity> {

	 /**
     *删除企业信息连带其子表也删除
     */
	void deleteBatchIds(Long[] ids);

	/**
	 * 查询客户信息列表
	 */
	@Select("SELECT tc.id , cat.id task_id, tc.data_belong, tc.company, tc.province, tc.city, tc.area, ifNULL(cat.business_status, 0) business_status, tc.`status`, tc.person_belong, tc.if_has_finished FROM t_company tc\n" +
			"LEFT JOIN (SELECT id, company_id, business_status, advance_id FROM custom_advance_task WHERE id IN(SELECT MAX(id) FROM custom_advance_task GROUP BY company_id)) cat ON cat.company_id = tc.id \n" +
			" ${ew.customSqlSegment}")
//	@Results({
//			@Result(column="personBelong", property="personBelong", jdbcType= JdbcType.VARCHAR)
//	})
	List<CompanyVo> selectList1(@Param(Constants.WRAPPER) QueryWrapper wrapper);

	/**
	 * 根据条件查询客户id、最早跟进、最近跟进时间
	 * @param wrapper 查询条件
	 * @return list
	 */
	@Select("SELECT tc.id FROM t_company tc\n" +
			"LEFT JOIN (SELECT company_id,MIN(advance_first_time) advance_first_time FROM custom_advance_task GROUP BY company_id HAVING advance_first_time is not NULL) cat1 ON cat1.company_id = tc.id\n" +
			"LEFT JOIN (SELECT company_id,MAX(advance_last_time) advance_last_time FROM custom_advance_task GROUP BY company_id HAVING advance_last_time is not NULL) cat2 ON cat2.company_id = tc.id " +
			"${ew.customSqlSegment}")
	List<Long> selectToBeOpenData(@Param(Constants.WRAPPER) QueryWrapper wrapper);

	/**
	 * 客户公海列表
	 * @param wrapper 查询条件
	 * @return list
	 */
	@Select("SELECT tc.id, tc.data_belong, tc.company, tc.province, tc.city, tc.area, tc.contract_last, tc.`status`\n" +
			"FROM t_company tc\n" +
			"LEFT JOIN (SELECT company_id, AVG(IF(business_status = 5 OR business_status = 0,10,-1)) new_status FROM custom_advance_task GROUP BY company_id) cat ON cat.company_id = tc.id " +
			"${ew.customSqlSegment}")
	List<CompanyPublicVo> selectPublicCompany(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
