package may.yuntian.anlian.service;



import com.baomidou.mybatisplus.extension.service.IService;

import may.yuntian.anlian.entity.CompanyContactEntity;



/**
 * 公司联系人
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-11-28
 */
public interface CompanyContactService extends IService<CompanyContactEntity> {

	/**
	 *根据企业ID删除公司联系人信息
	 */
	void deleteByCompanyId(Long companyId);

	
}