package may.yuntian.socket.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.socket.domain.AbuProjectNote;
import may.yuntian.socket.domain.dto.AbuSendNoteDTO;

import java.util.List;

/**
 * 项目留言Service接口
 *
 * @author yrb
 * @date 2023-04-06
 */
public interface IAbuProjectNoteService extends IService<AbuProjectNote> {

    /**
     *
     * @param abuSendNoteDTO 留言相关信息
     * @return 结果
     */
    void sendMessage(AbuSendNoteDTO abuSendNoteDTO);


    /**
     * 项目状态改变发送通知
     * @param abuSendNoteDTO
     */
    void sendStatusMessage(AbuSendNoteDTO abuSendNoteDTO);

    /**
     * 查询留言相关信息
     * @param projectId 项目编号
     * @return result
     */
    List<AbuProjectNote> selectProjectNoteList(String projectId);
}
