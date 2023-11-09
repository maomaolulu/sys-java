package may.yuntian.anlian.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.dto.CompanyDto;
import may.yuntian.anlian.service.CustomAdvanceTaskManageService;
import may.yuntian.anlian.vo.CompanyPublicVo;
import may.yuntian.anlian.vo.CompanyVo;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.external.oa.service.CustomAdvanceTaskService;
import may.yuntian.modules.sys.entity.SysRoleEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserRoleEntity;
import may.yuntian.modules.sys.service.SysRoleService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.modules.sys_v2.entity.TableDataInfo;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.anlian.entity.CompanyContactEntity;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.service.CompanyContactService;
import may.yuntian.anlian.service.CompanyService;
import may.yuntian.anlian.service.ContractService;
import may.yuntian.anlian.vo.CompanyNameVo;


/**
 * 企业信息管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@RestController
@Api(tags = "A~企业信息")
@RequestMapping("anlian/company")
public class CompanyController extends BaseController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ContractService contractService;//合同信息管理
    @Autowired
    private CompanyContactService companyContactService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private CustomAdvanceTaskManageService customAdvanceTaskManageService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;

    private static String TYPE_NAME_CONTACT = "contactType";//参数类型

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询企业信息列表")
    @RequiresPermissions("anlian:company:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = companyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 查询联系人类型列表
     */
    @GetMapping("/sysDictListByContact")
    @ApiOperation("查询联系人类型列表")
    @RequiresPermissions("anlian:company:list")
    public R sysDictListByContact() {
        List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_CONTACT);
        return R.ok().put("sysDictList", sysDictList);
    }
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示企业信息详情")
    @RequiresPermissions("anlian:company:info")
    public R info(@PathVariable("id") Long id) {
        CompanyEntity company = companyService.getById(id);
        return R.ok().put("company", company);
    }

    /**
     * 信息 (新)
     */
    @GetMapping("/infoNew/{id}")
    @ApiOperation("根据ID显示企业信息详情(新)")
    @RequiresPermissions("anlian:company:info")
    public R infoNew(@PathVariable("id") Long id) {
        CompanyEntity company = companyService.getById(id);
        company.setCompanyContactList(companyContactService.list(new QueryWrapper<CompanyContactEntity>().eq("company_id", id)));
        List<CustomAdvanceTaskEntity> list = customAdvanceTaskManageService.list(new QueryWrapper<CustomAdvanceTaskEntity>().eq("company_id", id).orderByDesc("id"));
        company.setBusinessStatus(0);
        if (list.size() > 0){
            company.setAdvanceId(list.get(0).getAdvanceId());
            company.setBusinessStatus(list.get(0).getBusinessStatus());
        }
        return R.ok().put("company", company);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增企业信息")
    @ApiOperation("新增企业信息")
    @RequiresPermissions("anlian:company:save")
    public R save(@RequestBody CompanyEntity company) {
        company.setDataBelong(ShiroUtils.getUserEntity().getSubjection());
        companyService.save(company);

        List<CompanyContactEntity> companyContactList = company.getCompanyContactList();
        if (companyContactList != null) {
            companyContactList.forEach(action -> action.setCompanyId(company.getId()));
            companyContactService.saveBatch(companyContactList);
        }

        return R.ok();
    }

    /**
     * 保存(新)
     */
    @PostMapping("/saveNew")
    @SysLog("新增企业信息(新)")
    @ApiOperation("新增企业信息(新)")
    @RequiresPermissions("company_v2:add")
    @Transactional(rollbackFor = Exception.class)
    public R saveNew(@RequestBody CompanyEntity company) {
        // 同公司下不能有同名客户
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_belong", ShiroUtils.getUserEntity().getSubjection());
        queryWrapper.eq("company", company.getCompany());
        List<CompanyEntity> companyEntities = companyService.list(queryWrapper);
        if (companyEntities.isEmpty()){
            company.setDataBelong(ShiroUtils.getUserEntity().getSubjection());
            company.setPersonBelong(ShiroUtils.getUserEntity().getUsername());
            companyService.save(company);

            List<CompanyContactEntity> companyContactList = company.getCompanyContactList();
            if (companyContactList != null) {
                companyContactList.forEach(action -> {
                    action.setCompanyId(company.getId());
                });
                companyContactService.saveBatch(companyContactList);
            }
            CustomAdvanceTaskEntity advanceTask = new CustomAdvanceTaskEntity();
            advanceTask.setCompanyId(company.getId());
            advanceTask.setBusinessStatus(0);
            if (company.getAdvanceId() != null){
                advanceTask.setAdvanceId(company.getAdvanceId());
                advanceTask.setBusinessStatus(1);
            }
            customAdvanceTaskManageService.add(advanceTask);
            return R.ok();
        }else {
            return R.error("本公司已有" + company.getCompany() + "此客户");
        }

    }


    /**
     * 批量保存
     */
    @PostMapping("/saveBatch")
    @SysLog("批量新增企业信息")
    @ApiOperation("批量新增企业信息")
    @RequiresPermissions("anlian:company:save")
    public R saveBatch(@RequestBody List<CompanyEntity> companyList) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        List<CompanyContactEntity> companyContactList = new ArrayList<>();
        for (CompanyEntity company : companyList) {
            company.setDataBelong(subjection);
            CompanyContactEntity companyContactEntity = new CompanyContactEntity();
            companyContactEntity.setCompanyId(company.getId());
        }
//        companyService.save(company);
//
//        List<CompanyContactEntity> companyContactList = company.getCompanyContactList();
//        if (companyContactList != null) {
//            companyContactList.forEach(action -> {
//                action.setCompanyId(company.getId());
//            });
//            companyContactService.saveBatch(companyContactList);
//        }

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改企业信息")
    @ApiOperation("修改企业信息")
    @RequiresPermissions("anlian:company:update")
    public R update(@RequestBody CompanyEntity company) {
        companyService.updateById(company);
        companyContactService.deleteByCompanyId(company.getId());
        List<CompanyContactEntity> companyContactList = company.getCompanyContactList();
        if (companyContactList != null) {
            companyContactList.forEach(action -> {
                action.setCompanyId(company.getId());
            });
            companyContactService.saveOrUpdateBatch(companyContactList);
        }

        return R.ok();
    }

    /**
     * 修改(新)
     */
    @PostMapping("/updateNew")
    @SysLog("修改企业信息(新)")
    @ApiOperation("修改企业信息(新)")
    @RequiresPermissions("company_v2:update")
    public R updateNew(@RequestBody CompanyEntity company) {
        companyService.updateById(company);
        companyContactService.deleteByCompanyId(company.getId());
        List<CompanyContactEntity> companyContactList = company.getCompanyContactList();
        if (companyContactList != null) {
            companyContactList.forEach(action -> {
                action.setCompanyId(company.getId());
            });
            companyContactService.saveOrUpdateBatch(companyContactList);
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除企业信息")
    @ApiOperation("删除企业信息")
    @RequiresPermissions("anlian:company:delete")
    public R delete(@RequestBody Long[] ids) {

        //删除前检查数据的关联性，存在关联的合同数据不允许删除
        for (long id : ids) {
            if (!contractService.notExistContractByCompany(id)) {
                return R.error("合同信息中已存在ID=[" + id + "]的企业信息，不允许删除！");
            }
        }

        companyService.deleteContactByIds(ids);

        return R.ok();
    }

    /**
     * 删除(新)
     */
    @PostMapping("/deleteNew")
    @SysLog("删除企业信息(新)")
    @ApiOperation("删除企业信息(新)")
    @RequiresPermissions("company_v2:delete")
    public R deleteNew(@RequestBody Long[] ids) {

        //删除前检查数据的关联性，存在关联的合同数据不允许删除
        for (long id : ids) {
            if (!contractService.notExistContractByCompany(id)) {
                return R.error("合同信息中已存在ID=[" + id + "]的企业信息，不允许删除！");
            }
            if (customAdvanceTaskManageService.existTaskByCompany(id)) {
                return R.error("跟进任务中存在企业信息，不允许删除！");
            }
        }

        companyService.deleteContactByIds(ids);

        return R.ok();
    }

    /**
     * 显示全部企业信息列表
     *
     * @return
     */
    @GetMapping("/listAll")
    @ApiOperation("显示全部企业信息列表")
    @RequiresPermissions("anlian:company:list")
    public R listAll() {
        List<CompanyNameVo> list = companyService.listAll();

        return R.ok().put("list", list);
    }

    /**
     * 根据企业名称模糊查询企业信息列表
     *
     * @param company
     * @return
     */
    @GetMapping("/listAllByCompanyName")
    @ApiOperation("根据企业名称模糊查询企业信息列表")
//    @RequiresPermissions("anlian:company:list")
    public R listAllByCompanyName(@RequestParam String company) {
        List<CompanyNameVo> list = companyService.listAllByCompanyName(company);

        return R.ok().put("list", list);
    }

    /**
     * 客户查询分页列表09.06
     */
    @GetMapping("/newList")
    @ApiOperation("客户查询分页列表")
    @RequiresPermissions("anlian:company:list")
    @AuthCode(url = "company",system = "sys")
    public TableDataInfo newList(CompanyDto dto, AuthCodeVo authCodeVo) {
        String subject = "",uName = "";
        Long userId = null;
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            //集团权限
            subject = "";
        }else {
            SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            uName = sysUserEntity.getUsername();
            if(sysUserEntity.getSubjection()!=null) {
                subject = sysUserEntity.getSubjection();
            }else {
                subject = "无隶属公司";
            }
            List<SysUserRoleEntity> result = userRoleService.list(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userId));
            if (result.size() > 0){
                List<Long> roleIds = result.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toList());
                List<SysRoleEntity> sysRoleEntities = roleService.list(new QueryWrapper<SysRoleEntity>().in("role_id", roleIds));
                if (sysRoleEntities.size() > 0){
                    for (SysRoleEntity role : sysRoleEntities){
                        if (role.getRoleName().contains("新客户管理") && role.getRoleName().contains("业务员")){
                            // 默认查询本公司数据,角色为业务员则查询个人
                            userId = sysUserEntity.getUserId();
                            break;
                        }
                    }
                }
            }
        }
        startPage();
        List<CompanyVo> list = companyService.newList(dto, subject, userId, uName);
        return getDataTable(list);
    }

    /**
     * 客户公海分页
     */
    @GetMapping("/companyPublicList")
    @ApiOperation("客户公海分页")
    public TableDataInfo companyPublicList(CompanyDto dto, AuthCodeVo authCodeVo) {
        String companyOrder = "";
        if (!IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            //分公司权限
            companyOrder = ((SysUserEntity) ShiroUtils.getSubject().getPrincipal()).getSubjection();
        }
        startPage();
        return getDataTable(companyService.selectPublicCompany(dto, companyOrder));
    }

    /**
     * 业务员主动领取
     */
    @PostMapping("/receive")
    @ApiOperation("业务员主动领取")
    public Result receive(@RequestBody CompanyEntity entity){
        CustomAdvanceTaskEntity one = new CustomAdvanceTaskEntity();
        one.setCompanyId(entity.getId());
        one.setAdvanceId(((SysUserEntity) ShiroUtils.getSubject().getPrincipal()).getUserId());
        one.setBusinessStatus(1);
        return customAdvanceTaskManageService.add(one) == 1 ? Result.ok("领取成功") : Result.error("领取异常");
    }

}
