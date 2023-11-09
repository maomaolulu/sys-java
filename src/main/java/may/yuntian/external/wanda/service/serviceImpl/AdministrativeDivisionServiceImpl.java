package may.yuntian.external.wanda.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.wanda.entity.AdministrativeDivision;
import may.yuntian.external.wanda.mapper.AdministrativeDivisionMapper;
import may.yuntian.external.wanda.service.AdministrativeDivisionService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-03-06 15:14
 */
@Service("administrativeDivisionService")
public class AdministrativeDivisionServiceImpl extends ServiceImpl<AdministrativeDivisionMapper, AdministrativeDivision> implements AdministrativeDivisionService {
}
