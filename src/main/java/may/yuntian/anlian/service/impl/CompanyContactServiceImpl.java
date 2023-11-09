package may.yuntian.anlian.service.impl;




import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.mapper.CompanyContactMapper;
import may.yuntian.anlian.service.CompanyContactService;
/**
 * 公司联系人
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-11-28
 */
@Service("companyContactService")
public class CompanyContactServiceImpl extends ServiceImpl<CompanyContactMapper, CompanyContactEntity> implements CompanyContactService {

	/**
	 *根据企业ID删除公司联系人信息
	 */
	public void deleteByCompanyId(Long companyId) {
		 baseMapper.delete(new QueryWrapper<CompanyContactEntity>().eq("company_id", companyId));
	}
}
