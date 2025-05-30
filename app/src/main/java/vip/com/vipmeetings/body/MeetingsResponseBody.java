package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetMeetingsResponse;
import vip.com.vipmeetings.response.GetResponsibleDetailsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class MeetingsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetMeetingsResponse", required = false)
    private GetMeetingsResponse getMeetingsResponse;

    public GetMeetingsResponse getGetMeetingsResponse() {
        return getMeetingsResponse;
    }

    public void setGetMeetingsResponse(GetMeetingsResponse getMeetingsResponse) {
        this.getMeetingsResponse = getMeetingsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
