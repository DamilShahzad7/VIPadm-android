package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "SendEvacuationPushResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendEvacuationPushResponse {


    @Element(name = "SendEvacuationPushResult", required = false)
    private String sendEvacuationPush;

    public String getSendEvacuationPush() {
        return sendEvacuationPush;
    }

    public void setSendEvacuationPush(String sendEvacuationPush) {
        this.sendEvacuationPush = sendEvacuationPush;
    }
}
