package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.ResultItem;
import may.yuntian.external.province.vo.ResultCodeVo;

import java.util.List;
import java.util.Map;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 11:12
 */
public interface ResultItemService extends IService<ResultItem> {

    /**
     * 结果项调用逻辑校验
     * @param projectId
     * @return
     */
    Map<String, Object> assertResult(Long projectId);

    /**
     * 结果项查询
     * @param projectId
     * @return
     */
    Map<Integer, Map<String, List<ResultItem>>> resultItemList(Long projectId);

    /**
     * 批量保存结果项
     * @param resultMap
     * @return
     */
    int batchSaveResult(Map<Integer, Map<String, List<ResultItem>>> resultMap);

    /**
     * 结果项：名称-编码
     * @return map
     */
    List<ResultCodeVo> getResultNameCodeList();
}
