package may.yuntian.anlianwage.service.impl;

import cn.hutool.core.date.DateTime;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.ProjectUserEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.service.ProjectUserService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.QueryProjectVo;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.entity.PerformanceEntity;
import may.yuntian.anlianwage.entity.TaskEntity;
import may.yuntian.anlianwage.service.PerCommissionService;
import may.yuntian.anlianwage.service.PerformanceService;
import may.yuntian.anlianwage.service.TaskService;
import may.yuntian.anlianwage.mapper.untils.ComissionMathUntils;
import may.yuntian.anlianwage.vo.PerformanceAllocationNewVO;
import may.yuntian.anlianwage.vo.PerformanceAllocationVo;
import may.yuntian.anlianwage.vo.PerformanceNodeVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlianwage.mapper.PerformanceAllocationMapper;
import may.yuntian.anlianwage.entity.PerformanceAllocationEntity;
import may.yuntian.anlianwage.service.PerformanceAllocationService;

/**
 * 绩效分配表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@SuppressWarnings("all")
@Service("performanceAllocationService")
public class PerformanceAllocationServiceImpl extends ServiceImpl<PerformanceAllocationMapper, PerformanceAllocationEntity> implements PerformanceAllocationService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private PerCommissionService perCommissionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;//角色与部门对应关系
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private TaskService taskService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = queryWrapperByParams(params);

        IPage<PerformanceAllocationEntity> page = this.page(
                new Query<PerformanceAllocationEntity>().getPage(params),
                queryWrapper
        );


        return new PageUtils(page);
    }


    /**
     * 绩效分配分页列表
     * @param params
     * @return
     */
    public List<PerformanceAllocationVo> getPageList(Map<String, Object> params){
        //数据权限控制
//        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
//        Long deptId = sysUserEntity.getDeptId();//登录用户部门ID
//        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = queryWrapperByParams(params);
//        queryWrapper.in("dept_id", roleDeptIds);//部门权限控制,只根据数据权限显示数据，不根据归属部门
        pageUtil2.startPage();
        List<PerformanceAllocationVo> performanceAllocationVoList = baseMapper.getListByQurryWrapper(queryWrapper);

        performanceAllocationVoList.forEach(action->{
            List<String> nameList = new ArrayList<>();
            String commissionMoney;
            if (action.getTypes().equals("采样提成")){
                List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(action.getId(),action.getProjectId(),action.getTypes());
                List<PerCommissionEntity> zuzhangList = commissionEntityList.stream().filter(i->i.getHumenType().equals(2)).collect(Collectors.toList());
                List<PerCommissionEntity> zuyuanList = commissionEntityList.stream().filter(i->i.getHumenType().equals(3)).collect(Collectors.toList());
                if (zuzhangList.size()>0){
                    String s = zuzhangList.get(0).getPersonnel()+"(组长):"+zuzhangList.get(0).getCmsAmount();
                    nameList.add(s);
                }
                if (zuyuanList.size()>0){
                    for(PerCommissionEntity com:zuyuanList){
                        String s = com.getPersonnel()+"(组员):"+com.getCmsAmount();
                        nameList.add(s);
                    }
                }
                commissionMoney = StringUtils.join(nameList,"、");
                action.setCommissionMoney(commissionMoney);
            }else {
                List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(action.getId(),action.getProjectId(),action.getTypes());
                if (commissionEntityList.size()>0){
                    action.setCommissionMoney(commissionEntityList.get(0).getPersonnel()+"(负责人):"+commissionEntityList.get(0).getCmsAmount());
                }
            }
        });
        return performanceAllocationVoList;

    }

    /**
     * 导出
     * @param params
     * @return
     */
    public List<PerformanceAllocationVo> export(Map<String, Object> params){
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = queryWrapperByParams(params);

        List<PerformanceAllocationVo> performanceAllocationVoList = baseMapper.getListByQurryWrapper(queryWrapper);

        performanceAllocationVoList.forEach(action->{
            List<String> nameList = new ArrayList<>();
            String commissionMoney;
            if (action.getTypes().equals("采样提成")){
                List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(action.getId(),action.getProjectId(),action.getTypes());
                List<PerCommissionEntity> zuzhangList = commissionEntityList.stream().filter(i->i.getHumenType().equals(2)).collect(Collectors.toList());
                List<PerCommissionEntity> zuyuanList = commissionEntityList.stream().filter(i->i.getHumenType().equals(3)).collect(Collectors.toList());
                if (zuzhangList.size()>0){
                    String s = zuzhangList.get(0).getPersonnel()+"(组长):"+zuzhangList.get(0).getCmsAmount();
                    nameList.add(s);
                }
                if (zuyuanList.size()>0){
                    for(PerCommissionEntity com:zuyuanList){
                        String s = com.getPersonnel()+"(组员):"+com.getCmsAmount();
                        nameList.add(s);
                    }
                }
                commissionMoney = StringUtils.join(nameList,"、");
                action.setCommissionMoney(commissionMoney);
            }else {
                List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(action.getId(),action.getProjectId(),action.getTypes());
                if (commissionEntityList.size()>0){
                    action.setCommissionMoney(commissionEntityList.get(0).getPersonnel()+"(负责人):"+commissionEntityList.get(0).getCmsAmount());
                }
            }
        });
        return performanceAllocationVoList;
    }



    /**
     * 根据项目ID和提成类型获取信息
     * @param projectId
     * @param types
     * @return
     */
    public PerformanceAllocationEntity getByProjectIdAndTypes(Long projectId, String types){
        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectOne(new QueryWrapper<PerformanceAllocationEntity>()
            .eq("types",types)
            .eq("project_id",projectId)
        );
        return performanceAllocationEntity;
    }


    /**
     * 签发提成--检评
     * @param performanceNodeVo
     */
    public void issueCommission(PerformanceNodeVo performanceNodeVo){
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);

        BigDecimal netvalue = projectEntity.getNetvalue();
        BigDecimal commissionMoney;//提成金额
        BigDecimal yearDeep;//年底提成
        SysUserEntity sysUserEntity = sysUserService.getById(projectEntity.getChargeId());
        if (sysUserEntity!=null&&"杭州安联".equals(sysUserEntity.getSubjection())){
//            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            if (1 == projectEntity.getIncludedOutput()){ //判断是否计入产出
                PerformanceEntity performanceEntity = performanceService.getByUserid(projectEntity.getChargeId());
                if(performanceEntity!=null) {

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//人员产出累计金额
                    BigDecimal leiji = cumulativeOutput.add(netvalue);
                    BigDecimal outPut = netvalue;
//                    BigDecimal bili = leiji.divide(mubiao,2,BigDecimal.ROUND_HALF_UP); //人员指标额的比例
                    performanceEntity.setCumulativeOutput(leiji);

                    Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"签发");
                    commissionMoney = map.get("commissionMoney");
                    yearDeep = map.get("yearDeep");

                    //查询是否已生成一条绩效分配记录
                    PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId, "签发提成");
                    if (performanceAllocationEntity == null) {
                        performanceService.updateById(performanceEntity);
                        PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                        performanceAllocation.setPerformanceMoney(commissionMoney);
                        performanceAllocation.setProjectId(projectId);
                        performanceAllocation.setPerformanceDate(performanceNodeVo.getReportIssue());
                        performanceAllocation.setTypes("签发提成");
                        this.save(performanceAllocation);
                        PerCommissionEntity perCommissionEntity = new PerCommissionEntity();
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                        perCommissionEntity.setProjectId(projectId);
                        perCommissionEntity.setPType(projectEntity.getType());
                        perCommissionEntity.setPersonnel(projectEntity.getCharge());
                        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
                        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
                        perCommissionEntity.setDeptName("检评");
                        perCommissionEntity.setCmsAmount(commissionMoney);
                        perCommissionEntity.setCommissionDate(performanceNodeVo.getReportIssue());
                        perCommissionEntity.setType("签发提成");
                        perCommissionService.save(perCommissionEntity);
                        PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
                        perCommissionEntity1.setPerformanceAllocationId(performanceAllocation.getId());
                        perCommissionEntity1.setProjectId(projectId);
                        perCommissionEntity1.setPType(projectEntity.getType());
                        perCommissionEntity1.setPersonnel(projectEntity.getCharge());
                        perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
                        perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
                        perCommissionEntity1.setDeptName("检评");
                        perCommissionEntity1.setCmsAmount(yearDeep);
                        perCommissionEntity1.setCommissionDate(performanceNodeVo.getReportIssue());
                        perCommissionEntity1.setType("签发年底提成");
                        perCommissionService.save(perCommissionEntity1);
                    }
                }
            }else {
                PerformanceEntity performanceEntity = performanceService.getByUserid(projectEntity.getChargeId());
                if (performanceEntity!=null) {

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//人员产出累计金额

                    //计算累计产出比例后 计算出提成
                    Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,netvalue,"签发");
                    commissionMoney = map.get("commissionMoney");
                    yearDeep = map.get("yearDeep");

                    //查询是否已生成一条绩效分配记录
                    PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId, "签发提成");
                    if (performanceAllocationEntity == null) {
                        PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                        performanceAllocation.setPerformanceMoney(commissionMoney);
                        performanceAllocation.setProjectId(projectId);
                        performanceAllocation.setPerformanceDate(performanceNodeVo.getReportIssue());
                        performanceAllocation.setTypes("签发提成");
                        this.save(performanceAllocation);
                        PerCommissionEntity perCommissionEntity = new PerCommissionEntity();
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                        perCommissionEntity.setProjectId(projectId);
                        perCommissionEntity.setPType(projectEntity.getType());
                        perCommissionEntity.setPersonnel(projectEntity.getCharge());
                        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
                        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
                        perCommissionEntity.setDeptName("检评");
                        perCommissionEntity.setCmsAmount(commissionMoney);
                        perCommissionEntity.setCommissionDate(performanceNodeVo.getReportIssue());
                        perCommissionEntity.setType("签发提成");
                        perCommissionService.save(perCommissionEntity);
                        PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
                        perCommissionEntity1.setPerformanceAllocationId(performanceAllocation.getId());
                        perCommissionEntity1.setProjectId(projectId);
                        perCommissionEntity1.setPType(projectEntity.getType());
                        perCommissionEntity1.setPersonnel(projectEntity.getCharge());
                        perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
                        perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
                        perCommissionEntity1.setDeptName("检评");
                        perCommissionEntity1.setCmsAmount(yearDeep);
                        perCommissionEntity1.setCommissionDate(performanceNodeVo.getReportIssue());
                        perCommissionEntity1.setType("签发年底提成");
                        perCommissionService.save(perCommissionEntity1);
                    }
                }
            }
        }

    }

    /**
     * 归档提成--检评
     * @param performanceNodeVo
     */
    public void fillingCommission(PerformanceNodeVo performanceNodeVo){
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);

        BigDecimal netvalue = projectEntity.getNetvalue();
        BigDecimal commissionMoney;
        SysUserEntity sysUserEntity = sysUserService.getById(projectEntity.getChargeId());
        if (sysUserEntity!=null&&"杭州安联".equals(sysUserEntity.getSubjection())){
//            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
//            if (1 == projectEntity.getIncludedOutput()){ //判断是否计入产出
//                PerformanceEntity performanceEntity = performanceService.getByUserid(projectEntity.getChargeId());
//                if (performanceEntity!=null){
//
//
//                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//人员产出目标金额
//                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//人员产出累计金额
//                    BigDecimal leiji = cumulativeOutput.add(netvalue);
//                    BigDecimal outPut = netvalue;
////                    BigDecimal bili = leiji.divide(mubiao,2,BigDecimal.ROUND_HALF_UP); //人员指标额的比例
//                    performanceEntity.setCumulativeOutput(leiji);
//
//                    Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"报告");
//                    commissionMoney = map.get("commissionMoney");
//
//                    //查询是否已生成一条绩效分配记录
//                    PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId,"报告提成");
//                    if (performanceAllocationEntity==null){
//                        performanceService.updateById(performanceEntity);
//                        PerformanceAllocationEntity performanceAllocation = newCommission PerformanceAllocationEntity();
//                        performanceAllocation.setPerformanceMoney(commissionMoney);
//                        performanceAllocation.setProjectId(projectId);
//                        performanceAllocation.setPerformanceDate(performanceNodeVo.getReportFiling());
//                        performanceAllocation.setTypes("报告提成");
//                        this.save(performanceAllocation);
//                        PerCommissionEntity perCommissionEntity = newCommission PerCommissionEntity();
//                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
//                        perCommissionEntity.setProjectId(projectId);
//                        perCommissionEntity.setPType(projectEntity.getType());
//                        perCommissionEntity.setPersonnel(projectEntity.getCharge());
//                        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
//                        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
//                        perCommissionEntity.setDeptName("检评");
//                        perCommissionEntity.setCmsAmount(commissionMoney);
//                        perCommissionEntity.setCommissionDate(performanceNodeVo.getReportFiling());
//                        perCommissionEntity.setType("报告提成");
//                        perCommissionService.save(perCommissionEntity);
//                    }
//                }
//            }else {
                PerformanceEntity performanceEntity = performanceService.getByUserid(projectEntity.getChargeId());
                if (performanceEntity != null) {

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//人员产出累计金额
                    //计算累计产出比例后 计算出提成
//                    Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,netvalue,"报告");
//                    commissionMoney = map.get("commissionMoney");
                    PerformanceAllocationEntity performanceAllocationEntity1 = this.getByProjectIdAndTypes(projectId, "签发提成");
                    if (performanceAllocationEntity1!=null){
                        BigDecimal issue = performanceAllocationEntity1.getPerformanceMoney();
                        commissionMoney = issue.divide(new BigDecimal("0.5"),3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("0.3"));
                    }else {
                        commissionMoney = BigDecimal.ZERO;
                    }
                    //查询是否已生成一条绩效分配记录
                    PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId, "报告提成");
                    if (performanceAllocationEntity == null) {
                        PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                        performanceAllocation.setPerformanceMoney(commissionMoney);
                        performanceAllocation.setProjectId(projectId);
                        performanceAllocation.setPerformanceDate(performanceNodeVo.getReportFiling());
                        performanceAllocation.setTypes("报告提成");
                        this.save(performanceAllocation);
                        PerCommissionEntity perCommissionEntity = new PerCommissionEntity();
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                        perCommissionEntity.setProjectId(projectId);
                        perCommissionEntity.setPType(projectEntity.getType());
                        perCommissionEntity.setPersonnel(projectEntity.getCharge());
                        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
                        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
                        perCommissionEntity.setDeptName("检评");
                        perCommissionEntity.setCmsAmount(commissionMoney);
                        perCommissionEntity.setCommissionDate(performanceNodeVo.getReportFiling());
                        perCommissionEntity.setType("报告提成");
                        perCommissionService.save(perCommissionEntity);
//                    }
                }
            }
        }

    }

    /**
     * 评价采样提成
     * @param performanceNodeVo
     */
    public void caiyangPjCommission(PerformanceNodeVo performanceNodeVo){
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);
//        System.out.println("评价采样提成:"+performanceNodeVo);
        BigDecimal netvalue = projectEntity.getNetvalue();
        BigDecimal commissionMoney = BigDecimal.ZERO;//提成金额
        BigDecimal yearDeep = BigDecimal.ZERO;//年底提成
        BigDecimal zongticheng = BigDecimal.ZERO;
//        Map<String,BigDecimal> commissionMoneyMap = newCommission HashMap<>();
//        Map<String,BigDecimal> yearDeepMoneyMap = newCommission HashMap<>();
        List<PerformanceEntity> performanceEntityList = new ArrayList<>();
        List<PerCommissionEntity> perCommissionEntityList = new ArrayList<>();
        List<ProjectUserEntity> headmanList = projectUserService.getListByType(4,projectId);//获取项目采样组长
        List<ProjectUserEntity> zuyuanList = projectUserService.getListByType(1,projectId);//获取项目采样组员

        SysUserEntity sysUserEntity2 = new SysUserEntity();
        if (headmanList.size()>0){
            sysUserEntity2 = sysUserService.getById(headmanList.get(0).getUserId());
        }else if(zuyuanList.size()>0){
            sysUserEntity2 = sysUserService.getById(zuyuanList.get(0).getUserId());
        }
        if (sysUserEntity2!=null&&"杭州安联".equals(sysUserEntity2.getSubjection())) {
//            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity2.getDeptId());

            //比较日期大小 靠后的为提成日期
            Date commissionDate;
            if (performanceNodeVo.getIsTime() != null && performanceNodeVo.getIsTime() == 2) {
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())) {
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
            } else if (performanceNodeVo.getIsTime() != null && performanceNodeVo.getIsTime() == 3) {
                commissionDate = performanceNodeVo.getPhysicalAcceptDate();
            } else {
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())) {
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
                if (commissionDate.before(performanceNodeVo.getPhysicalAcceptDate())) {
                    commissionDate = performanceNodeVo.getPhysicalAcceptDate();
                }
            }


            //查询是否已生成一条绩效分配记录
            PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId, "采样提成");
            if(1 == projectEntity.getIncludedOutput()){
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长

                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员目前累计金额

                    if (zuyuanList.size()>0){
                        BigDecimal outPut = netvalue.multiply(new BigDecimal("0.60"));
                        BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组长人员产出累计金额

                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"评价采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");

                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"评价");

                    }else {
                        BigDecimal headmanLeiji =cumulativeOutput.add(netvalue);//采样组长人员产出累计金额 .multiply(newCommission BigDecimal("0.60"))
                        BigDecimal outPut = netvalue;
                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"评价采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"评价");

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"评价采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"评价");

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.divide(new BigDecimal(zuyuanList.size()));
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"评价采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"评价");

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    performanceService.updateBatchById(performanceEntityList);
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }

            }else {
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长
                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());
                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员产出累计金额


                    if(zuyuanList.size()>0){
                        BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.60"));
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"评价采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"评价");

                    }else {
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"评价");

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"评价采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"评价");

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.divide(new BigDecimal(zuyuanList.size()));

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"评价采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"评价");

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }
            }
        }
    }


    /**
     * 检评采样提成
     * @param performanceNodeVo
     */
    public void caiyangZjCommission(PerformanceNodeVo performanceNodeVo){
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);

        BigDecimal netvalue = projectEntity.getNetvalue();
        BigDecimal commissionMoney = BigDecimal.ZERO;//提成金额
        BigDecimal yearDeep = BigDecimal.ZERO;//年底提成
        BigDecimal zongticheng = BigDecimal.ZERO;
        List<PerformanceEntity> performanceEntityList = new ArrayList<>();
        List<PerCommissionEntity> perCommissionEntityList = new ArrayList<>();
        List<ProjectUserEntity> headmanList = projectUserService.getListByType(4,projectId);//获取项目采样组长
        List<ProjectUserEntity> zuyuanList = projectUserService.getListByType(1,projectId);//获取项目采样组员

        SysUserEntity sysUserEntity2 = new SysUserEntity();
        if (headmanList.size()>0){
            sysUserEntity2 = sysUserService.getById(headmanList.get(0).getUserId());
        }else if(zuyuanList.size()>0){
            sysUserEntity2 = sysUserService.getById(zuyuanList.get(0).getUserId());
        }
        if (sysUserEntity2!=null&&"杭州安联".equals(sysUserEntity2.getSubjection())){
            //比较日期大小 靠后的为提成日期
            Date commissionDate ;
            if(performanceNodeVo.getIsTime()!=null&&performanceNodeVo.getIsTime()==2){
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())){
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
            }else if (performanceNodeVo.getIsTime()!=null&&performanceNodeVo.getIsTime()==3){
                commissionDate = performanceNodeVo.getPhysicalAcceptDate();
            }else {
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())){
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
                if (commissionDate.before(performanceNodeVo.getPhysicalAcceptDate())){
                    commissionDate = performanceNodeVo.getPhysicalAcceptDate();
                }
            }
            //查询是否已生成一条绩效分配记录
            PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId,"采样提成");
//("职卫监督").equals(projectEntity.getType())
            if(1 == projectEntity.getIncludedOutput()){
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长

                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员目前累计金额

                    if (zuyuanList.size()>0){
                        BigDecimal outPut = netvalue.multiply(new BigDecimal("0.60"));
                        BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组长人员产出累计金额

                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");

                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"检评");

                    }else {
                        BigDecimal headmanLeiji =cumulativeOutput.add(netvalue);//采样组长人员产出累计金额 .multiply(newCommission BigDecimal("0.60"))
                        BigDecimal outPut = netvalue;
                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"检评");

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"检评");

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.divide(new BigDecimal(zuyuanList.size()));
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"检评");

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    performanceService.updateBatchById(performanceEntityList);
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }

            }else {
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长
                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());
                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员产出累计金额


                    if(zuyuanList.size()>0){
                        BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.60"));
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"检评");

                    }else {
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2,"检评");

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"检评");

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.divide(new BigDecimal(zuyuanList.size()));

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3,"检评");

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }
            }
        }


    }


    /**
     * 保存提成信息的处理
     * @param performanceAllocationEntity
     * @param projectEntity
     * @param sysUserEntity
     * @param commissionMoney
     * @param yearDeep
     * @param commissionDate
     * @param perCommissionEntityList
     * @param humanType
     * @return
     */
    private List<PerCommissionEntity> newPerCommissionEntity(PerformanceAllocationEntity performanceAllocationEntity,ProjectEntity projectEntity,SysUserEntity sysUserEntity,BigDecimal commissionMoney,BigDecimal yearDeep,Date commissionDate,List<PerCommissionEntity> perCommissionEntityList,Integer humanType,String deptName ){
        PerCommissionEntity perCommissionEntity = new PerCommissionEntity();
        perCommissionEntity.setPerformanceAllocationId(performanceAllocationEntity==null?null:performanceAllocationEntity.getId());
        perCommissionEntity.setProjectId(projectEntity.getId());
        perCommissionEntity.setPType(projectEntity.getType());
        perCommissionEntity.setPersonnel(sysUserEntity.getUsername());
        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
        perCommissionEntity.setDeptName(deptName);
        perCommissionEntity.setCmsAmount(commissionMoney);
        perCommissionEntity.setHumenType(humanType);
        perCommissionEntity.setCommissionDate(commissionDate);
        perCommissionEntity.setType("采样提成");
        perCommissionEntityList.add(perCommissionEntity);
        PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
        perCommissionEntity1.setPerformanceAllocationId(performanceAllocationEntity==null?null:performanceAllocationEntity.getId());
        perCommissionEntity1.setProjectId(projectEntity.getId());
        perCommissionEntity1.setPType(projectEntity.getType());
        perCommissionEntity1.setPersonnel(sysUserEntity.getUsername());
        perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
        perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
        perCommissionEntity1.setDeptName(deptName);
        perCommissionEntity1.setCmsAmount(yearDeep);
        perCommissionEntity1.setCommissionDate(commissionDate);
        perCommissionEntity1.setType("采样年底提成");
        perCommissionEntityList.add(perCommissionEntity1);

        return perCommissionEntityList;
    }



    /**
     * 环境采样提成  todo 环境采样提成 只查询task表中 采样任务
     * @param performanceNodeVo
     */
    public boolean caiyangHjCommission(PerformanceNodeVo performanceNodeVo){
        ProjectEntity projectEntity = projectService.getById(performanceNodeVo.getProjectId());
        TaskEntity taskEntity = taskService.getOneByProjectId(performanceNodeVo.getProjectId(),1);
        SysUserEntity sysUserEntity;
        if (StringUtils.isBlank(taskEntity.getChief())){
            return false;
        }
        String[] charges = taskEntity.getChief().split(":");
        sysUserEntity = sysUserService.getById(Long.valueOf(charges[0]));

        if (sysUserEntity!=null&&"杭州安联".equals(sysUserEntity.getSubjection())){
//            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            if (performanceNodeVo.getPhysicalAcceptDate()!=null){
                List<PerCommissionEntity> perCommissionEntityList = new ArrayList<>();
                String[] huanjingTeam = new String[]{"环境监测","环评监测","环境监督","环境验收","排污许可"};
                String[] gongweiTeam = new String[]{"公共卫生检测","一次性用品用具检测","学校卫生检测","公卫监督","洁净区域检测"};
                BigDecimal netvalue = projectEntity.getNetvalue();
                BigDecimal commissionMoney = BigDecimal.ZERO;
                BigDecimal yearCommission = BigDecimal.ZERO;
                if (Arrays.asList(huanjingTeam).contains(projectEntity.getType())){
                    commissionMoney = netvalue.multiply(new BigDecimal("0.035").multiply(new BigDecimal("0.8")));
                    yearCommission = netvalue.multiply(new BigDecimal("0.035").multiply(new BigDecimal("0.2")));
                }else if (Arrays.asList(gongweiTeam).contains(projectEntity.getType())){
                    commissionMoney = netvalue.multiply(new BigDecimal("0.02").multiply(new BigDecimal("0.8")));
                    yearCommission = netvalue.multiply(new BigDecimal("0.02").multiply(new BigDecimal("0.2")));
                }

                PerCommissionEntity perCommissionEntity = perCommissionService.getByPlanId(taskEntity.getProjectId(),"采样提成");
                if (perCommissionEntity==null){
                    PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
                    perCommissionEntity1.setProjectId(projectEntity.getId());
                    perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
                    perCommissionEntity1.setDeptName("环境");
                    perCommissionEntity1.setPType(projectEntity.getType());
                    perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
                    perCommissionEntity1.setHumenType(2);
                    perCommissionEntity1.setCmsAmount(commissionMoney);
                    perCommissionEntity1.setType("采样提成");
                    perCommissionEntity1.setCommissionDate(performanceNodeVo.getPhysicalAcceptDate());
                    perCommissionEntity1.setPersonnel(sysUserEntity.getUsername());
                    perCommissionEntity1.setPlanId(taskEntity.getId());
                    perCommissionEntityList.add(perCommissionEntity1);
                    PerCommissionEntity perCommissionEntity2 = new PerCommissionEntity();
                    perCommissionEntity2.setProjectId(projectEntity.getId());
                    perCommissionEntity2.setDeptId(sysUserEntity.getDeptId());
                    perCommissionEntity2.setDeptName("环境");
                    perCommissionEntity2.setPType(projectEntity.getType());
                    perCommissionEntity2.setSubjection(sysUserEntity.getSubjection());
                    perCommissionEntity2.setHumenType(2);
                    perCommissionEntity2.setCmsAmount(yearCommission);
                    perCommissionEntity2.setType("采样年底提成");
                    perCommissionEntity2.setCommissionDate(performanceNodeVo.getPhysicalAcceptDate());
                    perCommissionEntity2.setPersonnel(sysUserEntity.getUsername());
                    perCommissionEntity2.setPlanId(taskEntity.getId());
                    perCommissionEntityList.add(perCommissionEntity2);
                }
                perCommissionService.saveBatch(perCommissionEntityList);
            }
        }
        return true;

    }


    /**
     * 环境归档提成  todo 环境归档只查询task表中 咨询任务
     * @param performanceNodeVo
     */
    public void issueHjCommission(PerformanceNodeVo performanceNodeVo){
        String[] huanjing = new String[]{"环境验收","环境应急预案","排污许可证申请","排污许可后管理","环保管家","应急预案","环境示范","排污许可"};
        ProjectEntity projectEntity = projectService.getById(performanceNodeVo.getProjectId());
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(performanceNodeVo.getProjectId());
        TaskEntity taskEntity = taskService.getOneByProjectId(performanceNodeVo.getProjectId(),2);
        if (Arrays.asList(huanjing).contains(projectEntity.getType())){
            List<PerCommissionEntity> perCommissionEntityList = new ArrayList<>();
            BigDecimal netvalue = projectEntity.getNetvalue();
            SysUserEntity sysUserEntity = sysUserService.getById(projectEntity.getChargeId());
//            SimpleDateFormat format = newCommission SimpleDateFormat("yyyy-MM-dd");
            Date date = new DateTime("2022-03-15","yyyy-MM-dd");
            BigDecimal commissionMoney = BigDecimal.ZERO;
            BigDecimal yearMoney = BigDecimal.ZERO;
            if (sysUserEntity!=null&&"杭州安联".equals(sysUserEntity.getSubjection())){
//                SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
                if (projectEntity.getType().equals("环境验收")){
                    if (netvalue.compareTo(new BigDecimal("15000"))== -1){
                        commissionMoney = new BigDecimal("900").multiply(new BigDecimal("0.8"));
                        yearMoney = new BigDecimal("900").multiply(new BigDecimal("0.2"));
                    }else {
                        commissionMoney = netvalue.multiply(new BigDecimal("0.06")).multiply(new BigDecimal("0.8"));
                        yearMoney = netvalue.multiply(new BigDecimal("0.06")).multiply(new BigDecimal("0.2"));
                    }
                }else if (projectEntity.getType().equals("环境应急预案")||projectEntity.getType().equals("应急预案")){
                    if (netvalue.compareTo(new BigDecimal("15000"))== -1){
                        commissionMoney = new BigDecimal("1500").multiply(new BigDecimal("0.8"));
                        yearMoney = new BigDecimal("1500").multiply(new BigDecimal("0.2"));
                    }else {
                        commissionMoney = netvalue.multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.8"));
                        yearMoney = netvalue.multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.2"));
                    }
                }else if (projectEntity.getType().equals("排污许可证申请")||projectEntity.getType().equals("环境示范")){
                    if (netvalue.compareTo(new BigDecimal("6000"))== -1){
                        commissionMoney = new BigDecimal("600").multiply(new BigDecimal("0.8"));
                        yearMoney = new BigDecimal("600").multiply(new BigDecimal("0.2"));
                    }else {
                        commissionMoney = netvalue.multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.8"));
                        yearMoney = netvalue.multiply(new BigDecimal("0.1")).multiply(new BigDecimal("0.2"));
                    }
                }else if (projectEntity.getType().equals("排污许可后管理")||projectEntity.getType().equals("排污许可")){
                    if (projectDateEntity.getSignDate().after(date)){
                        if (netvalue.compareTo(new BigDecimal("6000"))== -1){
                            commissionMoney = new BigDecimal("1200").multiply(new BigDecimal("0.8"));
                            yearMoney = new BigDecimal("1200").multiply(new BigDecimal("0.2"));
                        }else {
                            commissionMoney = netvalue.multiply(new BigDecimal("0.2")).multiply(new BigDecimal("0.8"));
                            yearMoney = netvalue.multiply(new BigDecimal("0.2")).multiply(new BigDecimal("0.2"));
                        }
                    }else {
                        commissionMoney = netvalue.multiply(new BigDecimal("0.06")).multiply(new BigDecimal("0.8"));
                        yearMoney = netvalue.multiply(new BigDecimal("0.06")).multiply(new BigDecimal("0.2"));
                    }

                }else if (projectEntity.getType().equals("环保管家")){
                    if (netvalue.compareTo(new BigDecimal("20000"))== -1){
                        commissionMoney = new BigDecimal("4000").multiply(new BigDecimal("0.8"));
                        yearMoney = new BigDecimal("4000").multiply(new BigDecimal("0.2"));
                    }else {
                        commissionMoney = netvalue.multiply(new BigDecimal("0.2")).multiply(new BigDecimal("0.8"));
                        yearMoney = netvalue.multiply(new BigDecimal("0.2")).multiply(new BigDecimal("0.2"));
                    }
                }
//            SysUserEntity sysUserEntity = sysUserService.getById(Long.valueOf(taskEntity.getChief()));
                PerCommissionEntity perCommissionEntity = perCommissionService.getByPlanId(performanceNodeVo.getProjectId(),"报告提成");
                if (perCommissionEntity==null){
                    PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
                    perCommissionEntity1.setProjectId(projectEntity.getId());
                    perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
                    perCommissionEntity1.setDeptName("环境");
                    perCommissionEntity1.setPType(projectEntity.getType());
                    perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
                    perCommissionEntity1.setHumenType(2);
                    perCommissionEntity1.setCmsAmount(commissionMoney);
                    perCommissionEntity1.setType("报告提成");
                    perCommissionEntity1.setCommissionDate(performanceNodeVo.getReportFiling());
                    perCommissionEntity1.setPersonnel(sysUserEntity.getUsername());
                    perCommissionEntity1.setPlanId(taskEntity==null?null:taskEntity.getId());
                    perCommissionEntityList.add(perCommissionEntity1);
                    PerCommissionEntity perCommissionEntity2 = new PerCommissionEntity();
                    perCommissionEntity2.setProjectId(projectEntity.getId());
                    perCommissionEntity2.setDeptId(sysUserEntity.getDeptId());
                    perCommissionEntity2.setDeptName("环境");
                    perCommissionEntity2.setPType(projectEntity.getType());
                    perCommissionEntity2.setSubjection(sysUserEntity.getSubjection());
                    perCommissionEntity2.setHumenType(2);
                    perCommissionEntity2.setCmsAmount(yearMoney);
                    perCommissionEntity2.setType("报告年底提成");
                    perCommissionEntity2.setCommissionDate(performanceNodeVo.getReportFiling());
                    perCommissionEntity2.setPersonnel(sysUserEntity.getUsername());
                    perCommissionEntity2.setPlanId(taskEntity==null?null:taskEntity.getId());
                    perCommissionEntityList.add(perCommissionEntity2);
                    perCommissionService.saveBatch(perCommissionEntityList);
                }

            }

        }

    }



    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    public PerformanceAllocationEntity getInfo(Long id){
        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectById(id);
        List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(performanceAllocationEntity.getId(),performanceAllocationEntity.getProjectId(),performanceAllocationEntity.getTypes());
        performanceAllocationEntity.setPerCommissionEntityList(commissionEntityList);

        return performanceAllocationEntity;
    }


    /**
     * 提成分配接口-获取
     * @param id
     * @return
     */
    public List<PerCommissionEntity> royaltyDistribution(Long id){
        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectById(id);
        List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(performanceAllocationEntity.getId(),performanceAllocationEntity.getProjectId(),performanceAllocationEntity.getTypes());
        for (PerCommissionEntity perCommissionEntity:commissionEntityList){
            perCommissionEntity.setPerformanceMoney(performanceAllocationEntity.getPerformanceMoney());
        }
        return commissionEntityList;
    }


    /**
     * 提成分配--修改
     * @param commissionEntityList
     * @return
     */
    public int updateCaiYangCommission(List<PerCommissionEntity> commissionEntityList){
        int ret = -1;
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal performanceMoney = BigDecimal.ZERO;
        if (commissionEntityList.size()>0){
            performanceMoney = commissionEntityList.get(0).getPerformanceMoney();
            for(int i=0;i<commissionEntityList.size();i++){
                num = num.add(commissionEntityList.get(i).getCmsAmount());
            }
        } else {
            ret = 0;
        }
        if (num.compareTo(performanceMoney)==-1){//小于总提成
            ret = 1;
        }else if (num.compareTo(performanceMoney)==0){//等于总提成
            ret = 2;
        }else if (num.compareTo(performanceMoney)==1){//大于总提成
            ret = 3;
        }
        return ret;
    }


    /**
     * 采样编辑
     * @param performanceAllocationEntity
     * @return
     */
    public Map<Integer,List<PerCommissionEntity>> getEditInformation(PerformanceAllocationEntity performanceAllocationEntity){
        Map<Integer,List<PerCommissionEntity>> commissionMap = new HashMap<>();
//        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectById(id);
        List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(performanceAllocationEntity.getId(),performanceAllocationEntity.getProjectId(),performanceAllocationEntity.getTypes());
        List<Integer> commissionTimesList = commissionEntityList.stream().map(PerCommissionEntity::getCommissionTimes).distinct().collect(Collectors.toList());
        Map<Integer,List<PerCommissionEntity>> map = commissionEntityList.stream().collect(Collectors.groupingBy(PerCommissionEntity::getCommissionTimes));
        for(Integer commissionTimes:commissionTimesList){
            commissionMap.put(commissionTimes,map.get(commissionTimes));
        }
        return commissionMap;
    }

    /**
     * 采样编辑提交
     * @param commissionEntityList
     */
    public void saveOrUpdateBatchCommission(List<PerCommissionEntity> commissionEntityList){
        ProjectEntity projectEntity = projectService.getById(commissionEntityList.get(0).getProjectId());
        PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(commissionEntityList.get(0).getProjectId(),"采样提成");
        for(PerCommissionEntity perCommissionEntity:commissionEntityList){
            if(perCommissionEntity.getId()==null){
                SysUserEntity sysUserEntity = sysUserService.queryByUserName(perCommissionEntity.getPersonnel());
                perCommissionEntity.setType("采样提成");
                perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
                perCommissionEntity.setPType(projectEntity.getType());
                perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
                perCommissionEntity.setDeptName("检评");
                perCommissionEntity.setPerformanceAllocationId(performanceAllocationEntity.getId());
            }
        }
        perCommissionService.saveOrUpdateBatch(commissionEntityList);
        performanceAllocationEntity.setPerformanceMoney(commissionEntityList.get(0).getPerformanceMoney());
        this.updateById(performanceAllocationEntity);
        List<PerCommissionEntity> perCommissionEntityList = perCommissionService.getListByIdAndType(performanceAllocationEntity.getId(),projectEntity.getId(),"采样年底提成");
        QueryWrapper<PerCommissionEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id",projectEntity.getId())
                .eq("performance_allocation_id",performanceAllocationEntity.getId())
                .eq("type","采样年底提成");
        perCommissionService.remove(queryWrapper);
    }


    /**
     * 统计相关接口
     * @param params
     * @return
     */
    public PerformanceAllocationNewVO statisticalCorrelation(Map<String,Object> params){

        QueryWrapper<PerformanceAllocationEntity> queryWrapper = queryStatisticsByParams(params);
        PerformanceAllocationNewVO map = baseMapper.getMapByQurryWrapper(queryWrapper);
//        if(map==null){
//            map = new HashMap<>();
//            map.put("netvalue",BigDecimal.ZERO);
//            map.put("issueCommission",BigDecimal.ZERO);
//            map.put("fillingCommission",BigDecimal.ZERO);
//        }
        return map;
    }



    /**
     * 查询wrapper
     * @param map
     * @return
     */
    private QueryWrapper queryWrapperByParams(Map<String,Object> map){
        String company = (String)map.get("company");
        String identifier = (String)map.get("identifier");
        String charge = (String)map.get("charge");
        String personnel = (String)map.get("personnel");
        String types = (String)map.get("types");
//        String performanceDate = (String)map.get("performanceDate");
        String startDate = (String)map.get("startDate");
        String endDate = (String)map.get("endDate");
//        String username = (String)map.get("username");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = DateUtils.dateTimeMonthStart();//当前月的第一天
        String performanceDateStart = format.format(date);



        QueryProjectVo queryVo = new QueryProjectVo();
        List<Long> idList = new ArrayList<>();
        if (StringUtils.isNotBlank(company)||StringUtils.isNotBlank(identifier)||StringUtils.isNotBlank(charge)){
            queryVo.setCompany(company);
            queryVo.setIdentifier(identifier);
            queryVo.setCharge(charge);
            idList = projectService.getProjectIdsByParams(queryVo);
        }
        List<Long> ids = new ArrayList<>();
        if(StringUtils.isNotBlank(personnel)){
            ids = perCommissionService.getListByPersonnel(personnel);
//            if (ids.size()>0){
//                idList.addAll(ids);
//            }
        }
//        if (idList.size()>0){
//            idList = idList.stream().distinct().collect(Collectors.toList());
//        }

        QueryWrapper<PerformanceAllocationEntity> queryWrapper = new QueryWrapper<PerformanceAllocationEntity>()
                .eq(StringUtils.isNotBlank(types),"pa.types",types)
//                .eq(StringUtils.isNotBlank(performanceDate),"performance_date",performanceDate)
                .ge(StringUtils.isBlank(endDate),"pa.performance_date",performanceDateStart)
                .between(StringUtils.isNotBlank(endDate),"pa.performance_date",startDate,endDate)
//                .like(StringUtils.isNotBlank(company),"p.company",company)
//                .like(StringUtils.isNotBlank(identifier),"p.identifier",identifier)
//                .eq(StringUtils.isNotBlank(charge),"p.charge",charge)
//                .eq(StringUtils.isNotBlank(personnel),"c.personnel",personnel);
                .in(ids.size()>0,"project_id",ids)
                .in(idList.size()>0,"project_id",idList);
        return queryWrapper;
    }

    /**
     * 查询wrapper
     * @param map
     * @return
     */
    private QueryWrapper queryStatisticsByParams(Map<String,Object> map){
        String charge = (String)map.get("charge");
        String types = (String)map.get("types");
        String startDate = (String)map.get("startDate");
        String endDate = (String)map.get("endDate");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = DateUtils.dateTimeMonthStart();//当前月的第一天
        String performanceDateStart = format.format(date);


        QueryProjectVo queryVo = new QueryProjectVo();
        List<Long> idList = new ArrayList<>();
        if (StringUtils.isNotBlank(charge)){
            queryVo.setCharge(charge);
            idList = projectService.getProjectIdsByParams(queryVo);
        }
        QueryWrapper<PerformanceAllocationEntity> queryWrapper = new QueryWrapper<PerformanceAllocationEntity>()
                .eq(StringUtils.isNotBlank(types),"types",types)
                .ge(StringUtils.isBlank(endDate),"performance_date",performanceDateStart)
                .between(StringUtils.isNotBlank(endDate),"performance_date",startDate,endDate)
                .in(idList.size()>0,"project_id",idList);
        return queryWrapper;
    }


}
