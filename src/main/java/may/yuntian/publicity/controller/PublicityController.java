package may.yuntian.publicity.controller;

import cn.hutool.http.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.publicity.dto.PublicPjDto;
import may.yuntian.publicity.service.PublicityService;
import may.yuntian.publicity.vo.PublicPjPageVo;
import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.publicity.vo.PublictyPjInfoVo;
import may.yuntian.untils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 项目公示管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
@RestController
@Api(tags = "项目公示-Publicty")
@RequestMapping("/publicity")
public class PublicityController {
    @Autowired
    private PublicityService publicityService;

//
//    /**
//     * 获取公示详细信息
//     */
//    @GetMapping("/getPublicityOne/{projectId}")
//    @ApiOperation("获取公示详细信息")
//    public Result getPublicityOne(@PathVariable("projectId") Long projectId) {
//        PublicityInfoVo publicityInfoVo = publicityService.getInfo(projectId);
//        return Result.data(publicityInfoVo);
//    }

    /**
     * 获取公示xin详细信息
     */
    @GetMapping("/getPublicityNewOne/{projectId}")
    @ApiOperation("获取公示详细信息")
    public Result getPublicityNewOne(@PathVariable("projectId") Long projectId) {
        PublicityInfoVo publicityInfoVo = publicityService.getNewInfo(projectId);
        return Result.data(publicityInfoVo);
    }

    /**
     * 邮件发送测试
     */
    @PostMapping("/sendMail")
    @ApiOperation("邮件发送测试")
    public Result sendMail(@RequestBody PublicityInfoVo publicityInfoVo) {
        publicityService.sendMail();
        return Result.ok();
    }


    /**
     * 生成PDF
     */
    @PostMapping("/publicityPdf")
    @ApiOperation("生成PDF")
    public Result publicityPdf(HttpServletRequest httpRequest, @RequestBody PublicityInfoVo publicityInfoVo) {
        String path = publicityService.publicityPdf(httpRequest,publicityInfoVo);
        if (path == null) {
            return Result.error("生成失败");
        } else {
            return Result.ok().put("path", path);
        }
    }

//    /**
//     * 提交审核
//     */
//    @PostMapping("/saveReview")
//    @SysLog("提交审核")
//    @ApiOperation("提交审核")
//    public Result saveReview(@RequestBody PublicityInfoVo publicityInfoVo) {
//
//        if (StringUtils.isNotBlank(publicityInfoVo.getDetectionType()) && publicityInfoVo.getDetectionType().equals("定期检测")) {
//            if (StringUtils.isBlank(publicityInfoVo.getCharge())) {
//                return Result.error("未填写报告编制人，无法提交审核");
//            }
//            if (StringUtils.isBlank(publicityInfoVo.getLaboratoryPerson())) {
//                return Result.error("未填写实验室检测人员，无法提交审核");
//            }
//            if (StringUtils.isBlank(publicityInfoVo.getSamplingCompany())) {
//                return Result.error("未填写采样陪同人，无法提交审核");
//            }
//            if (StringUtils.isBlank(publicityInfoVo.getTechnicalPersons())) {
//                return Result.error("未填写技术项目组服务人员，无法提交审核");
//            }
//            if (StringUtils.checkValNull(publicityInfoVo.getReportCoverDate())) {
//                return Result.error("未填写报告签发日期，无法提交审核");
//            }
//            if (StringUtils.isBlank(publicityInfoVo.getPublicityPath())) {
//                return Result.error("未生成公示文件，无法提交审核");
//            }
//        } else {
//            if (StringUtils.isBlank(publicityInfoVo.getDetectionType())) {
//                return Result.error("未填写检测类型，无法提交审核");
//            }
//        }
//
//        publicityService.saveReview(publicityInfoVo);
//        return Result.ok();
//    }

    /**
     * xin提交审核
     */
    @PostMapping("/saveNewReview")
    @SysLog("xin提交审核")
    @ApiOperation("xin提交审核")
    public Result saveNewReview(@RequestBody PublicityInfoVo publicityInfoVo) {
        PublicityInfoVo newInfo = publicityService.getType(publicityInfoVo.getId());
        // 项目的检测类型
        String type = newInfo.getType();
        // 前端传递过来的检测类型
        String detectionType = publicityInfoVo.getDetectionType();
        if (detectionType != null) {
            if (StringUtils.isNotBlank(detectionType) && detectionType.equals("定期检测")
                    && StringUtils.isNotBlank(type) && type.equals("检评")) {
                // 公示模块
                if (StringUtils.isBlank(publicityInfoVo.getCharge())) {
                    return Result.error("未填写报告编制人，无法提交审核");
                } else if (StringUtils.isBlank(publicityInfoVo.getLaboratoryPerson())) {
                    return Result.error("未填写实验室检测人员，无法提交审核");
                } else if (StringUtils.isBlank(publicityInfoVo.getSamplingCompany())) {
                    return Result.error("未填写采样陪同人，无法提交审核");
                } else if (StringUtils.isBlank(publicityInfoVo.getTechnicalPersons())) {
                    return Result.error("未填写技术项目组服务人员，无法提交审核");
                } else if (StringUtils.checkValNull(publicityInfoVo.getReportCoverDate())) {
                    return Result.error("未填写报告签发日期，无法提交审核");
                } else if (StringUtils.isBlank(publicityInfoVo.getPublicityPath())) {
                    return Result.error("未生成公示文件，无法提交审核");
                }
                // 签发模块
                else if (StringUtils.checkValNull(publicityInfoVo.getReportCoverDate2())) {
                    return Result.error("未填写报告封面日期，无法提交审核");
                }
//                else {
//                    return Result.error("未填写检测类型，无法提交审核");
//                }
            } else if (StringUtils.isNotBlank(detectionType) && !detectionType.equals("定期检测")
                    && StringUtils.isNotBlank(type) && type.equals("检评")) {
                // 签发模块
                if (StringUtils.checkValNull(publicityInfoVo.getReportCoverDate2())) {
                    return Result.error("未填写报告封面日期，无法提交审核");
                }
//                else {
//                    return Result.error("未填写检测类型，无法提交审核");
//                }
            } else if (StringUtils.isNotBlank(type) && type.equals("职卫监督")) {
                // 签发模块
                if (StringUtils.checkValNull(publicityInfoVo.getReportCoverDate2())) {
                    return Result.error("未填写报告封面日期，无法提交审核");
                }
            }
        } else if (StringUtils.isNotBlank(type) && type.equals("检评") &&
                !StringUtils.isNotBlank(detectionType)) {
            return Result.error("未填写检测类型，无法提交审核");
        }
        publicityService.saveNewReview(publicityInfoVo);
        return Result.ok();
    }

    /**
     * 保存公示信息
     */
    @PostMapping("/saveInfo")
    @SysLog("保存公示信息")
    @ApiOperation("保存公示信息")
    public Result saveInfo(@RequestBody PublicityInfoVo publicityInfoVo) {

        publicityService.saveInfo(publicityInfoVo);
        return Result.ok();
    }

//TODO 主管相关 --> start

    /**
     * 主管页面待审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("/getHeadPageList")
    @ApiOperation("主管页面待审核列表")
    public Result getHeadPageList(@RequestParam Map<String, Object> params) {
        List<PublicityPageVo> list = publicityService.getHeadPageList(params);

        return Result.resultData(list);
    }

    /**
     * 主管页面已审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("/getHeadPageList2")
    @ApiOperation("主管页面已审核列表")
    public Result getHeadPageList2(@RequestParam Map<String, Object> params) {
        List<PublicityPageVo> list = publicityService.getHeadPageList2(params);

        return Result.resultData(list);
    }

    /**
     * 主管审核通过
     */
    @PostMapping("/passHeadReview")
    @SysLog("主管审核通过")
    @ApiOperation("主管审核通过")
    public Result passHeadReview(@RequestBody PublicityPageVo publicityPageVo) {
        publicityService.passHeadReview(publicityPageVo);

        return Result.ok();
    }

    /**
     * 主管批量审核通过
     */
    @PostMapping("/passHeadReviewBatch")
    @SysLog("主管批量审核通过")
    @ApiOperation("主管批量审核通过")
    public Result passHeadReviewBatch(@RequestBody List<PublicityPageVo> publicityPageVos) {
        publicityService.passHeadReviewBatch(publicityPageVos);

        return Result.ok();
    }

    /**
     * 主管驳回
     */
    @PostMapping("/rejectHeadReview")
    @SysLog("主管驳回")
    @ApiOperation("主管驳回")
    public Result rejectHeadReview(@RequestBody PublicityPageVo publicityPageVo) {
        publicityService.rejectHeadReview(publicityPageVo);

        return Result.ok();
    }

//TODO 主管 --> end


//TODO 质控 --> start

    /**
     * 质控页面待审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("/getQualityPageList")
    @ApiOperation("质控页面待审核列表")
    public Result getQualityPageList(@RequestParam Map<String, Object> params) {
        List<PublicityPageVo> list = publicityService.getQualityPageList(params);

        return Result.resultData(list);
    }

    /**
     * 质控页面已审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("/getQualityPageList2")
    @ApiOperation("质控页面已审核列表")
    public Result getQualityPageList2(@RequestParam Map<String, Object> params) {
        List<PublicityPageVo> list = publicityService.getQualityPageList2(params);
        Map<String, Object> map = publicityService.export(params);

        return Result.resultData(list).put("map", map);
    }

    /**
     * 质控审核通过
     */
    @PostMapping("/passQualityReview")
    @SysLog("质控审核通过")
    @ApiOperation("质控审核通过")
    public Result passQualityReview(@RequestBody PublicityPageVo publicityPageVo) {
        publicityService.passQualityReview(publicityPageVo);

        return Result.ok();
    }

    /**
     * 质控批量审核通过
     */
    @PostMapping("/passQualityReviewBatch")
    @SysLog("质控批量审核通过")
    @ApiOperation("质控批量审核通过")
    public Result passQualityReviewBatch(@RequestBody List<PublicityPageVo> publicityPageVos) {

        publicityService.passQualityReviewBatch(publicityPageVos);

        return Result.ok();
    }

//    /**
//     * 质控批量审核通过
//     */
//    @PostMapping("/ces")
//    @ApiOperation("质控批量审核通过")
//    public Result ces(@RequestBody PublicityPageVo publicityPageVo) {
//        Integer pubStatus = publicityPageVo.getPubStatus();
//        if (pubStatus > 0) {
//            publicityService.automaticPublicity(publicityPageVo.getPubStatus());
//        }
//        return Result.ok();
//    }

    /**
     * 质控驳回
     */
    @PostMapping("/rejectQualityReview")
    @SysLog("质控驳回")
    @ApiOperation("质控驳回")
    public Result rejectQualityReview(@RequestBody PublicityPageVo publicityPageVo) {
        publicityService.rejectQualityReview(publicityPageVo);

        return Result.ok();
    }

    /**
     * 手动公示
     */
    @PostMapping("/publicityManual")
    @SysLog("手动公示")
    @ApiOperation("手动公示")
    public Result publicityManual(@RequestBody PublicityPageVo publicityPageVo) {
        boolean a = publicityService.publicity(publicityPageVo);
        if (a) {
            publicityService.sendSuccessfulMessage(publicityPageVo.getId());
            return Result.ok();
        } else {
            return Result.error("官网服务异常，公示失败");
        }
    }

//TODO 质控 --> end


//TODO 胶装 --> start

    /**
     * 胶装列表/客服部开放查询
     *
     * @param params
     * @return
     */
    @GetMapping("/getBindingPageList")
    @ApiOperation("胶装列表/客服部开放查询")
    public Result getBindingPageList(@RequestParam Map<String, Object> params) {
        List<PublicityPageVo> list = publicityService.getBindingPageList(params);

        return Result.resultData(list);
    }

    /**
     * 更新下载次数
     *
     * @param projectId
     * @return
     */
    @PostMapping("/updateDownloadNumber")
    @ApiOperation("更新下载次数")
    public Result updateDownloadNumber(Long projectId) {
        publicityService.updateDownloadNumber(projectId);
        return Result.ok();
    }

    /**
     * 胶装
     */
    @PostMapping("/passBinding")
    @SysLog("胶装")
    @ApiOperation("胶装")
    public Result passBinding(@RequestBody PublicityPageVo publicityPageVo) {
        Integer a = publicityService.passBinding(publicityPageVo);
        if (a.equals(2)) {
            return Result.error("官网服务异常，公示失败");
        } else if (a.equals(3)) {
            return Result.error("距签发时间较长，无法胶装。请修改公示中签发日期后再胶装");
        } else {
            publicityService.sendSuccessfulMessage(publicityPageVo.getId());
            return Result.ok();
        }
    }

    /**
     * 修改有无委托协议
     *
     * @param publicityPageVo
     */
    @PostMapping("/saveProtocol")
    @SysLog("修改有无委托协议")
    @ApiOperation("修改有无委托协议")
    public Result saveProtocol(@RequestBody PublicityPageVo publicityPageVo) {
        publicityService.saveProtocol(publicityPageVo);
        return Result.ok();

    }

    /**
     * 质控主管胶装  可自定义公示日期
     */
    @PostMapping("/superBuiding")
    @SysLog("质控主管胶装")
    @ApiOperation("质控主管胶装")
    @RequiresPermissions("super:buiding:admin")
    public Result superBuiding(@RequestBody PublicityPageVo publicityPageVo) {
        Integer a = publicityService.superBuiding(publicityPageVo);
        if (a.equals(2)) {
            return Result.error("官网服务异常，公示失败");
        } else if (a.equals(3)) {
            return Result.error("距签发时间较长，无法胶装。请修改公示中签发日期后再胶装");
        } else {
            publicityService.sendSuccessfulMessage(publicityPageVo.getId());
            return Result.ok();
        }
    }

//TODO 胶装 --> end

//TODO 评价公示-》》》》------------------------------------------------


    /**
     * 获取评价公示详细信息
     */
    @GetMapping("/getPjInfo/{projectId}")
    @ApiOperation("PJ-获取评价公示详细信息")
    public Result getPjInfo(@PathVariable("projectId") Long projectId) {
        PublictyPjInfoVo pjInfoVo = publicityService.getPjInfo(projectId);
        return Result.data(pjInfoVo);
    }

    /**
     * 负责人公示项目列表
     *
     * @param publicPjDto
     * @return
     */
    @GetMapping("/getPjChargePageList")
    @ApiOperation("PJ-评价负责人公示列表")
    public Result getPjChargePageList(PublicPjDto publicPjDto) {
        List<PublicPjPageVo> list = publicityService.getPjChargePublicList(publicPjDto);

        return Result.resultData(list);
    }

    /**
     * 主管公示项目列表
     *
     * @param publicPjDto
     * @return
     */
    @GetMapping("/getPjHeadPageList")
    @ApiOperation("PJ-评价负责人公示列表")
    public Result getPjHeadPageList(PublicPjDto publicPjDto) {
        List<PublicPjPageVo> list = publicityService.getPjHeadPublicList(publicPjDto);

        return Result.resultData(list);
    }


    /**
     * 评价保存公示信息
     */
    @PostMapping("/savePjInfo")
    @SysLog("评价保存公示信息")
    @ApiOperation("PJ-评价保存公示信息")
    public Result savePjInfo(@RequestBody PublictyPjInfoVo pjInfoVo) {
        publicityService.savePjInfo(pjInfoVo);

        return Result.ok();
    }

    /**
     * 评价提交审核
     */
    @PostMapping("/tiJiaoPublicty")
    @SysLog("评价提交审核")
    @ApiOperation("PJ-评价提交审核")
    public Result tiJiaoPublicty(@RequestBody PublictyPjInfoVo pjInfoVo) {
        publicityService.tiJiaoPublicty(pjInfoVo);

        return Result.ok();
    }

    /**
     * 评价撤销审核
     */
    @PostMapping("/cheXiaoPublicty")
    @SysLog("评价撤销审核")
    @ApiOperation("PJ-评价撤销审核")
    public Result cheXiaoPublicty(@RequestBody PublictyPjInfoVo pjInfoVo) {
        publicityService.cheXiaoPublicty(pjInfoVo);

        return Result.ok();
    }


    @PostMapping("/generatePjPdf")
    @SysLog("生成评价公示PDF")
    @ApiOperation("PJ-生成PDF")
    public Result generatePjPdf(HttpServletRequest httpRequest,@RequestBody PublictyPjInfoVo pjInfoVo){
        String path = publicityService.generatePjPdf(httpRequest,pjInfoVo);
        if (path == null) {
            return Result.error("生成失败，python接口返回值异常");
        } else {
            return Result.ok().put("path", path);
        }
    }


}
