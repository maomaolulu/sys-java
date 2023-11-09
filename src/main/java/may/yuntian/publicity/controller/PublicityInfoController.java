package may.yuntian.publicity.controller;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.untils.Result;
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
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;

import may.yuntian.publicity.entity.PublicityInfoEntity;
import may.yuntian.publicity.service.PublicityInfoService;


/**
 * 项目公示记录管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
@RestController
@Api(tags="项目公示记录")
@RequestMapping("/publicityInfo")
public class PublicityInfoController {
    @Autowired
    private PublicityInfoService publicityInfoService;

    /**
     * 检评公示记录列表
     */
    @GetMapping("/listZj")
    @ApiOperation("检评公示记录列表")
    public Result list(@RequestParam Map<String, Object> params){
        List<PublicityPageVo> list = publicityInfoService.getZjList(params);
        return Result.resultData(list);
    }

    /**
     * 评价公示记录列表
     */
    @GetMapping("/listPj")
    @ApiOperation("PJ-评价公示记录列表")
    public Result listPj(@RequestParam Map<String, Object> params){
        List<PublicityPageVo> list = publicityInfoService.getPjList(params);
        return Result.resultData(list);
    }

    /**
     * 质控公示记录列表
     */
    @GetMapping("/listSys")
    @ApiOperation("质控公示记录列表")
    public Result listSys(@RequestParam Map<String, Object> params){
        List<PublicityPageVo> list = publicityInfoService.getSysList(params);
        return Result.resultData(list);
    }

    /**
     * 获取公示记录详细信息
     */
    @GetMapping("/getPublicityInfo/{projectId}")
    @ApiOperation("获取公示记录详细信息")
    public Result getPublicityOne(@PathVariable("projectId") Long projectId){
        List<PublicityInfoEntity> publicityInfos = publicityInfoService.getInfoList(projectId);

        return Result.data(publicityInfos);
    }

}
