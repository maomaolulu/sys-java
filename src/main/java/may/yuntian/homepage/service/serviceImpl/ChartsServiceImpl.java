package may.yuntian.homepage.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.homepage.constant.CollectionBudget;
import may.yuntian.homepage.domain.dto.*;
import may.yuntian.homepage.domain.vo.*;
import may.yuntian.homepage.entity.SalesmanIndexEntity;
import may.yuntian.homepage.mapper.SalesmanIndexMapper;
import may.yuntian.homepage.service.ChartsService;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-02-23 17:03
 */
@Service("chartsService")
public class ChartsServiceImpl implements ChartsService {

    /**
     * 自定义面板选项常量: quality（数量）; amount（金额）。
     */
    static final String PANEL_OPTION_ONE = "quality";
    static final String PANEL_OPTION_TWO = "amount";

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private SalesmanIndexMapper salesmanIndexMapper;

    /**
     * 业务员：新老客户分析饼 + 业务费打款 + 本年度项目回款信息。
     */
    @Override
    public Map<String, Object> getAnalyseCake() {
        Map<String, Object> map = new HashMap<>();
        AnalyseCakeVo conditions = getQueryConditions();
        // 1.新老客户分析饼
        List<AnalyseCakeVo> newOldClientList = projectMapper.getNewOldClientList(conditions.getStartDate(), conditions.getEndDate(), conditions.getSalesmenid());
        int totalCount = newOldClientList.size();
        if (CollUtil.isEmpty(newOldClientList)){
            map.put("oldCustomerCount", "0");
            map.put("newCustomerCount", "0");
            map.put("contractRate", "0");
        } else {
            long oldCustomerCount = newOldClientList.stream().filter(analyseCakeVo -> analyseCakeVo.getOld() == 1).count();
            long newCustomerCount = totalCount - oldCustomerCount;
            map.put("oldCustomerCount", String.valueOf(oldCustomerCount));
            map.put("newCustomerCount", String.valueOf(newCustomerCount));
            BigDecimal divide = BigDecimal.valueOf(oldCustomerCount).divide(BigDecimal.valueOf(totalCount), 3, BigDecimal.ROUND_HALF_UP);
            NumberFormat percent = NumberFormat.getPercentInstance();
            // 设置数字的小数部分所允许最大位数
            percent.setMaximumFractionDigits(2);
            map.put("contractRate", percent.format(divide.doubleValue()));
        }
        // 2.业务费打款 and 本年度回款信息
        FeesCollectionVo feesCollection = projectMapper.getTotalFees(conditions.getStartDate(), conditions.getEndDate(), conditions.getSalesmenid());
        if (feesCollection == null){
            map.put("serviceFeePaid", "0");
            map.put("dealWithFees", "0");
            map.put("contractAmount", "0");
            map.put("payInReturn", "0");
        } else {
            BigDecimal serviceFeePaid = feesCollection.getDealWithFees().subtract(feesCollection.getUnsettledCommission());
            map.put("serviceFeePaid", serviceFeePaid.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP).toString());
            map.put("dealWithFees", feesCollection.getDealWithFees().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP).toString());
            map.put("contractAmount", feesCollection.getContractAmount().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP).toString());
            map.put("payInReturn", feesCollection.getPayInReturn().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP).toString());
        }
        return map;
    }

    /**
     * 业务员：项目节点开票提醒
     */
    @Override
    public JSONObject getItemNodeInvoiceRemind() {
        JSONObject jsonObject = new JSONObject();
        AnalyseCakeVo queryConditions = getQueryConditions();
        List<AnalyseCakeVo> remindList = projectMapper.getItemNodeInvoiceRemind(queryConditions.getSalesmenid(), queryConditions.getStartDate(), queryConditions.getEndDate());
        jsonObject.put("count", remindList.size());
        jsonObject.put("remindList", remindList);
        return jsonObject;
    }

    /**
     * 业务员：已开票未回款情况
     */
    @Override
    public Map<String, Object> getReturnMoney() {
        Map<String, Object> map = new HashMap<>(2);
//        BigDecimal o = new BigDecimal("0.00");
        List<String> leftDataList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        List<String> rightDataList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        AnalyseCakeVo selectCondition = getQueryConditions();
        List<AnalyseCakeVo> voList = projectMapper.getItemNodeInvoiceRemind(selectCondition.getSalesmenid(), selectCondition.getStartDate(), selectCondition.getEndDate());
        if (CollUtil.isNotEmpty(voList)) {
            Map<Integer, List<AnalyseCakeVo>> byMonthGroup = voList.stream().filter(analyseCakeVo -> analyseCakeVo.getTotalMoney().compareTo(analyseCakeVo.getReceiptMoney()) != 0).collect(Collectors.groupingBy(AnalyseCakeVo::getMonth));
            byMonthGroup.forEach((month, valueList) -> {
                BigDecimal sumReturnMoney = valueList.stream().map(AnalyseCakeVo::getReceiptMoney).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal sumInvoiceMoney = valueList.stream().map(AnalyseCakeVo::getInvoiceMoney).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                leftDataList.set(month - 1, sumReturnMoney.toString());
                rightDataList.set(month -1, sumInvoiceMoney.toString());
            });
        }
        map.put("leftData", leftDataList);
        map.put("rightData", rightDataList);
        return map;
    }

    /**
     * 业务员：合同指标 + 当年回款
     *
     * @param contractIndexDto 面板选项
     * @return map
     */
    @Override
    public Map<String, Object> getContractIndex(ContractIndexDto contractIndexDto) {
        String panelOptions = contractIndexDto.getPanelOptions();
        Map<String, Object> map = new HashMap<>(8);
        List<Integer> planList = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);
        List<String> factList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        List<String> percentList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        /**
         * ToDo：各公司中介情况暂不考虑，业务方需求待定（中介名单：sys_user表用户名以"Z-"开头。）
         * ToDo：or 中介这部分单独分离出来，先搞业务员和业务总监，后续写中介相关接口！
         */
        Long userId = ShiroUtils.getUserEntity().getUserId();
        SalesmanIndexEntity indexEntity = salesmanIndexMapper.selectOne(new LambdaQueryWrapper<SalesmanIndexEntity>().eq(SalesmanIndexEntity::getSalesmanId, userId));
        if (indexEntity != null) {
            String[] split = (PANEL_OPTION_ONE.equals(panelOptions) || PANEL_OPTION_TWO.equals(panelOptions)) ? indexEntity.getMonthTarget().split(",") : indexEntity.getReturnMonthIndex().split(",");
            planList = Arrays.stream(split).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }
//        AnalyseCakeVo analyseVo = getQueryConditions();
        QueryWrapper<ProjectEntity> queryWrapper = contractIndexQuery(contractIndexDto);
//        String data;
//        if ("createtime".equals(contractIndexDto.getQueryDate())){
//            data = "ap.createtime";
//        }else {
//            data = "apd.sign_date";
//        }
        // newCommission Date(122, 0, 01), newCommission Date(123, 0, 01)
        List<AnalyseCakeVo> contractList = projectMapper.getContractIndex(queryWrapper,contractIndexDto.getQueryDate());
        if (PANEL_OPTION_ONE.equals(panelOptions)) {
            /** ToDo：(合同指标-数量面板) 待业务方提供合同份额指标表！ 此面板需求业务方已取消。 **/
            throw new RRException("待业务方提供合同份额指标！");
        } else {
            // 合同指标-金额面板 and 当年回款页面
            getSumMoney(planList, factList, percentList, contractList, panelOptions);
        }
        map.put("blue", planList);
        map.put("cyan", factList);
        map.put("yellow", percentList);
        return map;
    }


    public QueryWrapper<ProjectEntity> contractIndexQuery(ContractIndexDto contractIndexDto){
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        AnalyseCakeVo analyseVo = getQueryConditions();
        queryWrapper.eq("ap.salesmenid",analyseVo.getSalesmenid());
//        queryWrapper.eq("ap.salesmenid",458);
        if ("createtime".equals(contractIndexDto.getQueryDate())){
//            queryWrapper.select("select ap.total_money, apa.receipt_money, MID(ap.createtime,6,2) as 'month'\n" +
//                    "from al_project as ap \n" +
//                    "left join al_project_date as apd on ap.id = apd.project_id\n" +
//                    "left join al_project_amount as apa on ap.id = apa.project_id\n");
//            queryWrapper.eq("ap.salesmenid",analyseVo.getSalesmenid());
            queryWrapper.ge("ap.createtime",analyseVo.getStartDate());
            queryWrapper.le("ap.createtime",analyseVo.getEndDate());
            queryWrapper.orderByAsc("ap.createtime");
        }else {
//            queryWrapper.select("select ap.total_money, apa.receipt_money, MID(apd.sign_date,6,2) as 'month'\n" +
//                    "from al_project as ap \n" +
//                    "left join al_project_date as apd on ap.id = apd.project_id\n" +
//                    "left join al_project_amount as apa on ap.id = apa.project_id\n");
//            queryWrapper.eq("ap.salesmenid",analyseVo.getSalesmenid());
            queryWrapper.ge("apd.sign_date",analyseVo.getStartDate());
            queryWrapper.le("apd.sign_date",analyseVo.getEndDate());
            queryWrapper.orderByAsc("apd.sign_date");
        }

        return queryWrapper;
    }

    /**
     * 公共方法抽取：计算求和（total_money：项目金额。 receipt_money：回款额。）
     *
     * @param planList     指标列表
     * @param factList     实际完成额列表
     * @param percentList  每月占比列表
     * @param contractList 需要的数据List
     * @param panelOptions 面板选项（接口调用指南）
     */
    private void getSumMoney(List<Integer> planList, List<String> factList, List<String> percentList, List<AnalyseCakeVo> contractList, String panelOptions) {
        if (CollUtil.isNotEmpty(contractList)){
            Map<Integer, List<AnalyseCakeVo>> collectMap = contractList.stream().collect(Collectors.groupingBy(AnalyseCakeVo::getMonth));
            List<Integer> finalPlanList = planList;
            collectMap.forEach((month, valueList) -> {
                BigDecimal sumTotalMoney = "return".equals(panelOptions) ? valueList.stream().map(AnalyseCakeVo::getReceiptMoney).reduce(BigDecimal.ZERO, BigDecimal::add) : valueList.stream().map(AnalyseCakeVo::getTotalMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 单位换算成：万元
                BigDecimal divide = sumTotalMoney.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                factList.set(month - 1, divide.toString());
                Integer monthlyTarget = (Integer) finalPlanList.get(month - 1);
                if (monthlyTarget == 0) {
                    percentList.set(month - 1, "0");
                } else {
                    String format = NumberFormat.getPercentInstance().format(sumTotalMoney.divide(new BigDecimal(monthlyTarget + "0000"), 2, RoundingMode.HALF_UP).doubleValue());
                    percentList.set(month - 1, format.substring(0, format.length() - 1));
                }
            });
        }
    }


    /**
     * 公共查询条件方法抽取（userId、startDate、endDate）
     */
    private static AnalyseCakeVo getQueryConditions() {
        //TODO 后续有根据年份查询需求可将参数传入此方法封装
        AnalyseCakeVo cakeVo = new AnalyseCakeVo();
        String year = DateUtils.dateTimeNow("yyyy");
        Long loginUserId = ShiroUtils.getUserEntity().getUserId();
        // 组装查询条件的起始日期和结束日期
        Date startDate = DateUtils.dateTime("yyyy-MM-dd", year.concat("-01-01"));
        Date endDate = DateUtils.dateTime("yyyy-MM-dd", String.valueOf(Integer.parseInt(year) + 1).concat("-01-01"));
        cakeVo.setSalesmenid(loginUserId);
        cakeVo.setStartDate(startDate);
        cakeVo.setEndDate(endDate);
        return cakeVo;
    }

//Todo 业务总监--- start

    /**
     * 业务总监各类别收入图
     *
     * @return
     */
    @Override
    public Map<String, Object> getTypeIncome1() {

        BigDecimal zhiweidivide = BigDecimal.ZERO;
        BigDecimal huanWeidivide = BigDecimal.ZERO;
        BigDecimal fuShedivide = BigDecimal.ZERO;
        Map<String, Object> map = new HashMap<>();
        zhiweidivide = projectMapper.getTypeIncome(getCollectionByTypesWrapper(CollectionBudget.zhiWeiTypes));
        huanWeidivide = projectMapper.getTypeIncome(getCollectionByTypesWrapper(CollectionBudget.huanJingTypes));
        fuShedivide = projectMapper.getTypeIncome(getCollectionByTypesWrapper(CollectionBudget.fuSheTypes));
        map.put("zhiweiBudget", CollectionBudget.zhiWeiBudget2023.toString());
        map.put("zhiweiMoney", zhiweidivide.toString());
        map.put("huanweiBudget", CollectionBudget.huanJingBudget2023.toString());
        map.put("huanweiMoney", huanWeidivide.toString());
        map.put("fusheBudget", CollectionBudget.fuSheBudget2023.toString());
        map.put("fusheMoney", fuShedivide.toString());
        return map;
    }


    /**
     * 业务总监
     * 每月回款及完成率
     *
     * @return
     */
    @Override
    public Map<String, Object> getCollectionBudget1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        Map<String, Object> map = new HashMap<>();
        List<Integer> planList = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);
        List<String> factList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        List<String> percentList = Arrays.asList("0%","0%","0%","0%","0%","0%","0%","0%","0%","0%","0%","0%");

        String[] split = CollectionBudget.collectionBudget2023.split(",");
        planList = Arrays.stream(split).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        List<BusinessDirectorVo> list = projectMapper.getBudgetAndContractMoney1(cakeVo.getStartDate(), cakeVo.getEndDate());
        getCommissioner1(planList, factList, percentList, list, "return");

        map.put("blue", planList);
        map.put("cyan", factList);
        map.put("yellow", percentList);
        return map;
    }


    /**
     * 业务总监
     * 签订合同及完成率
     *
     * @return
     */
    @Override
    public Map<String, Object> getSignBudget1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        Map<String, Object> map = new HashMap<>();
        List<Integer> planList = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);
        List<String> factList = Arrays.asList("0","0","0","0","0","0","0","0","0","0","0","0");
        List<String> percentList = Arrays.asList("0%","0%","0%","0%","0%","0%","0%","0%","0%","0%","0%","0%");

        String[] split = CollectionBudget.signBudget2023.split(",");
        planList = Arrays.stream(split).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        List<BusinessDirectorVo> list = projectMapper.getBudgetAndContractMoney1(cakeVo.getStartDate(), cakeVo.getEndDate());
        getCommissioner1(planList, factList, percentList, list, "totalMoney");

        map.put("blue", planList);
        map.put("cyan", factList);
        map.put("yellow", percentList);
        return map;
    }

    /**
     * 业务总监
     * 获取业务来源占比饼图--动态
     */
    @Override
    public Map<String, String> getBusinessSource1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        Map<String, String> map = new HashMap<>();
        List<BusinessDirectorVo> list = projectMapper.getBusinessSource(cakeVo.getStartDate(), cakeVo.getEndDate());
        if (list.size() > 0) {
            Map<String, BigDecimal> listMap = list.stream().collect(Collectors.toMap(BusinessDirectorVo::getBusinessSource, BusinessDirectorVo::getTotalMoney));
            map = listMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        }
        return map;
    }


    /**
     * 业务总监
     * 获取项目隶属占比饼图--动态
     */
    @Override
    public Map<String, String> getCompanyOrder1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        Map<String, String> map = new HashMap<>();
        List<BusinessDirectorVo> list = projectMapper.getCompanyOrder(cakeVo.getStartDate(), cakeVo.getEndDate());
        if (list.size() > 0) {
            Map<String, BigDecimal> listMap = list.stream().collect(Collectors.toMap(BusinessDirectorVo::getCompanyOrder, BusinessDirectorVo::getTotalMoney));
            map = listMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        }
        return map;
    }


    /**
     * 业务总监
     * 获取各业务类型占比饼图--动态
     */
    @Override
    public Map<String, String> getTypes1() {
        BigDecimal zhiweidivide = BigDecimal.ZERO;
        BigDecimal huanWeidivide = BigDecimal.ZERO;
        BigDecimal fuShedivide = BigDecimal.ZERO;
        Map<String, String> map = new HashMap<>();
        fuShedivide = projectMapper.getTypes(getCollectionByTypesWrapper(CollectionBudget.fuSheTypes));
        zhiweidivide = projectMapper.getTypes(getCollectionByTypesWrapper(CollectionBudget.zhiWeiTypes));
        huanWeidivide = projectMapper.getTypes(getCollectionByTypesWrapper(CollectionBudget.huanJingTypes));
        map.put("辐射", fuShedivide.toString());
        map.put("职卫", zhiweidivide.toString());
        map.put("环卫", huanWeidivide.toString());
        return map;
    }

    /**
     * 业务总监
     * 业务员签单排名
     *
     * @return
     */
    @Override
    public HashMap<String, Long> getSignRanking1() {
        List<BusinessDirectorVo> list = projectMapper.getSignRanking(getBudgetRankingWrapper1());
        HashMap<String, Long> finalMap = new LinkedHashMap<String, Long>();
        if (list.size() > 0) {
            Map<String, Long> map = list.stream().collect(Collectors.toMap(BusinessDirectorVo::getSalesmen, BusinessDirectorVo::getQuantity));
            List<Map.Entry<String, Long>> mapList = map.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList());
            //遍历集合，将排好序的键值对Entry<K,V>放入新的map并返回
            //因为HashMap默认是按照键排序，所以新的map还是HashMap，那么就还原了。达不到排序的效果。
            //所以新的map需要LinkedHashMap类型。这里也可以看出LinkedHashMap是按照顺序存储。
            mapList.forEach(ele -> finalMap.put(ele.getKey(), ele.getValue()));
        }
        return finalMap;
    }

    /**
     * 业务总监
     * 业务员回款排名
     *
     * @return
     */
    @Override
    public HashMap<String, String> getBudgetRanking1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        HashMap<String, String> finalMap = new LinkedHashMap<String, String>();
        List<BusinessDirectorVo> list = projectMapper.getBudgetRanking1(getBudgetRankingWrapper1());
        if (list.size() > 0) {
            Map<String, BigDecimal> map = list.stream().collect(Collectors.toMap(BusinessDirectorVo::getSalesmen, BusinessDirectorVo::getReceiptMoney));
            List<Map.Entry<String, BigDecimal>> mapList = map.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList());
            //遍历集合，将排好序的键值对Entry<K,V>放入新的map并返回
            //因为HashMap默认是按照键排序，所以新的map还是HashMap，那么就还原了。达不到排序的效果。
            //所以新的map需要LinkedHashMap类型。这里也可以看出LinkedHashMap是按照顺序存储。
            mapList.forEach(ele -> finalMap.put(ele.getKey(), ele.getValue().toString()));
        }

        return finalMap;
    }


    /**
     * 业务总监
     * 历年回款详情
     *
     * @return
     */
    @Override
    public Map<String, Map<Integer, String>> getAllRanking1() {
        List<BusinessDirectorVo> list = projectMapper.getAllRanking1();
        Map<String, Map<Integer, String>> returnMap = new HashMap<>();
        Map<Integer, String> huikuanMap;
        Map<Integer, BigDecimal> map = list.stream().filter(i->StringUtils.isNotNull(i.getYear())).collect(Collectors.toMap(BusinessDirectorVo::getYear, BusinessDirectorVo::getReceiptMoney));
        if (CollUtil.isNotEmpty(map)) {
            huikuanMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
            returnMap.put("huikuan", huikuanMap);
        }
        Map<Integer, String> qiandanMap;
        Map<Integer, BigDecimal> map1 = list.stream().filter(i->StringUtils.isNotNull(i.getYear())).collect(Collectors.toMap(BusinessDirectorVo::getYear, BusinessDirectorVo::getTotalMoney));
        if (CollUtil.isNotEmpty(map1)) {
            System.out.println("map1 = " + map1);
            qiandanMap = map1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
            returnMap.put("qianfa", qiandanMap);
        }
        return returnMap;
    }


    /**
     * 处理业务总监月份完成目标图表方法
     *
     * @param planList     预算数组
     * @param factList     已完成数组
     * @param percentList  比例数组 完成/预算
     * @param contractList
     * @param type         统计类型 return 回款
     */
    private void getCommissioner1(List<Integer> planList, List<String> factList, List<String> percentList, List<BusinessDirectorVo> contractList, String type) {
        if (CollUtil.isNotEmpty(contractList)) {
            Map<Integer, BigDecimal> collectMap = contractList.stream().collect(Collectors.toMap(BusinessDirectorVo::getMonth, "return".equals(type) ? BusinessDirectorVo::getReceiptMoney : BusinessDirectorVo::getTotalMoney));
            collectMap.forEach((month, sumTotalMoney) -> {
                factList.set(month - 1, sumTotalMoney.toString());
                Integer monthlyTarget = (Integer) planList.get(month - 1);
                if (monthlyTarget == 0) {
                    percentList.set(month - 1, "0%");
                } else {
                    percentList.set(month - 1, NumberFormat.getPercentInstance().format(sumTotalMoney.divide(new BigDecimal(monthlyTarget), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                }
            });
        }
    }


    /**
     * 业务总监业务员查询器
     *
     * @return
     */
    private QueryWrapper<ProjectEntity> getBudgetRankingWrapper1() {
        AnalyseCakeVo cakeVo = getQueryConditions();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("pd.sign_date", cakeVo.getEndDate());
        queryWrapper.ge("pd.sign_date", cakeVo.getStartDate());
        queryWrapper.in("p.salesmen", Arrays.asList(CollectionBudget.salesmans));
        queryWrapper.groupBy("p.salesmen");
        return queryWrapper;
    }

    /**
     * 业务总监类型查询器
     *
     * @return
     */
    private QueryWrapper<ProjectEntity> getCollectionByTypesWrapper(String[] types) {
        AnalyseCakeVo cakeVo = getQueryConditions();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("pd.sign_date", cakeVo.getEndDate());
        queryWrapper.ge("pd.sign_date", cakeVo.getStartDate());
        queryWrapper.in("p.type", Arrays.asList(types));
        return queryWrapper;
    }

    /**
     * 生产概览
     * 查看已签单/已完成的项目净值，及已完成的报告数量
     */
    @Override
    public Map<String,Map<String,Object>> getProductionOverview(ProductionOverviewDto dto){
        Date startTime = null, endTime = null;
        startTime = dto.getStartTime();
        switch (dto.getTimeLevel()) {
            case "year":// 加一年
                startTime = DateUtil.parse(DateUtil.year(dto.getStartTime()) + "-01-01", "yyyy-MM-dd");
                endTime = DateUtil.parse((DateUtil.year(dto.getEndTime()) + 1) + "-01-01", "yyyy-MM-dd");
                break;
            case "month":// 加一个月
                endTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(dto.getEndTime(), 1));
                break;
            case "day": // 加一天
                endTime = DateUtil.offsetDay(dto.getEndTime(), 1);
                break;
            default: break;
        }
        Wrapper<Object> netValueWrapper = getWrapper(dto,true, startTime, endTime);
        Wrapper<Object> finishOrPublicityWrapper = getWrapper(dto,false, startTime, endTime);
        List<ProductionOverviewVo> productionOverviews = projectMapper.getProductionOverviewNetValue(netValueWrapper);
        List<ProductionOverviewVo> productionOverFinishs = projectMapper.getProductionOverviewPublicityInfo(finishOrPublicityWrapper);
        return mapHandler(dto.getTimeLevel(), productionOverviews, productionOverFinishs, startTime, endTime);
    }

    private QueryWrapper<Object> getWrapper(ProductionOverviewDto dto, Boolean ifNetValue, Date startTime,Date endTime){
        List<Integer> lostProjects = new ArrayList<>();
        lostProjects.add(1);
        lostProjects.add(98);
        lostProjects.add(99);
        QueryWrapper<Object> wrapper = new QueryWrapper<>()
                .eq(StringUtils.isNotBlank(dto.getCompanyOrder()),"ap.company_order",dto.getCompanyOrder())
                .eq(StringUtils.isNotBlank(dto.getType()),"ap.type",dto.getType())
                .notIn("ap.`status`",lostProjects);
        if (ifNetValue){
            wrapper.ge("apd.sign_date", startTime).lt("apd.sign_date", endTime);
        }else {
            wrapper.ge("apd.report_issue", startTime).lt("apd.report_issue", endTime).ge("ap.status",40);
        }
        return wrapper;
    }

    private Map<String,Map<String,Object>> mapHandler(String timelevel, List<ProductionOverviewVo> list1, List<ProductionOverviewVo> list3, Date sTime, Date eTime){
        Map<String,Map<String,Object>> returnMap = new LinkedHashMap<>();
        Map<String, List<ProductionOverviewVo>> netValueMap = new HashMap<>(12);
        Map<String, List<ProductionOverviewVo>> finishMap = new HashMap<>(12);
        String yearFormat = "yyyy", monthFormat = "yyyy-MM", dateFormat = "MM-dd";
        switch (timelevel){
            case "year":
                netValueMap = list1.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getYear));
                finishMap = list3.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getYear));
                dateFormat = yearFormat;
                break;
            case "month":
                netValueMap = list1.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getMonth));
                finishMap = list3.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getMonth));
                dateFormat = monthFormat;
                break;
            case "day":
                netValueMap = list1.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getDay));
                finishMap = list3.stream().collect(Collectors.groupingBy(ProductionOverviewVo::getDay));
                break;
            default:
                break;
        }
        long i = sTime.getTime();
        while (i < eTime.getTime()) {
            Date key = new Date(i);
            String stringKey = DateUtil.format(key, "yyyy-MM-dd");
            Map<String, Object> map = new HashMap<>(3);
            // 填充已完成项目净值
            if (netValueMap.containsKey(stringKey)) {
                List<ProductionOverviewVo> list = netValueMap.get(stringKey);
                map.put("started", list.stream().map(ProductionOverviewVo::getNetvalue).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
            } else {
                map.put("started", 0);
            }
            //已签发项目净值和报告数量(等同于项目数量)
            if (finishMap.containsKey(stringKey)) {
                List<ProductionOverviewVo> list = finishMap.get(stringKey);
                map.put("finished", list.stream().map(ProductionOverviewVo::getNetvalue).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
                map.put("reports", finishMap.get(stringKey).size()+"");
            } else {
                map.put("finished", 0);
                map.put("reports", 0);
            }

            returnMap.put(DateUtil.format(key, dateFormat), map);
            Date nextStep = null;
            switch (timelevel) {
                case "year":
                    nextStep = DateUtil.parse((DateUtil.year(key)+1) + "-01-01", "yyyy-MM-dd");
                    break;
                case "month":
                    nextStep = DateUtil.offsetMonth(key, 1);
                    break;
                case "day":
                    nextStep = DateUtil.offsetDay(key, 1);
                    break;
                default:
                    return null;
            }
            assert nextStep != null;
            i = i + nextStep.getTime()- key.getTime();
        }
        return returnMap;
    }

    @Override
    public Map<String,Map<String,Integer>> getFinishSpeed(FinishSpeedDto dto){
        Map<String,Map<String,Integer>> returnMap = new LinkedHashMap<>();
        Date startTime = dto.getSTime(), endTime = null;
        switch (dto.getTimeLevel()){
            case "year":
                startTime = DateUtil.parse(DateUtil.year(startTime)+"-01-01","yyyy-MM-dd");
                endTime = DateUtil.parse((DateUtil.year(startTime)+1)+"-01-01","yyyy-MM-dd");
                break;
            case "month":
                endTime = DateUtil.offsetMonth(startTime,1);
                break;
            case "week":
                endTime = DateUtil.offsetDay(startTime,7);
                break;
            default:
                break;
        }
        List<FinishSpeedVo> list = projectMapper.getFinishSpeedList(dto.getCompanyOrder(), startTime, endTime);
        if (list.size()==0) { return null; }
        Map<String, List<FinishSpeedVo>> map = list.stream().collect(Collectors.groupingBy(FinishSpeedVo::getType));
        for (int i=1; i<=101; i+=5){
            Map<String, Integer> piece = new HashMap<>(map.keySet().size());
            // 第一步要统计花费天数在0-5的数据
            if (i != 1 && i != 101){
                for (String key : map.keySet()){
                    int finalI = i;
                    List<FinishSpeedVo> l = map.get(key).stream().filter(one -> one.getUseDate()!= null && one.getUseDate() > finalI && one.getUseDate() < (finalI + 5)).collect(Collectors.toList());
                    piece.put(key, l.size());
                }
                returnMap.put(i+"-"+(i+4), piece);
            }else if (i == 1){
                for (String key : map.keySet()){
                    List<FinishSpeedVo> l = map.get(key).stream().filter(one -> one.getUseDate()!= null && one.getUseDate() >= 0 && one.getUseDate() < 6).collect(Collectors.toList());
                    piece.put(key, l.size());
                }
                returnMap.put("0-5", piece);
            }else {
                for (String key : map.keySet()){
                    List<FinishSpeedVo> l = map.get(key).stream().filter(one -> one.getUseDate()!= null && one.getUseDate() > 100).collect(Collectors.toList());
                    piece.put(key, l.size());
                }
                returnMap.put("100+", piece);
            }
        }
        return returnMap;
    }

    @Override
    public List<ProjectSpeedDetailVo> getFinishSpeedDetailList(FinishSpeedDetailDto dto) {
        List<Integer> ignoreProjects = new ArrayList<>();
        ignoreProjects.add(1);
        ignoreProjects.add(98);
        ignoreProjects.add(99);
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.notIn("ap.`status`",ignoreProjects);
        wrapper.like(StringUtils.isNotBlank(dto.getCompanyOrder()),"ap.company_order",dto.getCompanyOrder());
        wrapper.like(StringUtils.isNotBlank(dto.getType()),"ap.type",dto.getType());
        wrapper.like(StringUtils.isNotBlank(dto.getIdentifier()),"ap.identifier",dto.getIdentifier());
        wrapper.like(StringUtils.isNotBlank(dto.getProjectName()),"ap.project_name",dto.getProjectName());
        wrapper.ge(dto.getStartTime()!=null,"apd.sign_date",dto.getStartTime());
        if (dto.getEndTime()!=null){
            Date eTime = DateUtil.offsetMonth(dto.getEndTime(),1);
            wrapper.lt("apd.sign_date",eTime);
        }
        //过滤掉存在的复数以及全是null的数据
        wrapper.and(a -> a
                        .and(b->b.and(c->c.isNull("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apc.plan_commit, tpp.createtime),DATEDIFF(tpp.createtime, apd.task_release_date))").or()
                                .ge("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apc.plan_commit, tpp.createtime),DATEDIFF(tpp.createtime, apd.task_release_date))",0))
                                .and(c->c.isNull("DATEDIFF(apc.sample_record, apc.plan_commit)").or().ge("DATEDIFF(apc.sample_record, apc.plan_commit)",0))
                                .and(c->c.isNull("DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record)").or()
                                        .ge("DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record)",0))
                                .and(c->c.isNull("DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record)").or()
                                        .ge("DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record)",0))
                                .and(c->c.isNull("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apd.report_issue, lrr.audit_date),DATEDIFF(apd.report_issue, tpp.createtime))").or()
                                        .ge("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apd.report_issue, lrr.audit_date),DATEDIFF(apd.report_issue, tpp.createtime))",0)))
                        .and(b->b.isNotNull("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(tpp.createtime, apd.task_release_date),NULL)").or()
                            .isNotNull("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apc.plan_commit, tpp.createtime),DATEDIFF(tpp.createtime, apd.task_release_date))").or()
                            .isNotNull("DATEDIFF(apc.sample_record, apc.plan_commit)").or()
                            .isNotNull("DATEDIFF(IF(apd.received_date < apd.physical_send_date, apd.received_date,apd.physical_send_date),apc.sample_record)").or()
                            .isNotNull("DATEDIFF(lrr.audit_date, IF(apd.deliver_date < apd.physical_send_date,apd.deliver_date,apd.physical_send_date))").or()
                            .isNotNull("IF(ap.type IN ('检评','控评','现状'),DATEDIFF(apd.report_issue, lrr.audit_date),DATEDIFF(apd.report_issue, tpp.createtime))")));
        return projectMapper.getProjectSpeedList(wrapper);
    }

    @Override
    public Map<String, Map<String, Integer>> getFinishSpeedClassOne(FinishSpeedClassDto dto) {
        QueryWrapper<Object> wrapper = getClassWrapper(dto, true);
        List<ProjectSpeedDetailVo> projectSpeedList = projectMapper.getProjectSpeedList(wrapper);
        return FinishSpeedClassHandler(projectSpeedList, dto.getLinkName(), true);
    }

    @Override
    public Map<String, Map<String, Integer>> getFinishSpeedClassTwo(FinishSpeedClassDto dto) {
        QueryWrapper<Object> wrapper = getClassWrapper(dto, false);
        List<ProjectSpeedDetailVo> projectSpeedList = projectMapper.getProjectSpeedList(wrapper);
        return FinishSpeedClassHandler(projectSpeedList, dto.getLinkName(), false);
    }

    private QueryWrapper<Object> getClassWrapper(FinishSpeedClassDto dto,Boolean ifOne){
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        List<Integer> ignoreProjects = new ArrayList<>();
        List<String> oneTypes = new ArrayList<>();
        ignoreProjects.add(1);
        ignoreProjects.add(98);
        ignoreProjects.add(99);
        oneTypes.add("检评");
        oneTypes.add("控评");
        oneTypes.add("现状");
        wrapper.notIn("ap.`status`",ignoreProjects);
        wrapper.eq("ap.company_order",dto.getCompanyOrder());
        Date startTime = dto.getSTime(), endTime = null;
        switch (dto.getTimeLevel()){
            case "year":
                startTime = DateUtil.parse(DateUtil.year(startTime)+"-01-01","yyyy-MM-dd");
                endTime = DateUtil.parse((DateUtil.year(startTime)+1)+"-01-01","yyyy-MM-dd");
                break;
            case "month":
                endTime = DateUtil.offsetMonth(startTime,1);
                break;
            case "week":
                endTime = DateUtil.offsetDay(startTime,7);
                break;
            default:
                break;
        }
        wrapper.ge("apd.sign_date",startTime).lt("apd.sign_date",endTime);
        if (ifOne){
            wrapper.in("ap.type",oneTypes);
        }else {
            wrapper.notIn("ap.type",oneTypes);
        }
        return wrapper;
    }

    private Map<String, Map<String, Integer>> FinishSpeedClassHandler(List<ProjectSpeedDetailVo> list,String linkName, Boolean ifOne){
        Map<String, Map<String, Integer>> returnMap = new LinkedHashMap<>();
        List<ProjectSpeedDetailVo> listByClean = null;
        Map<Integer,List<ProjectSpeedDetailVo>> listByMap = null;
        switch (linkName){
            case "任务安排":
                listByClean = list.stream().filter(a -> a.getTime1() !=null && a.getTime1() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime1)); }
                break;
            case "反应时间":
                listByClean = list.stream().filter(a -> a.getTime2() !=null && a.getTime2() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime2)); }
                break;
            case "方案制定":
                listByClean = list.stream().filter(a -> a.getTime3() !=null && a.getTime3() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime3)); }
                break;
            case "采样完成":
                listByClean = list.stream().filter(a -> a.getTime4() !=null && a.getTime4() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime4)); }
                break;
            case "数据完成":
                listByClean = list.stream().filter(a -> a.getTime5() !=null && a.getTime5() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime5)); }
                break;
            case "报告完成":
                listByClean = list.stream().filter(a -> a.getTime6() !=null && a.getTime6() >=0).collect(Collectors.toList());
                if (listByClean.size()>0){ listByMap = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getTime6)); }
                break;
            default:
                return getEmptyReturnMap(ifOne);
        }
        if (listByMap != null){
            Map<String, List<ProjectSpeedDetailVo>> mapTypes = listByClean.stream().collect(Collectors.groupingBy(ProjectSpeedDetailVo::getType));
            List<Integer> days = new ArrayList<>(listByMap.keySet());
            Collections.sort(days);
            List<String> lines = new ArrayList<>();
            if (ifOne) {
                lines.add("检评");
                lines.add("控评");
                lines.add("现状");
            }else {
                lines = new ArrayList<>(mapTypes.keySet());
            }
            for (Integer day : days){
                Map<String, Integer> map = new HashMap<>(3);
                for (String key : lines){
                    map.put(key, (int) listByMap.get(day).stream().filter(type -> type.getType().equals(key)).count());
                }
                returnMap.put(day.toString(), map);
            }
            return returnMap;
        }else {
            return getEmptyReturnMap(ifOne);
        }
    }

    private Map<String, Map<String, Integer>> getEmptyReturnMap(Boolean ifOne){
        Map<String, Map<String, Integer>> map = new HashMap<>(1);
        Map<String, Integer> piece = new HashMap<>(1);
        if (ifOne){
            piece.put("检评", 0);
        }else {
            piece.put("职卫监督", 0);
        }
        map.put("1",piece);
        return map;
    }
}
