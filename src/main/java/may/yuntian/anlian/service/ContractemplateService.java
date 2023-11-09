package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ContractemplateEntity;

import java.util.Map;

/**
 * 合同模板共同信息
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
public interface ContractemplateService extends IService<ContractemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

