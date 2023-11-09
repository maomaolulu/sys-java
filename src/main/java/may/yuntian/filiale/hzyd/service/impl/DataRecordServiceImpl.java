package may.yuntian.filiale.hzyd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.filiale.hzyd.entity.DataRecord;
import may.yuntian.filiale.hzyd.mapper.DataRecordMapper;
import may.yuntian.filiale.hzyd.service.DataRecordService;
import org.springframework.stereotype.Service;

/**
 * @author: liyongqiang
 * @create: 2023-08-11 18:35
 */
@Service("dataRecordService")
public class DataRecordServiceImpl extends ServiceImpl<DataRecordMapper, DataRecord> implements DataRecordService {
}
