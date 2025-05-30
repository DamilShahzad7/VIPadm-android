package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetFormerVisitorsResponse;
import vip.com.vipmeetings.response.GetMeetingDetailsVisitorsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetMeetingDetailsVisitorsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;

    @Element(name = "GetMeetingVisitorsResponse", required = false)
    private GetMeetingDetailsVisitorsResponse getMeetingDetailsVisitorsResponse;

    public GetMeetingDetailsVisitorsResponse getGetMeetingDetailsVisitorsResponse() {
        return getMeetingDetailsVisitorsResponse;
    }

    public void setGetMeetingDetailsVisitorsResponse(GetMeetingDetailsVisitorsResponse getMeetingDetailsVisitorsResponse) {
        this.getMeetingDetailsVisitorsResponse = getMeetingDetailsVisitorsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
