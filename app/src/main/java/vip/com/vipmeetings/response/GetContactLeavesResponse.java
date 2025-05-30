package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetContactLeavesResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetContactLeavesResponse {


    @Element(name = "GetContactLeavesResult", required = false)
    private GetContactLeavesResult getContactLeavesResult;

    public GetContactLeavesResult getGetContactLeavesResult() {
        return getContactLeavesResult;
    }

    public void setGetContactLeavesResult(GetContactLeavesResult getContactLeavesResult) {
        this.getContactLeavesResult = getContactLeavesResult;
    }
}
