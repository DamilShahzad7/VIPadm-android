package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetEvacuationResponsiblesResponse;
import vip.com.vipmeetings.response.GetEvacuationsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetEvacuationsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetEvacuationsResponse", required = false)
    private GetEvacuationsResponse getEvacuationsResponse;

    public GetEvacuationsResponse getGetEvacuationsResponse() {
        return getEvacuationsResponse;
    }

    public void setGetEvacuationsResponse(GetEvacuationsResponse getEvacuationsResponse) {
        this.getEvacuationsResponse = getEvacuationsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
