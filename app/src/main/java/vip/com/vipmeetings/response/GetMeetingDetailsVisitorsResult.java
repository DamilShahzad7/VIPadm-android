package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.MeetingDetailsVisitors;
import vip.com.vipmeetings.models.Visitors;

@Root(name = "GetMeetingVisitorsResult", strict = false)
public class GetMeetingDetailsVisitorsResult {

    @Element(name = "VISITORS", required = false)
    private MeetingDetailsVisitors meetingDetailsVisitors;

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;

    public MeetingDetailsVisitors getMeetingDetailsVisitors() {
        return meetingDetailsVisitors;
    }

    public void setMeetingDetailsVisitors(MeetingDetailsVisitors meetingDetailsVisitors) {
        this.meetingDetailsVisitors = meetingDetailsVisitors;
    }
    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }


    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }
}
