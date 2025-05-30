package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetUnReadMessageCountResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetUnReadMessageCountResponse {


    @Element(name = "GetUnReadMessageCountResult", required = false)
    private String unreadResult;

    public String getUnreadResult() {
        return unreadResult;
    }

    public void setUnreadResult(String unreadResult) {
        this.unreadResult = unreadResult;
    }
}
