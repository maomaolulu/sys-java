package may.yuntian.anlian.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ContraccustomDataMapper;
import may.yuntian.anlian.entity.ContraccustomDataEntity;
import may.yuntian.anlian.service.ContraccustomDataService;

/**
 * 合同模板自定义字段数据
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:53
 */
@Service("contraccustomDataService")
public class ContraccustomDataServiceImpl extends ServiceImpl<ContraccustomDataMapper, ContraccustomDataEntity> implements ContraccustomDataService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContraccustomDataEntity> page = this.page(
                new Query<ContraccustomDataEntity>().getPage(params),
                new QueryWrapper<ContraccustomDataEntity>()
        );

        return new PageUtils(page);
    }

}
