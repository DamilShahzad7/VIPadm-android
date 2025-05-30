package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetAppointmentsResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetAppointmentResponse {


    @Element(name = "GetAppointmentsResult", required = false)
    private GetAppointmentsResult getResponsibleDetailsResult;

    public GetAppointmentsResult getGetResponsibleDetailsResult() {
        return getResponsibleDetailsResult;
    }

    public void setGetResponsibleDetailsResult(GetAppointmentsResult getResponsibleDetailsResult) {
        this.getResponsibleDetailsResult = getResponsibleDetailsResult;
    }
}
