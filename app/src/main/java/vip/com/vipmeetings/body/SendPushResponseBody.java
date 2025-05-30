package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetMessagesResponse;
import vip.com.vipmeetings.response.SendPushMessageResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendPushResponseBody {


    @Element(name = "SendPushMessageResponse", required = false)
    private SendPushMessageResponse sendPushMessageResponse;

    public SendPushMessageResponse getSendPushMessageResponse() {
        return sendPushMessageResponse;
    }

    public void setSendPushMessageResponse(SendPushMessageResponse sendPushMessageResponse) {
        this.sendPushMessageResponse = sendPushMessageResponse;
    }
}
