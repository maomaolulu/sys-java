package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.mapper.BasicInfoMapper;
import may.yuntian.external.province.service.DemoService;
import may.yuntian.external.wanda.entity.ProjectInfo;
import may.yuntian.external.wanda.mapper.ProjectInfoMapper;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-11-07 13:13
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {

    @Resource
    private BasicInfoMapper basicInfoMapper;
    @Resource
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 省报送：项目列表
     *
     * @param info 筛选条件
     * @return list
     */
    @Override
    public List<BasicInfo> selectProvinceProjectList(BasicInfo info) {
        QueryWrapper<BasicInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(info.getEmpName()), "emp_name", info.getEmpName());
        wrapper.like(StrUtil.isNotBlank(info.getCode()), "code", info.getCode());
        wrapper.like(StrUtil.isNotBlank(info.getCheckType()), "check_type", info.getCheckType());
        wrapper.like(StrUtil.isNotBlank(info.getProjectDirectorName()), "project_director_name", info.getProjectDirectorName());
        wrapper.ge(StrUtil.isNotBlank(info.getSubmitDate()), "submit_date", info.getSubmitDate());
        wrapper.le(StrUtil.isNotBlank(info.getSubmitEndDate()), "submit_date", info.getSubmitEndDate());
        wrapper.eq("belong_company", ShiroUtils.getUserEntity().getSubjection());
        // viewer 1负责人，2质控   （没有主管：项目负责人--->质控）
//        wrapper.in(info.getViewer() == 1, "status", 1, 4, 5);
//        wrapper.in(info.getViewer() == 2, "status", 3, 5);
        wrapper.orderByDesc("submit_date");
        pageUtil2.startPage();
        return basicInfoMapper.selectList(wrapper);
    }

    /**
     * 万达仓：项目列表
     *
     * @param info 筛选条件
     * @return list
     */
    @Override
    public List<ProjectInfo> selectWarehouseList(ProjectInfo info) {
        QueryWrapper<ProjectInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(info.getComName()), "wpi.com_name", info.getComName());
        wrapper.like(StringUtils.isNotBlank(info.getProjectCode()), "wpi.project_code", info.getProjectCode());
        wrapper.like(StringUtils.isNotBlank(info.getType()), "ap.type", info.getType());
        wrapper.like(StrUtil.isNotBlank(info.getCheckType()), "check_type", info.getCheckType());
        wrapper.like(StringUtils.isNotBlank(info.getCharge()), "ap.charge", info.getCharge());
        wrapper.eq("wpi.belong_company", ShiroUtils.getUserEntity().getSubjection());
        wrapper.ge(StrUtil.isNotBlank(info.getStartTime()), "wpi.push_time", info.getStartTime());
        wrapper.le(StrUtil.isNotBlank(info.getEndTime()), "wpi.push_time", info.getEndTime());
//        wrapper.in(info.getViewer() == 2, "wpi.node_status", 3, 5);
        pageUtil2.startPage();
        return projectInfoMapper.getProjectInfoList(wrapper);
    }

}
