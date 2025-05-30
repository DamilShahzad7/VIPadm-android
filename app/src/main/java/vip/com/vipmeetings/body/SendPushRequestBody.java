package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetMessages;
import vip.com.vipmeetings.request.SendPushMessage;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendPushRequestBody {


    @Element(name = "SendPushMessage", required = false)
    private SendPushMessage sendPushMessage;

    public SendPushMessage getSendPushMessage() {
        return sendPushMessage;
    }

    public void setSendPushMessage(SendPushMessage sendPushMessage) {
        this.sendPushMessage = sendPushMessage;
    }
}
