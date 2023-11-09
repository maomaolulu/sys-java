package may.yuntian.anlian.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ContraccustomTemplateMapper;
import may.yuntian.anlian.entity.ContraccustomTemplateEntity;
import may.yuntian.anlian.service.ContraccustomTemplateService;

/**
 * 合同模板自定义字段
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Service("contraccustomTemplateService")
public class ContraccustomTemplateServiceImpl extends ServiceImpl<ContraccustomTemplateMapper, ContraccustomTemplateEntity> implements ContraccustomTemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContraccustomTemplateEntity> page = this.page(
                new Query<ContraccustomTemplateEntity>().getPage(params),
                new QueryWrapper<ContraccustomTemplateEntity>()
        );

        return new PageUtils(page);
    }

}
