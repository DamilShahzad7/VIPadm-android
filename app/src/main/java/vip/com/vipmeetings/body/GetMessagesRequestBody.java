package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetMessages;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMessagesRequestBody {


    @Element(name = "GetMessages", required = false)
    private GetMessages getMessages;

    public GetMessages getGetMessages() {
        return getMessages;
    }

    public void setGetMessages(GetMessages getMessages) {
        this.getMessages = getMessages;
    }
}
