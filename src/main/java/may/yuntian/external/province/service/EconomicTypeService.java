package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.EconomicType;
import may.yuntian.external.province.vo.EconomicTypeVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 13:34
 */
public interface EconomicTypeService extends IService<EconomicType> {

    /**
     * 查询经济类型及编码列表
     * @return list
     */
    List<EconomicTypeVo> getEconomicTypeCode();

}
