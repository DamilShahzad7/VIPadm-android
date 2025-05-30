package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetUnReadMessageCountResponseBody;
import vip.com.vipmeetings.body.UpdateResponsibleAvailabilityResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetUnreadMessageCountResponseEnvelope {

    @Element(name = "Body", required = false)
    private GetUnReadMessageCountResponseBody body;

    public GetUnReadMessageCountResponseBody getBody() {
        return body;
    }

    public void setBody(GetUnReadMessageCountResponseBody body) {
        this.body = body;
    }
}
