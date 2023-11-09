package may.yuntian.anlian.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang.WordUtils;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.MoneyVo;
import may.yuntian.common.exception.RRException;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.annotation.JsonAlias;
//import com.google.gson.JsonArray;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.AccountEntity;
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.AccountService;
import may.yuntian.anlian.service.CommissionService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.modules.sys.service.SysUserService;

/**
 *收付款记录管理
 * @author LiXin
 * @date 2020-11-05
 */

@RestController
@Api(tags="收付款记录管理")
@RequestMapping("anlian/account")
public class AccountController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private ProjectDateService projectDateService;
	
	private static String TYPE_NAME = "commissionRatio";//参数类型
	
	/**
	 *列表
	 */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询收付款记录列表")
    @RequiresPermissions("anlian:account:list")
	@AuthCode(url = "receiptsPayments",system = "sys")
    public Result list(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo){
		// 登录人隶属公司
		String subjection = ShiroUtils.getUserEntity().getSubjection();
		// 权限验证码
		String authCode = authCodeVo.getAuthCode();
		if (IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
			// 职能权限:集团
			params.put("subjection", "ces");
		} else {
			// 职能权限:分公司
			params.put("subjection", subjection);
		}
		List<AccountEntity> list = accountService.queryPage(params);

        MoneyVo queryMap = new MoneyVo();

        queryMap = accountService.sumAmountByWrapper(params);

        return Result.resultData(list).put("queryMap", queryMap);
    }
    
    
    /**
     * 根据项目ID获取收付款记录列表
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取收付款记录列表")
    @RequiresPermissions("anlian:account:list")
    public R listByProjectId(@PathVariable("projectId") Long projectId){
    	List<AccountEntity> list = accountService.listByProjectId(projectId);

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", String.valueOf(projectId));
    	PageUtils queryPageByAccount = projectService.queryPage(params);

    	return R.ok().put("list", list).put("queryPageByAccount", queryPageByAccount);
    }


	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示收付款记录详情")
	@RequiresPermissions("anlian:account:info")
	public R info(@PathVariable("id") Long id) {
		AccountEntity account = accountService.getById(id);

		return R.ok().put("account", account);
	}
    

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增收付款记录")
    @ApiOperation("新增收付款记录")
    @RequiresPermissions("anlian:account:save")
    public R save(@RequestBody AccountEntity account){
    	
//    	String nString = WordUtils.capitalizeFully(json_str, newCommission char[]{'_'}).replace("_", "" );
//		System.out.println("..............."+nString);
//    	* account = (*) JSON.parse(nString);
    	
    	//用户名
    	SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
    	Long userid = sysUserEntity.getUserId();
		String username = sysUserEntity.getUsername();
		account.setUserid(userid);
		account.setUsername(username);
		
    	accountService.save(account);
    	
    	//项目款
    	if(account.getAcType() == 1) {
    		//当是项目款时 将收付时间回填至项目表中
    		Long projectId = account.getProjectId();
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setReceiveAmount(account.getHappenTime());
            projectDateService.updateById(projectDateEntity);
    		
    		projectCommission(account);//项目提成计算
    	}
        if(account.getAcType() == 2){//开票日期回填
            //当是项目款时 将收付时间回填至项目表中
            Long projectId = account.getProjectId();
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setInvoiceDate(account.getHappenTime());
            projectDateService.updateById(projectDateEntity);
        }
        accountService.amountBackfill(account.getProjectId());//虚拟税费，项目已收款，开票金额回填至项目中
    	
        return R.ok().put("account", account);
    }
    
    /**
     * 导入  批量保存
     */
    @PostMapping("/importSave")
    @SysLog("导入收付款记录")
    @ApiOperation("导入收付款记录")
    @RequiresPermissions("anlian:account:save")
    @Transactional(rollbackFor = Exception.class)
    public R importSave(@RequestBody List<AccountEntity> accountList){
    	//用户名
    	SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
    	Long userid = sysUserEntity.getUserId();
		String username = sysUserEntity.getUsername();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy/MM/dd");
//		String time = "2019-09-19";
//		Date date = ft.parse(time)
		for(AccountEntity account:accountList) {
//            String idenfier = project.getIdentifier();
			String identifierStr = account.getIdentifier();
			String identifier = identifierStr.trim();
			Long projectId = projectService.getIdByIdentifier(identifier);
            if (projectId == null){
                new RRException(identifier+ "项目未找到");
            }
			String happendtimeStr = account.getHappenTimeStr();
			try {
                Date happendTime;
                if (happendtimeStr.contains("/")){
                    happendTime = ft1.parse(happendtimeStr);
                }else {
                    happendTime = ft.parse(happendtimeStr);
                }
				account.setHappenTime(happendTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
//			account.setHappenTime(happendTime);
			account.setProjectId(projectId);
			account.setUserid(userid);
			account.setUsername(username);
			
	    	accountService.save(account);
	    	
	    	//项目款
	    	if(account.getAcType() == 1) {
	    		
	    		//当是项目款时 将收付时间回填至项目表中

                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
                projectDateEntity.setReceiveAmount(account.getHappenTime());
                projectDateService.updateById(projectDateEntity);
	    		
	    		projectCommission(account);//项目提成计算
	    	}
            if(account.getAcType() == 2){//开票日期回填
                //当是开票时 将收付时间回填至项目表中
                ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
                projectDateEntity.setInvoiceDate(account.getHappenTime());
                projectDateService.updateById(projectDateEntity);
            }
            accountService.amountBackfill(account.getProjectId());//虚拟税费，项目已收款，开票金额回填至项目中
		}
		
		
		
    	
        return R.ok();
    } 
    
    
    
    /**
     * 项目提成计算
     * @param account
     */
    private void projectCommission(AccountEntity account) {
//    	计算收款情况，如果完成收款，则初始化项目提成记录
    	Long projectId = account.getProjectId();
    	ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
    	//根据项目ID与款项类别查询收付款记录
    	BigDecimal totalMoneySettled = accountService.getAmountByProjectIdAndType(projectId,1);//项目款类型1

		ProjectEntity project = projectService.getById(projectId);
    	BigDecimal totalMoney = project.getTotalMoney();
    	BigDecimal netvalue = project.getNetvalue();

//    	System.out.println("totalMoney:"+totalMoney+",netvalue="+netvalue+",totalMoneySettled="+totalMoneySettled);
//    	System.out.println("是否完成收款判断："+totalMoney.equals(totalMoneySettled) );

    	Long salesmenId = project.getSalesmenid();
    	String salesmen = "";
    	String subjection="";
    	if (salesmenId!=null) {
    		salesmen = project.getSalesmen();
    		SysUserEntity sysUserEntity = sysUserService.getById(salesmenId);
        	subjection = sysUserEntity.getSubjection();
		}


    	if(totalMoney.equals(totalMoneySettled)) {

    		SysDictEntity sysDict = sysDictService.queryByTypeAndCode(TYPE_NAME, "1");
        	Double commissionRatioDouble = Double.valueOf(sysDict.getValue());
        	BigDecimal commissionRatio = BigDecimal.valueOf(commissionRatioDouble);//提成比例
        	BigDecimal cmsAmount = netvalue.multiply(commissionRatio);//项目净值*提成比例
//        	System.out.println("commissionRatioDouble:"+commissionRatioDouble+",commissionRatio="+commissionRatio+",cmsAmount="+cmsAmount);
        	CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId, "业务提成");
        	if (commissionEntity == null) {
        		CommissionEntity commission = new CommissionEntity();
        		commission.setProjectId(projectId);
        		commission.setCommissionDate(projectDateEntity.getReceiveAmount());
        		commission.setPersonnel(project.getSalesmen());
        		commission.setType("业务提成");
        		commission.setState(1);
        		commission.setSubjection(subjection);
        		commission.setCmsAmount(cmsAmount);
        		commissionService.save(commission);
			}else if (!commissionEntity.getCmsAmount().equals(cmsAmount)) {
				commissionEntity.setCmsAmount(cmsAmount);
				commissionEntity.setPersonnel(salesmen);
				commissionEntity.setSubjection(subjection);
				commissionEntity.setCommissionDate(projectDateEntity.getReceiveAmount());
				commissionService.updateById(commissionEntity);
			}

    	}
    	
	}


    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改收付款记录")
    @ApiOperation("修改收付款记录")
    @RequiresPermissions("anlian:account:update")
    public R update(@RequestBody AccountEntity account){
    	//用户名
    	SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
    	Long userid = sysUserEntity.getUserId();
		String username = sysUserEntity.getUsername();
		account.setEditorId(userid);
		account.setEditorName(username);
		
    	accountService.updateById(account);
    	
    	if(account.getAcType() == 1) { //收款日期回填
    		//当是项目款时 将收付时间回填至项目表中
    		Long projectId = account.getProjectId();
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setReceiveAmount(account.getHappenTime());
            projectDateService.updateById(projectDateEntity);
    		
    		projectCommission(account);//项目提成计算
    	}
        if(account.getAcType() == 2){//开票日期回填
            //当是项目款时 将收付时间回填至项目表中
            Long projectId = account.getProjectId();
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            projectDateEntity.setInvoiceDate(account.getHappenTime());
            projectDateService.updateById(projectDateEntity);
        }

        accountService.amountBackfill(account.getProjectId());//虚拟税费，项目已收款，开票金额回填至项目中
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除收付款记录") 
    @ApiOperation("删除收付款记录") 
    @RequiresPermissions("anlian:account:delete")
    public R delete(@RequestBody Long[] ids){
    	AccountEntity account = new AccountEntity();
    	for(Long id : ids) {
    		account = accountService.getById(id);
    	}
    	accountService.removeByIds(Arrays.asList(ids));
    	BigDecimal totalMoneySettled = accountService.getAmountByProjectIdAndType(account.getProjectId(),1);//项目款类型1
		ProjectEntity projectEntity = projectService.getById(account.getProjectId());
		if(!totalMoneySettled.equals(projectEntity.getTotalMoney())) {
			CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(account.getProjectId(), "业务提成");
    		if(commissionEntity!=null) {
    			commissionService.removeById(commissionEntity.getId());
    		}
		}else {
            projectCommission(account);
        }
    	
    	accountService.amountBackfill(account.getProjectId());//虚拟税费，项目已收款，开票金额回填至项目中
        return R.ok();
    }

    
    
    /**
     * 显示全部收付款信息列表
     * @return
     */
    @GetMapping("/listAll")
    @ApiOperation("显示全部收付款信息列表")
    @RequiresPermissions("anlian:account:list")
    @AuthCode(url = "receiptsPayments",system = "sys")
    public R listAll(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo){
        // 登录人隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            params.put("subjection", "ces");
        } else {
            // 职能权限:分公司
            params.put("subjection", subjection);
        }
        List<AccountEntity> list = accountService.listAll(params);
        MoneyVo queryMap = new MoneyVo();
        queryMap = accountService.sumAmountByWrapper(params);
//        MoneyVo moneyVo = new MoneyVo();
//        moneyVo.setAmount(queryMap.get("amount"));
//        moneyVo.setTotalMoney(queryMap.get("totalMoney"));
//        moneyVo.setInvoiceAmount(queryMap.get("invoiceAmount"));
        return R.ok().put("list", list).put("queryMap", queryMap);
    }
    
}
