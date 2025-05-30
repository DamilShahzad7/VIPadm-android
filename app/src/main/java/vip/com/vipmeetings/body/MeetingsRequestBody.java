package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetMeetingsRequest;
import vip.com.vipmeetings.response.GetMeetingsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class MeetingsRequestBody {


    @Element(name = "GetMeetings", required = false)
    private GetMeetingsRequest getMeetings;

    public GetMeetingsRequest getGetMeetings() {
        return getMeetings;
    }

    public void setGetMeetings(GetMeetingsRequest getMeetings) {
        this.getMeetings = getMeetings;
    }
}
