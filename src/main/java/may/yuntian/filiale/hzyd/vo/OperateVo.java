package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.modules.sys.entity.SysUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目：下发、确认vo
 *
 * @author: liyongqiang
 * @create: 2023-08-15 14:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目负责人id
     */
    private Long projectLeaderId;
    /**
     * 项目负责人
     */
    private SysUserEntity projectLeader;
    /**
     * 项目ids
     */
    private List<Long> projectIds;
    /**
     * 项目完成日期
     */
    private String projectFinishDate;

}
