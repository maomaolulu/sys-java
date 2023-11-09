package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.CommentEntity;
import may.yuntian.anlian.service.CommentService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 新闻评论
 * WEB请求处理层
 *
 * @author ZhangHao
 *
 * @date 2021-05-31
 */
@RestController
@Api(tags="新闻评论")
@RequestMapping("anlian/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;


	/**
	 * 用于新闻评论的分页查询列表
	 */
	@GetMapping("/list")
	@ApiOperation("获取新闻评论列表")
	@RequiresPermissions("anlian:comment:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = commentService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 添加
	 * @param commentEntity
	 * @return
	 */

	@PostMapping("/save")
	@SysLog("新增新闻评论记录")
	@ApiOperation("新增新闻评论记录")
	//@RequiresPermissions("anlian:comment:save")
	public R save(@RequestBody CommentEntity commentEntity){
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		commentEntity.setName(sysUserEntity.getUsername());
		commentEntity.setUserId(sysUserEntity.getUserId());
		commentService.save(commentEntity);

		return R.ok().put("commentEntity",commentEntity);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除新闻评论记录")
	@ApiOperation("删除新闻评论记录")
	//@RequiresPermissions("anlian:comment:delete")
	public R delete(@RequestBody Long[] ids) {
		commentService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

