package may.yuntian.anlianwage.newCommission.controller;

import java.util.*;

import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlianwage.newCommission.dto.GrantDto;
import may.yuntian.anlianwage.newCommission.dto.NewCommissionQueryDto;
import may.yuntian.anlianwage.newCommission.dto.NewCommissionTempDto;
import may.yuntian.anlianwage.newCommission.vo.CommissionStatVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.modules.sys_v2.utils.BeanUtils;
import may.yuntian.modules.sys_v2.utils.poi.ExcelUtil;
import may.yuntian.untils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;
import may.yuntian.anlianwage.newCommission.service.NewCommissionService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 新绩效提成表管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2023-10-22 21:15:24
 */
@RestController
@Api(tags="新绩效提成表")
@RequestMapping("newCommission/")
public class NewCommissionController {
    @Autowired
    private NewCommissionService commissionService;
    @Autowired
    private ProjectService projectService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页绩效提成查询列表")
    @RequiresPermissions("newCommission:commission:list")
    public Result list(NewCommissionQueryDto newCommissionQueryDto){
        if (StringUtils.isNotBlank(newCommissionQueryDto.getCommissionStatus())){
            newCommissionQueryDto.setCommissionStatusMonth(newCommissionQueryDto.getCommissionStatus());
            newCommissionQueryDto.setCommissionStatusYear(newCommissionQueryDto.getCommissionStatus());
        }
        List<NewCommissionEntity> list = commissionService.queryPage(newCommissionQueryDto);
        CommissionStatVo commissionStatVo = commissionService.getCount(newCommissionQueryDto);
        return Result.resultData(list).put("amountCount",commissionStatVo);
    }

    /**
     * 列表
     */
    @GetMapping("/accrualList")
    @ApiOperation("根据条件分页绩效提成发放列表 month 月度待发放 year 年度待发放")
    @RequiresPermissions("newCommission:commission:list")
    public Result accrualList(NewCommissionQueryDto newCommissionQueryDto){
        if ("month".equals(newCommissionQueryDto.getAccrualType())){
            newCommissionQueryDto.setCommissionStatusMonth("待提成");
        }else if ("year".equals(newCommissionQueryDto.getAccrualType())){
            newCommissionQueryDto.setCommissionStatusYear("待提成");
        }
        List<NewCommissionEntity> list = commissionService.accrualList(newCommissionQueryDto);
        CommissionStatVo commissionStatVo = commissionService.getCount(newCommissionQueryDto);
        return Result.resultData(list).put("amountCount",commissionStatVo);
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/template")
    @ApiOperation("获取导入模板")
    public void importContractTemplate(HttpServletResponse response) {
        ExcelUtil<NewCommissionTempDto> util = new ExcelUtil<>(NewCommissionTempDto.class);
        util.importTemplateExcel(response,"绩效信息导入模板");
    }

    @SysLog("新绩效信息导入")
    @ApiOperation("新绩效信息导入")
    @PostMapping("/import")
    @RequiresPermissions("newCommission:commission:import")
    public Result importCommissionData(MultipartFile file) throws Exception{
        //返回信息
        StringBuilder resultMsg = new StringBuilder();
        //失败信息
        StringBuilder fileMsg = new StringBuilder();
        int addNum = 0;
        int failNum = 0;
        //提取excel数据
        ExcelUtil<NewCommissionTempDto> util = new ExcelUtil<>(NewCommissionTempDto.class);
        List<NewCommissionTempDto> commissionList = util.importExcel(file.getInputStream());
        if (StringUtils.isNotEmpty(commissionList)){
            //循环数据
            for (NewCommissionTempDto commissionTempDto:commissionList){
                Long projectId = projectService.getIdByIdentifier(commissionTempDto.getIdentifier());
                if (StringUtils.isNull(projectId)){
                    //未找到相关项目信息
                    failNum++;
                    fileMsg.append("<BR />数据异常-").append(failNum).append("---项目编号：").append(commissionTempDto.getIdentifier()).append(" ，原因：未找到相关项目信息；");
                    continue;
                }else {
                    commissionTempDto.setProjectId(projectId);
                }
                NewCommissionEntity newCommissionEntity = new NewCommissionEntity();
                //复制数据
                BeanUtils.copyBeanProp(newCommissionEntity, commissionTempDto);
                //继续延用之前的业务逻辑接口
                commissionService.save(newCommissionEntity);
                addNum++;
            }
        }else {
            failNum++;
            fileMsg.append("<BR />数据异常-").append(failNum).append(" ，原因：excel表格数据异常；");
        }
        //整理返回提示信息
        if (addNum > 0) {
            resultMsg.append("成功新增条数：").append(addNum).append("条。");
        }
        if (failNum > 0) {
            resultMsg.append("数据导入失败，共").append(failNum).append("条，错误如下：");
            resultMsg.append(fileMsg);
        }
        return Result.ok(resultMsg.toString());
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @ApiOperation("新绩效信息导出")
    @RequiresPermissions("newCommission:commission:export")
    public void export(HttpServletResponse response,@RequestBody NewCommissionQueryDto newCommissionQueryDto) {
        List<NewCommissionTempDto> exportList = new ArrayList<>();
        List<NewCommissionEntity> list = commissionService.exportList(newCommissionQueryDto);
        //复制数据
        exportList = ObjectConversion.copy(list,NewCommissionTempDto.class);
        ExcelUtil<NewCommissionTempDto> util = new ExcelUtil<>(NewCommissionTempDto.class);
        util.exportExcel(response,exportList,"绩效导出信息");
    }



    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示新绩效提成表详情")
    @RequiresPermissions("newCommission:commission:info")
    public Result info(@PathVariable("id") Long id){
        NewCommissionEntity commission = commissionService.getById(id);

        return Result.ok().put("commission", commission);
    }

    /**
     * 绩效发放
     */
    @PostMapping("/grant")
    @SysLog("新绩效提成发放")
    @ApiOperation("新绩效提成发放 grantType： month 月度 year 年度")
    @RequiresPermissions("newCommission:commission:grant")
    public Result grant(@RequestBody GrantDto grantDto){
        List<NewCommissionEntity> commissionList = grantDto.getCommissionList();
        if ("month".equals(grantDto.getGrantType())){
            for (NewCommissionEntity commission:commissionList){
                commission.setCommissionDateMonth(new Date());
                commission.setCommissionStatusMonth("已提成");
            }
            commissionService.updateBatchById(commissionList);
            return Result.ok();
        }else if ("year".equals(grantDto.getGrantType())){
            for (NewCommissionEntity commission:commissionList){
                commission.setCommissionDateYear(new Date());
                commission.setCommissionStatusYear("已提成");
            }
            commissionService.updateBatchById(commissionList);
            return Result.ok();
        }else {
            return Result.error("无此发放类型");
        }
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增新绩效提成")
    @ApiOperation("新增新绩效提成")
    @RequiresPermissions("newCommission:commission:save")
    public Result save(@RequestBody NewCommissionEntity commission){
        commissionService.save(commission);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改新绩效提成")
    @ApiOperation("修改新绩效提成")
    @RequiresPermissions("newCommission:commission:update")
    public Result update(@RequestBody NewCommissionEntity commission){
        commissionService.updateById(commission);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除新绩效提成")
    @ApiOperation("删除新绩效提成")
    @RequiresPermissions("newCommission:commission:delete")
    public Result delete(@RequestBody Long[] ids){
        commissionService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
