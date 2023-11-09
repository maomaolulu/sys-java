package anlian;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.ApiOperation;

public class exportDate {
    @GetMapping("/exportData")
    @ApiOperation("导出绩效提成数据")
    public void exportData(HttpServletResponse response,Map<String, Object> params) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();

        
        String []columnNames = {"用户名","年龄","手机号","性别","性别","性别","性别","性别","性别","性别","性别","性别","性别","性别",
        		"性别","性别","性别","性别","性别","性别","性别","性别","性别","性别","性别","性别"};

        Sheet sheet = workbook.createSheet();
        Font titleFont = workbook.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(0);

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(titleStyle);
        }
        //模拟构造数据
//        List<UserExcelModel> dataList = newCommission ArrayList<>();
//        dataList.add(newCommission UserExcelModel("张三",12,"13867098765","男"));
//        dataList.add(newCommission UserExcelModel("张三1",12,"13867098765","男"));
//        dataList.add(newCommission UserExcelModel("张三2",12,"13867098765","男"));
//        dataList.add(newCommission UserExcelModel("张三3",12,"13867098765","男"));

        //创建数据行并写入值
//        for (int j = 0; j < dataList.size(); j++) {
//            UserExcelModel userExcelModel = dataList.get(j);
//            int lastRowNum = sheet.getLastRowNum();
//            Row dataRow = sheet.createRow(lastRowNum + 1);
//            dataRow.createCell(0).setCellValue(userExcelModel.getName());
//            dataRow.createCell(1).setCellValue(userExcelModel.getAge());
//            dataRow.createCell(2).setCellValue(userExcelModel.getMobile());
//            dataRow.createCell(3).setCellValue(userExcelModel.getSex());
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode("easyexcel.xls", "utf-8"));
        response.setHeader("Access-Control-Expose-Headers", "content-Disposition");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    
}
