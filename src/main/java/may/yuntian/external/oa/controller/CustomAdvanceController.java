package may.yuntian.external.oa.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.common.utils.R;
import may.yuntian.external.oa.dto.CustomAdvanceTaskDto;
import may.yuntian.external.oa.entity.CustomAdvanceRecordEntity;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.external.oa.service.CustomAdvanceRecordService;
import may.yuntian.external.oa.service.CustomAdvanceTaskService;
import may.yuntian.minio.config.MinioConfig;
import may.yuntian.minio.utils.DateUtils;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys_v2.controller.BaseController;
import may.yuntian.untils.AlRedisUntil;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 任务跟进
 *
 * @Author yrb
 * @Date 2023/8/21 17:32
 * @Version 1.0
 * @Description 任务跟进
 */
@RestController
@RequestMapping("/oa/advance")
@Slf4j
public class CustomAdvanceController extends BaseController {
    private final CustomAdvanceRecordService customAdvanceRecordService;
    private final CustomAdvanceTaskService customAdvanceTaskService;
    private final SysUserService sysUserService;

    @Autowired
    private MinioConfig prop;
    @Autowired
    private AlRedisUntil alRedisUntil;

    public CustomAdvanceController(CustomAdvanceRecordService customAdvanceRecordService,
                                   CustomAdvanceTaskService customAdvanceTaskService,
                                   SysUserService sysUserService) {
        this.customAdvanceRecordService = customAdvanceRecordService;
        this.customAdvanceTaskService = customAdvanceTaskService;
        this.sysUserService = sysUserService;
    }

    /**
     * 获取我的客户
     *
     * @param customAdvanceTaskDto 查询条件
     * @return result
     */
    @ApiOperation("获取我的客户")
    @GetMapping("/task/myTasks")
    public Result myTasks(HttpServletRequest request, CustomAdvanceTaskDto customAdvanceTaskDto) {
        try {
            if (StrUtil.isBlank(customAdvanceTaskDto.getEmail())) {
                return Result.error("邮箱不能为空");
            }
            SysUserEntity sysUserEntity = sysUserService.queryByEmail(customAdvanceTaskDto.getEmail());
            if (sysUserEntity == null || sysUserEntity.getUserId() == null) {
                return Result.error("通过邮箱未匹配到跟进人，请联系管理员！");
            }
            customAdvanceTaskDto.setAdvanceId(sysUserEntity.getUserId());
            startPage();
            return Result.resultData(customAdvanceTaskService.getMyTasks(customAdvanceTaskDto));
        } catch (Exception e) {
            log.error("获取我的客户异常=====" + e);
            return Result.error("获取我的客户异常");
        }
    }

    /**
     * 获取公司联系人
     *
     * @param companyId 公司ID
     * @return 集合
     */
    @ApiOperation("获取公司联系人")
    @GetMapping("/company/contacts")
    public Result getContacts(HttpServletRequest request, Long companyId) {
        try {
            if (companyId == null) {
                return Result.error("公司ID不能为空");
            }
            return Result.data(customAdvanceTaskService.getCompanyContactInfo(companyId));
        } catch (Exception e) {
            log.error("获取公司联系人异常=========" + e);
            return Result.error("获取公司联系人异常");
        }
    }

    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return result
     */
    @ApiOperation("新增跟进记录")
    @PutMapping("/record/save")
    public R save(HttpServletRequest request, @RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                return R.error("要跟进的任务ID不能为空");
            }
            if (customAdvanceRecordService.add(customAdvanceRecord) > 0) {
                return R.ok("新增成功");
            }
            return R.error("新增失败");
        } catch (Exception e) {
            log.error("新增跟进记录异常======" + e);
            return R.error("新增跟进记录异常");
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
    public R update(HttpServletRequest request, @RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (customAdvanceTask.getId() == null) {
                return R.error("任务ID不能为空");
            }
            if (customAdvanceTaskService.modify(customAdvanceTask) > 0) {
                return R.ok("提交成功");
            }
            return R.error("提交失败");
        } catch (Exception e) {
            log.error("提交跟进记录异常======" + e);
            return R.error("提交跟进记录异常");
        }
    }

//    /**
//     * 人员替换
//     *
//     * @param customAdvanceTask 任务信息
//     * @return result
//     */
//    @ApiOperation("人员替换")
//    @PostMapping("/task/replace")
//    public R replace(HttpServletRequest request, @RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
//        try {
//            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
//                return R.error("要替换的任务ID不能为空");
//            }
//            if (customAdvanceTask.getAdvanceId() == null) {
//                return R.error("请选择要替换的人员");
//            }
//            if (customAdvanceTaskService.replaceUserBatch(customAdvanceTask) > 0) {
//                return R.ok("替换成功");
//            }
//            return R.error("替换失败");
//        } catch (Exception e) {
//            log.error("替换任务跟进人员异常======" + e);
//            return R.error("替换任务跟进人员异常");
//        }
//    }

//    /**
//     * 查询跟进任务列表
//     *
//     * @param dto 查询条件
//     * @return result
//     */
//    @ApiOperation("查询跟进任务列表")
//    @GetMapping("/record/listTasks")
//    public TableDataInfo listTasks(HttpServletRequest request, CustomAdvanceTaskDto dto) {
//        try {
//            startPage();
//            return getDataTable(customAdvanceTaskService.listTasks(dto));
//        } catch (Exception e) {
//            log.error("查询跟进任务列表发生异常========" + e);
//            TableDataInfo tableDataInfo = new TableDataInfo();
//            tableDataInfo.setCode(500);
//            tableDataInfo.setMsg("查询跟进任务列表发生异常");
//            return tableDataInfo;
//        }
//    }

//    /**
//     * 批量分配
//     *
//     * @param customAdvanceTask 任务信息
//     * @return 结果
//     */
//    @ApiOperation("批量分配")
//    @PostMapping("/task/allocation")
//    public R allocationBatch(HttpServletRequest request, CustomAdvanceTaskEntity customAdvanceTask) {
//        try {
//            if (customAdvanceTask.getAdvanceId() == null) {
//                return R.error("要分配的业务员ID不能为空");
//            }
//            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
//                return R.error("要分配的任务为空");
//            }
//            if (customAdvanceTaskService.allocationBatch(customAdvanceTask) == 0) {
//                return R.error("任务分配失败");
//            }
//            return R.ok("任务分配成功");
//        } catch (Exception e) {
//            log.error("任务分配发生异常===========" + e);
//            return R.error("任务分配发生异常");
//        }
//    }

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 跟进记录
     * @return result
     */
    @ApiOperation("跟进详情")
    @GetMapping("/getDetail")
    public R getDetail(HttpServletRequest request, CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                return R.error("请输入要查询的任务ID");
            }
            startPage();
            return R.ok().put("detail", customAdvanceRecordService.getDetail(customAdvanceRecord));
        } catch (Exception e) {
            return R.error("获取跟进详情异常");
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
    public R edit(HttpServletRequest request, @RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getId() == null) {
                return R.error("记录ID不能为空");
            }
            if (customAdvanceRecord.getTaskId() == null) {
                return R.error("任务ID不能为空");
            }
            if (customAdvanceRecordService.updateAdvanceInfo(customAdvanceRecord) > 0) {
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
    public R delete(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            if (id == null) {
                return R.error("跟进记录id不能为空");
            }
            return customAdvanceRecordService.delete(id) == 1 ? R.ok("删除成功") : R.error("删除失败");
        } catch (Exception e) {
            log.error("删除跟进记录异常================" + e);
            return R.error("删除跟进记录异常");
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 路径
     */
    @ApiOperation(value = "文件上传返回url")
    @PostMapping("/upload")
    public Result upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
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
     * 文件下载
     *
     * @param fileName 文件路径
     * @param res      响应
     * @return 结果
     */
    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public Result download(HttpServletRequest request, @RequestParam("fileName") String fileName, HttpServletResponse res) {
        try {
            if (fileName.contains("uploadFile/")) {
                return Result.error("图片加载失败");
            }
            String objName = fileName.substring(fileName.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
            Integer download = MinioUtil.download(objName, res);
            if (download == 1) {
                return Result.ok();
            } else {
                return Result.error("图片加载失败");
            }
        } catch (Exception e) {
            log.error("图片加载异常===========" + e);
            return Result.error("图片加载异常");
        }
    }

    /**
     * 图片/视频预览
     *
     * @param fileName
     * @return result
     */
    @ApiOperation(value = "图片/视频预览")
    @GetMapping("/preview")
    public Result preview(HttpServletRequest request, @RequestParam("fileName") String fileName) {
        try {
            String objName = fileName.substring(fileName.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
            return Result.ok().put("filleName", MinioUtil.preview(objName));
        } catch (Exception e) {
            log.error("图片预览发生异常==========" + e);
            return Result.error("图片预览发生异常");
        }
    }

    /**
     * 获取我的任务单个
     *
     * @param customAdvanceTaskDto 任务ID
     * @return 信息
     */
    @ApiOperation("获取我的任务单个")
    @GetMapping("/task/myTaskOne")
    public Result myTaskOne(HttpServletRequest request, CustomAdvanceTaskDto customAdvanceTaskDto) {
        try {
            if (customAdvanceTaskDto.getId() == null) {
                return Result.error("任务ID不能为空");
            }
            return Result.ok().put("one", customAdvanceTaskService.getMyTaskOne(customAdvanceTaskDto));
        } catch (Exception e) {
            log.error("获取我的单个任务发生异常=========" + e);
            return Result.error("获取我的单个任务发生异常");
        }
    }
}
