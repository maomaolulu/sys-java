package may.yuntian.external.province.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.province.entity.AreaCode;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-04-06 14:01
 */
public interface AreaCodeService extends IService<AreaCode> {

    /**
     * 技术服务地区下拉列表
     * @param province 省
     * @return
     */
    List<AreaCode> getServiceAreaMenu(String province);

}
