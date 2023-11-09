package may.yuntian.filiale.hzyd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.filiale.hzyd.entity.DetectInfo;
import may.yuntian.filiale.hzyd.vo.DetectInfoQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author: lixin
 */
public interface DetectInfoService extends IService<DetectInfo> {
    List<DetectInfo> getPageList(DetectInfoQueryVo detectInfoQueryVo);

    boolean saveInfo(DetectInfo entity);

    boolean updateInfo(DetectInfo entity);

    boolean delFlagById(Long[] ids);
}
