package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddDeviceTokenResponse;
import vip.com.vipmeetings.response.GetFormerVisitorsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetFormersResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetFormerVisitorsResponse", required = false)
    private GetFormerVisitorsResponse getFormerVisitorsResponse;

    public GetFormerVisitorsResponse getGetFormerVisitorsResponse() {
        return getFormerVisitorsResponse;
    }

    public void setGetFormerVisitorsResponse(GetFormerVisitorsResponse getFormerVisitorsResponse) {
        this.getFormerVisitorsResponse = getFormerVisitorsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
