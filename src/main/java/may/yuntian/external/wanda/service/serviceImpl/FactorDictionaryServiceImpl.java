package may.yuntian.external.wanda.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.wanda.entity.FactorDictionary;
import may.yuntian.external.wanda.mapper.FactorDictionaryMapper;
import may.yuntian.external.wanda.service.FactorDictionaryService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-03-08 11:06
 */
@Service("factorDictionaryService")
public class FactorDictionaryServiceImpl extends ServiceImpl<FactorDictionaryMapper, FactorDictionary> implements FactorDictionaryService {

}
