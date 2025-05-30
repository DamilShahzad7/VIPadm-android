package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.EmployRequestBody;
import vip.com.vipmeetings.body.EmployResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

      //  @Namespace(reference = "http://tempuri.org/",prefix = "xmlns"),
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")

})
public class SoapEmployResponseEnvelope {

    @Element(name = "Body", required = false)
    private EmployResponseBody body;

    public EmployResponseBody getBody() {
        return body;
    }

    public void setBody(EmployResponseBody body) {
        this.body = body;
    }
}
