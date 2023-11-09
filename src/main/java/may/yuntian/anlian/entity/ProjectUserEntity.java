package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 *采样人员人员实体类
 *
 * @author LiXin
 * @data 2022-02-21
 */
@TableName("al_project_user")
public class ProjectUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *自增主键ID
     */
    @TableId
    private Long id;
    /**
     *项目ID
     */
    private Long projectId;
    /**
     *技术人员记录ID
     */
    private Long artisanId;
    /**
     *人员类型
     * 1:实际采样组员  2报告签字人员   3报告审核人员 4组长
     * 110: 现场调查(报告中)   120: 现场采样/检测(报告中)
     * 130: 实验室检测(报告中)   140: 项目负责人(评价项目为负责人  检评项目中负责人和编制人为同一人)(报告中)
     * 150:报告编制人(仅评价项目)(报告中)   160:报告调查陪同人（仅评价项目）170:报告采样陪同人（仅评价项目）
     */
    private Integer types;
    /**
     *任务工作类型(组员，采样，调查，组长)
     */
    private String jobType;
    /**
     *人员用户ID
     */
    private Long userId;
    /**
     *人员用户名
     */
    private String username;
    /**
     *人员工号
     */
    private String jobNum;
    /**
     * 数据入库时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(Long artisanId) {
        this.artisanId = artisanId;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ProjectUserEntity{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", artisanId=" + artisanId +
                ", types=" + types +
                ", jobType='" + jobType + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", jobNum='" + jobNum + '\'' +
                '}';
    }
}
