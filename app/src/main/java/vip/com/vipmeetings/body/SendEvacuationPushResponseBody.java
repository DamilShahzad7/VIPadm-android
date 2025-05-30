package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetMessagesResponse;
import vip.com.vipmeetings.response.SendEvacuationPushResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SendEvacuationPushResponseBody {


    @Element(name = "SendEvacuationPushResponse", required = false)
    private SendEvacuationPushResponse sendEvacuationPushResponse;

    public SendEvacuationPushResponse getSendEvacuationPushResponse() {
        return sendEvacuationPushResponse;
    }

    public void setSendEvacuationPushResponse(SendEvacuationPushResponse sendEvacuationPushResponse) {
        this.sendEvacuationPushResponse = sendEvacuationPushResponse;
    }
}
