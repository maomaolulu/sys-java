package may.yuntian.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.modules.sys.entity.SysMenuEntity;
import may.yuntian.modules.sys.entity.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单管理
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();

	@Select("SELECT su.user_id, su.username FROM sys_role_menu srm\n" +
			"LEFT JOIN sys_user_role sur ON srm.role_id = sur.role_id\n" +
			"LEFT JOIN sys_user su ON su.user_id = sur.user_id\n" +
			"WHERE srm.menu_id = #{menuId} AND su.username IS NOT NULL\n" +
			"GROUP BY su.user_id")
	List<UserVo> havaMenuPeople(@Param("menuId") Long menuId);
}
