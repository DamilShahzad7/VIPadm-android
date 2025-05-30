package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddDeviceTokenResponse;
import vip.com.vipmeetings.response.GetEvacuationStatusResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationStatusResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;

    @Element(name = "GetEvacuationStatusResponse", required = false)
    private GetEvacuationStatusResponse getEvacuationStatusResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public GetEvacuationStatusResponse getGetEvacuationStatusResponse() {
        return getEvacuationStatusResponse;
    }

    public void setGetEvacuationStatusResponse(GetEvacuationStatusResponse getEvacuationStatusResponse) {
        this.getEvacuationStatusResponse = getEvacuationStatusResponse;
    }
}
