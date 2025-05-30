package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.NotifyAboutAreaClearence;
import vip.com.vipmeetings.request.SendPushMessage;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SoapNotifyAboutAreaClearenceRequestBody {


    @Element(name = "NotifyAboutAreaClearence", required = false)
    private NotifyAboutAreaClearence notifyAboutAreaClearence;

    public NotifyAboutAreaClearence getNotifyAboutAreaClearence() {
        return notifyAboutAreaClearence;
    }

    public void setNotifyAboutAreaClearence(NotifyAboutAreaClearence notifyAboutAreaClearence) {
        this.notifyAboutAreaClearence = notifyAboutAreaClearence;
    }
}
