package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.dto.WeComMessageDTO;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.service.WeComMessageService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description 企业微信消息管理
 * @Date 2023/4/11 13:05
 * @Author maoly
 **/
@RestController
@SuppressWarnings("all")
@Api(tags="企业微信消息管理")
@RequestMapping("anlian/wecommessage")
public class WeComMessageController {

    @Autowired
    private WeComMessageService weComMessageService;

    @Autowired
    private ProjectService projectService;

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增")
    public R save(@RequestBody WeComMessageDTO weComMessageDTO){
        if(weComMessageDTO == null){
            return R.error("入参为空！");
        }
        if(StringUtils.isBlank(weComMessageDTO.getProjectNo())){
            return R.error("项目编号为空！");
        }
        if(StringUtils.isBlank(weComMessageDTO.getMessageDate())){
            return R.error("留言时间为空！");
        }
        if(StringUtils.isBlank(weComMessageDTO.getMessageContent())){
            return R.error("留言内容为空！");
        }
        if(projectService.notExistContractByIdentifier(weComMessageDTO.getProjectNo())) {
            return R.error("该项目编号不存在！");
        }
        weComMessageService.saveWeComMessage(weComMessageDTO);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询留言列表")
    //@RequiresPermissions("anlian:commission:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = weComMessageService.queryPage(params);
        return R.ok().put("page", page);
    }
}
