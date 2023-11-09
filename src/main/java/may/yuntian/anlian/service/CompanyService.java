package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.dto.CompanyDto;
import may.yuntian.anlian.vo.CompanyPublicVo;
import may.yuntian.anlian.vo.CompanyVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.vo.CompanyNameVo;

import java.util.List;
import java.util.Map;

/**
 * 企业信息
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
public interface CompanyService extends IService<CompanyEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 显示全部企业信息列表
     * @return
     */
    List<CompanyNameVo> listAll();

    /**
     * 根据企业名称模糊查询企业信息列表
     * @param company
     * @return
     */
    List<CompanyNameVo> listAllByCompanyName(String company);

    /**
     *删除企业信息连带其子表也删除
     */
	void deleteContactByIds(Long[] ids);

    /**
     * 获取一个公司信息（最新）
     *
     * @param company    公司名称
     * @param dataBelong 数据归属
     * @return 公司信息
     */
    CompanyEntity getOneCompanyInfoByCompanyName(String company, String dataBelong);

    /**
     * 新查询客户表信息
     */
    List<CompanyVo> newList(CompanyDto dto, String subject, Long userId, String userName);

    /**
     * 客户公海列表
     */
    List<CompanyPublicVo> selectPublicCompany(CompanyDto dto, String companyOrder);
}

