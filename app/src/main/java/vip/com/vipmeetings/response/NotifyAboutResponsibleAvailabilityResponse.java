package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutResponsibleAvailabilityResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutResponsibleAvailabilityResponse {

    @Element(name = "NotifyAboutResponsibleAvailabilityResult", required = false)
    private NotifyAboutResponsibleAvailabilityResult notifyAboutResponsibleAvailabilityResult;

    public NotifyAboutResponsibleAvailabilityResult getNotifyAboutResponsibleAvailabilityResult() {
        return notifyAboutResponsibleAvailabilityResult;
    }

    public void setNotifyAboutResponsibleAvailabilityResult(NotifyAboutResponsibleAvailabilityResult notifyAboutResponsibleAvailabilityResult) {
        this.notifyAboutResponsibleAvailabilityResult = notifyAboutResponsibleAvailabilityResult;
    }
}
