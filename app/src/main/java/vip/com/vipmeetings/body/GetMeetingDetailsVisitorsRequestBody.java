package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetFormerVisitors;
import vip.com.vipmeetings.request.GetMeetingDetailsVisitors;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMeetingDetailsVisitorsRequestBody {

    @Element(name = "GetMeetingVisitors", required = false)
    private GetMeetingDetailsVisitors getMeetingDetailsVisitors;


    public GetMeetingDetailsVisitors getGetMeetingDetailsVisitors() {
        return getMeetingDetailsVisitors;
    }

    public void setGetMeetingDetailsVisitors(GetMeetingDetailsVisitors getMeetingDetailsVisitors) {
        this.getMeetingDetailsVisitors = getMeetingDetailsVisitors;
    }


}
