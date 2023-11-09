package may.yuntian.external.province.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.province.entity.AreaCode;
import may.yuntian.external.province.mapper.AreaCodeMapper;
import may.yuntian.external.province.service.AreaCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 14:01
 */
@Service("areaCodeService")
public class AreaCodeServiceImpl extends ServiceImpl<AreaCodeMapper, AreaCode> implements AreaCodeService{

    @Resource
    private AreaCodeMapper areaCodeMapper;

    /**
     * 技术服务地区：二级菜单（市、区）
     */
    @Override
    public List<AreaCode> getServiceAreaMenu(String province) {
        if (StrUtil.isBlank(province)) {
            throw new RRException("请求参数：项目所属省份不能为空！");
        }
        // 根据省份查询所有数据
        List<AreaCode> areaCodeList = areaCodeMapper.selectList(Wrappers.lambdaQuery(AreaCode.class).likeRight(StrUtil.isNotBlank(province), AreaCode::getFullName, province));
        // 省-市：最后五位都以0结尾
        List<AreaCode> cityList = areaCodeList.stream().filter(areaCode -> !StrUtil.equals(province,areaCode.getAreaName()) && StrUtil.endWith(areaCode.getAreaCode(), "00000")).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cityList)) {
            for (AreaCode areaCode : cityList) {
                // 省-市-区/县/市：根据省-市的前四位过滤 且 后三位为0
                List<AreaCode> childrenList = areaCodeList.stream().filter(area -> !StrUtil.equals(areaCode.getAreaName(),area.getAreaName()) && StrUtil.startWith(area.getAreaCode(), areaCode.getAreaCode().substring(0,4)) && StrUtil.endWith(area.getAreaCode(), "000")).collect(Collectors.toList());
                areaCode.setChildrenList(childrenList);
            }
        }
        return cityList;
    }


}
