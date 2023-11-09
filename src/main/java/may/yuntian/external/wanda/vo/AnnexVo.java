package may.yuntian.external.wanda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.external.wanda.entity.AffixRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 附件vo
 * @author: liyongqiang
 * @create: 2023-03-21 16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnexVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 项目id **/
    private Long projectId;
    /** 附件列表 **/
    private List<AffixRecord> affixRecordList;

}
