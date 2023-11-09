package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ChineseAddressEntity;
import may.yuntian.anlian.vo.ChineseAddressVo;

import java.util.List;

/**
 * @Author gy
 * @Date 2023/8/28 9:11
 */
public interface IChineseAddressService extends IService<ChineseAddressEntity> {

    /**
     * 查询树形结构地区数据
     * @param regionParentId 父级id
     */
    List<ChineseAddressVo> getRegions(String regionParentId);
}
