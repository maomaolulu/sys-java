package may.yuntian.anlianwage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.entity.GradePointEntity;
import may.yuntian.anlianwage.entity.IndustryBenchmarkEntity;
import may.yuntian.anlianwage.entity.PersonnelIndicatorEntity;
import may.yuntian.anlianwage.mapper.GradePointMapper;
import may.yuntian.anlianwage.mapper.IndustryBenchmarkMapper;
import may.yuntian.anlianwage.mapper.PersonnelIndicatorMapper;
import may.yuntian.anlianwage.service.PerCommissionService;
import may.yuntian.anlianwage.service.GradePointService;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.anlianwage.vo.ProjectPerformanceVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:47
 */
@Service("gradePointService")
@SuppressWarnings("all")
public class GradePointServiceImpl extends ServiceImpl<GradePointMapper, GradePointEntity> implements GradePointService {

    @Autowired
    private GradePointMapper gradePointMapper;
    @Autowired
    private IndustryBenchmarkMapper industryBenchmarkMapper;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private PerCommissionService commissionService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PersonnelIndicatorMapper personnelIndicatorMapper;


    /**
     * 不分页查询评价项目的绩效列表
     */
    @Override
    public List<ProjectPerformanceVo> listAll(ProjectPerformanceVo proPerformanceVo) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = DateUtils.dateTimeMonthStart();//当前月的第一天
        String performanceDateStart = format.format(date);
        //模糊查询
        QueryWrapper<ProjectPerformanceVo> wrapper = new QueryWrapper<>();

        //分流所需字段
        String subjection = proPerformanceVo.getSubjection();

        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            wrapper.and(wrapper1 -> wrapper1.eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "business_source", subjection));
        }

        wrapper.like(StrUtil.isNotBlank(proPerformanceVo.getCharge()),"p.charge",proPerformanceVo.getCharge());
        wrapper.like(StrUtil.isNotBlank(proPerformanceVo.getIdentifier()),"p.identifier" ,proPerformanceVo.getIdentifier());
        wrapper.like(StrUtil.isNotBlank(proPerformanceVo.getCompany()), "p.company" , proPerformanceVo.getCompany());
        wrapper.between(StringUtils.isNotBlank(proPerformanceVo.getEndDate()),"pd.report_issue", proPerformanceVo.getStartDate(), proPerformanceVo.getEndDate());
        wrapper.ge(StringUtils.isBlank(proPerformanceVo.getEndDate()),"pd.report_issue",performanceDateStart );
        wrapper.ge("pd.project_id",0);

        List<ProjectPerformanceVo> performanceInfoList = gradePointMapper.getPerformanceInfoList(wrapper);

        for (ProjectPerformanceVo projectPerformanceVo : performanceInfoList) {

            Long projectId = projectPerformanceVo.getProjectId();
            GradePointEntity pointEntity = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", projectId));
            if (pointEntity != null){
                Double technicalGradeScore = pointEntity.getTechnicalGradeScore();
                projectPerformanceVo.setTechnicalGradeScore(technicalGradeScore);
                projectPerformanceVo.setScaleBasisScore(pointEntity.getScaleBasisScore());
                projectPerformanceVo.setIndustryBenchmarkScore(pointEntity.getIndustryBenchmarkScore());
                projectPerformanceVo.setUrgentCaseScore(pointEntity.getUrgentCaseScore());
                projectPerformanceVo.setItemTypeScore(pointEntity.getItemTypeScore());

                projectPerformanceVo.setProjectGradePoint(pointEntity.getProjectGradePoint());
                projectPerformanceVo.setTotalPerformance(pointEntity.getTotalPerformance());
                projectPerformanceVo.setReferPerformance(pointEntity.getReferPerformance());
                projectPerformanceVo.setDifference(pointEntity.getDifference());

                continue;
            }

            // ②：设置行业基准分
            Double point = 0D;
            String industryCategory = projectPerformanceVo.getIndustryCategory();
            // 行业基准有null 和 空白串 '' 和 长度<4 , 默认行业基准分为 0
            if (industryCategory == null || industryCategory == "" || industryCategory.length() < 4){
                projectPerformanceVo.setIndustryBenchmarkScore(0D);
            } else{
                String substring = "";
                if (projectId.equals(34309L)){
                     substring = industryCategory.substring(0, 5); //截取行业分类字符串的前五位
                }else {
                     substring = industryCategory.substring(0, 4); //截取行业分类字符串的前四位
                }
                IndustryBenchmarkEntity code = industryBenchmarkMapper.selectOne(new QueryWrapper<IndustryBenchmarkEntity>().eq("code", substring));
                if (code == null){
                    projectPerformanceVo.setIndustryBenchmarkScore(0D); //查不到默认为0
                } else{
                    point = code.getScore();
                    projectPerformanceVo.setIndustryBenchmarkScore(point);
                }
            }

            // 根据一线作业人数设置规模基准分
            Integer totalPerson = projectPerformanceVo.getLaborQuota(); //一线作业人数
            // 线上数据库有null值，要加非空判断，防止NullPointException！
            if (totalPerson == null || totalPerson == 0){ // 人数不详，默认规模基准分为1。
                projectPerformanceVo.setScaleBasisScore(1D);
            }else if (totalPerson <= 20){
                projectPerformanceVo.setScaleBasisScore(1D);
            }else if (totalPerson <= 100){
                projectPerformanceVo.setScaleBasisScore(1.1D);
            }else if (totalPerson <= 300){
                projectPerformanceVo.setScaleBasisScore(1.2D);
            }else if (totalPerson <= 999){
                projectPerformanceVo.setScaleBasisScore(1.3D);
            }else {
                projectPerformanceVo.setScaleBasisScore(1.5D);
            }

            //设置加急情况分：1
            projectPerformanceVo.setUrgentCaseScore(1D);

            //计算参考绩效：净值 * 0.065
            BigDecimal bigDecimal = new BigDecimal("0.065");
            BigDecimal netvalue = projectPerformanceVo.getNetvalue();
            projectPerformanceVo.setReferPerformance(bigDecimal.multiply(netvalue));

            //先将项目id、行业基准分、规模基准分、项目类型分、加急情况分、参考绩效逐条保存到绩点表
            GradePointEntity gradePoint = new GradePointEntity();
            gradePoint.setProjectId(projectPerformanceVo.getProjectId()); // project_id
            gradePoint.setIndustryBenchmarkScore(projectPerformanceVo.getIndustryBenchmarkScore()); // 行业基准分
            gradePoint.setScaleBasisScore(projectPerformanceVo.getScaleBasisScore()); // 规模基准分
            gradePoint.setItemTypeScore(projectPerformanceVo.getItemTypeScore()); // 项目类型分
            gradePoint.setUrgentCaseScore(1D); // 加急情况分固定值为1
            gradePoint.setReferPerformance(projectPerformanceVo.getReferPerformance()); // 参考绩效

            // 若绩点表中存在该项目id，不插入; 否则，插入！
            if (gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", gradePoint.getProjectId())) == null){
                gradePointMapper.insert(gradePoint);
            } else {
                gradePointMapper.update(gradePoint, new QueryWrapper<GradePointEntity>().eq("project_id", gradePoint.getProjectId()));
            }

        }
        return performanceInfoList;
    }

    /**
     *  计算绩效并保存（填写一个技术分，保存一条记录）
     *  计算签发提成、归档提成！
     */
    @Override
    public GradePointEntity editSave(GradePointEntity gradePointEntity) {
        // 得到填写的技术分
        Double technicalScore = gradePointEntity.getTechnicalGradeScore();
        Double scaleBasisScore = gradePointEntity.getScaleBasisScore();
        Double industryBenchmarkScore = gradePointEntity.getIndustryBenchmarkScore();
        Double itemTypeScore = gradePointEntity.getItemTypeScore();
        Double urgentCaseScore = gradePointEntity.getUrgentCaseScore();

        // 根据项目id，获取相关分数，然后进行计算。
        GradePointEntity pointEntity = baseMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", gradePointEntity.getProjectId()));

        // 累积: 项目绩点
        Double projectGradePoint =(technicalScore * industryBenchmarkScore * scaleBasisScore * itemTypeScore * urgentCaseScore);
        // 计算总绩效 BigDecimal.valueOf(d)  （注：650为固定的提成基数。有可能改变，后续可放在字典里！）
        BigDecimal totalPerformance = new BigDecimal(650).multiply(BigDecimal.valueOf(projectGradePoint));
        Optional.ofNullable(totalPerformance).orElse(BigDecimal.ZERO);
        // 得到参考绩效：净值 * 0.065
       // BigDecimal referPerformance = Optional.ofNullable(pointEntity.getReferPerformance()).orElse(BigDecimal.ZERO);
        BigDecimal referPerformance = projectService.getOne(new QueryWrapper<ProjectEntity>()
                .eq("id",pointEntity.getProjectId()))
                .getNetvalue()
                .multiply(new BigDecimal(0.065));
        // 计算差值
        BigDecimal difference = totalPerformance.subtract(referPerformance);
        // 绩效表保存该条记录：只更新技术分、项目绩点、总绩效、差值！
        GradePointEntity entity = new GradePointEntity();
        entity.setProjectId(gradePointEntity.getProjectId());
        entity.setTechnicalGradeScore(technicalScore);
        entity.setScaleBasisScore(scaleBasisScore);
        entity.setUrgentCaseScore(urgentCaseScore);
        entity.setItemTypeScore(itemTypeScore);
        entity.setIndustryBenchmarkScore(industryBenchmarkScore);
        entity.setProjectGradePoint(projectGradePoint);
        entity.setTotalPerformance(totalPerformance);
        entity.setReferPerformance(referPerformance);
        entity.setDifference(difference);
        baseMapper.update(entity ,new UpdateWrapper<GradePointEntity>().eq("project_id",gradePointEntity.getProjectId()));

        /** 计算提成：
         *    commissionIssue 签发提成 = 总绩效 × 70% （不论哪一年都按此公式计算！）
         *    filingFees 归档提成 = 总绩效 × 30% （2022、2021、2020年及之前，分为三类）
         */
        ProjectEntity projectEntity = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", gradePointEntity.getProjectId()));
        SysUserEntity sysUserEntity = sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("user_id", projectEntity.getChargeId()));
        // 报告签发日期
        Date date = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", gradePointEntity.getProjectId())).getReportIssue();
        // 签发提成
        BigDecimal commissionIssue = (date == null) ? BigDecimal.ZERO : totalPerformance.multiply(new BigDecimal(0.7));
        /* 将签发提成保存到co_commission */
        PerCommissionEntity commissionEntity = commissionService.getByPlanId(projectEntity.getId(),"签发提成");
        if (commissionEntity != null){
            commissionEntity.setCmsAmount(commissionIssue);
            commissionService.updateById(commissionEntity); // 在co_commission中保存签发提成！
        } else {
            // 根据project_id插入一条签发提成
            PerCommissionEntity commission = new PerCommissionEntity();
            commission.setProjectId(gradePointEntity.getProjectId());
            commission.setType("签发提成");
            commission.setCmsAmount(commissionIssue); // 提成金额
            commission.setPersonnel(projectEntity.getCharge());  // 提成人：只有1个。==> al_project_charge
            commission.setSubjection(sysUserEntity.getSubjection());
            commission.setDeptId(sysUserEntity.getDeptId());
            commission.setDeptName("评价");
            // 提成日期:(签发提成的提成日期就是签发日期)
            commission.setCommissionDate(date);
            commission.setPType(projectEntity.getType());  // pType: 项目类型
            commissionService.save(commission); // 在co_commission中保存签发提成！
        }

        /*  归档提成：按提成类型，保存到co_commission   */
        // 根据项目id，得到签发日期: reportIssue , 截取年份 , 判断
        BigDecimal filingFees = null;  // 归档提成
        PerCommissionEntity perCommissionEntity = commissionService.getByPlanId(projectEntity.getId(),"报告提成");

        // 报告归档日期
        Date dateB = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", gradePointEntity.getProjectId())).getReportFiling();
        if (date == null && dateB == null){
            perCommissionEntity.setCmsAmount(BigDecimal.ZERO);
            commissionService.updateById(perCommissionEntity);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);  // 年份

        if (perCommissionEntity != null){
            if (year == 2022){
                filingFees = totalPerformance.multiply(new BigDecimal("0.3")); // 计算归档提成：2022年 ===> 总绩效 * 30%
                perCommissionEntity.setCmsAmount(filingFees);
                commissionService.updateById(perCommissionEntity);  // 保存2022年份归档提成
            }
            if (year == 2021){ // 归档提成：2021年
                // ①计算个人累计金额
                BigDecimal sum = BigDecimal.ZERO; // 累计金额（项目净值的累计）
                // 提成人姓名
                String charge = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", gradePointEntity.getProjectId())).getCharge();
                PersonnelIndicatorEntity personnelIndicator = personnelIndicatorMapper.selectOne(new QueryWrapper<PersonnelIndicatorEntity>().eq(StrUtil.isNotBlank(charge), "user_name", charge));
                BigDecimal aggregateAmount = personnelIndicator.getAggregateAmount();
                // 该提成人21年度指标
                BigDecimal performanceIndicators = personnelIndicator.getPerformanceIndicators();
                // 项目净值
                BigDecimal netvalue = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", gradePointEntity.getProjectId())).getNetvalue();
                sum = netvalue.add(aggregateAmount);

                // 累计/指标：比例区间范围
                // newCommission BigDecimal(16).divide(newCommission BigDecimal(233), 2, BigDecimal.ROUND_DOWN);  保留两位小数，多余的舍去！
                BigDecimal percent = sum.divide(performanceIndicators,3, BigDecimal.ROUND_DOWN);
                int retValue1 = percent.compareTo(new BigDecimal(0.6));
                int retValue2 = percent.compareTo(new BigDecimal(0.8));
                int retValue3 = percent.compareTo(new BigDecimal(1.0));

                // ②计算归档提成
                if (retValue1 <= 0){
                    filingFees = new BigDecimal(0.065).multiply(netvalue).multiply(new BigDecimal(0.3));  // 6.5% × 合同净值 × 30%
                }else if (retValue2 <= 0){
                    BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("0.6")); // 目标金额 * 60%
                    if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                        filingFees = new BigDecimal(0.08).multiply(netvalue).multiply(new BigDecimal(0.3));  // 8% × 合同净值
                    }else { // 卡点了
                        BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.08 计算提成
                        BigDecimal filingFees1 = new BigDecimal("0.08").multiply(overMoney).multiply(new BigDecimal("0.3"));
                        BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.065")).multiply(new BigDecimal("0.3"));
                        filingFees = filingFees1.add(fillingFees2);
                    }

                }else if (retValue3 <= 0){
                    BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("0.8")); // 目标金额 * 80%
                    if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                        filingFees = new BigDecimal(0.1).multiply(netvalue).multiply(new BigDecimal(0.3));  // 10% × 合同净值
                    }else { // 卡点了
                        BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.1 计算提成
                        BigDecimal filingFees1 = new BigDecimal("0.1").multiply(overMoney).multiply(new BigDecimal("0.3"));
                        BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.08")).multiply(new BigDecimal("0.3"));
                        filingFees = filingFees1.add(fillingFees2);
                    }

                }else{
                    BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("1")); // 目标金额 * 100%
                    if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                        filingFees = new BigDecimal(0.14).multiply(netvalue).multiply(new BigDecimal(0.3));  // 14% × 合同净值
                    }else { // 卡点了
                        BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.14 计算提成
                        BigDecimal filingFees1 = new BigDecimal("0.14").multiply(overMoney).multiply(new BigDecimal("0.3"));
                        BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.3"));
                        filingFees = filingFees1.add(fillingFees2);
                    }

                }

                // 保存累计金额（项目净值的累计）: co_personnel_indicator
                PersonnelIndicatorEntity indicatorEntity = new PersonnelIndicatorEntity();
                indicatorEntity.setAggregateAmount(sum);
                personnelIndicatorMapper.update(indicatorEntity , new UpdateWrapper<PersonnelIndicatorEntity>().eq("user_name",charge));

                perCommissionEntity.setCmsAmount(filingFees);
                commissionService.updateById(perCommissionEntity);

            }
            if (year <= 2020){
                // 归档提成：2020年及以前     注：2020年合同净值低于20000默认提成为540 ，否则 2.4% × 净值！
                BigDecimal netvalue = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("project_id", gradePointEntity.getProjectId())).getNetvalue();
                // BigDecimal比较大小用compareTo()方法，返回结果=-1,表示小于; =0,表示等于; =1,表示大于：netvalue.compareTo(newCommission BigDecimal(20000))
                if (netvalue.compareTo(new BigDecimal(20000)) == -1){
                    filingFees = new BigDecimal(540);
                }else{
                    filingFees = netvalue.multiply(new BigDecimal(0.024));
                }
                perCommissionEntity.setCmsAmount(filingFees);
                commissionService.updateById(perCommissionEntity);  // 保存2020年份及之前的归档提成
            }

        }

        // 根据projectId，返回更新后的GradePointEntity对象！(前台页面不用显示签发提成和归档提成。)
        return baseMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id" , gradePointEntity.getProjectId()));
    }

    // 批量导出
    @Override
    public void exportExcel(HttpServletResponse response , ProjectPerformanceVo proPerformanceVo){

        List<ProjectPerformanceVo> list = this.listAll(proPerformanceVo);
        for (ProjectPerformanceVo vo : list) {
            // 通过projectId从绩点表中查出技术打分、项目绩点、总绩效、差值，填入vo对象里
            GradePointEntity one = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_Id", vo.getProjectId()));
            vo.setTechnicalGradeScore(one.getTechnicalGradeScore());
            vo.setProjectGradePoint(one.getProjectGradePoint());
            vo.setTotalPerformance(one.getTotalPerformance());
            vo.setDifference(one.getDifference());
        }

        List<Map<String, Object>> listmap = new ArrayList<>();
        for (ProjectPerformanceVo projectPerformanceVo : list) {
            Map<String,Object> map = new LinkedHashMap<>();

            map.put("签发时间", projectPerformanceVo.getReportTransfer());
            map.put("项目负责人", projectPerformanceVo.getCharge());
            map.put("项目编号", projectPerformanceVo.getIdentifier());
            map.put("受检单位", projectPerformanceVo.getCompany());
            map.put("净值", projectPerformanceVo.getNetvalue());
            map.put("行业分类", projectPerformanceVo.getIndustryCategory());
            map.put("一线作业人数", projectPerformanceVo.getLaborQuota());
            map.put("项目类型", projectPerformanceVo.getType());
            map.put("行业基准", projectPerformanceVo.getIndustryBenchmarkScore());
            map.put("规模基准", projectPerformanceVo.getScaleBasisScore());
            map.put("项目类型分", projectPerformanceVo.getItemTypeScore());
            map.put("加急技术", projectPerformanceVo.getUrgentCaseScore());
            map.put("技术打分", projectPerformanceVo.getTechnicalGradeScore());
            map.put("项目绩点", projectPerformanceVo.getProjectGradePoint());
            map.put("总绩效", projectPerformanceVo.getTotalPerformance());
            map.put("参考绩效", projectPerformanceVo.getReferPerformance());
            map.put("差值", projectPerformanceVo.getDifference());

            listmap.add(map);
        }

        FileUtils.downloadExcel(listmap, response);

    }

    /**
     * 签发提成！
     */
    @Override
    public Boolean getCommissionIssue(PerformanceNodeVo performanceNodeVo) {
        ProjectEntity projectEntity = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", performanceNodeVo.getProjectId()));
        //隶属公司:根据提成人从sys_user表里查subjection
        SysUserEntity sysUserEntity = sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("user_id", projectEntity.getChargeId()));
        if (sysUserEntity != null&&sysUserEntity.getSubjection().equals("杭州安联")){
            System.out.println("pingjiaticheng ----------");
            GradePointEntity gradePointEntity = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", performanceNodeVo.getProjectId()));
            if (gradePointEntity == null){
                gradePointEntity = new GradePointEntity();
                gradePointEntity.setProjectId(performanceNodeVo.getProjectId());
                gradePointMapper.insert(gradePointEntity);
            }
            // 技术打分为空的话，默认其值为0
            BigDecimal totalPerformance = (gradePointEntity.getTotalPerformance() == null) ? BigDecimal.ZERO : gradePointEntity.getTotalPerformance();
            BigDecimal commissionIssue = totalPerformance.multiply(new BigDecimal(0.7)); // 签发提成
            /* 将签发提成保存到co_commission */
            PerCommissionEntity commissionEntity = commissionService.getByPlanId(performanceNodeVo.getProjectId(),"签发提成");
            if(commissionEntity!=null){
                commissionEntity.setCmsAmount(commissionIssue); // 提成金额
                commissionEntity.setPersonnel(projectEntity.getCharge());  // 提成人：只有1个。==> al_project_charge
                commissionEntity.setSubjection(sysUserEntity.getSubjection());
                commissionEntity.setDeptId(sysUserEntity.getDeptId());
                // 提成日期:(签发提成的提成日期就是签发日期)
                commissionEntity.setCommissionDate(performanceNodeVo.getReportIssue());
                commissionService.updateById(commissionEntity); // 在co_commission中保存签发提成！
            }else {
                PerCommissionEntity commissionEntity1 = new PerCommissionEntity();
                commissionEntity1.setProjectId(performanceNodeVo.getProjectId());
                commissionEntity1.setType("签发提成");
                commissionEntity1.setCmsAmount(commissionIssue); // 提成金额
                commissionEntity1.setPersonnel(projectEntity.getCharge());  // 提成人：只有1个。==> al_project_charge
                commissionEntity1.setSubjection(sysUserEntity.getSubjection());
                commissionEntity1.setDeptId(sysUserEntity.getDeptId());
                commissionEntity1.setDeptName("评价");
                // 提成日期:(签发提成的提成日期就是签发日期)
                commissionEntity1.setCommissionDate(performanceNodeVo.getReportIssue());
                commissionEntity1.setPType(projectEntity.getType());  // pType: 项目类型
                commissionService.save(commissionEntity1); // 在co_commission中保存签发提成！
            }

        }

        return true;
    }

    /**
     * 归档提成！
     */
    public Boolean getFilingFees(PerformanceNodeVo performanceNodeVo){
        ProjectEntity projectEntity = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", performanceNodeVo.getProjectId()));
        SysUserEntity sysUserEntity1 = sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("user_id", projectEntity.getChargeId()));
        if (sysUserEntity1 != null && sysUserEntity1.getSubjection().equals("杭州安联")){
            GradePointEntity gradePointEntity = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", performanceNodeVo.getProjectId()));
            if (gradePointEntity == null){
                gradePointEntity = new GradePointEntity();
                gradePointEntity.setProjectId(performanceNodeVo.getProjectId());
                gradePointMapper.insert(gradePointEntity);
            }
            // 报告签发日期
            Date date = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", performanceNodeVo.getProjectId())).getReportIssue();
            BigDecimal totalPerformance = (gradePointEntity.getTotalPerformance() == null) ? BigDecimal.ZERO : gradePointEntity.getTotalPerformance(); // 总绩效
            BigDecimal commissionIssue = (date == null) ? BigDecimal.ZERO : totalPerformance.multiply(new BigDecimal(0.7)); // 签发提成
            /*  归档提成：按提成类型，保存到co_commission   */
            // 根据项目id，得到报告归档日期: reportFiling , 截取年份 , 判断
            BigDecimal filingFees = null;  // 归档提成
            PerCommissionEntity perCommissionEntity = commissionService.getByPlanId(projectEntity.getId(),"报告提成");
            // 报告归档日期
            Date dateB = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", performanceNodeVo.getProjectId())).getReportFiling();
            if (date == null && dateB == null){
                if (perCommissionEntity!=null){
                    perCommissionEntity.setCmsAmount(BigDecimal.ZERO);
                    commissionService.updateById(perCommissionEntity);
                }

            }
            Calendar c = Calendar.getInstance();
//            System.out.println("11111111111111"+c);
            if (null == c || null == date) {return false;} // 归档日期或签发日期为空，无法生成提成!
            c.setTime(date);
            int year = c.get(Calendar.YEAR);  // 年份

            PerCommissionEntity commissionEntity = null;
            if (perCommissionEntity == null){
                commissionEntity = new PerCommissionEntity();
                commissionEntity.setProjectId(performanceNodeVo.getProjectId());
                commissionEntity.setType("报告提成");
                commissionEntity.setPersonnel(projectEntity.getCharge());
                commissionEntity.setSubjection(sysUserEntity1.getSubjection());
                commissionEntity.setDeptId(sysUserEntity1.getDeptId());
                commissionEntity.setDeptName("评价");
                // 提成日期：（归档提成的提成日期就是报告归档日期，由质控提供 al_project_date.report_filing）
                commissionEntity.setCommissionDate(performanceNodeVo.getReportFiling());
                commissionEntity.setPType(projectEntity.getType());
            } else {
                commissionEntity = perCommissionEntity;
                commissionEntity.setPersonnel(projectEntity.getCharge());
                commissionEntity.setSubjection(sysUserEntity1.getSubjection());
                commissionEntity.setDeptId(sysUserEntity1.getDeptId());
                // 提成日期：（归档提成的提成日期就是报告归档日期，由质控提供 al_project_date.report_filing）
                commissionEntity.setCommissionDate(performanceNodeVo.getReportFiling());
            }

            if (year >= 2022){
                filingFees = totalPerformance.subtract(commissionIssue); // 计算归档提成：2022年 ===> （总绩效 - 签发提成）！
                commissionEntity.setCmsAmount(filingFees);
                commissionService.saveOrUpdate(commissionEntity);  // 保存2022年份归档提成
            }

            if (year == 2021){ // 归档提成：2021年
                // ①计算个人累计金额
                BigDecimal sum = BigDecimal.ZERO; // 累计金额（项目净值的累计）
                String userName = null;
                // 提成人姓名
                String charge = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", gradePointEntity.getProjectId())).getCharge();
                PersonnelIndicatorEntity personnelIndicator = personnelIndicatorMapper.selectOne(new QueryWrapper<PersonnelIndicatorEntity>().eq(StrUtil.isNotBlank(charge), "user_name", charge));
                BigDecimal netvalue = projectService.getBaseMapper().selectById(performanceNodeVo.getProjectId()).getNetvalue();
                // 该提成人21年度指标
                BigDecimal performanceIndicators = personnelIndicator.getPerformanceIndicators();
                // 求出此人21年累计金额：净值的累计！ 先得到21年项目列表list ---> 遍历 ---> 净值求和
//                List<ProjectEntity> projectEntityList = projectService.list(newCommission QueryWrapper<ProjectEntity>()
//                        .eq(StrUtil.isNotBlank(charge), "charge", charge)
//                        .in("type", "现状", "控评", "预评", "专篇")
//                        .apply("MID(identifier,3,2) = '21'"));
//                for (ProjectEntity projectEntityA : projectEntityList) {
//                    BigDecimal netvalue = projectEntityA.getNetvalue();
//                    sum = sum.add(netvalue);
//                }
                BigDecimal aggregateAmount = personnelIndicator.getAggregateAmount();
                sum = aggregateAmount.add(netvalue);

                // 累计/指标：比例区间范围
                // newCommission BigDecimal(16).divide(newCommission BigDecimal(233), 2, BigDecimal.ROUND_DOWN);  保留两位小数，多余的舍去！
                BigDecimal percent = sum.divide(performanceIndicators,2, BigDecimal.ROUND_DOWN);
                int retValue1 = percent.compareTo(new BigDecimal(0.6));
                int retValue2 = percent.compareTo(new BigDecimal(0.8));
                int retValue3 = percent.compareTo(new BigDecimal(1.0));


                // ②计算归档提成
                if (retValue1 <= 0){
                    filingFees = new BigDecimal(0.065).multiply(netvalue).multiply(new BigDecimal(0.3));  // 6.5% × 合同净值 × 30%
                }else if (retValue2 <= 0){
                    filingFees = new BigDecimal(0.08).multiply(netvalue).multiply(new BigDecimal(0.3));  // 8% × 合同净值
                }else if (retValue3 <= 0){
                    filingFees = new BigDecimal(0.1).multiply(netvalue).multiply(new BigDecimal(0.3));  // 10% × 合同净值
                }else{
                    filingFees = new BigDecimal(0.14).multiply(netvalue).multiply(new BigDecimal(0.3));  // 14% × 合同净值
                }

                // 保存累计金额: co_personnel_indicator
                PersonnelIndicatorEntity indicatorEntity = new PersonnelIndicatorEntity();
                indicatorEntity.setAggregateAmount(sum);
                personnelIndicatorMapper.update(indicatorEntity , new UpdateWrapper<PersonnelIndicatorEntity>().eq("user_name",charge));

                commissionEntity.setCmsAmount(filingFees);
                commissionService.saveOrUpdate(commissionEntity);

            }
            if (year <= 2020){
                // 归档提成：2020年及以前     注：2020年合同净值低于20000默认提成为540 ，否则 2.4% × 净值！
                BigDecimal netvalue = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("project_id", performanceNodeVo.getProjectId())).getNetvalue();
                // BigDecimal比较大小用compareTo()方法，返回结果=-1,表示小于; =0,表示等于; =1,表示大于：netvalue.compareTo(newCommission BigDecimal(20000))
                if (netvalue.compareTo(new BigDecimal(20000)) == -1){
                    filingFees = new BigDecimal(540);
                }else{
                    filingFees = netvalue.multiply(new BigDecimal(0.024));
                }
                commissionEntity.setCmsAmount(filingFees);
                commissionService.saveOrUpdate(commissionEntity);  // 保存2020年份及之前的归档提成
            }
        }
        return true;
    }

    // 项目提成脚本：评价报告提成！
    @Override
    public void getCommission(List<Long> projectIds) {

        if (projectIds.size() > 0){
            for (Long projectId : projectIds) {
                /** 计算提成：
                 *    commissionIssue 签发提成 = 总绩效 × 70% （不论哪一年都按此公式计算！）
                 *    filingFees 归档提成 = 总绩效 × 30% （2022、2021、2020年及之前，分为三类）
                 */
                ProjectEntity projectEntity = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", projectId));
                SysUserEntity sysUserEntity = sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("user_id", projectEntity.getChargeId()));
                // 报告签发日期
                Date date = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", projectId)).getReportIssue();

                /*  归档提成：按提成类型，保存到co_commission   */
                // 根据项目id，得到签发日期: reportIssue , 截取年份 , 判断
                BigDecimal filingFees = null;  // 归档提成
                PerCommissionEntity perCommissionEntity = commissionService.getByPlanId(projectEntity.getId(),"报告提成");

                // 报告归档日期
                Date dateB = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", projectId)).getReportFiling();
                if (date == null && dateB == null){
                    perCommissionEntity.setCmsAmount(BigDecimal.ZERO);
                    commissionService.updateById(perCommissionEntity);
                }
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(Calendar.YEAR);  // 年份

                BigDecimal totalPerformance = null;
                BigDecimal commissionIssue = null;
                BigDecimal referPerformance = null;
                if (year == 2022){
                    totalPerformance = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", projectId)).getTotalPerformance();
                    referPerformance = gradePointMapper.selectOne(new QueryWrapper<GradePointEntity>().eq("project_id", projectId)).getReferPerformance();
                    // 签发提成
                    commissionIssue = (date == null) ? BigDecimal.ZERO : totalPerformance.multiply(new BigDecimal(0.7));
                }

                if (perCommissionEntity != null){
                    if (year == 2022){
                        filingFees = totalPerformance.multiply(new BigDecimal("0.3")); // 计算归档提成：2022年 ===> 总绩效 * 30%
                        perCommissionEntity.setCmsAmount(filingFees);
                        commissionService.updateById(perCommissionEntity);  // 保存2022年份归档提成
                    }
                    if (year == 2021){ // 归档提成：2021年
                        // ①计算个人累计金额
                        BigDecimal sum = BigDecimal.ZERO; // 累计金额（项目净值的累计）
                        // 提成人姓名
                        String charge = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", projectId)).getCharge();
                        PersonnelIndicatorEntity personnelIndicator = personnelIndicatorMapper.selectOne(new QueryWrapper<PersonnelIndicatorEntity>().eq(StrUtil.isNotBlank(charge), "user_name", charge));
                        BigDecimal aggregateAmount = personnelIndicator.getAggregateAmount();
                        // 该提成人21年度指标：目标金额
                        BigDecimal performanceIndicators = personnelIndicator.getPerformanceIndicators();
                        // 项目净值
                        BigDecimal netvalue = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", projectId)).getNetvalue();
                        sum = netvalue.add(aggregateAmount);

                        // 累计/指标：比例区间范围
                        // newCommission BigDecimal(16).divide(newCommission BigDecimal(233), 2, BigDecimal.ROUND_DOWN);  保留两位小数，多余的舍去！
                        BigDecimal percent = sum.divide(performanceIndicators,3, BigDecimal.ROUND_DOWN);
                        int retValue1 = percent.compareTo(new BigDecimal(0.6));  // <= 0
                        int retValue2 = percent.compareTo(new BigDecimal(0.8));
                        int retValue3 = percent.compareTo(new BigDecimal(1.0));

                        // ②计算归档提成
                        if (retValue1 <= 0){
                            filingFees = new BigDecimal(0.065).multiply(netvalue).multiply(new BigDecimal(0.3));  // 6.5% × 合同净值 × 30%
                        }else if (retValue2 <= 0){
                            BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("0.6")); // 目标金额 * 60%
                            if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                                filingFees = new BigDecimal(0.08).multiply(netvalue).multiply(new BigDecimal(0.3));  // 8% × 合同净值
                            }else { // 卡点了
                                BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.08 计算提成
                                BigDecimal filingFees1 = new BigDecimal("0.08").multiply(overMoney).multiply(new BigDecimal("0.3"));
                                BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.065")).multiply(new BigDecimal("0.3"));
                                filingFees = filingFees1.add(fillingFees2);
                            }

                        }else if (retValue3 <= 0){
                            BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("0.8")); // 目标金额 * 80%
                            if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                                filingFees = new BigDecimal(0.1).multiply(netvalue).multiply(new BigDecimal(0.3));  // 10% × 合同净值
                            }else { // 卡点了
                                BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.1 计算提成
                                BigDecimal filingFees1 = new BigDecimal("0.1").multiply(overMoney).multiply(new BigDecimal("0.3"));
                                BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.08")).multiply(new BigDecimal("0.3"));
                                filingFees = filingFees1.add(fillingFees2);
                            }

                        }else{
                            BigDecimal m1 = performanceIndicators.multiply(new BigDecimal("1")); // 目标金额 * 100%
                            if (sum.subtract(m1).compareTo(netvalue) == 1 || sum.subtract(m1).compareTo(netvalue) == 0){
                                filingFees = new BigDecimal(0.14).multiply(netvalue).multiply(new BigDecimal(0.3));  // 14% × 合同净值
                            }else { // 卡点了
                                BigDecimal overMoney = sum.subtract(m1); // 跨点的部分金额按照 * 0.14 计算提成
                                BigDecimal filingFees1 = new BigDecimal("0.14").multiply(overMoney).multiply(new BigDecimal("0.3"));
                                BigDecimal fillingFees2 = netvalue.subtract(overMoney).multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.3"));
                                filingFees = filingFees1.add(fillingFees2);
                            }

                        }

                        // 保存累计金额（项目净值的累计）: co_personnel_indicator
                        PersonnelIndicatorEntity indicatorEntity = new PersonnelIndicatorEntity();
                        indicatorEntity.setAggregateAmount(sum);
                        personnelIndicatorMapper.update(indicatorEntity , new UpdateWrapper<PersonnelIndicatorEntity>().eq("user_name",charge));

                        perCommissionEntity.setCmsAmount(filingFees);
                        commissionService.updateById(perCommissionEntity);

                    }
                    if (year <= 2020){
                        // 归档提成：2020年及以前     注：2020年合同净值低于20000默认提成为540 ，否则 2.4% × 净值！
                        BigDecimal netvalue = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("project_id",projectId)).getNetvalue();
                        // BigDecimal比较大小用compareTo()方法，返回结果=-1,表示小于; =0,表示等于; =1,表示大于：netvalue.compareTo(newCommission BigDecimal(20000))
                        if (netvalue.compareTo(new BigDecimal(20000)) == -1){
                            filingFees = new BigDecimal(540);
                        }else{
                            filingFees = netvalue.multiply(new BigDecimal(0.024));
                        }
                        perCommissionEntity.setCmsAmount(filingFees);
                        commissionService.updateById(perCommissionEntity);  // 保存2020年份及之前的归档提成
                    }

                }

            }
        }

    }


}
