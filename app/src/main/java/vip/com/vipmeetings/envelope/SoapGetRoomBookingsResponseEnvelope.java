package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetMessagestResponseBody;
import vip.com.vipmeetings.body.GetRoomBookingsResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetRoomBookingsResponseEnvelope {

    @Element(name = "Body", required = false)
    private GetRoomBookingsResponseBody body;

    public GetRoomBookingsResponseBody getBody() {
        return body;
    }

    public void setBody(GetRoomBookingsResponseBody body) {
        this.body = body;
    }
}
