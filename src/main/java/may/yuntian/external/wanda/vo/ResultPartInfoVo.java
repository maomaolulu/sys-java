package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 结果表部分通用字段
 * @author: liyongqiang
 * @create: 2023-03-10 11:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    private Long id;
    /** project_id **/
    private Long projectId;

    /** belong_third_prj_id **/
    private String belongThirdPrjId;

    /** 车间名称 **/
    private String workUnitName;
    /** 岗位/工种 **/
    private String workJobName;
    /** 劳动定员人数 **/
    private String workNum;
    /** 检测点 **/
    private String checkPointName;

    /** 更新者 **/
    private String updateBy;
    /** 更新时间 **/
    private Date updateTime;
    /** 标识该条记录是否被删除：0未，1删除 **/
    private int remove;

}
