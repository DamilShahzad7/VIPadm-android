package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetContactsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetContactsResponse {


    @Element(name = "GetContactsResult", required = false)
    private GetContactsResult getContactsResult;

    public GetContactsResult getGetContactsResult() {
        return getContactsResult;
    }

    public void setGetContactsResult(GetContactsResult getContactsResult) {
        this.getContactsResult = getContactsResult;
    }
}
