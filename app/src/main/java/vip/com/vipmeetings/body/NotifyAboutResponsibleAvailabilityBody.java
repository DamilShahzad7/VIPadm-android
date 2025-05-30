package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.NotifyAboutResponsibleAvailability;
import vip.com.vipmeetings.request.RemoveClearTimeToEvacuation;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutResponsibleAvailabilityBody {


    @Element(name = "NotifyAboutResponsibleAvailability", required = false)
    private NotifyAboutResponsibleAvailability notifyAboutResponsibleAvailability;

    public NotifyAboutResponsibleAvailability getNotifyAboutResponsibleAvailability() {
        return notifyAboutResponsibleAvailability;
    }

    public void setNotifyAboutResponsibleAvailability(NotifyAboutResponsibleAvailability notifyAboutResponsibleAvailability) {
        this.notifyAboutResponsibleAvailability = notifyAboutResponsibleAvailability;
    }
}
