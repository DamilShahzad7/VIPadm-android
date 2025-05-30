package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutEvacuationEnabledResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutEvacuationEnabledResponse {


    @Element(name = "NotifyAboutEvacuationEnabledResult", required = false)
    private NotifyAboutEvacuationEnabledResult notifyAboutEvacuationEnabledResult;

    public NotifyAboutEvacuationEnabledResult getNotifyAboutEvacuationEnabledResult() {
        return notifyAboutEvacuationEnabledResult;
    }

    public void setNotifyAboutEvacuationEnabledResult(NotifyAboutEvacuationEnabledResult notifyAboutEvacuationEnabledResult) {
        this.notifyAboutEvacuationEnabledResult = notifyAboutEvacuationEnabledResult;
    }
}
