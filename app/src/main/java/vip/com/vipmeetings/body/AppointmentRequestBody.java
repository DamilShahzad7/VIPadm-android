package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetAppointmentsRequest;
import vip.com.vipmeetings.request.GetMeetingsRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AppointmentRequestBody {


    public GetAppointmentsRequest getGetAppointments() {
        return getAppointments;
    }

    public void setGetAppointments(GetAppointmentsRequest getAppointments) {
        this.getAppointments = getAppointments;
    }

    @Element(name = "GetAppointments", required = false)
    private GetAppointmentsRequest getAppointments;

}
