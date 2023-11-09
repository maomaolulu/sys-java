package may.yuntian.external.province.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.province.entity.SubstanceContrastResult;
import may.yuntian.external.province.mapper.SubstanceContrastResultMapper;
import may.yuntian.external.province.service.SubstanceContrastResultService;
import may.yuntian.external.province.vo.ResultCodeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liyongqiang
 * @create: 2023-04-23 15:32
 */
@Service("detectionSubstanceService")
public class DetectionSubstanceServiceImpl extends ServiceImpl<SubstanceContrastResultMapper, SubstanceContrastResult> implements SubstanceContrastResultService {

    @Resource
    private SubstanceContrastResultMapper substanceContrastResultMapper;

    /**
     * 获取检测物质list
     */
    @Override
    public List<SubstanceContrastResult> getSubstanceCodeList() {
        List<SubstanceContrastResult> contrastResultList = substanceContrastResultMapper.selectList(new QueryWrapper<SubstanceContrastResult>()
                .select("sub_id", "sub_name", "s_type")
                .isNotNull("sub_name"));
        return contrastResultList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 物质对应的结果项-编码-计量单位
     */
    @Override
    public List<ResultCodeVo> getCheckItemCode(Long subId) {
        List<SubstanceContrastResult> resultList = baseMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>().eq(SubstanceContrastResult::getSubId, subId));
        // 粉尘：因省平台并不区分总、呼尘，需特殊处理，避免报送时缺少必填的结果项
        if (resultList.get(0).getSType() == 2) {
            List<SubstanceContrastResult> selectList = baseMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>().eq(SubstanceContrastResult::getCheckItemCode, resultList.get(0).getCheckItemCode()));
            resultList.addAll(selectList);
        }
        return resultList.stream().distinct().map(substanceContrastResult -> {
            ResultCodeVo resultCodeVo = new ResultCodeVo();
            resultCodeVo.setItemName(substanceContrastResult.getItemName());
            resultCodeVo.setCheckItemCode(substanceContrastResult.getCheckItemCode());
            resultCodeVo.setResultItemCode(substanceContrastResult.getResultItemCode());
            resultCodeVo.setResultItemName(substanceContrastResult.getResultItemName());
            resultCodeVo.setUnit(substanceContrastResult.getUnit());
            return resultCodeVo;
        }).collect(Collectors.toList());
    }

}
