package may.yuntian.external.wanda.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.wanda.entity.ChemicalDustFactor;
import may.yuntian.external.wanda.mapper.ChemicalDustFactorMapper;
import may.yuntian.external.wanda.service.ChemicalDustFactorService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-03-08 11:02
 */
@Service("chemicalDustFactorService")
public class ChemicalDustFactorServiceImpl extends ServiceImpl<ChemicalDustFactorMapper, ChemicalDustFactor> implements ChemicalDustFactorService {

}
