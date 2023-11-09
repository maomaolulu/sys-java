package may.yuntian.wordgenerate.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.wordgenerate.entity.WordContractTypeEntity;
import may.yuntian.wordgenerate.mapper.WordContractTypeMapper;
import may.yuntian.wordgenerate.service.WordContractTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
@Service("wordContractTypeService")
public class WordContractTypeServiceImpl extends ServiceImpl<WordContractTypeMapper, WordContractTypeEntity> implements WordContractTypeService {

    @Override
    public List<WordContractTypeEntity> getList(){
        return this.list();
    }

}