package may.yuntian.anliantest.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anliantest.entity.PublicityResults;
import may.yuntian.anliantest.mapper.PublicityResultsMapper;
import may.yuntian.anliantest.service.PublicityResultsService;
import org.springframework.stereotype.Service;

/**
 * 项目公示
 * 业务逻辑层实现类
 *
 * @author zhanghao
 * @date 2022-04-18
 */
@Service("publicityResultsService")
public class PublicityResultsServiceImpl extends ServiceImpl<PublicityResultsMapper, PublicityResults> implements PublicityResultsService {


}
