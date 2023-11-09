package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.dto.WeComMessageDTO;
import may.yuntian.anlian.entity.WeComMessageEntity;
import may.yuntian.common.utils.PageUtils;

import java.util.Map;


public interface WeComMessageService extends IService<WeComMessageEntity> {
    /**
     * 企业微信消息内容保存
     * @param weComMessageDTO
     */
    void saveWeComMessage(WeComMessageDTO weComMessageDTO);

    /**
     * 分页查询消息列表
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);
}
