package may.yuntian.anlian.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.service.CustomAdvanceRecordManageService;
import may.yuntian.anlian.service.CustomAdvanceTaskManageService;
import may.yuntian.anlian.vo.HistoryAdvanceVo;
import may.yuntian.common.utils.R;
import may.yuntian.external.oa.dto.CustomAdvanceTaskDto;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.minio.utils.DateUtils;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.modules.sys_v2.entity.TableDataInfo;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.untils.AlRedisUntil;
import may.yuntian.untils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务跟进
 *
 * @Author yrb
 * @Date 2023/8/21 17:32
 * @Version 1.0
 * @Description 任务跟进
 */
@RestController
@RequestMapping("/advance")
@Slf4j
public class CustomAdvanceManageController extends BaseController {
    private final CustomAdvanceRecordManageService customAdvanceRecordManageService;
    private final CustomAdvanceTaskManageService customAdvanceTaskManageService;

    @Autowired
    private AlRedisUntil alRedisUntil;

    public CustomAdvanceManageController(CustomAdvanceRecordManageService customAdvanceRecordManageService,
                                         CustomAdvanceTaskManageService customAdvanceTaskManageService) {
        this.customAdvanceRecordManageService = customAdvanceRecordManageService;
        this.customAdvanceTaskManageService = customAdvanceTaskManageService;
    }

    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return result
     */
    @ApiOperation("新增跟进记录")
    @PutMapping("/record/save")
    public R save(@RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                return R.error("要跟进的任务ID不能为空");
            }
            if (customAdvanceRecordManageService.add(customAdvanceRecord) > 0) {
                return R.ok("新增成功");
            }
            return R.error("新增失败");
        } catch (Exception e) {
            log.error("新增跟进记录异常======" + e);
            return R.error("新增跟进记录异常");
        }
    }

    /**
     * 人员替换
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @ApiOperation("人员替换")
    @PostMapping("/task/replace")
    public R replace(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
                return R.error("要替换的任务不能为空");
            }
            if (customAdvanceTask.getAdvanceId() == null) {
                return R.error("请选择要替换的人员");
            }
            if (customAdvanceTaskManageService.replaceUserBatch(customAdvanceTask) > 0) {
                return R.ok("替换成功");
            }
            return R.error("替换失败");
        } catch (Exception e) {
            log.error("替换任务跟进人员异常======" + e);
            return R.error("替换任务跟进人员异常");
        }
    }

    /**
     * 查询跟进任务列表
     *
     * @param dto 查询条件
     * @return result
     */
    @ApiOperation("查询跟进任务列表")
    @GetMapping("/record/listTasks")
    @RequiresPermissions("anlian:company:list")
    @AuthCode(url = "company", system = "sys")
    public TableDataInfo listTasks(CustomAdvanceTaskDto dto, AuthCodeVo authCodeVo) {
        try {
            if (StrUtil.isBlank(authCodeVo.getAuthCode())) {
                return getDataTable(new ArrayList<CustomAdvanceTaskDto>());
            }
            if (!IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())) {
                SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
                if (sysUserEntity.getSubjection() != null) {
                    dto.setSubjection(sysUserEntity.getSubjection());
                } else {
                    return getDataTable(new ArrayList<CustomAdvanceTaskDto>());
                }
            }
            startPage();
            return getDataTable(customAdvanceTaskManageService.listTasks(dto));
        } catch (Exception e) {
            log.error("查询跟进任务列表发生异常========" + e);
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setCode(500);
            tableDataInfo.setMsg("查询跟进任务列表发生异常");
            return tableDataInfo;
        }
    }

    /**
     * 批量分配
     *
     * @param customAdvanceTask 任务信息
     * @return 结果
     */
    @ApiOperation("批量分配")
    @PostMapping("/task/allocation")
    public R allocationBatch(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (customAdvanceTask.getAdvanceId() == null) {
                return R.error("要分配的业务员ID不能为空");
            }
            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
                return R.error("要分配的任务为空");
            }
            if (customAdvanceTaskManageService.allocationBatch(customAdvanceTask) == 0) {
                return R.error("任务分配失败");
            }
            return R.ok("任务分配成功");
        } catch (Exception e) {
            log.error("任务分配发生异常===========" + e);
            return R.error("任务分配发生异常");
        }
    }

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 跟进记录
     * @return result
     */
    @ApiOperation("跟进详情")
    @GetMapping("/getDetail")
    public TableDataInfo getDetail(CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                TableDataInfo tableDataInfo = new TableDataInfo();
                tableDataInfo.setCode(500);
                tableDataInfo.setMsg("请输入要查询的任务ID");
                return tableDataInfo;
            }
            startPage();
            return getDataTable(customAdvanceRecordManageService.getDetail(customAdvanceRecord));
        } catch (Exception e) {
            log.error("查询跟进任务列表发生异常========" + e);
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setCode(500);
            tableDataInfo.setMsg("查询跟进任务列表发生异常");
            return tableDataInfo;
        }
    }

    /**
     * 编辑跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return result
     */
    @ApiOperation("编辑跟进记录")
    @PutMapping("/record/edit")
    public R edit(@RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getId() == null) {
                return R.error("记录ID不能为空");
            }
            if (customAdvanceRecord.getTaskId() == null) {
                return R.error("任务ID不能为空");
            }
            if (customAdvanceRecordManageService.updateAdvanceInfo(customAdvanceRecord) > 0) {
                return R.ok("编辑成功");
            }
            return R.error("编辑失败");
        } catch (Exception e) {
            log.error("编辑跟进记录异常======" + e);
            return R.error("编辑跟进记录异常");
        }
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录id
     * @return result
     */
    @ApiOperation("删除跟进记录")
    @DeleteMapping("/record/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        try {
            if (id == null) {
                return R.error("跟进记录id不能为空");
            }
            return customAdvanceRecordManageService.delete(id) == 1 ? R.ok("删除成功") : R.error("删除失败");
        } catch (Exception e) {
            log.error("删除跟进记录异常================" + e);
            return R.error("删除跟进记录异常");
        }
    }

//    /**
//     * 获取我的任务单个
//     *
//     * @param customAdvanceTaskDto 任务ID
//     * @return 信息
//     */
//    @ApiOperation("获取我的任务单个")
//    @GetMapping("/task/myTaskOne")
//    public Result myTaskOne(CustomAdvanceTaskDto customAdvanceTaskDto) {
//        try {
//            if (customAdvanceTaskDto.getId() == null) {
//                return Result.error("任务ID不能为空");
//            }
//            return Result.ok().put("one", customAdvanceTaskManageService.getMyTaskOne(customAdvanceTaskDto));
//        } catch (Exception e) {
//            log.error("获取我的单个任务发生异常=========" + e);
//            return Result.error("获取我的单个任务发生异常");
//        }
//    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 路径
     */
    @ApiOperation(value = "文件上传返回url")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            String type = "oa/advance";
            String objectName = MinioUtil.upload(file, type);
            if (null != objectName) {
                alRedisUntil.hset("anlian-java", objectName, DateUtils.getDate());
                return Result.ok().put("path", objectName);
            }
            return Result.error("文件上传失败");
        } catch (Exception e) {
            log.error("文件上传发生异常==========" + e);
            return Result.error("文件上传发生异常");
        }
    }

    /**
     * 修改任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @ApiOperation("修改任务信息")
    @PostMapping("/task/update")
    public R update(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (customAdvanceTask.getId() == null) {
                return R.error("任务ID不能为空");
            }
            if (customAdvanceTask.getAdvanceResult() == null) {
                return R.error("请选择是否已成单");
            }
            if (customAdvanceTaskManageService.modify(customAdvanceTask) > 0) {
                return R.ok("提交成功");
            }
            return R.error("提交失败");
        } catch (Exception e) {
            log.error("提交跟进记录异常======" + e);
            return R.error("提交跟进记录异常");
        }
    }

    /**
     * 历史跟进
     */
    @GetMapping("/history")
    public TableDataInfo historyAdvance(Long companyId) {
        startPage();
        List<HistoryAdvanceVo> list = customAdvanceRecordManageService.historyAdvance(companyId);
        return getDataTable(list);
    }

    /**
     * 业务员主动释放
     */
    @PostMapping("/releaseCompany")
    @ApiOperation("业务员主动释放")
    public Result releaseCompany(@RequestBody CustomAdvanceTaskDto dto){
        if (dto.getId() == null || dto.getCompanyId() == null){
            return Result.error("客户id或跟进任务id为null");
        }
        return customAdvanceTaskManageService.releaseCompany(dto) ? Result.ok("操作成功") : Result.error("操作失败");
    }

}
