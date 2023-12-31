package may.yuntian.external.province.server;

import may.yuntian.external.province.constant.UrlConstants;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 08:48
 *  * This class was generated by Apache CXF 3.3.13
 *  * 2022-04-24T10:45:31.921+08:00
 *  * Generated source version: 3.3.13
 */
@WebServiceClient(name = "main",
        wsdlLocation = UrlConstants.WSDL_URL,
        targetNamespace = "http://server.zhejian.com/")
public class Main extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://server.zhejian.com/", "main");
    public final static QName MainServerImplPort = new QName("http://server.zhejian.com/", "MainServerImplPort");
    static {
        URL url = null;
        try {
            url = new URL(UrlConstants.WSDL_URL);
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(Main.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", UrlConstants.WSDL_URL);
        }
        WSDL_LOCATION = url;
    }

    public Main(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public Main(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Main() {
        super(WSDL_LOCATION, SERVICE);
    }

    public Main(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public Main(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public Main(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns MainServer
     */
    @WebEndpoint(name = "MainServerImplPort")
    public MainServer getMainServerImplPort() {
        return super.getPort(MainServerImplPort, MainServer.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MainServer
     */
    @WebEndpoint(name = "MainServerImplPort")
    public MainServer getMainServerImplPort(WebServiceFeature... features) {
        return super.getPort(MainServerImplPort, MainServer.class, features);
    }

}
