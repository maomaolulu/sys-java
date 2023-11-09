package may.yuntian.anlian.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业名称 显示层对象
 * Web向模板渲染引擎层传输的对象
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-28
 */
public class ProjectAchiveVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	private Long id;

    /**
     * 报告签发日期
     */
	private Date reportIssue;
    /**
     * 报告移交质控日期
     */
    private Date reportAccept;
	
	/**
	 * 设置：主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getId() {
		return id;
	}

    public Date getReportIssue() {
        return reportIssue;
    }

    public void setReportIssue(Date reportIssue) {
        this.reportIssue = reportIssue;
    }

    public Date getReportAccept() {
        return reportAccept;
    }

    public void setReportAccept(Date reportAccept) {
        this.reportAccept = reportAccept;
    }
}
