package may.yuntian.publicity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.CategoryService;
import may.yuntian.anlian.service.OrderSourceService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.modules.sys.dao.SysDeptDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.publicity.mapper.PublicityInfoMapper;
import may.yuntian.publicity.entity.PublicityInfoEntity;
import may.yuntian.publicity.service.PublicityInfoService;

/**
 * 项目公示记录
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-12-21 15:45:22
 */
@Service("publicityInfoService")
public class PublicityInfoServiceImpl extends ServiceImpl<PublicityInfoMapper, PublicityInfoEntity> implements PublicityInfoService {

    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private OrderSourceService orderSourceService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SysDeptDao deptDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PublicityInfoEntity> page = this.page(
                new Query<PublicityInfoEntity>().getPage(params),
                new QueryWrapper<PublicityInfoEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 检评公示记录列表
     *
     * @param params
     * @return
     */
    @Override
    public List<PublicityPageVo> getZjList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("status");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("p.type", "检评");
//        queryWrapper.eq("cs.detection_type", "定期检测");
        queryWrapper.gt("p.pub_status", 0);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);

        QueryWrapper newQuery = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = baseMapper.getAllList(newQuery);
        return list;
    }

    /**
     * 评价公示记录列表
     *
     * @param params
     * @return
     */
    @Override
    public List<PublicityPageVo> getPjList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("pubStatus");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("p.type", "现状","控评");
//        queryWrapper.gt("p.pub_status", 0);
        if (StringUtils.checkValNotNull(status)){
            queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);
        }else {
            queryWrapper.gt("p.pub_status", 0);
        }
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(identifier), "p.identifier", identifier);
//        queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);

        QueryWrapper newQuery = queryWrapperByParamsAuth(queryWrapper);
        pageUtil2.startPage();
        List<PublicityPageVo> list = baseMapper.getAllList(newQuery);
        return list;
    }

    /**
     * 质控公示记录列表
     *
     * @param params
     * @return
     */
    @Override
    public List<PublicityPageVo> getSysList(Map<String, Object> params) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("status");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("p.type", "检评","现状","控评");
//        queryWrapper.eq("cs.detection_type", "定期检测");
        queryWrapper.gt("p.pub_status", 0);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.checkValNotNull(status), "p.pub_status", status);
        // 支持分流
        queryWrapper.like(StringUtils.checkValNotNull(subjection), "p.company_order", subjection);
        pageUtil2.startPage();
        List<PublicityPageVo> list = baseMapper.getAllList(queryWrapper);
        return list;
    }


    /**
     * 获取公示记录详细信息
     *
     * @param projectId
     * @return
     */
    @Override
    public List<PublicityInfoEntity> getInfoList(Long projectId) {
        List<PublicityInfoEntity> list = baseMapper.getListByProjectId(projectId);
        return list;
    }


    /**
     * 获取最近一次操作记录
     *
     * @param projectId
     * @param status
     * @return
     */
    public PublicityInfoEntity getLastLimit(Long projectId, Integer status) {
        QueryWrapper<PublicityInfoEntity> queryWrapper = new QueryWrapper<PublicityInfoEntity>();
        queryWrapper.eq("project_id", projectId);
        if (status == 2){
            queryWrapper.eq("operation", "提交审核");
        }else {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("operation_time");
        queryWrapper.last("limit 1");
        PublicityInfoEntity publicityInfo = baseMapper.selectOne(queryWrapper);

        return publicityInfo;
    }

    /**
     * 获取最近一次操作记录
     *
     * @param projectId
     * @return
     */
    public PublicityInfoEntity getTjLastLimit(Long projectId) {
        PublicityInfoEntity publicityInfo = baseMapper.selectOne(new QueryWrapper<PublicityInfoEntity>()
                .eq("project_id", projectId)
                .eq("operation", "提交审核")
                .orderByDesc("operation_time")
                .last("limit 1")
        );

        return publicityInfo;
    }

    /**
     * 获取最近一次操作记录--批量
     *
     * @param projectIds
     * @param status
     * @return
     */
    public List<PublicityInfoEntity> getLastLimitList(List<Long> projectIds, Integer status) {
        QueryWrapper<PublicityInfoEntity> queryWrapper = new QueryWrapper<PublicityInfoEntity>();
        queryWrapper.in("project_id", projectIds);
        if (status == 2){
            queryWrapper.eq("operation", "提交审核");
        }else {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("operation_time");
        List<PublicityInfoEntity> publicityInfoEntities = baseMapper.selectList(queryWrapper);

        return getMaxTimeList(publicityInfoEntities);
    }

    /**
     * 根据getPersonIdNumber分组
     * 按照getAppearTime max 排序 取第一条
     */
    private List<PublicityInfoEntity> getMaxTimeList(List<PublicityInfoEntity> tracePointPersonLogList) {
        if (CollectionUtils.isEmpty(tracePointPersonLogList)) {
            return Collections.emptyList();
        }
        return tracePointPersonLogList.stream()
                .collect(Collectors.groupingBy(PublicityInfoEntity::getStatus,
                        Collectors.maxBy(Comparator.comparing(PublicityInfoEntity::getOperationTime))))
                .values()
                .stream()
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .sorted(Comparator.comparing(PublicityInfoEntity::getOperationTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * xin 权限控制
     *
     * @param
     * @return
     */
    public QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(QueryWrapper<ProjectEntity> wappr) {
        // 隶属公司
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        // 公司旗下所有部门及其子部门
        QueryWrapper<SysDeptEntity> deptWrapper = new QueryWrapper<>();
        deptWrapper.eq("name", subjection);
        Long subjectionDeptId = deptDao.selectOne(deptWrapper).getDeptId();
        QueryWrapper<SysDeptEntity> childrenDeptWrapper = new QueryWrapper<>();
        String deptStructure = "," + subjectionDeptId + ",";
        childrenDeptWrapper.like("dept_structure", deptStructure);
        List<Long> deptList = deptDao.selectList(childrenDeptWrapper).stream().distinct().map(SysDeptEntity::getDeptId).collect(Collectors.toList());
        System.out.println("deptList = " + deptList);
//        List<String> projectTypeNames = new ArrayList<>();
//        projectTypeNames.add("检评");
//        projectTypeNames.add("职卫监督");
//        projectTypeNames.add("职卫示范");
        // 数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = wappr
                // 部门权限控制
                .in("dept_id", deptList);
                // 项目类型权限控制
//                .in("type", projectTypeNames);

        return queryWrapper;
    }


//    /**
//     * 用于项目信息分页的查询条件的处理
//     * 限制部门数据权限及项目类型权限
//     *
//     * @param
//     * @return
//     */
//    public QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(QueryWrapper<ProjectEntity> wappr) {
//        //数据权限控制
//        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
//        Long deptId = sysUserEntity.getDeptId();//登录用户部门ID
//        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
//        List<Long> projectTypes = sysRoleDeptService.queryProjectTypeListByUserId(sysUserEntity.getUserId());
//        List<Long> orderIds = sysRoleDeptService.queryOrderListByUserId(sysUserEntity.getUserId());
//        List<Long> sourceIds = sysRoleDeptService.querySourceListByUserId(sysUserEntity.getUserId());
//
//        //根据ID列表查询类型信息名称列表
//        List<String> projectTypeNames = new ArrayList<>();
//        if (projectTypes != null && projectTypes.size() > 0) {
//            projectTypeNames = categoryService.getCategoryNameByIds(projectTypes);
//        } else {
//            projectTypeNames.add("无项目类型权限");//项目类型权限控制,无任何权限时故意赋值0查不到任何数据
//        }
//
//        //数据权限控制
//        QueryWrapper<ProjectEntity> queryWrapper = wappr
//                .in("dept_id", roleDeptIds)//部门权限控制,只根据数据权限显示数据，不根据归属部门
//                .in((projectTypeNames != null && projectTypeNames.size() > 0), "type", projectTypeNames);//项目类型权限控制,>0判断逻辑上稍有漏洞
//
//        queryWrapper.and(wr -> {
//            //根据ID获取项目隶属名称列表
//            List<String> companyOrderList = new ArrayList<>();
//            if (orderIds != null && orderIds.size() > 0) {
//                companyOrderList = orderSourceService.getOrderSourceByIds(orderIds);
//            } else {
//                companyOrderList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
//            }
//            //根据ID获取业务来源名称列表
//            List<String> businessSourcesList = new ArrayList<String>();
//            if (sourceIds != null && sourceIds.size() > 0) {
//                businessSourcesList = orderSourceService.getOrderSourceByIds(sourceIds);
//            } else {
//                businessSourcesList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
//            }
//            wr.in("company_order", companyOrderList);
//            wr.or();
//            wr.in("business_source", businessSourcesList);
//        });
//
//        return queryWrapper;
//    }

}
