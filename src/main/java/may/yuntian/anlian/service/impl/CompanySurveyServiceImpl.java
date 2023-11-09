package may.yuntian.anlian.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.ProjectUserEntity;
import may.yuntian.anlian.entity.SampleImgEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.ProjectUserService;
import may.yuntian.anlian.service.SampleImgService;
import may.yuntian.anlian.vo.CityCompanySurveyVo;
import may.yuntian.anlian.vo.CompanySurveyVo;
import may.yuntian.modules.sys.dao.SysDeptDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.publicity.vo.PublicityPageVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.CompanySurveyMapper;
import may.yuntian.anlian.entity.CompanySurveyEntity;
import may.yuntian.anlian.service.CompanySurveyService;

/**
 * 用人单位概况调查
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Service("companySurveyService")
public class CompanySurveyServiceImpl extends ServiceImpl<CompanySurveyMapper, CompanySurveyEntity> implements CompanySurveyService {
    @Autowired
    private SampleImgService sampleImgService;
    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private SysDeptDao deptDao;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String projectId = (String) params.get("projectId");//项目ID
        String identifier = (String) params.get("identifier");//项目编号    模糊搜索
        String project = (String) params.get("project");//项目名称    模糊搜索
        String company = (String) params.get("company");//受检企业名称  模糊搜索
        String industry = (String) params.get("industry");//所属行业  模糊搜索
        String riskLevel = (String) params.get("riskLevel");//职业病危害风险分类(0一般、1较重、2严重)

        IPage<CompanySurveyEntity> page = this.page(
                new Query<CompanySurveyEntity>().getPage(params),
                new QueryWrapper<CompanySurveyEntity>()
                        .eq(StringUtils.isNotBlank(projectId), "project_id", projectId)
                        .like(StringUtils.isNotBlank(identifier), "identifier", identifier)
                        .like(StringUtils.isNotBlank(project), "project", project)
                        .like(StringUtils.isNotBlank(company), "company", company)
                        .like(StringUtils.isNotBlank(industry), "industry", industry)
                        .eq(StringUtils.isNotBlank(riskLevel), "risk_level", riskLevel)
                        .orderByDesc("id")
        );

        return new PageUtils(page);
    }

    /**
     * 根据项目ID查询是否已经存在于用人单位概况调查信息中
     *
     * @param projectId 任务排单ID
     * @return boolean
     */
    public Boolean notExistCompanySurveyByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<CompanySurveyEntity>().eq("project_id", projectId));
        if (count > 0)
            return false;
        else
            return true;
    }

    /**
     * 通过项目id查询用人单位概况调查信息
     *
     * @param projectId
     * @return
     */
    public CompanySurveyEntity seleteByProjectId(Long projectId) {
        CompanySurveyEntity companySurvey = baseMapper.selectOne(new QueryWrapper<CompanySurveyEntity>().eq("project_id", projectId));
        return companySurvey;
    }

    /**
     * 根据项目id删除用人单位概况调查信息
     *
     * @param projectId
     */
    public void deleteByProjectId(Long projectId) {
        baseMapper.delete(new QueryWrapper<CompanySurveyEntity>()
                .eq("project_id", projectId)
        );
    }

    /**
     * 查询市级申报详情
     */
    public CityCompanySurveyVo selectCityCompanySurveyList(Long projectId) {

        CityCompanySurveyVo cityCompanySurveyVo = baseMapper.getCityCompanySurveyVo(projectId);

        return cityCompanySurveyVo;
    }

    /**
     * 公示信息
     *
     * @param
     */
    @Override
    public List<CompanySurveyVo> publicityList(CompanySurveyVo company) {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        QueryWrapper<ProjectEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq(company.getProjectId() != null, "cs.project_id", company.getProjectId());
        //需要公示的项目
//		objectQueryWrapper.eq("ap.status",40);
        objectQueryWrapper.eq("ap.apply_publicity_status", 2);
        objectQueryWrapper.like(StrUtil.isNotBlank(company.getCompany()), "cs.company", company.getCompany());
        // 支持分流
        objectQueryWrapper.like(StrUtil.isNotBlank(subjection), "company_order", subjection);
        objectQueryWrapper.like(StrUtil.isNotBlank(company.getIdentifier()), "ap.identifier", company.getIdentifier());
        objectQueryWrapper.eq("ap.type", "检评");
//		objectQueryWrapper.eq("ap.publicity_status",0);
//		objectQueryWrapper.orderByDesc("apd.report_issue");
//		objectQueryWrapper.orderByAsc("samplingDate1Z","id");

        //权限控制

        List<CompanySurveyVo> companySurveyVos = baseMapper.publicityList(objectQueryWrapper);
        if (companySurveyVos != null && companySurveyVos.size() > 0) {
            for (CompanySurveyVo companySurveyVo : companySurveyVos
            ) {
                //检测图
                List<SampleImgEntity> sampleImgEntityList = sampleImgService.list(new QueryWrapper<SampleImgEntity>().eq("project_id", companySurveyVo.getProjectId()));
                if (sampleImgEntityList != null && sampleImgEntityList.size() > 0) {
                    companySurveyVo.setImgs(sampleImgEntityList);
                } else {
                    companySurveyVo.setImgs(new ArrayList<SampleImgEntity>());
                }
                //技术服务项目组人员
                Set<String> strings1 = new HashSet<>();
                String[] stringss = new String[]{"姜翠霞", "孙春花", "赵鑫", "郭伟立", "潘长卫"};
                strings1.addAll(Arrays.asList(stringss));
                //项目负责人
                String charge = companySurveyVo.getCharge();
                if (StrUtil.isNotBlank(charge)) {
                    if (charge.equals("张纯")) {
                        strings1.add("戴文雅");
                    } else if (charge.equals("欧阳婷")) {
                        strings1.add("王玲玲");
                    } else {
                        strings1.add(charge);
                    }
                }

//                String.join(",",strings1);
                //现场调查人员
                List<ProjectUserEntity> list = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 110));
                if (list != null && list.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join(",", strings);
                    companySurveyVo.setFieldInvestigators(join);
                } else {
                    companySurveyVo.setFieldInvestigators("");
                }


                //现场采样人员
                List<ProjectUserEntity> list2 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 120));
                if (list2 != null && list2.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list2
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join(",", strings);
                    companySurveyVo.setFieldSampling(join);
                } else {
                    companySurveyVo.setFieldSampling("");
                }
                //技术服务项目组人员
                companySurveyVo.setTechnicalPersons(String.join(",", strings1));
                //项目采样陪同人
                if (companySurveyVo.getSamplingCompany() == null) {
                    companySurveyVo.setSamplingCompany(companySurveyVo.getAccompany());
                }
                //修改为企业概况的testdata
//				//项目采样陪同时间
//				if(companySurveyVo.getReportStartDate()==null){
//					//修改为采样计划的采样日期去重
//
//					if(companySurveyVo.getReportEndDate()==null){
//						companySurveyVo.setSamplingDate("");
//					}else {
//						companySurveyVo.setSamplingDate( DateUtil.format(companySurveyVo.getReportEndDate(), "yyyy-MM-dd"));
//					}
//				}else {
//					if(companySurveyVo.getReportEndDate()==null){
//						companySurveyVo.setSamplingDate("");
//					}else {
//						String format = DateUtil.format(companySurveyVo.getReportStartDate(), "yyyy-MM-dd");
//						String format1 = DateUtil.format(companySurveyVo.getReportEndDate(), "yyyy-MM-dd");
//						if(format.equals(format1)){
//							companySurveyVo.setSamplingDate( format);
//						}else {
//							companySurveyVo.setSamplingDate( format+"-"+format1);
//						}
//
//					}
//				}


            }

        }

        return companySurveyVos;
    }

    /**
     * xin 评价项目公示-公示记录列表
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectEntity> listSys(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String applyPublicityStatus = (String) params.get("applyPublicityStatus");
//        QueryWrapper queryWrapper = new QueryWrapper();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("apply_publicity_status", 0);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company), "company", company);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(identifier), "identifier", identifier);
        queryWrapper.like(may.yuntian.anlian.utils.StringUtils.checkValNotNull(applyPublicityStatus), "apply_publicity_status", applyPublicityStatus);
        queryWrapper.orderByDesc("publicity_last_time");
        QueryWrapper<ProjectEntity> newQuery = queryWrapperByParamsAuth1();

        pageUtil2.startPage();
        List<ProjectEntity> list = projectMapper.selectList(newQuery);
        return list;
    }

    /**
     * xin 评价项目公示-信息公示列表
     *
     * @param company
     * @return
     */
    public List<CompanySurveyVo> publicityListPj(CompanySurveyVo company) {
        QueryWrapper<ProjectEntity> objectQueryWrapper = new QueryWrapper<>();
        List<CompanySurveyVo> companySurveyVos = new ArrayList<>();
        objectQueryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company.getCompany()), "ap.company", company.getCompany());
        objectQueryWrapper.like(may.yuntian.anlian.utils.StringUtils.isNotBlank(company.getIdentifier()), "ap.identifier", company.getIdentifier());
        //需要公示的项目
        objectQueryWrapper.lt("ap.status", 98);

//        objectQueryWrapper.gt("apd.report_issue","2022-05-30 23:59:59");
        objectQueryWrapper.gt("apd.report_cover_date", "2022-05-30 23:59:59");
        //需要公示的项目
        objectQueryWrapper.eq("ap.hide_status", 0);
        if (company.getType() != null) {//sys
            objectQueryWrapper.eq("ap.apply_publicity_status", 2);
            objectQueryWrapper.orderByDesc("ap.publicity_last_time", "ap.id");
//            objectQueryWrapper.and(i->i.eq("ap.type","现状").or().eq("ap.type","专篇"));

            pageUtil2.startPage();
            companySurveyVos = baseMapper.publicityListPj(objectQueryWrapper);
        } else {//pj

            SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
            boolean isPermitted = SecurityUtils.getSubject().isPermitted("anliantest:declare:update");
            QueryWrapper<ProjectEntity> projectEntityQueryWrapper = queryWrapperByParamsAuth(objectQueryWrapper);
            pageUtil2.startPage();
            //部门主管
            if (isPermitted && !("何佳吉".equals(sysUserEntity.getUsername()))) {//何佳吉是 个人权限
                objectQueryWrapper.and(i -> i.in("ap.apply_publicity_status", 1, 4).or(i1 -> i1.eq("ap.charge", sysUserEntity.getUsername()).in("ap.apply_publicity_status", 0, 3)));
                pageUtil2.startPage();
                if ("杭州安联".equals(sysUserEntity.getSubjection())) {
                    //权限控制
                    companySurveyVos = baseMapper.publicityListPj(projectEntityQueryWrapper);
                } else { // TODO: 2022-09-26  判断是否是宁波的 是宁波的 走宁波的查询
                    objectQueryWrapper.eq("us.subjection", "宁波安联");
                    companySurveyVos = baseMapper.publicityListPjNb(projectEntityQueryWrapper);
                }
            } else {//个人

                objectQueryWrapper.and(i -> i.eq("ap.apply_publicity_status", 0).or().eq("ap.apply_publicity_status", 3));
                objectQueryWrapper.eq("ap.charge", sysUserEntity.getUsername());

                // TODO: 2022-09-26 个人 不走权限
                pageUtil2.startPage();
                companySurveyVos = baseMapper.publicityListPj(projectEntityQueryWrapper);
            }
            //最后操作时间，id降序
            objectQueryWrapper.orderByDesc("ap.publicity_last_time", "ap.id");
        }
        if (companySurveyVos != null && companySurveyVos.size() > 0) {
            for (CompanySurveyVo companySurveyVo : companySurveyVos
            ) {
                //检测图
                List<SampleImgEntity> sampleImgEntityList = sampleImgService.list(new QueryWrapper<SampleImgEntity>().eq("project_id", companySurveyVo.getProjectId()));
                if (sampleImgEntityList != null && sampleImgEntityList.size() > 0) {
                    companySurveyVo.setImgs(sampleImgEntityList);
                } else {
                    companySurveyVo.setImgs(new ArrayList<SampleImgEntity>());
                }

                //现场调查人员
                List<ProjectUserEntity> list = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 110));
                if (list != null && list.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list
                    ) {
                        strings.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    companySurveyVo.setFieldInvestigatorsPj(join);
                } else {
                    companySurveyVo.setFieldInvestigatorsPj("");
                }

                //现场采样人员
                List<ProjectUserEntity> list2 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 120));
                if (list2 != null && list2.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list2
                    ) {
                        strings.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    companySurveyVo.setFieldSamplingPj(join);
                } else {
                    companySurveyVo.setFieldSamplingPj("");
                }
                //报告编制人员
                List<ProjectUserEntity> list3 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 140));
                if (list3 != null && list3.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list3
                    ) {
                        strings.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    companySurveyVo.setPPreparePerson(join);
                } else {
                    companySurveyVo.setPPreparePerson("");
                }
            }
        }
        return companySurveyVos;
    }

    /**
     * xin权限控制
     *
     * @param wappr
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(QueryWrapper<ProjectEntity> wappr) {
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
        List<String> projectTypeNames = new ArrayList<>();
        projectTypeNames.add("预评");
        projectTypeNames.add("专篇");
        projectTypeNames.add("控评");
        projectTypeNames.add("现状");
        projectTypeNames.add("职卫监督");
        projectTypeNames.add("职卫示范");
        QueryWrapper<ProjectEntity> queryWrapper = wappr
                // 部门权限控制
                .in("ap.dept_id", deptList)
                // 项目类型权限控制
                .in("ap.type", projectTypeNames);
        return queryWrapper;
    }


    /**
     * xin权限控制
     *
     * @param
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParamsAuth1() {
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
        List<String> projectTypeNames = new ArrayList<>();
        projectTypeNames.add("预评");
        projectTypeNames.add("专篇");
        projectTypeNames.add("控评");
        projectTypeNames.add("现状");
        projectTypeNames.add("职卫监督");
        projectTypeNames.add("职卫示范");
        System.out.println("111111111111111111=="+deptList);
        System.out.println("222222222222222222=="+projectTypeNames);
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        // 部门权限控制
        queryWrapper.in("dept_id", deptList)
                // 项目类型权限控制
                .in("type", projectTypeNames);

        return queryWrapper;
    }
}
