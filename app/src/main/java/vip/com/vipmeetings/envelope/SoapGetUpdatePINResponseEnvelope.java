package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetEvacuationInformationResponseBody;
import vip.com.vipmeetings.body.GetUpdatePINResponseBody;


@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetUpdatePINResponseEnvelope {


    public GetUpdatePINResponseBody getBody() {
        return body;
    }

    public void setBody(GetUpdatePINResponseBody body) {
        this.body = body;
    }

    @Element(name = "Body", required = false)
    private GetUpdatePINResponseBody body;


}
