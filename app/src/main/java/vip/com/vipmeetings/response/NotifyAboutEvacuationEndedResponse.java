package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutEvacuationEndedResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutEvacuationEndedResponse {

    @Element(name = "NotifyAboutEvacuationEndedResult", required = false)
    private NotifyAboutEvacuationEndedResult notifyAboutEvacuationEndedResult;

    public NotifyAboutEvacuationEndedResult getNotifyAboutEvacuationEndedResult() {
        return notifyAboutEvacuationEndedResult;
    }

    public void setNotifyAboutEvacuationEndedResult(NotifyAboutEvacuationEndedResult notifyAboutEvacuationEndedResult) {
        this.notifyAboutEvacuationEndedResult = notifyAboutEvacuationEndedResult;
    }
}
