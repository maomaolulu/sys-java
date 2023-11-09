package may.yuntian.anlianwage.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlianwage.mapper.PerformanceLevelMapper;
import may.yuntian.anlianwage.entity.PerformanceLevelEntity;
import may.yuntian.anlianwage.service.PerformanceLevelService;

/**
 * 目标产出等级表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Service("performanceLevelService")
public class PerformanceLevelServiceImpl extends ServiceImpl<PerformanceLevelMapper, PerformanceLevelEntity> implements PerformanceLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PerformanceLevelEntity> page = this.page(
                new Query<PerformanceLevelEntity>().getPage(params),
                new QueryWrapper<PerformanceLevelEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 根据类型返回数组
     * @param type
     * @return
     */
    public List<PerformanceLevelEntity> list(String type) {
        List<PerformanceLevelEntity> list = baseMapper.selectList(new QueryWrapper<PerformanceLevelEntity>()
                .eq("type",type)
        );
        return list;
    }
}
