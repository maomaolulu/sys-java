package may.yuntian.external.province.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 08:53
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_transportResponse", propOrder = {
        "_return"
})
public class TransportResponse {

    @XmlElement(name = "return")
    protected String _return;

    /**
     * 获取return属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReturn(String value) {
        this._return = value;
    }

}
