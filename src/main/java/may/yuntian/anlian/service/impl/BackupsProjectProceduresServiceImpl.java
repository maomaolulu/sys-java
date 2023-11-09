package may.yuntian.anlian.service.impl;

import may.yuntian.anlian.entity.BackupsProjectProceduresEntity;
import may.yuntian.anlian.mapper.BackupsProjectProceduresMapper;
import may.yuntian.anlian.service.BackupsProjectProceduresService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;


/**
 * 项目流程表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-10-28 15:48:41
 */
@Service("backupsProjectProceduresService")
public class BackupsProjectProceduresServiceImpl extends ServiceImpl<BackupsProjectProceduresMapper, BackupsProjectProceduresEntity> implements BackupsProjectProceduresService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BackupsProjectProceduresEntity> page = this.page(
                new Query<BackupsProjectProceduresEntity>().getPage(params),
                new QueryWrapper<BackupsProjectProceduresEntity>()
        );

        return new PageUtils(page);
    }

}
