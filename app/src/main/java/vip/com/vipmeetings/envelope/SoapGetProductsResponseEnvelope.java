package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.DeleteMeetingResponseBody;
import vip.com.vipmeetings.body.GetProductsResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetProductsResponseEnvelope {

    @Element(name = "Body", required = false)
    private GetProductsResponseBody body;

    public GetProductsResponseBody getBody() {
        return body;
    }

    public void setBody(GetProductsResponseBody body) {
        this.body = body;
    }
}
