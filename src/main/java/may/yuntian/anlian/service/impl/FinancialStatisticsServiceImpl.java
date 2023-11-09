package may.yuntian.anlian.service.impl;

import may.yuntian.anlian.dto.YearProjectStatisticsDto;
import may.yuntian.anlian.entity.AccountEntity;
import may.yuntian.anlian.entity.ProjectStatistics;
import may.yuntian.anlian.mapper.FinancialStatisticsMapper;
import may.yuntian.anlian.service.FinancialStatisticsService;
import may.yuntian.modules.sys_v2.utils.DateUtils;
import may.yuntian.modules.sys_v2.utils.RedisCache;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static may.yuntian.modules.sys_v2.utils.DateUtils.YYYY;

/**
 * 财务统计-业务处理层
 *
 * @author hjy
 * @date 2023/5/11 11:52
 */
@Service
public class FinancialStatisticsServiceImpl implements FinancialStatisticsService {

    private final FinancialStatisticsMapper statisticsMapper;

    /**
     * 登录用户信息
     */
    private static final String CAPTCHA_PREFIX_SALESMEN = "financial:statistics:salesmen:";
    /**
     * 缓存前缀-公司
     */
    private static final String CAPTCHA_PREFIX_COMPANY = "financial:statistics:company:";

    @Autowired
    private RedisCache redisTemplate;

    public FinancialStatisticsServiceImpl(FinancialStatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    /**
     * 获取财务统计数据-公司维度
     *
     * @param company        项目隶属公司
     * @param businessSource 业务隶属公司
     * @param startTime      统计起始时间
     * @param endTime        统计结束时间
     * @return 财务统计信息
     */
    @Override
    public Map<String, Object> selectCompanyFinancialStatisticsInfo(String company, String businessSource, String startTime, String endTime) {
        //key
        String cacheKey = CAPTCHA_PREFIX_COMPANY + StringUtils.nvl(company, "all") + "||" + StringUtils.nvl(businessSource, "all") + "||" + startTime;
        Map<String, Object> cacheMap = redisTemplate.getCacheMap(cacheKey);
        if (StringUtils.isNotEmpty(cacheMap)) {
            return cacheMap;
        } else {
            Map<String, Object> resultMap = financialStatistics(false, null, company, businessSource, startTime, endTime);
            redisTemplate.setCacheMap(cacheKey, resultMap);
            redisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
            return resultMap;
        }
    }

    /**
     * 获取财务统计数据-业务员维度
     *
     * @param salesmenId 业务员id
     * @param company    项目隶属公司
     * @param startTime  统计起始时间
     * @param endTime    统计结束时间
     * @return 财务统计信息
     */
    @Override
    public Map<String, Object> selectSalesmenFinancialStatisticsInfo(Integer salesmenId, String company, String startTime, String endTime) {
        //业务员key
        String cacheKey = CAPTCHA_PREFIX_SALESMEN + StringUtils.nvl(salesmenId, "all") + "-" + StringUtils.nvl(company, "all") + "||" + startTime;
        Map<String, Object> cacheMap = redisTemplate.getCacheMap(cacheKey);
        if (StringUtils.isNotEmpty(cacheMap)) {
            return cacheMap;
        } else {
            Map<String, Object> resultMap = financialStatistics(true, salesmenId, company, null, startTime, endTime);
            redisTemplate.setCacheMap(cacheKey, resultMap);
            redisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
            return resultMap;
        }
    }

    /**
     * 清空缓存
     *
     * @param type 类型
     */
    @Override
    public void clearCaching(String type) {
        Collection<String> keys;
        if ("company".equals(type)) {
            keys = redisTemplate.keys(CAPTCHA_PREFIX_COMPANY + "*");
        } else {
            keys = redisTemplate.keys(CAPTCHA_PREFIX_SALESMEN + "*");
        }
        redisTemplate.deleteObject(keys);
    }

    /**
     * 获取财务统计数据
     *
     * @param flag           统计标记(true 业务员；false 公司)
     * @param salesmenId     业务员
     * @param company        项目隶属公司
     * @param businessSource 业务隶属公司
     * @param startTime      统计起始时间
     * @param endTime        统计结束时间
     * @return 财务统计信息
     */
    private Map<String, Object> financialStatistics(boolean flag, Integer salesmenId, String company, String businessSource, String startTime, String endTime) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取项目数据-如果跨越年份较大  则查询时间略长（默认数据初始年份2020-01-01）
        List<ProjectStatistics> list = statisticsMapper.selectProjectInfo(salesmenId, company, businessSource, startTime, endTime);
        //组装数据
        if (StringUtils.isNotEmpty(list)) {
            //返回数据
            List<Map<String, Object>> resultList = new ArrayList<>();
            //临时载体数据-用于所有数据整合统计
            List<Map<String, YearProjectStatisticsDto>> tempList = new ArrayList<>();
            //先按照项目隶属分组，然后在统计业务员数据（原因：一个业务员如果公司发生变动，比如人员调用，按照要求分开进行统计）
            //项目隶属分组
            Map<String, List<ProjectStatistics>> orderMap = list.stream().collect(Collectors.groupingBy(ProjectStatistics::getCompanyOrder));
            if (flag) {
                orderMap.forEach((key, orderList) -> {
                    //业务员分组
                    Map<String, List<ProjectStatistics>> salesmenMap = orderList.stream().collect(Collectors.groupingBy(ProjectStatistics::getSalesmen));
                    statisticsHandle(salesmenMap, resultList, tempList, true, key);
                });
            } else {
                //统一数据处理
                orderMap.forEach((key, orderList) -> {
                    Map<String, List<ProjectStatistics>> businessMap = orderList.stream().collect(Collectors.groupingBy(ProjectStatistics::getBusinessSource));
                    statisticsHandle(businessMap, resultList, tempList, false, key);
                });
//                statisticsHandle(orderMap, resultList, tempList, false, null);
            }
            //所有数据合计统计
            YearProjectStatisticsDto allYears = allYearsStatistics(tempList);
            resultMap.put("allStatistics", allYears);
            resultMap.put("list", resultList);
            return resultMap;
        }
        //返回空数据
        resultMap.put("allStatistics", YearProjectStatisticsDto.builder().build());
        resultMap.put("list", list);
        return resultMap;
    }

    /**
     * 财务数据统一处理
     *
     * @param sourceMap  数据源
     * @param resultList 返回列表数据
     * @param tempList   临时数据
     * @param flag       统计标记(true 业务员；false 公司)
     * @param parentKey  父节点key
     */
    private void statisticsHandle(Map<String, List<ProjectStatistics>> sourceMap, List<Map<String, Object>> resultList, List<Map<String, YearProjectStatisticsDto>> tempList, boolean flag, String parentKey) {
        //当前年（统计当前年数据 当年开票金额、当年回款金额 ）
        String nowYear = DateUtils.dateTimeNow(YYYY);
        sourceMap.forEach((key, salesmenList) -> {
            //存放所有统计信息
            Map<String, Object> map = new HashMap<>();
            if (flag) {
                //业务员
                map.put("salesmen", key);
                map.put("company", parentKey);
                map.put("salesmenCompanyOrder", salesmenList.get(0).getSalesmenCompanyOrder());
            } else {
                //隶属公司
                map.put("company", parentKey);
                map.put("businessSource", key);
            }
            //年统计信息
            Map<String, YearProjectStatisticsDto> yearStatisticsMap = new HashMap<>();
            //日历
            Calendar instance = Calendar.getInstance();
            for (ProjectStatistics info : salesmenList) {
                //获取年份
                instance.setTime(info.getSignDate());
                String year = String.valueOf(instance.get(Calendar.YEAR));
                //年度项目数据统计
                YearProjectStatisticsDto yearDto = yearStatisticsMap.get(year);
                if (StringUtils.isNull(yearDto)) {
                    yearDto = YearProjectStatisticsDto.builder().build();
                    yearStatisticsMap.put(year, yearDto);
                }
                //本项目金额
                BigDecimal projectMoney = StringUtils.nvl(info.getTotalMoney(), BigDecimal.ZERO);
                //已开票金额
                BigDecimal invoiceMoney = StringUtils.nvl(info.getInvoiceMoney(), BigDecimal.ZERO);
                //已收款金额
                BigDecimal receiptMoney = StringUtils.nvl(info.getReceiptMoney(), BigDecimal.ZERO);
                //未结算金额(应崔收款)
                BigDecimal unsettledAmount = StringUtils.nvl(info.getUnsettledAmount(), BigDecimal.ZERO);
                //未开票金额（项目金额-开票金额）  废除（2023-07-03）
                BigDecimal amountUnissuedInvoice = projectMoney.subtract(invoiceMoney);
                //应开票金额  条件1：未结算金额(应崔收款) >0   ;条件2：应崔收款-应收项目已开票 >0
                BigDecimal itemsReceivableInvoicedMoney = StringUtils.nvl(info.getShouldInvoiceMoney(), BigDecimal.ZERO);
                BigDecimal shouldInvoiceMoney = BigDecimal.ZERO;
                //条件1：未结算金额(应崔收款) >0
                if (unsettledAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal subtract = unsettledAmount.subtract(itemsReceivableInvoicedMoney);
                    //条件2：应崔收款-应收项目已开票 >0
                    if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                        shouldInvoiceMoney = subtract;
                    }
                }
                //已开票未回款（应收账款） -（开票金额-已收金额 >0 的时候计算）
                BigDecimal accountsReceivable = invoiceMoney.subtract(receiptMoney);
                //只累加大于0的金额
                if (accountsReceivable.compareTo(BigDecimal.ZERO) < 0) {
                    accountsReceivable = BigDecimal.ZERO;
                }
                //项目收付款相关（只计算当前日历年的数据）
                //当年开票金额
                BigDecimal amountInvoiceYear = BigDecimal.ZERO;
                //当年回款金额
                BigDecimal amountMoneyRefundedYear = BigDecimal.ZERO;
                List<AccountEntity> accountList = info.getAccountList();
                if (StringUtils.isNotEmpty(accountList)) {
                    for (AccountEntity account : accountList) {
                        //收付款时间
                        Date happenTime = account.getHappenTime();
                        //若时间不存在，跳过（数据存在异常，防止报错）
                        if (StringUtils.isNull(happenTime)) {
                            continue;
                        }
                        instance.setTime(happenTime);
                        //如果在当前年则统计
                        if (nowYear.equals(String.valueOf(instance.get(Calendar.YEAR)))) {
                            //1项目款、2发票
                            Integer acType = account.getAcType();
                            if (acType == 1) {
                                amountMoneyRefundedYear = amountMoneyRefundedYear.add(new BigDecimal(account.getAmount()));
                            } else if (acType == 2) {
                                amountInvoiceYear = amountInvoiceYear.add(new BigDecimal(account.getInvoiceAmount()));
                            }
                        }
                    }
                }
                //账龄统计： 开票金额 - 已收金额 > 0 的时候统计
                BigDecimal ageAmount = invoiceMoney.subtract(receiptMoney);
                if (ageAmount.compareTo(BigDecimal.ZERO) > 0) {
                    if (flag && StringUtils.isNotEmpty(accountList)) {
                        //账龄金额分类
                        amountHandle(ageAmount, accountList, yearDto);
                    }
                }
                //签订项目总额
                yearDto.setTotalAmountProject(projectMoney.add(yearDto.getTotalAmountProject()));
                //已开票总额
                yearDto.setTotalInvoiceAmount(invoiceMoney.add(yearDto.getTotalInvoiceAmount()));
                //已回款总额
                yearDto.setTotalAmountRefunded(receiptMoney.add(yearDto.getTotalAmountRefunded()));
                //应崔收款总额
                yearDto.setTotalAmountAccountsReceivable(unsettledAmount.add(yearDto.getTotalAmountAccountsReceivable()));
                //应开票金额
                yearDto.setTotalShouldInvoiceMoney(shouldInvoiceMoney.add(yearDto.getTotalShouldInvoiceMoney()));
                //未开票总额
                yearDto.setTotalAmountUnissuedInvoice(amountUnissuedInvoice.add(yearDto.getTotalAmountUnissuedInvoice()));
                //已开票未回款（应收账款）
                yearDto.setTotalAccountsReceivable(accountsReceivable.add(yearDto.getTotalAccountsReceivable()));
                //当年开票金额
                yearDto.setTotalAmountInvoiceYear(amountInvoiceYear.add(yearDto.getTotalAmountInvoiceYear()));
                //当年回款金额
                yearDto.setTotalAmountMoneyRefundedYear(amountMoneyRefundedYear.add(yearDto.getTotalAmountMoneyRefundedYear()));
                //项目回款率、应收账款回款率
                paybackRateCalculate(yearDto);
            }
            //个体（人或公司）数据统计
            YearProjectStatisticsDto unitYears = unitYearsStatistics(yearStatisticsMap);
            map.put("unitYears", unitYears);
            //年统计数据
            map.putAll(yearStatisticsMap);
            tempList.add(yearStatisticsMap);
            resultList.add(map);
        });
    }

    /**
     * 所有数据合计统计
     *
     * @param resultList 数据集合
     * @return 数据体
     */
    private YearProjectStatisticsDto allYearsStatistics(List<Map<String, YearProjectStatisticsDto>> resultList) {
        YearProjectStatisticsDto build = YearProjectStatisticsDto.builder().build();
        for (Map<String, YearProjectStatisticsDto> map : resultList) {
            yearProjectStatisticsAdd(build, unitYearsStatistics(map));
        }
        //项目回款率、应收账款回款率
        paybackRateCalculate(build);
        return build;
    }

    /**
     * 个人所有年份数据合计统计
     *
     * @param yearStatisticsMap 待合计数据
     * @return 数据体
     */
    private YearProjectStatisticsDto unitYearsStatistics(Map<String, YearProjectStatisticsDto> yearStatisticsMap) {
        YearProjectStatisticsDto build = YearProjectStatisticsDto.builder().build();
        //遍历并统计数据
        yearStatisticsMap.forEach((key, yearStatistics) -> {
            //数据合计
            yearProjectStatisticsAdd(build, yearStatistics);
        });
        //项目回款率、应收账款回款率
        paybackRateCalculate(build);
        return build;
    }

    /**
     * 回款率数据
     *
     * @param yearStatistics 待处理数据
     */
    private void paybackRateCalculate(YearProjectStatisticsDto yearStatistics) {
        //项目回款率 = 已回款总额 / 签订项目总额
        BigDecimal totalAmountProject = yearStatistics.getTotalAmountProject();
        BigDecimal totalAmountRefunded = yearStatistics.getTotalAmountRefunded();
        if (totalAmountProject.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = totalAmountRefunded.divide(totalAmountProject, 4, BigDecimal.ROUND_HALF_UP);
            NumberFormat percent = NumberFormat.getPercentInstance();
            percent.setMaximumFractionDigits(2);
            yearStatistics.setPaybackRate(percent.format(rate.doubleValue()));
        }
        //应收账款回款率 = 已回款总额 / 开票总额
        BigDecimal totalInvoiceAmount = yearStatistics.getTotalInvoiceAmount();
        if (totalInvoiceAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = totalAmountRefunded.divide(totalInvoiceAmount, 4, BigDecimal.ROUND_HALF_UP);
            NumberFormat percent = NumberFormat.getPercentInstance();
            percent.setMaximumFractionDigits(2);
            yearStatistics.setAccountsReceivablePaybackRate(percent.format(rate.doubleValue()));
        }
    }

    /**
     * 账龄金额分类统计
     *
     * @param ageAmount 账龄差额
     * @param list      项目收付款相关
     * @param yearDto   年度项目数据
     */
    private void amountHandle(BigDecimal ageAmount, List<AccountEntity> list, YearProjectStatisticsDto yearDto) {
        //当前日期
        Date nowDate = DateUtils.getNowDate();
        //数据处理-取第一次未抵消的开票日期
        Map<Integer, List<AccountEntity>> collect = list.stream().filter(temp -> StringUtils.isNotNull(temp.getHappenTime())).sorted(Comparator.comparing(AccountEntity::getHappenTime)).collect(Collectors.groupingBy(AccountEntity::getAcType));
        //开票金额数据
        List<AccountEntity> accountList2 = collect.get(2);
        if (StringUtils.isNotEmpty(accountList2)) {
            int day = 0;
            //项目金额数据
            List<AccountEntity> accountList1 = collect.get(1);
            //不存在
            if (StringUtils.isEmpty(accountList1)) {
                //账龄   计算两个日期天数然后范围比较
                day = DateUtils.differentDaysByMillisecond(accountList2.get(0).getHappenTime(), nowDate);
            } else {
                //项目金额数据边界
                int size = accountList1.size();
                for (int i = 0; i < accountList2.size(); i++) {
                    AccountEntity accountTemp2 = accountList2.get(i);
                    //防止角标越界
                    if (i < size) {
                        //开票金额
                        BigDecimal invoiceAmount = new BigDecimal(accountTemp2.getInvoiceAmount());
                        //项目数据
                        AccountEntity accountTemp1 = accountList1.get(i);
                        //项目金额
                        BigDecimal amount = new BigDecimal(accountTemp1.getAmount());
                        if (invoiceAmount.subtract(amount).compareTo(BigDecimal.ZERO) > 0) {
                            day = DateUtils.differentDaysByMillisecond(accountTemp2.getHappenTime(), nowDate);
                            break;
                        }
                    } else {
                        day = DateUtils.differentDaysByMillisecond(accountTemp2.getHappenTime(), nowDate);
                        break;
                    }
                }
            }
            //账分类
            if (day < 30) {
                yearDto.setAccountAge1(ageAmount.add(yearDto.getAccountAge1()));
            } else if (day <= 90) {
                yearDto.setAccountAge2(ageAmount.add(yearDto.getAccountAge2()));
            } else if (day <= 365) {
                yearDto.setAccountAge3(ageAmount.add(yearDto.getAccountAge3()));
            } else if (day <= 1095) {
                yearDto.setAccountAge4(ageAmount.add(yearDto.getAccountAge4()));
            } else {
                yearDto.setAccountAge5(ageAmount.add(yearDto.getAccountAge5()));
            }
        }
    }


    /**
     * 数据合计
     *
     * @param dto1 数据1
     * @param dto2 数据1
     */
    private void yearProjectStatisticsAdd(YearProjectStatisticsDto dto1, YearProjectStatisticsDto dto2) {
        //账龄
        dto1.setAccountAge1(dto1.getAccountAge1().add(dto2.getAccountAge1()));
        dto1.setAccountAge2(dto1.getAccountAge2().add(dto2.getAccountAge2()));
        dto1.setAccountAge3(dto1.getAccountAge3().add(dto2.getAccountAge3()));
        dto1.setAccountAge4(dto1.getAccountAge4().add(dto2.getAccountAge4()));
        dto1.setAccountAge5(dto1.getAccountAge5().add(dto2.getAccountAge5()));
        //项目总额
        dto1.setTotalAmountProject(dto1.getTotalAmountProject().add(dto2.getTotalAmountProject()));
        //已开票总额
        dto1.setTotalInvoiceAmount(dto1.getTotalInvoiceAmount().add(dto2.getTotalInvoiceAmount()));
        //已回款总额
        dto1.setTotalAmountRefunded(dto1.getTotalAmountRefunded().add(dto2.getTotalAmountRefunded()));
        //应崔收款总额
        dto1.setTotalAmountAccountsReceivable(dto1.getTotalAmountAccountsReceivable().add(dto2.getTotalAmountAccountsReceivable()));
        //应开票总额
        dto1.setTotalShouldInvoiceMoney(dto1.getTotalShouldInvoiceMoney().add(dto2.getTotalShouldInvoiceMoney()));
        // 未开票总额
        dto1.setTotalAmountUnissuedInvoice(dto1.getTotalAmountUnissuedInvoice().add(dto2.getTotalAmountUnissuedInvoice()));
        //已开票未回款（应收账款）
        dto1.setTotalAccountsReceivable(dto1.getTotalAccountsReceivable().add(dto2.getTotalAccountsReceivable()));
        //当年开票金额
        dto1.setTotalAmountInvoiceYear(dto1.getTotalAmountInvoiceYear().add(dto2.getTotalAmountInvoiceYear()));
        //当年回款金额
        dto1.setTotalAmountMoneyRefundedYear(dto1.getTotalAmountMoneyRefundedYear().add(dto2.getTotalAmountMoneyRefundedYear()));
    }
}
