package may.yuntian.anlian.service.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcelFactory;
import may.yuntian.anlian.dto.ContractExportDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ContractMapper;
import may.yuntian.anlian.entity.ContractEntity;
import may.yuntian.anlian.service.ContractService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.vo.ContractStatisticVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 合同信息
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@Service("contractService")
public class ContractServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements ContractService {
//	 @Autowired
//	 private SalesTargetService salesTargetService;//业务员销售目标信息

    /**
     * 用于普通用户的分页查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        if(!ShiroUtils.getUserEntity().getSubjection().contains("杭州")){
//            params.put("dataBelong", ShiroUtils.getUserEntity().getSubjection());
//        }else {
//            params.put("dataBelong", "安联");
//        }
        QueryWrapper<ContractEntity> queryWrapper = queryWrapperByParams(params);
        IPage<ContractEntity> page = this.page(new Query<ContractEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /**
     * 根据条件查询所有信息并导出
     * @param params
     * @return
     */
    public void exportList(HttpServletResponse response, Map<String, Object> params){
        QueryWrapper<ContractEntity> queryWrapper = queryWrapperByParams(params);
        List<ContractExportDto> list = baseMapper.exportList(queryWrapper);
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("合同信息", "UTF-8").replaceAll("\\+", "%20") + ".xlsx" + ";");
            out = response.getOutputStream();
            // 指定用哪个class去写，写到第一个sheet，文件流会自动关闭
            EasyExcelFactory.write(out, ContractExportDto.class).sheet("sheet").doWrite(list);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IoUtil.close(out);
        }
    }


    /**
     * 用于合同信息分页的查询条件的处理
     *
     * @param params
     * @return
     */
    private QueryWrapper<ContractEntity> queryWrapperByParams(Map<String, Object> params) {
//    	String parentid = (String)params.get("parentid");//父级ID,一级指令为0
//    	String identifier = (String)params.get("identifier");//合同编号    模糊搜索
//    	String projectName = (String)params.get("projectName");//项目名称    模糊搜索
//    	String company = (String)params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索
//    	String entrustCompany = (String)params.get("entrustCompany");//委托单位名称  模糊搜索
//    	String salesmen = (String)params.get("salesmen");//业务员    模糊搜索
//    	String telephone = (String)params.get("telephone");//联系电话    模糊搜索
//    	String status = (String)params.get("status");//合同状态
//    	String contractStatus = (String)params.get("contractStatus");//合同状态
//    	String dealStatus = (String)params.get("dealStatus");//协议签订状态状态
//    	String entrustType = (String)params.get("entrustType");//委托类型
//    	String type = (String)params.get("type");//合同类型
//    	String companyOrder = (String)params.get("companyOrder");//项目隶属公司
////    	String childQuantity = (String)params.get("childQuantity");//子级数量
//    	//合同签订日期signDate
//    	String startDate = (String)params.get("startDate");	//合同签订日期
//    	String endDate = (String)params.get("endDate");		//合同签订日期
//    	
//    	QueryWrapper<ContractEntity> queryWrapper = newCommission QueryWrapper<ContractEntity>()
////    			.eq(StringUtils.isNotBlank(parentid),"parentid", parentid)
//    			.like(StringUtils.isNotBlank(identifier),"identifier", identifier)
//    			.like(StringUtils.isNotBlank(projectName),"project_name", projectName)
//    			.like(StringUtils.isNotBlank(entrustCompany),"entrust_company", entrustCompany)
//    			.like(StringUtils.isNotBlank(salesmen),"salesmen", salesmen)
//    			.like(StringUtils.isNotBlank(telephone),"telephone", telephone)
//    			.eq(StringUtils.checkValNotNull(status), "status", status)
//    			.eq(StringUtils.checkValNotNull(contractStatus), "contract_status", contractStatus)
//    			.eq(StringUtils.checkValNotNull(dealStatus), "deal_status", dealStatus)
//    			.eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
//                .eq(StringUtils.checkValNotNull(type), "type", type)
//    			.eq(StringUtils.checkValNotNull(companyOrder), "company_order", companyOrder)
////    			.eq(StringUtils.checkValNotNull(childQuantity), "child_quantity", childQuantity)
//    			.between(StringUtils.isNotBlank(endDate), "sign_date", startDate, endDate)//合同签订日期signDate
//    			.and(StringUtils.isNotBlank(company),i -> 
//    			i.like("company", company)
//    			.or()
//    			.like("entrust_company", company)
//    					)
//    			.orderByDesc("id");
        String id = (String) params.get("id");
        String identifier = (String) params.get("identifier");//合同编号    模糊搜索
        String projectName = (String) params.get("projectName");//项目名称    模糊搜索
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索
        String salesmen = (String) params.get("salesmen");//业务员    模糊搜索
        String telephone = (String) params.get("telephone");//联系电话    模糊搜索
        String entrustType = (String) params.get("entrustType");//委托类型
        String type = (String) params.get("type");//合同类型
        String companyOrder = (String) params.get("companyOrder");//合同隶属公司
//        String subjection = ShiroUtils.getUserEntity().getSubjection();
        String contractStatus = (String) params.get("contractStatus");//合同状态
        String dealStatus = (String) params.get("dealStatus");//协议签订状态状态
        String subjection = (String) params.get("subjection"); //当前登录用户所属公司
        String types = (String) params.get("types");
        List<String> typeList = new ArrayList<>();
        if (StringUtils.isNotBlank(types)) {
            String[] typev = types.split(",");
            typeList = Arrays.asList(typev);
        }

        String province = (String) params.get("province");//省份
        String city = (String) params.get("city");//市
        String area = (String) params.get("area");//区
        String officeAddress = (String) params.get("officeAddress");//受检地址
        String contact = (String) params.get("contact");//联系人

//    	String totalMoney = (String)params.get("totalMoney");//项目金额
        String totalMoneyMin = (String) params.get("totalMoneyMin");//小于合同金额
        String totalMoneyMax = (String) params.get("totalMoneyMax");//大于合同金额

//    	String commission = (String)params.get("commission");//佣金
        String commissionMin = (String) params.get("commissionMin");//小于佣金
        String commissionMax = (String) params.get("commissionMax");//大于佣金

//    	String evaluationFee = (String)params.get("evaluationFee");//评审费
        String evaluationFeeMin = (String) params.get("evaluationFeeMin");//小于评审费
        String evaluationFeeMax = (String) params.get("evaluationFeeMax");//大于评审费

//    	String subprojectFee = (String)params.get("subprojectFee");//分包费
        String subprojectFeeMin = (String) params.get("subprojectFeeMin");//小于分包费
        String subprojectFeeMax = (String) params.get("subprojectFeeMax");//大于分包费

//    	String serviceCharge = (String)params.get("serviceCharge");//服务费
        String serviceChargeMin = (String) params.get("serviceChargeMin");//小于服务费
        String serviceChargeMax = (String) params.get("serviceChargeMax");//大于服务费

//    	String otherExpenses = (String)params.get("otherExpenses");//其他支出
        String otherExpensesMin = (String) params.get("otherExpensesMin");//小于其他支出
        String otherExpensesMax = (String) params.get("otherExpensesMax");//大于其他支出

//    	String netvalue = (String)params.get("netvalue");//项目净值
        String netvalueMin = (String) params.get("netvalueMin");//小于合同净值
        String netvalueMax = (String) params.get("netvalueMax");//大于合同净值

//    	String receiptMoney = (String)params.get("receiptMoney");//已收款金额
        String receiptMoneyMin = (String) params.get("receiptMoneyMin");//小于已收款金额
        String receiptMoneyMax = (String) params.get("receiptMoneyMax");//大于已收款金额

//    	String invoiceMoney = (String)params.get("invoiceMoney");//已开票金额
        String invoiceMoneyMin = (String) params.get("invoiceMoneyMin");//小于已开票金额
        String invoiceMoneyMax = (String) params.get("invoiceMoneyMax");//大于已开票金额

//    	String virtualTax = (String)params.get("virtualTax");//虚拟税费
        String virtualTaxMin = (String) params.get("virtualTaxMin");//小于虚拟税费
        String virtualTaxMax = (String) params.get("virtualTaxMax");//大于虚拟税费

        String urgent = (String) params.get("urgent");//加急状态(0正常，1较急、2加急)
        String old = (String) params.get("old");//新老业务(0新业务，1续签业务)
        String careof = (String) params.get("careof");//是否为转交业务(0否，1是)
        String careofType = (String) params.get("careofType");//转交类型(0意向客户，1确定合作客户)
        String careofUsername = (String) params.get("careofUsername");//转交人
        String keyClauses = (String) params.get("keyClauses");//关键条款
        String username = (String) params.get("username");//录入人姓名

        //项目签订日期signDate
        String startDate = (String) params.get("startDate");    //合同签订日期开始
        String endDate = (String) params.get("endDate");        //合同签订日期结束


        String contractReviewDateStart = (String) params.get("contractReviewDateStart");//合同评审日期开始
        String contractReviewDateEnd = (String) params.get("contractReviewDateEnd");    //合同评审日期结束

        String receiptDateStart = (String) params.get("receiptDateStart");//收款日期开始
        String receiptDateEnd = (String) params.get("receiptDateEnd");    //收款日期结束

        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<ContractEntity>()
//    			.eq(StringUtils.isNotBlank(parentid),"parentid", parentid)
                .eq(StringUtils.isNotBlank(id), "id", id)
                .like(StringUtils.isNotBlank(identifier), "identifier", identifier)
                .like(StringUtils.isNotBlank(projectName), "project_name", projectName)
                .like(StringUtils.isNotBlank(salesmen), "salesmen", salesmen)
                .like(StringUtils.isNotBlank(telephone), "telephone", telephone)
                .like(StringUtils.isNotBlank(province), "province", province)
                .like(StringUtils.isNotBlank(city), "city", city)
                .like(StringUtils.isNotBlank(area), "area", area)
                .like(StringUtils.isNotBlank(officeAddress), "office_address", officeAddress)
                .like(StringUtils.isNotBlank(contact), "contact", contact)
                .like(StringUtils.isNotBlank(careofUsername), "careof_username", careofUsername)
                .like(StringUtils.isNotBlank(keyClauses), "keyClauses", keyClauses)
                .like(StringUtils.isNotBlank(username), "username", username)
//				.and(i -> i.like(!"杭州安联".equals(dataBelong), "company_order", dataBelong.substring(0,2)).or().like(!"杭州安联".equals(dataBelong), "business_source", dataBelong.substring(0,2)))
                // 支持分流


//    			.eq(StringUtils.checkValNotNull(totalMoney),"total_money", totalMoney)
                .ge(StringUtils.checkValNotNull(totalMoneyMin), "total_money", totalMoneyMin)
                .le(StringUtils.checkValNotNull(totalMoneyMax), "total_money", totalMoneyMax)

//    			.eq(StringUtils.checkValNotNull(commission),"commission", commission)
                .ge(StringUtils.checkValNotNull(commissionMin), "total_money", commissionMin)
                .le(StringUtils.checkValNotNull(commissionMax), "total_money", commissionMax)

//    			.eq(StringUtils.checkValNotNull(evaluationFee),"evaluation_fee", evaluationFee)
                .ge(StringUtils.checkValNotNull(evaluationFeeMin), "total_money", evaluationFeeMin)
                .le(StringUtils.checkValNotNull(evaluationFeeMax), "total_money", evaluationFeeMax)

//    			.eq(StringUtils.checkValNotNull(subprojectFee),"subproject_fee", subprojectFee)
                .ge(StringUtils.checkValNotNull(subprojectFeeMin), "total_money", subprojectFeeMin)
                .le(StringUtils.checkValNotNull(subprojectFeeMax), "total_money", subprojectFeeMax)

//    			.eq(StringUtils.checkValNotNull(serviceCharge),"service_charge", serviceCharge)
                .ge(StringUtils.checkValNotNull(serviceChargeMin), "total_money", serviceChargeMin)
                .le(StringUtils.checkValNotNull(serviceChargeMax), "total_money", serviceChargeMax)

//    			.eq(StringUtils.checkValNotNull(otherExpenses),"other_expenses", otherExpenses)
                .ge(StringUtils.checkValNotNull(otherExpensesMin), "total_money", otherExpensesMin)
                .le(StringUtils.checkValNotNull(otherExpensesMax), "total_money", otherExpensesMax)

//    			.eq(StringUtils.checkValNotNull(netvalue),"netvalue", netvalue)
                .ge(StringUtils.checkValNotNull(netvalueMin), "total_money", netvalueMin)
                .le(StringUtils.checkValNotNull(netvalueMax), "total_money", netvalueMax)

//    			.eq(StringUtils.checkValNotNull(receiptMoney),"receipt_money", receiptMoney)
                .ge(StringUtils.checkValNotNull(receiptMoneyMin), "total_money", receiptMoneyMin)
                .le(StringUtils.checkValNotNull(receiptMoneyMax), "total_money", receiptMoneyMax)

//    			.eq(StringUtils.checkValNotNull(invoiceMoney),"invoice_money", invoiceMoney)
                .ge(StringUtils.checkValNotNull(invoiceMoneyMin), "total_money", invoiceMoneyMin)
                .le(StringUtils.checkValNotNull(invoiceMoneyMax), "total_money", invoiceMoneyMax)

//    			.eq(StringUtils.checkValNotNull(virtualTax),"virtual_tax", virtualTax)
                .ge(StringUtils.checkValNotNull(virtualTaxMin), "total_money", virtualTaxMin)
                .le(StringUtils.checkValNotNull(virtualTaxMax), "total_money", virtualTaxMax)

                .eq(StringUtils.checkValNotNull(urgent), "urgent", urgent)
                .eq(StringUtils.checkValNotNull(old), "old", old)
                .eq(StringUtils.checkValNotNull(careof), "careof", careof)
                .eq(StringUtils.checkValNotNull(careofType), "careof_type", careofType)

                .eq(StringUtils.checkValNotNull(contractStatus), "contract_status", contractStatus)
                .eq(StringUtils.checkValNotNull(dealStatus), "deal_status", dealStatus)

                .eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
                .eq(StringUtils.checkValNotNull(type), "type", type)
                .eq(StringUtils.checkValNotNull(companyOrder), "company_order", companyOrder)
                .between(StringUtils.isNotBlank(endDate), "sign_date", startDate, endDate)//项目签订日期signDate

                .between(StringUtils.isNotBlank(contractReviewDateEnd), "contract_review_date", contractReviewDateStart, contractReviewDateEnd)//合同评审日期

                .between(StringUtils.isNotBlank(receiptDateEnd), "receipt_date", receiptDateStart, receiptDateEnd)//收款日期

                .in(typeList.size() > 0, "type", typeList)

                .and(StringUtils.isNotBlank(company), i ->
                        i.like("company", company)
                                .or()
                                .like("entrust_company", company)
                )
                .orderByDesc("id");

        if (StringUtils.isNotBlank(subjection)){
            queryWrapper.and(wrapper -> wrapper.like(StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(StringUtils.isNotBlank(subjection), "business_source", subjection));
        }
        return queryWrapper;
    }

    /**
     * 用于合同信息统计报表的查询条件的处理
     *
     * @param queryVo
     * @return
     */
    private QueryWrapper<ContractEntity> queryWrapperReportByParams(ContractStatisticVo queryVo) {
        String salesmen = queryVo.getSalesmen();//业务员    模糊搜索
        Integer status = queryVo.getStatus();//合同状态
        String entrustType = queryVo.getEntrustType();//委托类型
        String type = queryVo.getType();//合同类型
        //合同签订日期signDate
        String startDate = queryVo.getStartDate();    //合同签订日期
        String endDate = queryVo.getEndDate();        //合同签订日期

        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<ContractEntity>()
                .like(StringUtils.isNotBlank(salesmen), "salesmen", salesmen)
                .eq(StringUtils.checkValNotNull(status), "status", status)
                .eq(StringUtils.checkValNotNull(entrustType), "entrust_type", entrustType)
                .eq(StringUtils.checkValNotNull(type), "type", type)
                .between(StringUtils.isNotBlank(endDate), "sign_date", startDate, endDate);//合同签订日期signDate
        return queryWrapper;
    }


    /**
     * 根据企业ID查询是否已经存在于合同信息中
     *
     * @param companyId 企业ID
     * @return boolean
     */
    public Boolean notExistContractByCompany(Long companyId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ContractEntity>().eq("company_id", companyId));
        if (count > 0)
            return false;
        else
            return true;
    }

    /**
     * 根据合同编号查询是否已经存在于合同信息中
     *
     * @param identifier 合同编号
     * @return boolean
     */
    public Boolean notExistContractByIdentifier(String identifier) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ContractEntity>().eq("identifier", identifier));
        if (count > 0)
            return false;
        else
            return true;
    }

    /**
     * 根据合同ID查询合同编号
     *
     * @param id
     * @return
     */
    private String queryIdentifierById(Long id) {
        ContractEntity entity = baseMapper.selectById(id);

        return entity.getIdentifier();
    }

    /**
     * 根据合同ID获得合同编号后缀号
     * 用于生成样品编号
     *
     * @param id
     * @return
     */
    public String queryIdentifierSuffixById(Long id) {
        String identifier = queryIdentifierById(id);
        if (identifier == null) {
            return "000";
        } else {
            int index = identifier.lastIndexOf("-");
            identifier = identifier.substring(index + 1);
            return identifier;
        }
    }

    /**
     * 修改合同信息
     * 当合同状态status为合同签订1、合同中止99时，连带其子项状态也连带更新
     * 满足需求：合同终止接口包含其子集也终止；签订合同时包含其子集也签订
     */
    public boolean updateContractById(ContractEntity contractEntity) {
        boolean retBool = this.updateById(contractEntity);//修改合同信息
        //System.out.println("修改合同信息:"+contractEntity.toString());

        return retBool;
    }

    /**
     * 统计报表：根据业务员分组显示合同数量及总额
     */
    public List<Map<String, Object>> getReportAmountBySalesmen(ContractStatisticVo queryVo) {
        List<Map<String, Object>> list = this.listMaps(
                queryWrapperReportByParams(queryVo)
                        .select("salesmen,SUM(total_money) totalMoney,COUNT(*) amount")
                        .groupBy("salesmen")
                        .orderByDesc("totalMoney")
        );
        //list.forEach(System.out::println);

        return list;
    }

    /**
     * 统计报表：根据合同签定日期分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    public List<Map<String, Object>> getReportAmountBySignDate(ContractStatisticVo queryVo) {
        String asColumns = "signDate";
        String dateColumns = DateUtils.formatYMWD(queryVo.getDateType(), "sign_date", asColumns);
        List<Map<String, Object>> list = this.listMaps(
                queryWrapperReportByParams(queryVo)
                        .select(dateColumns + ",SUM(total_money) totalMoney,COUNT(*) amount")
                        .groupBy(asColumns)
                        .orderByAsc(asColumns)
        );
        //list.forEach(System.out::println);
        return list;
    }

    /**
     * 统计报表：根据合同签定日期与业务员分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    public List<Map<String, Object>> getReportAmountBySignDateAndSalesmen(ContractStatisticVo queryVo) {
        String asColumns = "signDate";
        String dateColumns = DateUtils.formatYMWD(queryVo.getDateType(), "sign_date", asColumns);
        List<Map<String, Object>> list = this.listMaps(
                queryWrapperReportByParams(queryVo)
                        .select(dateColumns + ",salesmen,SUM(total_money) totalMoney,COUNT(*) amount")
                        .groupBy("salesmen", asColumns)
                        .orderByAsc(asColumns)
        );

        //list.forEach(System.out::println);
//		SalesTargetEntity params = newCommission SalesTargetEntity();
//		String dateType = queryVo.getDateType();
//		params.setType(dateType);//日期类型(年Y,月M,周W,日D)
//		List<SalesTargetEntity> listAllBySales = salesTargetService.listAllBySales(params);//业务员销售目标信息
//		for(Map<String, Object> map : list) {
//			for(SalesTargetEntity st : listAllBySales) {
//				if( st.getUsername().equals(map.get("salesmen")) && st.getSalesDate().equals(map.get("signDate")) ) {
//					map.put("salesTarget", st.getSalesTarget());
//					break;
//				}
//			}
//		}
        //System.out.println("业务员销售目标信息......");
        //list.forEach(System.out::println);

        return list;
    }
//	SELECT DATE_FORMAT(sign_date,'%Y') signDate,SUM(total_money) money,COUNT(*) amount FROM t_contract GROUP BY signDate ORDER BY signDate;
//	SELECT DATE_FORMAT(sign_date,'%Y-%m') signDate,SUM(total_money) money,COUNT(*) amount FROM t_contract GROUP BY signDate ORDER BY signDate;
//	SELECT DATE_FORMAT(sign_date,'%XW%V') signDate,SUM(total_money) money,COUNT(*) amount FROM t_contract GROUP BY signDate ORDER BY signDate;

    /**
     * 统计报表：根据合同签定日期与合同类型分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    public List<Map<String, Object>> getReportAmountBySignDateAndType(ContractStatisticVo queryVo) {
        String asColumns = "signDate";
        String dateColumns = DateUtils.formatYMWD(queryVo.getDateType(), "sign_date", asColumns);
        List<Map<String, Object>> list = this.listMaps(
                queryWrapperReportByParams(queryVo)
                        .select(dateColumns + ",type,SUM(total_money) totalMoney,COUNT(*) amount")
                        .groupBy("type", asColumns)
                        .orderByAsc(asColumns)
        );
        return list;
    }

    /**
     * 统计报表：根据合同签定日期与委托类型分组显示合同数量及总额
     * 统计日期类型(年Y,月M,周W,日D)
     */
    public List<Map<String, Object>> getReportAmountBySignDateAndEntrustType(ContractStatisticVo queryVo) {
        String asColumns = "signDate";
        String dateColumns = DateUtils.formatYMWD(queryVo.getDateType(), "sign_date", asColumns);
        List<Map<String, Object>> list = this.listMaps(
                queryWrapperReportByParams(queryVo)
                        .select(dateColumns + ",entrust_type as entrustType,SUM(total_money) totalMoney,COUNT(*) amount")
                        .groupBy("entrustType", asColumns)
                        .orderByAsc(asColumns)
        );
        return list;
    }

}
