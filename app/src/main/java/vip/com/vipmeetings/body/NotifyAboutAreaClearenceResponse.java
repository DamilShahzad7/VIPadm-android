package vip.com.vipmeetings.body;

import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.response.GetMeetingsResponse;
import vip.com.vipmeetings.response.NotifyAboutAreaClearenceResult;

@Root(name = "NotifyAboutAreaClearenceResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutAreaClearenceResponse {


    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "NotifyAboutAreaClearenceResult", required = false)
    private NotifyAboutAreaClearenceResult notifyAboutAreaClearenceResult;

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public NotifyAboutAreaClearenceResult getNotifyAboutAreaClearenceResult() {
        return notifyAboutAreaClearenceResult;
    }

    public void setNotifyAboutAreaClearenceResult(NotifyAboutAreaClearenceResult notifyAboutAreaClearenceResult) {
        this.notifyAboutAreaClearenceResult = notifyAboutAreaClearenceResult;
    }
}
