package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.IndustryCategory;
import may.yuntian.external.province.vo.IndustryCategoryVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-18 16:50
 */
public interface IndustryCategoryService extends IService<IndustryCategory> {

    /**
     * 查询行业分类与编码
     * @return list
     */
    List<IndustryCategoryVo> getIndustryTypeCode();

}
