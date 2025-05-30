package vip.com.vipmeetings.envelope;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.SoapNotifyAboutEvacuationEnabledRequestBody;
import vip.com.vipmeetings.body.SoapNotifyAboutEvacuationEndedBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapNotifyAboutEvacuationEndedRequest {

    @Element(name = "soap12:Body", required = false)
    private SoapNotifyAboutEvacuationEndedBody body;

    public SoapNotifyAboutEvacuationEndedBody getBody() {
        return body;
    }

    public void setBody(SoapNotifyAboutEvacuationEndedBody body) {
        this.body = body;
    }
}
