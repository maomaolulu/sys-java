package may.yuntian.external.wanda.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.wanda.entity.AffixRecord;
import may.yuntian.external.wanda.mapper.AffixRecordMapper;
import may.yuntian.external.wanda.service.AffixRecordService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-03-07 13:52
 */
@Service("affixRecordService")
public class AffixRecordServiceImpl extends ServiceImpl<AffixRecordMapper, AffixRecord> implements AffixRecordService {

}
