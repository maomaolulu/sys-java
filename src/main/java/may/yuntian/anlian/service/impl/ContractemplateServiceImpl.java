package may.yuntian.anlian.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ContractemplateMapper;
import may.yuntian.anlian.entity.ContractemplateEntity;
import may.yuntian.anlian.service.ContractemplateService;

/**
 * 合同模板共同信息
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Service("contractemplateService")
public class ContractemplateServiceImpl extends ServiceImpl<ContractemplateMapper, ContractemplateEntity> implements ContractemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContractemplateEntity> page = this.page(
                new Query<ContractemplateEntity>().getPage(params),
                new QueryWrapper<ContractemplateEntity>()
        );

        return new PageUtils(page);
    }

}
