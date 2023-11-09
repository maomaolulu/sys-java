package may.yuntian.anlianwage.newCommission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlianwage.newCommission.dto.NewCommissionQueryDto;
import may.yuntian.anlianwage.newCommission.entity.NewCommissionEntity;
import may.yuntian.anlianwage.newCommission.vo.CommissionStatVo;

import java.util.List;
import java.util.Map;

/**
 * 新绩效提成表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-10-22 21:15:24
 */
public interface NewCommissionService extends IService<NewCommissionEntity> {

    List<NewCommissionEntity> queryPage(NewCommissionQueryDto newCommissionQueryDto);

    /**
     * 提成发放列表 month 月度待发放 year 年度待发放
     * @param newCommissionQueryDto
     * @return
     */
    List<NewCommissionEntity> accrualList(NewCommissionQueryDto newCommissionQueryDto);

    /**
     * 不分页查询列表 用于导出
     * @param newCommissionQueryDto
     * @return
     */
    List<NewCommissionEntity> exportList(NewCommissionQueryDto newCommissionQueryDto);
    public CommissionStatVo getCount(NewCommissionQueryDto newCommissionQueryDto);
    /**
     * 根据项目ID数组获取数据
     * @param ids
     * @return
     */
    public List<NewCommissionEntity> getListByProjectIdS(List<Long> ids);
}

