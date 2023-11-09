package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import may.yuntian.anlian.entity.CompanyContactEntity;



/**
 * 公司联系人
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-11-28
 */
@Mapper
public interface CompanyContactMapper extends BaseMapper<CompanyContactEntity> {

	/**
	 *根据企业ID删除公司联系人信息
	 */
	void deleteByCompanyId(Long companyId);

}
