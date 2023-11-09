package may.yuntian.external.province.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import may.yuntian.external.province.entity.EconomicType;

import java.io.Serializable;
import java.util.List;

/**
 * 省报送-经济类型vo
 * @author: liyongqiang
 * @create: 2023-04-20 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EconomicTypeVo extends EconomicType implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<EconomicTypeVo> childrenList;

}
