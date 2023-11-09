package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId,String type);

//	/**
//	 * 退出，修改token值
//	 * @param userId  用户ID
//	 */
//	void logout(long userId,String type);

    /**
     * 根据用户ID删除token
     * @param userId
     */
    void deleteToken(Long userId);

	/**
	 * xin redis 中创建Token及缓存信息
	 * @param userId
	 * @param type
	 * @return
	 */
	R newCreateToken(Long userId, String type);

	/**
	 * xin退出
	 * @param userId
	 * @param type
	 */
	void logoutNew(Long userId, String type);
}
