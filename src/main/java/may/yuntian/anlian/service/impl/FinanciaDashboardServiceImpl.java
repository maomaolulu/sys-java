package may.yuntian.anlian.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.mapper.FinanciaDashboardMapper;
import may.yuntian.anlian.service.FinanciaDashboardService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.ThisYearReceiptVo;
import may.yuntian.anlian.vo.TotalMoneyVo;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gy
 * @Description 财务数据看板
 * @date 2023-07-12 13:44
 */
@Service
public class FinanciaDashboardServiceImpl implements FinanciaDashboardService {

    @Autowired
    private FinanciaDashboardMapper financiaDashboardMapper;

    @Override
    public Map<String, Map<String, Object>> getTotalMoney(String companyOrder) {
        Map<String, Map<String, Object>> returnMap = new HashMap<>(4);
        int nowYear = DateUtil.year(new Date());
        Date start = DateUtil.parse((nowYear - 1) + "-01-01", "yyyy-MM-dd");
        Date end = DateUtil.parse((nowYear + 1) + "-01-01", "yyyy-MM-dd");
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.ge("apd.sign_date", start);
        wrapper.lt("apd.sign_date", end);
        wrapper.eq(StringUtils.isNotBlank(companyOrder), "ap.company_order", companyOrder);
        wrapper.groupBy("DATE_FORMAT(apd.sign_date,'%Y-%m')");
        List<TotalMoneyVo> result1 = financiaDashboardMapper.getTotalMoney(wrapper);
        returnMap.put("Signed", getReturnMap(result1, nowYear));
        //在wrapper的基础上添加项目状态='中止'条件
        QueryWrapper<Object> wrapper2 = wrapper.eq("ap.`status`", 99);
        List<TotalMoneyVo> result2 = financiaDashboardMapper.getTotalMoney(wrapper2);
        returnMap.put("Stopped", getReturnMap(result2, nowYear));

        QueryWrapper<Object> wrapper3 = new QueryWrapper<>();
        wrapper3.ge("ta.happen_time", start);
        wrapper3.lt("ta.happen_time", end);
        wrapper3.eq("ta.ac_type", 1);
        wrapper3.eq(StringUtils.isNotBlank(companyOrder), "ap.company_order", companyOrder);
        wrapper3.groupBy("DATE_FORMAT(ta.happen_time,'%Y-%m')");
        // 回款
        List<TotalMoneyVo> result3 = financiaDashboardMapper.getReceiptMoney(wrapper3);
        returnMap.put("Receipt", getReturnMap(result3, nowYear));
        // 开票
        QueryWrapper<Object> wrapper4 = new QueryWrapper<>();
        wrapper4.ge("ta.happen_time", start);
        wrapper4.lt("ta.happen_time", end);
        wrapper4.eq("ta.ac_type", 2);
        wrapper4.eq(StringUtils.isNotBlank(companyOrder), "ap.company_order", companyOrder);
        wrapper4.groupBy("DATE_FORMAT(ta.happen_time,'%Y-%m')");
        List<TotalMoneyVo> result4 = financiaDashboardMapper.getInvoiceMoney(wrapper4);
        returnMap.put("Invoice", getReturnMap(result4, nowYear));
        return returnMap;
    }

    @Override
    public void exportExcel(Map<String, Map<String, Object>> map, HttpServletResponse response, String companyOrder) {
        if (StringUtils.isBlank(companyOrder)) {
            companyOrder = "EHS事业部";
        }
        int year = DateUtil.year(new Date());
        int month = DateUtil.month(new Date());
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 预先设置好需要合并的单元格
        writer.merge(0, 0, 0, 20, companyOrder + "同期运营数据对比    （单位：万元）", true);
        // 跳过当前行,否则设置合并会出问题
        writer.passCurrentRow();
        writer.merge(1, 2, 0, 0, null, false);
        for (int i = 0; i < 4; i++) {
            writer.merge(1, 1, i * 5 + 1, i * 5 + 2, null, false);
            writer.merge(3, 3 + month, (i + 1) * 5, (i + 1) * 5, null, false);
            writer.merge(15, 15, i * 5 + 3, (i + 1) * 5, null, false);
            for (int j = 1; j < 4; j++) {
                writer.merge(1, 2, i * 5 + 2 + j, i * 5 + 2 + j, null, false);
            }
            for (int k = 1; k <= 4; k++) {
                writer.merge(3 * k, 3 * k + 2, i * 5 + 4, i * 5 + 4, null, false);
            }
        }
        // 填充标题
        List<List<String>> rows = new ArrayList<>();
        List<String> row1 = new ArrayList<>();
        row1.add("月份");
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    row1.add("签单金额（含税）");
                    break;
                case 1:
                    row1.add("退单金额（含税）");
                    break;
                case 2:
                    row1.add("回款金额（含税）");
                    break;
                case 3:
                    row1.add("开票金额（含税）");
                    break;

                default:
                    break;
            }
            row1.add("");
            row1.add("月同比");
            row1.add("季同比");
            row1.add("同期同比");
        }
        rows.add(row1);
        List<String> row2 = new ArrayList<>();
        row2.add("");
        for (int i = 0; i < 4; i++) {
            row2.add((year - 1) + "年");
            row2.add(year + "年");
            row2.add("");
            row2.add("");
            row2.add("");
        }
        rows.add(row2);
        // 数据行
        for (int i = 1; i <= 12; i++) {
            List<String> rowData = new ArrayList<>();
            rowData.add(i + "月");
            for (int j = 1; j <= 4; j++) {
                String key = i + "";
                if (i < 10) {
                    key = "0" + key;
                }
                Map<String, Object> piece;
                switch (j) {
                    case 1:
                        piece = map.get("Signed");
                        break;
                    case 2:
                        piece = map.get("Stopped");
                        break;
                    case 3:
                        piece = map.get("Receipt");
                        break;
                    default:
                        piece = map.get("Invoice");
                        break;
                }
                Map<String, Object> lastYear = JSONObject.parseObject(JSONObject.toJSONString(piece.get("lastYear")), Map.class);
                Map<String, Object> thisYear = JSONObject.parseObject(JSONObject.toJSONString(piece.get("thisYear")), Map.class);
                Map<String, Object> monthRate = JSONObject.parseObject(JSONObject.toJSONString(piece.get("monthRate")), Map.class);
                Map<String, Object> quarterRate = JSONObject.parseObject(JSONObject.toJSONString(piece.get("quarterRate")), Map.class);
                String yearRate = piece.get("yearRate").toString();
                addMonthData(rowData, key, lastYear);
                addMonthData(rowData, key, thisYear);
                addMonthRateData(rowData, key, monthRate);
                addQuarterData(rowData, i, quarterRate);
                if (i == 1) {
                    BigDecimal rate = new BigDecimal(yearRate);
                    BigDecimal hungered = new BigDecimal(100);
                    rowData.add(rate.multiply(hungered).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                } else {
                    rowData.add("");
                }
            }
            rows.add(rowData);
        }
        // 总计
        List<String> row3 = new ArrayList<>();
        row3.add("总计");
        for (int a = 1; a <= 4; a++) {
            Map<String, Object> crazy;
            switch (a) {
                case 1:
                    crazy = map.get("Signed");
                    break;
                case 2:
                    crazy = map.get("Stopped");
                    break;
                case 3:
                    crazy = map.get("Receipt");
                    break;
                default:
                    crazy = map.get("Invoice");
                    break;
            }
            String lastYearTotal = crazy.get("lastYearTotal").toString();
            String thisYearTotal = crazy.get("thisYearTotal").toString();
            row3.add(lastYearTotal);
            row3.add(thisYearTotal);
            row3.add("");
            row3.add("");
            row3.add("");
        }
        rows.add(row3);

        // 设置标题样式
        StyleSet style = writer.getStyleSet();
        CellStyle headCellStyle = style.getHeadCellStyle();
        //水平居中
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置内容字体
        Font font = writer.createFont();
        //加粗
        font.setBold(true);
        //设置标题字体大小
        font.setFontHeightInPoints((short) 20);
        headCellStyle.setFont(font);
        //设置每列宽度，-1为全部列
        writer.setColumnWidth(-1, 10);
        //设置行的默认高度
        writer.setRowHeight(0, 50);
        writer.setDefaultRowHeight(30);
        writer.write(rows, true);

        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        String fileName = companyOrder + "同期运营数据对比";
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = null;
        try {
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //设置Http响应头告诉浏览器下载这个附件
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
            response.setHeader("Content-Transfer-Encoding", "binary");
            out = response.getOutputStream();
            System.out.println(out.toString());
            writer.flush(out, true);
            System.out.println("导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    @Override
    public Map<String, Object> getThisYearReceipt(String companyOrder) {
        Map<String, Object> returnMap = new HashMap<>(4);
        Date today = DateUtil.beginOfDay(new Date());
        Date thisYearFirst = DateUtil.beginOfYear(today),
                thisMonthFirst = DateUtil.beginOfMonth(today),
                thisWeekFirst = DateUtil.beginOfWeek(today),
                nextYearFirst = DateUtil.offset(thisYearFirst, DateField.YEAR, 1);
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.eq("ac_type", 1);
        wrapper.ge("ta.happen_time", thisYearFirst);
        wrapper.eq(StringUtils.isNotBlank(companyOrder), "ap.company_order", companyOrder);
        List<ThisYearReceiptVo> thisYearReceipt = financiaDashboardMapper.getThisYearReceipt(wrapper);
        returnMap.put("today", getReceiptMap(today, today, thisYearReceipt,false));
        returnMap.put("thisWeek", getReceiptMap(thisWeekFirst, today, thisYearReceipt,false));
        returnMap.put("thisMonth", getReceiptMap(thisMonthFirst, today, thisYearReceipt,false));
        returnMap.put("thisYear", getReceiptMap(thisYearFirst, nextYearFirst, thisYearReceipt,false));
        if (StringUtils.isBlank(companyOrder)){
            returnMap.put("todayCom", getReceiptMap(today, today, thisYearReceipt,true));
            returnMap.put("thisWeekCom", getReceiptMap(thisWeekFirst, today, thisYearReceipt,true));
            returnMap.put("thisMonthCom", getReceiptMap(thisMonthFirst, today, thisYearReceipt,true));
            returnMap.put("thisYearCom", getReceiptMap(thisYearFirst, nextYearFirst, thisYearReceipt,true));
        }
        return returnMap;
    }

    private void addMonthData(List<String> rowData, String key, Map<String, Object> dataMap) {
        if (dataMap.containsKey(key)) {
            rowData.add(dataMap.get(key).toString());
        } else {
            rowData.add("");
        }
    }

    private void addMonthRateData(List<String> rowData, String key, Map<String, Object> dataMap) {
        if (dataMap.containsKey(key)) {
            rowData.add(objectToRateString(dataMap.get(key)));
        } else {
            rowData.add("");
        }
    }

    private String objectToRateString(Object o) {
        BigDecimal number = (BigDecimal) o;
        BigDecimal hungerd = new BigDecimal(100);
        return number.multiply(hungerd).setScale(2, RoundingMode.HALF_UP) + "%";
    }

    private void addQuarterData(List<String> rowData, int month, Map<String, Object> quarterRate) {
        String quarter = null;
        switch (month) {
            case 1:
                quarter = "01";
                break;
            case 4:
                quarter = "02";
                break;
            case 7:
                quarter = "03";
                break;
            case 10:
                quarter = "04";
                break;
            default:
                break;
        }
        if (quarter != null) {
            if (quarterRate.containsKey(quarter)) {
                rowData.add(objectToRateString(quarterRate.get(quarter)));
            } else {
                rowData.add("");
            }
        } else {
            rowData.add("");
        }
    }

    private Map<String, Object> getReturnMap(List<TotalMoneyVo> result, int nowYear) {
        Map<String, Object> returnMap = new HashMap<>(7);
        Map<String, Double> lastYear = result.stream().filter(a -> a.getSignDate().contains((nowYear - 1) + "")).peek(b -> b.setSignDate(b.getSignDate().substring(5))).collect(Collectors.toMap(TotalMoneyVo::getSignDate, TotalMoneyVo::getTotalMoney));
        Map<String, Double> thisYear = result.stream().filter(a -> a.getSignDate().contains(nowYear + "")).peek(b -> b.setSignDate(b.getSignDate().substring(5))).collect(Collectors.toMap(TotalMoneyVo::getSignDate, TotalMoneyVo::getTotalMoney));
        Map<String, Double> lastYearMap = new LinkedHashMap<>();
        Map<String, Double> thisYearMap = new LinkedHashMap<>();
        Map<String, Double> monthRate = new LinkedHashMap<>();
        Map<String, Double> quarterRate = new LinkedHashMap<>();
        BigDecimal lastYearRelateTotal = BigDecimal.ZERO, thisYearRelateTotal = BigDecimal.ZERO, lastYearTotal = BigDecimal.ZERO, thisYearTotal = BigDecimal.ZERO;
        for (int i = 1; i <= 4; i++) {
            int month = 3 * i - 2;
            BigDecimal lastQuarter = BigDecimal.ZERO, thisQuarter = BigDecimal.ZERO;
            boolean ifCalculateQuarter = false;
            for (int j = month; j < month + 3; j++) {
                String key = j + "";
                if (j < 10) {
                    key = "0" + key;
                }

                if (lastYear.containsKey(key)) {
                    BigDecimal lastMonth = BigDecimal.valueOf(lastYear.get(key));
                    lastYearTotal = lastYearTotal.add(lastMonth);
                    lastQuarter = lastQuarter.add(lastMonth);
                    // 避免出现没有数据的情况
                    lastYearMap.put(key, lastMonth.setScale(2, RoundingMode.HALF_UP).doubleValue());
                    if ((DateUtil.month(new Date()) + 1) >= j) {
                        lastYearRelateTotal = lastYearRelateTotal.add(lastMonth);
                    }
                } else {
                    lastYearMap.put(key, 0.0000);
                }

                if (thisYear.containsKey(key)) {
                    BigDecimal thisMonth = BigDecimal.valueOf(thisYear.get(key));
                    thisYearMap.put(key, thisMonth.setScale(2, RoundingMode.HALF_UP).doubleValue());
                    thisYearTotal = thisYearTotal.add(thisMonth);
                    BigDecimal monthZeroRate = thisMonth.setScale(4, RoundingMode.HALF_UP);
                    if (lastYear.containsKey(key)) {
                        BigDecimal lastMonth = BigDecimal.valueOf(lastYear.get(key));
                        thisQuarter = thisQuarter.add(thisMonth);
                        ifCalculateQuarter = true;
                        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
                            monthRate.put(key, monthZeroRate.doubleValue());
                        } else {
                            monthRate.put(key, thisMonth.subtract(lastMonth).divide(lastMonth, 4, RoundingMode.HALF_UP).doubleValue());
                        }
                    } else {
                        // 处理被除数为0的情况
                        monthRate.put(key, monthZeroRate.doubleValue());
                    }

                    if ((DateUtil.month(new Date()) + 1) >= j) {
                        // 同期同比只计算到当月 不管去年是否有值都会统计到今年本月的值
                        thisYearRelateTotal = thisYearRelateTotal.add(thisMonth);
                    }
                } else {
                    // 处理被除数为0的情况
                    if ((DateUtil.month(new Date()) + 1) >= j) {
                        thisYearMap.put(key, 0.0000);
                        monthRate.put(key, 0.0000);
                    }
                }
            }
            //计算季同比
            BigDecimal quarterZeroRate = thisQuarter.setScale(4, RoundingMode.HALF_UP);
            if (ifCalculateQuarter) {
                if (lastQuarter.compareTo(BigDecimal.ZERO) == 0) {
                    quarterRate.put("0" + i, quarterZeroRate.doubleValue());
                } else {
                    quarterRate.put("0" + i, thisQuarter.subtract(lastQuarter).divide(lastQuarter, 4, RoundingMode.HALF_UP).doubleValue());
                }
            } else if (month <= DateUtil.month(new Date()) + 1) {
                quarterRate.put("0" + i, quarterZeroRate.doubleValue());
            }
        }
        if (lastYearRelateTotal.compareTo(BigDecimal.ZERO) == 0) {
            returnMap.put("yearRate", thisYearRelateTotal.setScale(4,RoundingMode.HALF_UP).doubleValue());
        } else {
            returnMap.put("yearRate", thisYearRelateTotal.subtract(lastYearRelateTotal).divide(lastYearRelateTotal, 4, RoundingMode.HALF_UP).toString());
        }
        returnMap.put("lastYear", lastYearMap);
        returnMap.put("thisYear", thisYearMap);
        returnMap.put("monthRate", monthRate);
        returnMap.put("quarterRate", quarterRate);
        returnMap.put("lastYearTotal", lastYearTotal.setScale(2, RoundingMode.HALF_UP).doubleValue());
        returnMap.put("thisYearTotal", thisYearTotal.setScale(2, RoundingMode.HALF_UP).doubleValue());
        return returnMap;
    }

    private Map<String, Object> getReceiptMap(Date startDay, Date endDay, List<ThisYearReceiptVo> thisYearReceipt, boolean ifAllCompany) {
        Map<String, Object> returnMap = new HashMap<>(7);
        List<ThisYearReceiptVo> filterList;
        if (startDay.equals(endDay)) {
            filterList = thisYearReceipt.stream().filter(thisYearReceiptVo -> thisYearReceiptVo.getHappenTime().getTime() == startDay.getTime()).collect(Collectors.toList());
        } else {
            filterList = thisYearReceipt.stream().filter(thisYearReceiptVo -> thisYearReceiptVo.getHappenTime().getTime() >= startDay.getTime() && thisYearReceiptVo.getHappenTime().getTime() <= endDay.getTime()).collect(Collectors.toList());
        }
        List<String> businessKey = Arrays.asList("hz", "jt", "jx", "nb", "sh", "yd", "wk", "zk");
        List<String> businessValue = Arrays.asList("杭州安联", "集团发展", "嘉兴安联", "宁波安联", "上海量远", "杭州亿达", "杭州卫康", "金华职康");
        if (!ifAllCompany){
            for (int i = 0; i < businessValue.size(); i++) {
                int finalI = i;
                List<ThisYearReceiptVo> collect = filterList.stream()
                        .filter(vo -> vo.getBusinessSource() != null && vo.getBusinessSource().equals(businessValue.get(finalI))).collect(Collectors.toList());
                fillDataMap(returnMap, filterList, businessKey, i, collect);
            }
        }else {
            for (int i = 0; i < businessValue.size(); i++) {
                int finalI = i;
                List<ThisYearReceiptVo> collect = filterList.stream()
                        .filter(vo -> vo.getCompanyOrder() != null && vo.getCompanyOrder().equals(businessValue.get(finalI))).collect(Collectors.toList());
                fillDataMap(returnMap, filterList, businessKey, i, collect);
            }
        }

        return returnMap;
    }

    private void fillDataMap(Map<String, Object> returnMap, List<ThisYearReceiptVo> filterList, List<String> businessKey, int i, List<ThisYearReceiptVo> collect) {
        if (collect.size() > 0) {
            returnMap.put(businessKey.get(i), collect.stream().map(ThisYearReceiptVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        } else {
            returnMap.put(businessKey.get(i), "-");
        }
        returnMap.put("total", filterList.stream().map(ThisYearReceiptVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
    }

}
