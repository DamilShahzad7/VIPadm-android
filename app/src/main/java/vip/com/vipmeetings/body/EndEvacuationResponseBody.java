package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.EndEvacuationResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EndEvacuationResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "EndEvacuationResponse", required = false)
    private EndEvacuationResponse endEvacuationResponse;

    public EndEvacuationResponse getEndEvacuationResponse() {
        return endEvacuationResponse;
    }

    public void setEndEvacuationResponse(EndEvacuationResponse endEvacuationResponse) {
        this.endEvacuationResponse = endEvacuationResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
