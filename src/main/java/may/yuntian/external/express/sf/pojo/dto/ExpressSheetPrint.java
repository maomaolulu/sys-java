package may.yuntian.external.express.sf.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 云打印面单dto
 * @author: liyongqiang
 * @create: 2023-06-07 13:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExpressSheetPrint implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 模板编码（fm_76130_standard_ALJCJZAF2LL8） **/
    private String templateCode;
    /** 业务数据 **/
    private Document[] documents;
    /** 版本号：固定2.0 **/
    private String version = "2.0";
    /** 生成面单文件格式 **/
    private String fileType = "pdf";
    /** 是否同步（true: 同步,false: 异步） **/
    private boolean sync = true;
    /** 扩展字段 **/
    private Extend extJson;

}
