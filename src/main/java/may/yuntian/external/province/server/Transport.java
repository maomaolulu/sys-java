package may.yuntian.external.province.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 08:53
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_transport", propOrder = {
        "arg0"
})
public class Transport {

    protected String arg0;

    /**
     * 获取arg0属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getArg0() {
        return arg0;
    }

    /**
     * 设置arg0属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setArg0(String value) {
        this.arg0 = value;
    }

}
