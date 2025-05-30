package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.NotifyAboutEvacuationEnabled;
import vip.com.vipmeetings.request.SendPushMessage;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SoapNotifyAboutEvacuationEnabledRequestBody {



    @Element(name = "NotifyAboutEvacuationEnabled", required = false)
    private NotifyAboutEvacuationEnabled notifyAboutEvacuationEnabled;

    public NotifyAboutEvacuationEnabled getNotifyAboutEvacuationEnabled() {
        return notifyAboutEvacuationEnabled;
    }

    public void setNotifyAboutEvacuationEnabled(NotifyAboutEvacuationEnabled notifyAboutEvacuationEnabled) {
        this.notifyAboutEvacuationEnabled = notifyAboutEvacuationEnabled;
    }
}
