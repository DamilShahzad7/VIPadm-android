package vip.com.vipmeetings.body;

import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.NotifyAboutEvacuationEndedResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutEvacuationEndedResponseBody {

    @Element(name = "Fault", required = false)
    private SoapFault fault;

    @Element(name = "NotifyAboutEvacuationEndedResponse", required = false)
    private NotifyAboutEvacuationEndedResponse notifyAboutEvacuationEndedResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public NotifyAboutEvacuationEndedResponse getNotifyAboutEvacuationEndedResponse() {
        return notifyAboutEvacuationEndedResponse;
    }

    public void setNotifyAboutEvacuationEndedResponse(NotifyAboutEvacuationEndedResponse notifyAboutEvacuationEndedResponse) {
        this.notifyAboutEvacuationEndedResponse = notifyAboutEvacuationEndedResponse;
    }
}
