package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetMeetingVisitorsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMeetingDetailsVisitorsResponse {

    @Element(name = "GetMeetingVisitorsResult", required = false)
    private GetMeetingDetailsVisitorsResult getMeetingDetailsVisitorsResult;

    public GetMeetingDetailsVisitorsResult getGetMeetingDetailsVisitorsResult() {
        return getMeetingDetailsVisitorsResult;
    }

    public void setGetMeetingDetailsVisitorsResult(GetMeetingDetailsVisitorsResult getMeetingDetailsVisitorsResult) {
        this.getMeetingDetailsVisitorsResult = getMeetingDetailsVisitorsResult;
    }



}
