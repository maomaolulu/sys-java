package may.yuntian.anlianwage.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlianwage.entity.GradePointEntity;
import may.yuntian.anlianwage.service.GradePointService;
import may.yuntian.anlianwage.vo.ProjectPerformanceVo;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: ANLIAN-JAVA
 * @description: 评价-绩效控制层
 * @author: liyongqiang
 * @create: 2022-05-29 17:20
 */
@RestController
@Api(tags="评价-绩效")
@RequestMapping("anlianwage/performance")
@Slf4j
@SuppressWarnings("all")
public class PerformanceController {

    @Autowired
    private GradePointService gradePointService;

    /**
     * 不分页查询评价项目的绩效列表
     */
    @GetMapping("/listAll")
    @ApiOperation("不分页查询评价项目的绩效列表")
    @AuthCode(url = "",system = "")
    public R listAll(ProjectPerformanceVo proPerformanceVo, AuthCodeVo authCodeVo){
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            proPerformanceVo.setSubjection("");
        }else {
            // 登录人隶属公司
            String subjection = ShiroUtils.getUserEntity().getSubjection();
            proPerformanceVo.setSubjection(subjection);
        }

        log.info("listAll params={}", JSONObject.toJSONString(proPerformanceVo));
        List<ProjectPerformanceVo> list = gradePointService.listAll(proPerformanceVo);
        return R.ok().put("data", list);  // List<ProjectPerformanceVo>
    }

    /**
     * 单条编辑保存：填写技术打分后，计算项目绩点、总绩效、差值，保存到数据库并将结果返回给前端！
     * （暂不使用，无编辑修改按钮。）
     */
    @PostMapping("/editSave")
    @ApiOperation("填写技术打分,计算结果，保存")
    public R editSave(GradePointEntity gradePointEntity){
        // 计算绩效并保存
        log.info("editSave params={}", JSONObject.toJSONString(gradePointEntity));
        GradePointEntity entity = gradePointService.editSave(gradePointEntity);
        return R.ok().put("data" , entity); // gradePointEntity
    }

    /**
     * 批量保存：在页面技术打分栏填写完所有的值，再进行批量计算、保存！
     */
    @PostMapping("/saveBatch")
    @ApiOperation("技术打分栏的批量计算、保存")
    public R saveBatch(@RequestBody List<GradePointEntity> gradePointEntityList){
        log.info("saveBatch params={}", JSONObject.toJSONString(gradePointEntityList));
        // 根据projectId，批量计算，再保存 ---> 本质还是逐个计算！
        List<GradePointEntity> gradePointEntities = new ArrayList<>();

        for (GradePointEntity gradePointEntity : gradePointEntityList) {
            //技术分未填写的不给计算，已计算过的不再重复计算！
            // 判断技术打分是否填写?
            if (gradePointEntity.getTechnicalGradeScore() != null){
                /**
                 * 判断是否已计算?（如果差值difference为空，肯定没计算;否则，已计算。）
                 * 问题：如果计算完得到差值，打分人员修改了技术分呢？  加个技术分是否修改判断，比较麻烦。
                 * 最简单的做法：不管有没计算过，全给重新计算一遍！
                 */
                    // 调用editSave()方法逐条计算
                    GradePointEntity entity = gradePointService.editSave(gradePointEntity);
                    gradePointEntities.add(entity);
            }
        }

       // gradePointService.saveOrUpdateBatch(gradePointEntities);
        return R.ok().put("data",gradePointEntities);
    }


    /**
     * 导出Excel
     */
    @PostMapping("/export")
    @ApiOperation("导出评价项目绩效表")
    public void export(HttpServletResponse response , @RequestBody ProjectPerformanceVo proPerformanceVo){
        gradePointService.exportExcel(response , proPerformanceVo);
    }

    // 项目提成脚本：评价报告提成！
    @PostMapping("/getCommission")
    public R getCommission(@RequestBody Long[] projectIds){
        // 计算绩效并保存
        log.info("getCommission params={}", JSONObject.toJSONString(projectIds));
        gradePointService.getCommission(Arrays.asList(projectIds));
        return R.ok();
    }

}
