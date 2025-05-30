package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetResponsiblePhotoUrl;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsiblePhotoUrlRequestBody {


    @Element(name = "GetResponsiblePhotoUrl", required = false)
    private GetResponsiblePhotoUrl getResponsiblePhotoUrl;

    public GetResponsiblePhotoUrl getGetResponsiblePhotoUrl() {
        return getResponsiblePhotoUrl;
    }

    public void setGetResponsiblePhotoUrl(GetResponsiblePhotoUrl getResponsiblePhotoUrl) {
        this.getResponsiblePhotoUrl = getResponsiblePhotoUrl;
    }
}
