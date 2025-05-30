package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetAppointmentResponse;
import vip.com.vipmeetings.response.GetMeetingsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AppointmentsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetAppointmentsResponse", required = false)
    private GetAppointmentResponse getAppointmentResponse;

    public GetAppointmentResponse getAppointmentResponse() {
        return getAppointmentResponse;
    }

    public void setGetMeetingsResponse(GetAppointmentResponse getAppointmentResponse) {
        this.getAppointmentResponse = getAppointmentResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
