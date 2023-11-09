package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import may.yuntian.anlian.dto.WeComMessageDTO;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.WeComMessageEntity;
import may.yuntian.anlian.mapper.WeComMessageMapper;
import may.yuntian.anlian.service.WeComMessageService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.Map;

/**
 * @Description 企业微信消息
 * @Date 2023/4/11 13:44
 * @Author maoly
 **/
@Service("weComMessageService")
public class WeComMessageServiceImpl  extends ServiceImpl<WeComMessageMapper, WeComMessageEntity> implements WeComMessageService {

    @Autowired
    private WeComMessageMapper weComMessageMapper;
    @Autowired
    private ProjectServiceImpl projectService;
    @Override
    public void saveWeComMessage(WeComMessageDTO weComMessageDTO) {
        ProjectEntity projectEntity = projectService.getProjectByIdentifier(weComMessageDTO.getProjectNo());
        if(projectEntity != null){
            WeComMessageEntity weComMessageEntity = new WeComMessageEntity();
            weComMessageEntity.setProjectNo(weComMessageDTO.getProjectNo());
            weComMessageEntity.setMessageDate(weComMessageDTO.getMessageDate());
            weComMessageEntity.setMessageContent(weComMessageDTO.getMessageContent());
            weComMessageEntity.setProjectName(projectEntity.getProjectName());
            weComMessageEntity.setSendUser(StringUtils.isBlank(projectEntity.getSalesmen()) ? "" : projectEntity.getSalesmen() );
            weComMessageEntity.setSendUserId(projectEntity.getSalesmenid() == null ? null : String.valueOf(projectEntity.getSalesmenid()));
            weComMessageEntity.setReceiveUser(StringUtils.isBlank(projectEntity.getCharge()) ? "" : projectEntity.getCharge());
            weComMessageEntity.setReceiveUserId(projectEntity.getChargeId() == null ? null : String.valueOf(projectEntity.getChargeId()));
            weComMessageEntity.setReceiveLeaderUser(StringUtils.isBlank(projectEntity.getUsername()) ? "" : projectEntity.getUsername());
            weComMessageEntity.setReceiveLeaderUserId(projectEntity.getUserid() == null ? null : String.valueOf(projectEntity.getUserid()));
            weComMessageEntity.setCreateDate(new Date());
            weComMessageMapper.insert(weComMessageEntity);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WeComMessageEntity> queryWrapper = queryWrapperByParams(params);
        IPage<WeComMessageEntity> page = this.page(new Query<WeComMessageEntity>().getPage(params),queryWrapper.orderByDesc("id"));
        return new PageUtils(page);
    }

    private QueryWrapper<WeComMessageEntity> queryWrapperByParams(Map<String, Object> params){
        String projectNo = (String)params.get("projectNo");
        String projectName = (String)params.get("projectName");
        String sendUserId = (String)params.get("sendUserId");
        String receiveUserId = (String)params.get("receiveUserId");
        String receiveLeaderUserId = (String)params.get("receiveLeaderUserId");
        String messageContent = (String)params.get("messageContent");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        QueryWrapper<WeComMessageEntity> queryWrapper = new QueryWrapper<WeComMessageEntity>()
                .like(com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNotNull(projectNo),"project_no", projectNo)
                .like(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(projectName),"project_name", projectName)
                .eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNotNull(sendUserId),"send_user_id", sendUserId)
                .eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNotNull(receiveUserId),"receive_user_id", receiveUserId)
                .eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNotNull(receiveLeaderUserId),"receive_leader_user_id", receiveLeaderUserId)
                .like(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(messageContent),"message_content", messageContent)
                .between(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(endDate),"message_date", startDate, endDate);
        return queryWrapper;
    }
}
