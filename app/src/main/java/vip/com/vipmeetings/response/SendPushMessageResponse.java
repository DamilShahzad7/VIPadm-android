package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "SendPushMessageResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendPushMessageResponse {


    @Element(name = "SendPushMessageResult", required = false)
    private String sendPushMessage;

    public String getSendPushMessage() {
        return sendPushMessage;
    }

    public void setSendPushMessage(String sendPushMessage) {
        this.sendPushMessage = sendPushMessage;
    }
}
