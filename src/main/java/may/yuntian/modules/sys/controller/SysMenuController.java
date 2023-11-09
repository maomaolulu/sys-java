package may.yuntian.modules.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.exception.RRException;
import may.yuntian.common.utils.Constant;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysMenuEntity;
import may.yuntian.modules.sys.service.ShiroService;
import may.yuntian.modules.sys.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@RestController
@Api(tags="系统菜单")
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	@ApiOperation("导航菜单")
	public R nav(@RequestParam Map<String, Object> params){
	    String type = "sys";
	    if(StringUtils.isNotBlank((String)params.get("type"))||(String)params.get("type")!=null){
	        type = (String)params.get("type");
        }
		List<SysMenuEntity> menuList = sysMenuService.getMenuListByUidAndtype(getUserId(),type);
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		List<SysMenuEntity> menuAllList = sysMenuService.list();
//		Set<String> permsList = shiroService.getHomePremsList();//首页权限
//		Set<String> timeNodeList = shiroService.getTimeNodeList();//时间节点权限
//		Set<String> userTimeNode = newCommission HashSet<>();
//		for(String perm:permissions) {
//			for (String tn:timeNodeList) {
//				if(perm.equals(tn)) {
//					userTimeNode.add(tn);
//				}
//			}
//		}

//		Set<String> userHomeList = newCommission HashSet<>();
//		for(String pe:permissions) {
//			for(String pl:permsList) {
//				if(pe.equals(pl)) {
//					userHomeList.add(pl);
//				}
//			}
//		}
		return R.ok().put("menuList", menuList).put("permissions", permissions).put("menuAllList", menuAllList);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@ApiOperation("显示所有菜单列表")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenuEntity> list(){
		List<SysMenuEntity> menuList = sysMenuService.list();
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
		}

		return menuList;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@ApiOperation("选择菜单(添加、修改菜单)")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		return R.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/info/{menuId}")
	@ApiOperation("菜单信息")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@PostMapping("/save")
	@ApiOperation("保存菜单")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);

		sysMenuService.save(menu);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@PostMapping("/update")
	@ApiOperation("修改菜单")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);

		sysMenuService.updateById(menu);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete/{menuId}")
	@ApiOperation("单条删除菜单")
	@RequiresPermissions("sys:menu:delete")
	public R delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return R.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}

	/**
	 * 菜单拥有人员查询
	 */
	@GetMapping("/havaMenuPeople")
	@ApiOperation("菜单信息")
	@RequiresPermissions("sys:menu:info")
	public R havaMenuPeople(Long menuId){
		return R.ok().put("data", sysMenuService.havaMenuPeople(menuId));
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}

		if(menu.getParentId() == null){
			throw new RRException("上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RRException("菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RRException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
