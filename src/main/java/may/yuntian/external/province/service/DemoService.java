package may.yuntian.external.province.service;

import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.wanda.entity.ProjectInfo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-11-07 13:13
 */
public interface DemoService {

    /**
     * 省报送：项目列表
     *
     * @param info 筛选条件
     * @return list
     */
    List<BasicInfo> selectProvinceProjectList(BasicInfo info);

    /**
     * 万达仓：项目列表
     *
     * @param info 筛选条件
     * @return list
     */
    List<ProjectInfo> selectWarehouseList(ProjectInfo info);

}
