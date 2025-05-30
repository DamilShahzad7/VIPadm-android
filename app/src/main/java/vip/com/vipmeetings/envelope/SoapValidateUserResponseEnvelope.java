package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.CitiesResponseBody;
import vip.com.vipmeetings.body.ValidateUserResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapValidateUserResponseEnvelope {

    @Element(name = "Body", required = false)
    private ValidateUserResponseBody body;

    public ValidateUserResponseBody getBody() {
        return body;
    }

    public void setBody(ValidateUserResponseBody body) {
        this.body = body;
    }
}
