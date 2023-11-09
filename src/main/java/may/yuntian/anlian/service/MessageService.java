package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.MessageEntity;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-03 19:25
 */
public interface MessageService extends IService<MessageEntity> {

    /**
     * 新建一条的发送和多条消息接收
     */
    void newMessage(MessageEntity messageEntity, List<Long> receiveIds);
}
