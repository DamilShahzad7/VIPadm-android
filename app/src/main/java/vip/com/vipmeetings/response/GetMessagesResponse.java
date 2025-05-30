package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetMessagesResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMessagesResponse {


    @Element(name = "GetMessagesResult", required = false)
    private GetMessagesResult getMessagesResult;

    public GetMessagesResult getGetMessagesResult() {
        return getMessagesResult;
    }

    public void setGetMessagesResult(GetMessagesResult getMessagesResult) {
        this.getMessagesResult = getMessagesResult;
    }
}
