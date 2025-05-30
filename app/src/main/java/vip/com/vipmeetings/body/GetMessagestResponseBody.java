package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetMessagesResponse;
import vip.com.vipmeetings.response.GetUnReadMessageCountResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMessagestResponseBody {


    @Element(name = "GetMessagesResponse", required = false)
    private GetMessagesResponse getMessagesResponse;

    public GetMessagesResponse getGetMessagesResponse() {
        return getMessagesResponse;
    }

    public void setGetMessagesResponse(GetMessagesResponse getMessagesResponse) {
        this.getMessagesResponse = getMessagesResponse;
    }
}
