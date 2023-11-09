package may.yuntian.external.province.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.province.entity.EconomicType;
import may.yuntian.external.province.mapper.EconomicTypeMapper;
import may.yuntian.external.province.service.EconomicTypeService;
import may.yuntian.external.province.vo.EconomicTypeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 13:34
 */
@Service("economicTypeService")
public class EconomicTypeServiceImpl extends ServiceImpl<EconomicTypeMapper, EconomicType> implements EconomicTypeService {

    @Resource
    private EconomicTypeMapper economicTypeMapper;

    /**
     * 查询经济类型及编码列表
     */
    @Override
    public List<EconomicTypeVo> getEconomicTypeCode() {
        List<EconomicTypeVo> economicTypeVoList = economicTypeMapper.getEconomicTypeAll();
        return economicTypeVoList.stream()
                .filter(economicTypeVo -> economicTypeVo.getParentId() == 0)
                .map(economicTypeVo -> {
                    economicTypeVo.setChildrenList(getEconomicTypeChildren(economicTypeVo, economicTypeVoList));
                    return economicTypeVo;
                }).collect(Collectors.toList());
    }

    /**
     * 递归查询子节点
     * @param root
     * @param economicTypeVoList
     * @return
     */
    private List<EconomicTypeVo> getEconomicTypeChildren(EconomicTypeVo root, List<EconomicTypeVo> economicTypeVoList) {
        return economicTypeVoList.stream()
                .filter(economicTypeVo -> economicTypeVo.getParentId().intValue() == root.getId().intValue())
                .map(economicTypeVo -> {
                    economicTypeVo.setChildrenList(getEconomicTypeChildren(economicTypeVo, economicTypeVoList));
                    return economicTypeVo;
                }).collect(Collectors.toList());
    }

}
