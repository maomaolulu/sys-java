package may.yuntian.publicity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.publicity.entity.ZjSampleImgEntity;
import may.yuntian.publicity.mapper.ZjSampleImgMapper;
import may.yuntian.publicity.service.ZjSampleImgService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 采样影像记录
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-25 00:31:49
 */
@Service("zjSampleImgService")
public class ZjSampleImgServiceImpl extends ServiceImpl<ZjSampleImgMapper, ZjSampleImgEntity> implements ZjSampleImgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String projectId = (String)params.get("projectId");//项目ID
        IPage<ZjSampleImgEntity> page = this.page(
                new Query<ZjSampleImgEntity>().getPage(params),
                new QueryWrapper<ZjSampleImgEntity>()
                .eq(StringUtils.isNotBlank(projectId),"project_id", projectId)
        );
        return new PageUtils(page);
    }


    /**
     * 根据项目ID获取采集影像列表
     * @param projectId
     * @return
     */
    public List<ZjSampleImgEntity> getListByProjectId(Long projectId){
        List<ZjSampleImgEntity> list = baseMapper.selectList(new QueryWrapper<ZjSampleImgEntity>().eq("project_id",projectId));
        return list;
    }

    /**
     * 先删除老数据 重新保存新数据
     * @param list
     */
    public void saveSampleImg(List<ZjSampleImgEntity> list){
        Long projectId = list.get(0).getProjectId();
//        List<SampleImgEntity> sampleImgList = getListByProjectId(projectId);
        this.remove(new QueryWrapper<ZjSampleImgEntity>().eq("project_id",projectId));
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        for (ZjSampleImgEntity sampleImg:list){
            sampleImg.setEditor(sysUserEntity.getUsername());
            this.save(sampleImg);
        }
    }

}
