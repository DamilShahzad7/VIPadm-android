package vip.com.vipmeetings.body;

import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.NotifyAboutResponsibleAvailabilityResponse;
import vip.com.vipmeetings.response.RemoveClearTimeToEvacuationResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutResponsibleAvailabilityResponseBody {

    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "NotifyAboutResponsibleAvailabilityResponse", required = false)
    private NotifyAboutResponsibleAvailabilityResponse notifyAboutResponsibleAvailabilityResponse;


    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public NotifyAboutResponsibleAvailabilityResponse getNotifyAboutResponsibleAvailabilityResponse() {
        return notifyAboutResponsibleAvailabilityResponse;
    }

    public void setNotifyAboutResponsibleAvailabilityResponse(NotifyAboutResponsibleAvailabilityResponse notifyAboutResponsibleAvailabilityResponse) {
        this.notifyAboutResponsibleAvailabilityResponse = notifyAboutResponsibleAvailabilityResponse;
    }
}
