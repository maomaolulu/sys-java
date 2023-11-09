package may.yuntian.external.express.sf.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 云打印面单2.0-业务数据documents
 * @author: liyongqiang
 * @create: 2023-06-07 15:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Document implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 主运单号 **/
    private String masterWaybillNo;
    /** 打印次数 **/
//    private String printNum;
    /** 是否打印LOGO水印 **/
    private String isPrintLogo = "true";
    /** 面单上打印的系统来源（固定传值：scp） **/
    private String systemSource = "scp";
    /** 简体中文 **/
    private String languageCN = "true";
    /** 自定义区域备注 **/
    private String remark;

    public Document(String masterWaybillNo, String remark) {
        this.masterWaybillNo = masterWaybillNo;
        this.remark = remark;
    }
}
