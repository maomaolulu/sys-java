package may.yuntian.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 根据用户邮箱，查询系统用户
	 */
	SysUserEntity queryByEmail(String email);

}
