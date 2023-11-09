package may.yuntian.external.wanda.controller;

import io.swagger.annotations.Api;
import may.yuntian.external.wanda.entity.FactorDictionary;
import may.yuntian.external.wanda.entity.ProjectInfo;
import may.yuntian.external.wanda.service.ProjectInfoService;
import may.yuntian.external.wanda.vo.AnnexVo;
import may.yuntian.external.wanda.vo.BusinessSystemDataVo;
import may.yuntian.external.wanda.vo.RejectVo;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 万达-仓库controll层
 * @author: liyongqiang
 * @create: 2023-03-08 13:25
 */
@RestController
@Api(tags="万达-仓库")
@RequestMapping("/wanda")
public class ProjectInfoController {

    @Autowired
    private ProjectInfoService projectInfoService;

    /**
     * 1.1、判断：基本信息。 0否（调python）；1是（调Java）
     */
    @GetMapping(value = "{projectId}")
    public Result getInfoById(@PathVariable Long projectId) {
        return Result.ok("查询成功", projectInfoService.getInfoByProjectId(projectId));
    }

    /**
     * 1.2、判断：检测结果。 0否（调python）；1是（调Java）
     */
    @GetMapping("/assertResultSave")
    public Result assertResultSave(Long projectId, Integer factorType) {
        return Result.ok("查询成功", projectInfoService.assertResultSave(projectId, factorType));
    }

    /**
     * 1.3、判断：附件。 0否（调python）；1是（调Java）
     */
    @GetMapping("/assert/annex")
    public Result assertAnnex(Long projectId) {
        return Result.ok("查询成功", projectInfoService.assertAnnex(projectId));
    }

    /**
     * 2.1、查看基本信息：项目负责人
     */
    @GetMapping("/viewBasicInfo")
    public Result getBasicInfo(Long projectId) {
        return Result.data(projectInfoService.getBasicInfo(projectId));
    }

    /**
     * 2.2、基本信息：保存
     */
    @PostMapping("/saveInfo")
    public Result saveInfo(@RequestBody ProjectInfo info) {
        return projectInfoService.saveInfo(info) == 1 ? Result.ok("保存成功！") : Result.error();
    }

    /**
     * 附件-保存
     */
    @PostMapping("/annex/save")
    public Result saveAnnex(@RequestBody AnnexVo annexVo) {
        return Result.ok(Boolean.TRUE.equals(projectInfoService.saveAnnex(annexVo)) ? "保存成功！" : "保存失败！");
    }

    /**
     * 附件-数据回显
     */
    @GetMapping("/annex/dataEcho")
    public Result dataEcho(Long projectId) {
        return Result.data(projectInfoService.dataEcho(projectId));
    }

    /**
     * 4.1、查看检测结果：业务员
     */
    @GetMapping("/detectionResult")
    public Result getDetectionResultData(Long projectId, Integer factorType) {
        return Result.data(projectInfoService.getDetectionResultData(projectId, factorType));
    }

    /**
     * 4.2、检测结果：逐个保存
     */
    @PostMapping("saveResult")
    public Result saveDetectionResultData(@RequestBody BusinessSystemDataVo dataVo) {
        return projectInfoService.saveDetectionResultData(dataVo) == 1 ? Result.ok("保存成功！") : Result.error();
    }

    /**
     * 5、项目负责人：提交
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody ProjectInfo info) {
        StringBuilder message = projectInfoService.submit(info.getProjectId());
        return Result.ok("提交成功。" + message);
    }

    /**
     * 6.1: 主管驳回
     */
    @PostMapping("/chargeReject")
    public Result chargeReject(@RequestBody RejectVo rejectVo) {
        projectInfoService.chargeReject(rejectVo);
        return Result.ok("驳回成功！");
    }

    /**
     * 6.2: 主管提交
     */
    @GetMapping("chargeRefer")
    public Result chargeRefer(Long projectId) {
        projectInfoService.chargeRefer(projectId);
        return Result.ok("提交成功！");
    }

    /**
     * 7.1：质控驳回
     */
    @GetMapping("/qualityReject")
    public Result qualityReject(Long projectId, String reason) {
        RejectVo rejectVo = new RejectVo(projectId, reason);
        projectInfoService.qualityReject(rejectVo);
        return Result.ok("驳回成功！");
    }

    /**
     * 质控推送：基本信息
     */
    @GetMapping("/push/info")
    public Result pushBasicInfo(Long projectId) {
        return Result.ok(projectInfoService.pushBasicInfo(projectId));
    }

    /**
     * 质控推送：附件、结果
     */
    @GetMapping("/qualityPush")
    public Result qualityPush(HttpServletRequest request, Long projectId) {
        String message = projectInfoService.qualityPush(request, projectId);
        return message.length() == 0 ? Result.ok("推送成功！") : Result.error("推送失败，第三方接口异常信息：" + message);
    }

    /**
     * 9 主管、质控: 查看所有数据信息
     * @param viewer 1主管，2质控
     */
    @GetMapping("viewAllInfo")
    public Result viewAllInfo(Long projectId, Integer viewer) {
        return Result.data(projectInfoService.selectAllDataInfo(projectId, viewer));
    }

    /**
     * 10、主管、质控：项目列表
     */
    @GetMapping("/list")
    public Result warehouseList(ProjectInfo info) {
        return Result.resultData(projectInfoService.selectWarehouseList(info));
    }

    /**
     * 11、危害因素列表
     */
    @GetMapping("/factorList")
    public Result factorList(FactorDictionary factorDictionary) {
        return Result.data(projectInfoService.factorList(factorDictionary));
    }

    /**
     * 项目负责人提交前：信息保存记录！
     */
    @GetMapping("/infoRecord")
    public Result infoRecord(Long projectId) {
        return Result.ok("操作成功", projectInfoService.selectInfoRecordList(projectId));
    }


}
