package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetClientUrlResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetClientUrlResponseBody {


    @Element(name = "GetClientUrlResponse", required = false)
    private GetClientUrlResponse getClientUrl;

    public GetClientUrlResponse getGetClientUrl() {
        return getClientUrl;
    }

    public void setGetClientUrl(GetClientUrlResponse getClientUrl) {
        this.getClientUrl = getClientUrl;
    }
}
