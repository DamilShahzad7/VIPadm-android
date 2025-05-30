package vip.com.vipmeetings.envelope;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.GetExcelDataResponseBody;
import vip.com.vipmeetings.body.GetFormersResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SoapGetExcelDataResponseEnvelope {


    @Element(name = "Body", required = false)
    private GetExcelDataResponseBody body;

    public GetExcelDataResponseBody getBody() {
        return body;
    }

    public void setBody(GetExcelDataResponseBody body) {
        this.body = body;
    }
}
