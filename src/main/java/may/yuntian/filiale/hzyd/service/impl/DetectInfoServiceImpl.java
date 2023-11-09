package may.yuntian.filiale.hzyd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.mapper.DetectInfoMapper;
import may.yuntian.filiale.hzyd.service.DetectInfoService;
import may.yuntian.filiale.hzyd.vo.DetectInfoQueryVo;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: lixin
 */
@Service("detectInfoService")
public class DetectInfoServiceImpl extends ServiceImpl<DetectInfoMapper, DetectInfo> implements DetectInfoService {


    @Override
    public List<DetectInfo> getPageList(DetectInfoQueryVo detectInfoQueryVo) {
        QueryWrapper<DetectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category",detectInfoQueryVo.getCategory());
        queryWrapper.eq(StringUtils.checkValNotNull(detectInfoQueryVo.getType()),"type",detectInfoQueryVo.getType());
        queryWrapper.eq(StringUtils.checkValNotNull(detectInfoQueryVo.getName()),"name",detectInfoQueryVo.getName());
        queryWrapper.eq("del_flag",0);
        queryWrapper.orderByAsc("type");
        pageUtil2.startPage();
        List<DetectInfo> list = baseMapper.selectList(queryWrapper);
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInfo(DetectInfo entity) {
        String userName = ShiroUtils.getUserEntity().getUsername();
        entity.setCreateBy(userName);
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(DetectInfo entity) {
        String userName = ShiroUtils.getUserEntity().getUsername();
        entity.setUpdateBy(userName);
        entity.setUpdateTime(new Date());
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delFlagById(Long[] ids) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("id", Arrays.asList(ids));
        updateWrapper.set("del_flag",1);
        return super.update(updateWrapper);
    }
}
