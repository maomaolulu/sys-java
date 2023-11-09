package may.yuntian.anlian.controller;

import java.util.Map;
import java.util.Arrays;
import java.util.List;

import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.utils.ObjectConversion;
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
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.anlian.service.ContractService;
import may.yuntian.anlian.vo.ContractStatisticVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 合同信息管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@RestController
@Api(tags="B~合同信息")
@RequestMapping("anlian/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;//合同信息管理
    
	@Autowired
	private SysDictService sysDictService;//数据字典
	
	private static String TYPE_NAME_PROJECT = "projectType";//参数类型
	
	private static String TYPE_NAME_MEMBERSHIP = "membershipType";//参数类型
	@Autowired
	private ProjectService projectService;
	@Autowired
    private ProjectDateService projectDateService;


    /**
     * 用于普通用户的分页查询列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询合同信息列表,用于普通用户")
    @RequiresPermissions("anlian:contract:list")
    @AuthCode(system = "sys",url = "contract")
    public R list(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo){

        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            String subjection = ShiroUtils.getUserEntity().getSubjection();
            params.put("subjection",subjection);
        }

        PageUtils page = contractService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息导出
     */
    @GetMapping("/export")
    @ApiOperation("根据条件分页查询合同信息列表,用于普通用户")
    @RequiresPermissions("anlian:contract:export")
    @AuthCode(system = "sys",url = "contract")
    public void export(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo, HttpServletResponse response){

        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            String subjection = ShiroUtils.getUserEntity().getSubjection();
            params.put("subjection",subjection);
        }

        contractService.exportList(response,params);
    }
    
    /**
     *查询合同类型列表
     */
    @GetMapping("/sysDictListByContract")
    @ApiOperation("查询合同类型列表")
//    @RequiresPermissions("anlian:contract:list")
    public R sysDictListByContract(){
    	List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_PROJECT);
    	return R.ok().put("sysDictList", sysDictList);
    }
    
    /**
     *查询合同隶属公司列表
     */
    @GetMapping("/sysDictListByMembershipType")
    @ApiOperation("查询合同隶属公司列表")
//    @RequiresPermissions("anlian:contract:list")
    public R sysDictListByMembershipType(){
    	List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_MEMBERSHIP);
    	return R.ok().put("sysDictList", sysDictList);
    }

    
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示合同信息详情")
    @RequiresPermissions("anlian:contract:info")
    public R info(@PathVariable("id") Long id){
        ContractEntity contract = contractService.getById(id);

        return R.ok().put("contract", contract);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增合同信息")
    @ApiOperation("新增合同信息")
    @RequiresPermissions("anlian:contract:save")
    public R save(@RequestBody ContractEntity contract){
    	//用户名
    	SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
    	Long userid = sysUserEntity.getUserId();
		String username = sysUserEntity.getUsername();
		
		contract.setUserid(userid);
		contract.setUsername(username);
		
		 // 项目编号
    	String projectIdentifier=contract.getProjectIdentifier();
    	// 项目状态
    	Integer projectStatus = contract.getProjectStatus();
    	//所属部门ID
    	Long projectDeptId = contract.getProjectDeptId();
		//项目类型
		String projectType = contract.getProjectType();
    	
		if(!contractService.notExistContractByIdentifier(contract.getIdentifier())) {
			return R.error("该合同编号已被占用，请更改！");
		}
		 //保存时判断数据库中是否存在此项目编号 如果存在提示
		if(projectIdentifier != null && !projectService.notExistContractByIdentifier(projectIdentifier)) {
			return R.error("该项目编号已被占用，请更改！");
		}
      
		ProjectEntity projectEntity = ObjectConversion.copy(contract, ProjectEntity.class);
		
    	contractService.save(contract);
    	
    	projectEntity.setSubprojectFee(contract.getSubcontractFee());
    	projectEntity.setIdentifier(projectIdentifier);
    	projectEntity.setStatus(projectStatus);
    	projectEntity.setDeptId(projectDeptId);
    	projectEntity.setType(projectType);
    	projectEntity.setContractId(contract.getId());
    	projectEntity.setContractIdentifier(contract.getIdentifier());
    	projectEntity.setEntrustDate(contract.getCommissionDate());
    	projectEntity.setSignDate(contract.getSignDate());
    	projectService.saveProject(projectEntity, true);//项目信息初始化 true金额回填到合同，false不回填
        
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改合同信息")
    @ApiOperation("修改合同信息")
    @RequiresPermissions("anlian:contract:update")
    public R update(@RequestBody ContractEntity contract){
    	
    	ContractEntity contractEntity = contractService.getById(contract.getId());
    	if(!contractEntity.getIdentifier().equals(contract.getIdentifier())) {
    		List<ProjectEntity> projectList = projectService.selectListByContractId(contract.getId());
    		if(projectList.size()>0) {
    			for(ProjectEntity project : projectList) {
    				project.setContractIdentifier(contract.getIdentifier());
    				projectService.updateById(project);
    			}
    		}
    	}
    	
    	
        contractService.updateContractById(contract);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除合同信息") 
    @ApiOperation("删除合同信息") 
    @RequiresPermissions("anlian:contract:delete")
    public R delete(@RequestBody Long[] ids){
    	//删除前检查数据的关联性，存在关联的合同数据不允许删除
    	for (long id : ids) {
    		if(projectService.notExistContractByContractId(id)) {
    			return R.error("该项目正在进行中ID=["+id+"]的项目信息，不允许删除！");
    		}
		}
    	
        contractService.removeByIds(Arrays.asList(ids));//删除（根据ID 批量删除）合同信息，并将其父级中的子集数量减1

        return R.ok();
    }

    /**
     * 统计报表：统计合同签定日期段内业务员的合同签订数量及总额
     */
    @GetMapping("/reportAmountBySalesmen")
    @ApiOperation("统计报表：统计合同签定日期段内业务员的合同签订数量及总额")
    @RequiresPermissions("anlian:contract:list")
    public R reportAmountBySalesmen(ContractStatisticVo params){
    	List<Map<String, Object>> list = contractService.getReportAmountBySalesmen(params);
    	return R.ok().put("list", list);
    }
    
    /**
     * 统计报表：根据合同签定日期分组统计合同签订数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    @GetMapping("/reportAmountBySignDate")
    @ApiOperation("统计报表：根据合同签定日期分组统计合同签订数量及总额,统计日期类型(年Y,月M,周W,日D)")
    @RequiresPermissions("anlian:contract:list")
    public R reportAmountBySignDate(ContractStatisticVo params){
    	List<Map<String, Object>> list = contractService.getReportAmountBySignDate(params);
    	return R.ok().put("list", list);
    }
    
    /**
     * 统计报表：根据合同签定日期与业务员分组统计合同签订数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    @GetMapping("/reportAmountBySignDateAndSalesmen")
    @ApiOperation("统计报表：根据合同签定日期与业务员分组统计合同签订数量及总额,统计日期类型(年Y,月M,周W,日D)")
    @RequiresPermissions("anlian:contract:list")
    public R reportAmountBySignDateAndSalesmen(ContractStatisticVo params){
    	List<Map<String, Object>> list = contractService.getReportAmountBySignDateAndSalesmen(params);
    	return R.ok().put("list", list);
    }
    
    /**
     * 统计报表：根据合同签定日期与合同类型分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    @GetMapping("/reportAmountBySignDateAndType")
    @ApiOperation("统计报表：根据合同签定日期与合同类型分组显示合同数量及总额,统计日期类型(年Y,月M,周W,日D)")
    @RequiresPermissions("anlian:contract:list")
    public R reportAmountBySignDateAndType(ContractStatisticVo params){
    	List<Map<String, Object>> list = contractService.getReportAmountBySignDateAndType(params);
    	return R.ok().put("list", list);
    }
    
    /**
     * 统计报表：根据合同签定日期与委托类型分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    @GetMapping("/reportAmountBySignDateAndEntrustType")
    @ApiOperation("统计报表：根据合同签定日期与委托类型分组显示合同数量及总额,统计日期类型(年Y,月M,周W,日D)")
    @RequiresPermissions("anlian:contract:list")
    public R reportAmountBySignDateAndEntrustType(ContractStatisticVo params){
    	List<Map<String, Object>> list = contractService.getReportAmountBySignDateAndEntrustType(params);
    	return R.ok().put("list", list);
    }
   
}

