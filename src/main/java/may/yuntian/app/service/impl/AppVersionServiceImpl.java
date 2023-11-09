package may.yuntian.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.app.entity.AppVersion;
import may.yuntian.app.mapper.AppVersionMapper;
import may.yuntian.app.service.AppVersionService;
import may.yuntian.datasources.annotation.DataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * app版本管理
 * 业务逻辑层实现类
 *
 * @author zhanghao
 * @date 2022-04-12
 */
@Service("appVersionService")
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {


    @Override
    @DataSource(name = "second")
    public Integer ce() {

        return baseMapper.ces();
    }

    @Override
    public List<AppVersion> listAll(Map<String,Object> params) {
        String types = (String)params.get("types");
        String name =  (String)params.get("name");
        List<AppVersion> list = this.list(new QueryWrapper<AppVersion>().eq(StringUtils.isNotBlank(types), "types", types)
                .eq(StringUtils.isNotBlank(name), "name", name)
                .orderByDesc("id")
        );
        return list;
    }
}
