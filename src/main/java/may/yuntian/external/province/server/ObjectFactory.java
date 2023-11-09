package may.yuntian.external.province.server;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 08:51
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Transport_QNAME = new QName("http://server.zhejian.com/", "transport");
    private final static QName _TransportResponse_QNAME = new QName("http://server.zhejian.com/", "transportResponse");

    /**
     * Create a newCommission ObjectFactory that can be used to create newCommission instances of schema derived classes for package: com.zhejian.server
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Transport }
     *
     */
    public Transport createTransport() {
        return new Transport();
    }

    /**
     * Create an instance of {@link TransportResponse }
     *
     */
    public TransportResponse createTransportResponse() {
        return new TransportResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transport }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the newCommission instance of {@link JAXBElement }{@code <}{@link Transport }{@code >}
     */
    @XmlElementDecl(namespace = "http://server.zhejian.com/", name = "transport")
    public JAXBElement<Transport> createTransport(Transport value) {
        return new JAXBElement<Transport>(_Transport_QNAME, Transport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransportResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the newCommission instance of {@link JAXBElement }{@code <}{@link TransportResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://server.zhejian.com/", name = "transportResponse")
    public JAXBElement<TransportResponse> createTransportResponse(TransportResponse value) {
        return new JAXBElement<TransportResponse>(_TransportResponse_QNAME, TransportResponse.class, null, value);
    }

}
