package may.yuntian.homepage.service;

import com.alibaba.fastjson.JSONObject;
import may.yuntian.homepage.domain.dto.*;
import may.yuntian.homepage.domain.vo.ProjectSpeedDetailVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-02-23 17:03
 */
public interface ChartsService {

    /**
     * 业务员：新老客户分析饼
     * @return map
     */
    Map<String, Object> getAnalyseCake();

    /**
     * 业务员：项目节点开票提醒
     * @return jsonObject
     */
    JSONObject getItemNodeInvoiceRemind();

    /**
     * 业务员：已开票未回款情况
     * @return map
     */
    Map<String, Object> getReturnMoney();

    /**
     * 业务员：合同指标
     * @param contractIndexDto 面板选项
     * @return map
     */
    Map<String, Object> getContractIndex(ContractIndexDto contractIndexDto);

    //TODO 业务总监接口
    /**
     * 业务总监各类别收入图
     * @return
     */
    Map<String,Object> getTypeIncome1();

    /**
     * 业务总监
     * 每月回款及完成率
     * @return
     */
    Map<String, Object> getCollectionBudget1();

    /**
     * 业务总监
     * 签订合同及完成率
     * @return
     */
    Map<String, Object> getSignBudget1();

    /**
     * 业务总监
     * 获取业务来源占比饼图--动态
     */
    Map<String,String> getBusinessSource1();

    /**
     * 业务总监
     * 获取项目隶属占比饼图--动态
     */
    Map<String,String> getCompanyOrder1();

    /**
     * 业务总监
     * 获取各业务类型占比饼图--动态
     */
    Map<String,String> getTypes1();
    /**
     * 业务总监
     * 业务员签单排名
     * @return
     */
    HashMap<String,Long> getSignRanking1();

    /**
     * 业务总监
     * 业务员回款排名
     * @return
     */
    HashMap<String, String> getBudgetRanking1();

    /**
     * 业务总监
     * 历年回款详情
     * @return
     */
    Map<String,Map<Integer,String>>  getAllRanking1();

    /**
     * 生产概览
     * 查看已签单/已完成的项目净值，及已完成的报告数量
     * @param dto 查询条件
     * @return <时间,数据<列名,数值>>
     */
    Map<String,Map<String,Object>> getProductionOverview(ProductionOverviewDto dto);

    /**
     * 项目完成时效
     * 查看生产部门项目的整体完成时效（从任务下发到报告签发）
     * @param dto 查询条件
     * @return <时间段,数据<列名,数值>>
     */
    Map<String,Map<String,Integer>> getFinishSpeed(FinishSpeedDto dto);

    /**
     * 项目完成时效详情
     * 展示工作完成时效项目详情，可查询、导出数据
     * @param dto 查询条件
     * @return List<ProjectSpeedDetailVo>
     */
    List<ProjectSpeedDetailVo> getFinishSpeedDetailList(FinishSpeedDetailDto dto);

    /**
     * 各环节完成时效(类型一)
     * 查看各生产环节的工作完成时效
     * @param dto 查询条件
     * @return <时间段,数据<列名,数值>>
     */
    Map<String,Map<String,Integer>> getFinishSpeedClassOne(FinishSpeedClassDto dto);

    /**
     * 各环节完成时效(类型二)
     * 查看各生产环节的工作完成时效
     * @param dto 查询条件
     * @return <时间段,数据<列名,数值>>
     */
    Map<String,Map<String,Integer>> getFinishSpeedClassTwo(FinishSpeedClassDto dto);
}
