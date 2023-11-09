package may.yuntian.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import may.yuntian.modules.sys.entity.SysConlogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录信息统计
 * 
 * @author LiXin
 * @date 2021-04-30
 */
@Mapper
public interface SysConlogDao extends BaseMapper<SysConlogEntity> {
	
}
