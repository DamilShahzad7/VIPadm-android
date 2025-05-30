package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.NotifyAboutEvacuationEnabledResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class NotifyAboutAreaClearenceResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;

    @Element(name = "NotifyAboutAreaClearenceResponse", required = false)
    private NotifyAboutAreaClearenceResponse notifyAboutAreaClearenceResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public NotifyAboutAreaClearenceResponse getNotifyAboutAreaClearenceResponse() {
        return notifyAboutAreaClearenceResponse;
    }

    public void setNotifyAboutAreaClearenceResponse(NotifyAboutAreaClearenceResponse notifyAboutAreaClearenceResponse) {
        this.notifyAboutAreaClearenceResponse = notifyAboutAreaClearenceResponse;
    }
}
