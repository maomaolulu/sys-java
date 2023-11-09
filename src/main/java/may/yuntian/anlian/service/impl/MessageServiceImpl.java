package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.MessageEntity;
import may.yuntian.anlian.entity.MessageReceiveEntity;
import may.yuntian.anlian.mapper.MessageMapper;
import may.yuntian.anlian.service.MessageReceiveService;
import may.yuntian.anlian.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-03 19:26
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

    @Autowired
    private MessageReceiveService messageReceiveService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newMessage(MessageEntity messageEntity, List<Long> receiveIds) {
        this.save(messageEntity);
        List<MessageReceiveEntity> saveList = new ArrayList<>();
        for (Long receiveId : receiveIds){
            MessageReceiveEntity one =  new MessageReceiveEntity();
            one.setMessageId(messageEntity.getId());
            one.setReceiverId(receiveId);
            one.setReadingStatus(0);
            saveList.add(one);
        }
        messageReceiveService.saveBatch(saveList);
    }
}
