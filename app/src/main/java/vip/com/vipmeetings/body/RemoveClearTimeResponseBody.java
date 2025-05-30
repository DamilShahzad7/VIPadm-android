package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.RemoveClearTimeToEvacuationResponse;
import vip.com.vipmeetings.response.SetClearTimeToEvacuationsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RemoveClearTimeResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "RemoveClearTimeToEvacuationResponse", required = false)
    private RemoveClearTimeToEvacuationResponse removeClearTimeToEvacuationResponse;

    public RemoveClearTimeToEvacuationResponse getRemoveClearTimeToEvacuationResponse() {
        return removeClearTimeToEvacuationResponse;
    }

    public void setRemoveClearTimeToEvacuationResponse(RemoveClearTimeToEvacuationResponse removeClearTimeToEvacuationResponse) {
        this.removeClearTimeToEvacuationResponse = removeClearTimeToEvacuationResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
