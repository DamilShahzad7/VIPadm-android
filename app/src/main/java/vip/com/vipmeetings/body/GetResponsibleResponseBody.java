package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.GetEvacuationResponsiblesResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsibleResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetEvacuationResponsiblesResponse", required = false)
    private GetEvacuationResponsiblesResponse getEvacuationResponsiblesResponse;

    public GetEvacuationResponsiblesResponse getGetEvacuationResponsiblesResponse() {
        return getEvacuationResponsiblesResponse;
    }

    public void setGetEvacuationResponsiblesResponse(GetEvacuationResponsiblesResponse getEvacuationResponsiblesResponse) {
        this.getEvacuationResponsiblesResponse = getEvacuationResponsiblesResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
