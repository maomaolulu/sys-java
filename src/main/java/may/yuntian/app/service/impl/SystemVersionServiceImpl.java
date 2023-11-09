package may.yuntian.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.app.entity.SystemVersion;
import may.yuntian.app.mapper.SystemVersionMapper;
import may.yuntian.app.service.SystemVersionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统版本详情
 * 业务逻辑层实现类
 *
 * @author LIXIN
 * @data 2022-10-12
 */
@Service("systemVersionService")
public class SystemVersionServiceImpl extends ServiceImpl<SystemVersionMapper, SystemVersion> implements SystemVersionService {




    @Override
    public List<SystemVersion> listAll(Map<String,Object> params) {
        String types = (String)params.get("systemType");
        List<SystemVersion> list = this.list(new QueryWrapper<SystemVersion>().eq(StringUtils.isNotBlank(types), "system_type", types)
        );
        return list;
    }
}
