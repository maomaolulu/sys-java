package may.yuntian.homepage.controller;

import io.swagger.annotations.Api;
import may.yuntian.homepage.domain.dto.*;
import may.yuntian.homepage.service.ChartsService;
import may.yuntian.untils.Result;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * sys系统主页（业务员、业务总监）
 *
 * @author: liyongqiang
 * @create: 2023-02-23 11:19
 */
@RestController
@Api(tags="主页-图表数据展示")
@RequestMapping("/homepage/chart")
public class ChartsController{

    @Autowired
    private ChartsService chartsService;


    // Todo: 业务员：历年项目回款信息 and 历年回款柱状图（延后开发）


    //TODO：业务总监统计图接口

    /**
     * 整合我的主页-业务员三个统计模块
     */
    @GetMapping("/getSalesPersonCharts")
    public Result getSalesPersonCharts(){
        HashMap<String, Object> map = new HashMap<>(5);
        HashMap<String, Object> signMap = new HashMap<>(2);
        HashMap<String, Object> enterMap = new HashMap<>(2);
        ContractIndexDto contractIndexDto = new ContractIndexDto();
        // 签订合同金额
        contractIndexDto.setPanelOptions("amount");
        try {
            signMap.put("amount", chartsService.getContractIndex(contractIndexDto));
        }catch (Exception e){
            signMap.put("amount", "fail");
        }
        // 签订合同回款
        contractIndexDto.setPanelOptions("return");
        try {
            signMap.put("return", chartsService.getContractIndex(contractIndexDto));
        }catch (Exception e){
            signMap.put("return", "fail");
        }
        // 录入合同回款
        contractIndexDto.setQueryDate("createtime");
        try {
            enterMap.put("return", chartsService.getContractIndex(contractIndexDto));
        }catch (Exception e){
            enterMap.put("return", "fail");
        }
        // 录入合同金额
        contractIndexDto.setPanelOptions("amount");
        try {
            enterMap.put("amount", chartsService.getContractIndex(contractIndexDto));
        }catch (Exception e){
            enterMap.put("amount", "fail");
        }
        map.put("signMap", signMap);
        map.put("enterMap", enterMap);
        // 新老客户分析饼+业务费打款/本年度回款信息
        try {
            map.put("analyseCake", chartsService.getAnalyseCake());
        }catch (Exception e){
            map.put("analyseCake", "fail");
        }
        // 项目节点开票提醒
        try {
            map.put("itemNodeInvoiceRemind", chartsService.getItemNodeInvoiceRemind());
        }catch (Exception e){
            map.put("itemNodeInvoiceRemind", "fail");
        }
        // 已开票未回款情况
        try {
            map.put("returnMoney" , chartsService.getReturnMoney());
        }catch (Exception e){
            map.put("returnMoney", "fail");
        }
        return Result.ok(map);
    }
    /**
     * 业务总监数据
     * @return
     */
    @GetMapping("/getDirectorData1")
    public Result getDirectorData1(){
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("getTypeIncome" ,chartsService.getTypeIncome1());
        } catch (Exception e) {
            map.put("getTypeIncome" ,"业务总监各类别收入图查询失败");
        }
        try {
            map.put("getCollectionBudget" ,chartsService.getCollectionBudget1());
        } catch (Exception e) {
            map.put("getCollectionBudget" ,"每月回款及完成率查询失败");
        }
        try {
            map.put("getSignBudget" ,chartsService.getSignBudget1());
        } catch (Exception e) {
            map.put("getSignBudget" ,"签订合同及完成率查询失败");
        }
        try {
            map.put("getBusinessSource" ,chartsService.getBusinessSource1());
        } catch (Exception e) {
            map.put("getBusinessSource" ,"获取业务来源占比饼图查询失败");
        }
        try {
            map.put("getCompanyOrder" ,chartsService.getCompanyOrder1());
        } catch (Exception e) {
            map.put("getCompanyOrder" ,"获取项目隶属占比饼图查询失败");
        }
        try {
            map.put("getTypes" ,chartsService.getTypes1());
        } catch (Exception e) {
            map.put("getTypes" ,"获取各业务类型占比饼图查询失败");
        }
        try {
            map.put("getSignRanking" ,chartsService.getSignRanking1());
        } catch (Exception e) {
            map.put("getSignRanking" ,"业务员签单排名查询失败");
        }
        try {
            map.put("getBudgetRanking" ,chartsService.getBudgetRanking1());
        } catch (Exception e) {
            map.put("getBudgetRanking" ,"业务员回款排名查询失败");
        }
        try {
            map.put("getAllRanking" ,chartsService.getAllRanking1());
        } catch (Exception e) {
            map.put("getAllRanking" ,"历年回款详情查询失败");
        }
        return Result.ok("业务总监数据",map);
    }

    /**
     * 我的主页-生产看板-生产概览
     */
    @GetMapping("/getProductionOverview")
    public Result getProductionOverview(ProductionOverviewDto dto){
        return Result.ok("查询成功",chartsService.getProductionOverview(dto));
    }

    /**
     * 我的主页-生产看板-项目完成时效
     */
    @GetMapping("/getFinishSpeed")
    public Result getFinishSpeed(FinishSpeedDto dto){
        return Result.ok("查询成功",chartsService.getFinishSpeed(dto));
    }

    /**
     * 我的主页-生产看板-项目完成时效详情
     */
    @GetMapping("/getFinishSpeedDetailList")
    public Result getFinishSpeedDetailList(FinishSpeedDetailDto dto){
        pageUtil2.startPage();
        return Result.resultData(chartsService.getFinishSpeedDetailList(dto));
    }

    /**
     * 我的主页-生产看板-各环节完成时效(类型一)
     */
    @GetMapping("/getFinishSpeedClassOne")
    public Result getFinishSpeedClassOne(FinishSpeedClassDto dto){
        return Result.ok("查询成功",chartsService.getFinishSpeedClassOne(dto));
    }

    /**
     * 我的主页-生产看板-各环节完成时效(类型二)
     */
    @GetMapping("/getFinishSpeedClassTwo")
    public Result getFinishSpeedClassTwo(FinishSpeedClassDto dto){
        return Result.ok("查询成功", chartsService.getFinishSpeedClassTwo(dto));
    }

    /**
     * 我的主页-生产看板-项目完成时效 导出查询所有数据
     */
    @GetMapping("/getFinishSpeedDetailAllList")
    public Result getFinishSpeedDetailAllList(FinishSpeedDetailDto dto){
        return Result.ok("查询成功",chartsService.getFinishSpeedDetailList(dto));
    }
}
