package may.yuntian.anlian.service.impl;

import may.yuntian.anlian.dto.CompanyDto;
import may.yuntian.anlian.vo.CompanyPublicVo;
import may.yuntian.anlian.vo.CompanyVo;
import may.yuntian.modules.sys.entity.SysRoleEntity;
import may.yuntian.modules.sys.entity.SysUserRoleEntity;
import may.yuntian.modules.sys.service.SysRoleService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.CompanyMapper;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.service.CompanyContactService;
import may.yuntian.anlian.service.CompanyService;
import may.yuntian.anlian.vo.CompanyNameVo;

/**
 * 企业信息
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Service("companyService")
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, CompanyEntity> implements CompanyService {
    @Autowired
    private CompanyContactService companyContactService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        String company = (String) params.get("company");
        String username = (String) params.get("username");
        String contact = (String) params.get("contact");
        String mobile = (String) params.get("mobile");
        String riskLevel = (String) params.get("riskLevel");
        //最近合作日期contractLast  (开始和结束时间)
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        IPage<CompanyEntity> page = this.page(
                new Query<CompanyEntity>().getPage(params),
                // 支持分流
                new QueryWrapper<CompanyEntity>().eq(StringUtils.isNotBlank(subjection), "data_belong", subjection)
                        .like(StringUtils.isNotBlank(company), "company", company)
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(contact), "contact", contact)
                        .like(StringUtils.isNotBlank(mobile), "mobile", mobile)
                        .eq(StringUtils.checkValNotNull(riskLevel), "risk_level", riskLevel)
                        .between(StringUtils.isNotBlank(endDate), "contract_last", startDate, endDate)
                        .orderByDesc("id")
        );
        page.getRecords().forEach(action -> {
            Long companyId = action.getId();
            List<CompanyContactEntity> companyContactList = companyContactService.list(
                    new QueryWrapper<CompanyContactEntity>()
                            .eq("company_id", companyId)
            );
            action.setCompanyContactList(companyContactList);
        });

        return new PageUtils(page);
    }

    /**
     * 显示全部企业信息列表
     *
     * @return
     */
    public List<CompanyNameVo> listAll() {
        List<CompanyEntity> list = this.list(
                new QueryWrapper<CompanyEntity>()
                        .select("id", "company", "province", "city", "area", "office_address", "contact", "mobile")
                        .last("limit 50")
                        .orderByDesc("id")
        );
        List<CompanyNameVo> result = ObjectConversion.copy(list, CompanyNameVo.class);
        return result;
    }

    /**
     * 根据企业名称模糊查询企业信息列表
     *
     * @param company
     * @return
     */
    public List<CompanyNameVo> listAllByCompanyName(String company) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        List<CompanyEntity> list = this.list(new QueryWrapper<CompanyEntity>()
                .select("id", "company", "province", "city", "area", "office_address", "contact", "mobile")
                .eq(StringUtils.isNotBlank(subjection), "data_belong", subjection)
                .like(StringUtils.isNotBlank(company), "company", company)
                .last("limit 50").orderByDesc("id"));
        List<CompanyNameVo> result = ObjectConversion.copy(list, CompanyNameVo.class);
//    	List<CompanyNameVo> result = newCommission ArrayList<CompanyNameVo>(list.size());
//    	CompanyNameVo vo;
//    	for(CompanyEntity entity : list) {
//    		vo = newCommission CompanyNameVo();
//    		BeanUtils.copyProperties(entity, vo);
//    		result.add(vo);
//    	}

        return result;
    }

    /**
     * 删除企业信息连带其子表也删除
     */
    @Override
    public void deleteContactByIds(Long[] ids) {
        for (Long id : ids) {
            companyContactService.deleteByCompanyId(id);
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 获取一个公司信息（最新）
     *
     * @param company    公司名称
     * @param dataBelong 数据归属
     * @return 公司信息
     */
    @Override
    public CompanyEntity getOneCompanyInfoByCompanyName(String company, String dataBelong) {
        return this.getOne(new QueryWrapper<CompanyEntity>()
                .select("id", "company", "province", "city", "area", "office_address", "contact", "mobile")
                .eq("data_belong", dataBelong)
                .eq("company", company)
                .orderByDesc("createtime").last("limit 1"));
    }

    @Override
    public List<CompanyVo> newList(CompanyDto dto, String subject, Long userId, String userName) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(subject)) {
            // 非集团权限 区分业务员和业务经理 新单查询隶属于公司或者跟进人为自己 旧单查询隶属于公司
            if (userId != null) {
                //业务员查看本公司下已完成跟进任务的公单和隶属于自己的私单
                queryWrapper.and(wrapper -> wrapper.and(wrapper1 -> wrapper1.eq("tc.data_belong", subject).eq("tc.if_has_finished", 1)).or(wrapper2 -> wrapper2.eq("tc.if_has_finished", 0).eq("tc.person_belong", userName)));
            } else {
                //业务经理或其他
                queryWrapper.eq("tc.data_belong", subject);
            }

        }
        if (StringUtils.isNotBlank(dto.getDataBelong())) {
            queryWrapper.and(wrapper -> wrapper.eq("IF(tc.if_has_finished = 0 && tc.person_belong != '',tc.person_belong, tc.data_belong)", dto.getDataBelong()));
        }
        queryWrapper.like(StringUtils.isNotBlank(dto.getCompany()), "tc.company", dto.getCompany());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getProvince()), "tc.province", dto.getProvince());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getCity()), "tc.city", dto.getCity());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getArea()), "tc.area", dto.getArea());
        queryWrapper.ge(dto.getContractLastStart() != null, "tc.contract_last", dto.getContractLastStart());
        queryWrapper.le(dto.getContractLastEnd() != null, "tc.contract_last", dto.getContractLastEnd());
        queryWrapper.eq(dto.getStatus() != null, "tc.status", dto.getStatus());
        Integer businessStatus = dto.getBusinessStatus();
        if (businessStatus != null) {
            if (businessStatus == 0) {
                queryWrapper.eq("ifnull(cat.business_status,0)", businessStatus);
            } else {
                queryWrapper.eq("cat.business_status", businessStatus);
            }
        }
        queryWrapper.orderByAsc("tc.`status`", "tc.contract_last");
        return baseMapper.selectList1(queryWrapper);
    }

    @Override
    public List<CompanyPublicVo> selectPublicCompany(CompanyDto dto, String companyOrder) {
        QueryWrapper<Object> query = new QueryWrapper<>();
        query.eq(StringUtils.isNotBlank(companyOrder), "tc.data_belong",companyOrder);
        query.eq("tc.`status`", 0);
        query.eq("tc.if_has_finished", 1);
        query.and(a -> a.eq("cat.new_status", 10).or().isNull("cat.new_status"));
        query.eq(StringUtils.isNotBlank(dto.getDataBelong()), "tc.data_belong", dto.getDataBelong());
        query.like(StringUtils.isNotBlank(dto.getCompany()), "tc.company", dto.getCompany());
        query.eq(StringUtils.isNotBlank(dto.getProvince()), "tc.province", dto.getProvince());
        query.eq(StringUtils.isNotBlank(dto.getCity()), "tc.city", dto.getCity());
        query.eq(StringUtils.isNotBlank(dto.getArea()), "tc.area", dto.getArea());
        query.ge(dto.getContractLastStart() != null, "tc.contract_last", dto.getContractLastStart());
        query.le(dto.getContractLastEnd() != null, "tc.contract_last", dto.getContractLastEnd());
        return baseMapper.selectPublicCompany(query);
    }

}
