package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetClientUrlRequest;
import vip.com.vipmeetings.response.GetAuthTokenResult;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetClientUrlRequestBody {



    @Element(name = "GetClientUrl", required = false)
    private GetClientUrlRequest getClientUrl;

    public GetClientUrlRequest getGetClientUrl() {
        return getClientUrl;
    }

    public void setGetClientUrl(GetClientUrlRequest getClientUrl) {
        this.getClientUrl = getClientUrl;
    }
}
