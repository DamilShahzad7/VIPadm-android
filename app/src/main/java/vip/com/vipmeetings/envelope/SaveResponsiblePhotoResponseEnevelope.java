package vip.com.vipmeetings.envelope;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.body.AddContactLeaveResponseBody;
import vip.com.vipmeetings.body.SaveResponsiblePhotoResponseBody;

@Root(name = "soap12:Envelope", strict = false)
@NamespaceList({

        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap12", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class SaveResponsiblePhotoResponseEnevelope {

    @Element(name = "soap12:Body", required = false)
    private SaveResponsiblePhotoResponseBody body;

    public SaveResponsiblePhotoResponseBody getBody() {
        return body;
    }

    public void setBody(SaveResponsiblePhotoResponseBody body) {
        this.body = body;
    }
}
