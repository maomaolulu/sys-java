package may.yuntian.publicity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mi
 */
@Data
@TableName("al_project_report")
public class ProjectDownloadNumberEntity implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 下载次数
     */
    private Integer downloadNumber;

}
