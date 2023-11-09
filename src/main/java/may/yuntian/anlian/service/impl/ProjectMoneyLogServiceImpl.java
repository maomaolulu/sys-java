package may.yuntian.anlian.service.impl;

import java.util.Map;
import java.util.List;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import may.yuntian.anlian.mapper.ProjectMoneyLogMapper;
import may.yuntian.anlian.entity.ProjectMoneyLogEntity;
import may.yuntian.anlian.service.ProjectMoneyLogService;

/**
 * 项目金额修改日志
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-04-03 08:38:30
 */
@Service("projectMoneyLogService")
public class ProjectMoneyLogServiceImpl extends ServiceImpl<ProjectMoneyLogMapper, ProjectMoneyLogEntity> implements ProjectMoneyLogService {

    @Override
    public List<ProjectMoneyLogEntity> queryPage(Map<String, Object> params) {
        QueryWrapper<ProjectMoneyLogEntity> queryWrapper = new QueryWrapper<>();

        List<ProjectMoneyLogEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }

}