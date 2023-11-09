package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.SubstanceContrastResult;
import may.yuntian.external.province.vo.ResultCodeVo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-23 15:32
 */
public interface SubstanceContrastResultService extends IService<SubstanceContrastResult> {

    /**
     * 获取检测物质list
     * @return
     */
    List<SubstanceContrastResult> getSubstanceCodeList();

    /**
     * 物质-结果项及编码
     * @param subId anlian物质表id
     * @return
     */
    List<ResultCodeVo> getCheckItemCode(Long subId);
}
