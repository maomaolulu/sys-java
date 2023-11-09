package may.yuntian.external.wanda.service.serviceImpl;

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
import may.yuntian.external.wanda.entity.ChemicalDustFactor;
import may.yuntian.external.wanda.entity.FactorDictionary;
import may.yuntian.external.wanda.entity.PhysicalHotspotFactor;
import may.yuntian.external.wanda.mapper.FactorDictionaryMapper;
import may.yuntian.external.wanda.service.ChemicalDustFactorService;
import may.yuntian.external.wanda.service.PhysicalHotspotFactorService;
import may.yuntian.external.wanda.service.PhysicalNonHotspotFactorService;
import may.yuntian.external.wanda.service.TemplateImportService;
import may.yuntian.external.wanda.vo.ResultData;
import may.yuntian.sys.utils.ShiroUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-07-07 15:23
 */
@Slf4j
@Service("templateImportService")
public class TemplateImportServiceImpl implements TemplateImportService {

    @Autowired
    private ChemicalDustFactorService chemicalDustFactorService;
    @Autowired
    private PhysicalHotspotFactorService hotspotFactorService;
    @Autowired
    private PhysicalNonHotspotFactorService nonHotspotFactorService;
    @Autowired
    private FactorDictionaryMapper factorDictionaryMapper;

    /** 物理热点因素名称列表 **/
    private static final List<String> PHYSICAL_HOT_FACTOR_NAME_LIST = Arrays.asList("噪声", "高温", "工频电场", "紫外辐射");

    /**
     * excel导入模板下载
     * @param response
     */
    @Override
    public void downloadExcelTemplate(HttpServletResponse response) {

        responseSetting(response, "wanda-结果项导入模板", ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 读取文件的输入流
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template/wanda/结果项导入模板.xlsx");
            assert inputStream != null;
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }

    }

    /**
     * 响应流设置
     * @param response
     * @param fileName
     * @param suffix
     * @param contentType
     */
    public static void responseSetting(HttpServletResponse response, String fileName, String suffix, String contentType) {
        String newFileName = null;
        try {   // 这里URLEncoder.encode可以防止中文乱码
            newFileName = URLEncoder.encode(fileName, "UTF-8").replace("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
        }
        // 当客户端请求的资源是一个可下载的资源（这里的“可下载”是指浏览器会弹出下载框或者下载界面）时，对这个可下载资源的描述（例如下载框中的文件名称）就是来源于该头域。
        response.setHeader("Content-disposition", "attachment;filename=" + newFileName + suffix + ";");
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        // 关闭缓存（HTTP/1.1）
        response.setHeader("Cache-Control", "no-store");
        // 关闭缓存（HTTP/1.0）
        response.setHeader("Pragma", "no-cache");
        // 缓存有效时间
        response.setDateHeader("Expires", 0);
    }

    /**
     * 导入检测结果
     * @param file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, Long projectId) {
        if (file.isEmpty()) {
            throw new RRException("结果导入的excel不能为空！");
        }
        // 危害因素id映射
        List<FactorDictionary> factorDictionaries = factorDictionaryMapper.selectList(new LambdaQueryWrapper<FactorDictionary>().isNotNull(FactorDictionary::getSubName));
        Map<String, List<FactorDictionary>> map = factorDictionaries.stream().collect(Collectors.groupingBy(FactorDictionary::getSubName));
        // 多sheet页导入读取
        InputStream fileInputStream = null;
        try {
            fileInputStream = file.getInputStream();
            EasyExcelFactory.read(fileInputStream, ResultData.class, new ReadListener<ResultData>() {
                // 单次缓存的数据量
                public static final int BATCH_COUNT = 500;
                // 临时存储
                private List<ResultData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override   // 解析每一行excel数据都会执行的回调方法
                public void invoke(ResultData data, AnalysisContext context) {
                    cachedDataList.add(data);
                }

                @Override   // 会在每个sheet读取完毕后调用一次。所有sheet都会往同一个监听器ReadListener<DemoData>里面写
                public void doAfterAllAnalysed(AnalysisContext context) {
                    if (CollUtil.isNotEmpty(cachedDataList)) {
                        String sheetName = context.readSheetHolder().getSheetName();
                        // 导入模板时：根据sheetName先删除，再保存
                        if ("化学因素".equals(sheetName)) {
                            chemicalDustFactorService.remove(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId).eq(ChemicalDustFactor::getFactorType, 1));
                        } else if ("粉尘".equals(sheetName)) {
                            chemicalDustFactorService.remove(new LambdaQueryWrapper<ChemicalDustFactor>().eq(ChemicalDustFactor::getProjectId, projectId).eq(ChemicalDustFactor::getFactorType, 2));
                        } else if ("噪声".equals(sheetName)) {
                            hotspotFactorService.remove(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, 6));
                        } else if ("高温".equals(sheetName)) {
                            hotspotFactorService.remove(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, 7));
                        } else if ("紫外辐射".equals(sheetName)) {
                            hotspotFactorService.remove(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, 8));
                        } else if ("工频电场".equals(sheetName)) {
                            hotspotFactorService.remove(new LambdaQueryWrapper<PhysicalHotspotFactor>().eq(PhysicalHotspotFactor::getProjectId, projectId).eq(PhysicalHotspotFactor::getPhysicalFactorType, 9));
                        } // Todo: 其它危害因素类型待开发...
                        saveData(sheetName);
                        // 存储完成清理list
                        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }

                // 处理并保存数据
                private void saveData(String sheetName) {
                    log.info("{}条" + sheetName + "数据，开始存储数据库！", cachedDataList.size());
                    // 化学等危害因素类型，需要factorId、factorName
                    if ("化学因素".equals(sheetName) || "粉尘".equals(sheetName)) {
                        List<ChemicalDustFactor> chemicalDustFactorList = new ArrayList<>();
                        for (ResultData demoData : cachedDataList) {
                            ChemicalDustFactor chemicalDustFactor = new ChemicalDustFactor();
                            BeanUtils.copyProperties(demoData, chemicalDustFactor);
                            chemicalDustFactor.setProjectId(projectId);
                            List<FactorDictionary> dictionaryList = map.get(demoData.getFactorName().replace(" ", "")); // 去除业务从报告中复制粘贴产生的space
                            if (CollUtil.isEmpty(dictionaryList)) {
                                throw new RRException("警告：" + sheetName + "-检测物质名【" + demoData.getFactorName().replace(" ", "") + "】不合法，请仔细查阅“危害因素字典”并更改其名称后，再进行导入操作！");
                            }
                            chemicalDustFactor.setFactorId(dictionaryList.get(0).getFactorId());
                            chemicalDustFactor.setFactorType("化学因素".equals(sheetName) ? 1 : 2);
                            chemicalDustFactor.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                            chemicalDustFactor.setUpdateTime(DateUtil.dateSecond());
                            chemicalDustFactorList.add(chemicalDustFactor);
                        }
                        chemicalDustFactorService.saveBatch(chemicalDustFactorList);
                    }

                    if (PHYSICAL_HOT_FACTOR_NAME_LIST.contains(sheetName)) {
                        Integer factorType = null;
                        List<PhysicalHotspotFactor> physicalHotspotFactors = new ArrayList<>();
                        for (ResultData demoData : cachedDataList) {
                            PhysicalHotspotFactor hotspotFactor = new PhysicalHotspotFactor();
                            BeanUtils.copyProperties(demoData, hotspotFactor);
                            hotspotFactor.setProjectId(projectId);
                            if ("噪声".equals(sheetName)) {
                                factorType = 6;
                            }
                            if ("高温".equals(sheetName)) {
                                factorType = 7;
                                hotspotFactor.setCheckValue(demoData.getCheckValueT());
                                hotspotFactor.setTouchTime(demoData.getTouchTime().replace("%", ""));
                            }
                            if ("紫外辐射".equals(sheetName)) {
                                factorType = 8;
                                hotspotFactor.setTouchTime(demoData.getTouchTimeU());
                            }
                            if ("工频电场".equals(sheetName)) {
                                factorType = 9;
                                hotspotFactor.setTouchTime(demoData.getTouchTimeP());
                            }
                            hotspotFactor.setPhysicalFactorType(factorType);
                            hotspotFactor.setUpdateBy(ShiroUtils.getUserEntity().getUsername());
                            hotspotFactor.setUpdateTime(DateUtil.dateSecond());
                            physicalHotspotFactors.add(hotspotFactor);
                        }
                        hotspotFactorService.saveBatch(physicalHotspotFactors);
                    }

                    // Todo: 其它危害因素类型及模板待开发......

                    log.info(sheetName + "存储数据库成功！");
                }
            }).doReadAll();
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
     * 导出危害因素字典
     * @param response
     */
    @Override
    public void factorDictDownload(HttpServletResponse response) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<FactorDictionary> factorDictionaries = factorDictionaryMapper.selectList(new LambdaQueryWrapper<FactorDictionary>().isNotNull(FactorDictionary::getSubName).orderByAsc(FactorDictionary::getFactorId));
        for (FactorDictionary dictionary : factorDictionaries) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("万达-危害因素id", dictionary.getFactorId());
      //      map.put("万达-危害因素名称", dictionary.getFactorName());
            map.put("安联-检测物质名称", dictionary.getSubName());
            listMap.add(map);
        }
        FileUtils.downloadExcel(listMap, response);
    }


}
