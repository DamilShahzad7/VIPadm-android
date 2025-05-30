package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.NotifyAboutEvacuationEnded;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SoapNotifyAboutEvacuationEndedBody {


    @Element(name = "NotifyAboutEvacuationEnded", required = false)
    private NotifyAboutEvacuationEnded notifyAboutEvacuationEnded;

    public NotifyAboutEvacuationEnded getNotifyAboutEvacuationEnded() {
        return notifyAboutEvacuationEnded;
    }

    public void setNotifyAboutEvacuationEnded(NotifyAboutEvacuationEnded notifyAboutEvacuationEnded) {
        this.notifyAboutEvacuationEnded = notifyAboutEvacuationEnded;
    }
}
