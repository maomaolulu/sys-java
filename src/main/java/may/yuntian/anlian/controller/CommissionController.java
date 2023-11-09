package may.yuntian.anlian.controller;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.service.CommissionService;

/**
 * 提成记录信息管理
 * WEB请求处理层
 *
 * @author LiXin
 * @date 2020-12-09
 */
@RestController
@Api(tags = "提成记录信息")
@RequestMapping("anlian/commission")
public class CommissionController {
    @Autowired
    private CommissionService commissionService;

    @Autowired
    private SysDictService sysDictService;//数据字典

    private static String TYPE_NAME_COMMISSIONTYPE = "commissionType";//参数类型

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询提成记录信息列表")
    @RequiresPermissions("anlian:commission:list")
    @AuthCode(url = "commission", system = "sys")
    public R list(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
        // 登录人隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            params.put("subjection", "");
        } else {
            // 职能权限:分公司
            params.put("subjection", subjection);
        }
        PageUtils page = commissionService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 老绩效提成列表--本公司
     */
    @GetMapping("/ourCompanyList")
    @ApiOperation("根据条件分页查询提成记录信息列表")
    @RequiresPermissions("anlian:commission:list")
    public R ourCompanyList(@RequestParam Map<String, Object> params) {

        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        if (sysUserEntity.getSubjection() != null) {
            params.put("subjection", sysUserEntity.getSubjection());
        } else {
            params.put("subjection", "无隶属公司");
        }

        PageUtils page = commissionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 老绩效提成列表--跨公司
     */
    @GetMapping("/crossCompanyList")
    @ApiOperation("根据条件分页查询提成记录信息列表")
    @RequiresPermissions("anlian:commission:list")
    public R crossCompanyList(@RequestParam Map<String, Object> params) {

        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        if (sysUserEntity.getSubjection() != null) {
            params.put("other", sysUserEntity.getSubjection());
        } else {
            params.put("subjection", "无隶属公司");
        }

        PageUtils page = commissionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示提成记录信息详情")
    @RequiresPermissions("anlian:commission:info")
    public R info(@PathVariable("id") Long id) {
        CommissionEntity commission = commissionService.getById(id);

        return R.ok().put("commission", commission);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增提成记录信息")
    @ApiOperation("新增提成记录信息")
    @RequiresPermissions("anlian:commission:save")
    public R save(@RequestBody CommissionEntity commission) {
        commissionService.save(commission);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改提成记录信息")
    @ApiOperation("修改提成记录信息")
    @RequiresPermissions("anlian:commission:update")
    public R update(@RequestBody CommissionEntity commission) {
        commissionService.updateById(commission);

        return R.ok();
    }

    /**
     * 根据查询条件伪删除  将状态改为4
     */
    @PostMapping("/fakeDelete")
    @SysLog("根据查询条件伪删除提成记录信息")
    @ApiOperation("根据查询条件伪删除提成记录信息")
    @RequiresPermissions("anlian:commission:delete")
    public R fakeDelete(@RequestParam Map<String, Object> params) {
        commissionService.updateStateByParams(params);
        return R.ok();
    }

    /**
     * 伪删除  将状态改为4
     */
    @PostMapping("/delete")
    @SysLog("伪删除提成记录信息")
    @ApiOperation("伪删除提成记录信息")
    @RequiresPermissions("anlian:commission:delete")
    public R updateState(@RequestBody Long[] ids) {
        List<CommissionEntity> commissionList = commissionService.listByIds(Arrays.asList(ids));
        commissionList.forEach(action -> {
            action.setState(4);
        });
        commissionService.updateBatchById(commissionList);
        return R.ok();
    }

    /**
     * 财务中心-绩效提成：导出
     */
    @GetMapping("/listAll")
    @RequiresPermissions("anlian:commission:list")
    @AuthCode(url = "commission", system = "sys")
    public void listAll(HttpServletResponse response, @RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            params.put("subjection", "");
        } else {
            // 职能权限:分公司
            params.put("subjection", subjection);
        }
        commissionService.listAll(response, params);
    }

    /**
     * 查询提成类型列表
     */
    @GetMapping("/sysDictListByCommission")
    @ApiOperation("查询提成类型列表")
    @RequiresPermissions("anlian:commission:list")
    public R sysDictListByCommission() {
        List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_COMMISSIONTYPE);
        return R.ok().put("sysDictList", sysDictList);
    }


    /**
     * 根据时间段生成提成记录
     */
    @PostMapping("/getListByCommissionDate")
    @SysLog("根据时间段生成提成记录")
    @ApiOperation("根据时间段生成提成记录")
    @RequiresPermissions("anlian:commission:update")
    public R getListByCommissionDate(@RequestBody CommissionEntity commission) {
        List<CommissionEntity> list = commissionService.getListByCommissionDate(commission);

        return R.ok().put("list", list);
    }


}
