package may.yuntian.anlian.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * 新闻公告数据持久层接口
 *
 * @author ZhangHao
 * @date 2021-05-06
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticeEntity> {
    //未点赞  点赞操作
    @Update("UPDATE t_notice SET like_numbers = like_numbers+1 ,like_state=2 WHERE id = #{id}")
    void updateLikeNumbersIncrease(Long id);
    //已点赞  取消点赞操作
    @Update("UPDATE t_notice SET like_numbers = like_numbers-1 ,like_state=1 WHERE id = #{id}")
    void updateLikeNumbersReduce(Long id);

}
