package may.yuntian.external.wanda.service;

import may.yuntian.external.wanda.vo.BusinessSystemDataVo;

/**
 * @author: liyongqiang
 * @create: 2023-03-10 11:25
 */
public interface DetectionResultService {

    /**
     * 检测结果修改保存
     * @param dataVo
     * @return
     */
    int detectionResultUpdate(BusinessSystemDataVo dataVo);

    /**
     * 检测结果信息，一键推送！
     * @param dataVo vo
     * @return 响应消息字符串
     */
    String resultDataPush(BusinessSystemDataVo dataVo);
}
