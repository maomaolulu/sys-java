package may.yuntian.external.province.server;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 08:50
 */
@WebService(targetNamespace = "http://server.zhejian.com/", name = "MainServer")
@XmlSeeAlso({ObjectFactory.class})
public interface MainServer {

    @WebMethod
    @RequestWrapper(localName = "transport", targetNamespace = "http://server.zhejian.com/", className = "com.zhejiang.server.Transport")
    @ResponseWrapper(localName = "transportResponse", targetNamespace = "http://server.zhejian.com/", className = "com.zhejiang.server.TransportResponse")
    @WebResult(name = "return", targetNamespace = "")
    public String transport(
            @WebParam(name = "arg0", targetNamespace = "")
                    String arg0
    );
}
