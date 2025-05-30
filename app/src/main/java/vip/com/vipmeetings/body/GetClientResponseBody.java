package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetClientResponse;
import vip.com.vipmeetings.response.GetClientUrlResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetClientResponseBody {

    @Element(name = "GetClientResponse", required = false)
    private GetClientResponse getClientResponse;

    public GetClientResponse getGetClientResponse() {
        return getClientResponse;
    }

    public void setGetClientResponse(GetClientResponse getClientResponse) {
        this.getClientResponse = getClientResponse;
    }
}
