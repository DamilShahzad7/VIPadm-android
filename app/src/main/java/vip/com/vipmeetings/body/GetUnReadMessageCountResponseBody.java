package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.StartEvacuation;
import vip.com.vipmeetings.response.GetUnReadMessageCountResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetUnReadMessageCountResponseBody {


    @Element(name = "GetUnReadMessageCountResponse", required = false)
    private GetUnReadMessageCountResponse getUnReadMessageCountResponse;

    public GetUnReadMessageCountResponse getGetUnReadMessageCountResponse() {
        return getUnReadMessageCountResponse;
    }

    public void setGetUnReadMessageCountResponse(GetUnReadMessageCountResponse getUnReadMessageCountResponse) {
        this.getUnReadMessageCountResponse = getUnReadMessageCountResponse;
    }
}
