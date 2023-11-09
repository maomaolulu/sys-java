package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.ProjecarchiveEntity;

import java.util.Map;

/**
 * 项目归档文件目录
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
public interface ProjecarchiveService extends IService<ProjecarchiveEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 通过项目ID查询项目归档信息
     * @param projectId
     * @return
     */
    ProjecarchiveEntity getByProjectId(Long projectId);
}

