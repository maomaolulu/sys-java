package may.yuntian.external.province.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.province.entity.BasicInfo;
import may.yuntian.external.province.entity.ParticipantTable;
import may.yuntian.external.province.entity.ResultItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据推送/回显vo（包含该项目所有信息）
 *
 * @author: liyongqiang
 * @create: 2023-04-11 09:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PushDataVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目基本信息
     **/
    private BasicInfo basicInfo;
    /**
     * 参与人员列表
     **/
    private List<ParticipantTable> participantList;
    /**
     * 结果map
     */
    private Map<Integer, Map<String, List<ResultItem>>> resultMap;


}
