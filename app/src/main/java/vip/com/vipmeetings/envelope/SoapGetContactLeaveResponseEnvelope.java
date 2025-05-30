package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.AddContactLeaveResponseBody;
import vip.com.vipmeetings.body.GetContactLeaveResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetContactLeaveResponseEnvelope {

    @Element(name = "Body", required = false)
    private GetContactLeaveResponseBody body;

    public GetContactLeaveResponseBody getBody() {
        return body;
    }

    public void setBody(GetContactLeaveResponseBody body) {
        this.body = body;
    }
}
