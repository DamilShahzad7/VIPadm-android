package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetPlaceIDRequestBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetPlaceIDRequestEnvelope {

    @Element(name = "soap12:Body", required = false)
    private GetPlaceIDRequestBody body;

    public GetPlaceIDRequestBody getBody() {
        return body;
    }

    public void setBody(GetPlaceIDRequestBody body) {
        this.body = body;
    }
}
