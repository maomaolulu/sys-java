package may.yuntian.external.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.ProjectProceduresEntity;
import may.yuntian.anlian.service.CommissionService;
import may.yuntian.anlian.service.ProjectProceduresService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.external.oa.entity.IpEntity;
import may.yuntian.external.oa.mapper.ProjectQueryMapper;
import may.yuntian.external.oa.service.IpService;
import may.yuntian.external.oa.service.ProjectQueryService;
import may.yuntian.external.oa.vo.ProjectAllQueryVo;
import may.yuntian.external.oa.vo.ProjectQueryVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static may.yuntian.untils.pageUtil2.startPage;

/**
 * 我的项目service实现层
 *
 * @author mi
 * @Create 2023-3-31 15:50:43
 */
@Service
public class ProjectQueryServiceImpl extends ServiceImpl<ProjectQueryMapper, ProjectEntity> implements ProjectQueryService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private IpService ipService;

    @Autowired
    private ProjectProceduresService proceduresService;

    @Autowired
    private CommissionService commissionService;

    /**
     * 项目编号+受检单位筛选我的项目
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectQueryVo> selectByIdentifierOrCompany(Map<String, Object> params) {
        String username = (String) params.get("username");
        String projectName = (String) params.get("projectName");
        String identifier = (String) params.get("identifier");
        String company = (String) params.get("company");
        SysUserEntity sysUserEntity = userService.queryByUserName(username);
        if (sysUserEntity == null) {
            return new ArrayList<>();
        } else {
            Long salesmenId = sysUserEntity.getUserId();
            QueryWrapper<ProjectQueryVo> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
            wrapper.like(StringUtils.isNotBlank(company), "p.company", company);
            wrapper.eq(StringUtils.isNotNull(salesmenId), "p.salesmenid", salesmenId);
            // 新增项目名称筛选
            wrapper.like(StringUtils.isNotNull(projectName), "p.project_name", projectName);
            // 创建时间倒序
            wrapper.orderByDesc("p.createtime");
            // 获取我的所有项目 业务员替换录入时间
            startPage();
            List<ProjectQueryVo> list = baseMapper.getMyAllProject(wrapper);

            List<Long> projectIds = list.stream().map(ProjectQueryVo::getId).distinct().collect(Collectors.toList());
            Map<Long,List<CommissionEntity>> commissionMap = new HashMap<>();
            if (projectIds!=null&&projectIds.size()>0){
                List<CommissionEntity> commissionEntityList = commissionService.getCommissionListByProjectId(projectIds);
                if (commissionEntityList!=null&&commissionEntityList.size()>0){
                    commissionMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getProjectId));
                }
            }else {
                commissionMap = new HashMap<>();
            }
            for (ProjectQueryVo projectQueryVo: list){
//            list.forEach(projectQueryVo -> {
                projectQueryVo.setSalesmenPhone(sysUserEntity.getMobile());
                Long projectId = projectQueryVo.getId();
                String status = projectQueryVo.getStatus();
                Integer statusNow = 0;
                switch (status) {
                    case "项目录入":
                        statusNow = 1;
                        break;
                    case "下发":
                        statusNow = 2;
                        break;
                    case "排单":
                        statusNow = 3;
                        break;
                    case "现场调查":
                        statusNow = 4;
                        break;
                    case "采样":
                        statusNow = 5;
                        break;
                    case "送样":
                        statusNow = 10;
                        break;
                    case "检测报告":
                        statusNow = 20;
                        break;
                    case "检测报告发送":
                        statusNow = 22;
                        break;
                    case "报告编制":
                        statusNow = 35;
                        break;
                    case "审核":
                        statusNow = 36;
                        break;
                    case "专家评审":
                        statusNow = 37;
                        break;
                    case "出版前校核":
                        statusNow = 38;
                        break;
                    case "质控签发":
                        statusNow = 40;
                        break;
                    case "报告寄送":
                        statusNow = 50;
                        break;
                    case "项目归档":
                        statusNow = 60;
                        break;
                    case "项目完结":
                        statusNow = 70;
                        break;
                    case "项目挂起":
                        statusNow = 98;
                        break;
                    case "项目终止":
                        statusNow = 99;
                        break;
                    default:
                        break;
                }
                // 获取每个项目的状态触发时间
                String stringStatusNow = String.valueOf(statusNow);
                String stringProjectId = String.valueOf(projectId);
                QueryWrapper<ProjectProceduresEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(StringUtils.isNotBlank(stringProjectId), "project_id", projectId);
                queryWrapper.eq(StringUtils.isNotBlank(stringStatusNow), "status", statusNow);
                // 降序排序只取最新的更新状态时间
                queryWrapper.last("limit 1");
                queryWrapper.orderByDesc("createtime");
                ProjectProceduresEntity one = proceduresService.getOne(queryWrapper);
                if (one == null) {
                    new ProjectProceduresEntity();
                } else {
                    Date createtime = one.getCreatetime();
                    projectQueryVo.setStateTriggerTime(createtime);
                }
                if (commissionMap.get(projectId)!=null){
                    List<CommissionEntity> commissionList = commissionMap.get(projectId);
                    Map<String,List<CommissionEntity>> amountListMap = commissionList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
                    projectQueryVo.setBusinessAmount(amountListMap.get("业务提成")!=null?amountListMap.get("业务提成").get(0).getCmsAmount():BigDecimal.ZERO);//业务提成
                    projectQueryVo.setSamplingAmount(amountListMap.get("采样提成")!=null?amountListMap.get("采样提成").get(0).getCmsAmount():BigDecimal.ZERO);//采样提成
                    projectQueryVo.setReportAmount(amountListMap.get("报告提成")!=null?amountListMap.get("报告提成").get(0).getCmsAmount():BigDecimal.ZERO);//报告提成
                    projectQueryVo.setDetectionAmount(amountListMap.get("检测提成")!=null?amountListMap.get("检测提成").get(0).getCmsAmount():BigDecimal.ZERO);//检测提成
                }else {
                    projectQueryVo.setBusinessAmount(BigDecimal.ZERO);//业务提成
                    projectQueryVo.setSamplingAmount(BigDecimal.ZERO);//采样提成
                    projectQueryVo.setReportAmount(BigDecimal.ZERO);//报告提成
                    projectQueryVo.setDetectionAmount(BigDecimal.ZERO);//检测提成
                }

            }
            return list;
        }

    }

    /**
     * 项目编号+受检单位+业务员查询所有公司项目
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectAllQueryVo> selectByIdentifierOrCompanyOrSaleMen(Map<String, Object> params) {
        String identifier = (String) params.get("identifier");
        String company = (String) params.get("company");
        String salesmen = (String) params.get("salesmen");
        String projectName = (String) params.get("projectName");
        QueryWrapper<ProjectQueryVo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        wrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        wrapper.like(StringUtils.isNotBlank(salesmen), "p.salesmen", salesmen);
        // 新增项目名称筛选
        wrapper.like(StringUtils.isNotNull(projectName), "p.project_name", projectName);
        // 创建时间倒序
        wrapper.orderByDesc("p.createtime");
        // 获取所有公司项目 业务员+录入时间
        startPage();
        List<ProjectAllQueryVo> list = baseMapper.getMyAllProjectAll(wrapper);

        List<Long> projectIds = list.stream().map(ProjectAllQueryVo::getId).distinct().collect(Collectors.toList());
        Map<Long,List<CommissionEntity>> commissionMap = new HashMap<>();
        if (projectIds!=null&&projectIds.size()>0){
            List<CommissionEntity> commissionEntityList = commissionService.getCommissionListByProjectId(projectIds);
            if (commissionEntityList!=null&&commissionEntityList.size()>0){
                commissionMap = commissionEntityList.stream().collect(Collectors.groupingBy(CommissionEntity::getProjectId));
            }
        }else {
            commissionMap = new HashMap<>();
        }

        for (ProjectAllQueryVo projectAllQueryVo: list){
//        list.forEach(projectAllQueryVo -> {
            Long projectId = projectAllQueryVo.getId();
            String status = projectAllQueryVo.getStatus();
            Integer statusNow = 0;
            switch (status) {
                case "项目录入":
                    statusNow = 1;
                    break;
                case "下发":
                    statusNow = 2;
                    break;
                case "排单":
                    statusNow = 3;
                    break;
                case "现场调查":
                    statusNow = 4;
                    break;
                case "采样":
                    statusNow = 5;
                    break;
                case "送样":
                    statusNow = 10;
                    break;
                case "检测报告":
                    statusNow = 20;
                    break;
                case "检测报告发送":
                    statusNow = 22;
                    break;
                case "报告编制":
                    statusNow = 35;
                    break;
                case "审核":
                    statusNow = 36;
                    break;
                case "专家评审":
                    statusNow = 37;
                    break;
                case "出版前校核":
                    statusNow = 38;
                    break;
                case "质控签发":
                    statusNow = 40;
                    break;
                case "报告寄送":
                    statusNow = 50;
                    break;
                case "项目归档":
                    statusNow = 60;
                    break;
                case "项目完结":
                    statusNow = 70;
                    break;
                case "项目挂起":
                    statusNow = 90;
                    break;
                case "项目终止":
                    statusNow = 99;
                    break;
                default:
                    break;
            }
            // 获取每个项目的状态触发时间
            String stringStatusNow = String.valueOf(statusNow);
            String stringProjectId = String.valueOf(projectId);
            QueryWrapper<ProjectProceduresEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotBlank(stringProjectId), "project_id", projectId);
            queryWrapper.eq(StringUtils.isNotBlank(stringStatusNow), "status", statusNow);
            // 降序排序只取最新的更新状态时间
            queryWrapper.last("limit 1");
            queryWrapper.orderByDesc("createtime");
            ProjectProceduresEntity one = proceduresService.getOne(queryWrapper);
            if (one == null) {
                new ProjectProceduresEntity();
            } else {
                Date createtime = one.getCreatetime();
                projectAllQueryVo.setStateTriggerTime(createtime);
            }
            if (commissionMap.get(projectId)!=null){
                List<CommissionEntity> commissionList = commissionMap.get(projectId);
                Map<String,List<CommissionEntity>> amountListMap = commissionList.stream().collect(Collectors.groupingBy(CommissionEntity::getType));
                projectAllQueryVo.setBusinessAmount(amountListMap.get("业务提成")!=null?amountListMap.get("业务提成").get(0).getCmsAmount():BigDecimal.ZERO);//业务提成
                projectAllQueryVo.setSamplingAmount(amountListMap.get("采样提成")!=null?amountListMap.get("采样提成").get(0).getCmsAmount():BigDecimal.ZERO);//采样提成
                projectAllQueryVo.setReportAmount(amountListMap.get("报告提成")!=null?amountListMap.get("报告提成").get(0).getCmsAmount():BigDecimal.ZERO);//报告提成
                projectAllQueryVo.setDetectionAmount(amountListMap.get("检测提成")!=null?amountListMap.get("检测提成").get(0).getCmsAmount():BigDecimal.ZERO);//检测提成
            }else {
                projectAllQueryVo.setBusinessAmount(BigDecimal.ZERO);//业务提成
                projectAllQueryVo.setSamplingAmount(BigDecimal.ZERO);//采样提成
                projectAllQueryVo.setReportAmount(BigDecimal.ZERO);//报告提成
                projectAllQueryVo.setDetectionAmount(BigDecimal.ZERO);//检测提成
            }

        }
        return list;
    }

    /**
     * 测试 获取ip
     *
     * @param ip
     */
    @Override
    public void getIp(String ip) {
        IpEntity ipEntity = new IpEntity();
        ipEntity.setIp(ip);
        ipService.save(ipEntity);
    }


}
