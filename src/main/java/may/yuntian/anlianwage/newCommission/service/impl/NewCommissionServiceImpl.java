package may.yuntian.anlianwage.newCommission.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.List;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlianwage.newCommission.dto.NewCommissionQueryDto;
import may.yuntian.anlianwage.newCommission.vo.CommissionStatVo;
import may.yuntian.untils.pageUtil2;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import may.yuntian.anlianwage.newCommission.mapper.NewCommissionMapper;
import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;
import may.yuntian.anlianwage.newCommission.service.NewCommissionService;

/**
 * 新绩效提成表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-10-22 21:15:24
 */
@Service("newCommissionService")
public class NewCommissionServiceImpl extends ServiceImpl<NewCommissionMapper, NewCommissionEntity> implements NewCommissionService {

    @Override
    public List<NewCommissionEntity> queryPage(NewCommissionQueryDto newCommissionQueryDto) {
        QueryWrapper<NewCommissionEntity> queryWrapper = queryWrapper(newCommissionQueryDto);
        pageUtil2.startPage();
        List<NewCommissionEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }

    /**
     * 提成发放列表 month 月度待发放 year 年度待发放
     * @param newCommissionQueryDto
     * @return
     */
    public List<NewCommissionEntity> accrualList(NewCommissionQueryDto newCommissionQueryDto){
        QueryWrapper<NewCommissionEntity> queryWrapper = queryWrapper(newCommissionQueryDto);
        pageUtil2.startPage();
        List<NewCommissionEntity> list = baseMapper.selectList(queryWrapper);
        return list;
    }


    public CommissionStatVo getCount(NewCommissionQueryDto newCommissionQueryDto){
        QueryWrapper<NewCommissionEntity> queryWrapper = queryWrapper(newCommissionQueryDto);
        CommissionStatVo commissionStatVo = baseMapper.countListByWapper(queryWrapper);
        return commissionStatVo;
    }

    /**
     * 不分页查询列表 用于导出
     * @param newCommissionQueryDto
     * @return
     */
    public List<NewCommissionEntity> exportList(NewCommissionQueryDto newCommissionQueryDto){
        if (StringUtils.isNotBlank(newCommissionQueryDto.getCommissionStatus())){
            newCommissionQueryDto.setCommissionStatusMonth(newCommissionQueryDto.getCommissionStatus());
            newCommissionQueryDto.setCommissionStatusYear(newCommissionQueryDto.getCommissionStatus());
        }
        QueryWrapper<NewCommissionEntity> queryWrapper = queryWrapper(newCommissionQueryDto);

        List<NewCommissionEntity> list = baseMapper.selectList(queryWrapper);
        CommissionStatVo commissionStatVo = this.getCount(newCommissionQueryDto);
        NewCommissionEntity entity = new NewCommissionEntity();
        entity.setPerson("总计");
        entity.setAccrualAmount(commissionStatVo.getAccrualCount());
        entity.setAccrualAmountMonth(commissionStatVo.getMonthCount());
        entity.setAccrualAmountYear(commissionStatVo.getYearCount());
        list.add(entity);
        return list;
    }

    /**
     * 根据项目ID数组获取数据
     * @param ids
     * @return
     */
    public List<NewCommissionEntity> getListByProjectIdS(List<Long> ids){
        List<NewCommissionEntity> list = baseMapper.selectList(new QueryWrapper<NewCommissionEntity>().in("project_id",ids));
        return list;
    }

    /**
     * 查询统一处理
     * @param newCommissionQueryDto
     * @return
     */
    public QueryWrapper<NewCommissionEntity> queryWrapper(NewCommissionQueryDto newCommissionQueryDto){
        String person = newCommissionQueryDto.getPerson();//提成人
        String subjection = newCommissionQueryDto.getSubjection();//隶属公司
        String dept = newCommissionQueryDto.getDept();//所属部门
        String identifier = newCommissionQueryDto.getIdentifier();//项目编号
        String commissionType = newCommissionQueryDto.getCommissionType();//提成类型
        String commissionStatusMonth = newCommissionQueryDto.getCommissionStatusMonth();//月度提成状态
        String commissionStatusYear = newCommissionQueryDto.getCommissionStatusYear();//年度提成状态
        String accrualDateStart = newCommissionQueryDto.getAccrualDateStart();//计提日期-开始
        String accrualDateEnd = newCommissionQueryDto.getAccrualDateEnd();//计提日期-结束
        QueryWrapper<NewCommissionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(subjection),"subjection",subjection);
        queryWrapper.eq(StringUtils.isNotBlank(dept),"dept",dept);
        queryWrapper.eq(StringUtils.isNotBlank(commissionType),"commission_type",commissionType);
        queryWrapper.eq(StringUtils.isNotBlank(commissionStatusMonth),"commission_status_month",commissionStatusMonth);
        queryWrapper.eq(StringUtils.isNotBlank(commissionStatusYear),"commission_status_year",commissionStatusYear);
        queryWrapper.like(StringUtils.isNotBlank(person),"person",person);
        queryWrapper.like(StringUtils.isNotBlank(identifier),"identifier",identifier);
        queryWrapper.ge(StringUtils.isNotBlank(accrualDateStart),"accrual_date",accrualDateStart);
        queryWrapper.le(StringUtils.isNotBlank(accrualDateEnd),"accrual_date",accrualDateEnd);
        return queryWrapper;
    }

}