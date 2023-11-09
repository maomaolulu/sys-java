package may.yuntian.anlian.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.NoticeLikeUserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 新闻点赞数据持久层接口
 *
 * @author ZhangHao
 * @date 2021-05-06
 */
@Mapper
public interface NoticeLikeUserMapper extends BaseMapper<NoticeLikeUserEntity> {


}
