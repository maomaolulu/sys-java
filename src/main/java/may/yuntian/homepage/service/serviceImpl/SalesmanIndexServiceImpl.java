package may.yuntian.homepage.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.homepage.entity.SalesmanIndexEntity;
import may.yuntian.homepage.mapper.SalesmanIndexMapper;
import may.yuntian.homepage.service.SalesmanIndexService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-02-28 12:00
 */
@Service("salesmanIndexService")
public class SalesmanIndexServiceImpl extends ServiceImpl<SalesmanIndexMapper, SalesmanIndexEntity> implements SalesmanIndexService {
}
