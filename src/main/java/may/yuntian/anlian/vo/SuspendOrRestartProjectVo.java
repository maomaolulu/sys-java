package may.yuntian.anlian.vo;

import lombok.Data;
import may.yuntian.anliantest.pojo.Participant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用于市级项目申报
 * @author zhanghao
 * @date 2022-05-18
 */
@Data
public class SuspendOrRestartProjectVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 重启类型  重置  恢复
	 */
	private String restartType;
	/**
	 * projectId
	 */
	private Long projectId;

}
