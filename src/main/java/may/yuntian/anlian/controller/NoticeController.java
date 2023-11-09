package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.CommentEntity;
import may.yuntian.anlian.entity.NoticeEntity;
import may.yuntian.anlian.entity.NoticeLikeUserEntity;
import may.yuntian.anlian.service.CommentService;
import may.yuntian.anlian.service.NoticeLikeUserService;
import may.yuntian.anlian.service.NoticeService;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDeptService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 新闻公告管理
 * WEB请求处理层
 *
 * @author ZhangHao
 *
 * @date 2021-05-06
 */
@RestController
@Api(tags="新闻公告")
@RequestMapping("anlian/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private NoticeLikeUserService noticeLikeUserService;
	@Autowired
	private SysDeptService sysDeptService;


	/**
	 * 用于新闻公告的分页查询列表
	 */
	@GetMapping("/list")
	@ApiOperation("获取新闻公告列表")
	@RequiresPermissions("anlian:notice:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = noticeService.queryPage(params);

		return R.ok().put("page", page);
	}

	
	/**
	 * 用于新闻公告的分页查询列表
	 */
	@GetMapping("/getNewNotice")
	@ApiOperation("获取最新的新闻公告列表")
//	@RequiresPermissions("anlian:notice:list")
	public R getNewNotice(){
		List<NoticeEntity> list = noticeService.getNewNotice();

		return R.ok().put("list", list);
	}
	

	@GetMapping("/info/{id}")
	@ApiOperation("根据ID查询详情")
	@RequiresPermissions("anlian:notice:info")
	public R info(@PathVariable("id") Long id) {
		NoticeEntity notice = noticeService.getById(id);
		notice.setNumber(notice.getNumber()+1);
		noticeService.updateById(notice);
		List<CommentEntity> commentEntities = commentService.selectByNoticeIdList(id);
		List<NoticeLikeUserEntity> noticeLikeUserEntities = noticeLikeUserService.selectByNoticeIdList(notice.getId());
		if(noticeLikeUserEntities.size()>0&&noticeLikeUserEntities!=null){
			notice.setLikeState(2);
		}
		return R.ok().put("notice", notice).put("commentEntities",commentEntities);

	}


	@PostMapping("/save")
	@SysLog("新增新闻公告记录")
	@ApiOperation("新增新闻公告记录")
	@RequiresPermissions("anlian:notice:save")
	public R save(@RequestBody NoticeEntity noticeEntity){
		SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
		SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
		noticeEntity.setAuthor(sysUserEntity.getUsername());
		noticeEntity.setDeptName(sysDeptEntity.getName());
		noticeService.save(noticeEntity);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改新闻公告记录")
	@ApiOperation("修改新闻公告记录")
	@RequiresPermissions("anlian:notice:update")
	public R update(@RequestBody NoticeEntity noticeEntity){
		noticeService.updateById(noticeEntity);

		return R.ok();
	}
	/**
	 * 修改点赞次数
	 */
	@PostMapping("/updateLikeNumbers/{id}")
	@SysLog("修改点赞次数")
	@ApiOperation("修改点赞次数")
	public R updateLikeNumbers(@PathVariable("id") Long id){
		noticeService.updateLikeNumbers(id);
		return R.ok();
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除新闻公告记录")
	@ApiOperation("删除新闻公告记录")
	@RequiresPermissions("anlian:notice:delete")
	public R delete(@RequestBody Long[] ids) {
		noticeService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

