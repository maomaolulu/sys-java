package may.yuntian.anlian.controller;

import may.yuntian.anlian.service.FinanciaDashboardService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gy
 * @Description 财务数据看板
 * @date 2023-07-12 13:42
 */
@RestController
@RequestMapping("/financial/dashboard")
public class FinanciaDashboardController {

    @Autowired
    private FinanciaDashboardService financiaDashboardService;

    @GetMapping("/getTotalMoney")
    private Result getTotalMoney(String companyOrder){
        return Result.ok("查询成功", financiaDashboardService.getTotalMoney(companyOrder));
    }

    @GetMapping("/exportExcel")
    private void exportExcel(String companyOrder, HttpServletResponse response){
        financiaDashboardService.exportExcel(financiaDashboardService.getTotalMoney(companyOrder), response, companyOrder);
    }

    @GetMapping("/getThisYearReceipt")
    private Result getThisYearReceipt(String companyOrder){
       return Result.ok("查询成功",financiaDashboardService.getThisYearReceipt(companyOrder));
    }
}
