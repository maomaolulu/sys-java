package may.yuntian.filiale.hzyd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.filiale.hzyd.dto.ContractDto;
import may.yuntian.filiale.hzyd.dto.ProjectDto;

import java.io.Serializable;
import java.util.List;

/**
 * 数据记录vo：包含合同、所有项目信息
 *
 * @author: liyongqiang
 * @create: 2023-08-11 14:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同信息
     */
    private ContractDto contractDto;
    /**
     * 项目信息
     */
    private List<ProjectDto> projectDtoList;


}
