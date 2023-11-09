package may.yuntian.anlian.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ChineseAddressEntity;
import may.yuntian.anlian.mapper.ChineseAddressMapper;
import may.yuntian.anlian.service.IChineseAddressService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.ChineseAddressVo;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description
 * @Date 2023/8/28 9:12
 * @Author gy
 **/
@Service
public class ChineseAddressServiceImpl extends ServiceImpl<ChineseAddressMapper, ChineseAddressEntity> implements IChineseAddressService {


    @Override
    public List<ChineseAddressVo> getRegions(String regionParentId) {
        QueryWrapper<ChineseAddressEntity> query = new QueryWrapper<>();
        if (StringUtils.isBlank(regionParentId)){
            query.eq("region_level", 1);
        }else {
            query.eq( "region_parent_id", regionParentId);
        }

        return JSON.parseArray(JSON.toJSONString(this.list(query)), ChineseAddressVo.class);
    }

}
