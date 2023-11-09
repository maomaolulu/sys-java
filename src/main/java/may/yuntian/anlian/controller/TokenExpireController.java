package may.yuntian.anlian.controller;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;
import may.yuntian.modules.sys.service.SysUserTokenService;

/**
 * WEB请求处理层
 * 
 * @author LiXin
 * @date 2021-05-06
 */

@RestController
@Api(tags = "token过期管理")
@RequestMapping("anlian/tokenExpire")
public class TokenExpireController {

	@Autowired
	private SysUserTokenService userTokenService;
	
	private final static int EXPIRE = 1800 ;
	
	@GetMapping("/updateTokenTime/{isUpDate}")
	@ApiOperation("修改token过期时间")
	public R updateTokenTime(@PathVariable("isUpDate") Integer isUpDate) {
		
    	SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
    	Long userid = sysUserEntity.getUserId();
		//判断是否生成过token
		SysUserTokenEntity tokenEntity = userTokenService.getById(userid);
		Date expireTime = tokenEntity.getExpireTime();
		Date now = new Date();
		Long nowTime = now.getTime();
		if(isUpDate==1) {
				expireTime = new Date(nowTime + EXPIRE * 1000);
				tokenEntity.setExpireTime(expireTime);
				userTokenService.updateById(tokenEntity);
		}
    	
		return R.ok().put("expireTime", expireTime);
	}


}
