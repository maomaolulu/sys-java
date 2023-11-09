package may.yuntian.filiale.hzyd.controller;

import io.swagger.annotations.Api;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.filiale.hzyd.vo.ContractVo;
import may.yuntian.filiale.hzyd.service.ContractManageService;
import may.yuntian.filiale.hzyd.vo.DataRecordVo;
import may.yuntian.untils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 亿达-业务中心：合同管理controller
 *
 * @author: liyongqiang
 * @create: 2023-08-11 12:14
 */
@RestController
@Api(tags="亿达-业务中心-合同管理")
@RequestMapping("/yd/contract/manage")
public class ContractManageController {

    private final ContractManageService contractManageService;

    public ContractManageController(ContractManageService contractManageService) {
        this.contractManageService = contractManageService;
    }

    /**
     * 项目or合同编号：流水号
     */
    @GetMapping("/generate/code")
    public Result generateCode(String code, Integer flag) {
        return Result.data(contractManageService.generateCode(code, flag));
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result contractList(ContractVo contractVo) {
        return Result.resultData(contractManageService.selectContractList(contractVo));
    }

    /**
     * 合同-项目-类目：列表
     */
    @GetMapping("/basic")
    public Result basicRelationList() {
        return Result.ok("查询成功", contractManageService.selectBasicRelationList());
    }

    /**
     * 检测信息
     */
    @GetMapping("/detect_info")
    public Result detectInfo(String detectInfoCategory) {
        return Result.data(contractManageService.selectDetectInfo(detectInfoCategory));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    @SysLog("YD-新增合同")
    public Result add(@RequestBody DataRecordVo dataRecordVo) {
        contractManageService.insertDataRecord(dataRecordVo);
        return Result.ok("新增成功");
    }

    /**
     * 编辑
     */
    @SysLog("YD-编辑合同")
    @PostMapping("/edit")
    public Result editContract(@RequestBody ContractVo contractVo) {
        return Result.ok(contractManageService.editContractById(contractVo) > 0 ? "编辑成功" : "编辑失败");
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public Result contractDetail(Long contractId) {
        return Result.data(contractManageService.contractDetailById(contractId));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{contractId}")
    @SysLog("YD-删除合同")
    public Result removeContract(@PathVariable Long contractId) {
        return Result.ok(contractManageService.removeContractById(contractId) > 0 ? "删除成功" : "删除失败");
    }

}
