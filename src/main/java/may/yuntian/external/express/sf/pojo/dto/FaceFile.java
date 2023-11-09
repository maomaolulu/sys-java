package may.yuntian.external.express.sf.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 云打印面单接口2.0-success报文：files
 * @author: liyongqiang
 * @create: 2023-06-07 19:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FaceFile implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    private Integer pageCount;

    private Integer seqNo;

    private Integer documentSize;

    /** 电子面板pdf的url（get请求） **/
    private String url;

    /** token：X-Auth-token **/
    private String token;

    private Integer areaNo;

    private Integer pageNo;

    /** 运单号 **/
    private String waybillNo;

}
