package may.yuntian.anlian.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.anlian.mapper.ProjecarchiveMapper;
import may.yuntian.anlian.entity.ProjecarchiveEntity;
import may.yuntian.anlian.service.ProjecarchiveService;

/**
 * 项目归档文件目录
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-25 15:32:52
 */
@Service("projecarchiveService")
public class ProjecarchiveServiceImpl extends ServiceImpl<ProjecarchiveMapper, ProjecarchiveEntity> implements ProjecarchiveService {


	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjecarchiveEntity> page = this.page(
                new Query<ProjecarchiveEntity>().getPage(params),
                new QueryWrapper<ProjecarchiveEntity>()
        );

        return new PageUtils(page);
    }
    
    /**
     * 通过项目ID查询项目归档信息
     * @param projectId
     * @return
     */
    public ProjecarchiveEntity getByProjectId(Long projectId) {
    	ProjecarchiveEntity projecarchive = baseMapper.selectOne(new QueryWrapper<ProjecarchiveEntity>()
				.eq("project_id", projectId)
                .last("limit 1")
				);
    	
    	
    	return projecarchive;
	}

}
