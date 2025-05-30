package vip.com.vipmeetings.envelope;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetClientResponseBody;
import vip.com.vipmeetings.body.GetContectLeaveRequestBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetClientResponseEnvelope {


    @Element(name = "Body", required = false)
    private GetClientResponseBody body;

    public GetClientResponseBody getBody() {
        return body;
    }

    public void setBody(GetClientResponseBody body) {
        this.body = body;
    }
}
