package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.EndEvacuation;
import vip.com.vipmeetings.request.GetUnReadMessageCount;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UnreadMessageCountRequestBody {


    @Element(name = "GetUnReadMessageCount", required = false)
    private GetUnReadMessageCount getUnReadMessageCount;

    public GetUnReadMessageCount getGetUnReadMessageCount() {
        return getUnReadMessageCount;
    }

    public void setGetUnReadMessageCount(GetUnReadMessageCount getUnReadMessageCount) {
        this.getUnReadMessageCount = getUnReadMessageCount;
    }
}
