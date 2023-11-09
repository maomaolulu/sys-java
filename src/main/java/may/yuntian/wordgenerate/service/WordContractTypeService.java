package may.yuntian.wordgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.wordgenerate.entity.WordContractTypeEntity;

import java.util.List;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-19 10:51:31
 */
public interface WordContractTypeService extends IService<WordContractTypeEntity> {



    public List<WordContractTypeEntity> getList();
}

