package may.yuntian.anlian.service.impl;




import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.anlian.entity.ProjectProceduresEntity;
import may.yuntian.anlian.mapper.ProjectProceduresMapper;
import may.yuntian.anlian.service.ProjectProceduresService;
/**
 * 项目流程
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-02
 */
@Service("projectProceduresService")
public class ProjectProceduresServiceImpl extends ServiceImpl<ProjectProceduresMapper, ProjectProceduresEntity> implements ProjectProceduresService {

	public List<ProjectProceduresEntity> listProceduresByProjectId(Long projectId) {
		List<ProjectProceduresEntity> projectProcedures = this.list(new QueryWrapper<ProjectProceduresEntity>()
				.eq("project_id", projectId)
				);
		return projectProcedures;
	}
	
	
	/**
	 * 通过项目ID和状态获取项目流程信息
	 * @param projectId
	 * @param status
	 * @return
	 */
	public List<ProjectProceduresEntity> getByProjectIdAndStatus(Long projectId,Integer status) {
		List<ProjectProceduresEntity> proceduresEntityList = this.list(new QueryWrapper<ProjectProceduresEntity>()
				.eq("project_id", projectId)
				.eq("status", status)
				.orderByDesc("createtime")
				);
		
		
		return proceduresEntityList;
	}
	
	/**
	 * 获取一条
	 * @param projectId
	 * @param status
	 * @return
	 */
	public ProjectProceduresEntity getProceduresEntity(Long projectId,Integer status) {
		ProjectProceduresEntity proceduresEntity = this.getOne(new QueryWrapper<ProjectProceduresEntity>()
				.eq("project_id", projectId)
				.eq("status", status)
				.last("limit 1")
				);
		
		return proceduresEntity;
	}

    /**
     * 获取最后一条信息
     */
    public ProjectProceduresEntity getProceduresLast(Long projectId) {
        ProjectProceduresEntity proceduresEntity = this.getOne(new QueryWrapper<ProjectProceduresEntity>()
                .eq("project_id", projectId)
                .orderByDesc("status")
                .last("limit 1")
        );

        return proceduresEntity;
    }

    /**
	 *根据项目ID删除项目流程信息
	 */
	public void deleteByProjectId(Long projectId) {
		 baseMapper.delete(new QueryWrapper<ProjectProceduresEntity>().eq("project_id", projectId));
	}

    /**
     *根据项目ID和状态删除项目流程信息
     */
    public void deleteByProjectIdAndType(Long projectId,Integer status) {
        int count = baseMapper.selectCount(new QueryWrapper<ProjectProceduresEntity>().eq("project_id", projectId).eq("status", status));
        if (count > 0){
            baseMapper.delete(new QueryWrapper<ProjectProceduresEntity>().eq("project_id", projectId).eq("status", status));
        }
    }

}
