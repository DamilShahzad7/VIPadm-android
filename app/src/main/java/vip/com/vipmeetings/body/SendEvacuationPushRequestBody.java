package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetMessages;
import vip.com.vipmeetings.request.SendEvacuationPush;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendEvacuationPushRequestBody {


    @Element(name = "SendEvacuationPush", required = false)
    private SendEvacuationPush sendEvacuationPush;

    public SendEvacuationPush getSendEvacuationPush() {
        return sendEvacuationPush;
    }

    public void setSendEvacuationPush(SendEvacuationPush sendEvacuationPush) {
        this.sendEvacuationPush = sendEvacuationPush;
    }
}
