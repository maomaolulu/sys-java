package may.yuntian.anlianwage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.anlian.service.ProjectAmountService;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.vo.QueryProjectVo;
import may.yuntian.anlianwage.entity.PerCommissionEntity;
import may.yuntian.anlianwage.mapper.PerCommissionMapper;
import may.yuntian.anlianwage.service.PerCommissionService;
import may.yuntian.anlianwage.vo.PojectCommissionVo;
import may.yuntian.untils.AlRedisUntil;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-30 15:38
 */
@Service("perCommissionService")
@SuppressWarnings("all")
public class PerCommissionServiceImpl extends ServiceImpl<PerCommissionMapper, PerCommissionEntity> implements PerCommissionService {



    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectAmountService projectAmountService;

    /**
     * 分页列表--个人明细
     * @param params
     * @return
     */
    @Override
    public List<PojectCommissionVo> queryPage(Map<String, Object> params) {


        QueryWrapper<PerCommissionEntity> queryWrapper = exportQueryWrapperByParams(params);
        queryWrapper.notIn("c.type","采样年底提成","签发年底提成","报告年底提成");
        pageUtil2.startPage();
        List<PojectCommissionVo> pageList = baseMapper.listProjectCommission(queryWrapper);
        return pageList;

    }

    /**
     * 分页列表--提成类型明细
     * @param params
     * @return
     */
    @Override
    public List<PojectCommissionVo> queryTypePage(Map<String, Object> params) {


        QueryWrapper<PerCommissionEntity> queryWrapper = exportQueryWrapperByParams(params);
        queryWrapper.notIn("c.type","采样年底提成","签发年底提成","报告年底提成");
        pageUtil2.startPage();
        List<PojectCommissionVo> pageList = baseMapper.listTypeCommission(queryWrapper);
        return pageList;

    }


    /**
     * 根据检评绩效分配ID获取列表信息
     * @param performanceAllocationId
     * @return
     */
    public List<PerCommissionEntity> getListByPerformanceAllocationId(Long performanceAllocationId,Long projectId){
        List<PerCommissionEntity> perCommissionList = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>()
            .eq("project_id",projectId)
            .eq("performance_allocation_id",performanceAllocationId)
            .eq("state",1)
        );
        return perCommissionList;
    }


    /**
     * 根据检评绩效分配ID获取信息
     * @param performanceAllocationId
     * @return
     */
    public PerCommissionEntity getByPerformanceAllocationId(Long performanceAllocationId,Long projectId,String name){
        PerCommissionEntity perCommission = baseMapper.selectOne(new QueryWrapper<PerCommissionEntity>()
                .eq("project_id",projectId)
                .eq("performance_allocation_id",performanceAllocationId)
                .eq("personnel",name)
        );
        return perCommission;
    }


    /**
     * 检评绩效分配页面所需获取列表接口
     * @param performanceAllocationId
     * @param projectId
     * @param type
     * @return
     */
    public List<PerCommissionEntity> getListByIdAndType(Long performanceAllocationId,Long projectId,String type){
        List<PerCommissionEntity> perCommissionList = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>()
                .eq("project_id",projectId)
                .eq("performance_allocation_id",performanceAllocationId)
                .eq("type",type)
        );
        return perCommissionList;
    }

    /**
     * 获取环境任务采样提成
     * @param planId
     * @param projectId
     * @return
     */
    public PerCommissionEntity getByPlanId(Long projectId,String types){
        PerCommissionEntity perCommissionEntity = baseMapper.selectOne(
                new QueryWrapper<PerCommissionEntity>().eq("project_id",projectId)
                .eq("type",types).eq("state",1)
        );
        return perCommissionEntity;
    }


//    public void deleteByPerformanceAllocationId(Long performanceAllocationId,Long projectId){
//        baseMapper.delete(newCommission QueryWrapper<PerCommissionEntity>()
//                .eq("project_id",projectId)
//                .eq("performance_allocation_id",performanceAllocationId)
//        );
//    }

    /**
     * 显示已提成和异常的信息列表--导出--个人明细
     * 状态大于1
     * @return
     */
    public List<PojectCommissionVo> listAll(Map<String, Object> params) {
        QueryWrapper<PerCommissionEntity> queryWrapper = exportQueryWrapperByParams(params);
        queryWrapper.notIn("c.type","采样年底提成","签发年底提成","报告年底提成");

        List<PojectCommissionVo> voList = baseMapper.listProjectCommission(queryWrapper);
        return voList;
    }


    /**
     * 显示已提成和异常的信息列表--导出--类型明细
     * 状态大于1
     * @return
     */
    public List<PojectCommissionVo> listTypeAll(Map<String, Object> params) {
        QueryWrapper<PerCommissionEntity> queryWrapper = exportQueryWrapperByParams(params);
        queryWrapper.notIn("c.type","采样年底提成","签发年底提成","报告年底提成");
        List<PojectCommissionVo> voList = baseMapper.listTypeCommission(queryWrapper);
        return voList;
    }

    /**
     * 根据时间段生成提成记录
     */
    public List<PojectCommissionVo> getListByCommissionDate(PerCommissionEntity commission){
        Date startDate = commission.getStartDate();
        Date endDate = commission.getEndDate();
        String types = commission.getType();

        String[] typeList;
        if(StringUtils.isNotBlank(types)) {
            typeList = types.split(",");
        }else {
            typeList=new String[0];
        }
        QueryWrapper<PerCommissionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("state", 2).between(StringUtils.isNotBlank(String.valueOf(endDate)),"commission_date", startDate, endDate);
        List<PerCommissionEntity> list = this.list(queryWrapper);
        List<PojectCommissionVo> voList = baseMapper.listProjectCommission(queryWrapper);
        for(PerCommissionEntity commissionEntity : list) {
            commissionEntity.setState(commission.getState());
            commissionEntity.setCountDate(new Date());
        }
        this.updateBatchById(list);
        return voList;
    }

    /**
     * 根据项目类型获取提成列表
     * @param projectId
     * @return
     */
    public List<PerCommissionEntity> getListByprojectId(Long projectId){
        List<PerCommissionEntity> list = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>()
            .eq("project_id",projectId)
            .notIn("type","采样年底提成","签发年底提成","报告年底提成")
        );
        return list;
    }


    /**
     * g根据提成人查询
     * @param personnel
     * @return
     */
    public List<Long> getListByPersonnel(String personnel){
        List<PerCommissionEntity> list = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>().eq("personnel",personnel));
        List<Long> projectIds = new ArrayList<>();
        if (list.size()>0){
            projectIds = list.stream().map(PerCommissionEntity::getProjectId).distinct().collect(Collectors.toList());
        }

        return projectIds;
    }

    /**
     * 查询条件
     */
    private QueryWrapper<PerCommissionEntity> queryWrapperByParams(Map<String, Object> params){
        String projectId = (String)params.get("projectId");
        String type = (String)params.get("type");
        String state = (String)params.get("state");
        String personnel = (String)params.get("personnel");
        String deptName = (String)params.get("deptName");
        String subjection = (String)params.get("subjection");
        String endDate = (String)params.get("endDate");
        String startDate = (String)params.get("startDate");
        String countDate = (String)params.get("countDate");
        String identifier = (String)params.get("identifier");//项目编号    模糊搜索
        String company = (String)params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索

        List<Long> projectIds = new ArrayList<>();
        if(StringUtils.checkValNotNull(company) || StringUtils.checkValNotNull(identifier)) {
            QueryProjectVo queryVo = new QueryProjectVo();
            queryVo.setCompany(company);
            queryVo.setIdentifier(identifier);
            projectIds = projectService.getProjectIdsByParams(queryVo);
        }

//		String countStartDate = (String)params.get("countStartDate");
//		String countEndDate = (String)params.get("countEndDate");

        QueryWrapper<PerCommissionEntity> queryWrapper = new QueryWrapper<PerCommissionEntity>()
                .eq(StringUtils.checkValNotNull(projectId),"project_id", projectId)
                .eq(StringUtils.isNotBlank(type),"type", type)
                .eq(StringUtils.checkValNotNull(state),"state", state)
                .like(StringUtils.isNotBlank(personnel),"personnel", personnel)
                .like(StringUtils.isNotBlank(deptName),"c.dept_name", deptName)
                .like(StringUtils.isNotBlank(subjection),"subjection", subjection)
                .eq(StringUtils.isNotBlank(countDate), "count_date" ,countDate)
                .in(projectIds.size() > 0, "project_id", projectIds)
                .between(StringUtils.isNotBlank(endDate),"commission_date", startDate, endDate)
                ;
//				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);


        return queryWrapper;
    }


    /**
     * 用于导出查询条件
     */
    private QueryWrapper<PerCommissionEntity> exportQueryWrapperByParams(Map<String, Object> params){
        String projectId = (String)params.get("projectId");
        String type = (String)params.get("type");
        String state = (String)params.get("state");
        String personnel = (String)params.get("personnel");
        String deptName = (String)params.get("deptName");
        String subjection = (String)params.get("subjection");
        String endDate = (String)params.get("endDate");
        String startDate = (String)params.get("startDate");
        String countDate = (String)params.get("countDate");
        String identifier = (String)params.get("identifier");//项目编号    模糊搜索
        String company = (String)params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索

        List<Long> projectIds = new ArrayList<>();
        if(StringUtils.checkValNotNull(company) || StringUtils.checkValNotNull(identifier)) {
            QueryProjectVo queryVo = new QueryProjectVo();
            queryVo.setCompany(company);
            queryVo.setIdentifier(identifier);
            projectIds = projectService.getProjectIdsByParams(queryVo);
        }

//		String countStartDate = (String)params.get("countStartDate");
//		String countEndDate = (String)params.get("countEndDate");

        QueryWrapper<PerCommissionEntity> queryWrapper = new QueryWrapper<PerCommissionEntity>()
                .eq(StringUtils.checkValNotNull(projectId),"c.project_id", projectId)
                .eq(StringUtils.isNotBlank(type),"c.type", type)
                .eq(StringUtils.checkValNotNull(state),"c.state", state)
                .like(StringUtils.isNotBlank(deptName),"c.dept_name", deptName)
                .like(StringUtils.isNotBlank(personnel),"c.personnel", personnel)
                .like(StringUtils.isNotBlank(subjection),"c.subjection", subjection)
                .eq(StringUtils.isNotBlank(countDate), "c.count_date" ,countDate)
                .in(projectIds.size() > 0, "c.project_id", projectIds)
                .between(StringUtils.isNotBlank(endDate),"c.commission_date", startDate, endDate)
                .ge("c.id", 1)
                ;
//				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);

        return queryWrapper;
    }




}
