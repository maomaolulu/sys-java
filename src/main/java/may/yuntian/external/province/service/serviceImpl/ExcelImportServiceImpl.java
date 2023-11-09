package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.entity.SubstanceContrastResult;
import may.yuntian.external.province.mapper.SubstanceContrastResultMapper;
import may.yuntian.external.province.service.ExcelImportService;
import may.yuntian.external.province.service.ResultItemService;
import may.yuntian.external.province.vo.ResultExcelVo;
import may.yuntian.external.wanda.service.serviceImpl.TemplateImportServiceImpl;
import may.yuntian.sys.utils.ShiroUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-07-14 13:20
 */
@Slf4j
@Service("excelImportService")
public class ExcelImportServiceImpl implements ExcelImportService {

    @Resource
    private ResultItemService resultItemService;
    @Resource
    private SubstanceContrastResultMapper substanceContrastResultMapper;

    /**
     * excel导入模板下载
     * @param response
     */
    @Override
    public void downloadExcelTemplate(HttpServletResponse response) {

        TemplateImportServiceImpl.responseSetting(response, "省平台-结果项导入模板", ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 读取文件的输入流
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template/province/结果项导入模板.xlsx");
            assert inputStream != null;
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }

    }

    /**
     * 导入检测结果
     * @param file
     * @param projectId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, Long projectId) {
        if (file.isEmpty()) {
            throw new RRException("导入的excel表格不能为空！");
        }
        // 根据物质名称分组
        List<SubstanceContrastResult> substanceContrastResults = substanceContrastResultMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>().isNotNull(SubstanceContrastResult::getSubName));
        Map<String, List<SubstanceContrastResult>> substanceMap = substanceContrastResults.stream().collect(Collectors.groupingBy(SubstanceContrastResult::getSubName));
        InputStream fileInputStream = null;
        try {
            List<ResultItem> resultItems = new ArrayList<>();
            fileInputStream = file.getInputStream();
            // 通过easyExcel读取数据
            EasyExcelFactory.read(fileInputStream, ResultExcelVo.class, new ReadListener<ResultExcelVo>() {
                // 单次缓存的数据量
                public static final int BATCH_COUNT = 500;
                // 临时存储
                private List<ResultExcelVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override   // 解析每一行excel数据都会执行的回调方法
                public void invoke(ResultExcelVo data, AnalysisContext context) {
                    cachedDataList.add(data);
                }

                @Override   // 在每个sheet读取完毕后调用一次。所有sheet都会往同一个监听器ReadListener<ResultExcelVo>里面写
                public void doAfterAllAnalysed(AnalysisContext context) {
                    if (CollUtil.isNotEmpty(cachedDataList)) {
                        String sheetName = context.readSheetHolder().getSheetName();
                        // 导入模板时，根据危害因素类型，先删除，再保存
                        if ("化学因素".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 1));
                        } else if ("粉尘".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 2));
                        } else if ("噪声".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 3));
                        } else if ("高温".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 4));
                        } else if ("紫外辐射".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 5));
                        } else if ("工频电场".equals(sheetName)) {
                            resultItemService.remove(new LambdaQueryWrapper<ResultItem>().eq(ResultItem::getProjectId, projectId).eq(ResultItem::getFactorType, 7));
                        } // Todo: 其它危害因素类型待开发...
                        addData(sheetName, cachedDataList, projectId, substanceMap, resultItems);
                        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }
            }).doReadAll();
            resultItemService.saveBatch(resultItems);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                log.error("io流关闭异常：" + e.getMessage());
            }
        }
    }

    /**
     * 往结果list中添加处理过的数据
     * @param sheetName sheet名
     * @param cachedDataList excel临时存储list
     * @param projectId 项目id
     * @param substanceMap 检测物质map
     * @param resultItems 结果list
     */
    public void addData(String sheetName, List<ResultExcelVo> cachedDataList, Long projectId, Map<String, List<SubstanceContrastResult>> substanceMap, List<ResultItem> resultItems) {
        for (ResultExcelVo resultExcelVo : cachedDataList) {
            ResultItem resultItem = new ResultItem();
            resultItem.setProjectId(projectId);
            resultItem.setRemark("0");  // 0：模板导入；1：系统中抓取
            BeanUtils.copyProperties(resultExcelVo, resultItem, "detectionDate");
            resultItem.setDetectionDate(resultExcelVo.getDetectionDate().replace("/", "-"));
            resultItem.setCreateTime(DateUtil.dateSecond());
            resultItem.setCreateBy(ShiroUtils.getUserEntity().getUsername());
            // 化学因素、粉尘、紫外辐射：均带有"检测项目"
            if ("化学因素".equals(sheetName) || "粉尘".equals(sheetName) || "紫外辐射".equals(sheetName)) {
                resultItem.setSubName(resultExcelVo.getSubName());
                boolean otherDustFlag = resultExcelVo.getSubName().contains("其他粉尘");
                if (otherDustFlag) {  // "其他粉尘"跳过物质验证
                    resultItem.setSubId(624L);
                    resultItem.setFactorType(2);
                    resultItem.setItemName("其他粉尘");
                    resultItem.setCheckItemCode("110057");
                    resultItem.setCode("127,104,128,105");
                    resultItem.setResult(resultExcelVo.getCPe() + "," + resultExcelVo.getPcTwaTotal() + "," + resultExcelVo.getCPeCall() + "," + resultExcelVo.getPcTwaCall());
                    resultItem.setUnit("1001,1001,1001,1001");
                } else {  // 除“其他粉尘”外的三类危害因素结果数据处理
                    commonFactorResult(sheetName, substanceMap, resultExcelVo, resultItem);
                }
            }
            // 噪声：8h等效声级、40h等效声级
            if ("噪声".equals(sheetName)) {
                resultItem.setFactorType(3);
                resultItem.setSubName("噪声");
                resultItem.setSubId(645L);
                resultItem.setItemName("稳态噪声");
                resultItem.setCheckItemCode("1300081");
                resultItem.setCode("115,116,117");
                resultItem.setUnit("1016,1016,1016");
                resultItem.setResult(resultExcelVo.getSoundLevel8() + "," + resultExcelVo.getSoundLevel40() + "," + resultExcelVo.getPeakSound());
            }
            // 高温
            if ("高温".equals(sheetName)) {
                resultItem.setFactorType(4);
                resultItem.setSubName("高温");
                resultItem.setSubId(644L);
                resultItem.setItemName("高温");
                resultItem.setCheckItemCode("130007");
                resultItem.setCode("130,131,114,132");
                resultItem.setUnit("1018,1000,1014,1014");
                resultItem.setResult(resultExcelVo.getContactTimeRate() + "," + resultExcelVo.getLaborIntensity() + "," + resultExcelVo.getWbgt() + "," + resultExcelVo.getExposureLimit());
            }
            // 工频电场
            if ("工频电场".equals(sheetName)) {
                resultItem.setFactorType(7);
                resultItem.setSubName("工频电场");
                resultItem.setSubId(632L);
                resultItem.setItemName("工频电场");
                resultItem.setCheckItemCode("130003");
                resultItem.setCode("106");
                resultItem.setUnit("1010");
                resultItem.setResult(resultExcelVo.getElectricIntensity());
            }

            // Todo: 其它危害因素类型及模板待开发......

            resultItems.add(resultItem);
        }
    }

    /**
     * 化学、粉尘（“其他粉尘”除外）、紫外辐射：结果数据处理
     * @param sheetName sheet名
     * @param substanceMap 检测项目map
     * @param resultExcelVo 读取的一行excel数据
     * @param resultItem 结果对象
     */
    public void commonFactorResult(String sheetName, Map<String, List<SubstanceContrastResult>> substanceMap, ResultExcelVo resultExcelVo, ResultItem resultItem) {
        List<SubstanceContrastResult> contrastResultList = substanceMap.get(resultExcelVo.getSubName());
        if (CollUtil.isEmpty(contrastResultList)) {
            throw new RRException("警告：" + sheetName + "-检测物质名【" + resultExcelVo.getSubName() + "】不合法，请仔细查阅“检测项目字典”并更改其名称后，再进行导入操作！");
        }
        resultItem.setSubId(contrastResultList.get(0).getSubId());
        resultItem.setItemName(contrastResultList.get(0).getItemName());
        resultItem.setCheckItemCode(contrastResultList.get(0).getCheckItemCode());
        if ("粉尘".equals(sheetName)) {
            resultItem.setFactorType(2);
            resultItem.setCode("127,104,128,105");
            resultItem.setResult(resultExcelVo.getCPe() + "," + resultExcelVo.getPcTwaTotal() + "," + resultExcelVo.getCPeCall() + "," + resultExcelVo.getPcTwaCall());
            resultItem.setUnit("1001,1001,1001,1001");
        } else {
            StringBuilder resultBuilder = new StringBuilder();
            StringBuilder codeBuilder = new StringBuilder();
            StringBuilder unitBuilder = new StringBuilder();
            contrastResultList.forEach(substanceContrastResult -> {
                String resultItemCode = substanceContrastResult.getResultItemCode();
                if ("化学因素".equals(sheetName)) {
                    resultItem.setFactorType(1);
                    if ("101".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getMac()).append(",");
                    }
                    if ("102".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getPcTwa()).append(",");
                    }
                    if ("103".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getPcStel()).append(",");
                    }
                    if ("129".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getPeak()).append(",");
                    }
                }
                if ("紫外辐射".equals(sheetName)) {
                    resultItem.setFactorType(5);
                    if ("109".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getIrradiance()).append(",");
                    }
                    if ("108".equals(resultItemCode)) {
                        resultBuilder.append(resultExcelVo.getExposure()).append(",");
                    }
                }
                codeBuilder.append(resultItemCode).append(",");
                unitBuilder.append(substanceContrastResult.getUnit()).append(",");
            });
            resultItem.setResult(resultBuilder.substring(0, resultBuilder.length() - 1));
            resultItem.setCode(codeBuilder.substring(0, codeBuilder.length() - 1));
            resultItem.setUnit(unitBuilder.substring(0, unitBuilder.length() - 1));
        }
    }

    /**
     * 导出检测项目字典；化学、粉尘、噪声、高温、紫外辐射
     * @param response
     */
    @Override
    public void factorDictDownload(HttpServletResponse response) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<SubstanceContrastResult> contrastResults = substanceContrastResultMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>()
                .select(SubstanceContrastResult::getItemName,SubstanceContrastResult::getCheckItemCode,SubstanceContrastResult::getResultItemName,SubstanceContrastResult::getSubName)
                .isNotNull(SubstanceContrastResult::getSubName).orderByAsc(SubstanceContrastResult::getSType).in(SubstanceContrastResult::getSType, 1,2,3,4,5))
                .stream().distinct().collect(Collectors.toList());
        for (SubstanceContrastResult contrastResult : contrastResults) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("省-检测项目编码", contrastResult.getCheckItemCode());
        //    map.put("省-检测项目名称", contrastResult.getItemName());
            map.put("省-结果项（必填）", contrastResult.getResultItemName());
            map.put("安联-检测物质名称", contrastResult.getSubName());
            listMap.add(map);
        }
        FileUtils.downloadExcel(listMap, response);
    }
}
