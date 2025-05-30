package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetMeetingsResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetMeetingsResponse {


    @Element(name = "GetMeetingsResult", required = false)
    private GetMeetingsResult getResponsibleDetailsResult;

    public GetMeetingsResult getGetResponsibleDetailsResult() {
        return getResponsibleDetailsResult;
    }

    public void setGetResponsibleDetailsResult(GetMeetingsResult getResponsibleDetailsResult) {
        this.getResponsibleDetailsResult = getResponsibleDetailsResult;
    }
}
