package may.yuntian.modules.sys_v2.controller;

import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.entity.SysMenu;
import may.yuntian.modules.sys_v2.entity.SysUser;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.modules.sys_v2.entity.vo.LoginVo;
import may.yuntian.modules.sys_v2.entity.vo.RouterVo;
import may.yuntian.modules.sys_v2.service.SysLoginV2Service;
import may.yuntian.modules.sys_v2.service.SysMenuV2Service;
import may.yuntian.modules.sys_v2.service.impl.SysPermissionService;
import may.yuntian.modules.sys_v2.utils.DateUtils;
import may.yuntian.modules.sys_v2.utils.ShiroUtilsV2;
import may.yuntian.modules.sys_v2.utils.SpringUtils;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录信息获取
 *
 * @author hjy
 */
@RestController
public class SysLoginV2Controller {

    /**
     * 请求有效期 3分钟
     */
    private static final long DATE_VALIDITY_PERIOD = 180000L;

    private final SysMenuV2Service menuService;

    private final SysPermissionService permissionService;

    private final SysLoginV2Service loginV2Service;

    public SysLoginV2Controller(SysMenuV2Service menuService, SysPermissionService permissionService, SysLoginV2Service loginV2Service) {
        this.menuService = menuService;
        this.permissionService = permissionService;
        this.loginV2Service = loginV2Service;
    }

    /**
     * 获取用户及路由信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo(String type) {
        SysUser user = ShiroUtilsV2.getUserEntity();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId(), type);
        List<RouterVo> menuList = menuService.buildMenus(menus);
        ajax.put("menuList", menuList);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = ShiroUtilsV2.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId, "");
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("test")
    @AuthCode(url = "testMenu", system = "sys")
    public AjaxResult getTest(AuthCodeVo authCode) {
        return AjaxResult.success(authCode);
    }

    /**
     * 获取token及其他信息
     */
    @PostMapping("/anlian/interface/sys/out/login")
    public AjaxResult login(@RequestBody LoginVo loginVo) {
        //请求时间戳
        Long timeStamp = loginVo.getTimeStamp();
        if (StringUtils.isEmpty(loginVo.getSecretKey())) {
            return AjaxResult.error("系统密钥不能为空！");
        } else if (StringUtils.isEmpty(loginVo.getEmail())) {
            return AjaxResult.error("邮箱不能为空！");
        } else if (StringUtils.isNull(timeStamp)) {
            return AjaxResult.error("时间戳不能为空");
        } else {
            //时间戳差值
            long l = DateUtils.getNowDate().getTime() - timeStamp;
            if (l > DATE_VALIDITY_PERIOD || l < -30000L) {
                return AjaxResult.error("请求已过期，请重新发起！");
            }
            //验证密钥
            String secretKey = SpringUtils.getRequiredProperty("anlian.sys.secretKey");
            if (!loginVo.getSecretKey().equals(secretKey)) {
                return AjaxResult.error("系统密钥无效，请联系管理员！");
            }
            //登录
            try {
                return AjaxResult.success(loginV2Service.login(loginVo));
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
        }
    }
}
