package may.yuntian.external.province.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.province.entity.IndustryCategory;
import may.yuntian.external.province.mapper.IndustryCategoryMapper;
import may.yuntian.external.province.service.IndustryCategoryService;
import may.yuntian.external.province.vo.IndustryCategoryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 16:50
 */
@Service("industryCategoryService")
public class IndustryCategoryServiceImpl extends ServiceImpl<IndustryCategoryMapper, IndustryCategory> implements IndustryCategoryService {

    @Resource
    private IndustryCategoryMapper industryCategoryMapper;

    /**
     * 查询行业分类与编码
     */
    @Override
    public List<IndustryCategoryVo> getIndustryTypeCode() {
        // 查询出行业分类表中所有记录
        List<IndustryCategoryVo> industryCategoryVoList = industryCategoryMapper.getCategoryTreeAll();
        return industryCategoryVoList.stream()
                // 查询出所有一级分类数据
                .filter(industryCategoryVo -> industryCategoryVo.getPid().intValue() == 0)
                .map(category -> {
                    // 传递当前的行业类别，和所有的行业分类，递归查询出每个一级分类下的子分类
                    category.setChildrenCategoryList(getChildren(category, industryCategoryVoList));
                    return category;
                }).collect(Collectors.toList());
    }

    /**
     * 递归查询子父类
     * @param root 当前节点的父级vo
     * @param categoryVoList 所有的行业分类
     * @return
     */
    private List<IndustryCategoryVo> getChildren(IndustryCategoryVo root, List<IndustryCategoryVo> categoryVoList) {
        return categoryVoList.stream()
                .filter(industryCategoryVo -> industryCategoryVo.getPid().intValue() == root.getId().intValue())
                .map(industryCategoryVo -> {
                    // 子菜单下可能还有子菜单，因此递归查询，查询出子菜单
                    industryCategoryVo.setChildrenCategoryList(getChildren(industryCategoryVo, categoryVoList));
                    return industryCategoryVo;
                }).collect(Collectors.toList());
    }
}
