package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.EndEvacuationResponse;
import vip.com.vipmeetings.response.SetClearTimeToEvacuationsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class ClearTimeResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "SetClearTimeToEvacuationsResponse", required = false)
    private SetClearTimeToEvacuationsResponse setClearTimeToEvacuationsResponse;

    public SetClearTimeToEvacuationsResponse getSetClearTimeToEvacuationsResponse() {
        return setClearTimeToEvacuationsResponse;
    }

    public void setSetClearTimeToEvacuationsResponse(SetClearTimeToEvacuationsResponse setClearTimeToEvacuationsResponse) {
        this.setClearTimeToEvacuationsResponse = setClearTimeToEvacuationsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
