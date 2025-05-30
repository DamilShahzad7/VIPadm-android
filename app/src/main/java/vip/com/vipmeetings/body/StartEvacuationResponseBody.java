package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.StartEvacuationResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class StartEvacuationResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "StartEvacuationResponse", required = false)
    private StartEvacuationResponse startEvacuationResponse;

    public StartEvacuationResponse getStartEvacuationResponse() {
        return startEvacuationResponse;
    }

    public void setStartEvacuationResponse(StartEvacuationResponse startEvacuationResponse) {
        this.startEvacuationResponse = startEvacuationResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
