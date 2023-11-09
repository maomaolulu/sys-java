package may.yuntian.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.app.entity.AppVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * APP版本管理
 * 
 * @author zhanghao
 * @date 2022-04-12
 */
@Mapper
public interface AppVersionMapper extends BaseMapper<AppVersion> {
    @Select("select aid from dede_addonarticle limit 1")
    Integer ces();
}
