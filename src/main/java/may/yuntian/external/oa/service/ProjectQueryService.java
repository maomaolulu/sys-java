package may.yuntian.external.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.external.oa.vo.ProjectAllQueryVo;
import may.yuntian.external.oa.vo.ProjectQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 我的项目service接口层
 *
 * @author mi
 * @Create 2023-3-31 15:50:43
 */
public interface ProjectQueryService extends IService<ProjectEntity> {

    /**
     * 项目编号+受检单位筛选我的项目
     * @param params
     * @return
     */
    List<ProjectQueryVo> selectByIdentifierOrCompany(Map<String, Object> params);

    /**
     * 项目编号+受检单位+业务员查询所有公司项目
     * @param params
     * @return
     */
    List<ProjectAllQueryVo> selectByIdentifierOrCompanyOrSaleMen(Map<String, Object> params);

    /**
     * 测试 获取ip
     * @param ip
     */
    void getIp(String ip);
}
