package may.yuntian.anlian.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import may.yuntian.anlian.entity.CategoryEntity;


/**
 * 类型信息记录
 * 数据持久层接口
 *
 * @author LiXin
 * @date 2020-12-10
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {


    /**
     * 通过模块查询出没有子集的子集列表
     *
     * @param module
     * @return
     */
    @Select("select id,name,name_en,pid,is_old, (select count(*) from t_category where pid=t.id) as child from t_category as t where module = #{module} HAVING child = 0")
    List<CategoryEntity> getList(@Param("module") String module);

    /**
     * 获取项目类型(及归属合同类型)
     *
     * @return 数据
     */
    @Select("SELECT DISTINCT tc1.`name`, tc2.`name` AS module \n" +
            "FROM t_category tc1 LEFT JOIN t_category tc2 ON tc1.pid = tc2.id \n" +
            "WHERE tc1.pid != 0 AND tc1.del_flag =0")
    List<Map<String, String>> getProjectType();

}
