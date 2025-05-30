package vip.com.vipmeetings.envelope;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetAuthTokenRequestBody;

/**
 * Created by Srinath on 26/03/18.
 */

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapAuthTokenRequestEnvelope {


    @Element(name = "soap12:Body", required = false)
    private GetAuthTokenRequestBody body;


    public GetAuthTokenRequestBody getBody() {
        return body;
    }

    public void setBody(GetAuthTokenRequestBody body) {
        this.body = body;
    }
}
