package may.yuntian.anlian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.dto.ExportAccountingDto;
import may.yuntian.anlian.dto.ProjectAccountingDto;
import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.mapper.CategoryMapper;
import may.yuntian.anlian.mapper.ContractMapper;
import may.yuntian.anlian.service.*;
import may.yuntian.anlian.utils.RequestHolder;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.*;
import may.yuntian.anlianwage.newCommission.dto.NewCommissionTempDto;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.modules.sys_v2.utils.SpringUtils;
import may.yuntian.modules.sys_v2.utils.poi.ExcelUtil;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 项目表(包含了原任务表的字段)管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@RestController
@SuppressWarnings("all")
@Api(tags = "项目表")
@RequestMapping("anlian/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SysDictService sysDictService;//数据字典

    private static String TYPE_NAME_PROJECT = "projectType";//参数类型

    private static String TYPE_NAME_MEMBERSHIP = "membershipType";//参数类型
    @Autowired
    private SysUserService sysUserService;

    @Value("${apiPath.pythonPjPdfApi}")
    private String pythonPjApiPath;
    @Value("${apiPath.pythonZjPdfApi}")
    private String pythonZjApiPath;
//    @Autowired
//    private ProjectCountService projectCountService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectAmountService projectAmountService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private CompanySurveyService companySurveyService;


    @GetMapping("/testLink")
    @ApiOperation("运行测试接口,不需token")
//    @RequiresPermissions("anlian:project:list")
    public Result testLink(HttpServletRequest httpRequest) {
        String host = httpRequest.getHeader("Host");
        String a = pythonPjApiPath;
        System.out.println("a = " + a);
        String b = pythonZjApiPath;
        System.out.println("b = " + b);


        return Result.ok().put("msg", "测试通过,访问成功! Host是http://" + host);
    }

    /**
     * 列表
     */
//    @GetMapping("/list")
//    @ApiOperation("根据条件分页查询项目列表")
//    @RequiresPermissions("anlian:project:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = projectService.queryPage(params);
//        Map<String, Object> map = projectService.sumMoneyByParams(params);
//
//        return R.ok().put("page", page).put("map",map);
//    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询项目列表")
    @RequiresPermissions("anlian:project:list")
    @AuthCode(url = "projectManage", system = "sys")
    public Result list(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
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
        List<ProjectEntity> queryProjectAmountDateVoList = projectService.getPageList(params);
        MoneyVo stringBigDecimalMap = projectService.sumMoneyByParams(params);

        return Result.resultData(queryProjectAmountDateVoList).put("map", stringBigDecimalMap);
    }

    /**
     * 收付款项页面列表
     */
    @GetMapping("/paymentReceiptList")
    @ApiOperation("根据条件分页查询项目列表")
    @RequiresPermissions("anlian:project:list")
    @AuthCode(url = "account", system = "sys")
    public Result paymentReceiptList(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
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
        List<ProjectEntity> queryProjectAmountDateVoList = projectService.getPaymentReceiptList(params);
        MoneyVo stringBigDecimalMap = projectService.getPaymentReceiptSumMoneyByParams(params);
//.put("map",map)
        return Result.resultData(queryProjectAmountDateVoList).put("map", stringBigDecimalMap);
    }

    /**
     * 实验室页面
     */
    @GetMapping("/listAndChild")
    @ApiOperation("根据条件分页查询项目列表")
    @RequiresPermissions("anlian:project:list")
    public R listAndChild(@RequestParam Map<String, Object> params) {
        PageUtils page = projectService.queryPageByChilden(params);

        return R.ok().put("page", page);
    }

    /**
     * 任务下发页面
     */
    @GetMapping("/taskDistribution")
    @ApiOperation("根据条件分页查询项目列表")
    @RequiresPermissions("anlian:project:list")
    public R taskDistribution(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        if (sysUserEntity.getSubjection().contains("杭州")) {
            params.put("subjection", "");
        } else {
            params.put("subjection", sysUserEntity.getSubjection());
        }
        PageUtils page = projectService.taskDistribution(params);

        return R.ok().put("page", page);
    }

    /**
     * 项目录入页面
     */
    @GetMapping("/projectEntry")
    @ApiOperation("根据条件分页查询项目列表")
    @RequiresPermissions("anlian:project:list")
    @AuthCode(url = "project", system = "sys")
    public R projectEntry(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
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


        PageUtils page = projectService.projectEntry(params);

        return R.ok().put("page", page);
    }

    /**
     * 项目归档列表
     */
    @GetMapping("/queryMyProjectAchive")
    @ApiOperation("根据条件分页查询项目归档列表")
    @RequiresPermissions("anlian:project:list")
    public R queryMyProjectAchive(@RequestParam Map<String, Object> params) {
        PageUtils page = projectService.queryMyProjectAchive(params);

        return R.ok().put("page", page);
    }

    /**
     * 报告接收时间
     */
    @PostMapping("/signIssue")
    @SysLog("报告接收")
    @ApiOperation("报告接收")
    @RequiresPermissions("anlian:project:update")
    public R signIssue(@RequestBody ProjectAchiveVo projectAchiveVo) {
        ProjectEntity projectEntity = projectService.getById(projectAchiveVo.getId());
        if (StringUtils.isBlank(projectEntity.getCharge())) {
            return R.error("未填写项目负责人，无法接收报告。");
        }
        boolean a = projectService.signIssue(projectAchiveVo);
        if (!a) {
            return R.error("未填写技术打分,无法计算提成");
        } else {
            return R.ok();
        }

    }


    /**
     * 实验室收样
     */
    @PostMapping("/receiveTime")
    @SysLog("实验室收样")
    @ApiOperation("实验室收样")
//    @RequiresPermissions("anlian:project:save")
    public R receiveTime(@RequestBody ProjectEntity project) {
        Long projectId = project.getId();
        ProjectEntity projectEntity = projectService.getById(projectId);
        if (StringUtils.isBlank(projectEntity.getCharge())) {
            return R.error("未填写负责人");
        }
        boolean a = projectService.receiveTime(project);
        if (!a) {
            return R.error("环境项目未指定组长,无法收样");
        } else {
            return R.ok();
        }

    }


    /**
     * 我的项目
     */
    @GetMapping("/myProjectList")
//    @SysLog("查询项目信息")
    @ApiOperation("根据条件分页查询项目信息列表,用于我的项目,不根据权限判定")
    @RequiresPermissions("anlian:project:list")
    public Result MyProjectList(@RequestParam Map<String, Object> params) {

        //用户名
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        String userName = sysUserEntity.getUsername();
        Long userId = sysUserEntity.getUserId();
        String salesmen = (String) params.get("salesmen");
        List<String> nameList = new ArrayList<>();
        // 我的中介名称列表
        List<String> mediationList = sysUserService.getMediationByUserid(userId);
        if (!(mediationList != null && mediationList.size() > 0)) {
            mediationList = new ArrayList<>();
        }
        if (salesmen == "" || salesmen == null) {
            params.put("salesmen", userName);
            params.put("nameList", mediationList);
        } else {
            params.put("salesmen", salesmen);
            params.put("nameList", nameList);
        }
        List<ProjectEntity> queryProjectAmountDateVoList;
        MoneyVo queryMap2 = new MoneyVo();
        queryProjectAmountDateVoList = projectService.getMyPageList(params);
        queryMap2 = projectService.sumMoneyByParams(params);//统计全部金额
        mediationList.add(userName);
        return Result.resultData(queryProjectAmountDateVoList).put("map", queryMap2).put("mediationList", mediationList);
    }

    /**
     * 导出列表
     * anlian:project:export
     */
    @GetMapping("/export")
    @ApiOperation("根据条件导出项目列表")
    @RequiresPermissions("anlian:account:list")
    @AuthCode(url = "account", system = "sys")
    public R export(@RequestParam Map<String, Object> params, AuthCodeVo authCodeVo) {
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
        List<ProjectEntity> list = projectService.exportOut(params);
//        Map<String, BigDecimal> map = projectService.sumMoneyByParams(params);
        MoneyVo stringBigDecimalMap = projectService.sumMoneyByParams(params);

        return R.ok().put("list", list).put("map", stringBigDecimalMap);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示项目详情")
    @RequiresPermissions("anlian:project:info")
    public R info(@PathVariable("id") Long id) {
        ProjectEntity project = projectService.getById(id);

        return R.ok().put("project", project);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增项目")
    @ApiOperation("新增项目")
    @RequiresPermissions("anlian:project:save")
    public R save(@RequestBody ProjectEntity project) {
        // 保存时判断数据库中是否存在此项目编号 如果存在提示
        if (!projectService.notExistContractByIdentifier(project.getIdentifier())) {
            return R.error("该项目编号已被占用，请更改！");
        }
        projectService.saveProject(project, true);//项目信息初始化 true金额回填到合同，false不回填

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改项目")
    @ApiOperation("修改项目")
    @RequiresPermissions("anlian:project:update")
    public R update(@RequestBody ProjectEntity project) {
        String[] huanJingType = new String[]{"环境验收", "环境应急预案", "排污许可证申请", "排污许可后管理", "环保管家", "应急预案", "环境示范", "排污许可", "环境监测", "环评监测", "环境监督", "环境验收", "公共卫生检测", "一次性用品用具检测", "学校卫生检测", "公卫监督", "洁净区域检测", "排污许可"};
        ProjectEntity oldProject = projectService.getById(project.getId());
//        ProjectCountEntity projectCountEntity = projectCountService.getOneByProjectId(project.getId());
        if (!oldProject.getIdentifier().equals(project.getIdentifier())) {
            if (Arrays.asList(huanJingType).contains(oldProject.getType())) {
                return R.error("无法修改项目编号,请联系管理员");
            } else if (oldProject.getStatus() > 5) {
//            else if (projectCountEntity!=null&&projectCountEntity.getSampleRecord()!=null){

                return R.error("该项目已采样,无法修改其项目编号");
            }
        }
        projectService.updateProject(project);

        return R.ok();
    }


    /**
     * 任务下发
     */
    @PostMapping("/taskRelease")
    @SysLog("任务下发")
    @ApiOperation("任务下发")
    @RequiresPermissions("anlian:project:update")
    public R taskRelease(@RequestBody ProjectEntity project) {
        projectService.taskRelease(project);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除项目表")
    @ApiOperation("删除项目")
    @RequiresPermissions("anlian:project:delete")
    public R delete(@RequestBody Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        if (idList.size() == 0){
            return R.error("请选中项目");
        }
        List<ProjectEntity> projects = projectService.list(new QueryWrapper<ProjectEntity>().in("id", idList));
        for (ProjectEntity project : projects){
            if (project.getStatus() >= 2){
                return R.error("选中的项目状态已处于下发之后状态,不可删除");
            }
        }
        if (idList.size() > 0){
            projectService.removeByIds(idList);
            projectDateService.remove(new QueryWrapper<ProjectDateEntity>().in("project_id",idList));
            projectAmountService.remove(new QueryWrapper<ProjectAmountEntity>().in("project_id",idList));
            accountService.remove(new QueryWrapper<AccountEntity>().in("project_id",idList));
            commissionService.remove(new QueryWrapper<CommissionEntity>().in("project_id",idList));
            companySurveyService.remove(new QueryWrapper<CompanySurveyEntity>().in("project_id",idList));
            System.out.println("-----项目删除关联删除project,projectDate,projectAmount,account,commission,companySurvey表数据,涉及项目id:" + ids + "-----");
        }
        return R.ok("删除成功");
    }

    /**
     * 查询项目类型列表
     */
    @GetMapping("/sysDictListByProject")
    @ApiOperation("查询项目类型列表")
//    @RequiresPermissions("anlian:project:list")
    public R sysDictListByProject() {
        List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_PROJECT);
        return R.ok().put("sysDictList", sysDictList);
    }

    /**
     * 查询项目隶属公司列表
     */
    @GetMapping("/sysDictListByMembership")
    @ApiOperation("查询项目隶属公司列表")
//    @RequiresPermissions("anlian:project:list")
    public R sysDictListByMembership() {
        List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME_MEMBERSHIP);
        return R.ok().put("sysDictList", sysDictList);
    }


    /**
     * 项目提成脚本--采样提成
     */
    @PostMapping("/commissionGather")
    @SysLog("采样提成脚本")
    @ApiOperation("采样提成脚本")
//    @RequiresPermissions("anlian:project:delete")
    public R commissionGather(@RequestBody String[] identifierList) {
        List<Long> ids = new ArrayList<>();
        for (String identifier : identifierList) {
            Long id = projectService.getIdByIdentifier(identifier);
            ids.add(id);
        }
        projectService.commissionGatherByProjectIdList(ids);

        return R.ok();
    }

    /**
     * 项目提成脚本--签发提成
     */
    @PostMapping("/commissionIssue")
    @SysLog("签发提成脚本")
    @ApiOperation("签发提成脚本")
//    @RequiresPermissions("anlian:project:delete")
    public R commissionIssue(@RequestBody Long[] ids) {
        projectService.issueCommissionByProjectIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 项目提成脚本--归档提成
     */
    @PostMapping("/commissionFilling")
    @SysLog("归档提成脚本")
    @ApiOperation("归档提成脚本")
//    @RequiresPermissions("anlian:project:delete")
    public R commissionFilling(@RequestBody Long[] ids) {
        projectService.fillingCommissionByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询检评项目相关信息
     */
    @GetMapping("/getZjProject")
    @ApiOperation("查询检评项目相关信息")
//    @RequiresPermissions("anlian:project:list")
    public R getZjProject(@RequestParam Map<String, Object> params) {
        PageUtils page = projectService.getPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 查询并导出检评项目相关信息
     */
    @GetMapping("/exportZjProject")
    @ApiOperation("查询并导出检评项目相关信息")
//    @RequiresPermissions("anlian:project:list")
    public R exportZjProject(@RequestParam Map<String, Object> params) {
        List<ProjectCountVo> projectCountVoList = projectService.exportByParams(params);
        return R.ok().put("data", projectCountVoList);
    }


    /**
     * 中止项目
     */
    @PostMapping("/suspendProject")
    @ApiOperation("中止项目")
    @SysLog("中止项目")
    @RequiresPermissions("anlian:project:suspend")
    public Result suspendProject(@RequestBody SuspendOrRestartProjectVo suspendOrRestartProjectVo) {
        projectService.suspendProject(suspendOrRestartProjectVo);
        return Result.ok();
    }


    /**
     * 重启项目
     */
    @PostMapping("/restartProject")
    @ApiOperation("重启项目")
    @SysLog("重启项目")
    @RequiresPermissions("anlian:project:restart")
    public Result restartProject(@RequestBody SuspendOrRestartProjectVo suspendOrRestartProjectVo) {
        boolean a = projectService.restartProject(suspendOrRestartProjectVo);
        if (a) {
            return Result.ok();
        } else {
            return Result.error("请正确选择重启类型");
        }

    }


    //TODO  新检评系统采样提成接口

    /**
     * 新检评系统采样提成接口
     *
     * @param commissionTimeNodeVo
     * @return
     */
    @PostMapping("/mathCommission")
    @SysLog("新检评系统采样提成接口")
    @ApiOperation("新检评系统采样提成接口")
//    @RequiresPermissions("anlian:project:save")
    public Result mathCommission(@RequestBody CommissionTimeNodeVo commissionTimeNodeVo) {
        String[] huanjingTeam = new String[]{"排污许可", "环境监测", "环评监测", "环境监督", "环境验收", "公共卫生检测", "一次性用品用具检测", "学校卫生检测", "公卫监督", "洁净区域检测"};
        Long projectId = commissionTimeNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);
        if (StringUtils.isBlank(projectEntity.getCharge())) {
            return Result.error("未填写负责人");
        }
        boolean a = projectService.mathCommission(commissionTimeNodeVo);
        if (!a && Arrays.asList(huanjingTeam).contains(projectEntity.getType())) {
            return Result.error("环境项目未指定组长,无法收样");
        } else if (!a) {
            return Result.error("信息不完整无法发送或接收!");
        } else {
            return Result.ok();
        }

    }

//TODO OA获取项目金额信息

    /**
     * OA获取项目金额信息
     */
    @GetMapping("/getInfomation")
    @ApiOperation("OA获取项目金额信息")
//    @RequiresPermissions("anlian:project:restart")
    public Result getInfomation(@RequestParam("identifier") String identifier, @RequestParam("companyName") String companyName) {
        String[] companyNames = companyName.contains("杭州安联") ? IntellectConstants.ANLIAN_HANGZHOU :
                (companyName.contains("宁波安联") ? IntellectConstants.ANLIAN_NINGBO :
                        (companyName.contains("嘉兴安联") ? IntellectConstants.ANLIAN_JIAXING :
                                (companyName.contains("上海量远") ? IntellectConstants.ANLIAN_SHANGHAI :
                                        (companyName.contains("亿达检测") ? IntellectConstants.ANLIAN_YIDA :
                                                (companyName.contains("杭州卫康") ? IntellectConstants.ANLIAN_WEIKANG :
                                                        (companyName.contains("金华职康") ? IntellectConstants.ANLIAN_JINHUA: null))))));
        if (null == companyNames) {
            return Result.error("无隶属公司，无法找到指定项目");
        }
        OAProjectAmountVo oaProjectAmountVo = projectService.getInfomation(identifier, Arrays.asList(companyNames));
        if (oaProjectAmountVo == null) {
            return Result.error("未找到指定项目");
        } else {
            return Result.data(oaProjectAmountVo);
        }

    }

    /**
     * OA修改项目金额信息
     */
    @PostMapping("/updateProAmount")
    @ApiOperation("OA修改项目金额信息")
//    @SysLog("OA项目金额修改")
//    @RequiresPermissions("anlian:project:restart")
    public Result updateProAmount(HttpServletRequest request, @RequestBody OAProjectAmountVo oaProjectAmountVo) {
//        String ip = StringUtils.getIp(request);
//        System.out.println("ip= " + ip);
        String newIp = StringUtils.getIp(RequestHolder.getHttpServletRequest());
//        System.out.println("newIp= " + newIp);
//        if(!("127.0.0.1".equals(newIp))){
//            return Result.error("非法访问");
//        }
        ProjectEntity project = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("identifier", oaProjectAmountVo.getIdentifier()));
        if (project != null) {
            projectService.updateProAmount(oaProjectAmountVo, project);
            return Result.ok();
        } else {
            return Result.error("未查找到项目，请确认项目编号是否正确后再提交");
        }


    }

    /**
     * OA-检查合同编号是否被占用
     */
    @GetMapping("/checkContractIdentifier")
    public AjaxResult checkContractIdentifier(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return AjaxResult.error("合同编号不能为空！");
        }
        //检查合同编号是否存在
        ContractEntity contractEntity = SpringUtils.getBean(ContractMapper.class).checkContractIdentifier(identifier);
        if (contractEntity != null) {
            return AjaxResult.error("该合同编号已存在，请更换。");
        }
        return AjaxResult.success("该合同编号暂时可用");
    }

    /**
     * OA-检查项目编号是否被占用
     */
    @GetMapping("/checkIdentifier")
    public AjaxResult checkIdentifier(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return AjaxResult.error("项目编号不能为空！");
        }
        //调用接口 判断项目编号是否被调用
        if (!projectService.notExistContractByIdentifier(identifier)) {
            return AjaxResult.error("该项目编号已被占用，请更改！");
        }
        return AjaxResult.success("该项目编号暂时可用");
    }

    /**
     * OA-获取项目信息(包含合同下的其他项目信息)
     */
    @GetMapping("/getContractProjects")
    public AjaxResult getContractProjects(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return AjaxResult.error("项目编号不能为空！");
        }
        return AjaxResult.success(projectService.getContractProjects(identifier));
    }

    /**
     * OA-获取项目类型(及归属合同类型)
     */
    @GetMapping("/getProjectType")
    public AjaxResult getProjectType() {
        return AjaxResult.success(SpringUtils.getBean(CategoryMapper.class).getProjectType());
    }

    /**
     * OA-修改项目信息
     */
    @SysLog("OA-修改项目信息")
    @PostMapping("/updateProjectInfo")
    public AjaxResult updateProjectInfo(@RequestBody ProjectChangeInfoVo projectInfo) {
        //校验参数
        if (StringUtils.isEmpty(projectInfo.getOldIdentifier()) || StringUtils.isEmpty(projectInfo.getNewIdentifier())) {
            return AjaxResult.error("新旧项目编号不能为空");
        } else if (StringUtils.isEmpty(projectInfo.getNewType())) {
            return AjaxResult.error("项目类型不能为空");
        }
        try {
            //变更项目信息
            projectService.updateProjectInfo(projectInfo);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/getAccounting")
    @ApiOperation("项目核算-列表")
    @RequiresPermissions("anlian:project:accounting")
    @AuthCode(url = "accounting", system = "sys")
    public Result getAccounting(ProjectAccountingDto projectAccountingDto,AuthCodeVo authCodeVo){
        // 登录人隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (!IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            projectAccountingDto.setCompanyOrder(subjection);
        }
        List<ProjectAccountingListVo> list = projectService.getAccountingList(projectAccountingDto);
        ProjectAccountingVo accountingVo = projectService.getCountReturn(projectAccountingDto);
        return Result.resultData(list).put("countData",accountingVo);
    }

    @PostMapping("/exportAccounting")
    @ApiOperation("项目核算-导出")
    @RequiresPermissions("anlian:project:exportAccounting")
    @AuthCode(url = "accounting", system = "sys")
    public void exportAccounting(HttpServletResponse response,@RequestBody ProjectAccountingDto projectAccountingDto, AuthCodeVo authCodeVo){
        // 登录人隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 权限验证码
        String authCode = authCodeVo.getAuthCode();
        if (!IntellectConstants.GROUP_PERMISSIONS.equals(authCode)) {
            // 职能权限:集团
            projectAccountingDto.setCompanyOrder(subjection);
        }
        List<ProjectAccountingListVo> list = projectService.exportAccountingList(projectAccountingDto);
        List<ExportAccountingDto> exportList = ObjectConversion.copy(list,ExportAccountingDto.class);
        ExcelUtil<ExportAccountingDto> util = new ExcelUtil<>(ExportAccountingDto.class);
        util.exportExcel(response,exportList,"项目核算导出信息");
    }


}
