package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.SetClearTimeToEvacuationsResponse;
import vip.com.vipmeetings.response.UpdateClearTimeToEvacuationResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateClearTimeResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "UpdateClearTimeToEvacuationResponse", required = false)
    private UpdateClearTimeToEvacuationResponse updateClearTimeToEvacuationResponse;

    public UpdateClearTimeToEvacuationResponse getUpdateClearTimeToEvacuationResponse() {
        return updateClearTimeToEvacuationResponse;
    }

    public void setUpdateClearTimeToEvacuationResponse(UpdateClearTimeToEvacuationResponse updateClearTimeToEvacuationResponse) {
        this.updateClearTimeToEvacuationResponse = updateClearTimeToEvacuationResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}
