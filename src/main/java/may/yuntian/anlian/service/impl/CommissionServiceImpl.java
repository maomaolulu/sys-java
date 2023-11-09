package may.yuntian.anlian.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcelFactory;
import may.yuntian.anlian.entity.ProjectAmountEntity;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.service.ProjectAmountService;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.dto.GradeExportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.CommissionMapper;
import may.yuntian.anlian.service.CommissionService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.vo.QueryProjectVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import javax.servlet.http.HttpServletResponse;

/**
 * 提成记录
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-09
 */
@Service("commissionService")
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, CommissionEntity> implements CommissionService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectAmountService projectAmountService;
    @Autowired
    private ProjectDateService projectDateService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	
    	
    	QueryWrapper<CommissionEntity> queryWrapper = queryWrapperByParams(params);
        IPage<CommissionEntity> page = this.page(new Query<CommissionEntity>().getPage(params),queryWrapper
        		.between("state", 2,3)
        		.orderByDesc("id")
        		);
   
        page.getRecords().forEach(action->{
        	Long projectId = action.getProjectId();
    		ProjectEntity project = projectService.getById(projectId);
            ProjectAmountEntity projectAmountEntity = projectAmountService.getOneByProjectId(projectId);
            ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
            if (project != null) {
                action.setProject(project);
                if (projectAmountEntity != null) {
                    project.setProjectAmountEntity(projectAmountEntity);
                } else {
                    projectAmountEntity = new ProjectAmountEntity();
                    project.setProjectAmountEntity(projectAmountEntity);
                }
                if (projectDateEntity != null) {
                    project.setProjectDateEntity(projectDateEntity);
                } else {
                    projectDateEntity = new ProjectDateEntity();
                    project.setProjectDateEntity(projectDateEntity);
                }
            }
        });

        return new PageUtils(page);
    }


    /**
     * 财务中心-绩效提成：导出
     */
    public void listAll(HttpServletResponse response, Map<String, Object> params) {
        // 用户所选字段
        Set<String> includeColumnFieldNames = new HashSet<>(Arrays.asList(((String) params.get("fields")).split(",")));
        // 待导出的数据
        List<GradeExportDto> dataList = baseMapper.ListCommissionAll(exportQueryWrapperByParams(params));
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("财务中心-绩效提成", "UTF-8").replaceAll("\\+", "%20") + ".xlsx" + ";");
            out = response.getOutputStream();
            // 指定用哪个class去写，写到第一个sheet，文件流会自动关闭
            EasyExcelFactory.write(out, GradeExportDto.class).includeColumnFieldNames(includeColumnFieldNames).sheet("sheet").doWrite(dataList);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IoUtil.close(out);
        }
    }

    /**
     * 根据查询条件软删除绩效提成
     *
     * @param params
     */
    public void updateStateByParams(Map<String, Object> params) {
        List<CommissionEntity> list = baseMapper.selectList(queryWrapperByParams(params)
                .between("state", 2, 3)
                .orderByDesc("id")
        );
        for (CommissionEntity commissionEntity : list) {
            commissionEntity.setState(4);
            this.updateById(commissionEntity);
        }

    }


    /**
     * 获取已提成项目id
     */
    public List<Long> getNotNullIdList() {
        List<CommissionEntity> list = baseMapper.selectList(new QueryWrapper<CommissionEntity>()
                .eq("type", "采样提成")
                .isNotNull("commission_date")
        );
        List<Long> idList = list.stream().distinct().map(CommissionEntity::getProjectId).collect(Collectors.toList());
        return idList;
    }


    /**
     * 根据提成日期查询提成列表的项目ID数组
     *
     * @param commissionStratDate
     * @param commissionEndDate
     * @return
     */
    public List<Long> getListByCommissionDate(String commissionStratDate, String commissionEndDate) {
        List<CommissionEntity> list = baseMapper.selectList(new QueryWrapper<CommissionEntity>()
                .ge(StringUtils.isNotBlank(commissionStratDate), "commission_date", commissionStratDate)
                .le(StringUtils.isNotBlank(commissionEndDate), "commission_date", commissionEndDate)
                .eq("type", "采样提成")
        );
        List<Long> projectIdList = list.stream().map(CommissionEntity::getProjectId).distinct().collect(Collectors.toList());
        return projectIdList;
    }


    /**
     * 根据时间段生成提成记录
     */
    public List<CommissionEntity> getListByCommissionDate(CommissionEntity commission) {
        Date startDate = commission.getStartDate();
        Date endDate = commission.getEndDate();
        String types = commission.getTypes();

        String[] typeList;
        if (StringUtils.isNotBlank(types)) {
            typeList = types.split(",");
        } else {
            typeList = new String[0];
        }

        List<CommissionEntity> list = this.list(new QueryWrapper<CommissionEntity>()
//    			.and(i->i.eq("state", 1).or().eq("state", 4))
                        .ne("state", 2)
                        .between(StringUtils.isNotBlank(String.valueOf(endDate)), "commission_date", startDate, endDate)
//    			.in(typeList.length>0,"type", typeList)
        );

        for (CommissionEntity commissionEntity : list) {
            ProjectEntity projectEntity = projectService.getById(commissionEntity.getProjectId());
            commissionEntity.setProject(projectEntity);
            commissionEntity.setState(commission.getState());
            commissionEntity.setCountDate(new Date());
        }
        this.updateBatchById(list);
        return list;
    }

    /**
     * 通过项目id和提成类型获取列表
     */
    public CommissionEntity getCommissionByProjectIdAndType(Long projectId, String type) {
        CommissionEntity commissionEntity = baseMapper.selectOne(new QueryWrapper<CommissionEntity>()
                .eq("project_id", projectId)
                .eq("type", type)
        );
        return commissionEntity;
    }


    /**
     * 通过项目id和获取列表
     */
    public List<CommissionEntity> getCommissionListByProjectId(List<Long> projectIds) {
        List<CommissionEntity> commissionList;
        if (projectIds!=null&&projectIds.size()>0){
            commissionList = baseMapper.selectList(new QueryWrapper<CommissionEntity>()
                    .in("project_id", projectIds)
            );
        }else {
            commissionList = new ArrayList<>();
        }

        return commissionList;
    }


    /**
     * 查询条件
     */
    private QueryWrapper<CommissionEntity> queryWrapperByParams(Map<String, Object> params) {
        String projectId = (String) params.get("projectId");
        String type = (String) params.get("type");
        String state = (String) params.get("state");
        String personnel = (String) params.get("personnel");
        String companyOrder = (String) params.get("companyOrder");//隶属公司
        String businessSource = (String) params.get("businessSource");//业务来源
        String subjection = (String) params.get("subjection");
        String endDate = (String) params.get("endDate");
        String startDate = (String) params.get("startDate");
        String countDate = (String) params.get("countDate");
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索

        List<Long> projectIds = new ArrayList<>();
        if (StringUtils.checkValNotNull(company) || StringUtils.checkValNotNull(identifier) || StringUtils.checkValNotNull(companyOrder) || StringUtils.checkValNotNull(businessSource)) {
            QueryProjectVo queryVo = new QueryProjectVo();
            queryVo.setCompany(company);
            queryVo.setIdentifier(identifier);
            queryVo.setCompanyOrder(companyOrder);
            queryVo.setBusinessSource(businessSource);
            projectIds = projectService.getProjectIdsByParams(queryVo);
            if (projectIds.size() <= 0) {
                projectIds.add(Long.valueOf(0));
            }
        }


//		String countStartDate = (String)params.get("countStartDate");
//		String countEndDate = (String)params.get("countEndDate");


        QueryWrapper<CommissionEntity> queryWrapper = new QueryWrapper<CommissionEntity>()
                .eq(StringUtils.checkValNotNull(projectId), "project_id", projectId)
                .eq(StringUtils.isNotBlank(type), "type", type)
                .eq(StringUtils.checkValNotNull(state), "state", state)
                .like(StringUtils.isNotBlank(personnel), "personnel", personnel)
                .like(StringUtils.isNotBlank(subjection), "subjection", subjection)
                .eq(StringUtils.isNotBlank(countDate), "count_date", countDate)
                .in(projectIds.size() > 0, "project_id", projectIds)
                .between(StringUtils.isNotBlank(endDate), "commission_date", startDate, endDate);
//				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);


        return queryWrapper;

//
//		QueryWrapper<CommissionEntity> queryWrapper = new QueryWrapper<CommissionEntity>()
//				.eq(StringUtils.checkValNotNull(projectId),"project_id", projectId)
//				.eq(StringUtils.isNotBlank(type),"type", type)
//				.eq(StringUtils.checkValNotNull(state),"state", state)
//				.like(StringUtils.isNotBlank(personnel),"personnel", personnel)
//
//				.eq(StringUtils.isNotBlank(countDate), "count_date" ,countDate)
//				.in(projectIds.size() > 0, "project_id", projectIds)
//				.between(StringUtils.isNotBlank(endDate),"commission_date", startDate, endDate)
//				;
////				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);
//
//    	if (params.get("other")!=null){
//            String other = (String)params.get("other");
//            queryWrapper.notIn("subjection", other);
//        }else {
//            queryWrapper.like(StringUtils.isNotBlank(subjection),"subjection", subjection);
//        }
//    	return queryWrapper;

    }


    /**
     * 用于导出查询条件
     */
    private QueryWrapper<CommissionEntity> exportQueryWrapperByParams(Map<String, Object> params) {
        String projectId = (String) params.get("projectId");
        String type = (String) params.get("type");
        String state = (String) params.get("state");
        String personnel = (String) params.get("personnel");
        String subjection = (String) params.get("subjection");
        String companyOrder = (String) params.get("companyOrder");//隶属公司
        String businessSource = (String) params.get("businessSource");//业务来源
        String endDate = (String) params.get("endDate");
        String startDate = (String) params.get("startDate");
        String countDate = (String) params.get("countDate");
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String company = (String) params.get("company");//受检企业名称或者委托单位名称共用  模糊搜索

        List<Long> projectIds = new ArrayList<>();
        if (StringUtils.checkValNotNull(company) || StringUtils.checkValNotNull(identifier) || StringUtils.checkValNotNull(companyOrder) || StringUtils.checkValNotNull(businessSource)) {
            QueryProjectVo queryVo = new QueryProjectVo();
            queryVo.setCompany(company);
            queryVo.setIdentifier(identifier);
            queryVo.setCompanyOrder(companyOrder);
            queryVo.setBusinessSource(businessSource);
            projectIds = projectService.getProjectIdsByParams(queryVo);
            if (!(projectIds.size() > 0)) {
                projectIds.add(Long.valueOf(0));
            }
        }


//		String countStartDate = (String)params.get("countStartDate");
//		String countEndDate = (String)params.get("countEndDate");


        QueryWrapper<CommissionEntity> queryWrapper = new QueryWrapper<CommissionEntity>()
                .eq(StringUtils.checkValNotNull(projectId), "c.project_id", projectId)
                .eq(StringUtils.isNotBlank(type), "c.type", type)
                .eq(StringUtils.checkValNotNull(state), "c.state", state)
                .like(StringUtils.isNotBlank(personnel), "c.personnel", personnel)
                .like(StringUtils.isNotBlank(subjection), "c.subjection", subjection)
                .eq(StringUtils.isNotBlank(countDate), "c.count_date", countDate)
                .in(projectIds.size() > 0, "c.project_id", projectIds)
                .between(StringUtils.isNotBlank(endDate), "c.commission_date", startDate, endDate)
                .ge("c.id", 1);
//				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);

        return queryWrapper;

//
//		QueryWrapper<CommissionEntity> queryWrapper = new QueryWrapper<CommissionEntity>()
//				.eq(StringUtils.checkValNotNull(projectId),"c.project_id", projectId)
//				.eq(StringUtils.isNotBlank(type),"c.type", type)
//				.eq(StringUtils.checkValNotNull(state),"c.state", state)
//				.like(StringUtils.isNotBlank(personnel),"c.personnel", personnel)
////				.like(StringUtils.isNotBlank(subjection),"c.subjection", subjection)
//				.eq(StringUtils.isNotBlank(countDate), "c.count_date" ,countDate)
//				.in(projectIds.size() > 0, "c.project_id", projectIds)
//				.between(StringUtils.isNotBlank(endDate),"c.commission_date", startDate, endDate)
//				.ge("c.id", 1)
//				;
////				.between(StringUtils.isNotBlank(countEndDate),"count_date", countStartDate, countEndDate);
//
//        if (params.get("other")!=null&&StringUtils.isNotBlank((String)params.get("other"))){
//            String other = (String)params.get("other");
//            queryWrapper.notIn("c.subjection", other);
//        }else {
//            queryWrapper.like(StringUtils.isNotBlank(subjection),"c.subjection", subjection);
//        }
//
//    	return queryWrapper;

    }


}
