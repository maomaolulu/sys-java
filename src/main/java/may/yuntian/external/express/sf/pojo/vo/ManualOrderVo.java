package may.yuntian.external.express.sf.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import may.yuntian.external.express.sf.pojo.entity.BusinessOrder;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 手工建单vo
 * @author: liyongqiang
 * @create: 2023-05-24 16:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ManualOrderVo implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 收寄双方信息 **/
    private List<ContactInfo> contactInfoList;
    /** 业务表：托寄物信息 + 物流信息 **/
    private BusinessOrder businessOrder;

}
